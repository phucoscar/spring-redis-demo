package com.aml.base;


import com.aml.enums.ResultEnum;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 11L;
    private T data = null;
    private String msg = null;
    private Integer code = ResultEnum.FAIL.getCode();

    public Result(){}

    public Result(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public Result(Integer code, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public static <T> Result<T> success() {
        return new Result<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc());
    }

    public static <T> Result<T> fail() {
        return new Result<T>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getDesc());
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>(ResultEnum.FAIL.getCode(), msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<T>(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
