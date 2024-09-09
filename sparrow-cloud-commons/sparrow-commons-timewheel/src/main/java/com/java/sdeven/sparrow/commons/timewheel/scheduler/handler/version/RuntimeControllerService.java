package com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.version;


import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;

/**
 * @author sdeven
 * @date 2024/3/28 11:05
 * @since 1.0.0
 */
public interface RuntimeControllerService {
    /**
     * 捡取可运行状态并设置独占和过期时间，
     *
     * @param  jobKey Cache key, key related to task information
     * @param  time cacheTime
     */
    default boolean preemptTheJob(String jobKey, Long time){
        throw new TimeWheelJobException("undefined job key task preemption");
    }
}
