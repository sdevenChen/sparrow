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
package com.sdeven.sparrow.starter.nodeflow;


 /**
   * @Description Nodeflow 异常
   * @Author sdeven
   * @Create 12/12/20 10:19
   */
public class NodeflowException extends Exception {
	private static final long serialVersionUID = 9222894717715477267L;
	private String[] params;

	public NodeflowException(String code, String... params) {
		super(code);
		this.params = params;
	}

	public NodeflowException(String code, Throwable t) {
		super(code, t);
	}

	public NodeflowException(String code, Throwable t, String... params) {
		super(code, t);
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

}
