package com.miaoshaproject.service;

import com.miaoshaproject.error.BuException;
import com.miaoshaproject.service.model.OrderModel;

/**
 * Author:subi
 * Time:2019/11/14 15:20
 **/
public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BuException;
}
