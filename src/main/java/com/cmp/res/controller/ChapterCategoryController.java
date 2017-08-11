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

import com.cmp.res.entity.ChapterCategory;
import com.cmp.res.entity.ChapterCategoryMapping;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.ChapterCategoryMappingService;
import com.cmp.res.service.ChapterCategoryService;
import com.cmp.res.service.CommonService;

@Controller
@RequestMapping("chapterCategory")
public class ChapterCategoryController {
	
	@Autowired
	private ChapterCategoryService chapterCategoryService;
	
	@Autowired
	private ChapterCategoryMappingService chapterCategoryMappingService;
	
	
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("getGategoryWithChapterid")
	public void getGategoryWithChapterid(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "chapterid",required=false) Long chapterid,
			HttpServletResponse response
			){
		List<ChapterCategory> sons=chapterCategoryService.findByParentid(pid),temp=new ArrayList<ChapterCategory>();
		if(chapterid==null){
			temp=sons;
		}else{
			Map<Long,ChapterCategoryMapping> map=new HashMap<Long,ChapterCategoryMapping>();
			List<ChapterCategoryMapping> ccms= chapterCategoryMappingService.findByChapterid(chapterid);
			for(ChapterCategoryMapping ccm:ccms){
				map.put(ccm.getCatid(), ccm);
			}
			for(ChapterCategory son:sons){
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
		commonService.returnDate(response, chapterCategoryService.findByParentid(pid));
		
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
				ChapterCategory parentChapterCategory=chapterCategoryService.findById(parentid);
				//新增
				ChapterCategory chapterCategory=new ChapterCategory();
				chapterCategory.setCreateTime(date);
				chapterCategory.setModifyTime(date);
				chapterCategory.setParent(parentChapterCategory);
				chapterCategory.setParentid(parentid);
				chapterCategory.setName(name);
				
				chapterCategoryService.save(chapterCategory);
			}else{
				//编辑
				ChapterCategory chapterCategory=chapterCategoryService.findById(id);
				chapterCategory.setModifyTime(date);
				chapterCategory.setName(name);
				
				chapterCategoryService.save(chapterCategory);
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
		ChapterCategory chapterCategory=chapterCategoryService.findById(id);
		chapterCategory.getParent().getSons().remove(chapterCategory);
		chapterCategoryService.delete(chapterCategory);
		
		ReturnJson rj=new ReturnJson();
		commonService.returnDate(response, rj);
	}
	
	public boolean cheknName(Long id,String name,Long pid){
		boolean flag=false;
		if(id==null){
			//新增
			flag=chapterCategoryService.findByParentidAndName(pid,name)!=null;
		}else{
			//编辑
			ChapterCategory chapterCategory=chapterCategoryService.findByParentidAndName(pid,name);
			flag=chapterCategory!=null&&chapterCategory.getId()!=id;
		}
		return flag;
		
	}

	

}
