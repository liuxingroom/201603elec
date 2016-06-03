package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecCommonMsgContentDao;
import com.xing.elec.domain.ElecCommonMsgContent;

@Repository(IElecCommonMsgContentDao.SERVICE_NAME)
public class ElecCommonMsgContentDaoImpl extends CommonDaoImpl<ElecCommonMsgContent> implements IElecCommonMsgContentDao{
	
}
