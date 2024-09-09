package com.java.sdeven.sparrow.commons.timewheel.service;


import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;
import com.java.sdeven.sparrow.commons.timewheel.model.JobInfoDO;

import java.util.List;

/**
 * <p>
 * 定义默认定时任务服务
 * </p>
 * @author sdeven
 * @date 2024/2/22 11:33
 * @since 1.0.0
 */
public interface JobInfoService {
    /**
     * 定义查找所有开启任务的默认接口实现
     * @author sdeven
     * @date  11:34
     * @param #time
     * @return {@link List< JobInfoDO >}
     */
    default List<JobInfoDO> findAllByEnable(Long time) {
        throw new TimeWheelJobException("未定义元数据异常");
    }

    /**
     * 定义保存或更新任务的下次触发时间和状态默认接口实现
     * @author sdeven
     * @date 2024/2/22 11:33
     * @param #updatedJobInfo {@link JobInfoDO}
     */
    default void save(JobInfoDO updatedJobInfo) {
        throw new TimeWheelJobException("未定义更新元数据异常");
    }
}
