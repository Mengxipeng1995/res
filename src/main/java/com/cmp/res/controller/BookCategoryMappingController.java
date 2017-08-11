package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.BookCategoryMappingService;
import com.cmp.res.service.BookCategoryService;
import com.cmp.res.service.CommonService;

@Controller
@RequestMapping("/bcm/")
public class BookCategoryMappingController {
	@Autowired
	private BookCategoryMappingService bookCategoryMappingService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BookCategoryService bookCategoryService;

	
	
	@RequestMapping("outline")
		public void outline(
		@RequestParam(value = "page",required=false,defaultValue="1") int pn,
		@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
		@RequestParam(value = "cid",required=false)Long cid,
		@RequestParam(value = "kw",required=false)String kw,
		HttpServletResponse response
			){
			if(cid==null){
				cid=bookCategoryService.getRoot().getId();
			}
			commonService.returnDate(response, bookCategoryMappingService.outline(cid, pn, ps,kw));

	}

}
