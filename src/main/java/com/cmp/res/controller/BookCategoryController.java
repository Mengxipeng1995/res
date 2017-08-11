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
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.BookCategoryMappingService;
import com.cmp.res.service.BookCategoryService;
import com.cmp.res.service.CommonService;


@Controller
@RequestMapping("/bookCategroy/")
public class BookCategoryController {
	@Autowired
	private BookCategoryService bookCategoryService;
	@Autowired
	private BookCategoryMappingService bookCategoryMappingService;
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("getGategoryWithBooid")
	public void getGategoryWithBooid(
			@RequestParam(value = "id") Long pid,
			@RequestParam(value = "bookid",required=false) Long bookid,
			HttpServletResponse response
			){
		List<BookCategory> sons=bookCategoryService.findByParentid(pid),temp=new ArrayList<BookCategory>();
		if(bookid==null){
			temp=sons;
		}else{
			Map<Long,BookCategoryMapping> map=new HashMap<Long,BookCategoryMapping>();
			List<BookCategoryMapping> bcms= bookCategoryMappingService.findByBookid(bookid);
			for(BookCategoryMapping bcm:bcms){
				map.put(bcm.getCatid(), bcm);
			}
			for(BookCategory son:sons){
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
		commonService.returnDate(response, bookCategoryService.findByParentid(pid));
		
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
				BookCategory parentBookCategory=bookCategoryService.findById(parentid);
				//新增
				BookCategory bookCategory=new BookCategory();
				bookCategory.setCreateTime(date);
				bookCategory.setModifyTime(date);
				bookCategory.setParent(parentBookCategory);
				bookCategory.setParentid(parentid);
				bookCategory.setName(name);
				
				bookCategoryService.save(bookCategory);
			}else{
				//编辑
				BookCategory bookCategory=bookCategoryService.findById(id);
				bookCategory.setModifyTime(date);
				bookCategory.setName(name);
				
				bookCategoryService.save(bookCategory);
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
		BookCategory bookCategory=bookCategoryService.findById(id);
		bookCategory.getParent().getSons().remove(bookCategory);
		bookCategoryService.delete(bookCategory);
		
		ReturnJson rj=new ReturnJson();
		commonService.returnDate(response, rj);
	}
	
	
	
	public boolean cheknName(Long id,String name,Long pid){
		boolean flag=false;
		if(id==null){
			//新增
			flag=bookCategoryService.findByParentidAndName(pid,name)!=null;
		}else{
			//编辑
			BookCategory bookCategory=bookCategoryService.findByParentidAndName(pid,name);
			flag=bookCategory!=null&&bookCategory.getId()!=id;
		}
		return flag;
		
	}

}
