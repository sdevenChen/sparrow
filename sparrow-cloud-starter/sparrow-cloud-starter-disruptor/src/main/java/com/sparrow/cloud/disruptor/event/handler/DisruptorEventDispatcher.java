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
package com.sparrow.cloud.disruptor.event.handler;

import com.sparrow.cloud.disruptor.event.handler.chain.HandlerChain;
import com.sparrow.cloud.disruptor.event.handler.chain.HandlerChainResolver;
import com.sparrow.cloud.disruptor.event.handler.chain.ProxiedHandlerChain;
import com.sparrow.cloud.disruptor.context.event.DisruptorEvent;
import com.lmax.disruptor.EventHandler;
import org.springframework.core.Ordered;

/**
 * Disruptor 事件分发实现
 */
public class DisruptorEventDispatcher extends AbstractRouteableEventHandler<DisruptorEvent> implements EventHandler<DisruptorEvent>, Ordered {
	
	private int order = 0;
 
	public DisruptorEventDispatcher(HandlerChainResolver<DisruptorEvent> filterChainResolver, int order) {
		super(filterChainResolver);
		this.order = order;
	}
	
	/*
	 * 责任链入口
	 */
	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		
		//构造原始链对象
		HandlerChain<DisruptorEvent> originalChain = new ProxiedHandlerChain();
		//执行事件处理链
		this.doHandler(event, originalChain);
		
	}

	@Override
	public int getOrder() {
		return order;
	}

}

