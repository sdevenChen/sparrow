package com.java.sdeven.sparrow.commons.timewheel.scheduler.handler.timing;


import com.java.sdeven.sparrow.commons.timewheel.scheduler.TimingStrategyHandler;

/**
 * @author Echo009
 * @since 2022/3/22
 */
public abstract class AbstractTimingStrategyHandler implements TimingStrategyHandler {
    @Override
    public void validate(String timeExpression) {
    }

    @Override
    public Long calculateNextTriggerTime(Long preTriggerTime, String timeExpression, Long startTime, Long endTime) {
        return null;
    }
}
