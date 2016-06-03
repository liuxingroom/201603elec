package com.xing.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xing.elec.dao.ElecSystemDDLDao;
import com.xing.elec.domain.ElecSystemDDL;
import com.xing.elec.service.ElecSystemDDLService;

@Service(ElecSystemDDLService.SERVICE_NAME)
public class ElecSystemDDLServiceImpl implements ElecSystemDDLService{

	@Resource(name=ElecSystemDDLDao.SERVICE_NAME)
	ElecSystemDDLDao elecSystemDDLDao;
	/**  
	* @Name: findSystemDDLListByDistinct
	* @Description: 查询数据字典，去掉重复值
	* @Parameters: 无
	* @Return: List<ElecSystemDDL>：存放数据类型的集合
	*/
	@Override
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		List<ElecSystemDDL> list=elecSystemDDLDao.findSystemDDLListByDistinct();
		return list;
	}
	
	/**  
	* @Name: findSystemDDLListByKeyword
	* @Description: 以数据类型作为条件，查询数据字典
	* @Parameters: String：数据类型
	* @Return: List<ElecSystemDDL>：存放数据字典的集合
	*/
	@Override
	public List<ElecSystemDDL> findSysteDDLListByKeyword(String keyword) {
		//查询条件
		String condition="";
		List<Object> list=new ArrayList<Object>();
		//拼接查询条件
		if(StringUtils.isNotBlank(keyword)){
			condition+=" and o.keyword=?";
			list.add(keyword);
		}
		//将含有查询条件的集合转换成数组
		Object[] params=list.toArray();
		//排序
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.ddlCode", "asc");
		List<ElecSystemDDL> paramsList=elecSystemDDLDao.findCollectionByConditionNoPageWithCache(condition, params, orderBy);
		return paramsList;
	}

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveSystemDDL(ElecSystemDDL elecSystemDDL) {
		// TODO Auto-generated method stub
		//类型名称
		String keyword=elecSystemDDL.getKeywordname();
		//业务类型
		String typeflag=elecSystemDDL.getTypeflag();
		//数据项的值
		String [] itemnames=elecSystemDDL.getItemname();
		//2.如何判断业务逻辑标识（new和add）
		//如果typeflag=new 就新增一种数据类型
		if(StringUtils.isNotBlank(typeflag) && typeflag.equalsIgnoreCase("new")){
			//遍历页面传递过来的数据项的名称。组织PO对象，执行保存
			this.saveDDL(itemnames,keyword);
			
			
		}// 如果typeflag==add：在已有的数据类型基础上进行编辑和修改
		else{
			//* 使用数据类型，查询该数据类型对应的list，删除list
			List<ElecSystemDDL> list=this.findSysteDDLListByKeyword(keyword);
			elecSystemDDLDao.deleteObjectByCollection(list);
			this.saveDDL(itemnames, keyword);
		}
		
		
	}

	/**
	 *  抽取出来的代码  用来保存数据项的信息
	 * @param itemnames 封装数据项的值
	 * @param keyword 数据类型
	 */
	//* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
	private void saveDDL(String[] itemnames, String keyword) {
		// TODO Auto-generated method stub
		for(int i=0;i<itemnames.length;i++){
			ElecSystemDDL systemDDL=new ElecSystemDDL();
			systemDDL.setKeyword(keyword);
			systemDDL.setDdlName(itemnames[i]);
			systemDDL.setDdlCode(i+1);
			elecSystemDDLDao.save(systemDDL);
		}
	}

	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: 使用数据类型和数据项的编号，获取数据项的值
	* @Parameters: String keyword, 数据类型
	* 				String ddlCode，数据项的编号
	* @Return: 数据项的值
	*/
	@Override
	public String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode) {
		// TODO Auto-generated method stub
		return	elecSystemDDLDao.findDdlNameByKeywordAndDelCode(keyword, ddlCode);
	}

	@Override
	public String findDdlCodeByKeywordAndDdlName(String keyword, String ddlName) {
		// TODO Auto-generated method stub
		return elecSystemDDLDao.findDdlCodeByKeywordAndDdlName(keyword,ddlName);
	}

}
