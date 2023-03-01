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
     * 自定义公共异常
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Result> handlerMException(CommonException me) {
        log.error(me.getMessage(), me);
        Result<String> response = Results.fail(ErrorCode.BASE_ERROR, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result> handlerException(BindException me) {
        log.error(me.getMessage(), me);
        Result<Void> response = Results.fail(ErrorCode.UNKNOWN, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BizServiceException.class)
    public ResponseEntity<Result> handlerException(BizServiceException me) {
        log.error(me.getMessage(), me);
        Result<Void> response = Results.fail(ErrorCode.BIZ_ERROR, me.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handlerException(Exception me) {
        log.error(me.getMessage(), me);
        Result<String> response = Results.fail(ErrorCode.UNKNOWN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
