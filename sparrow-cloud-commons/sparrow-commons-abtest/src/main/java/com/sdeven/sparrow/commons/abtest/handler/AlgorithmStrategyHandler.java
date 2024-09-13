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
package com.sdeven.sparrow.commons.abtest.handler;


import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;

/**
 * @author sdeven
 * @date 2024/6/14 9:39
 * @since 1.0.0
 */
public interface AlgorithmStrategyHandler {

    /**
     * 计算号段
     * @param cid 用户ID
     * @param salt 盐值
     */
    String calculateSegment(String cid, String salt);

    /**
     * 支持的算法策略
     *
     * @return AbExperimentGroupBucketAlgorithm
     */
        AbTestAlgorithmEnum supportType();
}
