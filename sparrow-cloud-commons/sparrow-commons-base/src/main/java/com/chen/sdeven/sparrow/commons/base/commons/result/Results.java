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
package com.chen.sdeven.sparrow.commons.base.commons.result;


import com.chen.sdeven.sparrow.commons.base.commons.exception.CommonError;
import com.chen.sdeven.sparrow.commons.base.commons.exception.ErrorCode;
import com.chen.sdeven.sparrow.commons.base.commons.result.Page;
import com.chen.sdeven.sparrow.commons.base.commons.result.PageResult;
import com.chen.sdeven.sparrow.commons.base.commons.result.Result;

import java.util.List;

/**
 * @Description 处理响应返回
 * @Author sdeven
 * @Create 11/18/20 16:42

 */
public abstract class Results {

	public static Result<Void> success() {
		Result<Void> response = new Result<Void>();
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	/**
	 * 成功响应
	 *
	 * @param <T>
	 * @return
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
	 * 成功响应
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> PageResult<T> success(Page<T> page) {
		PageResult<T> response = new PageResult<T>();
		response.setData(page);
		response.setCode(ErrorCode.SUCCESS.getCode());
		response.setMessage(ErrorCode.SUCCESS.getMessage());
		return response;
	}

	/**
	 * @Description 失败响应,并返回消息
	 * @Date 11/18/20 16:49
	 * @Param
	 * @return
	 */
	public static <T> Result<T> fail(String message) {
		Result<T> response = new Result<T>();
		response.setCode(ErrorCode.BIZ_ERROR.getCode());
		if (message == null || message.equals("")) {
			response.setMessage(ErrorCode.BIZ_ERROR.getMessage());
		} else {
			// 唯一约束异常
			response.setMessage(message);
		}
		return response;
	}

	/**
	 * @Description 业务异常并返回自定义消息
	 * @param message 错误码以及错误信息
	 * @param <T>
	 * @return
	 */
	public static <T> Result<T> fail(CommonError message) {
		Result<T> response = new Result<T>();
		response.setMessage(message.getMessage());
		response.setCode(message.getCode());
		return response;
	}

	/**
	 * @Description 业务异常并返回自定义消息
	 * @param message 错误码以及错误信息
	 * @param <T>
	 * @return
	 */
	public static <T> Result<T> fail(CommonError error, String message) {
		Result<T> response = new Result<T>();
		response.setMessage(message);
		response.setCode(error.getCode());
		return response;
	}

}
