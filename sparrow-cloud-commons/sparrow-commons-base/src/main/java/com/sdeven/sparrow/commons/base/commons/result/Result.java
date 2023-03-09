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

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A commons using response parameter base class
 * @author sdeven
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

	private static final long serialVersionUID = -655403293051655566L;
	/**
	 * Return message data
	 */
	private T data;

	private List<T> datas;
	/**
	 * Return Status Code
 	 */
	private Integer code;
	/**
	 * Return to description
	 */
	private String message;

	public Boolean success(){
		return Integer.valueOf(0).equals(code);
	}

	public Boolean fail(){
		return !success();
	}

	public Boolean nullData(){
		return data == null;
	}

	public Boolean nonNullData(){
		return !nullData();
	}

}