package com.xing.elec.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xing.elec.dao.IElecPopedomDao;
import com.xing.elec.dao.IElecRoleDao;
import com.xing.elec.dao.IElecRolePopedomDao;
import com.xing.elec.dao.IElecUserDao;
import com.xing.elec.domain.ElecPopedom;
import com.xing.elec.domain.ElecRole;
import com.xing.elec.domain.ElecRolePopedom;
import com.xing.elec.domain.ElecUser;
import com.xing.elec.service.ElecRoleService;

@Service(ElecRoleService.SERVICE_NAME)
public class ElecRoleServiceImpl implements ElecRoleService{

	/**注入用户Dao*/
	@Resource(name=IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;
	
	/**注入角色Dao表*/
	@Resource(name=IElecRoleDao.SERVICE_NAME)
	IElecRoleDao elecRoleDao;
	
	/**注入权限表Dao*/
	@Resource(name=IElecPopedomDao.SERVICE_NAME)
	IElecPopedomDao elecPopedomDao;
 	
	/**角色权限表Dao*/
	@Resource(name=IElecRolePopedomDao.SERVICE_NAME)
	IElecRolePopedomDao elecRolePopedomDao;
	
	
	/**  
	* @Name: findAllRoleList
	* @Description: 查询系统中所有的角色
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 2014-12-03（创建日期）
	* @Parameters: 无
	* @Return: List<ElecRole>：角色集合
	*/
	@Override	
	public List<ElecRole> findAllRoleList() {
		// TODO Auto-generated method stub
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.roleID", "asc");
		List<ElecRole> list=elecRoleDao.findCollectionByConditionNoPage("", null, orderBy);
		return list;
	}


	/**  
	* @Name: findAllPopedomList
	* @Description: 查询系统中所有的权限（满足父中包含子的集合）
	* @Parameters: 无
	* @Return: List<ElecPopedom>：权限集合
	*/
	@Override
	public List<ElecPopedom> findAllPopedomList() {
		// TODO Auto-generated method stub
		//查询权限的集合，存放所有的tr，也就是pid=0的集合，父集合（SELECT * FROM elec_popedom o WHERE 1=1 AND o.pid='0'）
				//组织查询条件
		String condition=" and o.pid=?";
		Object [] params={"0"};
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.mid", "asc");
		List<ElecPopedom> list=elecPopedomDao.findCollectionByConditionNoPage(condition, params, orderBy);
		if(list!=null && list.size()>0){
			for(ElecPopedom elecPopedom:list){
				//获取权限id的值，这个值是下一个pid所对应的值（父子关系）
				String mid=elecPopedom.getMid();
				String condition1=" and o.pid=?";
				Object [] params1={mid};
				Map<String,String> orderBy1=new LinkedHashMap<String, String>();
				orderBy1.put("o.mid", "asc");
				List<ElecPopedom> list1=elecPopedomDao.findCollectionByConditionNoPage(condition1, params1, orderBy1);
				//将所有的集合添加到付中的集合属性中
				elecPopedom.setList(list1);
			}
		}
		return list;
	}


	@Override
	public List<ElecPopedom> finAllPopedomListByRoleID(String roleID) {
		// TODO Auto-generated method stub
		//1.查询系统中所有的权限，父权限用户遍历tr， 父权限对应的子集合，用来遍历td
		List<ElecPopedom> list=this.findAllPopedomList();
		//2.使用角色ID组织查询条件，查询角色权限关联表，返回List<ElecRolePopedom>
		String condition=" and o.roleID=?";
		Object [] params={roleID};
		List<ElecRolePopedom> popedomList=elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		
		/**
		 * 3：匹配，向ElecPopedom对象中设置flag属性值的操作
		    * 如果匹配成功，设置1
		    * 如果匹配不成功，设置2
		    使用包含的技术来匹配（List集合、String，contains()）
		 */
		//定义一个权限的字符串
		
		StringBuffer popedomBuffer=new StringBuffer();
		//遍历popedomList,将每个mid的值获取，组织成一个字符串
		if(popedomList!=null && popedomList.size()>0){
			for(ElecRolePopedom elecRolePopedom:popedomList){
				//获取mid的值
				String mid=elecRolePopedom.getMid();
				//组织成一个权限的字符串（格式aa@bb@ab）
				popedomBuffer.append(mid).append("@");
			}
			//去掉最后一个@
			popedomBuffer.deleteCharAt(popedomBuffer.length()-1);
		}
		//存放权限，该权限就是当前角色具有的权限
		String popedom=popedomBuffer.toString();
		//定义一个方法，完成匹配  ，迭代
		this.findPopedomResult(popedom,list);
		return list;
	}


	/**定义一个方法，完成匹配，  迭代*/
	private void findPopedomResult(String popedom, List<ElecPopedom> list) {
		// TODO Auto-generated method stub
		//遍历所有权限，权限中只定义tr的List
		if(list!=null && list.size()>0){
			for(ElecPopedom elecPopedom:list){
				//获取每个权限的ID
				String mid=elecPopedom.getMid();
				//匹配，向ElecPopedom对象中设置flag属性值的操作
			    //* 如果匹配成功，设置1
			    //* 如果匹配不成功，设置2
				if(popedom.contains(mid)){
					
					elecPopedom.setFlag("1");
				}else{
					elecPopedom.setFlag("2");
				}
				//获取父对应的子的集合
				List<ElecPopedom> childList=elecPopedom.getList();
				if(childList!=null && childList.size()>0){
					this.findPopedomResult(popedom, childList);
				}
			}
		}
		
	}
	
	/**  
	* @Name: findAllUserListByRoleID
	* @Description: 使用当前角色ID，查询系统中所有的用户，并显示（匹配）
	* @Parameters: String：角色ID
	* @Return: List<ElecUser>：用户集合
	*/
	@Override
	public List<ElecUser> findAllUserListByRoleID(String roleID) {
		// TODO Auto-generated method stub
		//1.查询系统中所有的用户List<ElecUser>
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.onDutyDate", "asc");
		List<ElecUser> list=elecUserDao.findCollectionByConditionNoPage("", null, orderBy);
		//2.使用角色ID，查询角色对象，返回ElecRole对象，通过Set集合获取  该用户具有的角色
		ElecRole elecRole=elecRoleDao.findObjectByID(roleID);
		Set<ElecUser> elecUsers=elecRole.getElecUsers();
		
		//遍历elecUsers,组织List<String> 存放ID
		List<String> idList=new ArrayList<String>();
		if(elecUsers!=null && elecUsers.size()>0){
			for(ElecUser elecUser:elecUsers){
				//用户ID
				String userID=elecUser.getUserID();
				idList.add(userID);
			}
			
		}
		//3.匹配
		if(list!=null && list.size()>0){
			for(ElecUser elecUser:list){
				//获取用户id
				String userID=elecUser.getUserID();
				if(idList.contains(userID)){
					elecUser.setFlag("1");
				}else{
					elecUser.setFlag("2");
				}
			}
		}
		return list;
	}
	
	/**  
	* @Name: saveRole
	* @Description: 保存用户角色关联表，保存角色权限关联表
	* @Parameters: ElecPopedom：VO对象
	* @Return: 无
	*/
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveRole(ElecPopedom elecPopedom) {
		// TODO Auto-generated method stub
		//获取角色ID
		String roleID=elecPopedom.getRoleID();
		//获取到权限的主键数组，（格式pid_mid，例如0_aa）
		String [] selectopers=elecPopedom.getSelectoper();
		//后去用户ID数组
		String [] selectuser=elecPopedom.getSelectuser();
		/** 操作角色权限关联表*/
		this.saveRolePropedom(roleID,selectopers);
		/**操作用户角色关联表*/
		this.saveUserRole(roleID,selectuser);
		
	}

	/**操作用户角色关联表*/
	private void saveUserRole(String roleID, String[] selectuser) {
		// TODO Auto-generated method stub
		ElecRole elecRole=elecRoleDao.findObjectByID(roleID);
		/**
		 *方案一  首先获取Set集合 
		 *	如果Set集合中有数据  就删除原来的数据  删除数据之后 再向集合中添加新的数据 
		 */
		/*Set<ElecUser> elecUsers=elecRole.getElecUsers();
		//解散Set集合的关联关系（删除中间表）
		if(elecUsers!=null && elecUsers.size()>0){
			elecUsers.clear();
		}
		if(selectuser!=null && selectuser.length>0){
			for(String userID:selectuser){
				ElecUser elecUser=new ElecUser();
				elecUser.setUserID(userID);//建立关联关系
				elecUsers.add(elecUser);
			}
		}*/
		
		/**
		 * 方案二 新建一个Set集合 向新的Set集合中添加数据
		 * 然后用新建的Set集合  替换到快照中的Set集合
		 */
		Set<ElecUser> elecUsers=new HashSet<ElecUser>();
		if(selectuser!=null && selectuser.length>0){
			for(String userID:selectuser){
				ElecUser elecUser=new ElecUser();
				elecUser.setUserID(userID);//建立关联关系
				elecUsers.add(elecUser);
			}
		}
		//替换到 快照中的Set集合
		elecRole.setElecUsers(elecUsers);
	}

	/**操作角色权限表*/
	private void saveRolePropedom(String roleID, String[] selectopers) {
		// TODO Auto-generated method stub
		//1.使用角色ID 组织查询条件，查询角色 权限关联表，返回List<ElecRolePopedom>
		String condition=" and o.roleID=?";
		Object [] params={roleID};
		List<ElecRolePopedom> list=elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		//删除之前的list
		if(list!=null && list.size()>0){
			elecRolePopedomDao.deleteObjectByCollection(list);
		}
		if(selectopers!=null && selectopers.length>0){
			for(String ids:selectopers){
				//ids（格式pid_mid，例如0_aa）
				String [] arrays=ids.split("_");
				//组织Po对象，执行保存
				ElecRolePopedom elecRolePopedom=new ElecRolePopedom();
				elecRolePopedom.setRoleID(roleID);
				elecRolePopedom.setMid(arrays[1]);
				elecRolePopedom.setPid(arrays[0]);
				elecRolePopedomDao.save(elecRolePopedom);
				
			}
		}
	}


	/**  
	* @Name: findPopedomByRoleIDs
	* @Description: 使用角色ID的Hashtable的集合，获取角色对应的权限并集
	* @Parameters: Hashtable：角色ID的集合
	* @Return: String：表示权限的字符串：
	* 存放的权限的mid（字符串的格式：aa@ab@ac@ad@ae）
	* 
	* 使用hql或者是sql语句：
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	@Override
	public String findPopedomByRoleIDs(Hashtable<String, String> ht) {
		// TODO Auto-generated method stub
		//组织查询条件
		String condition="";
		StringBuffer buffercondition=new StringBuffer();
		//遍历Hashtable
		if(ht!=null && ht.size()>0){
			for(Iterator<Entry<String, String>> ite=ht.entrySet().iterator();ite.hasNext();){
				Entry<String,String> entry=ite.next();
				buffercondition.append("'").append(entry.getKey()).append("'").append(",");
			}
			buffercondition.deleteCharAt(buffercondition.length()-1);
		}
		
		//查询条件 
		condition=buffercondition.toString();
		//组织查询
		List<Object> list=elecRolePopedomDao.findPopedomByRoleIDs(condition);
		//组织权限封装的字符串，
		StringBuffer buffer=new StringBuffer();
		if(list!=null && list.size()>0){
			for(Object o:list){
				buffer.append(o.toString()).append("@");
			}
			//删除最有一个@
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}


	/**  
	* @Name: findPopedomListByString
	* @Description: 使用权限的字符串，查询当前权限（当前用户）对应的权限集合
	* @Parameters: String：表示权限的字符串：（字符串的格式：aa@ab@ac@ad@ae）
	* @Return: List<ElecPopedom>：权限的集合
	*  SELECT * FROM elec_popedom o WHERE 1=1 AND  o.ismenu = TRUE AND o.MID IN ('aa','ab','ac','ad','ae')
	*/
	@Override
	public List<ElecPopedom> findPopedomListByString(String popedom) {
		// TODO Auto-generated method stub
		//组织查询条件
		String condition=" and o.isMenu=? and o.mid in('"+popedom.replace("@", "','")+"')";
		//组织参数
		Object [] params={"1"};
		Map<String,String> orderBy=new LinkedHashMap<String, String>();
		orderBy.put("o.mid", "asc");
		List<ElecPopedom> list=null;
		try {
			list=elecPopedomDao.findCollectionByConditionNoPage(condition, params, orderBy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	/**  
	* @Name: findRolePopedomByID
	* @Description: 使用角色ID，权限的code，父级权限的code查询角色权限关联表，判断当前操作是否可以访问Action上的方法
	* @Parameters: String roleID：角色ID
	* 			 , String mid：权限code
	* 			 , String pid：父级权限的code
	* @Return: boolean
	* 			true：可以访问
	* 			false：没有权限，此时拒绝访问
	*/
	@Override
	public boolean findRolePopedomByID(String roleID, String mid, String pid) {
		// TODO Auto-generated method stub
		//组织查询条件
		String condition="";
		List<Object> paramsList=new ArrayList<Object>();
		//判断角色ID是否为空
		if(StringUtils.isNotBlank(roleID)){
			condition+=" and o.roleID=?";
			paramsList.add(roleID);
		}
		//判断角色权限code是否为空
		if(StringUtils.isNotBlank(mid)){
			condition+=" and o.mid=?";
			paramsList.add(mid);
		}
		
		//判断父级权限的code
		if(StringUtils.isNotBlank(pid)){
			condition+=" and o.pid=?";
			paramsList.add(pid);
		}
		Object [] params=paramsList.toArray();
		//查询对应对象的角色权限信息
		List<ElecRolePopedom> list=elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		boolean flag=false;
		if(list!=null && list.size()>0){
			flag=true;
		}
		
		return flag;
	}
}
