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
package com.chen.sdeven.sparrow.commons.base.commons.exception;


/**
 * Generic error interface implementation, providing error code definition and exception
 * conversion method enumeration
 * @author sdeven
 */
public enum ErrorCode implements CommonError {
    SUCCESS(0, "SUCCESS"),
    BIZ_ERROR(2, "BUSINESS_ERROR"),
    UNKNOWN(3, "UNKNOWN_ERROR"),
    BASE_ERROR(-1, "GENERAL_COMPONENT_ERROR");

    int code = -1;
    String message = null;
    Object data = null;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public Object getData() {
        return this.data;
    }

    public ErrorCode setData(Object data) {
        this.data = data;
        return this;
    }

}
