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
package com.java.sdeven.sparrow.commons.timewheel;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Custom Timer
 *
 * @author sdeven
 * @since 1.0.0
 */
public interface Timer {

    /**
     * Scheduling Timed Tasks
     * @param #task {@link TimerTask}
     * @param #unit {@link TimeUnit}
     */
    TimerFuture schedule(TimerTask task, long delay, TimeUnit unit);

    /**
     * Stopping all scheduling tasks
     * @return Set<TimerTask>
     */
    Set<TimerTask> stop();
}
