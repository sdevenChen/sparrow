package com.java.sdeven.sparrow.commons.idgenerator.snowflake;

import com.java.sdeven.sparrow.commons.idgenerator.IdGenerator;
import com.java.sdeven.sparrow.commons.idgenerator.snowflake.internal.SnowFlakeIdGenerator;
import com.java.sdeven.sparrow.commons.idgenerator.snowflake.internal.SnowflakeIdProperties;
import com.java.sdeven.sparrow.commons.idgenerator.zookeeper.ZookeeperService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SnowflakeZkIdGenerator
 * @Date 2019/8/11
 * @Author  sdeven
 */
@Slf4j
public class SnowflakeZkIdGenerator implements IdGenerator {
    private ZookeeperService zkService;
    private SnowflakeIdProperties properties;
    private Map<String, SnowFlakeIdGenerator> map;
    private final static String GLOBAL_BUSINESS_NAME = "__GLOBAL_BUSINESS";

    public SnowflakeZkIdGenerator(ZookeeperService zkService, SnowflakeIdProperties properties) {
        this.zkService = zkService;
        this.properties = properties;
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public long nextId(String name) {
        SnowFlakeIdGenerator snowFlakeIDGenerator = map.computeIfAbsent(name, o -> {
            try {
                return new SnowFlakeIdGenerator(zkService, properties, name);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new IllegalArgumentException(String.format("SnowFlakeIDGenerator[%s] init failed", name));
            }
        });
        return snowFlakeIDGenerator.nextId();
    }

    @Override
    public long nextId() {
        return nextId(GLOBAL_BUSINESS_NAME);
    }
}
