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
package com.java.sdeven.sparrow.commons.timewheel.scheduler.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * Scheduling time strategies
 *
 * @author tjq
 * @since 2020/3/30
 */
@Getter
@AllArgsConstructor
@ToString
public enum TimeExpressionType {

    CRON(1),
    FIXED_RATE(2),
    FIXED_DELAY(3),
    DAILY_TIME_INTERVAL(4);

    private final int v;

    public static final List<Integer> FREQUENT_TYPES = Collections.unmodifiableList(Lists.newArrayList(FIXED_RATE.v, FIXED_DELAY.v));
    /**
     * 首次计算触发时间时必须计算出一个有效值
     */
    public static final List<Integer> INSPECT_TYPES =  Collections.unmodifiableList(Lists.newArrayList(CRON.v, DAILY_TIME_INTERVAL.v));

    public static TimeExpressionType of(int v) {
        for (TimeExpressionType type : values()) {
            if (type.v == v) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown TimeExpressionType of " + v);
    }
}
