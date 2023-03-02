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
package com.sdeven.sparrow.starter.nodeflow;

/**
 * @Description 节点规则引擎解析支持字段
 * @Author sdeven
 */
public class NodeflowResolveField {
    /**
     * 定义层，一级节点字段，indexs 负责定义输入输出、类型、校验正则
     */
    public static final String KEY_INDEXS = "indexs";
    public static final String KEY_NODES = "nodes";

    /**
     * 执行层，二级节点执行
     */
    public static final String KEY_ID = "id";
    public static final String KEY_NEXT = "next";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_COMPONENT = "component";
    public static final String KEY_ERROR = "error";
    public static final String KEY_OPT = "opt";
    public static final String KEY_TYPE = "type";
}
