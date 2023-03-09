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
package com.sdeven.sparrow.starter.dubbo.exception;

import com.sdeven.sparrow.commons.base.commons.exception.CommonException;
import com.sdeven.sparrow.commons.base.commons.exception.ErrorCode;
import com.sdeven.sparrow.commons.base.commons.result.Result;
import com.sdeven.sparrow.commons.base.commons.result.Results;
import org.apache.dubbo.remoting.ExecutionException;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.RpcException;

import javax.validation.ValidationException;

/**
 * rpc provider Exception manage
 * @author sdeven
 * @since 1.0.0
 */
public class RpcExceptionManage {

    public static Result exceptionResult(Throwable throwable) {
        if (throwable instanceof ExecutionException) {
            return Results.failServiceRemoting();
        } else if (throwable instanceof RpcException) {
            return Results.failServiceRemoting();
        } else if (throwable instanceof IllegalStateException) {
            return Results.failServiceIllegalState();
        } else if (throwable instanceof TimeoutException) {
            return Results.failServiceTimeOut();
        } else if (throwable instanceof RemotingException) {
            return Results.failServiceRemoting();
        }else if(throwable instanceof CommonException) {
            return Results.fail(((CommonException) throwable).getError());
        } else if (throwable instanceof IllegalArgumentException) {
            return Results.fail(ErrorCode.PARAMETER_REQUIRED);
        } else if (throwable instanceof ValidationException) {
            return Results.fail(ErrorCode.VALIDATE_FAIL);
        } else if (throwable instanceof DataBaseException) {
            return Results.failServiceRemoting();
        } else {
            return Results.fail(ErrorCode.RPC_SERVICE_ERROR);
        }
    }
}
