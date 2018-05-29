package cn.appsys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppInfoMapper;
import cn.appsys.pojo.AppInfo;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoMapper mapper;

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId, Integer devId, Integer currentPageNo,
			Integer pageSize) throws Exception {
		return mapper.getAppInfoList(querySoftwareName, queryStatus,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				queryFlatformId, devId, (currentPageNo - 1) * pageSize,
				pageSize);
	}

	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId)
			throws Exception {
		return mapper.getAppInfoCount(querySoftwareName, queryStatus,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				queryFlatformId, devId);
	}

	@Override
	public AppInfo getAppinfoId(String id) {
		AppInfo info = null;
		try {
			info = mapper.getAppinfoId(id);
		} catch (Exception e) {
			e.printStackTrace();
			info = null;
		}
		return info;
	}

	@Override
	public boolean addAppinfo(AppInfo in) {
		boolean flag = false;
		try {
			if(mapper.addAppinfo(in)> 0)
			
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("rollback==================");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return flag;
	}

	@Override
	public AppInfo getAppFind(Integer id, String apkName) {
		return mapper.getAppFind(id, apkName);
	}

	@Override
	public boolean deleteAppinfo(int id) {
		boolean falg=false;
		if(mapper.deleteAppVersion(id)>0){
			if(mapper.deleteAppinfo(id)==1){
				falg=true;
			}
		}
		return falg;
	}

	@Override
	public boolean deleteAppVersion(String id) {
		boolean falg=false;
		if(mapper.deleteAppVersion(Integer.parseInt(id))==1){
			falg=true;
		}
		return falg;
	}

	@Override
	public boolean updateAppinfo(AppInfo info) {
		boolean falg=false;
		try {
			if(mapper.updateAppinfo(info)>0){
				falg=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return falg;
	}

	@Override
	public boolean updateStatus(Integer appid, Integer statu) {
		boolean falg=false;
		if(mapper.updateStatus(appid, statu)==1){
			falg=true;
		}
		return falg;
	}
	}
