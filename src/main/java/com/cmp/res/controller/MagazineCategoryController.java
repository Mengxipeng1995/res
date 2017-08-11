package com.cmp.res.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.BookCategory;
import com.cmp.res.entity.BookCategoryMapping;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.MagazineCategoryMappingService;
import com.cmp.res.service.MagazineCategoryService;

@Controller
@RequestMapping("/mcc/")
public class MagazineCategoryController {
	
	@Autowired
	private MagazineCategoryService magazineCategoryService;
	
	@Autowired
	private MagazineCategoryMappingService magazineCategoryMappingService;
	
	
	
	@Autowired
	private CommonService commonService;
	
	
	@RequestMapping("getGategoryWithMagid")
	public void getGategoryWithMagid(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "magid",required=false) Long magid,
			HttpServletResponse response
			){
		List<MagazineCategory> sons=magazineCategoryService.findByParentid(pid),temp=new ArrayList<MagazineCategory>();
		if(magid==null){
			temp=sons;
		}else{
			Map<Long,MagazineCategoryMapping> map=new HashMap<Long,MagazineCategoryMapping>();
			List<MagazineCategoryMapping> mcs= magazineCategoryMappingService.findByMagid(magid);
			for(MagazineCategoryMapping mc:mcs){
				map.put(mc.getCatid(), mc);
			}
			for(MagazineCategory son:sons){
				if(map.get(son.getId())!=null){
					son.setFlag(true);
				}
				temp.add(son);
			}
			
			
		}
		
		commonService.returnDate(response, temp);
		
	}
	
	
	@RequestMapping("getSons")
	public void getBookCategoryByParentId(
			@RequestParam(value = "id") Long pid,
			HttpServletResponse response){
		commonService.returnDate(response, magazineCategoryService.findByParentid(pid));
		
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
				MagazineCategory parentMagazineCategory=magazineCategoryService.findById(parentid);
				//新增
				MagazineCategory magazineCategory=new MagazineCategory();
				magazineCategory.setCreateTime(date);
				magazineCategory.setModifyTime(date);
				magazineCategory.setParent(parentMagazineCategory);
				magazineCategory.setParentid(parentid);
				magazineCategory.setName(name);
				
				magazineCategoryService.save(magazineCategory);
			}else{
				//编辑
				MagazineCategory magazineCategory=magazineCategoryService.findById(id);
				magazineCategory.setModifyTime(date);
				magazineCategory.setName(name);
				
				magazineCategoryService.save(magazineCategory);
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
		MagazineCategory magazineCategory=magazineCategoryService.findById(id);
		magazineCategory.getParent().getSons().remove(magazineCategory);
		magazineCategoryService.delete(magazineCategory);
		
		ReturnJson rj=new ReturnJson();
		commonService.returnDate(response, rj);
	}
	
	public boolean cheknName(Long id,String name,Long pid){
		boolean flag=false;
		if(id==null){
			//新增
			flag=magazineCategoryService.findByParentidAndName(pid,name)!=null;
		}else{
			//编辑
			MagazineCategory magazineCategory=magazineCategoryService.findByParentidAndName(pid,name);
			flag=magazineCategory!=null&&magazineCategory.getId()!=id;
		}
		return flag;
		
	}

}
