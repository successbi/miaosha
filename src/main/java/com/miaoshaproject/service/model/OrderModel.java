package com.miaoshaproject.service.model;

import java.math.BigDecimal;

/**
 * Author:subi
 * Time:2019/11/14 10:59
 **/
public class OrderModel {
    //交易号:是一个有意义的含义的交易
    private String id;
    //用户id
    private Integer userId;
    //商品
    private Integer itemId;
    //购买商品的单价
    private BigDecimal itemPrice;
    //数量
    private Integer amount;
    //金额
    private BigDecimal orderPrice;


    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice(){
    return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
