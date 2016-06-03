package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecTextDao;
import com.xing.elec.domain.ElecText;

@Repository(IElecTextDao.SERVICE_NAME)
public class ElecTextDaoImpl extends CommonDaoImpl<ElecText> implements IElecTextDao{
	
}
