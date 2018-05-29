package cn.appsys.service;


import cn.appsys.pojo.DevUser;

public interface DevService {
public DevUser getDevUserLogin(String devCode,String devPassWord);//登陆
}
