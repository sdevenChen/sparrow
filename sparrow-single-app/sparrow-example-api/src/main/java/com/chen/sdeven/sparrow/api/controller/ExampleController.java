package com.chen.sdeven.sparrow.api.controller;

import com.chen.sdeven.sparrow.commons.base.commons.result.Result;
import com.chen.sdeven.sparrow.commons.base.commons.result.Results;
import com.chen.sdeven.sparrow.example.sdk.params.TestCmd;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 示例 前端控制器
 * </p>
 *
 * @author sdeven
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    /**
    * @Description 保存
     *
    * @param: TestQmd
    * @return {@link <TestCmd>}
    * @Date 2022-01-14
    */
    @GetMapping (value = "")
    public Result<TestCmd> saveAggregation(@Valid @RequestBody TestCmd cmd) {

        return Results.success(cmd);
    }

}
