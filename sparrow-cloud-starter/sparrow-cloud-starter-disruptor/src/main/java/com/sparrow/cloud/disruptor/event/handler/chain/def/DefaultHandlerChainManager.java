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
package com.sparrow.cloud.disruptor.event.handler.chain.def;

import com.sparrow.cloud.disruptor.util.StringUtils;
import com.sparrow.cloud.disruptor.context.event.DisruptorEvent;
import com.sparrow.cloud.disruptor.event.handler.DisruptorHandler;
import com.sparrow.cloud.disruptor.event.handler.Nameable;
import com.sparrow.cloud.disruptor.event.handler.NamedHandlerList;
import com.sparrow.cloud.disruptor.event.handler.chain.HandlerChain;
import com.sparrow.cloud.disruptor.event.handler.chain.HandlerChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DefaultHandlerChainManager implements HandlerChainManager<DisruptorEvent> {
	
	private static transient final Logger log = LoggerFactory.getLogger(DefaultHandlerChainManager.class);

    private Map<String, DisruptorHandler<DisruptorEvent>> handlers;

    private Map<String, NamedHandlerList<DisruptorEvent>> handlerChains;

    private final static String DEFAULT_CHAIN_DEFINATION_DELIMITER_CHAR = ",";
    
    public DefaultHandlerChainManager() {
        this.handlers = new LinkedHashMap<String, DisruptorHandler<DisruptorEvent>>();
        this.handlerChains = new LinkedHashMap<String, NamedHandlerList<DisruptorEvent>>();
    }
    
    @Override
    public Map<String, DisruptorHandler<DisruptorEvent>> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, DisruptorHandler<DisruptorEvent>> handlers) {
        this.handlers = handlers;
    }

    public Map<String, NamedHandlerList<DisruptorEvent>> getHandlerChains() {
        return handlerChains;
    }
    
    public void setHandlerChains(Map<String, NamedHandlerList<DisruptorEvent>> handlerChains) {
        this.handlerChains = handlerChains;
    }

    public DisruptorHandler<DisruptorEvent> getHandler(String name) {
        return this.handlers.get(name);
    }

    @Override
    public void addHandler(String name, DisruptorHandler<DisruptorEvent> handler) {
        addHandler(name, handler, true);
    }
    
    protected void addHandler(String name, DisruptorHandler<DisruptorEvent> handler, boolean overwrite) {
        DisruptorHandler<DisruptorEvent> existing = getHandler(name);
        if (existing == null || overwrite) {
            if (handler instanceof Nameable) {
                ((Nameable) handler).setName(name);
            }
            this.handlers.put(name, handler);
        }
    }

    @Override
    public void createChain(String chainName, String chainDefinition) {
        if (StringUtils.isBlank(chainName)) {
            throw new NullPointerException("chainName cannot be null or empty.");
        }
        if (StringUtils.isBlank(chainDefinition)) {
            throw new NullPointerException("chainDefinition cannot be null or empty.");
        }
        if (log.isDebugEnabled()) {
            log.debug("Creating chain [" + chainName + "] from String definition [" + chainDefinition + "]");
        }
        String[] handlerTokens = splitChainDefinition(chainDefinition);
        for (String token : handlerTokens) {
            addToChain(chainName, token);
        }
    }

    /**
     * Splits the comma-delimited handler chain definition line into individual handler definition tokens.
     * @param chainDefinition chain definition line 
     * @return array of chain definition
     */
    protected String[] splitChainDefinition(String chainDefinition) {
    	String trimToNull = StringUtils.trimToNull(chainDefinition);
    	if(trimToNull == null){
    		return null;
    	}
    	String[] split = StringUtils.splits(trimToNull, DEFAULT_CHAIN_DEFINATION_DELIMITER_CHAR);
    	for (int i = 0; i < split.length; i++) {
    		split[i] = StringUtils.trimToNull(split[i]);
		}
        return split;
    }

    public static void main(String[] args) {
		
	}
    
    @Override
    public void addToChain(String chainName, String handlerName) {
        if (StringUtils.isBlank(chainName)) {
            throw new IllegalArgumentException("chainName cannot be null or empty.");
        }
        DisruptorHandler<DisruptorEvent> handler = getHandler(handlerName);
        if (handler == null) {
            throw new IllegalArgumentException("There is no handler with name '" + handlerName +
                    "' to apply to chain [" + chainName + "] in the pool of available Handlers.  Ensure a " +
                    "handler with that name/path has first been registered with the addHandler method(s).");
        }
        NamedHandlerList<DisruptorEvent> chain = ensureChain(chainName);
        chain.add(handler);
    }

    protected NamedHandlerList<DisruptorEvent> ensureChain(String chainName) {
        NamedHandlerList<DisruptorEvent> chain = getChain(chainName);
        if (chain == null) {
            chain = new DefaultNamedHandlerList(chainName);
            this.handlerChains.put(chainName, chain);
        }
        return chain;
    }

    @Override
    public NamedHandlerList<DisruptorEvent> getChain(String chainName) {
        return this.handlerChains.get(chainName);
    }

    @Override
    public boolean hasChains() {
        return !CollectionUtils.isEmpty(this.handlerChains);
    }

    @Override
    @SuppressWarnings("unchecked")
	public Set<String> getChainNames() {
        return this.handlerChains != null ? this.handlerChains.keySet() : Collections.EMPTY_SET;
    }

    @Override
    public HandlerChain<DisruptorEvent> proxy(HandlerChain<DisruptorEvent> original, String chainName) {
        NamedHandlerList<DisruptorEvent> configured = getChain(chainName);
        if (configured == null) {
            String msg = "There is no configured chain under the name/key [" + chainName + "].";
            throw new IllegalArgumentException(msg);
        }
        return configured.proxy(original);
    }

	
    

}
