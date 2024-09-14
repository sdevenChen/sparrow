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
package com.sdeven.sparrow.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@Slf4j
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CustomApplication.class, args);
        printBeanInfo(context);

    }

    private static void printBeanInfo(ConfigurableApplicationContext ctx) {
        StringBuilder allBeanNameStr = new StringBuilder(500);
        allBeanNameStr.append("----------------- spring beans ------------------------\n");
        String[] beanNames = ctx.getBeanDefinitionNames();
        allBeanNameStr.append("beanNames count：" + beanNames.length + "\n");
        Arrays.sort(beanNames);
        for (String bn : beanNames) {
            allBeanNameStr.append(bn + "\n");
        }
        allBeanNameStr.append("------------------ end -----------------------");
        log.info(allBeanNameStr.toString());
    }
}
