package cn.appsys.dao;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackMapper {
public BackendUser getBackendLogin(@Param("userCode")String userCode);//后台管理登陆
}
