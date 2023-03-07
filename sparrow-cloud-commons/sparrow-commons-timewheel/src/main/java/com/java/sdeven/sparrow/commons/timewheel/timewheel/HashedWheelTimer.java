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
package com.java.sdeven.sparrow.commons.timewheel.timewheel;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.java.sdeven.sparrow.commons.timewheel.RejectedExecutionHandlerFactory;
import com.java.sdeven.sparrow.commons.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Time Wheel Timer
 * Minimum accuracy supported: 1ms (Thread.sleep itself is not accurate, so accuracy cannot be improved)
 * Minimum error: 1ms
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
public class HashedWheelTimer implements Timer {

    private final long tickDuration;
    private final HashedWheelBucket[] wheel;
    private final int mask;

    private final Indicator indicator;

    private final long startTime;

    private final Queue<HashedWheelTimerFuture> waitingTasks = Queues.newLinkedBlockingQueue();
    private final Queue<HashedWheelTimerFuture> canceledTasks = Queues.newLinkedBlockingQueue();

    private final ExecutorService taskProcessPool;

    public HashedWheelTimer(long tickDuration, int ticksPerWheel) {
        this(tickDuration, ticksPerWheel, 0);
    }

    /**
     * New Time Wheel Timer
     * @param #tickDuration Time interval, in milliseconds
     * @param #ticksPerWheel Number of reels
     * Number of threads to process the task, 0 means no new threads are enabled (if the timed task requires a time-consuming operation, please enable the thread pool)
     * @param #processThreadNum
     */
    public HashedWheelTimer(long tickDuration, int ticksPerWheel, int processThreadNum) {

        this.tickDuration = tickDuration;

        /** Initialize the wheel, format the size as N times 2, you can use & instead of remainder */
        int ticksNum = CommonUtils.formatSize(ticksPerWheel);
        wheel = new HashedWheelBucket[ticksNum];
        for (int i = 0; i < ticksNum; i++) {
            wheel[i] = new HashedWheelBucket();
        }
        mask = wheel.length - 1;

        /** Initialize the execution thread pool */
        if (processThreadNum <= 0) {
            taskProcessPool = null;
        }else {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("HashedWheelTimer-Executor-%d").build();
            BlockingQueue<Runnable> queue = Queues.newLinkedBlockingQueue(16);
            int core = Math.max(Runtime.getRuntime().availableProcessors(), processThreadNum);
            taskProcessPool = new ThreadPoolExecutor(core, 4 * core,
                    60, TimeUnit.SECONDS,
                    queue, threadFactory, RejectedExecutionHandlerFactory.newCallerRun("NewTimeWheelPool"));
        }

        startTime = System.currentTimeMillis();

        /** Start background threads */
        indicator = new Indicator();
        new Thread(indicator, "HashedWheelTimer-Indicator").start();
    }

    @Override
    public TimerFuture schedule(TimerTask task, long delay, TimeUnit unit) {

        long targetTime = System.currentTimeMillis() + unit.toMillis(delay);
        HashedWheelTimerFuture timerFuture = new HashedWheelTimerFuture(task, targetTime);

        /** Run expired and expired tasks directly */
        if (delay <= 0) {
            runTask(timerFuture);
            return timerFuture;
        }

        /**
         * Write to a blocking queue for concurrency safety
         * (for further performance optimization consider Netty's Multi-Producer-Single-Consumer queue)
         */
        waitingTasks.add(timerFuture);
        return timerFuture;
    }

    @Override
    public Set<TimerTask> stop() {
        indicator.stop.set(true);
        taskProcessPool.shutdown();
        while (!taskProcessPool.isTerminated()) {
            try {
                Thread.sleep(100);
            }catch (Exception ignore) {
            }
        }
        return indicator.getUnprocessedTasks();
    }

    /**
     * Wrapping {@link TimerTask}, maintaining data such as expected execution time, total number of laps, etc.
     */
    private final class HashedWheelTimerFuture implements TimerFuture {

        /**
         * Expected execution time
         */
        private final long targetTime;
        private final TimerTask timerTask;

        /**
         * The time frame to which you belong, for quick deletion of the task
         */
        private HashedWheelBucket bucket;
        /**
         * Total number of laps
         */
        private long totalTicks;
        /**
         *  Current status 0 - initialization pending, 1 - running, 2 - completed, 3 - cancelled
         */
        private int status;

        /**
         * task execution state enum
         */
        private static final int WAITING = 0;
        private static final int RUNNING = 1;
        private static final int FINISHED = 2;
        private static final int CANCELED = 3;

        public HashedWheelTimerFuture(TimerTask timerTask, long targetTime) {

            this.targetTime = targetTime;
            this.timerTask = timerTask;
            this.status = WAITING;
        }

        @Override
        public TimerTask getTask() {
            return timerTask;
        }

        @Override
        public boolean cancel() {
            if (status == WAITING) {
                status = CANCELED;
                canceledTasks.add(this);
                return true;
            }
            return false;
        }

        @Override
        public boolean isCancelled() {
            return status == CANCELED;
        }

        @Override
        public boolean isDone() {
            return status == FINISHED;
        }
    }

    /**
     * Time grid (essentially a chain table that maintains all the tasks that may need to be performed at this moment in time)
     */
    private final class HashedWheelBucket extends LinkedList<HashedWheelTimerFuture> {

        public void expireTimerTasks(long currentTick) {

            removeIf(timerFuture -> {

                /**
                 * The cancellation of tasks by external actions
                 * after processCanceledTasks will result in a situation where CANCELED tasks still exist in BUCKET
                 */
                if (timerFuture.status == HashedWheelTimerFuture.CANCELED) {
                    return true;
                }

                if (timerFuture.status != HashedWheelTimerFuture.WAITING) {
                    log.warn("[HashedWheelTimer] impossible, please fix the bug");
                    return true;
                }

                /**
                 * This round of direct scheduling
                 */
                if (timerFuture.totalTicks <= currentTick) {

                    if (timerFuture.totalTicks < currentTick) {
                        log.warn("[HashedWheelTimer] timerFuture.totalTicks < currentTick, please fix the bug");
                    }

                    try {
                        // Commit and Execution
                        runTask(timerFuture);
                    }catch (Exception ignore) {
                    } finally {
                        timerFuture.status = HashedWheelTimerFuture.FINISHED;
                    }
                    return true;
                }

                return false;
            });

        }
    }

    private void runTask(HashedWheelTimerFuture timerFuture) {
        timerFuture.status = HashedWheelTimerFuture.RUNNING;
        if (taskProcessPool == null) {
            timerFuture.timerTask.run();
        }else {
            taskProcessPool.submit(timerFuture.timerTask);
        }
    }

    /**
     * Analog pointer rotation
     */
    private class Indicator implements Runnable {

        private long tick = 0;

        private final AtomicBoolean stop = new AtomicBoolean(false);
        private final CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void run() {

            while (!stop.get()) {

                // 1. Push tasks from the queue into the time wheel
                pushTaskToBucket();
                // 2. Processing cancelled tasks
                processCanceledTasks();
                // 3. Wait for the pointer to jump to the next moment
                tickTack();
                // 4. Execute timed tasks
                int currentIndex = (int) (tick & mask);
                HashedWheelBucket bucket = wheel[currentIndex];
                bucket.expireTimerTasks(tick);

                tick ++;
            }
            latch.countDown();
        }

        /**
         * Simulates pointer rotation, when returning the pointer has turned to the next scale
         */
        private void tickTack() {

            /** Absolute time of the next dispatch */
            long nextTime = startTime + (tick + 1) * tickDuration;
            long sleepTime = nextTime - System.currentTimeMillis();

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                }catch (Exception ignore) {
                }
            }
        }

        /**
         * Processing cancelled tasks
         */
        private void processCanceledTasks() {
            while (true) {
                HashedWheelTimerFuture canceledTask = canceledTasks.poll();
                if (canceledTask == null) {
                    return;
                }
                /**
                 * Remove the task from the chain table (a bucket of null means it has not been officially pushed into the time frame and does not need to be processed
                 */
                if (canceledTask.bucket != null) {
                    canceledTask.bucket.remove(canceledTask);
                }
            }
        }

        /**
         * Push the tasks in the queue into the time wheel
         */
        private void pushTaskToBucket() {

            while (true) {
                HashedWheelTimerFuture timerTask = waitingTasks.poll();
                if (timerTask == null) {
                    return;
                }

                /** Total Offset */
                long offset = timerTask.targetTime - startTime;
                /** Total number of pointer steps to be taken */
                timerTask.totalTicks = offset / tickDuration;
                /** Residual calculation bucket index */
                int index = (int) (timerTask.totalTicks & mask);
                HashedWheelBucket bucket = wheel[index];

                /**
                 * {@link TimerTask} Maintains a Bucket reference for deleting the task
                 */
                timerTask.bucket = bucket;
                if (timerTask.status == HashedWheelTimerFuture.WAITING) {
                    bucket.add(timerTask);
                }
            }
        }

        public Set<TimerTask> getUnprocessedTasks() {
            try {
                latch.await();
            }catch (Exception ignore) {
            }

            Set<TimerTask> tasks = Sets.newHashSet();

            Consumer<HashedWheelTimerFuture> consumer = timerFuture -> {
                if (timerFuture.status == HashedWheelTimerFuture.WAITING) {
                    tasks.add(timerFuture.timerTask);
                }
            };

            waitingTasks.forEach(consumer);
            for (HashedWheelBucket bucket : wheel) {
                bucket.forEach(consumer);
            }
            return tasks;
        }
    }
}
