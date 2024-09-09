package com.java.sdeven.sparrow.commons.timewheel.scheduler;


import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;
import com.java.sdeven.sparrow.commons.timewheel.model.JobInfoDO;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.VersionType;

/**
 * <p>
 * 版本策略适配
 * </p>
 * @author sdeven
 * @date 2024/2/22 11:39
 * @since 1.0.0
 */
public interface VersionStrategyHandler {

    /**
     * 默认定时任务执行器,未实现将抛出异常
     *
     */
   default void processor(JobInfoDO jobInfoDO){
       throw new TimeWheelJobException("未定义执行器异常");
   }

    /**
     * 支持的定时策略,未实现将抛出异常
     *
     * @return TimeExpressionType
     */
    default VersionType supportType() {
        throw new TimeWheelJobException("未定义执行器版本异常");
    }
}
