package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.ChapterCategoryMappingService;
import com.cmp.res.service.ChapterCategoryService;
import com.cmp.res.service.CommonService;

@Controller
@RequestMapping("chapterCategoryMapping")
public class ChapterCategoryMappingController {
	@Autowired
	private ChapterCategoryMappingService chapterCategoryMappingService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private ChapterCategoryService chapterCategoryService;
	
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
			@RequestParam(value = "cid",required=false)Long cid,
			HttpServletResponse response
			){
		if(cid==null){
			cid=chapterCategoryService.getRoot().getId();
		}
		commonService.returnDate(response, chapterCategoryMappingService.outline(cid, pn, ps));
		
	}

}
