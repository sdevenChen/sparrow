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
package com.sdeven.sparrow.starter.nodeflow.configuration;

import com.sdeven.sparrow.starter.nodeflow.Nodeflow;
import com.sdeven.sparrow.starter.nodeflow.spi.NfComponent;
import com.sdeven.sparrow.starter.nodeflow.util.SpringContextUtil;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collection;
import java.util.Map;

/**
 * Node extension component filter loader
 * @author sdeven
 */
@Configuration
@ComponentScan(basePackages = "com.sdeven.sparrow",includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NfComponent.class))
public class NfComponentLoaderConfigutation implements ApplicationListener<ContextRefreshedEvent> {
    @Setter
    private  ApplicationContext context;

    /**
     * Extension to implement component bean scan registration to Nodeflow
     * @param #cre {@link ContextRefreshedEvent}
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent cre) {
        context = cre.getApplicationContext();
        if (context.getParent() != null) {
            Map<String, NfComponent> beansMap = context.getBeansOfType(NfComponent.class);
            Collection<NfComponent> list = beansMap.values();
            for (NfComponent component : list) {
                Nodeflow.register(component);
            }
        }
    }

    @Bean
    public SpringContextUtil springContextUtil(){
        SpringContextUtil springContextUtil = new SpringContextUtil();
        springContextUtil.setApplicationContext(context);
        return springContextUtil;
    }

}
