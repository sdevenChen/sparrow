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

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.sdeven.sparrow.commons.eventbus.EventHandler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class EventHandlerRegistry {

	private final ConcurrentMap<Class<?>, Set<EventHandler>> handlersMap = Maps.newConcurrentMap();

	public void register(Class<?> eventType, EventHandler handler) {
		Set<EventHandler> eventHandlers = handlersMap.get(eventType);
		if (eventHandlers == null) {
			Set<EventHandler> newSet = new HashSet<>();
			eventHandlers = MoreObjects.firstNonNull(handlersMap.putIfAbsent(eventType, newSet), newSet);
		}
		eventHandlers.add(handler);
		this.addParentHandler(eventType);
		this.injection2SubEvent(eventType,handler);
	}

	/**
	 * Add the handler of the parent class
	 * @param #eventClass
	 */
	private void addParentHandler(Class<?> eventClass) {
		ImmutableSet<Class<?>> eventTypes = flattenHierarchy(eventClass);
		Set<EventHandler> existHandlers;
		Set<EventHandler> parentEventHandlers;
		for (Class<?> eventType : eventTypes) {
			parentEventHandlers = handlersMap.get(eventType);
			existHandlers = handlersMap.get(eventClass);
			if(existHandlers != null && parentEventHandlers != null){
				existHandlers.addAll(parentEventHandlers);
			}
		}
	}

	/**
	 * Injecting handlers into sub-events
	 * @param #eventClass {@link Class<?>}
	 * @param #handler {@link EventHandler}
	 */
	private void injection2SubEvent(Class<?> eventClass, EventHandler handler){
		Set<EventHandler> existHandlers;
		for(Class<?> clazz : handlersMap.keySet()){
			if(eventClass.isAssignableFrom(clazz)){
				existHandlers = handlersMap.get(clazz);
				if(existHandlers != null){
					existHandlers.add(handler);
				}
			}
		}
	}

	/**
	 * getting a handler
	 * @param #eventClass a handler class
	 * @return Set<EventHandler>
	 */
	public Set<EventHandler> getHandlers(Class<?> eventClass){
		return handlersMap.get(eventClass);
	}

	public Iterator<EventHandler> getHandlers(Object event) {
		ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());

		List<Iterator<EventHandler>> handlerIterators = Lists.newArrayListWithCapacity(eventTypes.size());

		for (Class<?> eventType : eventTypes) {
			Set<EventHandler> eventHandlers = handlersMap.get(eventType);
			if (eventHandlers != null) {
				handlerIterators.add(eventHandlers.iterator());
			}
		}
		return Iterators.concat(handlerIterators.iterator());
	}

	private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys()
			.build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>() {
				@Override
				public ImmutableSet<Class<?>> load(Class<?> concreteClass) {
					return ImmutableSet.<Class<?>>copyOf(TypeToken.of(concreteClass).getTypes().rawTypes());
				}
			});

	private static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
		try {
			return flattenHierarchyCache.getUnchecked(concreteClass);
		} catch (UncheckedExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
