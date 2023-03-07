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
package com.chen.sdeven.sparrow.commons.base.commons.params;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Generic paging query request class
 * @author sdeven
 */
@Data
public class PageQmd implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Current page
	 */
	@NotNull(message = "The current page cannot be empty")
	public Long current = 1L;

	/**
	 * Number of bars per page, default 10
	 */
	@NotNull(message = "The number of entries per page cannot be empty")
	public Long size = 10L;

	/**
	 * Sort Field String
	 */
	public String order;

}
