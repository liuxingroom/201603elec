package com.xing.elec.service;

import java.util.List;

import com.xing.elec.domain.ElecText;


public interface IElecTextService {
	public static final String SERVICE_NAME="com.xing.elec.service.impl.ElecTextServiceImpl";
	
	public void saveElecService(ElecText elecText);
	
	List<ElecText> findCollectionByConditionNoPage(ElecText elecText);
}
