package cn.appsys.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.BackMapper;
import cn.appsys.pojo.BackendUser;

@Service("backendService")
public class BackendServiceimpl implements BackendService {
	@Resource(name = "backMapper")
	public BackMapper backMapper;

	public BackendUser getBackendLogin(String userCode, String userPassword) {
		BackendUser backend = null;
		try {
			backend = backMapper.getBackendLogin(userCode);
		}catch (Exception e) {
			e.printStackTrace();
		}
		// 匹配密碼
		if (null != backend) {
			if (!backend.getUserPassword().equals(userPassword)) {
				backend = null;
			}
		}
		return backend;
	}
}
