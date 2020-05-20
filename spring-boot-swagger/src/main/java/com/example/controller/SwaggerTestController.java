package com.example.controller;

import com.example.vo.Result;
import com.example.vo.SwaggerDemoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwaggerTestController {

    @RequestMapping(value = "/api/test/getTest", method = RequestMethod.POST)
    @ApiOperation(value = "swagger示例test1", notes = "swagger示例test2")
    public Result<SwaggerDemoVO> getCount(
            @ApiParam(required = true, name = "swaggerDemoVO", value = "入参为swaggerDemoVO对象") @RequestBody SwaggerDemoVO swaggerDemoVO,
            @ApiParam(required = true, name = "id", value = "id值") @RequestParam Long id
    ) {
        return null;
    }


}
