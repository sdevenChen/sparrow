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
package com.sdeven.sparrow.api;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.sdeven.sparrow.starter.autoconfig.CustomImport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetAddress;
@Slf4j
@CustomImport({
        //web
        ServletWebServerFactoryAutoConfiguration.class,
        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        AopAutoConfiguration.class,
        //web end
//        RedisAutoConfiguration.class,
//        RedissonAutoConfiguration.class,
//        MybatisPlusAutoConfiguration.class,
})
@EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
@MapperScan(basePackages = {"com.sdeven.sparrow.infras.*.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
@ComponentScan(value = "com.sdeven.sparrow")
public class CustomApplication implements ApplicationListener<WebServerInitializedEvent> {

    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "Local: \t\thttp://{}:{}\n\t" +
                        "----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                event.getWebServer().getPort());
    }
}
