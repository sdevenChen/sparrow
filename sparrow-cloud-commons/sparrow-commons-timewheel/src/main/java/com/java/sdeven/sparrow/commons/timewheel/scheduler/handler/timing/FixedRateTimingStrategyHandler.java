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
package com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.timing;

import com.java.sdeven.sparrow.commons.timewheel.common.JobJVMKey;
import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.TimeExpressionType;
import org.springframework.stereotype.Component;


/**
 * @author Echo009
 * @since 2022/3/22
 */
@Component
public class FixedRateTimingStrategyHandler extends AbstractTimingStrategyHandler {

    @Override
    public void validate(String timeExpression) {
        long delay;
        try {
            delay = Long.parseLong(timeExpression);
        } catch (Exception e) {
            throw new TimeWheelJobException("invalid timeExpression!");
        }
        // 默认 120s ，超过这个限制应该使用考虑使用其他类型以减少资源占用
        int maxInterval = Integer.parseInt(System.getProperty(JobJVMKey.FREQUENCY_JOB_MAX_INTERVAL, "120000"));
        if (delay > maxInterval) {
            throw new TimeWheelJobException("the rate must be less than " + maxInterval + "ms");
        }
        if (delay <= 0) {
            throw new TimeWheelJobException("the rate must be greater than 0 ms");
        }
    }

    @Override
    public Long calculateNextTriggerTime(Long preTriggerTime, String timeExpression, Long startTime, Long endTime) {
        long r = startTime != null && startTime > preTriggerTime
                ? startTime : preTriggerTime + Long.parseLong(timeExpression);
        return endTime != null && endTime < r ? null : r;
    }

    @Override
    public TimeExpressionType supportType() {
        return TimeExpressionType.FIXED_RATE;
    }
}
