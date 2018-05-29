package cn.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppversionMapper {
	public int addVersion(AppVersion ve);// 新增版本

	public List<AppVersion> getAppVersionId(@Param("appId") Integer appId);//根据appid
	public AppVersion getVerSionId(@Param("id")Integer id);//根据版本id
	public int updateVersion(AppVersion in);//修改版本
}
