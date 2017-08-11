package com.cmp.res.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.Menu;
import com.cmp.res.entity.Resource;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.Role;
import com.cmp.res.entity.User;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.MenuService;
import com.cmp.res.service.ResourceService;
import com.cmp.res.service.RoleService;
import com.cmp.res.service.UserService;
import com.cmp.res.shiro.ShiroDBRealm;

@Controller
@RequestMapping("/role/")
public class RoleController {
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ShiroDBRealm shiroDbRealm;
	@Autowired
	private UserService userService;
	
	@RequestMapping("getHaveRole")
	public void getHaveRole(
			@RequestParam(value = "userId") Long userId,HttpServletResponse response) throws IOException{
		User user=userService.findUserById(userId);
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(new ArrayList<Role>(user.getRoles())));
		
	}
	@RequestMapping("checkRoleName")
	public void checkRoleName(
			@RequestParam(value = "roleId") Long id,
			@RequestParam(value = "roleName") String roleName,HttpServletResponse response) throws IOException{
		boolean flag=true;
		if(id==null){
			flag=roleService.findByName(roleName)!=null;
		}else{
			Role role=roleService.findByName(roleName);
			if(role==null){
				flag=false;
			}else{
				if(role.getId()==id){
					flag=false;
				}
			}
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(flag));
		
	}
	@RequestMapping("getUnHaveRole")
	public void getUnHaveRole(@RequestParam(value = "userId") Long userId,HttpServletResponse response) throws IOException{
		User user=userService.findUserById(userId),loginUser=commonService.getCurrentLogin();
		
		List<Role> allRole=roleService.getRole(loginUser.getUserLevel()),//当前登录用户可以授权的角色
				   currentHaveRole=new ArrayList<Role>(user.getRoles()),//当前已经拥有的角色
				   currentUnHaveRole=new ArrayList<Role>();//当前没有拥有的角色
		Map<Long,Role> hasRoleMap=new HashMap<Long,Role>();
		
		
		for(Role role:currentHaveRole){
			hasRoleMap.put(role.getId(), role);
		}
		
		for(Role role:allRole){
			if(hasRoleMap.get(role.getId())==null){
				currentUnHaveRole.add(role);
			}
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(currentUnHaveRole));
		
	}
	@RequestMapping("getRoleList")
	public void getRoleList(HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(roleService.getRole(commonService.getCurrentLogin().getUserLevel())));
		
	}
	
	/**
	 * 当前用户只能创建低于自己等级的角色
	 * 
	 * 用户角色一经创建不允许修改角色等级
	 * @param roleName
	 * @param roleDesc
	 * @param roleLevel
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("addRole")
	public void addRole(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "roleName") String roleName,
			@RequestParam(value = "roleDesc",required=false) String roleDesc,
			@RequestParam(value = "roleLevel",required=false,defaultValue="999999") Integer roleLevel,
			HttpServletResponse response
			) throws IOException{
		ReturnJson rj=new ReturnJson();
		User user=commonService.getCurrentLogin();
		if(user.getUserLevel()<roleLevel){
			if(id==null){
				//创建低于自己权限的角色
				Role role=new Role();
				role.setRoleDesc(roleDesc);
				role.setRoleLevel(roleLevel);
				role.setRoleName(roleName);
				roleService.saveRole(role);
			}else{
				//更新角色
				Role role=roleService.findById(id);
				if(role==null){
					rj.setSuccess(false);
					rj.setMsg("修改角色不存在");
				}else{
					role.setRoleName(roleName);
					role.setRoleLevel(roleLevel);
					role.setRoleDesc(roleDesc);
					roleService.saveRole(role);
				}
				
			}
			
		}else{
			//无权限
			rj.setSuccess(false);
			rj.setMsg("无权限");
		}
		
		 response.setContentType("text/html;charset=utf-8");
			response.getWriter()
			.write(JsonMapper.nonEmptyMapper().toJson(rj));
			
		
	}
	
	
	@RequestMapping("saveAuthorization")
	public void saveAuthorization(@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "resourcesIds", required = false) String resourcesIds,
			HttpServletResponse response) throws IOException{
		ReturnJson returnJson=new ReturnJson();
		Role role=roleService.findById(roleId);
		
		if(StringUtils.isNotBlank(resourcesIds)&&resourcesIds.split(";").length>0){
			try{
			Set<Menu> sets=new HashSet<Menu>();
			for(String str:resourcesIds.split(";")){
				Menu menu=menuService.findById(Long.parseLong(str));
				sets.add(menu);
			}
			role.setMenus(sets);
			
			roleService.saveRole(role);
			
		
			shiroDbRealm.clearAllCachedAuthorizationInfo();
			
			}catch(Exception e){
				e.printStackTrace();
				returnJson.setSuccess(false);
				returnJson.setMsg("保存角色授权失败");
			}
			
		}else{
			role.setMenus(null);
			try{
				roleService.saveRole(role);
			}catch(Exception e){
				e.printStackTrace();
				returnJson.setSuccess(false);
				returnJson.setMsg("保存角色授权失败");
			}
			
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(returnJson));
		
		
	}
	/**
	 * 保存角色的资源操作授权
	 * @throws IOException 
	 */
	@RequestMapping("saveResourceAuthorization")
	public void saveResourceAuthorization(
			@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "resourceId", required = false) Long[] resourceIds,
			HttpServletResponse response
			) throws IOException{

		ReturnJson returnJson=new ReturnJson();
		Role role=roleService.findById(roleId);
		
		if(resourceIds!=null&&resourceIds.length>0){
			try{
			Set<Resource> sets=new HashSet<Resource>();
			for(Long resourceId:resourceIds){
				Resource resource=resourceService.findById(resourceId);
				sets.add(resource);
			}
			role.setResources(sets);
			
			roleService.saveRole(role);
			
		
			shiroDbRealm.clearAllCachedAuthorizationInfo();
			
			}catch(Exception e){
				e.printStackTrace();
				returnJson.setSuccess(false);
				returnJson.setMsg("保存角色授权失败");
			}
			
		}else{
			role.setMenus(null);
			try{
				roleService.saveRole(role);
			}catch(Exception e){
				e.printStackTrace();
				returnJson.setSuccess(false);
				returnJson.setMsg("保存角色授权失败");
			}
			
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(returnJson));
		
		
	
	}
	
	

}
