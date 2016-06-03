package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecTextDao;
import com.xing.elec.dao.IElecUserFileDao;
import com.xing.elec.domain.ElecText;
import com.xing.elec.domain.ElecUserFile;

@Repository(IElecUserFileDao.SERVICE_NAME)
public class ElecUserFileDaoImpl extends CommonDaoImpl<ElecUserFile> implements IElecUserFileDao{
	
}
