package com.xing.elec.web.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.xing.elec.domain.ElecCommonMsg;
import com.xing.elec.service.IElecCommonMsgService;
import com.xing.elec.utils.ValueUtils;


@SuppressWarnings({ "serial", "unused" })
@Controller("elecCommonMsgAction")
@Scope("prototype")
public class ElecCommonMsgAction extends BaseAction<ElecCommonMsg>{
	
	ElecCommonMsg elecCommonMsg=this.getModel();
	
	/*注入运行监控*/
	@Resource(name=IElecCommonMsgService.SERVICE_NAME)
	IElecCommonMsgService elecCommonMsgService;
	
	/**  
	* @Name: home
	* @Description: 运行监控的首页显示
	* @Return: String：跳转到system/actingIndex.jsp
	*/
	public String home(){
		//1：查询数据库运行监控表的数据，返回惟一ElecCommonMsg
		ElecCommonMsg commonMsg=elecCommonMsgService.findElecCommonMsg();
		ValueUtils.putValueStack(commonMsg);
		return "home";
	}
	
	/**  
	* @Name: save
	* @Description: 保存运行监控数据
	* @Return: String：重定向到system/actingIndex.jsp（再查询）
	*/
	
	public String save(){
		elecCommonMsgService.saveCommonMsg(elecCommonMsg);
		return "save";
	}
	
	/**  
	* @Name: actingView
	* @Description: 使用highsliderJS完成查询设备运行情况的详细信息
	* @Parameters: 无
	* @Return: String：跳转到system/actingView.jsp
	*/
	public String actingView(){
		//查询运行监控的数据
		
		//1：查询数据库运行监控表的数据，返回惟一ElecCommonMsg
		ElecCommonMsg commonMsg=elecCommonMsgService.findElecCommonMsg();
		ValueUtils.putValueStack(commonMsg);
		return "actingView";
	}
}
