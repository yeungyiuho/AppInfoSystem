package cn.appsys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppInfoMapper;
import cn.appsys.dao.AppversionMapper;
import cn.appsys.pojo.AppVersion;
@Service
public class AppversionServiceimpl implements AppversionService {
@Resource
public AppversionMapper mapper;
@Resource
public AppInfoMapper info;

	@Override
	public boolean addVersion(AppVersion av) {
		boolean falg=false;
		try {
			if(mapper.addVersion(av)>0){
				if(info.updateVersionId(av.getId(),av.getAppId())==1){
					falg=true;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				System.out.println("rollback==================");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return falg;
	}

	@Override
	public List<AppVersion> getAppVersionId(Integer id) {
		return mapper.getAppVersionId(id);
	}

	@Override
	public AppVersion getVerSionId(Integer id) {
		return mapper.getVerSionId(id);
	}

	@Override
	public boolean updateVersion(AppVersion in) {
			boolean falg=false;
			try {
				if(mapper.updateVersion(in)>0){
					falg=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return falg;
	}

}
