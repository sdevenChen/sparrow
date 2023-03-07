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
package com.chen.sdeven.sparrow.cloud.api.advice;


import com.chen.sdeven.sparrow.commons.base.commons.exception.BizServiceException;
import com.chen.sdeven.sparrow.commons.base.commons.exception.CommonException;
import com.chen.sdeven.sparrow.commons.base.commons.exception.ErrorCode;
import com.chen.sdeven.sparrow.commons.base.commons.result.Result;
import com.chen.sdeven.sparrow.commons.base.commons.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    /**
     * Customized commons exceptions Processor
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Result> handlerMException(CommonException me) {
        log.error(me.getMessage(), me);
        Result<String> response = Results.fail(ErrorCode.BASE_ERROR, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Binding Exception Processor
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result> handlerException(BindException me) {
        log.error(me.getMessage(), me);
        Result<Void> response = Results.fail(ErrorCode.UNKNOWN, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Business Exception Processor
     */
    @ExceptionHandler(BizServiceException.class)
    public ResponseEntity<Result> handlerException(BizServiceException me) {
        log.error(me.getMessage(), me);
        Result<Void> response = Results.fail(ErrorCode.BIZ_ERROR, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * System Exception Processor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handlerException(Exception me) {
        log.error(me.getMessage(), me);
        Result<String> response = Results.fail(ErrorCode.UNKNOWN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
