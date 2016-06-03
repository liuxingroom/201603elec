package com.xing.elec.test;



import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xing.elec.domain.ElecText;
import com.xing.elec.service.IElecTextService;


public class TestService {
	@Test
	public void ServiceTest(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextService elecTextService=(IElecTextService) ac.getBean(IElecTextService.SERVICE_NAME);
		
		ElecText elecText=new ElecText();
		elecText.setTextDate(new Date());
		elecText.setTextName("测试service名称");
		elecText.setTextRemark("测试service备注");
		 
		elecTextService.saveElecService(elecText);	
	}

	/**模拟Action层，测试底层方法，指定查询条件查询结果列表*/
	@Test
	public void findCollectionByConditionNoPage(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextService elecTextService=(IElecTextService) ac.getBean(IElecTextService.SERVICE_NAME);
		
		ElecText elecText=new ElecText();
		elecText.setTextName("Dao");
		elecText.setTextRemark("Dao");
		
		elecTextService.findCollectionByConditionNoPage(elecText);
	}
}
