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
package com.sdeven.sparrow.starter.dubbo.provider;


import com.sdeven.sparrow.commons.context.ContextConst;
import com.sdeven.sparrow.commons.context.system.SystemContext;
import com.sdeven.sparrow.commons.context.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * rpc Custom Context Passing by provider
 *
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = -2000)
public class ProviderContextFilter implements Filter {
    public static UserContext userContext;

    public static SystemContext systemContext;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isProvider = RpcContext.getContext().isProviderSide();
        //Injecting custom contexts into providers
        if (isProvider) {
            userContext = (UserContext) RpcContext.getContext().getObjectAttachment(ContextConst.USER_CONTEXT);
            systemContext = (SystemContext) RpcContext.getContext().getObjectAttachment(ContextConst.SYSTEM_CONTEXT);
        }
        return invoker.invoke(invocation);
    }

}
