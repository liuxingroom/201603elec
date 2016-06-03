package com.xing.elec.web.action;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xing.elec.domain.ElecText;
import com.xing.elec.service.IElecTextService;


@SuppressWarnings("serial")
@Controller(value="elecTextAction")
@Scope("prototype")
public class ElecTextAction extends BaseAction<ElecText>{
	ElecText elecText=this.getModel();
	
	@Resource(name=IElecTextService.SERVICE_NAME)
	IElecTextService elecTextService;
	
	public String save(){
		elecTextService.saveElecService(elecText);
		String textName=request.getParameter("textName");
		System.out.println(textName);
		return "save";
	}
}
