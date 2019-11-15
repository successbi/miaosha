package com.miaoshaproject.controller;

import com.miaoshaproject.error.BuException;
import com.miaoshaproject.error.EnumBuError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:subi
 * Time:2019/11/14 16:41
 **/
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value="/createorder",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount) throws BuException {

        //获取用户的登录信息，从而得到userid
        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BuException(EnumBuError.USER_NOT_LOGIN,"用户未登录,不能下单");
        }
        //获取用户登录的信息
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,amount);

        return CommonReturnType.creat(null);
    }
}
