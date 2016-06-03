package com.xing.elec.dao;

import java.util.List;

import com.xing.elec.domain.ElecRolePopedom;

public interface IElecRolePopedomDao extends ICommonDao<ElecRolePopedom>{
	public static final String SERVICE_NAME="IElecRolePopedomDaoImpl";

	/**
	 * 根据用户的角色ID来查询  该用户所对应的权限
	 * @param condition 封装角色id的 字符串
	 * @return 返回封装权限的集合
	 */
	List<Object> findPopedomByRoleIDs(String condition);
}
