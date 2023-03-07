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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rejection Strategies
 *
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
public class RejectedExecutionHandlerFactory {

    private static final AtomicLong COUNTER = new AtomicLong();

    /**
     * Discard a task with one input parameter
     * @param source log name
     * @return A handler for tasks that cannot be executed by ThreadPool
     */
    public static RejectedExecutionHandler newReject(String source) {
        return (r, p) -> {
            log.error("[{}] ThreadPool[{}] overload, the task[{}] will be dropped!", source, p, r);
            log.warn("[{}] Maybe you need to adjust the ThreadPool config!", source);
        };
    }

    /**
     * Calling threads to run
     * @param source log name
     * @return A handler for tasks that cannot be executed by ThreadPool
     */
    public static RejectedExecutionHandler newCallerRun(String source) {
        return (r, p) -> {
            log.warn("[{}] ThreadPool[{}] overload, the task[{}] will run by caller thread!", source, p, r);
            log.warn("[{}] Maybe you need to adjust the ThreadPool config!", source);
            if (!p.isShutdown()) {
                r.run();
            }
        };
    }

    /**
     * Create a new thread and run it
     * @param source log name
     * @return A handler for tasks that cannot be executed by ThreadPool
     */
    public static RejectedExecutionHandler newThreadRun(String source) {
        return (r, p) -> {
            log.warn("[{}] ThreadPool[{}] overload, the task[{}] will run by a new thread!", source, p, r);
            log.warn("[{}] Maybe you need to adjust the ThreadPool config!", source);
            if (!p.isShutdown()) {
                String threadName = source + "-T-" + COUNTER.getAndIncrement();
                log.info("[{}] create new thread[{}] to run job", source, threadName);
                new Thread(r, threadName).start();
            }
        };
    }

}
