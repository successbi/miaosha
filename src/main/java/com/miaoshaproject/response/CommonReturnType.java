package com.miaoshaproject.response;

/**
 * Author:subi
 * Time:2019/11/9 14:10
 **/
public class CommonReturnType {

    //blic static CommonReturnType creat;
    //表明对应请求的对应返回结果（success or fail）
    private String status;
    //返回的数据类
    //若status=success，则data返回前端需要的json数据
    //若为fail，则返回通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType creat(Object result){
        return CommonReturnType.creat(result,"success");
    }

    public static CommonReturnType creat(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
