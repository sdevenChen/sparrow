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

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * ZookeeperService
 * @author sdeven
 */
public interface ZookeeperService {
    /**
     * Determine if a node exists
     * using checkExists() for {@link CuratorFramework }
     */
    boolean isExistNode(final String path) ;
    /**
     * Create Node
     * using creatingParentsIfNeeded() for {@link CuratorFramework }
     */
    String createNode(CreateMode mode, String path) ;
    /**
     * Create Node
     * @param #mode  {@link CreateMode}
     * @param #path path String
     * @param #nodeData body
     */
    String createNode(CreateMode mode, String path, String nodeData) ;
    /**
     * Set node data
     * using setData() for {@link CuratorFramework }
     *
     */
    void setNodeData(String path, String nodeData) ;
    /**
     * Get node data
     */
    String getNodeData(String path) ;
    /**
     * Get the data of the child nodes under the path
     */
    List<String> getNodeChild(String path) ;
    /**
     * Whether to recursively delete nodes
     */
    void deleteNode(String path, Boolean recursive) ;
    /**
     * Get read/write locks
     */
    InterProcessReadWriteLock getReadWriteLock(String path) ;
}
