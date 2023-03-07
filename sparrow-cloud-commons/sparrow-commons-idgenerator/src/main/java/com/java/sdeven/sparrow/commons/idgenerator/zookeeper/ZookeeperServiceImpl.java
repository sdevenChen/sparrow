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
package com.java.sdeven.sparrow.commons.idgenerator.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A zookeeper node management implementation {@link CreateMode}
 * @author sdeven
 */
@Slf4j
public class ZookeeperServiceImpl implements ZookeeperService {
    private CuratorFramework zkClient;

    public ZookeeperServiceImpl(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public boolean isExistNode(String path) {
        zkClient.sync() ;
        try {
            return zkClient.checkExists().forPath(path) != null;
        } catch (Exception e) {
            log.error("isExistNode error...", e);
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public String createNode(CreateMode mode, String path) {
        try {
            /* Recursively create the required parent node */
            return zkClient.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
        } catch (Exception e) {
            log.error("createNode error...", e);
            return null;
        }
    }

    @Override
    public String createNode(CreateMode mode, String path, String nodeData) {
        try {
            /* Create nodes and associate data */
            return zkClient.create().creatingParentsIfNeeded().withMode(mode)
                    .forPath(path,nodeData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("createNode error...", e);
            return null;
        }
    }
    @Override
    public void setNodeData(String path, String nodeData) {
        try {
            /** Set node data */
            zkClient.setData().forPath(path, nodeData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("setNodeData error...", e);
            e.printStackTrace();
        }
    }
    @Override
    public String getNodeData(String path) {
        try {
            /** Data reading and transformation */
            zkClient.sync();
            byte[] dataByte = zkClient.getData().forPath(path) ;
            String data = new String(dataByte,StandardCharsets.UTF_8) ;
            if (StringUtils.isNotEmpty(data)){
                return data ;
            }
        }catch (Exception e) {
            log.error("getNodeData error...", e);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<String> getNodeChild(String path) {
        List<String> nodeChildDataList = new ArrayList<>();
        try {
            /** Node down dataList */
            nodeChildDataList = zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("getNodeChild error...", e);
            e.printStackTrace();
        }
        return nodeChildDataList;
    }
    @Override
    public void deleteNode(String path, Boolean recursive) {
        try {
            if(recursive) {
                /* Recursive node deletion */
                zkClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            } else {
                /* Delete a single node */
                zkClient.delete().guaranteed().forPath(path);
            }
        } catch (Exception e) {
            log.error("deleteNode error...", e);
            e.printStackTrace();
        }
    }
    @Override
    public InterProcessReadWriteLock getReadWriteLock(String path) {
        /* Write lock mutex, read-write mutex */
        return new InterProcessReadWriteLock(zkClient, path);
    }
}

