package com.sdeven.sparrow.starter.nodeflow.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description SpringContextUtil
 * @Author sdeven.chen.dongwei@gmail.com
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    @Getter
    @Setter
    private  ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


}
