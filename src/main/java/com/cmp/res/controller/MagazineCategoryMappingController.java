package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.MagazineCategoryMappingService;
import com.cmp.res.service.MagazineCategoryService;

@Controller
@RequestMapping("/mcmc/")
public class MagazineCategoryMappingController {
	@Autowired
	private MagazineCategoryMappingService magazineCategoryMappingService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private MagazineCategoryService magazineCategoryService;
	
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
			@RequestParam(value = "cid",required=false)Long cid,
			HttpServletResponse response
			){
		if(cid==null){
			cid=magazineCategoryService.getRoot().getId();
		}
		commonService.returnDate(response, magazineCategoryMappingService.outline(cid, pn, ps));
		
	}

}
