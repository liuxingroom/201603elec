package com.xing.elec.dao.impl;

import org.springframework.stereotype.Repository;

import com.xing.elec.dao.IElecCommonMsgDao;
import com.xing.elec.domain.ElecCommonMsg;


@Repository(IElecCommonMsgDao.SERVICE_NAME)
public class IElecCommonDaoImpl extends CommonDaoImpl<ElecCommonMsg> implements IElecCommonMsgDao{

}
