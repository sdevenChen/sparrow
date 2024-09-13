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
