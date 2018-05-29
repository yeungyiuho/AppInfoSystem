package cn.appsys.service;

import java.util.List;


import cn.appsys.pojo.AppInfo;

public interface AppInfoService {

	



	/**
	 * 根据条件查询出app列表
	 * 
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param devId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId, Integer devId, Integer currentPageNo,
			Integer pageSize) throws Exception;

	/**
	 * 根据条件查询appInfo表记录数
	 * 
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param devId
	 * @return
	 * @throws Exception
	 */
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId)
			throws Exception;
	/**
	 * 根據id查询app信息
	 */
	public AppInfo getAppinfoId(String id);
	/**
	 * 新增
	 */
	public boolean addAppinfo(AppInfo in);
	
	public AppInfo getAppFind(Integer id,String apkName);
	public boolean deleteAppinfo(int id);
	public boolean deleteAppVersion(String id);
	
	public boolean updateAppinfo(AppInfo info);
	public boolean updateStatus(Integer appid,Integer statu);//修改状态
}
