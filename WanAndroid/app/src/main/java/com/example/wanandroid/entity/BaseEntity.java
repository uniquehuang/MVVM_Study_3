package com.example.wanandroid.entity;

/**
 * @author dengfeng
 * @data 2023/4/11
 * @description 解析实体基类
 */
public class BaseEntity<T> {

    private static int SUCCESS_CODE = 0;//成功的code

    private int errorCode;  //错误码为-1，成功为0

    private String errorMsg;
    private T data;




    public boolean isSuccess() {
        return getErrorCode() == SUCCESS_CODE;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
