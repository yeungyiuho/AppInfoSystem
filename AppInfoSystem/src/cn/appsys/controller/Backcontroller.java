package cn.appsys.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.BackendService;
import cn.appsys.tools.Constants;


@Controller
@RequestMapping("/Backend")
public class Backcontroller {
	private Logger log=Logger.getLogger(Backcontroller.class);
	@Resource(name="backendService")
	public BackendService backendService;
	
	//跳转页面
	@RequestMapping(value="/backendlogin")
	public String login()
	{
		log.info("进入后台管理===========");
		return "/backendlogin";
	}
	//登陆
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,HttpSession session,HttpServletRequest request)
	{
		log.info("doLogin===============");
		log.info(userCode+"=========="+userPassword);
		BackendUser backend=backendService.getBackendLogin(userCode, userPassword);
	
		if(null!=backend)
		{
				session.setAttribute(Constants.USER_SESSION,backend);
				return "backend/main";
			
		}else{
			request.setAttribute("error","账号或密码不对");
			return "/backendlogin";
		}
	}
	//注销
	@RequestMapping(value="/loginOut")
	public String loginOut(HttpSession session)
	{
		session.removeAttribute(Constants.USER_SESSION);
		return "redirect:../index.jsp";
	}
}
