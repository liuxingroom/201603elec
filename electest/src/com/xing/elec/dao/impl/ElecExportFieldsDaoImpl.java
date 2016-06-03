package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.ElecExportFieldsDao;
import com.xing.elec.domain.ElecExportFields;

@Repository(ElecExportFieldsDao.SERVICE_NAME)
public class ElecExportFieldsDaoImpl extends CommonDaoImpl<ElecExportFields> implements ElecExportFieldsDao{
	
}
