package com.xing.elec.test;



import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.xing.elec.domain.ElecText;


public class TestHibernate {
	/*
	 * ���Ա���
	 */
	@Test
	public void save(){
		Configuration configuration=new Configuration();
		configuration.configure();//����classpath�µ�hibernate.cfg.xml���ļ�
		SessionFactory sf=configuration.buildSessionFactory();
		Session session=sf.openSession();
		Transaction tr=session.beginTransaction();
		
		ElecText elecText=new ElecText();
		elecText.setTextName("����hibernate���");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("����hibernate��ע");
		session.save(elecText);
		
		tr.commit();
		session.close();
	}
}
