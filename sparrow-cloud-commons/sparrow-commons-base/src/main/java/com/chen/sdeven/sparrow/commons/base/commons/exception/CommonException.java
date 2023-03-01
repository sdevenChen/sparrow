package com.chen.sdeven.sparrow.commons.base.commons.exception;

/**
 * @Author sdeven
 */
public class CommonException extends RuntimeException {

	private static final long serialVersionUID = -1534266031480042987L;

	private CommonError error = null;

	public CommonException() {

    }

    public CommonException(CommonError error) {
        super(String.format("[%s] %s", error.getCode(), error.getMessage()));
        this.error = error;
    }

    public CommonException(CommonError error, String message) {
        super(String.format("[%s] %s", error.getCode(), message));
        this.error = error;
    }

    public CommonException(CommonError error, Throwable cause) {
        super(String.format("[%s] %s", error.getCode(), cause));
        this.error = error;
    }

    public CommonException(CommonError error, String message, Throwable cause) {
        super(String.format("[%s] %s", error.getCode(), message), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getCode();
    }

    public CommonError getError() {
        return error;
    }

    public boolean is(CommonError error) {
        return this.error.getCode() == error.getCode();
    }

}
