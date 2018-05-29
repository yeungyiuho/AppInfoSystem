package cn.appsys.service;

import cn.appsys.pojo.BackendUser;

public interface BackendService {
public BackendUser getBackendLogin(String userCode,String userPassword);//后台管理登陆
}
