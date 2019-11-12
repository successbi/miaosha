package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BuException;
import com.miaoshaproject.error.EnumBuError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * Author:subi
 * Time:2019/11/8 14:33
 **/
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户获得OTP短信的接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType getODP(@RequestParam(name="telephone")String telephone) throws BuException{

        //利用随机数的方式获取到OTP验证码：
        Random random = new Random();
        int rand = random.nextInt(99999);
        rand = rand+10000;
        String otpcode = String.valueOf(rand);
        //构建OTP验证码和手机号的对应关系,使用Httpsession的方式绑定他的手机号与otpcode
        httpServletRequest.getSession().setAttribute(telephone,otpcode);

        //发送otp验证码到对应的手机号上(可以调用其他的接口，以后学习)
        System.out.println("telephone:"+telephone+"  "+"otpcode:"+otpcode);


        return CommonReturnType.creat(null);
    }

    //用户登陆接口
    @RequestMapping(value="/login",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone")String telephone,
                                  @RequestParam(name="password")String password
                                  ) throws BuException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone)|| StringUtils.isEmpty(password)){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"登录账号和密码不能为空");
        }
        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登陆成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.creat(null);

    }



    //用户注册接口
    @RequestMapping(value="/register",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpcode")String otpcode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Byte gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="encrptPassword")String encrptPassword
                                    ) throws BuException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和对应的验证码是否相符合
        String inSessionOdpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpcode,inSessionOdpCode)){
            throw new BuException(EnumBuError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setEncrptPassword(this.EncodeByMd5(encrptPassword));
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setName(name);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byTelephone");

        userService.register(userModel);

        return CommonReturnType.creat(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return  newstr;
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BuException {
        //调用service服务获取对应id的用户对象并且返回给前端
        UserModel userModel = userService.getUserById(id);
        //若获取的用户信息不存在
        if(userModel==null){
            //userModel.setEncrptPassword("10000");
            throw new BuException(EnumBuError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromUserModel(userModel);
        CommonReturnType commonReturnType = CommonReturnType.creat(userVO);

        return commonReturnType;
    }

    private UserVO convertFromUserModel(UserModel userModel){

        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

}
