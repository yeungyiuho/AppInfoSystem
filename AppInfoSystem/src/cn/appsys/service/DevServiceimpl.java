package cn.appsys.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.DevMapper;
import cn.appsys.pojo.DevUser;

@Service("devService")
public class DevServiceimpl implements DevService {
	@Resource(name = "devMapper")
	public DevMapper devMapper;

	@Override
	public DevUser getDevUserLogin(String devCode, String devPassWord) {
		DevUser dev = null;
		try {
			dev = devMapper.getDevUserLogin(devCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != dev) {
			if (!dev.getDevPassword().equals(devPassWord)) {
				dev = null;
			}
		}
		return dev;
	}
}
