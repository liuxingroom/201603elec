package com.xing.elec.service;

import java.util.Hashtable;
import java.util.List;

import com.xing.elec.domain.ElecPopedom;
import com.xing.elec.domain.ElecRole;
import com.xing.elec.domain.ElecUser;

public interface ElecRoleService {
	public static final String SERVICE_NAME="com.xing.elec.service.impl.ElecRoleServiceImpl";

	/**
	 * 从数据库中查询 所有的角色
	 * @return 返回封装角色对象的list集合
	 */
	List<ElecRole> findAllRoleList();

	/**
	 * 查取权限
	 * @return 封装权限的List集合
	 */
	List<ElecPopedom> findAllPopedomList();

	/**
	 * 通过角色ID来获取所有的权限 信息
	 * @param roleID 角色ID
	 * @return 返回该角色所具有的权限的集合
	 */
	List<ElecPopedom> finAllPopedomListByRoleID(String roleID);

	/**
	 *	根据角色ID来获取系统中所用的用户
	 * @param roleID 用于查询的角色ID
	 * @return 返回封装所有用户的List集合
	 */
	List<ElecUser> findAllUserListByRoleID(String roleID);

	/**
	 * 向角色权限表（Elec_Role_Popedom）用户角色表（elec_user_role）中插入数据 
	 * @param elecPopedom
	 */
	void saveRole(ElecPopedom elecPopedom);

	/**
	 * 根据用户的角色 来获取该角色色权限
	 * @param ht 封装该用户角色的Hashtable
	 * @return 返回该用户权限的 拼接的字符串
	 */
	String findPopedomByRoleIDs(Hashtable<String, String> ht);

	/**
	 * 查询当前用户所对应的功能权限List<ElecPopedom>，将List集合转化成json
	 * @param popedom 权限mid组成的字符串
	 * @return 返回封装权限的List 集合
	 */
	List<ElecPopedom> findPopedomListByString(String popedom);

	/**
	 * 根据 用户id  权限code（mid）   父级权限code（pid） 查询角色权限表   如果存在该数据返回true 否则返回false
	 * @param roleID 角色id
	 * @param mid 权限code
	 * @param pid 父级权限code
	 * @return  如果存在该数据返回true 否则返回false
	 */
	boolean findRolePopedomByID(String roleID, String mid, String pid);
}
