package com.cmp.res.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.Menu;
import com.cmp.res.entity.Role;
import com.cmp.res.service.MenuService;
import com.cmp.res.service.RoleService;



@RestController
@RequestMapping("menu")
public class MenuController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	/**
	 * 由于数据量很小 整棵树全部返回（并且添加授权标识）
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getMenuByParentId")
	public void getCategoryByParentId(@RequestParam(value = "id", required = false, defaultValue = "") Long id,
			@RequestParam(value = "roleId", required = false, defaultValue = "") Long roleId,
			HttpServletResponse response) throws IOException{
		if(id==null||id==-1){
			id=null;
		}
		List<Menu> obj=menuService.getMenuByParentId(id);
		if(obj!=null&&obj.size()>0&&roleId!=null){
			Role role=roleService.findById(roleId);
			if(role!=null){
				Map<Long,Menu> map=new HashMap<Long,Menu>();
				Set<Menu> menus=role.getMenus();
				if(menus!=null&&menus.size()>0){
					for(Menu menu:menus){
						map.put(menu.getId(), menu);
					}
				}
				//标识菜单权限
				if(map.size()>0){
					markPermission(map, obj);
				}
			}
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(obj));
	}
	
	public void markPermission(Map<Long,Menu> map,List<Menu> menus){
		for(Menu menu:menus){
			if(map.get(menu.getId())!=null){
				menu.setCheckFlag(true);
				List<Menu> sons=menu.getSonsMenu();
				if(sons!=null&&sons.size()>0){
					markPermission(map, sons);
				}
			}
		}
	}

}
