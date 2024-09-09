package com.java.sdeven.sparrow.commons.timewheel.exception;

/**
 * 时间轮任务运行时异常
 *
 * @author tjq
 * @since 2020/5/26
 */
public class TimeWheelJobException extends RuntimeException {

    public TimeWheelJobException() {
    }

    public TimeWheelJobException(String message) {
        super(message);
    }

    public TimeWheelJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeWheelJobException(Throwable cause) {
        super(cause);
    }

    public TimeWheelJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
