package com.xing.elec.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecRolePopedomDao;
import com.xing.elec.domain.ElecRolePopedom;

@Repository(IElecRolePopedomDao.SERVICE_NAME)
public class IElecRolePopedomDaoImpl extends CommonDaoImpl<ElecRolePopedom> implements IElecRolePopedomDao{

	/**  
	* @Name: findPopedomByRoleIDs
	* @Description: 使用角色ID的Hashtable的集合，获取的当前角色ID条件，查询角色对应的权限并集
	* @Parameters: String：存放条件
	* @Return: List<Object>：表示权限的字符串集合：
	* 使用hql或者是sql语句：
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	@Override
	public List<Object> findPopedomByRoleIDs(String condition) {
		// TODO Auto-generated method stub
		String hql="select distinct o.mid from ElecRolePopedom o where 1=1 and o.roleID in("+condition+")";
		List<Object> list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
}
