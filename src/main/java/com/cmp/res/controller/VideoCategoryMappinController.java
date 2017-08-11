package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.VideoCategoryMappingService;

@Controller
@RequestMapping("/vcmc/")
public class VideoCategoryMappinController {
	@Autowired
	private VideoCategoryMappingService videoCategoryMappingService;
	
	@Autowired
	private CommonService commonService;
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "catid")Long catid,
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			HttpServletRequest request,HttpServletResponse response
			){
		
		
		commonService.returnDate(response, videoCategoryMappingService.outline(pn, ps, catid));
		
	}
	
	

}
