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
package com.sdeven.sparrow.starter.dubbo.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.sdeven.sparrow.commons.base.commons.exception.BizServiceException;
import com.sdeven.sparrow.commons.base.commons.exception.CommonError;
import com.sdeven.sparrow.starter.dubbo.util.LogingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;


/**
 * Consumer-side custom Dubbo exception handling to catch consumer-side exception issues, such as timeout exceptions,
 * no-provider service exceptions and transferring custom result classes
 * @author sdeven
 * @since  1.0.0
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER, order = -20000)
public class ConsumerExceptionFilter implements Filter, Filter.Listener {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //Print pre-call log
        LogingUtil.doLogBefore(invoker, invocation);
        Result result = null;
        try {
            result = invoker.invoke(invocation);
        } finally {
            //Print post-call log
            LogingUtil.doLogAfter(invoker, (result == null ? result : result.getValue()), invocation);
        }
        return result;
    }


    @Override
    public void onResponse(Result provider, Invoker<?> invoker, Invocation invocation) {

        boolean isGenericException = provider.hasException()
                                    && GenericService.class == invoker.getInterface() &&
                                    (provider.getException() instanceof com.alibaba.dubbo.rpc.service.GenericException
                                    || provider.getException() instanceof GenericException);
        if (isGenericException) {
            Map<String,Object> exception = BeanUtil.beanToMap(provider.getException());
            String exceptionClass = (String) exception.get("exceptionClass");
            String exceptionMessage = (String) exception.get("exceptionMessage");
            provider.setException(new BizServiceException(new CommonError() {
                @Override
                public int getCode() {
                    return Integer.parseInt(exceptionMessage);
                }

                @Override
                public String getMessage() {
                    return "Exception in generalized service execution class by ".concat(exceptionClass);
                }
            }));
        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

    }
}

