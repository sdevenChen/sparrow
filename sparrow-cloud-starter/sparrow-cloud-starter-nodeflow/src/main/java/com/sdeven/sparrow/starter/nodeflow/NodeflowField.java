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
 * Script input and output field objects
 * @author sdeven
 */
public class NodeflowField {
    /**
     * index key
     */
    public static final String KEY_IDX = "idx";
    /**
     * index name
     */
    public static final String KEY_NAME = "name";
    /**
     * Embedded Field Types 1:input, 2:output, 3:input&output
     */
    public static final String KEY_MODE = "mode";
    /**
     * Regular expression execution block
     */
    public static final String KEY_REGX = "regx";
    /**
     * Regular expression name
     */
    public static final String KEY_REGX_NAME = "regxName";
}
