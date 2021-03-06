package com.xing.elec.web.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xing.elec.utils.ExcelFileGenerator;
import com.xing.elec.domain.ElecSystemDDL;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.domain.ElecUserFile;
import com.xing.elec.service.ElecSystemDDLService;
import com.xing.elec.service.IElecUserService;
import com.xing.elec.utils.AnnotationLimit;
import com.xing.elec.utils.ChartUtils;
import com.xing.elec.utils.DateUtils;
import com.xing.elec.utils.GenerateSqlFromExcel;
import com.xing.elec.utils.MD5keyBean;
import com.xing.elec.utils.ValueUtils;


@SuppressWarnings("serial")
@Controller("elecUserAction")
@Scope(value="prototype")
public class ElecUserAction extends BaseAction<ElecUser>{
	ElecUser elecUser=this.getModel();
	/**注入用户Service*/
	@Resource(name=IElecUserService.SERVICE_NAMAE)
	IElecUserService elecUserService;
	
	/**注入数据字典Service*/
	@Resource(name=ElecSystemDDLService.SERVICE_NAME)
	ElecSystemDDLService elecSystemDDLService;
	
	/**  
	* @Name: home
	* @Description: 用户管理的首页显示
	* @Parameters: 无
	* @Return: String：跳转到system/userIndex.jsp
	*/
	@AnnotationLimit(mid="an",pid="am")
	public String home(){
		//1.加载数据类型是所属单位的数据字典的集合，遍历在页面的下拉菜单
		List<ElecSystemDDL> list=elecSystemDDLService.findSysteDDLListByKeyword("所属单位");
		request.setAttribute("list",list);
		//2.组织页面中传递的查询条件，查询用户表，返回List<ElecUser>
		List<ElecUser> userList=elecUserService.findUserListByCondition(elecUser);
		request.setAttribute("userList", userList);
		
		/**2014-12-9,添加分页 begin*/
		String initpage = request.getParameter("initpage");
		//执行ajax操作跳转到userList.jsp
		if(initpage!=null && initpage.equals("1")){
			return "list";
		}
		/**2014-12-9,添加分页 end*/
		
		return "home";
	}
	
	/**  
	* @Name: add
	* @Description: 跳转到用户管理的新增页面显示
	* @Parameters: 无
	* @Return: String：跳转到system/userAdd.jsp
	*/
	public String add(){
		//1.加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		return "add";
	}
	
	/**1：加载数据字典，用来遍历性别，职位，所属单位，是否在职*/
	private void initSystemDDL() {
		// TODO Auto-generated method stub
		List<ElecSystemDDL> sexList=elecSystemDDLService.findSysteDDLListByKeyword("性别");
		request.setAttribute("sexList", sexList);
		List<ElecSystemDDL> postList=elecSystemDDLService.findSysteDDLListByKeyword("职位");
		request.setAttribute("postList", postList);
		List<ElecSystemDDL> jctList=elecSystemDDLService.findSysteDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		List<ElecSystemDDL> isDutyList=elecSystemDDLService.findSysteDDLListByKeyword("是否在职");
		request.setAttribute("isDutyList", isDutyList);
	}
	
	
	/**  
	* @Name: findJctUnit
	* @Description: 使用jquery的ajax完成二级联动，使用所属单位，关联单位名称
	* @Parameters: 无
	* @Return: 使用struts2的json插件包
	*/
	public String findJctUnit(){
		//获取所属单位下的数据项的值
		String  jctID=elecUser.getJctID();
		//2.使用该值作为数据类型，查询对应数据字典的值，返回List<ElecSystemDDL>
		List<ElecSystemDDL> list=elecSystemDDLService.findSysteDDLListByKeyword(jctID);
		//3.将List<ElecSystemDDL>转换成json数组，将List集合放置到栈顶
		ValueUtils.putValueStack(list);
		return "findJctUnit";
	}
	
	/**  
	* @Name: checkUser
	* @Description: 使用jquery的ajax完成登录名的后台校验，判断是否数据库中存在，保证登录名惟一
	* @Parameters: 无
	* @Return: 使用struts2的json插件包
	*/
	public String checkUser(){
		//1.获取登陆名
		String logonName=elecUser.getLogonName();
		//2.判断登陆名是否出现重复
		String message=elecUserService.checkUser(logonName);
		elecUser.setMessage(message);//该设置方式  栈顶对象是 ElecUser 对象
		/**
		 * 对应的result（结果集）配置
		 * <result name="checkUser" type="json">	
		 *		<param name="includeProperties">message</param>
		 *	</result>
		 * 
		 * <result name="checkUser" type="json">
				<param name="root">message</param>
				
			</result>
		 */
		//ValueUtils.putValueStack(message);//栈顶对象是String类型的属性
		/**
		 * 对应的result（结果集）配置
		 * <result name="checkUser" type="json"></result>
		 */
		return "checkUser";
	}
	
	/**  
	* @Name: save
	* @Description: 保存用户
	* @Parameters: 无
	* @Return: 跳转到close.jsp
	*/
	public String save(){
		elecUserService.saveUser(elecUser);
		
		/**
		 * 用来判断
		 * roleflag==null：系统管理员操作编辑页面，此时保存：close.jsp
		 * roleflag==1：如果不是系统管理员操作编辑页面，此时保存：重定向到编辑页面
		 */
		String roleflag=elecUser.getRoleflag();
		if(roleflag!=null && roleflag.equals("1")){
			return "redirectEdit";
		}
		
		return "close";
	}
	
	/**  
	* @Name: edit
	* @Description: 跳转到编辑页面
	* @Parameters: 无
	* @Return: 跳转到system/userEdit.jsp
	*/
	public String edit(){
		//获取用户ID
		String userID=elecUser.getUserID();
		//1.使用用户ID，查询对象ElecUser对象，支持表单回显
		ElecUser user=elecUserService.finUserByID(userID);
		//将VO对象的属性，放置到PO对象的属性值
		user.setViewflag(elecUser.getViewflag());
		user.setRoleflag(elecUser.getRoleflag());
		
		//2.jiang Elecuser对象放置到栈顶，页面使用struts2的标签支持回显
		ValueUtils.putValueStack(user);//栈顶对象
		//3.加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		//4.二级联动的表单回显
		//(1)获取到所属单位的编号
		String ddlCode=user.getJctID();
		//(2)使用所属单位和数据项的编号，获取数据项的值
		String ddlName=elecSystemDDLService.findDdlNameByKeywordAndDdlCode("所属单位",ddlCode);
		//(3)使用查询的数据项的值，作为数据类型，查询该数据类型的对应集合，返回List<ElecSystemDDL>
		List<ElecSystemDDL> jctUnitList=elecSystemDDLService.findSysteDDLListByKeyword(ddlName);
		request.setAttribute("jctUnitList", jctUnitList);
		return "edit";
	}
	
//	/**
//	 * 文件下载 （普通方式）
//	 */
//	public String noStrutsDownload(){
//		try{
//			//获取文件ID
//			String fileID=elecUser.getFileID();
//			//1. 使用文件id查询用户列表获取路径path
//			ElecUserFile elecUserFile=elecUserService.findUserFileByID(fileID);
//			//路径path
//			String path=ServletActionContext.getServletContext().getRealPath("")+elecUserFile.getFileURL();
//			//2. 使用路径path， 查找到对应的文件，转换成InputStream 
//			InputStream inputStream=new FileInputStream(new File(path));
//				//获取要下载文件的名字
//				String fileName=elecUserFile.getFileName();
//				//设置文件的编码格式  （用来防止文件下载时出现乱码）
//				fileName=new String(fileName.getBytes("GBK"),"iso8859-1");
//				//填写下载文件的头部信息
//				response.setHeader("Content-disposition", "attachment;filename="+fileName);
//			// 3.从响应对象Response中获取输出流OutputStream
//			OutputStream outputStream=response.getOutputStream();
//			//4.将输入流的数据读取，写入输入流中
//			//第一种方式
//			//IOUtils.copy(inputStream, outputStream);
//			//第二种方式
//			byte [] b=new byte[1024];
//			int a=0;
//			while((a=inputStream.read(b))!=-1){
//				outputStream.write(b, 0, b.length);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return "download";
//	}
	
	/**  
	* @Name: download
	* @Description: 文件下载（struts2的方式）
	* @Parameters: 无
	* @Return: struts2的结果类型
	*/
	public String download(){
		try {
			//获取文件ID
			String fileID=elecUser.getFileID();
			//1.使用文件ID，查询用户文件表，获取到路径path
			ElecUserFile elecUserFile= elecUserService.findUserFileByID(fileID);
			//路径path
			String path=ServletActionContext.getServletContext().getRealPath(elecUserFile.getFileURL());
			//文件名称
			String filename=elecUserFile.getFileName();
			//可以出现中文
			filename=new String(filename.getBytes("gbk"),"iso8859-1");
			request.setAttribute("filename", filename);
			
			//2.使用路径path，查找对应的文件，转成InputStream
			InputStream in=new FileInputStream(new File(path));
			//与栈顶的InputStream关联
			elecUser.setInputStream(in);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "download";
	}
	
	/**  
	* @Name: delete
	* @Description: 删除用户信息
	* @Parameters: 无
	* @Return: String 重定向到system/userIndex.jsp
	*/
	public String delete(){
		elecUserService.deleteUserByID(elecUser);
		/**
		 * 添加执行删除，传递当前页，这样做， 可以删除后定向到当前页
		 */
		request.setAttribute("pageNO", request.getParameter("pageNO"));
		return "delete";
	}
	
	/**  
	* @throws 、Exception 
	* @Name: exportExcel
	* @Description: 将数据通过查询条件，导出对应数据的excel报表
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-9（创建日期）
	* @Parameters: 无
	* @Return: 不使用struts2开发，导出
	*/
//	public String exportExcel()throws Exception{
//		//1.初始化数据
//		//excel 的标题数据（只有一条）
//		ArrayList<String> fieldName=elecUserService.findFileNameWithExcel();
//		//excel的内容数据（多条）
//		ArrayList<ArrayList<String>> fieldData=elecUserService.finFieldDateWithExcel(elecUser);
//		//2.调用封装的POI报表的导出类ExcelFileGenerator.java，完成excel报表的导出
//		ExcelFileGenerator excelFileGenerator=new ExcelFileGenerator(fieldName, fieldData);
//		
//		/**导出报表的文件名*/
//		String filename="用户报表（"+DateUtils.dateToStringWithExcel(new Date())+"）.xls";
//		//处理乱码
//		filename=new String(filename.getBytes("gbk"),"iso-8859-1");
//		
//		/**response中进行设置,总结下载，导出，需要io流和头*/
//		response.setContentType("application/vnd.ms-excel");
//		response.setHeader("Content-disposition", "attachment;filename="+filename);
//		response.setBufferSize(1024);
//		
//		//获取输出流
//		OutputStream outputStream=response.getOutputStream();
//		excelFileGenerator.expordExcel(outputStream);
//		return null;
//	}
	
	
	/**  
	* @throws 、Exception 
	* @Name: exportExcel
	* @Description: 将数据通过查询条件，导出对应数据的excel报表
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-9（创建日期）
	* @Parameters: 无
	* @Return: 使用struts2开发，导出
	*/
	public String exportExcel() throws Exception{
		//1.初始化数据
		//excel的标题数据（只有一条）
		ArrayList<String> fieldName=elecUserService.findFileNameWithExcel();
		//excel的内容数据
		ArrayList<ArrayList<String>> fieldData=elecUserService.finFieldDateWithExcel(elecUser);
		//2.调用封装的POI报表的导出类ExcelFieldGeneration.java，完成excel报表的导出
		ExcelFileGenerator excelFileGenerator=new ExcelFileGenerator(fieldName, fieldData);
		
		/**导出报表的文件名*/
		String filename="用户报表（"+DateUtils.dateToStringWithExcel(new Date())+"）.xls";
		//处理乱码
		filename=new String(filename.getBytes("gbk"),"utf-8");
		request.setAttribute("filename", filename);
		
		//获取输出流
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		excelFileGenerator.expordExcel(os);
		
		//将文件输出流os中的文件写到输入流中
		byte [] buf=os.toByteArray();
		ByteArrayInputStream in=new ByteArrayInputStream(buf);
		//将文件放到输入流中 InputStream （将文件放到栈顶）
		elecUser.setInputStream(in);
		return "exportExcel";
	}
	
	/**  
	* @Name: importPage
	* @Description: 跳转到导入页面
	* @Parameters: 无
	* @Return: 跳转到system/userImport.jsp
	*/
	public String importPage(){
		return "importPage";
	}
	
	/**  
	* @throws Exception 
	* @Name: importData
	* @Description: excel的数据导入
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-9（创建日期）
	* @Parameters: 无
	* @Return: 跳转到system/userImport.jsp
	*/
	public String importData() throws Exception{
		//获取上传的文件
		File formFile=elecUser.getFile();
		
		//读取excel的数据，将数据导入到数据库中
		GenerateSqlFromExcel fromExcel=new GenerateSqlFromExcel();
		//存放所有行的数据
		ArrayList<String[]> arrayList=fromExcel.generateUserSql(formFile);
		/**
		 * List<String> errorList:用来添加错误信息的集合
		 *   * 如果没有错误：errorList==null
		 *   * 如果存在错误：errorList有值
		 */
		
		List<String> errorList=new ArrayList<String>();
		//将ArrayList<String[]>转换成List<ElecUser>,	同时完成校验
		List<ElecUser> userList=this.fromExcelListToPOList(arrayList,errorList);
		//如果存在错误： errorList有值
		if(errorList!=null && errorList.size()>0){
			request.setAttribute("errorList", errorList);
		}
		//如果没有错误 errorList==null
		else{
			//执行保存
			elecUserService.saveUserList(userList);
		}
		return "importPage";
	}

	/**将ArrayList<String[]> 转换成List<ElecUser>*/
	private List<ElecUser> fromExcelListToPOList(ArrayList<String[]> arrayList,
			List<String> errorList) {
		// TODO Auto-generated method stub
		//返回的结果集
		List<ElecUser> userList=new ArrayList<ElecUser>();
		//遍历所有行的数据
		if(arrayList!=null && arrayList.size()>0){
			for(int i=0;i<arrayList.size();i++){
				//获取的是每一行的数据（模板上的字段：登录名	密码	用户姓名	性别	所属单位	联系地址	是否在职	出生日期	职位）
				String [] arrays=arrayList.get(i);
				//将每一行的数据封装成PO对象
				ElecUser elecUser=new ElecUser();
				//将值从数组中取出来，放置到PO对象中
				//登录名
				if(StringUtils.isNotBlank(arrays[0])){
					//校验登录名，再数据库中是否出现重复
					String message =elecUserService.checkUser(arrays[0]);
					//登录名不存在重复的，此时可以保存
					if(message!=null && message.equals("3")){
						elecUser.setLogonName(arrays[0]);
					}else{
						errorList.add("第"+(i+2)+"行，第"+(0+1)+"列，登录名在数据库中已经存在！");
					}
				}else{
					errorList.add("第"+(i+2)+"行，第"+(0+1)+"列，登录名不能为空！");
				}
				
				//密码
				if(StringUtils.isNotBlank(arrays[1])){
					//md5的密码加密
					MD5keyBean bean=new MD5keyBean();
					String logonPwd=bean.getkeyBeanofStr(arrays[1]);
					elecUser.setLogonPwd(logonPwd);
				}
				
				//用户姓名
				if(StringUtils.isNotBlank(arrays[2])){
					elecUser.setUserName(arrays[2]);
				}
				
				//性别
				if(StringUtils.isNotBlank(arrays[3])){
					//实现一个数据字典的转换，使用数据类型和数据项的值，获取数据行的编号
					String ddlCode=elecSystemDDLService.findDdlCodeByKeywordAndDdlName("性别", arrays[3]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setSexID(ddlCode);
					}else{
						errorList.add("第"+(i+2)+"行，第"+(3+1)+"列，性别在数据字典转换中不存在！");
					}
				}else{
					errorList.add("第"+(i+2)+"行，第"+(3+1)+"列，性别不能为空！");
				}
				
				//所属单位
				if(StringUtils.isNotBlank(arrays[4])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("所属单位",arrays[4]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setJctID(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(4+1)+"列，所属单位在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(4+1)+"列，所属单位不能为空！");
				}
				
				//联系地址
				if(StringUtils.isNotBlank(arrays[5])){
					elecUser.setAddress(arrays[5]);
				}
				
				//是否在职
				if(StringUtils.isNotBlank(arrays[6])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("是否在职",arrays[6]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setIsDuty(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(6+1)+"列，是否在职在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(6+1)+"列，是否在职不能为空！");
				}
				//出生日期
				if(StringUtils.isNotBlank(arrays[7])){
					Date birthday = DateUtils.stringToDate(arrays[7]);
					elecUser.setBirthday(birthday);
				}
				//职位
				if(StringUtils.isNotBlank(arrays[8])){
					//实现一个数据字典的转换,使用数据类型和数据项的值，获取数据项的编号
					String ddlCode = elecSystemDDLService.findDdlCodeByKeywordAndDdlName("职位",arrays[8]);
					if(StringUtils.isNotBlank(ddlCode)){
						elecUser.setPostID(ddlCode);
					}
					else{
						errorList.add("第"+(i+2)+"行，第"+(8+1)+"列，职位在数据字典转换中不存在！");
					}
				}
				else{
					errorList.add("第"+(i+2)+"行，第"+(8+1)+"列，职位不能为空！");
				}
				userList.add(elecUser);
			}
		}
		return userList;
	}
	
	/**  
	* @Name: chartUser
	* @Description: 用户统计
	* @Parameters: 无
	* @Return: 跳转到system/userReport.jsp
	*/
	public String chartUser(){
		//查询数据库，构造对应的数据集合
		List<Object[]> list=elecUserService.chartUser("所属单位","jctID");
		//1.使用Jfreechart生成图片，放置到chart的文件夹下，返回文件名
		String filename=ChartUtils.createBarChart(list);
		// *2：将文件名放置到request作用域中，传递给页面，页面可以使用：<img src="${pageContext.request.contextPath}/chart/${filename}" border="0">
		request.setAttribute("filename", filename);
		return "chartUser";
	}
	
	/**  
	* @Name: chartUserFCF
	* @Description: 用户统计（按照性别）技术FCF报表
	* @Parameters: 无
	* @Return: 跳转到system/userReportFCF.jsp
	*/
	public String chartUserFCF(){
		//查询数据库，获取图形需要数据集合
		List<Object[]> list=elecUserService.chartUser("性别", "sexID");
		//组织XML的数据
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<list.size();i++){
			/**b.keyword,b.ddlName,COUNT(b.ddlCode)*/
			Object[] objects=list.get(i);
			if(i==0){//组织第一个值
				String x="男女比例统计";
				String y="unit";//存在FusionChart中的一个问题，Y轴的显示不支持中文，所以我们用英文代替
				buffer.append("<graph caption='用户统计报表("+objects[0].toString()+")' xAxisName='"+x+"' bgColor='FFFFDD' yAxisName='"+y+"' showValues='1'  decimals='0' baseFontSize='18'  maxColWidth='60' showNames='1' decimalPrecision='0'> ");
				
			}
			buffer.append("<set name='"+objects[1].toString()+"' value='"+objects[2].toString()+"' color='AFD8F8'/>");
			if(i==list.size()-1){//组织最后一个值
				buffer.append("</graph>");
			}
		}
		request.setAttribute("chart", buffer);
		return "chartUserFCF";
	}
}

