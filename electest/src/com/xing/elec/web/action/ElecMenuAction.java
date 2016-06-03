package com.xing.elec.web.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;


import com.xing.elec.domain.ElecCommonMsg;
import com.xing.elec.domain.ElecPopedom;
import com.xing.elec.domain.ElecRole;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.service.ElecRoleService;
import com.xing.elec.service.IElecCommonMsgService;
import com.xing.elec.service.IElecUserService;
import com.xing.elec.utils.LogonUtils;
import com.xing.elec.utils.MD5keyBean;
import com.xing.elec.utils.ValueUtils;
import com.xing.elec.web.form.MenuForm;


@SuppressWarnings("serial")
@Controller("elecMenuAction")
@Scope(value="prototype")
public class ElecMenuAction extends BaseAction<MenuForm> {
	MenuForm menuForm=this.getModel();
	
	/**注入运行监控Service*/
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**注入用户Service*/
	@Resource(name=IElecUserService.SERVICE_NAMAE)
	IElecUserService elecUserService;
	
	/**注入角色Service*/
	@Resource(name=ElecRoleService.SERVICE_NAME)
	ElecRoleService elecRoleService;
	
	/**
	 * @Name:menuHome
	 * @Description :跳转到系统登录的首页
	 * @author xing
	 * @return  String 跳转到menu/home.jsp
	 */
	public String menuHome(){
		//获取用户名和密码
		String  name=menuForm.getName();
		String password =menuForm.getPassword();
		
		/**验证码*/
		/**
		 * flag=true:验证成功
		 * flag=false:验证失败
		 */
		boolean flag=LogonUtils.checkNumber(request);
		if(!flag){
			this.addActionError("验证码输入有误");
			return "logonError";//跳转到登录页面
		}
		
		/** 验证用户名和密码是否输入正确*/
		//* 使用登录名作为查询条件，查询用户表，因为登录名是惟一值，获取ElecUser的对象
		ElecUser elecUser=elecUserService.finUserByLogonName(name);
		if(elecUser==null){
			this.addActionError("用户名输入有误");
			return "logonError";//掉转到登陆页面
		}
		//*校验密码是否正确
		if(StringUtils.isBlank(password)){
			this.addActionError("密码不能为空");
			return "logonError";
		}else{
			MD5keyBean md5keyBean=new MD5keyBean();
			String md5password=md5keyBean.getkeyBeanofStr(password);
			
			//比对
			if(!md5password.equals(elecUser.getLogonPwd())){
				this.addActionError("密码输入错误");
				return "logonError";//跳转到登陆页面
			}
		}
		/** 2.判断用户是否分配类角色，如果分配了角色，将角色的信息存放起来 */
		/**
		 * 存放角色信息
		 * key 角色ID
		 * value 角色名称
		 */
		Hashtable<String,String> ht=new Hashtable<String,String>();
		Set<ElecRole> elecRoles=elecUser.getElecRoles();
		//当前用户没有分配角色
		if(elecRoles==null && elecRoles.size()==0){
			this.addActionError("当前用户没有分配角色，请与管理员联系");
			return "logonError";
		}//如果分配了角色
		else{
			for(ElecRole elecRole:elecRoles){
				//一个用户可以对应多个角色
				ht.put(elecRole.getRoleID(), elecRole.getRoleName());
			}
		}
		
		/**三：判断用户对应的角色是否分配了权限，如果分配了权限，将权限的信息存放起来（aa）*/
		//将权限的细心你存放到字符串中，存放的权限的mid（字符串的格式：aa@ab@ac@ad@ae） -----jquery的ztree动态数据加载
		String popedom=elecRoleService.findPopedomByRoleIDs(ht);
		if(StringUtils.isBlank(popedom)){
			this.addActionError("当前用户具有的角色，没有分配权限，请与管理员联系");
			return "logonError";
		}
		/**记住我  注：传递过去的密码没有进行md5加密*/
		LogonUtils.remeberMe(name,password,request,response);
		
		//将ElecUser对象放置到Session中
		request.getSession().setAttribute("globle_user", elecUser);
		//将Hashtable中存放的角色信息，放置到Session中
		request.getSession().setAttribute("globle_role", ht);
		//将权限的字符串（格式：aa@ab@ac@ad@ae）存放到Session中
		request.getSession().setAttribute("globle_popedom", popedom);
		
 		return "menuHome";
	}
	
	/**标题*/
	public String title(){
		return "title";
	}
	
	/**菜单*/
	public String left(){
		return "left";
	}
	
	/**框架大小改变*/
	public String change(){
		return "change";
	}
	
	/**  
	* @Name: loading
	* @Description: 功能页面的显示
	* @Author: xing
	* @Return: String：跳转到menu/loading.jsp
	*/
	public String loading(){
		//查询设备运行情况，放置到浮动框中
		//1.查询数据库运行监控表的数据 ，返回唯一ElecCommonMsg
		ElecCommonMsg commonMsg=elecCommonMsgService.findElecCommonMsg();
		//2.把ElecCommonMsg对象压入值栈顶，支持表单回显
		ValueUtils.putValueStack(commonMsg);
		return "loading";
	}
	
	/**  
	* @Name: logout
	* @Parameters: 无
	* @Return: String：重定向到index.jsp
	*/
	public String logout(){
		//在重新登录之前 应该吧登录的信息清除（即 清除session）
		request.getSession().invalidate();
		return "logout";
	}
	
	/**  
	* @Name: alermStation
	* @Parameters: 无
	* @Return: String：跳转到menu/alermStation.jsp
	*/
	public String alermStation(){
	
		//1.查询数据库运行监控表的数据 ，返回唯一ElecCommonMsg
		ElecCommonMsg commonMsg=elecCommonMsgService.findElecCommonMsg();
		//2.把ElecCommonMsg对象压入值栈顶，支持表单回显
		ValueUtils.putValueStack(commonMsg);
		return "alermStation";
	}
	
	
	/**  
	* @Name: alermDevice
	* @Parameters: 无
	* @Return: String：跳转到menu/alermDevice.jsp
	*/
	public String alermDevice(){
	
		//1.查询数据库运行监控表的数据 ，返回唯一ElecCommonMsg
		ElecCommonMsg commonMsg=elecCommonMsgService.findElecCommonMsg();
		//2.把ElecCommonMsg对象压入值栈顶，支持表单回显
		ValueUtils.putValueStack(commonMsg);
		return "alermDevice";
	}
	
	/**  
	* @Name: showMenu
	* @Description: 使用ajax动态加载左侧的树型菜单
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-05 （创建日期）
	* @Parameters: 无
	* @Return: String：showMenu，使用struts2提供的json插件包
	*/
	public String showMenu(){
		//获取当前用户的角色
		Hashtable<String,String> ht=(Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		//获取当前用户的对象
		ElecUser elecUser=(ElecUser) request.getSession().getAttribute("globle_user");
		//1.从session获取当前用户具有的权限字符串
		String popedom=(String) request.getSession().getAttribute("globle_popedom");
		//2.查询当前用户所对应的功能List<ElecPopedom> ,将List集合转化成json
		List<ElecPopedom> list=elecRoleService.findPopedomListByString(popedom);
		
		/**使用角色控制系统的URL*/
		//不是系统管理员
		if(!ht.containsKey("1")){
			if(list!=null && list.size()>0){
				for(ElecPopedom elecPopedom:list){
					String mid=elecPopedom.getMid();
					String pid=elecPopedom.getPid();
					//改变用户管理的URL
					if("an".equals(mid)  && "am".equals(pid)){
						elecPopedom.setUrl("../system/elecUserAction_edit.do?userID="+elecUser.getUserID()+"&roleflag=1");
					}
				}
			}
		}
		
		
		//3.将List转化成json，只需要将list集合放置到栈顶
		ValueUtils.putValueStack(list);
		return "showMenu";
	}
}
