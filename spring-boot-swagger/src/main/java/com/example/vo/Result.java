package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Result")
public class Result<T> {

    @ApiModelProperty(value = "code")
    private int code;

    @ApiModelProperty(value = "bean")
    private T bean;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", bean=" + bean +
                '}';
    }
}
