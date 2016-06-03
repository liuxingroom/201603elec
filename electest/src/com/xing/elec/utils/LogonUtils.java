package com.xing.elec.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;

public class LogonUtils {

	/**验证码*/
	public static boolean checkNumber(HttpServletRequest request) {
		// TODO Auto-generated method stub
		//获取页面的验证码
		String imageNumber=request.getParameter("checkNumber");
		if(StringUtils.isBlank(imageNumber)){
			return false;
		}
		//从session中获取生成的验证码
		String CHECK_NUMBER_KEY=(String) request.getSession().getAttribute("CHECK_NUMBER_KEY");
		if(StringUtils.isBlank(CHECK_NUMBER_KEY)){
			return false;
		}
		return imageNumber.equalsIgnoreCase(CHECK_NUMBER_KEY);
	}

	/**记住我*/
	public static void remeberMe(String name, String password,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		//1.创建2个cookie，分别存放用户户名和密码
		//Cookie中不能存放中文
		try {
			name=URLEncoder.encode(name,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Cookie nameCookie=new Cookie("name", name);
		Cookie passwordCookie=new Cookie("password",password);
		//2.判断页面中复选框中是否被选中，选中设置，不选中就不设置
		String remeberMe=request.getParameter("remeberMe");
		//设置路径
		nameCookie.setPath(request.getContextPath());
		passwordCookie.setPath(request.getContextPath());
		if(remeberMe!=null && remeberMe.equals("yes")){
			//设置cookie存放时间
			nameCookie.setMaxAge(7*24*60*60);//一周
			passwordCookie.setMaxAge(7*24*60*60);//一周
		}else{
			nameCookie.setMaxAge(0);
			passwordCookie.setMaxAge(0);
		}
		//3.精cookie对象存放到response对象
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);
		
	}
	
}
