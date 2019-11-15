package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.error.BuException;
import com.miaoshaproject.error.EnumBuError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Author:subi
 * Time:2019/11/14 15:23
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BuException {
        //校验下单状态：下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(itemModel == null){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        if(amount<=0 || amount >99){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"购买数量不正确");
        }
        //落单减库存
        boolean orderReturn = itemService.decreaseStock(itemId,amount);
        if(!orderReturn){
            throw new BuException(EnumBuError.STOCK_NOT_ENOUGH);
        }
        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setAmount(amount);
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal((amount))));
        //生成交易订单号
        orderModel.setId(generateOrderId());
        //写入数据库
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //商品销量增加
        itemService.increaseSales(itemId,amount);

        //返回前端
        return orderModel;
    }

    //保证Sql不受后面的SQL语句事务的影响，propagation = Propagation.REQUIRES_NEW让她继续提交
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderId(){
        //订单号为16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息
        LocalDateTime now = LocalDateTime.now();
        String nowdate = now.format((DateTimeFormatter.ISO_DATE)).replace("-","");
        stringBuilder.append(nowdate);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        for(int i = 0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后两位为分库分表位
        stringBuilder.append("bi");

        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
