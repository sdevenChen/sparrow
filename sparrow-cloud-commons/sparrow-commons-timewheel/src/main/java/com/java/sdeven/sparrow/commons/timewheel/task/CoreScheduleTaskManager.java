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
package com.java.sdeven.sparrow.commons.timewheel.task;

import com.java.sdeven.sparrow.commons.timewheel.scheduler.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sdeven
 * @since 2022/10/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoreScheduleTaskManager implements InitializingBean, DisposableBean {
    private final ScheduleService powerScheduleService;

    private final List<Thread> coreThreadContainer = new ArrayList<>();


    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void afterPropertiesSet() {
        // 定时调度
        coreThreadContainer.add(new Thread(new LoopRunnable("ScheduleCronJob", ScheduleService.SCHEDULE_RATE, () -> powerScheduleService.scheduleNormalJob()), "Thread-ScheduleCronJob"));
        // 数据清理
        coreThreadContainer.forEach(Thread::start);
    }

    @Override
    public void destroy() {
        coreThreadContainer.forEach(Thread::interrupt);
    }


    @RequiredArgsConstructor
    private static class LoopRunnable implements Runnable {

        private final String taskName;

        private final Long runningInterval;

        private final Runnable innerRunnable;

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            log.info("start task : {}.", taskName);
            while (true) {
                try {
                    innerRunnable.run();
                    Thread.sleep(runningInterval);
                } catch (InterruptedException e) {
                    log.warn("[{}] task has been interrupted!", taskName, e);
                    break;
                } catch (Exception e) {
                    log.error("[{}] task failed!", taskName, e);
                }
            }
        }
    }

}
