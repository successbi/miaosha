package com.miaoshaproject.controller.viewobject;

/**
 * Author:subi
 * Time:2019/11/8 16:08
 **/
public class UserVO {

    //去除无用信息和保护信息（密码）的数据模型
    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String telephone;


}
