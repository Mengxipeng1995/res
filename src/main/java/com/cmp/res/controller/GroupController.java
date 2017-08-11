package com.cmp.res.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.GroupSequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.Group;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.UserGroupMapping;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.GroupService;
import com.cmp.res.service.UserGroupMappingService;
@Controller
@RequestMapping("/group/")
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserGroupMappingService userGroupMappingService;
	
	
	@RequestMapping("getGroupWithUserId")
	public void getGategoryWithMagid(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "userId",required=false) Long userId,
			HttpServletResponse response
			){
		List<Group> sons=groupService.findByParentid(pid),temp=new ArrayList<Group>();
		if(userId==null){
			temp=sons;
		}else{
			Map<Long,UserGroupMapping> map=new HashMap<Long,UserGroupMapping>();
			List<UserGroupMapping> ugms= userGroupMappingService.findByUserid(userId);
			for(UserGroupMapping ugm:ugms){
				map.put(ugm.getGroupId(), ugm);
			}
			for(Group son:sons){
				if(map.get(son.getId())!=null){
					son.setFlag(true);
				}
				temp.add(son);
			}
			
			
		}
		
		commonService.returnDate(response, temp);
		
	}
	
	@RequestMapping("findById")
	public void findById(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		commonService.returnDate(response, groupService.findById(id));
		
	}
	
	@RequestMapping("getSons")
	public void getGroupByParentId(
			@RequestParam(value = "id") Long pid,
			HttpServletResponse response){
		commonService.returnDate(response, groupService.findByParentid(pid));
		
	}
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "pid",required=false) Long parentid,
			HttpServletResponse response
			){
		ReturnJson rs=new ReturnJson();
		
		if(!cheknName(id,name,parentid)){
			Date date=new Date();
			if(id==null){
				Group parentGroup=groupService.findById(parentid);
				//新增
				Group group=new Group();
				group.setCreateTime(date);
				group.setModifyTime(date);
				group.setParent(parentGroup);
				group.setParentid(parentid);
				group.setName(name);
				
				groupService.save(group);
			}else{
				//编辑
				Group group=groupService.findById(id);
				group.setModifyTime(date);
				group.setName(name);
				
				groupService.save(group);
			}
		}else{
			rs.setSuccess(false);
			rs.setMsg("组织机构【"+name+"】在当前父节点下已经存在");
		}
		
		commonService.returnDate(response, rs);
		
		
	}
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response){
		Group group=groupService.findById(id);
		group.getParent().getSons().remove(group);
		groupService.delete(group);
		
		ReturnJson rj=new ReturnJson();
		commonService.returnDate(response, rj);
	}
	
	public boolean cheknName(Long id,String name,Long pid){
		boolean flag=false;
		if(id==null){
			//新增
			flag=groupService.findByParentidAndName(pid,name)!=null;
		}else{
			//编辑
			Group group=groupService.findByParentidAndName(pid,name);
			flag=group!=null&&group.getId()!=id;
		}
		return flag;
		
	}

}
