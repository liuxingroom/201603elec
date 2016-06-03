package com.xing.elec.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.xing.elec.dao.IElecCommonMsgContentDao;
import com.xing.elec.dao.IElecCommonMsgDao;
import com.xing.elec.domain.ElecCommonMsg;
import com.xing.elec.domain.ElecCommonMsgContent;
import com.xing.elec.service.IElecCommonMsgService;
import com.xing.elec.utils.StringUtil;


@Service(IElecCommonMsgService.SERVICE_NAME)
public class IElecCommonMsgServiceImpl implements IElecCommonMsgService{
	/**运行监控表dao*/
	@Resource(name=IElecCommonMsgDao.SERVICE_NAME)
	IElecCommonMsgDao elecCommonMsgDao;
	
	/**运行监控数据表dao*/
	@Resource(name=IElecCommonMsgContentDao.SERVICE_NAME)
	IElecCommonMsgContentDao elecCommonMsgContentDao;

//	@Override
//	public ElecCommonMsg findElecCommonMsg() {
//		// TODO Auto-generated method stub
//		List<ElecCommonMsg> list=elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		ElecCommonMsg commonMsg=null;
//		if(list!=null && list.size()>0){
//			commonMsg=list.get(0);
//		}
//		return commonMsg;
//	}
//
//	@Override
//	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
//	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {
//		// TODO Auto-generated method stub
//		//1.查询数据库进行监控表的数据，返回List，用来判断数据是否存在
//		List<ElecCommonMsg> list=elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		/**
//		 * 如果数据存在 
//		 * 组织PO对象 ，执行update操作
//		 */
//		if(list!=null && list.size()>0){
//			/**使用快照更新*/
//			ElecCommonMsg commonMsg=list.get(0);
//			commonMsg.setStationRun(elecCommonMsg.getStationRun());
//			commonMsg.setDevRun(elecCommonMsg.getDevRun());
//			commonMsg.setCreateDate(new Date());
//		}else{
//			/**
//			 * 如果数据不存在
//			 * 组织PO对象，执行save操作
//			 */
//			elecCommonMsg.setCreateDate(new Date());
//			elecCommonMsgDao.save(elecCommonMsg);
//			
//		}
//	}
	
	//该方法是用来操作文本编译器中录入大量数据时使用的
	/**  
	* @Name: findElecCommonMsg
	* @Description: 获取运行监控中的数据
	* @Parameters: 无
	* @Return: ElecCommonMsg：封装数据对象
	*/
	@Override
	public ElecCommonMsg findElecCommonMsg(){
		List<ElecCommonMsg> list=elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		ElecCommonMsg commonMsg=null;
		if(list!=null && list.size()>0){
			commonMsg = list.get(0);
			/**********************************************begin**********************************************************/
			//获取数据内容
			//以类型作为条件，按照显示顺序升序排列，查询站点运行情况的数据
			String stationCondition = " and o.type=?";
			Object [] stationParams = {"1"};
			Map<String, String> stationOrderby = new LinkedHashMap<String, String>();
			stationOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> stationList = elecCommonMsgContentDao.findCollectionByConditionNoPage(stationCondition, stationParams, stationOrderby);
			//获取返回的数据（拼装之后）
			String stationContent = "";
			if(stationList!=null && stationList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:stationList){
					String content = elecCommonMsgContent.getContent();
					stationContent += content;
				}
			}
			//将数据赋值给页面的属性（站点运行情况）
			commonMsg.setStationRun(stationContent);
			/**********************************************************************************/
			//以类型作为条件，按照显示顺序升序排列，查询站点运行情况的数据
			String devCondition = " and o.type=?";
			Object [] devParams = {"2"};
			Map<String, String> devOrderby = new LinkedHashMap<String, String>();
			devOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> devList = elecCommonMsgContentDao.findCollectionByConditionNoPage(devCondition, devParams, devOrderby);
			//获取返回的数据（拼装之后）
			String devContent = "";
			if(devList!=null && devList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:devList){
					String content = elecCommonMsgContent.getContent();
					devContent += content;
				}
			}
			//将数据赋值给页面的属性（设备运行情况）
			commonMsg.setDevRun(devContent);
			/**********************************************end**********************************************************/
		}
		return commonMsg;
	}
	
	
	//该方法是用来操作文本编译器中录入大量数据时使用的
	/**  
	* @Name: saveElecCommonMsg
	* @Description: 保存运行监控 
	* @Parameters: ElecCommonMsg：封装保存的参数
	* @Return: 无
	*/
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveCommonMsg(ElecCommonMsg elecCommonMsg){
		/**********************************************begin**********************************************************/
		//保存到数据表中
		//删除之前的数据
		List<ElecCommonMsgContent> contentList=elecCommonMsgContentDao.findCollectionByConditionNoPage("", null, null);
		//如果运行监控数据表中存在数据
		if(contentList!=null && contentList.size()>0){
			elecCommonMsgContentDao.deleteObjectByCollection(contentList);
		}
		//从页面获取站点运行情况和设备运行情况，根据站点运行情况，和设备运行情况保存数据
		String stationRun=elecCommonMsg.getStationRun();
		String devRun=elecCommonMsg.getDevRun();
		//调用StringUtil方法，分割字符串
		List<String> stationList=StringUtil.getContentByList(stationRun, 50);
		if(stationList.size()>0 && stationList!=null){
			for(int i=0;i<stationList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent=new ElecCommonMsgContent();
				elecCommonMsgContent.setType("1");//1表示站点运行情况
				elecCommonMsgContent.setContent(stationList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		
		List<String> devList = StringUtil.getContentByList(devRun, 50);
		if(devList!=null && devList.size()>0){
			for(int i=0;i<devList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("2");//2表示设备运行情况
				elecCommonMsgContent.setContent(devList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		/**********************************************end**********************************************************/
		
		//查询运行监控表，获取运行监控表的数据，返回List<ElecCommonMsg> list，使用list作为判断数据库中是否存在数据
		List<ElecCommonMsg> list=elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		//如果list!=null:数据表表中存在数据，获取页面传递的2个参数，组织PO对象，执行更新（update）
		if(list!=null && list.size()>0){
			/**使用快照更新*/
			ElecCommonMsg commonMsg=list.get(0);
			commonMsg.setStationRun("1");//1 表示站点运行情况
			commonMsg.setDevRun("2");//2 表示设备运行情况
			commonMsg.setCreateDate(new Date());
		}//如果list==null 数据表 表中不存在数据 ，获取页面传递的2个参数，组织PO对象  执行新增(save)
		else{
			ElecCommonMsg commonMsg=new ElecCommonMsg();
			commonMsg.setCreateDate(new Date());
			commonMsg.setStationRun("1");//1 表示站点运行情况
			commonMsg.setDevRun("2");//2 表示设备运行情况
			elecCommonMsgDao.save(commonMsg);
		}
		
	}
}
