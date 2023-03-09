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
package com.sdeven.sparrow.api.controller;

import com.sdeven.sparrow.commons.base.commons.result.Result;
import com.sdeven.sparrow.commons.base.commons.result.Results;
import com.sdeven.sparrow.example.sdk.params.TestCmd;
import com.sdeven.sparrow.example.sdk.params.TestQmd;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * a example controller
 * @author sdeven
 */
@RestController
@RequestMapping("/example")
public class ExampleController {
    /**
     * test
     * @param #cmd {@link TestQmd}
     * @return {@link <TestCmd>}
     */
    @GetMapping (value = "")
    public Result<TestCmd> test(@Valid @RequestBody TestCmd cmd) {

        return Results.success(cmd);
    }

}
