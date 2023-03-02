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

import java.io.Serializable;

public class BizServiceException extends RuntimeException {

    private static final long serialVersionUID = -1534266031480042987L;

    private CommonError error = null;

    public BizServiceException() {

    }

    public BizServiceException(CommonError error) {
        super(String.format("[%s] %s", error.getCode(), error.getMessage()));
        this.error = error;
    }

    public BizServiceException(CommonError error, String message) {
        super(String.format("[%s] %s", error.getCode(), message));
        this.error = error;
    }

    public BizServiceException(CommonError error, Throwable cause) {
        super(String.format("[%s] %s", error.getCode(), cause));
        this.error = error;
    }

    public BizServiceException(CommonError error, String message, Throwable cause) {
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


    public static class BizServiceExceptionBuilder implements Serializable {

        private static final long serialVersionUID = -1534266031480044987L;

        private int code;
        private String message;

        public BizServiceExceptionBuilder setCode(int code) {
            this.code = code;
            return this;
        }

        public BizServiceExceptionBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public BizServiceException build() {
            return new BizServiceException(new CommonError() {
                @Override
                public int getCode() {
                    return BizServiceExceptionBuilder.this.code;
                }

                @Override
                public String getMessage() {
                    return BizServiceExceptionBuilder.this.message;
                }
            });
        }
    }


}
