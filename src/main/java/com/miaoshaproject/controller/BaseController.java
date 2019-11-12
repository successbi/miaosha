package com.miaoshaproject.controller;

import com.miaoshaproject.error.BuException;
import com.miaoshaproject.error.EnumBuError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:subi
 * Time:2019/11/9 15:37
 **/
public class BaseController {

    public static final String CONTENT_TYPE="application/x-www-form-urlencoded";

    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if(ex instanceof BuException){
            BuException buException = (BuException)ex;
            responseData.put("errCode",buException.getErrorCode());
            responseData.put("errMessage",buException.getErrorMessage());
        } else{
            responseData.put("errCode",EnumBuError.NOT_KNOW_ERROR.getErrorCode());
            responseData.put("errMessage", EnumBuError.NOT_KNOW_ERROR.getErrorMessage());
        }

        return CommonReturnType.creat(responseData,"fail");
    }
}
