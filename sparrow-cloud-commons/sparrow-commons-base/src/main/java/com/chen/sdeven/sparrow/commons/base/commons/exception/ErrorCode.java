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
 * 通用错误接口,提供错误码定义与异常转换方法
 * @Author sdeven
 * @Create 12/9/20 09:12 AM
 */
public enum ErrorCode implements CommonError {

    /**
     * 全局成功信息
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 业务异常
     */
    BIZ_ERROR(2, "业务异常"),
    /**
     * 未知异常
     */
    UNKNOWN(2, "服务器开小差了，请稍后再试!"),
    BASE_ERROR(-1, "公共组件异常");

    int code = -1;
    String message = null;
    Object data = null;

    /**
     * 全局系统异常
     */
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
