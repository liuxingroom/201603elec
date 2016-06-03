package com.xing.elec.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xing.elec.dao.ICommonDao;
import com.xing.elec.domain.ElecText;
import com.xing.elec.utils.PageInfo;
import com.xing.elec.utils.TUtil;


public class CommonDaoImpl<T> extends HibernateDaoSupport implements ICommonDao<T>{

	Class entityClass=TUtil.getActualType(this.getClass());
	//此处必须是setXXX方法    名字可以随便起
	@Resource(name="sessionFactory")  //注入的sessionFactory就是该方法的形参
	public void setDi(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public void save(T entity) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(entity);
	}

	@Override
	public void saveList(List<T> list) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().saveOrUpdateAll(list);
	}
	
	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(entity);
	}
	
	@Override
	public T findObjectByID(Serializable id) {
		// TODO Auto-generated method stub
		T entity=(T) this.getHibernateTemplate().get(entityClass, id);
		return entity;
	}

	@Override
	public void deleteObjectByIds(Serializable... ids) {
		// TODO Auto-generated method stub
		if(ids!=null && ids.length>0){
			for(Serializable id:ids){
				//先查询
				Object entity=this.findObjectByID(id);
				this.getHibernateTemplate().delete(entity);
			}
		}
	}

	@Override
	public void deleteObjectByCollection(List<T> list) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().deleteAll(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findCollectionByConditionNoPage(String condition,
			final Object[] params, Map<String, String> orderBy) {
		// TODO Auto-generated method stub
		
		//hql语句
		String hql="from "+entityClass.getSimpleName()+" o where 1=1";
		//将map集合中存放的字段排序 ，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderByCondition=this.orderByHql(orderBy);
		//添加查询条件
		final String finalHql=hql+condition+orderByCondition;
		//查询
		/*方案一*/
	    //List<T> list=this.getHibernateTemplate().find(finalHql, params);
		/*方案二*/
//		SessionFactory sf=this.getHibernateTemplate().getSessionFactory();
//		Session session=sf.getCurrentSession();//获取当前线程
//		Query query=session.createQuery(finalHql);
//		if(params!=null && params.length>0){
//			for(int i=0;i<params.length;i++){
//				query.setParameter(i, params[i]);
//			}
//		}
//		List list=query.list();
		
		
		/*方案三 */
		List<T> list=this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query query=session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
			
		});
		
		return list;
	}
	
	
	/**指定查询条件，查询列表（使用分页）*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao层
		AND o.textName LIKE '%张%'   #Service层
		AND o.textRemark LIKE '%张%'   #Service层
		ORDER BY o.textDate ASC,o.textName DESC  #Service层
	 */
	public List<T> findCollectionByConditionWithPage(String condition,
			final Object[] params, Map<String, String> orderby,final PageInfo pageInfo) {
		//hql语句
		String hql = "from "+entityClass.getSimpleName()+" o where 1=1 ";
		//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderByHql(orderby);
		//添加查询条件
		final String finalHql = hql + condition + orderbyCondition;
		
		List<T> list = this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				/**2014-12-9,添加分页 begin*/
				pageInfo.setTotalResult(query.list().size());//初始化总的记录数
				query.setFirstResult(pageInfo.getBeginResult());//当前页从第几条开始检索，默认是0,0是第一条
				query.setMaxResults(pageInfo.getPageSize());//当前页最多显示多少条记录
				/**2014-12-9,添加分页 end*/
				return query.list();
			}
			
		});
		return list;
	}
	
	
	/**指定查询条件，查询列表 （不分页），使用投影查询*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao层
		AND o.textName LIKE '%张%'   #Service层
		AND o.textRemark LIKE '%张%'   #Service层
		ORDER BY o.textDate ASC,o.textName DESC  #Service层
	 */
	@Override
	public List findCollectionByConditionNoPageWithSelectCondition(
			String condition, final Object[] params, Map<String, String> orderBy,
			String selectCondition) {
		//hql语句
				String hql = "select "+selectCondition+" from "+entityClass.getSimpleName()+" o where 1=1 ";
				//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
				String orderbyCondition = this.orderByHql(orderBy);
				//添加查询条件
				final String finalHql = hql + condition + orderbyCondition;
				//查询，执行hql语句
				List list = this.getHibernateTemplate().execute(new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(finalHql);
						if(params!=null && params.length>0){
							for(int i=0;i<params.length;i++){
								query.setParameter(i, params[i]);
							}
						}
						return query.list();
					}
					
				});
				return list;
	}
	
	/**指定查询条件，查询列表，指定查询缓存*/
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findCollectionByConditionNoPageWithCache(String condition,
			final Object[] params, Map<String, String> orderBy) {
		// TODO Auto-generated method stub
		
		//hql语句
		String hql="from "+entityClass.getSimpleName()+" o where 1=1";
		//将map集合中存放的字段排序 ，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderByCondition=this.orderByHql(orderBy);
		//添加查询条件
		final String finalHql=hql+condition+orderByCondition;
		//查询
		
		
		/*方案三 */
		List<T> list=this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query query=session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				query.setCacheable(true);
				return query.list();
			}
			
		});
		return list;
	}

	private String orderByHql(Map<String, String> orderBy) {
		// TODO Auto-generated method stub
		StringBuffer buffer=new StringBuffer();
		if(orderBy!=null && orderBy.size()>0){
			buffer.append(" order by ");
			for(Map.Entry<String, String> entry:orderBy.entrySet()){
				buffer.append(entry.getKey()+" "+entry.getValue()+",");
			}
			//删除hql语句中的最后一个逗号
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}


}
