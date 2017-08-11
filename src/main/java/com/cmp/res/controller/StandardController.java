package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.StandardService;

@Controller
@RequestMapping("/standard/")
public class StandardController {

	@Autowired
	private StandardService standardService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(name="page",defaultValue="1",required=false)int page,
			@RequestParam(name="limit",defaultValue="20",required=false)int limit,
			HttpServletResponse response
			){
		
		commonService.returnDate(response, standardService.outline(page, limit));
		
	}
	
}
