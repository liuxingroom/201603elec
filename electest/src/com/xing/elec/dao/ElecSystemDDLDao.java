package com.xing.elec.dao;

import java.util.List;
import java.util.Map;

import com.xing.elec.domain.ElecSystemDDL;

public interface ElecSystemDDLDao extends ICommonDao<ElecSystemDDL>{
	public static final String SERVICE_NAME="com.xing.elec.dao.impl.ElecSystemDDLDaoImpl";
	
	/**
	 * 查询数据库中已有的数据类型（去掉重复的）
	 * @return 返回封装ElecSystemDDL javabean的list集合
	 */
	List<ElecSystemDDL> findSystemDDLListByDistinct();

	/**
	 * 使用数据类型和数据项的编号，获取数据项的值
	 * @param keyword 数据类型
	 * @param sexID 数据编号
	 * @return 返回数据项的值
	 */
	String findDdlNameByKeywordAndDelCode(String keyword, String sexID);

	/**
	 * 使用数据类型和数据项的值 ，获取数据项的编号
	 * @param keyword 数据类型
	 * @param ddlName 数据项的值
	 * @return 返回数据编号的值
	 */
	String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName);

	

	
}
