package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;



/**
 * 拦截用户请求,用来判断用户是否已登录
 */
public class SysInterceptor extends HandlerInterceptorAdapter{
	/**
	 * 判断是否登录
	 */
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		HttpSession session = request.getSession();
		DevUser user = (DevUser)session.getAttribute(Constants.USER_SESSION);
		BackendUser buser=(BackendUser) session.getAttribute(Constants.USER_SESSION);
		if (buser==null) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		if(user==null){
			//未登录
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}
