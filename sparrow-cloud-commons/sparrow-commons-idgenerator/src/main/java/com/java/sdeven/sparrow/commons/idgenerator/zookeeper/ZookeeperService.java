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

import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * ZookeeperService
 *
 * @Date 2020/2/17 下午5:19
 * @Author  sdeven
 */
public interface ZookeeperService {
    /**
     * 判断节点是否存在
     */
    boolean isExistNode(final String path) ;
    /**
     * 创建节点
     */
    String createNode(CreateMode mode, String path) ;
    /**
     * 创建节点
     */
    String createNode(CreateMode mode, String path, String nodeData) ;
    /**
     * 设置节点数据
     */
    void setNodeData(String path, String nodeData) ;
    /**
     * 获取节点数据
     */
    String getNodeData(String path) ;
    /**
     * 获取节点下数据
     */
    List<String> getNodeChild(String path) ;
    /**
     * 是否递归删除节点
     */
    void deleteNode(String path, Boolean recursive) ;
    /**
     * 获取读写锁
     */
    InterProcessReadWriteLock getReadWriteLock(String path) ;
}
