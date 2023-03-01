package com.chen.sdeven.sparrow.commons.base.commons.exception;

import lombok.Builder;

@Builder
public class MutableError implements CommonError {

    private int code = -1;
    private String message = null;
    private  Object data = null;

    public MutableError(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }


}
