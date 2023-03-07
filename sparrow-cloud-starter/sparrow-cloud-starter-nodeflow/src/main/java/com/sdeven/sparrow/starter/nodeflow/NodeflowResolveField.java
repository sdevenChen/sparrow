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
 * Support fields for node rule engine parsing
 * @author sdeven
 */
public class NodeflowResolveField {
    /**
     * Definition layer, first level node fields, "indexes"
     * responsible for defining input and output, types, checksum regularity
     */
    public static final String KEY_INDEXS = "indexs";
    public static final String KEY_NODES = "nodes";

    /**
     * Execution level, secondary node execution
     */
    public static final String KEY_ID = "id";
    public static final String KEY_NEXT = "next";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_COMPONENT = "component";
    public static final String KEY_ERROR = "error";
    public static final String KEY_OPT = "opt";
    public static final String KEY_TYPE = "type";
}
