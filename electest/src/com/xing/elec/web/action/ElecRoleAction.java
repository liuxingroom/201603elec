package com.xing.elec.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import com.xing.elec.domain.ElecPopedom;
import com.xing.elec.domain.ElecRole;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.service.ElecRoleService;

@SuppressWarnings("serial")
@Controller("elecRoleAction")
@Scope(value="prototype")
public class ElecRoleAction extends BaseAction<ElecPopedom>{
	ElecPopedom elecPopedom=this.getModel();
	
	/**注入角色的Service*/
	@Resource(name=ElecRoleService.SERVICE_NAME)
	ElecRoleService elecRoleService;
	
	/**  
	* @Name: home
	* @Description: 角色管理的首页显示
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-03（创建日期）
	* @Parameters: 无
	* @Return: String：跳转到system/roleIndex.jsp
	*/
	public String home(){
		//一:查询系统中所有的角色
		List<ElecRole> roleList =elecRoleService.findAllRoleList();
		request.setAttribute("roleList",roleList);
		//2.查询系统中所有的权限
		//返回List<ElecPopedom>,存放多有的tr，也就是pid=0的集合 ，父集合
		List<ElecPopedom> popedomList=elecRoleService.findAllPopedomList();
		request.setAttribute("popedomList", popedomList);
		return "home";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑页面
	* @Parameters: 无
	* @Return: String：跳转到system/roleEdit.jsp
	*/
	public String edit(){
		//角色ID 该id的获取 是由于 在roleIndex.jsp 界面中Form1表单 通过点击触发一个ajax事件（参数为form1表单） 
		//然后通过Pub.submitActionWithForm（）函数中的 var str = Pub.getParams2Str(sForm);将form1中的表单解析出来 
		//最后 通过  req.send(str);方法向action传递参数
		String roleID=elecPopedom.getRoleID();
		//1.使用用戶ID,查询系统中所有的权限，并显示
		List<ElecPopedom> popedomList=elecRoleService.finAllPopedomListByRoleID(roleID);
		request.setAttribute("popedomList", popedomList);
		//2.使用当前角色ID,查询系统中所有的用户，并显示（匹配）
		List<ElecUser> userList= elecRoleService.findAllUserListByRoleID(roleID);
		request.setAttribute("userList", userList);
		return "edit";
	}
	
	/**  
	* @Name: save
	* @Description: 保存角色和权限，角色和用户的关联关系
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-03（创建日期）
	* @Parameters: 无
	* @Return: String：重定向到system/roleIndex.jsp
	*/
	public String save(){
		elecRoleService.saveRole(elecPopedom);
		return "save";
	}
}
