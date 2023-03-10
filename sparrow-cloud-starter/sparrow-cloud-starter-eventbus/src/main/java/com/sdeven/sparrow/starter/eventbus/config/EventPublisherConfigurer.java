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
package com.sdeven.sparrow.starter.eventbus.config;

import com.google.common.base.Preconditions;
import com.sdeven.sparrow.commons.eventbus.EventHandler;
import com.sdeven.sparrow.commons.eventbus.EventPublisher;
import com.sdeven.sparrow.commons.eventbus.EventSubscribe;
import com.sdeven.sparrow.starter.eventbus.impl.EventHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class EventPublisherConfigurer implements BeanPostProcessor, BeanFactoryAware {

	@NonNull
	private EventPublisher eventPublisher;

	private BeanFactory beanFactory;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		int order = 0;

		for (Method method : bean.getClass().getMethods()) {

			EventSubscribe subscribe = AnnotationUtils.findAnnotation(method, EventSubscribe.class);

			if (subscribe == null) {
				continue;
			}
			order = subscribe.order();
			Preconditions.checkState(method.getParameterCount() == 1, "The event subscriber method can only have one parameter: " + method);

			Class<?> eventClass = method.getParameterTypes()[0];

			EventHandler eventHandler = new EventHandlerImpl(beanFactory, beanName, method, eventClass);
			eventPublisher.register(eventClass, eventHandler);

		}
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
