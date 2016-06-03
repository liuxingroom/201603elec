package com.xing.elec.dao;

import java.util.List;
import java.util.Map;

import com.xing.elec.domain.ElecFileUpload;

public interface ElecFileUploadDao extends ICommonDao<ElecFileUpload>{
	public static final String SERVICE_NAME="com.xing.elec.dao.impl.ElecFileUploadDaoImpl";

	List<ElecFileUpload> findFileUploadListByCondition(String condition,
			Object[] params, Map<String, String> orderBy);
}
