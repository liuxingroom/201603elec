package com.xing.elec.service;

import java.util.List;

import com.xing.elec.domain.ElecFileUpload;

public interface ElecFileUploadService {
	public static final String SERVICE_NAME="com.xing.elec.service.impl.ElecFileUploadSeriveImpl";

	/**
	 * 保存资料图纸管理（管理员进行文件上传操作）
	 * @param elecFileUpload 将上传的文件信息封装成了javabean
	 */
	void saveFileUpload(ElecFileUpload elecFileUpload);

	/**
	 * 通过选择所属单位和图纸类别的查询条件，查询对应单位和图纸下的文件上传的列表，返回List<ElecFileUpload>
	 * @param elecFileUpload 封装所属单位和图纸类别的javabean
	 * @return 封装图表信息的javabean
	 */
	List<ElecFileUpload> findFileUploadListByCondition(
			ElecFileUpload elecFileUpload);

	/**
	 * 通过文件ID，查询资料图纸管理表，获取到路径
	 * @param fileID 文件ID
	 * @return 返回封装路径信息的javabean
	 */
	ElecFileUpload findFileByID(Integer fileID);

	/**
	 * 使用lucene进行全文检索
	 * @param elecFileUpload 封装检索条件的javabean
	 * @return 将查询的对象封装到list集合中
	 */
	List<ElecFileUpload> findFileUploadListByLuceneCondition(
			ElecFileUpload elecFileUpload);

	/**
	 * 删除资料图纸管理的数据
	 * @param seqId 要删除的文件的id
	 */
	void deleteFileUploadByID(Integer seqId);

	
}
