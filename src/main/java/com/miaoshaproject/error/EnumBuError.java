package com.miaoshaproject.error;

/**
 * Author:subi
 * Time:2019/11/9 14:37
 **/
public enum EnumBuError implements CommonError{
    //通用的错误类型88888X
    PARAMETER_VALIDATION_ERROR(888881,"参数不合法"),

    NOT_KNOW_ERROR(888882,"未知错误"),

    //1000开头为用户信息相关错误定义
    USER_NOT_EXIST(10001,"用户不存在"),

    USER_LOGIN_FAIL(10002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(10003,"用户未登录"),

    //交易信息错误为20000开头
    STOCK_NOT_ENOUGH(20001,"库存不足"),

    ;

    private int errCode;
    private String errMessage;

    private EnumBuError(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }


    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errMessage;
    }


    @Override
    public CommonError setErrorMessage(String errMessage) {
        this.errMessage = errMessage;
        return this;
    }
}
