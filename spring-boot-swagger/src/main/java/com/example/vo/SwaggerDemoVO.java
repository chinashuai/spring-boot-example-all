package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SwaggerDemoVO")
public class SwaggerDemoVO {

    @ApiModelProperty(value = "aaa")
    private String aaa;

    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    @Override
    public String toString() {
        return "SwaggerDemoVO{" +
                "aaa='" + aaa + '\'' +
                '}';
    }
}
