package com.miaoshaproject.error;

/**
 * Author:subi
 * Time:2019/11/9 14:54
 **/

//包装器业务异常的实现
public class BuException extends Exception implements CommonError {

    private CommonError commonError;

    //直接接受EnumBuError的传参用于狗仔业务异常
    public BuException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接受自定义的errMessage的方式构造业务异常
    public BuException(CommonError commonError,String message){
        super();
        this.commonError = commonError;
        commonError.setErrorMessage(message);
    }






    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return this.commonError.getErrorMessage();
    }

    @Override
    public CommonError setErrorMessage(String errMessage) {
        this.commonError.setErrorMessage(errMessage);
        return this;
    }
}
