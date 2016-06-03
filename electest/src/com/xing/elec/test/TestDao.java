package com.xing.elec.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.xing.elec.dao.IElecTextDao;
import com.xing.elec.domain.ElecText;


public class TestDao {
	/**保存*/
	@Test
	public void save(){
		//加载spring容器
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextDao elecTextDao=(IElecTextDao)ac.getBean(IElecTextDao.SERVICE_NAME);
		
		ElecText elecText=new ElecText();
		elecText.setTextName("测试Dao名称");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("测试Dao备注");
		elecTextDao.save(elecText);
	}
	/**更新*/
	@Test
	public void update(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextDao elecTextDao=(IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		ElecText elecText=new ElecText();
		elecText.setTextID("402881e5533b8a2c01533b8a2fa80001");
		elecText.setTextName("王五");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("小五");
		elecTextDao.update(elecText);
		
		
	}
	
	/*使用主键ID查询对象 */
	@Test
	public void findObjectByID(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextDao elecTextDao=(IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		Serializable id="402881e5533b8a2c01533b8a2fa80001";
		ElecText elecText=elecTextDao.findObjectByID(id);
		System.out.println(elecText.getTextName()+"  "+elecText.getTextRemark());
	}
	
	/*删除（使用主键ID删除）*/
	@Test
	public void deleteObjectByIds(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextDao elecTextDao=(IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		Serializable ids="402881e5533b8a2c01533b8a2fa80001";
		elecTextDao.deleteObjectByIds(ids);
	}
	
	/*删除（使用集合List惊醒删除）*/
	@Test
	public void deleteObjectByCollection(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		IElecTextDao elecTextDao=(IElecTextDao) ac.getBean(IElecTextDao.SERVICE_NAME);
		
		List<ElecText> list=new ArrayList<ElecText>();
		ElecText elecText1=new ElecText();
		elecText1.setTextID("402881e55340340c015340340fbc0001");
		
		ElecText elecText2=new ElecText();
		elecText2.setTextID("402881e553403ef20153403ef5bd0001");
		
		ElecText elecText3=new ElecText();
		elecText3.setTextID("402881e5534042110153404214310001");
		list.add(elecText1);
		list.add(elecText2);
		list.add(elecText3);
		elecTextDao.deleteObjectByCollection(list);
	}
}
