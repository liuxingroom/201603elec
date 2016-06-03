package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecRoleDao;
import com.xing.elec.domain.ElecRole;

@Repository(IElecRoleDao.SERVICE_NAME)
public class ElecRoleDaoImpl extends CommonDaoImpl<ElecRole> implements IElecRoleDao{
	
}
