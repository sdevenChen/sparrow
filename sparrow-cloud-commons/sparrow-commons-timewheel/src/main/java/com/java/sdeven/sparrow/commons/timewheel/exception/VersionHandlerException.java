package com.java.sdeven.sparrow.commons.timewheel.exception;

/**
 * 版本适配运行时异常
 *
 * @author tjq
 * @since 2020/5/26
 */
public class VersionHandlerException extends RuntimeException {

    public VersionHandlerException() {
    }

    public VersionHandlerException(String message) {
        super(message);
    }

    public VersionHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionHandlerException(Throwable cause) {
        super(cause);
    }

    public VersionHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
