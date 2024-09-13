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
package com.java.sdeven.sparrow.commons.timewheel.scheduler;


import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;
import com.java.sdeven.sparrow.commons.timewheel.model.JobInfoDO;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.VersionType;

/**
 * <p>
 * 版本策略适配
 * </p>
 * @author sdeven
 * @date 2024/2/22 11:39
 * @since 1.0.0
 */
public interface VersionStrategyHandler {

    /**
     * 默认定时任务执行器,未实现将抛出异常
     *
     */
   default void processor(JobInfoDO jobInfoDO){
       throw new TimeWheelJobException("未定义执行器异常");
   }

    /**
     * 支持的定时策略,未实现将抛出异常
     *
     * @return TimeExpressionType
     */
    default VersionType supportType() {
        throw new TimeWheelJobException("未定义执行器版本异常");
    }
}
