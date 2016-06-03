package com.xing.elec.service;

import com.xing.elec.domain.ElecExportFields;

public interface ElecExportFieldsService {
	public static final String WEVICE_NAME ="com.xing.elec.service.impl.ElecExportFieldsServiceImpl";

	/**
	 * 使用主键id，查询导出设置表，获取导出设置的字段，返回ElecExportFields对象
	 * @param belongTo 主键ID
	 * @return 返回导出表中组成的javabean对象
	 */
	ElecExportFields findExportFieldsByID(String belongTo);

	/**
	 * 保存导入数据的信息
	 * @param elecExportFields 封装导入信息的javabean
	 */
	void saveSetExportExcel(ElecExportFields elecExportFields);
}
