
package com.chen.sdeven.sparrow.commons.base.commons.exception;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 通用错误接口,提供错误码定义与异常转换方法
 * @Author sdeven
 * @Create 12/9/20 09:12
 */
public interface CommonError extends Supplier<CommonException>, Serializable {

    int getCode();

    String getMessage();

    default Object getData(){
        return null;
    }

    default void fire() {
        throw get();
    }

    default void fire(Throwable e) {
        throw wrap(e);
    }


    /**
     * Throw UnprocessableException if true.<br>
     * <i>Example:</i><br>2
     * {@code CommonError.XXX.throwIf(!Optional.ofNullable(a).isPresent())}
     * throw UnprocessableException while Optional.ofNullable(a) is empty.
     * @param throwIf true is throw
     */
    default void throwIf(boolean throwIf) {
        if (throwIf) {
            throw new CommonException(this);
        }
    }

    /**
     * Throw UnprocessableException unless true.<br>
     * Throw UnprocessableException if false.<br>
     * <i>Example:</i><br>
     * {@code CommonError.XXX.throwUnless(Optional.ofNullable(a).isPresent())}<br>
     * throw UnprocessableException while Optional.ofNullable(a) is empty.
     * @param throwUnless false is throw
     */
    default void throwUnless(boolean throwUnless) {
        if (!throwUnless) {
            throw new CommonException(this);
        }
    }

    /**
     * get UnprocessableException.<br>
     * <i>Example:</i><br>
     * {@code Optional.ofNullable(a).orElseThrow(CommonError.XXX)}
     * @return UnprocessableException
     */
    @Override
    default CommonException get() {
        return new CommonException(this);
    }

    @FunctionalInterface
    interface CodeWrapper<T> {
        /**
         * call the function
         * @return
         * @throws Exception
         */
        T call() throws Exception;
    }

    /**
     * try to call a function <br>
     * <i>Example:</i><br>
     * {@code CommonError.XXX.wrap(() -> \{ some code \}, IOException.class)}
     * @param fn The function you need to call.
     * @param exs The exceptions you need to catch, if not, all exceptions are thrown as UnprocessableException.
     * @param <T>
     * @return the value of fn
     */
    @SuppressWarnings("unchecked")
	default <T> T wrap(CodeWrapper<T> fn, Class<? extends Throwable>... exs) {
        try {
            return fn.call();
        } catch (Exception e) {
            if (exs.length == 0) {
                throw new CommonException(this, e);
            }

            for (Class<? extends Throwable> ee : exs) {
                if (ee.isAssignableFrom(e.getClass())) {
                    throw new CommonException(this, e);
                }
            }

            throw new RuntimeException(e);
        }
    }

    /**
     * try to call a function <br>
     * <i>Example:</i><br>
     * {@code CommonError.XXX.wrap(() -> \{ some code \}, IOException.class)}
     * @param e The exceptions you need to catch, if not, all exceptions are thrown as UnprocessableException.
     * @return the value of fn
     */
    default CommonException wrap(Throwable e) {
        if (e instanceof CommonException) { return (CommonException)e;}
        return new CommonException(this, getMessage(), e);
    }


}
