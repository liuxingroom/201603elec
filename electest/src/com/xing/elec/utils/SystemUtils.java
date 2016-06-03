package com.xing.elec.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.elec.domain.ElecUser;

public class SystemUtils implements Filter {

	
	/**在登录之前，有些URL是没有Session的，查找到这些URL，统一进行控制，相当于过滤器中不包含这些方法和连接*/
	List<String> list=new ArrayList<String>();
	
	/**web容器初始化*/
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		//没有session，蛋需要放行的页面
		list.add("/index.jsp");
		list.add("/image.jsp");
		list.add("/system/elecMenuAction_menuHome.do");
		
		//5秒 倒计时的页面
		list.add("/error.jsp");
		list.add("/system/elecMenuAction_logout.do");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		/**将request  response 转换一下*/
		HttpServletRequest  httpServletRequest=(HttpServletRequest)request; 
		HttpServletResponse httpServletResponse=(HttpServletResponse)response;
		//获取访问的Servlet路径
		String path=httpServletRequest.getServletPath();
		//在跳转到index.jsp 页面之前，先要获取Cookie ，传值的方式个index.jsp
		this.initIndexPage(httpServletRequest,path);
		
		/**添加粗颗粒权限控制*/
		//没有session但需要放行的连接
		if(list.contains(path)){
			//放行
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		//判断Session是否存在
		ElecUser elecUser=(ElecUser) httpServletRequest.getSession().getAttribute("globle_user");
		//Session中存在数据，放行，指定对应的URL的页面
		if(elecUser!=null){
			//放行
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		
		/**返回登陆界面的第一方式*/
		//如果Session中不存在数据，重定向到index.jsp的页面上
		//httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/index.jsp");
		/**返回登陆界面的第二种方式*/
		//如果Session中不存在数据，重定向到error.jsp的页面上
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/error.jsp");
	}

	/**在跳转到index.jsp页面之前，先获取Cookie，传值的方式给index.jsp*/
	private void initIndexPage(HttpServletRequest request, String path) {
		// TODO Auto-generated method stub
		if(path!=null && path.equals("/index.jsp")){
			//获取Cookie中存放的用户名和密码
			//用户名
			String name="";
			//密码
			String password="";
			//复选框
			String checked="";
			//从Cookie张获取记住我的中存放登录名和密码
			Cookie [] cookies=request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(Cookie cookie:cookies){
					if("name".equals(cookie.getName())){
						//用户名
						name=cookie.getValue();
						//中文解码 
						try {
							name=URLDecoder.decode(name, "utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//复选框选中
						checked="checked";
					}
					if("password".equals(cookie.getName())){
						//密码
						password=cookie.getValue();
					}
				}
			}
			//存放到request中(为了回显登陆名  已经登录密码)
			request.setAttribute("name", name);
			request.setAttribute("password", password);
			request.setAttribute("checked", checked);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
