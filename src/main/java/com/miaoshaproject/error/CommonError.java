package com.miaoshaproject.error;

/**
 * Author:subi
 * Time:2019/11/9 14:35
 **/
public interface CommonError {
    public int getErrorCode();
    public String getErrorMessage();

    CommonError setErrorMessage(String errMessage);
}
