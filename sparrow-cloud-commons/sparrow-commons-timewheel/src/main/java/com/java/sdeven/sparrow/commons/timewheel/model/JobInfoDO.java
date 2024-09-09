package com.java.sdeven.sparrow.commons.timewheel.model;


import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.TimeExpressionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务信息表
 *
 * @author tjq
 * @since 2020/3/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfoDO<T> {
    /**
     * 任务Id （metricsId）
     */
    private Integer id;

    /* ************************** 任务基本信息 ************************** */
    /**
     * 任务名称 （metricsName）
     */
    private String jobName;
    /**
     * 任务描述 (metrics description)
     */
    private String jobDescription;

    /**
     * 任务自带的参数 (sqlScript)
     */
    private String jobParams;

    /* ************************** 定时参数 ************************** */
    /**
     * 时间表达式类型（CRON/FIX_RATE/FIX_DELAY）
     */
    private TimeExpressionType timeExpressionType;
    /**
     * 执行SQL
     */
    private String sqlScript;

    /**
     * 执行查询结果SQL
     */
    private String resultSqlScript;

    /**
     * 时间表达式，CRON/NULL/LONG/LONG
     */
    private String timeExpression;
    /**
     * 值类型，数值/百分比
     */
    private Integer varType;

    /* ************************** 运行时配置 ************************** */
    /**
     * 并发度，同时执行某个任务的最大线程数量
     */
    private Integer concurrency;
    /**
     * 任务整体超时时间
     */
    private Long instanceTimeLimit;

    /* ************************** 重试配置 ************************** */

    private Integer taskRetryNum;

    /**
     * 1 正常运行，2 停止（不再调度）
     */
    private Integer status;
    /**
     * 下一次调度时间
     */
    private Long nextTriggerTime;
    /* ************************** 集群配置 ************************** */

    private Long lifecycle;

    private Long startTime;

    private Long endTime;

    private T metrics;

}
