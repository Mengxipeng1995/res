package com.cmp.res.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.Subject;
import com.cmp.res.entity.SubjectCategory;
import com.cmp.res.entity.User;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.SubjectCategoryService;
import com.cmp.res.service.SubjectService;

@Controller
@RequestMapping("/subjectCategory/")
public class SubjectCategoryController {
	@Autowired
	private SubjectCategoryService subjectCategoryService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("findById")
	public void findById(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response
			){
		commonService.returnDate(response, subjectCategoryService.findById(id));
	}
	
	@RequestMapping("save")
	public void saveSubject(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "pid",required=false) Long parentid,
			@RequestParam(value = "title") String name,
			HttpServletResponse response
			){
		
		ReturnJson rj=new ReturnJson();
		if(!checkConflict(id, name, parentid)){
			User user=commonService.getCurrentLogin();
			if(id==null){
				//新增
				SubjectCategory sc=new SubjectCategory();
				sc.setCreaterUserName(user.getUserName());
				sc.setCreateDate(new Date());
				sc.setName(name);
				sc.setParentid(parentid);

				subjectCategoryService.save(sc);
			}else{
				//编辑
				SubjectCategory sc=subjectCategoryService.findById(id);
				
				sc.setName(name);
				
				subjectCategoryService.save(sc);
			}
		}else{
			rj.setSuccess(false);
			rj.setMsg("当前分类节点下应该存在【"+name+"】");
		}
			
		
		commonService.returnDate(response, rj);
		
	}
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response
			){
		SubjectCategory sc=subjectCategoryService.findById(id);
		sc.getSons().remove(sc);
		subjectCategoryService.delete(sc);
		
	}
	
	@RequestMapping("getSons")
	public void getCatsByParentId(
			@RequestParam(value = "id") Long pid,
			HttpServletResponse response){
		commonService.returnDate(response, subjectCategoryService.findByParentid(pid));
		
	}
	@RequestMapping("getSonsWihtSbjectid")
	public void getSonsWihtSbjectid(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "sid",required=false) Long sid,
			HttpServletResponse response
			){
		
		List<SubjectCategory> sons=subjectCategoryService.findByParentid(pid),
				temp= new ArrayList<SubjectCategory>();
		if(sid!=null){
		Subject subject=subjectService.findById(sid);
		if(sons!=null&&sons.size()>0&&subject!=null&&StringUtils.isNoneBlank(subject.getCategoryids())){
			String[] ids=subject.getCategoryids().substring(1).split(";");
			Map<String,Object> map=new HashMap<String,Object>();
			for(String id:ids){
				map.put(id, true);
			}
			for(SubjectCategory sc:sons){
				if(map.get(sc.getId()+"")==null){
					sc.setFlag(false);
				}else{
					sc.setFlag(true);
				}
				temp.add(sc);
			}
			
		}else{
			temp=sons;
		}
		}else{
			temp=sons;
		}
		
		commonService.returnDate(response, temp);
	}
	
	public boolean checkConflict(Long id,String title,Long parentid){
		if(id==null){
			return subjectCategoryService.findByParentidAndName(parentid, title)!=null;
		}else{
			SubjectCategory subjectCategory=subjectCategoryService.findByParentidAndName(parentid, title);
			return !(subjectCategory==null||(subjectCategory!=null&&subjectCategory.getId()==id));
		}
		
	}

}
