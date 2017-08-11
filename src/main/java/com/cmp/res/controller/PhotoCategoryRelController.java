package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.PhotoCategoryRelService;

@Controller
@RequestMapping("/pcrc/")
public class PhotoCategoryRelController {
	@Autowired
	private PhotoCategoryRelService photoCategoryRelService;
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "catid",required=false) Long catid,
			@RequestParam(value = "page",required=false,defaultValue="1") Integer page,
			@RequestParam(value = "limit",required=false,defaultValue="20") Integer limit,
			HttpServletResponse response
			
			){
		commonService.returnDate(response, photoCategoryRelService.outline(catid,page,limit));
		
		
	}

}
