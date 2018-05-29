package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.DevService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/DevUser")
public class Devcontroller {
	private Logger log = Logger.getLogger(Backcontroller.class);
	@Resource(name = "devService")
	public DevService devService;

	@RequestMapping(value = "/devlogin")
	public String login() {
		log.info("进入开发者=========");
		return "/devlogin";
	}
	
	//跳转到App信息管理页面
	@RequestMapping(value="/appinfolist")
	public String Appfo()
	{
		log.info("进入到App信息管理页面");
		return "developer/appinfolist";
	}

	// 登陆
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public String doLogin(@RequestParam String devCode,
			@RequestParam String devPassword, HttpSession session,
			HttpServletRequest request) {
		log.info("doLogin===============");
		log.info(devCode + "==========" + devPassword);
		DevUser dev = devService.getDevUserLogin(devCode, devPassword);
	
		if (null != dev) {
			session.setAttribute(Constants.DEV_USER_SESSION, dev);
			return "developer/main";
		} else {
			request.setAttribute("error", "账号或密码错误");
			return "/devlogin";
		}
	}

	// 注销
	@RequestMapping(value = "/loginOut")
	public String loginOut(HttpSession session) {
		session.removeAttribute(Constants.USER_SESSION);
		return "redirect:../index.jsp";
	}
}
