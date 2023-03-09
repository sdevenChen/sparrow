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
package com.sdeven.sparrow.commons.base.commons.result;


import com.sdeven.sparrow.commons.base.commons.exception.CommonError;
import com.sdeven.sparrow.commons.base.commons.exception.ErrorCode;

import java.util.List;

/**
 * Response Processor
 * @author sdeven
 * @since 1.0.0
 */
public abstract class Results {

	public static Result<Void> success() {
		Result<Void> response = new Result<Void>();
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	/**
	 * Results of the response on success
	 * @param #result <T>
	 * @return Result<T>
	 */
	public static <T> Result<T> success(T result) {
		Result<T> response = new Result<T>();
		response.setData(result);
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	public static <T> Result<T> success(List<T> result) {
		Result<T> response = new Result<T>();
		response.setDatas(result);
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	/**
	 * Successful response
	 * @param #page {@link Page<T>}
	 * @return <T>
	 */
	public static <T> PageResult<T> success(Page<T> page) {
		PageResult<T> response = new PageResult<T>();
		response.setData(page);
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	/**
	 * Failure response method
	 * @param #message
	 * @return <T>
	 */
	public static <T> Result<T> fail(String message) {
		Result<T> response = new Result<T>();
		response.setCode(ErrorCode.BIZ_ERROR.getCode());
		if (message == null || message.equals("")) {
			response.setMessage(ErrorCode.BIZ_ERROR.getMessage());
		} else {
			/** Unique constraint exception */
			response.setMessage(message);
		}
		return response;
	}

	/**
	 * Business exceptions and return custom messages
	 * @param #error {@link CommonError} Error codes and error messages
	 * @return <T>
	 */
	public static <T> Result<T> fail(CommonError error) {
		Result<T> response = new Result<T>();
		response.setMessage(error.getMessage());
		response.setCode(error.getCode());
		return response;
	}

	/**
	 * Business exceptions and return custom messages
	 * @param #error {@link CommonError} Error codes and error messages
	 * @param #message Tip Message
	 * @return <T>
	 */
	public static <T> Result<T> fail(CommonError error, String message) {
		Result<T> response = new Result<T>();
		response.setMessage(message);
		response.setCode(error.getCode());
		return response;
	}

	/**
	 * Failure to respond-Service timeout
	 */
	public static <T> Result<T> failServiceRemoting() {
		Result<T> response = new Result<>();
		response.setCode(ErrorCode.RPC_SERVICE_ERROR.getCode());
		response.setMessage(ErrorCode.RPC_SERVICE_ERROR.getMessage());
		response.setData(null);
		return response;
	}

	/**
	 * Failure to respond - service timeout
	 */
	public static <T> Result<T> failServiceTimeOut() {
		Result<T> response = new Result<T>();
		response.setCode(ErrorCode.RPC_SERVICE_TIMEOUT.getCode());
		response.setMessage(ErrorCode.RPC_SERVICE_TIMEOUT.getMessage());
		response.setData(null);
		return response;
	}

	/**
	 * Failure response-services are invoked in an abnormal state
	 */
	public static <T> Result<T> failServiceIllegalState() {
		Result<T> response = new Result<T>();
		response.setCode(ErrorCode.RPC_SERVICE_ILLEGAL_STATE.getCode());
		response.setMessage(ErrorCode.RPC_SERVICE_ILLEGAL_STATE.getMessage());
		response.setData(null);
		return response;
	}


}
