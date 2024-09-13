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
package com.sdeven.sparrow.commons.abtest;

import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;
import com.sdeven.sparrow.commons.abtest.constant.ExceptionConstant;
import com.sdeven.sparrow.commons.abtest.handler.AlgorithmStrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author sdeven
 * @date 2024/6/14 9:51
 * @since 1.0.0
 */
@Slf4j
@Service
public class AlgorithmStrategyService {
    private final Map<AbTestAlgorithmEnum, AlgorithmStrategyHandler> strategyContainer;

    public AlgorithmStrategyService(List<AlgorithmStrategyHandler> algorithmStrategyHandlers) {
        strategyContainer = new EnumMap<>(AbTestAlgorithmEnum.class);
        for (AlgorithmStrategyHandler algorithmStrategyHandler : algorithmStrategyHandlers) {
            strategyContainer.put(algorithmStrategyHandler.supportType(), algorithmStrategyHandler);
        }
    }

    /**
     *  校验
     * @param algorithm
     * @param cid
     * @param salt
     */
    public Boolean validate(AbTestAlgorithmEnum algorithm, String cid, String salt ) {
        if (ObjectUtils.isEmpty(algorithm) || StringUtils.isEmpty(cid)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 处理器
     * @param algorithm
     * @param cid
     * @param salt
     */

    public String processor(AbTestAlgorithmEnum algorithm, String cid, String salt ) throws Exception {
        Boolean validate = this.validate(algorithm, cid, salt);
        if (!validate) {
            throw new IllegalArgumentException(ExceptionConstant.PARAM_ILLEGAL);
        }
        //计算号段
        return this.getHandler(algorithm).calculateSegment(cid,salt);
    }

    private AlgorithmStrategyHandler getHandler(AbTestAlgorithmEnum algorithm) {
        AlgorithmStrategyHandler algorithmStrategyHandler = strategyContainer.get(algorithm);
        if (algorithmStrategyHandler == null) {
            throw new IllegalArgumentException(ExceptionConstant.PARAM_ILLEGAL);
        }
        return algorithmStrategyHandler;
    }

}
