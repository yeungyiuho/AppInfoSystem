package cn.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {

	public List<AppInfo> getAppInfoList(
			@Param(value = "softwareName") String querySoftwareName,
			@Param(value = "status") Integer queryStatus,
			@Param(value = "categoryLevel1") Integer queryCategoryLevel1,
			@Param(value = "categoryLevel2") Integer queryCategoryLevel2,
			@Param(value = "categoryLevel3") Integer queryCategoryLevel3,
			@Param(value = "flatformId") Integer queryFlatformId,
			@Param(value = "devId") Integer devId,
			@Param(value = "from") Integer currentPageNo,
			@Param(value = "pageSize") Integer pageSize) throws Exception;

	public int getAppInfoCount(
			@Param(value = "softwareName") String querySoftwareName,
			@Param(value = "status") Integer queryStatus,
			@Param(value = "categoryLevel1") Integer queryCategoryLevel1,
			@Param(value = "categoryLevel2") Integer queryCategoryLevel2,
			@Param(value = "categoryLevel3") Integer queryCategoryLevel3,
			@Param(value = "flatformId") Integer queryFlatformId,
			@Param(value = "devId") Integer devId) throws Exception;

	public AppInfo getAppinfoId(@Param("id") String id) throws Exception;
	public int addAppinfo(AppInfo in);
	public AppInfo getAppFind(@Param("id")Integer id,@Param("apkName")String apkName);
	public int updateVersionId(@Param("VerSionId")Integer verSionId,@Param("pid")Integer id);
	public int deleteAppinfo(@Param("id")Integer id);
	public int deleteAppVersion(@Param("appid")Integer appid);
	public int updateAppinfo(AppInfo info);//修改app信息
	public int updateStatus(@Param("appid")Integer id,@Param("statu")Integer statu);//修改状态
}
