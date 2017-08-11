package com.cmp.res.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.Group;
import com.cmp.res.entity.Magazine;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.Role;
import com.cmp.res.entity.User;
import com.cmp.res.entity.UserGroupMapping;
import com.cmp.res.service.GroupService;
import com.cmp.res.service.PasswordHelper;
import com.cmp.res.service.RoleService;
import com.cmp.res.service.UserGroupMappingService;
import com.cmp.res.service.UserService;
import com.cmp.res.shiro.ShiroDBRealm;


//@RestController
@Controller
@RequestMapping("/user/")
public class UserController {
	
	public static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordHelper passwordHelper;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ShiroDBRealm shiroDbRealm;
	@Autowired
	private UserGroupMappingService userGroupMappingService;
	@Autowired
	private GroupService groupService;
	
	
	/**
	 * 检查用户信息冲突
	 * 
	 * @param attributeName  参数名
	 * @param attributeValue 参数值
	 * @param response
	 * @throws IOException
	 * 
	 * 
	 */
	@RequestMapping("chekUserInfoConflict")
	public void chekUserInfoConflict(
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "attributeName") String attributeName,
			@RequestParam(value = "attributeValue") String attributeValue,
			HttpServletResponse response) throws IOException{
		boolean conflictFlag=false;
		ReturnJson rj=new ReturnJson();
		rj.setSuccess(false);
		if("userName".equals(attributeName)){
			if(id==null){
			//ture表示有冲突
				conflictFlag="admin".equals(attributeValue)||userService.getUserByName(attributeValue)!=null;
				rj.setSuccess(conflictFlag);
			}else{
				User user=userService.findUserById(id);
				rj.setSuccess(user!=null&&!user.getUserName().equals(attributeValue));
			}
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(rj));
	}
	
	/**
	 * 添加修改用户
	 * @param userName
	 * @param password
	 * @param nickName
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("add")
	public void addUser(
			@RequestParam(value = "userId", required = false) Long id,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "groupids", required = false) String groupids,
			HttpServletResponse response) throws IOException{
		ReturnJson returnJson=new ReturnJson();
		StringBuffer sb=new StringBuffer();
		List<Long> ids=getCids(groupids);
		if(id==null){
			//新增用户
			//密码参数校验参数
			Pattern pattern=Pattern.compile("^[0-9A-Za-z!@#$%^&*]{8,20}$");
			Matcher matcher = pattern.matcher(password); 
	        boolean passwordFlag = matcher.matches(); 
	        //登录名名校验
	        pattern=Pattern.compile("^[a-zA-Z0-9]{6,10}$");
	        matcher=pattern.matcher(userName); 
	        boolean userNameFlag = matcher.matches();
	        //昵称校验
	        pattern=Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$");
	        matcher=pattern.matcher(nickName); 
	        boolean nickNameFlag = matcher.matches();
	        // 
	        //
			if(!passwordFlag){
				returnJson.setSuccess(false);
				sb.append("非法的密码格式;");
			}
			if(!userNameFlag){
				returnJson.setSuccess(false);
				sb.append("非法的用户名格式;");
			}
			if(!nickNameFlag){
				returnJson.setSuccess(false);
				sb.append("非法的用户昵称格式;");
			}
			if(returnJson.getSuccess()){
				User user=new User();
				user.setNickName(nickName);
				user.setPassword(password);
				user.setUserName(userName);
				try{
					userService.add(user);
					
					if(ids!=null&&ids.size()>0){
						for(Long cid:ids){
							Group group= groupService.findById(cid);
							if(group!=null){
								UserGroupMapping ugm=createUserGroupMappingEntry(user,group);
								userGroupMappingService.save(ugm);
							}
							
						}
					}
					
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					returnJson.setSuccess(false);
					returnJson.setMsg("用户保存异常");
				}
				
			}
			
			
			
			
		}else{
			//检验用户昵称
			Pattern pattern=Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$");
			Matcher matcher=pattern.matcher(nickName); 
	        boolean nickNameFlag = matcher.matches();
			
	        if(nickNameFlag){
				//修改用户
				User user=userService.findUserById(id);
				if(user!=null){
					user.setNickName(nickName);
					try{
						userService.add(user);
						userGroupMappingService.deleteByUserid(id);
						
						if(ids!=null&&ids.size()>0){
							for(Long cid:ids){
								Group group= groupService.findById(cid);
								if(group!=null){
									UserGroupMapping ugm=createUserGroupMappingEntry(user,group);
									userGroupMappingService.save(ugm);
								}
								
							}
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						returnJson.setSuccess(false);
						returnJson.setMsg("用户保存异常");
					}
					
				}else{
					returnJson.setSuccess(false);
					returnJson.setMsg("用户不存在");
				}
	        }else{
	        	returnJson.setSuccess(false);
				returnJson.setMsg("非法的用户昵称格式");
	        }
			
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(returnJson));
		
	}
	@RequestMapping("updateUserPwdV2")
	public void updateUserPwdV2(
			@RequestParam(value = "userName") String userName,//需要重置的用户账号
			@RequestParam(value = "oldPassword",required=false) String  oldPassword,
			@RequestParam(value = "newPassword") String  newPassword,HttpServletResponse response
			){
		ReturnJson returnJson=new ReturnJson();
		String currentUserName=SecurityUtils.getSubject().getPrincipal().toString();//当前登录用户
		User currentUser=userService.getUserByName(currentUserName);
		
		//admin用户不能修改密码
		if(!"admin".equals(userName)){
			//校验用户密码是否合法
			Pattern pattern=Pattern.compile("^[0-9A-Za-z!@#$%^&*]{8,20}$");
			Matcher matcher = pattern.matcher(newPassword); 
	        boolean passwordFlag = matcher.matches(); 
	        if(passwordFlag){
	        	if(currentUserName.equals(userName)){
	        		//自己重置自己密码
	        		String originalEncryption=currentUser.getPassword();
	        		currentUser.setPassword(oldPassword);
	        		passwordHelper.encryptPassword(currentUser);
	        		if(originalEncryption.equals(currentUser.getPassword())){
	        			currentUser.setPassword(newPassword);
	        			passwordHelper.encryptPassword(currentUser);
	        			userService.add(currentUser);
	        		}else{
	        			returnJson.setMsg("原密码错误");
	    	        	returnJson.setSuccess(false);
	        		}
	        	}else{
	        		//管理员重置他人密码
	        		if(SecurityUtils.getSubject().hasRole("userManager")||currentUserName.equals("admin")){
	        			User user=userService.getUserByName(userName);
	        			if(user!=null){
	        				user.setPassword(newPassword);
	        				passwordHelper.encryptPassword(user);
	        				userService.add(user);
	        				
	        			}else{
	        				returnJson.setMsg("用户不存在");
		    	        	returnJson.setSuccess(false);
	        			}
	        		}
	        		
	        	}
	        	
	        	
	        }else{
	        	returnJson.setMsg("新密码格式不合法");
	        	returnJson.setSuccess(false);
	        }
			
		}else{
			returnJson.setMsg("admin用户不允许修改密码");
        	returnJson.setSuccess(false);
		}
		
		
	}
	/**
	 * 重置用户密码
	 * @throws IOException 
	 */
	@RequestMapping("updateUserPwd")
	public void updateUserPwd(
			@RequestParam(value = "type") Integer type,//重置类型   0、普通用户重置自己密码   1、管理员重置密码
			@RequestParam(value = "userNames", required = false) String[] userNames,//需要重置的用户账号
			@RequestParam(value = "oldPassword",required=false) String  oldPassword,
			@RequestParam(value = "newPassword") String  newPassword,HttpServletResponse response) throws IOException{
		
		/**
		 * 此方法还需判断当前登录用户是否具有修改用户密码的权限         后续完善，当前仅判断用户等级
		 */
		
		ReturnJson returnJson=new ReturnJson();
		//校验用户密码是否合法
		Pattern pattern=Pattern.compile("^[0-9A-Za-z!@#$%^&*]{8,20}$");
		Matcher matcher = pattern.matcher(newPassword); 
        boolean passwordFlag = matcher.matches(); 
        if(passwordFlag){
        	//获取当前登录用户
        	String userName=SecurityUtils.getSubject().getPrincipal().toString();
        	User user=null;
        	if(!"admin".equals(userName)){//admin为内置用户
        		user=userService.getUserByName(userName);
        	}else{
        		user=new User();
        		user.setUserName("admin");
        		user.setUserLevel(-1);
        	}
        	
        	if(type!=null&&type==0){
        		//普通用户重置自己密码
        		
//        		admin为内置用户不允许修改密码
        		if(!"admin".equals(userName)){
	        		String originalEncryption=user.getPassword();
	        		user.setPassword(oldPassword);
	        		passwordHelper.encryptPassword(user);
	        		//判断原始密码和用户当前传递的密码是否一致
	        		if(originalEncryption.equals(user.getPassword())){
	        			user.setPassword(newPassword);
	        			userService.add(user);
	        			
	        		}else{
	        			returnJson.setMsg("用户原密码错误");
	                	returnJson.setSuccess(false);
	        		}
        		}else{
        			returnJson.setMsg("admin用户不允许修改密码");
                	returnJson.setSuccess(false);
        		}
        		
        	}else{
        		/**
        		 * 管理员重置普通用户密码
        		 * 
        		 */
        		StringBuffer sb=new StringBuffer();
        		if(userNames!=null&&userNames.length>0){
        			for(String name:userNames){
        				User tempUser=userService.getUserByName(name);
        				if(tempUser!=null){
        					//判断当前登录用户是否有权限修改所要修改的用户   用户是否拥有用户管理与角色
        					if(SecurityUtils.getSubject().hasRole("userManager")){
        						//有权限修改
        						tempUser.setPassword(newPassword);
        						userService.add(tempUser);
        					}else{
        						//无权限修改
        						returnJson.setSuccess(false);
            					sb.append("无权限修改"+name+"密码;");
        					}
        				}else{
        					returnJson.setSuccess(false);
        					sb.append(name+"密码修改失败;");
        				}
        			}
        		}else{
        			returnJson.setMsg("请选择要修改的用户");
                	returnJson.setSuccess(false);
        		}
        	
        		
        	}
        	
        	
        	
        	
        }else{
        	returnJson.setMsg("新密码格式不合法");
        	returnJson.setSuccess(false);
        }
		
        response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(returnJson));
		
	}
	
	@RequestMapping("saveUserRole")
	public void saveUserRole(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "roleIds", required = false) String roleIds,
			HttpServletResponse response) throws IOException{
		ReturnJson returnJson=new ReturnJson();
		User user=userService.findUserById(id);
		Integer userLevel=999999;
		Set<Role> roles=new HashSet<Role>();
		try{
		if(StringUtils.isNotBlank(roleIds)&&roleIds.split(";").length>0){
			for(String str:roleIds.split(";")){
				Role role=roleService.findById(Long.parseLong(str));
				if(role!=null){
					roles.add(role);
					if(role.getRoleLevel()<userLevel){
						userLevel=role.getRoleLevel();
					}
				}
			}
			
		}
		
		user.setRoles(roles);
		user.setUserLevel(userLevel);
		userService.add(user);
		shiroDbRealm.clearAllCachedAuthorizationInfo();
		
		}catch(Exception e){
			e.printStackTrace();
			returnJson.setSuccess(true);
			returnJson.setMsg("保存用户角色失败");
			
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(returnJson));
		
	}
	@RequestMapping("getUserList")
	public void getUserList(
			@RequestParam(value = "page", defaultValue="1") Integer pn,
			@RequestParam(value = "limit", defaultValue="20") Integer ps,
			HttpServletResponse response
			) throws IOException{
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(userService.searchUser(pn, ps)));
		
	}
	
	public UserGroupMapping createUserGroupMappingEntry(User user,Group group){
		UserGroupMapping ugm=new UserGroupMapping();
		ugm.setGroupId(group.getId());
		ugm.setNickName(user.getNickName());
		ugm.setUser(user);
		ugm.setUserId(user.getId());
		ugm.setUserLevel(user.getUserLevel());
		ugm.setUserName(user.getUserName());
		
		
		return ugm;
	}
	
	public List<Long> getCids(String idsStr){
		if(StringUtils.isNotBlank(idsStr)&&idsStr.split(";").length>0){
			List<Long> list=new ArrayList<Long>();
			for(String id:idsStr.split(";")){
				try{
					list.add(Long.parseLong(id));
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			return list;
		}else{
			return null;
		}
	}
	
	
	
	


}
