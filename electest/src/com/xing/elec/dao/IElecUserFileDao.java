package com.xing.elec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xing.elec.domain.ElecText;
import com.xing.elec.domain.ElecUserFile;


@SuppressWarnings("unused")
public interface IElecUserFileDao extends ICommonDao<ElecUserFile>{
	public static  final String SERVICE_NAME="com.xing.elec.dao.impl.ElecUserFileDaoImpl";

}
