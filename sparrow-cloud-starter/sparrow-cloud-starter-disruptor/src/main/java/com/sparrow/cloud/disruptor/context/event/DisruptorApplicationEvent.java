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
package com.sparrow.cloud.disruptor.context.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DisruptorApplicationEvent extends ApplicationEvent {

	/**
	 * 当前事件绑定的数据对象
	 */
	protected Object bind;

	public DisruptorApplicationEvent(Object source, Object bind) {
		super(source);
		this.bind = bind;
	}
	
	public DisruptorApplicationEvent(Object source) {
		super(source);
	}

	public Object getBind() {
		return bind;
	}
	
	public void bind(Object bind) {
		this.bind = bind;
	}
	
	
}
