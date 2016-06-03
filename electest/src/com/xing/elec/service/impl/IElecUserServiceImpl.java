package com.xing.elec.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;


import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xing.elec.dao.ElecExportFieldsDao;
import com.xing.elec.dao.ElecSystemDDLDao;
import com.xing.elec.dao.IElecUserDao;
import com.xing.elec.dao.IElecUserFileDao;
import com.xing.elec.domain.ElecExportFields;
import com.xing.elec.domain.ElecRole;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.domain.ElecUserFile;
import com.xing.elec.service.IElecUserService;
import com.xing.elec.utils.FileUtils;
import com.xing.elec.utils.ListUtils;
import com.xing.elec.utils.MD5keyBean;
import com.xing.elec.utils.PageInfo;

@Service(IElecUserService.SERVICE_NAMAE)
public class IElecUserServiceImpl implements IElecUserService{

	/**注入用户表Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**用户附表Dao*/
	@Resource(name=IElecUserFileDao.SERVICE_NAME)
	IElecUserFileDao elecUserFileDao;
	
	/**数据字典Dao*/
	@Resource(name=ElecSystemDDLDao.SERVICE_NAME)
	ElecSystemDDLDao elecSystemDDLDao;
	
	/**文件导出Dao*/
	@Resource(name=ElecExportFieldsDao.SERVICE_NAME)
	ElecExportFieldsDao elecExportFieldsDao;
	
	/**  
	* @Name: findUserListByCondition
	* @Description: 组织查询条件，查询用户列表
	* @Parameters: ElecUser:VO对象
	* @Return: List<ElecUser>：用户集合
	*/
	@Override
	public List<ElecUser> findUserListByCondition(ElecUser elecUser) {
		
		
		// TODO Auto-generated method stub
		//组织查询条件
		String condition="";
		List<Object> paramsList=new ArrayList<Object>();
		//用户名称
		String userName=elecUser.getUserName();
		if(StringUtils.isNotBlank(userName)){
			condition+=" and o.userName like ? ";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID =elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition+=" and o.jctID=? ";
			paramsList.add(jctID);
		}
		
		//入职开始时间
		Date onDutyDateBegin=elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition+=" and o.onDutyDateBegin >=?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd=elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition+=" and o.onDutyDateEnd <=?";
			paramsList.add(onDutyDateEnd);
		}
		
		Object [] params=paramsList.toArray();
		//排序（按照入职时间的升序排序）
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.onDutyDate", "asc");
		/***方案一：查询用户表，在转换数据字典表*/
		/*List<ElecUser> list=elecUserDao.findCollectionByConditionNoPage(condition, params, orderBy);
		*/
		
		/**2014-12-9,添加分页 begin*/
		PageInfo pageInfo = new PageInfo(ServletActionContext.getRequest());
		List<ElecUser> list = elecUserDao.findCollectionByConditionWithPage(condition, params, orderBy,pageInfo);
		//把当前页面的信息放到request域中
		ServletActionContext.getRequest().setAttribute("page", pageInfo.getPageBean());
		/**2014-12-9,添加分页 end*/
		
		/**
		 * 数据字典的转换
		 * 使用数据类型和数据项的编号，查询数据字典，获取数据项的值
		 */
		this.convertSystemDDL(list);
		
		/**方案二：直接使用sql语句，完成1条语句操作用户表，检索出结果*/
		//List<ElecUser> list=elecUserDao.findCollectionByConditionNoPageWithSql(condition,params,orderBy);
		return list;
	}

	/**使用数据类型和数据项的编号，查询数据字典，获取数据项的值*/
	private void convertSystemDDL(List<ElecUser> list) {
		// TODO Auto-generated method stub
		if(list!=null && list.size()>0){
			for(ElecUser elecUser:list){
				//性别
				String sexID=elecSystemDDLDao.findDdlNameByKeywordAndDelCode("性别",elecUser.getSexID());
				elecUser.setSexID(sexID);
				
				//职位
				String postID=elecSystemDDLDao.findDdlNameByKeywordAndDelCode("职位", elecUser.getPostID());
				elecUser.setPostID(postID);
			}
		}
	}

	/**  
	* @Name: checkUser
	* @Description: 验证登录名是否存在
	* @Parameters: String：登录名
	* @Return: String
	* 判断登录名是否出现重复，返回一个标识message属性
		  * message=1：表示登录名为空，不可以保存
		  * message=2：表示登录名在数据库中已经存在，不可以保存
		  * message=3：表示登录名在数据库中不存在，可以保存
	*/
	@Override
	public String checkUser(String logonName){
		// TODO Auto-generated method stub
		String message="";
		if(StringUtils.isNotBlank(logonName)){
			//以登陆名作为查询条件，查询数据库
			String condition=" and o.logonName=?";
			Object[] params={logonName};
			List<ElecUser> list=elecUserDao.findCollectionByConditionNoPage(condition, params, null);
			//表示数据库存在登陆名的记录
			if(list!=null && list.size()>0){
				message="2";
			}else{//数据库不存在该登陆名的记录，可以保存
				message="3";
			}
		}else{//传递的参数（登陆名）为空
			message="1";
		}
		
		return message;
	}

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveUser(ElecUser elecUser) {
		// TODO Auto-generated method stub
		// 1：遍历多个附件，组织附件的PO对象，完成文件上传，保存用户的附件（多条数据），建立附件表和用户表的关联关系
		this.saveUserFiles(elecUser);
		//添加md5的密码加密
		this.md5password(elecUser);
		//获取页面传过来的用户ID
		String userID=elecUser.getUserID();
		if(StringUtils.isNotBlank(userID)){//如果用户id为空
			elecUserDao.update(elecUser);
		}else{
			elecUserDao.save(elecUser);
		}
	}
	
	/**添加MD5的密码加密*/
	private void md5password(ElecUser elecUser) {
		// TODO Auto-generated method stub
		//获取加密前的密码
		String logonPsw=elecUser.getLogonPwd();
		//加密后的密码
		String md5password="";
		//如果没有填写密码，设置初始密码为123
		if(StringUtils.isBlank(logonPsw)){
			logonPsw="123";
		}
		//获取修改用户之前的密码
		String password=elecUser.getPassword();
		//编辑的时候，没有修改密码的时候，是不需要加密的
		if(password!=null && password.equals(logonPsw)){
			md5password=logonPsw;
		}//新增的时候，或者是编辑的时候修改密码的时候，需要加密
		else{
			//使用md5加密
			MD5keyBean md5keyBean=new MD5keyBean();
			md5password=md5keyBean.getkeyBeanofStr(logonPsw);
		}
		//放置到ElecUser对象中
		elecUser.setLogonPwd(md5password);
		
	}
	
	//文件上传练习
	private void saveUserFiles2(ElecUser elecUser){
		//获取上传时间
		Date progressTime=new Date();
		//获取上传文件
		File [] uploads=elecUser.getUploads();
		//获取上传文件的名字
		String [] fileNames=elecUser.getUploadsFileName();
		//获取上传文件的类型
		String [] contentTypes=elecUser.getUploadsContentType();
		//遍历要上传的文件  然后将每个文件上传
		for(int i=0;i<uploads.length;i++){
			ElecUserFile elecUserFile=new ElecUserFile();
			//在多的一方设置关联
			elecUserFile.setElecUser(elecUser);
			elecUserFile.setFileName(fileNames[i]);
			//设置上传时间
			elecUserFile.setProgressTime(progressTime);
			//将文件上传同时获取上传文件的路径
			String fileURL=FileUtils.findUploadReturnPath2(uploads[i], fileNames[i],"用户管理" );
			elecUserFile.setFileURL(fileURL);
			elecUserFileDao.save(elecUserFile);
		}
	}
	
	
	//遍历多个附件，组织附件的PO对象，完成文件上传，保存用户的附件（多条数据），建立附件表和用户表的关联关系
	private void saveUserFiles(ElecUser elecUser) {
		// TODO Auto-generated method stub
		//上传时间
		Date progressTime=new Date();
		//获取上传的文件
		File [] uploads=elecUser.getUploads();
		//获取上传的文件名
		String [] fileNames=elecUser.getUploadsFileName();
		//获取上传的文件类型
		String [] contentTypes=elecUser.getUploadsContentType();
		//遍历
		if(uploads!=null && uploads.length>0){
			for(int i=0;i<uploads.length;i++){
				//组织附件的PO对象
				ElecUserFile elecUserFile=new ElecUserFile();
				//在多的一方设置关联
				elecUserFile.setElecUser(elecUser);
				elecUserFile.setFileName(fileNames[i]);//上传的文件名
				elecUserFile.setProgressTime(progressTime);
				/**将文件上传同时返回路径path*/
				String fileURL=FileUtils.findUploadReturnPath(uploads[i],fileNames[i],"用户管理");
				elecUserFile.setFileURL(fileURL);//上传路径（保存，应用与下载）
				elecUserFileDao.save(elecUserFile);
				
			}
		}
		
	}

	/**  
	* @Name: findUserFileByID
	* @Description: 使用用户附件ID，查询用户附件对象
	* @Parameters: String：用户附件ID
	* @Return: ElecUserFile：用户附件信息
	*/
	@Override
	public ElecUser finUserByID(String userID) {
		// TODO Auto-generated method stub
		return elecUserDao.findObjectByID(userID);
	}
	
	
	/**  
	* @Name: findUserFileByID
	* @Description: 使用用户附件ID，查询用户附件对象
	* @Parameters: String：用户附件ID
	* @Return: ElecUserFile：用户附件信息
	*/
	@Override
	public ElecUserFile findUserFileByID(String fileID) {
		// TODO Auto-generated method stub
		return elecUserFileDao.findObjectByID(fileID);
	}
	
	
	/**  
	* @Name: deleteUserByID
	* @Description: 删除用户信息
	*  	 1：删除该用户对应的文件
		 2：删除该用户对应的用户附件表数据
		 3：删除用户表的信息
	* @Parameters: ElecUser:vo对象
	* @Return: 无
	*/
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void deleteUserByID(ElecUser elecUser) {
		// TODO Auto-generated method stub
		//获取ID String userID 如果是多个值，struts2 默认采用", "的形式
		String userID=elecUser.getUserID();
		String [] userIDs=userID.split(", ");
		if(userIDs!=null && userIDs.length>0){
			//获取每个用户ID
			for(String uuid:userIDs){
				//使用用户ID，查询用户对象，获取当前用户具有的附件
				ElecUser user=elecUserDao.findObjectByID(uuid);
				Set<ElecUserFile> elecUserFiles=user.getElecUserFiles();
				//遍历用户的附件
				if(elecUserFiles!=null && elecUserFiles.size()>0){
					for(ElecUserFile elecUserFile:elecUserFiles){
						//1.删除该用户对应的文件
						//获取路径
						String path=ServletActionContext.getServletContext().getRealPath(elecUserFile.getFileURL());
						File file=new File(path);
						if(file.exists()){//如果文件存在
							//删除文件
							file.delete();
						}
						//删除每个用户的附件
						//2：删除该用户对应的用户附件表数据(级联删除)
						//elecUserFileDao.deleteObjectByIds(elecUserFile.getFileID());
						//<set name="elecUserFiles" table="Elec_User_File" inverse="true" order-by="progressTime desc" cascade="delete">
					}
				}
				
				/**
				 * 由于ElecUser的映射文件 的关联关系设置 为（inverse="true"） 也就是说   通过角色可以操作用户角色表  而通过用户不能操作用户角色表   
				 *   如果用户想操作用第三张表（用户角色表） 需要用户通过获取角色的set集合    来获取角色   进而操作第三张表（用户角色表）
				 */
				//删除用户角色表中的关联关系
				Set<ElecRole> elecRoles=user.getElecRoles();
				if(elecRoles!=null && elecRoles.size()>0){
					for(ElecRole elecRole:elecRoles){
						//(通过角色获取封装用户的set集合   来删除用户)
						elecRole.getElecUsers().remove(user);
					}
				}
				
			}
		}
		
		//3.删除用户表的信息
		elecUserDao.deleteObjectByIds(userIDs);
	}

	/**  
	* @Name: findUserByLogonName
	* @Description: 使用登录名作为查询条件，查询登录名对应的用户信息
	* @Parameters: String：登录名
	* @Return: ElecUser：用户对象
	*/
	@Override
	public ElecUser finUserByLogonName(String name) {
		// TODO Auto-generated method stub
		//查询条件
		String condition=" and o.logonName=?";
		List<Object> paramsList=new ArrayList<Object>();
		if(StringUtils.isNotBlank(name)){
			paramsList.add(name);
		}
		Object [] params=paramsList.toArray();
		//查询用户信息
		List<ElecUser> list=elecUserDao.findCollectionByConditionNoPage(condition, params, null);
		//返回唯一的值
		ElecUser elecUser=null;
		if(list!=null && list.size()>0){
			elecUser=list.get(0);
		}
		return elecUser;
	}

	/**  
	* @Name: findFieldNameWithExcel
	* @Description: 获取excel的标题字段，通过导出设置表（动态导出）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-09（创建日期）
	* @Parameters: 无
	* @Return: ArrayList<String>：excel的标题：
	* 			
			例如：
			  fieldName.add("登录名");
			  fieldName.add("用户姓名");
			  ...
	*/
	@Override
	public ArrayList<String> findFileNameWithExcel() {
		// TODO Auto-generated method stub
		//查询导出设置表，获取导出的中文字段作为标题
		ElecExportFields elecExportFields=elecExportFieldsDao.findObjectByID("5-1");
		//获取导出的中文字段（格式登录名#用户姓名#性别#联系电话#入职时间#职位）
		String zName=elecExportFields.getExpNameList();
		ArrayList<String> fieldName=(ArrayList<String>) ListUtils.stringToList(zName, "#");
		return fieldName;
	}
	
	
	/**  
	* @Name: findFieldDataWithExcel
	* @Description: 获取excel的数据字段，通过导出设置表（动态导出）
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-09（创建日期）
	* @Parameters: 无
	* @Return: ArrayList<ArrayList<String>>：excel的内容：
	* 			
			例如：
			  ArrayList<String> data1（存放每条数据）
			    data1.add("liubei");
			    data1.add("刘备");
			   
			    ArrayList<String> data2（存放每条数据）
			    data2.add("zhugeliang");
			    data2.add("诸葛亮");
			
			   fieldData.add(data1);
			   fieldData.add(data2);
	*/
	@Override
	public ArrayList<ArrayList<String>> finFieldDateWithExcel(ElecUser elecUser) {
		// TODO Auto-generated method stub
		//组织数据
		ArrayList<ArrayList<String>> fieldData=new ArrayList<ArrayList<String>>();
		//组织投影查询的条件（从导出设置表的导出的英文字段获取）
		//查询导出设置表，获取导出的中文字段作为标题
		ElecExportFields elecExportFields=elecExportFieldsDao.findObjectByID("5-1");
		//获取导出的中文字段（格式：登录名#用户姓名#性别#联系电话#入职时间#职位）
		String zName=elecExportFields.getExpNameList();
		List<String> zList=ListUtils.stringToList(zName, "#");
		//胡群殴导出的英文字段（格式：logonName#userName#sexID#contactTel#onDutyDate#postID）
		String eName=elecExportFields.getExpFieldName();
		//组织投影的条件，使用,号替换#号
		String selectCondition=eName.replace("#", ",");
		/******************************************************/
		
		//组织查询条件
		String condition="";
		List<Object> paramsList=new ArrayList<Object>();
		
		//用户名称
		String userName=elecUser.getUserName();
		try {
			userName=URLDecoder.decode(userName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//入职开始时间
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object[] params=paramsList.toArray();
		//排序（按照入职时间的升序排序）
		Map<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("o.onDutyDate", "asc");
		/**查询数据结果，list中存放的是所有的数据*/
		List list=elecUserDao.findCollectionByConditionNoPageWithSelectCondition(condition,params,orderBy,selectCondition);
		//组织poi报表需要的数据ArrayList<ArrayList<String>>
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				//使用数组用来存放每一行的数据
				Object []arrays=null;
				//使用投影查询是多个字段，此时应该返回Object []
				if(selectCondition.contains(",")){//用来判断导出的数据是几个字段  如果含有','表示导出的是多个字段  如果不含有','表示导出的是一个字段
					arrays=(Object[]) list.get(i);
				}
				//说明投影查询是一个字段，此时应该返回Object对象
				else{
					arrays=new Object[1];
				}
				//将Object[] 转换成ArrayList<String>,用来存放每一行的数据
				ArrayList<String> data=new ArrayList<String>();
				if(arrays!=null && arrays.length>0){
					for(int j=0;j<arrays.length;j++){
						//存放每个字段的值
						Object o=arrays[j];
						if(zList!=null && zList.get(j).equals("性别") || zList.get(j).equals("所属单位") || zList.get(j).equals("是否在职") || zList.get(j).equals("职位")){
							//如果遍历的每一行数据  属于性别，属单位 ，是否在职， 职位 这一类的数据   则需要查询数据字典 进行转换
							//集合arrays集合中存放的数据顺序  跟标题（导出的中文字段）的顺序是一样的
							data.add(o!=null?elecSystemDDLDao.findDdlNameByKeywordAndDelCode(zList.get(j), o.toString()):"");
						}//否则的  数据就可以将数据直接显示
						else{
							data.add(o!=null?o.toString():"");
						}
					}
				}
				//将每一行的数据，组织成ArrayList<ArrayList<String>>
				fieldData.add(data);
			}
		}	
		
		return fieldData;
	}
	
	
	/**  
	* @Name: saveUserList
	* @Description: 保存一个用户的集合
	* @Parameters: saveUserList：保存用户集合
	* @Return:无
	*/
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveUserList(List<ElecUser> userList) {
		// TODO Auto-generated method stub
		elecUserDao.saveList(userList);
	}

	@Override
	public List<Object[]> chartUser(String zName, String eName) {
		// TODO Auto-generated method stub
		return elecUserDao.chartUser(zName,eName);
	}
}
