
/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.sdeven.sparrow.commons.base.commons.exception;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Generic error interface, providing error code definition and exception conversion methods
 * @author sdeven
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
