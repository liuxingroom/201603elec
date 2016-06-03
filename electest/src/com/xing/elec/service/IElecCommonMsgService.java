package com.xing.elec.service;

import com.xing.elec.domain.ElecCommonMsg;

public interface IElecCommonMsgService {
	public static final String SERVICE_NAME="com.xing.elec.service.impl.IElecCommonMsgServiceImpl";

	/**
	 * 获取ElecCommonMsg对象
	 * @return 返回 查找到的对象值
	 */
	ElecCommonMsg findElecCommonMsg();

	/**
	 * 保存ElecCommonMsg对象
	 * @param elecCommonMsg 新添加的Javabean对象
	 */
	void saveCommonMsg(ElecCommonMsg elecCommonMsg);
}
