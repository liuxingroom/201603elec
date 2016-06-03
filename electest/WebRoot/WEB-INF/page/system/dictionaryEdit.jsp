<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"  prefix="s"%>
<%@ taglib uri="http://openhome.cc/jstl/fake" prefix="f" %>

   <table cellSpacing="1" cellPadding="0" width="90%" align="center" bgColor="#f5fafe" border="0" >
    <tr>
     <td >
	   <table cellspacing="0"   cellpadding="1" rules="all" bordercolor="gray" border="1" id="dictTbl"
		    style="BORDER-RIGHT:gray 1px solid; BORDER-TOP:gray 1px solid; BORDER-LEFT:gray 1px solid; WIDTH:100%; WORD-BREAK:break-all; BORDER-BOTTOM:gray 1px solid; BORDER-COLLAPSE:collapse; BACKGROUND-COLOR:#f5fafe; WORD-WRAP:break-word">
					
				<tr style="FONT-WEIGHT:bold;FONT-SIZE:12pt;HEIGHT:25px;BACKGROUND-COLOR:#afd1f3">
					<td class="ta_01" align="center"  width="20%" height=22 background="${pageContext.request.contextPath }/images/tablehead.jpg">编号</td>
					<td class="ta_01" align="center"  width="60%" height=22 background="${pageContext.request.contextPath }/images/tablehead.jpg">名称</td>
					<td class="ta_01" align="center"  width="20%" height=22 background="${pageContext.request.contextPath }/images/tablehead.jpg">删除</td>					
				</tr>
			    <s:if test="#request.list!=null && #request.list.size()>0">
			    	<s:iterator value="#request.list" var="list">
			    		<tr>
				   			<td class="ta_01" align="center"  width="20%">
				   				<s:property value="#list.ddlCode"/>
				   			</td>
				  			<td class="ta_01" align="center"  width="60%">
				 				  <input id="<s:property value='#list.ddlCode'/>" name="itemname" type="text" value="<s:property value='#list.ddlName'/>"  size="45" maxlength="25">
				 		    </td>
				 		    <td class="ta_01" align="center"  width="20%">
				 		    	<!-- 第一种方式     该方式第一次初始化时  delTableRow函数中传递的都是1  等删除一个重新整合时 该函数传递的值才会发生变化-->
				  		 		<!-- <a href="#" onclick="delTableRow(1)"> -->
				  		 		<!-- 第二种方式     该方式第一次初始时  delTableRow函数中传的的值跟该行的编号一样-->
				  		 		<a href="#" onclick="delTableRow('<s:property value='#list.ddlCode'/>')">
								<img src="${pageContext.request.contextPath }/images/delete.gif" width="16" height="16" border="0" style="CURSOR:hand"></a>
				  			</td>
						</tr>
			    	</s:iterator>
			    </s:if>
			     <s:else>
			     	<tr>
					   <td class="ta_01" align="center"  width="20%">1</td>
					   <td class="ta_01" align="center"  width="60%">
					   <input name="itemname" type="text"  size="45" maxlength="25"></td>
					   <td class="ta_01" align="center"  width="20%"></td>
					</tr>
			     </s:else>
	     </table>
	   </td>
	 </tr>
  <tr>
     <td >   
	 </td>
 </tr>
 <TR height=10><td colspan=3></td></TR>
  <tr>
     <td align="center" colspan=3>
     	<!-- 通过自定义标签来设置权限 -->
     	<f:if pattern="ec">
       		<input type="button" name="saveitem" value="保存" style="font-size:12px; color:black; height=20;width=50" onClick="saveDict()">
	 	</f:if>
	 </td>
 </tr>
 
       <input type="hidden" name="keywordname" >
       <input type="hidden" name="typeflag" >
	 
  </table>
