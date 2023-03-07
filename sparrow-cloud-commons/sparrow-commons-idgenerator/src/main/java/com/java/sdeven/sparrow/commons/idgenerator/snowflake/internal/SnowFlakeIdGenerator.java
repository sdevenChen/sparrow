/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.java.sdeven.sparrow.commons.idgenerator.snowflake.internal;

import com.java.sdeven.sparrow.commons.idgenerator.zookeeper.ZookeeperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * Customize SnowFlake
 *
 * The structure of SnowFlake is as follows (each part is separated by -):
 * 0 - 00000000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1 bit identification, because the long basic type in Java is signed, the highest bit is the sign bit, positive is 0, negative is 1, so the id is generally positive, the highest bit is 0
 * 41-bit time intercept (milliseconds), note that the 41-bit time intercept is not the time intercept to store the current time, but to store the difference between the time intercept (the current time intercept - start time intercept) to get the value), where the start time intercept, generally our id generator to start using the time specified by our program (the following program IdWorker class startTime property).
 * 41-bit time intercept, you can use 69 years, year T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * 10-bit data machine bits, can be deployed in 1024 nodes, including 5-bit datacenterId and 5-bit workerId 12-bit sequence, assuming that the count in milliseconds: 12-bit count sequence number supports each node every millisecond (the same machine, the same time intercept) to generate 4096 ID sequence number adds up to exactly 64 bits, for a Long type .
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
public class SnowFlakeIdGenerator {
    /** Start End Time Stamp*/
    private final static long twepoch = 1577836800000L;// 2020-01-01
    /** The number of bits occupied by the machine id */
    private final static long workerIdBits = 8L;
    /** The number of bits occupied by the data identifier id */
    private final static long dataCenterIdBits = 2L;
    /** The number of bits the sequence occupies in the id */
    private final static long sequenceBits = 12L;

    /** Machine ID shifted 12 bits to the left */
    private final static long workerIdShift = sequenceBits;
    /** The data identifier id is shifted 20 bits to the left (12+8) */
    private final static long dataCenterIdShift = sequenceBits + workerIdBits;
    // Time cut-off shifted 22 places to the left(2+8+12)
    private final static long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    // Generate the mask for the sequence, here 4095 (0b111111111111=0xfff=4095)
    private final static long sequenceMask = ~(-1L << sequenceBits);

    /** Work Machine ID(0~2^8)*/
    private long workerId;
    /** Data Center ID (0~2^2) */
    private long dataCenterId;


    /** Intra-millisecond sequence (0~4095) */
    private long sequence;
    /** Time cutoff of last ID generation */
    private long lastTimestamp;
    /** zookeeper worker node paths */
    private String zkWorkerNodePath;
    /** Custom business string */
    private String business;

    private ZookeeperService zkService;
    private SnowflakeIdProperties properties;

    /**
     * Initialize constructor
     * @param #business Customize Business String
     * @param #zkService {@link ZookeeperService}
     * @param #properties {@link SnowflakeIdProperties}
     * @author sdeven
     */
    public SnowFlakeIdGenerator(ZookeeperService zkService, SnowflakeIdProperties properties, String business) throws Exception {
        // sanity check for workerId
        if (properties.getDataCenterId() > (~(-1L << dataCenterIdBits))) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", (~(-1L << dataCenterIdBits))));
        }

        this.dataCenterId = properties.getDataCenterId();
        this.sequence = 0;
        this.business = business;

        this.zkService = zkService;
        this.properties = properties;

        this.setWorkerId();
    }

    private void setWorkerId() throws Exception {
        String PATH_PERSISTENT = properties.getZkIdAppPath() + "/" + business + "/PERSISTENT/i";
        String PATH_EPHEMERAL = properties.getZkIdAppPath() + "/" + business + "/EPHEMERAL/i";
        String PATH_EPHEMERAL_SEQUENTIAL = properties.getZkIdAppPath() + "/" + business + "/EPHEMERAL_SEQUENTIAL/i";
        String PATH_LOCK = properties.getZkIdAppPath() + "/" + business + "/LOCK/i";

        boolean success = false;
        int n = 1;
        while (n++ < 1000 && !success) {
            String path = zkService.createNode(CreateMode.EPHEMERAL_SEQUENTIAL, PATH_EPHEMERAL_SEQUENTIAL);
            zkService.deleteNode(path, true);

            long id = Long.parseLong(path.trim().substring(PATH_EPHEMERAL_SEQUENTIAL.length())) % (2^workerIdBits);

            if (!zkService.isExistNode(PATH_EPHEMERAL + id)) {
                InterProcessMutex lock = zkService.getReadWriteLock(PATH_LOCK).writeLock();
                if (lock.acquire(3, TimeUnit.SECONDS)) {
                    if (!zkService.isExistNode(PATH_EPHEMERAL + id)) {
                        zkService.createNode(CreateMode.EPHEMERAL, PATH_EPHEMERAL + id);
                        if (zkService.isExistNode(PATH_PERSISTENT + id)) {
                            this.lastTimestamp = Long.parseLong(zkService.getNodeData(PATH_PERSISTENT + id));
                        }
                        else {
                            this.lastTimestamp = timeGen();
                            zkService.createNode(CreateMode.PERSISTENT, PATH_PERSISTENT + id, String.valueOf(0));
                        }
                        this.zkWorkerNodePath = PATH_PERSISTENT + id;
                        this.workerId = id;
                        success = true;
                    }
                    lock.release();
                }
            }
        }
        if (!success) {
            throw new IllegalArgumentException("Worker ID init failed");
        }
    }


    /**
     * Thread safe to get the next ID
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (dataCenterId << dataCenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * Blocks to the next millisecond until a new timestamp is obtained
     * @param #lastTimestamp Time cutoff of the last generated ID
     * @return Current Timestamp
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen(){
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            timestamp = lastTimestamp + 1;
            zkService.setNodeData(this.zkWorkerNodePath, String.valueOf(timestamp));
        }
        return timestamp;
    }
}
