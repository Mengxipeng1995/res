package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.SysPropertyService;

@Controller
@RequestMapping("/syspro/")
public class SysPropertyController {
	
	@Autowired
	private SysPropertyService sysPropertyService; 
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("get")
	public void get(
			HttpServletRequest request,HttpServletResponse response
			){
		
		commonService.returnDate(response, sysPropertyService.findAll());
	}
}
