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
package com.sdeven.sparrow.starter.eventbus.impl;

import com.google.common.eventbus.Subscribe;
import com.sdeven.sparrow.commons.eventbus.EventHandler;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

	@NonNull
	private BeanFactory beanFactory;
	@NonNull
	private String beanName;
	@NonNull
	private Method method;
	@NonNull
	private Class<?> eventType;

	@Override
	@Subscribe
	public void handle(Object event) {
		try {
			Object bean = beanFactory.getBean(beanName);
			method.invoke(bean, event);
		} catch (InvocationTargetException e){
			if(null != e.getCause() && e.getCause() instanceof RuntimeException){
				throw (RuntimeException)e.getCause();
			}
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "EventHandlerImpl{" +
				//"beanFactory=" + beanFactory +
				", beanName='" + beanName + '\'' +
				", method=" + method +
				", eventType=" + eventType +
				'}';
	}

}
