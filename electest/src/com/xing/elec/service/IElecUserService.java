package com.xing.elec.service;

import java.util.ArrayList;
import java.util.List;

import com.xing.elec.domain.ElecUser;
import com.xing.elec.domain.ElecUserFile;

public interface IElecUserService {
	public static final String SERVICE_NAMAE="com.xing.elec.service.impl.IElecUserServiceImpl";

	/**
	 * 组织查询条件，查询用户列表
	 * @param elecUser 封装查询条件的javabean
	 * @return 返回符合条件的javabean的集合
	 */
	List<ElecUser> findUserListByCondition(ElecUser elecUser);

	/**
	 * 判断登陆名是否存在
	 * @param logonName 登陆名
	 * @return 返回值 1 表示登陆名框中的值为"" 2:表示登陆名框中的值 在数据库中已经存在  3：表示登陆名框中的值在数据库中不存在  因此可以使用
	 */
	String checkUser(String logonName);

	/**
	 * 保存用户新增的用户信息
	 * @param elecUser 封装新增的用户信息的javabean
	 */
	void saveUser(ElecUser elecUser);

	/**
	 * 通过用的id来获取用户
	 * @param userID 要查询的用户id
	 * @return 返回 用户的javabean对象
	 */
	ElecUser finUserByID(String userID);

	/**
	 * 通过用户文件的id来查找该文件
	 * @param fileID 用户文件的ID
	 * @return 返回封装该用户文件的javabean
	 */
	ElecUserFile findUserFileByID(String fileID);

	/**
	 * 根据用户id来删除用户
	 * @param elecUser 封装用户信息的javabean
	 */
	void deleteUserByID(ElecUser elecUser);

	/**
	 * 根据登陆名来查询用户
	 * @param name 登陆名
	 * @return 返回符合该登陆名的javabean对象
	 */
	ElecUser finUserByLogonName(String name);

	/**
	 * 按照excel格式 导出数据
	 * @return 返回封装标题数据的list集合
	 */
	ArrayList<String> findFileNameWithExcel();

	/**
	 * 获取excel的数据字段，通过导出设置表（动态导出）
	 * @param elecUser 封装查询条件的javabean
	 * @return 要导出的数据内容
	 */
	ArrayList<ArrayList<String>> finFieldDateWithExcel(ElecUser elecUser);

	/**
	 * 批量保存用户信息
	 * @param userList 封装用户信息的list集合
	 */
	void saveUserList(List<ElecUser> userList);

	/**
	 * 根据数据的类别来查询数据库
	 * @param zName
	 * @param eName
	 * @return 查询的数据集合
	 */
	List<Object[]> chartUser(String zName, String eName);
}
