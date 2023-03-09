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
package com.sdeven.sparrow.starter.dubbo.provider.valida;

import com.sdeven.sparrow.commons.base.commons.exception.ErrorCode;
import com.sdeven.sparrow.commons.base.commons.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * Create a digital verifier for consumer-server participation to reduce false debugging IOs
 * @author sdeven
 * @since 1.0.0
 */
@Slf4j
@Activate(group = CommonConstants.CONSUMER, order = 9999)
public class ValidationFilter implements Filter {
    private static ExecutableValidator ev = Validation.buildDefaultValidatorFactory().getValidator().forExecutables();
    
	@Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        Method method = getMethod(invoker, invocation);
        if (Objects.nonNull(method)) {
            Class<?> inf = invoker.getInterface();
            Object bean = getBean(inf);
            if(bean != null) {
            	Object[] paramList = invocation.getArguments();
                Set<ConstraintViolation<Object>> constraintViolations = ev.validateParameters(bean, method, paramList);
                com.sdeven.sparrow.commons.base.commons.result.Result<Object> validationResult = getValidationResult(constraintViolations);
                if (Objects.nonNull(validationResult) && Objects.nonNull(validationResult.getData())) {
                    return AsyncRpcResult.newDefaultAsyncResult(validationResult, null, invocation);
                }
            }
        }
        return invoker.invoke(invocation);
    }
	
	private Object getBean(Class<?> clazz) {
		Set<ApplicationContext> contexts = SpringExtensionFactory.getContexts();
		for(ApplicationContext context: contexts) {
			ObjectProvider<?> provider = context.getBeanProvider(clazz);
			Object bean = provider.getIfAvailable();
			if(bean != null) return bean;
		}
		return null;
	}

    /**
     * Get the calibration method
     */
    private static Method getMethod(Invoker<?> invoker, Invocation invocation) {
        Method[] methods = invoker.getInterface().getDeclaredMethods();
        invocation.getArguments();
        for (Method m : methods) {
            boolean needCheck = m.getName().equals(invocation.getMethodName()) && invocation.getArguments().length == m.getParameterCount();
            boolean matchstatus = matchMethod(invocation.getParameterTypes(), m.getParameterTypes());
            if (needCheck && matchstatus) {
                return m;
            }
        }
        return null;
    }

    /**
     * Methods for getting matches
     */
    private static boolean matchMethod(Class[] invokerMethodParamClassList, Class[] matchMethodParamClassList) {
        if(invokerMethodParamClassList.length != matchMethodParamClassList.length){
            return false;
        }
        for (int i = 0; i < invokerMethodParamClassList.length; i++) {
            if (!invokerMethodParamClassList[i].equals(matchMethodParamClassList[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the calibration result object
     */
    private static <T> com.sdeven.sparrow.commons.base.commons.result.Result<T> getValidationResult(Set<ConstraintViolation<T>> set) {
        if (set != null && !set.isEmpty()) {
            for (ConstraintViolation<T> violation : set) {
                return Results.fail(ErrorCode.VALIDATE_FAIL, violation.getMessage());
            }
        }
        return null;
    }

}
