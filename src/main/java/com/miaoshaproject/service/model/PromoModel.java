package com.miaoshaproject.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Author:subi
 * Time:2019/11/14 21:50
 **/
public class PromoModel {
    private Integer id;

    //秒杀活动名称
    private String promoName;

    private DateTime startDate;

    private Integer itemId;

    private BigDecimal promoPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }
}
