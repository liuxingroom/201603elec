package com.xing.elec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.xing.elec.domain.ElecSystemDDL;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.utils.PageInfo;

/*
 * 将dao重构
 */
public interface ICommonDao<T> {
	/*
	 * 保存
	 */
	public void save(T entity);
	
	/*
	 * 批量保存
	 */
	void saveList(List<T> list);
	
	/*
	 * 更新
	 */
	void update(T entity);
	
	/*
	 * 查找方法
	 */
	T findObjectByID(Serializable id);
	
	/*
	 * 根据id来删除对象
	 */
	void deleteObjectByIds(Serializable... ids);
	
	/*
	 * 批量删除
	 */
	void deleteObjectByCollection(List<T> list);

	
	List<T> findCollectionByConditionNoPage(String condition,Object[] params, Map<String, String> orderBy);
	
	List<T> findCollectionByConditionNoPageWithCache(
			String condition, Object[] params, Map<String, String> orderBy);
	List<T> findCollectionByConditionWithPage(String condition,Object[] params, Map<String, String> orderby, PageInfo pageInfo);
	
	List findCollectionByConditionNoPageWithSelectCondition(String condition,
			Object[] params, Map<String, String> orderBy, String selectCondition);
}
