package com.miaoshaproject.service;

import com.miaoshaproject.error.BuException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

/**
 * Author:subi
 * Time:2019/11/12 20:07
 **/
public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BuException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount) throws BuException;

    //商品销量增加
    void increaseSales(Integer itemId,Integer amount)throws  BuException;
}
