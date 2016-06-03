package com.xing.elec.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xing.elec.dao.ElecExportFieldsDao;
import com.xing.elec.domain.ElecExportFields;
import com.xing.elec.service.ElecExportFieldsService;

@Service(ElecExportFieldsService.WEVICE_NAME)
public class ElecExportFieldsServiceImpl implements ElecExportFieldsService{
	
	/**注入 导入的Dao*/
	@Resource(name=ElecExportFieldsDao.SERVICE_NAME)
	ElecExportFieldsDao elecExportFieldsDao;

	
	/**  
	* @Name: findExportFieldsByID
	* @Description: 使用主键ID，查询对应的导出设置对象
	* @Parameters: String：主键ID
	* @Return: ElecExportFields：PO对象
	*/
	@Override
	public ElecExportFields findExportFieldsByID(String belongTo) {
		// TODO Auto-generated method stub
		return elecExportFieldsDao.findObjectByID(belongTo);
	}


	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveSetExportExcel(ElecExportFields elecExportFields) {
		// TODO Auto-generated method stub
		elecExportFieldsDao.update(elecExportFields);
	}
}
