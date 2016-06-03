package com.xing.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xing.elec.dao.IElecTextDao;
import com.xing.elec.domain.ElecText;
import com.xing.elec.service.IElecTextService;


@Service(IElecTextService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecTextServiceImpl implements IElecTextService {
	
	@Resource(name=IElecTextDao.SERVICE_NAME)
	private IElecTextDao elecTextDao;
	
	/**保存测试表*/
	@Override
	@Transactional(readOnly=false)
	public void saveElecService(ElecText elecText) {
		// TODO Auto-generated method stub
		this.elecTextDao.save(elecText);
	}	
	
	/**指定查询条件，查询列表*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao层
		AND o.textName LIKE '%张%'   #Service层
		AND o.textRemark LIKE '%张%'   #Service层
		ORDER BY o.textDate ASC,o.textName DESC  #Service层
	 */
	@Override
	public List<ElecText> findCollectionByConditionNoPage(ElecText elecText) {
		// TODO Auto-generated method stub
		//查询条件
		String condition="";
		//查询条件所对应的参数
		List<Object> paramsList=new ArrayList<Object>();
		//判断 对象 elecText 的TextName值是否为空
		if(StringUtils.isNotBlank(elecText.getTextName())){//如果不为空
			condition+=" and o.textName like ? ";
			paramsList.add("%"+elecText.getTextName()+"%");
		}
		//判断对象elecText 的TextRemark值是否为空
		if(StringUtils.isNotBlank(elecText.getTextRemark())){
			condition+=" and o.textRemark like ?";
			paramsList.add("%"+elecText.getTextRemark()+"%");
		}
		//传递参数
		Object [] params=paramsList.toArray();
		//排序
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.textDate", "asc");
		orderBy.put("o.textName", "desc");
		//查询
		List<ElecText> list=elecTextDao.findCollectionByConditionNoPage(condition,params,orderBy);
		return list;
	}
	
}
