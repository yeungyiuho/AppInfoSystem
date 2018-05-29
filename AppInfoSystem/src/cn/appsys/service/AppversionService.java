package cn.appsys.service;

import java.util.List;


import cn.appsys.pojo.AppVersion;

public interface AppversionService {
public boolean addVersion(AppVersion av);//新增
public List<AppVersion> getAppVersionId(Integer id);

public AppVersion getVerSionId(Integer id);

public boolean updateVersion(AppVersion appVersion);//修改
}
