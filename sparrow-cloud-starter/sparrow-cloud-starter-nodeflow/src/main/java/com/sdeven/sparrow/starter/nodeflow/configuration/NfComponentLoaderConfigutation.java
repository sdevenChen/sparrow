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
 * @Description NfComponent 扩展组件过滤加载器
 * @Author sdeven
 */
@Configuration
@ComponentScan(basePackages = "com.sdeven.sparrow",includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NfComponent.class))
public class NfComponentLoaderConfigutation implements ApplicationListener<ContextRefreshedEvent> {
    @Setter
    private  ApplicationContext context;

    /**
     * @Description  扩展实现组件bean扫描注册到Nodeflow
     * @Date 12/11/20 10:08
     * @Param ContextRefreshedEvent
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
