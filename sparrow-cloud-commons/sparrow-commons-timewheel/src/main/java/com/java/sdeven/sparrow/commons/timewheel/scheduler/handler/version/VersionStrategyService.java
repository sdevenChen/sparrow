package com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.version;

import com.java.sdeven.sparrow.commons.timewheel.exception.VersionHandlerException;
import com.java.sdeven.sparrow.commons.timewheel.model.JobInfoDO;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.VersionStrategyHandler;
import com.java.sdeven.sparrow.commons.timewheel.scheduler.enums.VersionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Application execution implementation, compatible with multi-version executors
 * @author sdeven
 * @since 2022/3/21
 */
@Slf4j
@Service
public class VersionStrategyService {
    private final Map<VersionType, VersionStrategyHandler> strategyContainer;

    public VersionStrategyService(List<VersionStrategyHandler> versionStrategyHandlers) {
        strategyContainer = new EnumMap<>(VersionType.class);
        for (VersionStrategyHandler version : versionStrategyHandlers) {
            strategyContainer.put(version.supportType(), version);
        }
    }

    /**
     * 计算下次的调度时间
     * 任务参数 obj
     *
     */
    public void execHandler(JobInfoDO jobInfoDO, VersionType version) {
        getHandler(version).processor(jobInfoDO);
    }

    private VersionStrategyHandler getHandler(VersionType versionType) {
        VersionStrategyHandler versionStrategyHandler = strategyContainer.get(versionType);
        if (versionStrategyHandler == null) {
            throw new VersionHandlerException("No matching VersionStrategyHandler for this VersionType:" + versionStrategyHandler);
        }
        return versionStrategyHandler;
    }

}
