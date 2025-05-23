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
package com.java.sdeven.sparrow.commons.timewheel.holder;


import com.java.sdeven.sparrow.commons.timewheel.HashedWheelTimer;

/**
 * Time Wheel Single Example
 *
 * @author sdeven
 * @since 1.0.0
 */
public class HashedWheelTimerHolder {

    /** Non-precision time wheel, one frame every 5S */
    public static final HashedWheelTimer INACCURATE_TIMER = new HashedWheelTimer(5, 16, 0);

    private HashedWheelTimerHolder() {
    }
}
