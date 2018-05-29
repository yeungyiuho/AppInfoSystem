package cn.appsys.dao;


import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevMapper {
public DevUser getDevUserLogin(@Param("devCode")String devCode);//登陆
}
