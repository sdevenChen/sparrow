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

import com.java.sdeven.sparrow.commons.timewheel.constants.SwitchableStatus;
import com.java.sdeven.sparrow.commons.timewheel.holder.InstanceTimeWheelService;
import com.java.sdeven.sparrow.commons.timewheel.model.JobInfoDO;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.TimeExpressionType;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.VersionType;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.timing.TimingStrategyService;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.version.RuntimeControllerService;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.version.VersionStrategyService;
import com.java.sdeven.sparrow.commons.timewheel.service.JobInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ahoo.cosid.provider.IdGeneratorProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 任务调度执行服务（调度 CRON 表达式的任务进行执行）
 * 原：FIX_RATE和FIX_DELAY任务不需要被调度，创建后直接被派发到Worker执行，只需要失败重试机制（在InstanceStatusCheckService中完成）
 * 先：那样写不太优雅，东一坨代码西一坨代码的，还是牺牲点性能统一调度算了 （优雅，永不过时～ BY：青钢影）
 *
 * @author tjq
 * @since 2020/4/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final JobInfoService jobInfoService;
    private final TimingStrategyService timingStrategyService;
    private final VersionStrategyService versionStrategyService;
    private final IdGeneratorProvider idGeneratorProvider;
    private final RuntimeControllerService runtimeControllerService;
    private final String MONITOR_SCHEDULE = "MONITOR:SCHEDULE:";

    //计算下一次调度时间（忽略5S内的重复执行，即CRON模式下最小的连续执行间隔为 SCHEDULE_RATE ms）
    public static final long SCHEDULE_RATE = 15000;

    public void scheduleNormalJob() {
        long start = System.currentTimeMillis();
        // 调度 CRON 表达式 JOB
        long timeThreshold = start + 2 * SCHEDULE_RATE;
        // 查询条件：任务开启 + 使用CRON表达调度时间 + 即将需要调度执行
        List<JobInfoDO> jobInfos = jobInfoService.findAllByEnable(timeThreshold);
        if (CollectionUtils.isEmpty(jobInfos)) {
            return;
        }
        // 1. 批量写调度日志表
        log.info("[普通调度器] 将安排这些 {} 作业： {}.", "CRON", jobInfos);
        try {
            scheduleNormalJob0(jobInfos);
        } catch (Exception e) {
            log.error("[普通调度器] 计划 cron 作业失败.", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[普通调度器] {} 任务调度耗时{} ms.", "CRON", cost);
        if (cost > SCHEDULE_RATE) {
            log.warn("[普通调度器] 数据库查询使用时间过多（{}ms），请检查数据库负载是否过高！", cost);
        }
    }

    /**
     * 调度普通服务端计算表达式类型（CRON、DAILY_TIME_INTERVAL）的任务
     */
    private void scheduleNormalJob0(List<JobInfoDO> jobInfos) {
        long nowTime = System.currentTimeMillis();
        try {
            // 2. Pushed into the time wheel to wait for scheduling execution
            jobInfos.forEach(jobInfoDO -> {
                //Preempt the execution status and synchronously preempt the key to redis
                String jobKey = MONITOR_SCHEDULE.concat(String.valueOf(jobInfoDO.getId()));
                boolean preempt = runtimeControllerService.preemptTheJob(jobKey, jobInfoDO.getLifecycle());
                if (!preempt) {
                    return;
                }
                long targetTriggerTime = jobInfoDO.getNextTriggerTime();
                long delay = 0;
                if (targetTriggerTime < nowTime) {
                    log.warn("[metrics_rule-Id-{}] 延时调度, expect: {}, current: {}", jobInfoDO.getId(), targetTriggerTime, System.currentTimeMillis());
                } else {
                    delay = targetTriggerTime - nowTime;
                }
                Long uniqueId = idGeneratorProvider.getShare() == null ? null : idGeneratorProvider.getShare().generate();
                jobInfoDO.setStartTime(System.currentTimeMillis());
                InstanceTimeWheelService.schedule(uniqueId, delay, () -> versionStrategyService.execHandler(jobInfoDO, VersionType.V1));
                // 3. Calculate the next scheduling time (ignoring repeated executions within 5S, that is, the minimum continuous execution interval in CRON mode is SCHEDULE_RATE ms)
                try {
                    refreshJob(jobInfoDO.getTimeExpressionType(), jobInfoDO);
                } catch (Exception e) {
                    log.error("[metrics_rule-Id-{}] 任务刷新失败.", jobInfoDO.getId(), e);
                }
            });
        } catch (Exception e) {
            log.error("[普通调度器] 任务调度失败.", e);
        }
    }

    private void refreshJob(TimeExpressionType timeExpressionType, JobInfoDO jobInfo) {
        Long nextTriggerTime = timingStrategyService.calculateNextTriggerTime(jobInfo.getNextTriggerTime(), timeExpressionType, jobInfo.getTimeExpression(), jobInfo.getStartTime(), jobInfo.getEndTime());
        if (nextTriggerTime == null) {
            log.warn("[Job-{}] 该作业将不再被安排，系统会将状态设置为 DISABLE！", jobInfo.getId());
            jobInfo.setStatus(SwitchableStatus.DISABLE.getV());
        } else {
            jobInfo.setNextTriggerTime(nextTriggerTime);
        }
        jobInfoService.save(jobInfo);
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() - 1711605409538L);
    }
}
