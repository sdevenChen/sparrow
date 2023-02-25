package com.java.sdeven.sparrow.commons.timewheel.timewheel.holder;


import com.java.sdeven.sparrow.commons.timewheel.timewheel.HashedWheelTimer;

/**
 * 时间轮单例
 *
 * @author sdeven
 * @since 2020/4/5
 */
public class HashedWheelTimerHolder {

    // 非精确时间轮，每 5S 走一格
    public static final HashedWheelTimer INACCURATE_TIMER = new HashedWheelTimer(5, 16, 0);

    private HashedWheelTimerHolder() {
    }
}
