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
package com.chen.sdeven.sparrow.commons.context.system;

import lombok.Data;

import java.util.Map;

@Data
public class SystemContext {
    //是否灰度环境
    private Boolean isGrayscale;

    //接口超时时间配置， key(class.method string): ms
    private Map<String,Integer> rpcInterfaceTime;

    //来源,中台、消息、三方
    private Character client;
}
