package com.cmp.res.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.User;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.util.CSVUtils;
import com.cmp.res.util.Log4jUtils;

@Controller
@RequestMapping("/category/")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ConfigService configService;
	
	public static Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@RequestMapping("getSons")
	public void getCatsByParentId(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "resourcesType",required=false,defaultValue="0") Integer resourcesType,
			HttpServletResponse response){
		commonService.returnDate(response, categoryService.findByParentid(pid,resourcesType));
		
	}
	@RequestMapping("findById")
	public void findById(@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response){
		commonService.returnDate(response, categoryService.findById(id));
	}
	
	@RequestMapping("checkName")
	public void checkName(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "pid") Long parentid,
			@RequestParam(value = "resourcesType") Integer resourcesType,
		HttpServletResponse response){
		ReturnJson rj=new ReturnJson();
		rj.setSuccess(checkConflict(id,name,parentid,resourcesType));
		commonService.returnDate(response, rj);
	}
	
	public boolean checkConflict(Long id,String name,Long parentid,Integer resourcesType){
		if(id==null){
			return categoryService.findByParentidAndNameResourcesType(parentid, name,resourcesType)!=null;
		}else{
			Category cat=categoryService.findByParentidAndNameResourcesType(parentid, name,resourcesType);
			return !(cat==null||(cat!=null&&cat.getId()==id));
		}
		
	}
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "pid",required=false) Long parentid,
			@RequestParam(value = "resourcesType",required=false,defaultValue="0") Integer resourcesType,
			HttpServletResponse response
			){
		User user=commonService.getCurrentLogin();
		ReturnJson rj=new ReturnJson();
		if(id==null){
			//新增
			Category parentCat=categoryService.findById(parentid);
			Category checkCategory=categoryService.findByParentidAndNameResourcesType(parentCat.getId(), name,resourcesType);
			if(checkCategory==null){
				Category cat=new Category();
				Date date=new Date();
				cat.setName(name);
				cat.setParent(parentCat);
				cat.setParentid(parentid);
				cat.setCreateTime(date);
				cat.setModifyTime(date);
				cat.setType(parentCat.getType());
				cat.setResourcesType(resourcesType);
				categoryService.saveCategory(cat);
				
				logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
						user.getUserName(), 11, id,name) ,
						Log4jDBAppender.EXTENT_MSG_DIVIDER,"新增分类体系【ID:"+id+"】");
			}else{
				rj.setSuccess(false);
				rj.setMsg("分类名称在当前父节点下已经存在");
			}
			
		}else{
			//编辑
			Category cat=categoryService.findById(id);
			if(cat!=null){
				Category checkCategory=categoryService.findByParentidAndNameResourcesType(cat.getParentid(), name,resourcesType);
				if(checkCategory==null||(checkCategory!=null&&checkCategory.getId()==id)){
					cat.setName(name);
					cat.setModifyTime(new Date());
					categoryService.saveCategory(cat);
					
					
					logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
							user.getUserName(), 11, id,name) ,
							Log4jDBAppender.EXTENT_MSG_DIVIDER,"编辑分类体系【ID:"+id+"】");
				}else{
					rj.setSuccess(false);
					rj.setMsg("分类名称在当前父节点下已经存在");
				}
			}else{
				rj.setSuccess(false);
				rj.setMsg("您操作的分类不存在");
			}
			
		}
		commonService.returnDate(response,rj);
	}
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response){
		
		
		Category cat=categoryService.findById(id);
		String name=cat.getName();
		cat.getParent().getSons().remove(cat);
		categoryService.delete(cat);
		
		User user=commonService.getCurrentLogin();
		logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
				user.getUserName(), 11, id,name) ,
				Log4jDBAppender.EXTENT_MSG_DIVIDER,"删除分类体系【ID:"+id+"】");
		
		ReturnJson rj=new ReturnJson();
		commonService.returnDate(response, rj);
	}
	
	@RequestMapping("export")
	public void export(@RequestParam(value = "id") Long id,
			HttpServletResponse response) throws IOException{
		
		User user=commonService.getCurrentLogin();
		
		Category cat=categoryService.findById(id);
		logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
				user.getUserName(), 11, id, cat.getName()) ,
				Log4jDBAppender.EXTENT_MSG_DIVIDER,"导出分类体系【根节点:"+id+"】");
		List<String> exportData=new ArrayList<String>();
		exportData.add("ID,名称,父节点,创建时间");
		getExportData(exportData,cat);
		File file=new File(configService.getDownlTempPath()+UUID.randomUUID().toString()+".csv");
		 boolean isSuccess=CSVUtils.exportCsv(file, exportData);
		 
		 response.setContentType("application/x-zip-compressed;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
			file.delete();
		
		
	}
	
	public void getExportData(List<String> exportData,Category cat){
		if(cat!=null){
			exportData.add(cat.getId()+","+cat.getName()+","+cat.getParentid()+","+cat.getCreateTime());
			List<Category> sons=cat.getSons();
			if(sons!=null&&sons.size()>0){
				for(Category son:sons){
					getExportData(exportData,son);
				}
			}
			
		}
		
	}
	

}
