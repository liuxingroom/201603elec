package com.xing.elec.utils;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.service.ElecRoleService;


@SuppressWarnings("serial")
public class errorAndLimitInerceptor extends MethodFilterInterceptor{

	/**拦截器*/
	@SuppressWarnings("unused")
	@Override
	protected String doIntercept(ActionInvocation actioninvocation) throws Exception {
		// TODO Auto-generated method stub
		//把自定义错误信息  放置到request中
		HttpServletRequest request=(HttpServletRequest) actioninvocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		try{
			
			//获取请求的action对象
			Object action=actioninvocation.getAction();
			//获取请求方法的名称
			String methodName=actioninvocation.getProxy().getMethod();
			//获取action中的方法的封装类（action中的方法没有参数）
			Method method=action.getClass().getMethod(methodName, null);
			//action 的返回值
			String result=null;
			
			//在完成跳转Action之前完成细颗粒权限控制，控制Action的每个方法 
			//检查注解，是否可以操作权限的URl
//			boolean flag=isCheckLimit(request,method);
			if(true){
				//运行被拦截的Action,期间如果发生异常会被catch
				result=actioninvocation.invoke();
				
			}else{
				request.setAttribute("errorMsg", "对不起！您没有权限操作此功能！");
				return "errorMsg";
			}
			return result;
		}catch (Exception e) {
			/**
			 * 处理异常
			 */
			String errorMsg="出现错误信息，请查看日志!";
			//通过instanceof 判断到底是什么异常数据类型
			if(e instanceof RuntimeException){
				//未知的运行异常
				RuntimeException re=(RuntimeException)e;
				re.printStackTrace();
				errorMsg=re.getMessage().trim();
			}
			/**
			 * 发送错误消息到页面
			 */
			request.setAttribute("errorMsg", errorMsg);
			/**
			 *log4j记录日志 
			 *	那个Action类抛出了异常   就获取的那个类
			 *	actioninvocation.getAction().getClass()
			 */
			Log log=LogFactory.getLog(actioninvocation.getAction().getClass());
			log.error(errorMsg,e);
			return "errorMsg";
			
		}
	
	}

	/**验证细颗粒权限控制*/
	private boolean isCheckLimit(HttpServletRequest request, Method method) {
		// TODO Auto-generated method stub
		if(method==null){
			return false;
		}
		//获取当前的登陆用户
		ElecUser elecUser=(ElecUser) request.getSession().getAttribute("globle_user");
		if(elecUser==null){
			return false;
		}
		//获取当前登陆角色的用户（一个用户可能有多个角色）
		Hashtable<String,String> ht=(Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		if(ht==null || ht.size()<=0){
			return false;
		}

		//处理注解，判断方法上是否存在注解（注解的名称为：AnnotationLimit）
		/*
		* 例如：
	    * @AnnotationLimit(mid="aa",pid="0")
		  public String home(){
		*/
		boolean isAnnotationPresent = method.isAnnotationPresent(AnnotationLimit.class);
		
		//不存在注解（此时不能操作该方法）
		if(!isAnnotationPresent){
			return false;
		}
		
		//存在注解（调用注解）
		AnnotationLimit limit = method.getAnnotation(AnnotationLimit.class);
		
		//获取注解上的值
		String mid = limit.mid();  //权限子模块名称
		String pid = limit.pid();  //权限父操作名称
		
		/**
		 * 如果登陆用户的角色id+注解上的@AnnotationLimit(mid="aa",pid="0")
		 *   * 在elec_role_popedom表中存在   flag=true，此时可以访问Action的方法;
		 *   * 在elec_role_popedom表中不存在 flag=false，此时不能访问Action的方法;
		 */
		boolean flag = false;
		//拦截器中加载spring容器，从而获取Service类，使用Service类查询对应的用户信息
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ElecRoleService elecRoleService = (ElecRoleService)wac.getBean(ElecRoleService.SERVICE_NAME);
		//遍历角色ID
		if(ht!=null && ht.size()>0){
			for(Iterator<Entry<String, String>> ite = ht.entrySet().iterator();ite.hasNext();){
				Entry<String, String> entry = ite.next();
				//获取角色ID
				String roleID = entry.getKey();
				flag = elecRoleService.findRolePopedomByID(roleID, mid, pid);
				if(flag){
					break;
				}
			}
		}
		return flag;
	}

	

}
