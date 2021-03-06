package com.xing.elec.web.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xing.elec.domain.ElecExportFields;
import com.xing.elec.domain.ElecFileUpload;
import com.xing.elec.service.ElecExportFieldsService;
import com.xing.elec.utils.ListUtils;

@SuppressWarnings({ "rawtypes", "serial" })
@Controller("elecExportFieldsAction")
@Scope(value="prototype")
public class ElecExportFieldsAction extends BaseAction<ElecExportFields>{
	ElecExportFields elecExportFields = this.getModel();
	
	/**注入导出设置的Service*/
	@Resource(name=ElecExportFieldsService.WEVICE_NAME)
	ElecExportFieldsService elecExportFieldsService;
	
	/**  
	* @Name: setExportFields
	* @Description: 跳转到导出设置的功能页面
	* @Parameters: 无
	* @Return: String：跳转到system/exportExcel.jsp
	*/
	public String setExportFields(){
		//获取主键
		String belongTo=elecExportFields.getBelongTo();
		//使用主键ID，查询设置表，获取导出设置的字段，返回ElecExportFields对象
		ElecExportFields elecExportFields= elecExportFieldsService.findExportFieldsByID(belongTo);
		/**
		 * 3：组织2个Map集合，分别存放导出的字段和未导出的字段
		   * map集合的key：表示英文的名称
		   * map集合的value：表示中文的名称
		 */
		Map<String,String> map=new LinkedHashMap<String, String>();
		Map<String,String> nomap=new LinkedHashMap<String,String>();
		//4：组织map集合（提示：数据库中存放的字符串的长度是一一对应的）
		//从对象exportFields中读取4个字段，按照#号分割，获取到4个List<String>，分别存放导出的中文和英文，未导出的中文和英文
		List<String> zList=ListUtils.stringToList(elecExportFields.getExpNameList(), "#");
		List<String> eList=ListUtils.stringToList(elecExportFields.getExpFieldName(), "#");
		List<String> nozList=ListUtils.stringToList(elecExportFields.getNoExpNameList(), "#");
		List<String> noeList=ListUtils.stringToList(elecExportFields.getNoExpFieldName(), "#");
		//zList和eList 一一对应
		if(zList!=null && zList.size()>0){
			for(int i=0;i<zList.size();i++){
				map.put(eList.get(i), zList.get(i));
			}
		}
		//nozList 和noeList一一对应
		if(nozList!=null && nozList.size()>0){
			for(int i=0;i<nozList.size();i++){
				nomap.put(noeList.get(i), nozList.get(i));
			}
		}
		
		//存放到request中
		request.setAttribute("map", map);
		request.setAttribute("nomap", nomap);
		return "setExportFields";
	}
	
	
	/**  
	* @Name: saveSetExportExcel
	* @Description: 保存更新导出设置的配置
	* @Parameters: 无
	* @Return: String：跳转到close.jsp（关闭子页面，刷新父页面）
	*/
	public String saveSetExportExcel(){
		//1.获取页面中传递的5个隐藏域的值，执行update操作
		elecExportFieldsService.saveSetExportExcel(elecExportFields);
		return "close";
	}
}
