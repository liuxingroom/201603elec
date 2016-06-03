package com.xing.elec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xing.elec.domain.ElecText;
import com.xing.elec.domain.ElecUser;


@SuppressWarnings("unused")
public interface IElecUserDao extends ICommonDao<ElecUser>{
	public static  final String SERVICE_NAME="com.xing.elec.dao.impl.ElecUserDaoImpl";

	/**
	 * 使用sql语句的联合查询，查询用户表，关联数据字典表
	 * @param condition 查询条件
	 * @param params 查询要传递的参数
	 * @param orderBy  控制按时间排序
	 * @return 返回查询的用户对象组成的list集合
	 */
	List<ElecUser> findCollectionByConditionNoPageWithSql(String condition,
			Object[] params, Map<String, String> orderBy);

	/**
	 * 根据数据的类别来查询数据库
	 * @param zName
	 * @param eName
	 * @return 查询的数据集合
	 */
	List<Object[]> chartUser(String zName, String eName);


}
