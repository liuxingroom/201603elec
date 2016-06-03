package com.xing.elec.service;

import java.util.List;

import com.xing.elec.domain.ElecSystemDDL;

public interface ElecSystemDDLService {
	public static final String SERVICE_NAME="com.xing.elec.service.impl.ElecSystemDDLServiceImpl";
	
	/**
	 * 查询数据库中已有的数据类型（去掉重复的）
	 * @return
	 */
	List<ElecSystemDDL> findSystemDDLListByDistinct();

	/**
	 * 根据数据类型作为条件 查询数据字典 
	 * @param keyword 数据类型
	 * @return 返回List<ElecSystemDDL>
	 */
	List<ElecSystemDDL> findSysteDDLListByKeyword(String keyword);

	/**
	 * 保存数据字典信息
	 * @param elecSystemDDL 封装数据信息的javabean
	 */
	void saveSystemDDL(ElecSystemDDL elecSystemDDL);

	/**
	 * 根据所属单位和数据编号，获取数据项的值
	 * @param keyword 所属单位
	 * @param ddlCode 数据编号
	 * @return 返回数据项的值
	 */
	String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode);

	/**
	 * 根据所属单位和数据名称，获取数据编号的值
	 * @param keyword 所属单位
	 * @param ddlName 数据项的值
	 * @return 返回数据编号的值
	 */
	String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName);
}
