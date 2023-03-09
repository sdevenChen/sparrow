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
package com.sdeven.sparrow.starter.dubbo.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

/**
 * rpc log processing
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
public class LogingUtil {
    private static final String BEFORE_LOG_MSG = "Calling remote service[{}.{}][{}].\nSent request:{}.";
    private static final String AFTER_LOG_MSG = "Called remote service[{}.{}].\n[{}]";
    /**
     * Print Before Calling
     */
    public static void doLogBefore(Invoker<?> invoker, Invocation invocation) {
        log.info(BEFORE_LOG_MSG, invoker.getInterface().getSimpleName(), invocation.getMethodName(),
                invoker.getUrl().getAddress(), invocation.getArguments());
    }

    /**
     * Print after call
     */
    public static void doLogAfter(Invoker<?> invoker, Object response, Invocation invocation) {
        //get class name
        String clazzName = invoker.getInterface().getSimpleName();
        //get method name
        String methodName = invocation.getMethodName();
        log.info(AFTER_LOG_MSG, clazzName, methodName, response == null ? null : JSONUtil.toJsonStr(response));
    }
}
