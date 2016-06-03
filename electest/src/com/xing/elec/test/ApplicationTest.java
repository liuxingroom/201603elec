package com.xing.elec.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {
	@Test
	public void testApplication(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.print(ac);
	}
}
