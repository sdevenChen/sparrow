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
package com.java.sdeven.sparrow.commons.timewheel.model;

import com.java.sdeven.sparrow.commons.timewheel.utils.serialize.JsonUtils;
import lombok.Data;

/**
 * @author Echo009
 * @since 2022/3/22
 */
@Data
public class LifeCycle {
    public static final LifeCycle EMPTY_LIFE_CYCLE = new LifeCycle();
    private Long start;
    private Long end;

    public static LifeCycle parse(String lifeCycle){
        try {
           return JsonUtils.parseObject(lifeCycle,LifeCycle.class);
        }catch (Exception e){
            // ignore
            return EMPTY_LIFE_CYCLE;
        }
    }

}
