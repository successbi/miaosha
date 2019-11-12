package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BuException;
import com.miaoshaproject.error.EnumBuError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author:subi
 * Time:2019/11/8 14:38
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdomapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            return null;
        }
        //通过用户id获取用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO,userPasswordDO);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BuException {
        if(userModel==null){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//                ||userModel.getAge()==null
//                ||userModel.getGender()==null
//                ||StringUtils.isEmpty(userModel.getTelephone())
//        ){
//            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validata(userModel);
        if(result.isHasErrors()){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        new UserDO();
        UserDO userDO;
        new UserPasswordDO();
        UserPasswordDO userPasswordDO;

        //实现model->dataobject的方法
        userDO = convertToUserDO(userModel);

        //防止手机号码重复使用
        try {
            userDOMapper.insertSelective(userDO);

        }catch (DuplicateKeyException ex){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"手机号码已经被注册");
        }



        userPasswordDO = convertToPasswordDO(userModel,userDO);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BuException {
        //通过用户的手机号码从数据库获取到用户的信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO==null){
            throw new BuException(EnumBuError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输来地密码匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BuException(EnumBuError.USER_LOGIN_FAIL);
        }
        return userModel;
    }


    private UserDO convertToUserDO(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }

    private UserPasswordDO convertToPasswordDO(UserModel userModel,UserDO userDO){
        if(userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserid(userDO.getId());

        return userPasswordDO;

    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null){
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
