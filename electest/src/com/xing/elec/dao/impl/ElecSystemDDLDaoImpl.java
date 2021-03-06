package com.xing.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.xing.elec.dao.ElecSystemDDLDao;
import com.xing.elec.domain.ElecSystemDDL;

@Repository(ElecSystemDDLDao.SERVICE_NAME)
public class ElecSystemDDLDaoImpl extends CommonDaoImpl<ElecSystemDDL> implements ElecSystemDDLDao{

	@Override
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		List<ElecSystemDDL> systemList=new ArrayList<ElecSystemDDL>();
		/**投影查询  1.直接查*/
//		String hql="select distinct o.keyword from ElecSystemDDL o";
//		List<Object> list=this.getHibernateTemplate().find(hql);
//		//组织页面返回的结果
//		if(list!=null && list.size()>0){
//			for(Object o:list){
//				ElecSystemDDL elecSystemDDL=new ElecSystemDDL();
//				elecSystemDDL.setKeyword(o.toString());
//				systemList.add(elecSystemDDL);
//			}
//		}
		
		String hql="SELECT DISTINCT new com.xing.elec.domain.ElecSystemDDL(o.keyword) FROM ElecSystemDDL o";
		systemList=this.getHibernateTemplate().find(hql);
		System.out.println(systemList);
		return systemList;
	}

	
	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: 使用数据类型和数据项的编号，获取数据项的值
	* @Parameters: String keywrod,数据类型
	* 			   String ddlCode：数据项的编号
	* @Return: String：数据项的值
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String findDdlNameByKeywordAndDelCode(final String keyword, final String ddlCode) {
		// TODO Auto-generated method stub
		final String hql="select o.ddlName from ElecSystemDDL o where o.keyword=? and o.ddlCode=?";
		List<Object> list=this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query query=session.createQuery(hql);
				query.setParameter(0,keyword);
				query.setParameter(1, Integer.parseInt(ddlCode));
				//启动查询缓存
				query.setCacheable(true);
				return query.list();
			}
		});
		
		//返回数据项的值
		String ddlName="";
		if(list!=null && list.size()>0){
			Object o=list.get(0);
			ddlName=o.toString();
		}
		return ddlName;
	}


	@Override
	public String findDdlCodeByKeywordAndDdlName(final String keyword, final String ddlName) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				final String hql="select o.ddlCode from ElecSystemDDL o where o.keyword=? and o.ddlName=?";
				List<Object> list=this.getHibernateTemplate().execute(new HibernateCallback() {

					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						Query query=session.createQuery(hql);
						query.setParameter(0,keyword);
						query.setParameter(1, ddlName);
						//启动查询缓存
						query.setCacheable(true);
						return query.list();
					}
				});
				
				//返回数据项的值
				String ddlCode="";
				if(list!=null && list.size()>0){
					Object o=list.get(0);
					ddlCode=o.toString();
				}
				return ddlCode;
	}
	
}
