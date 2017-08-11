package com.cmp.res.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.cmp.res.service.CommonService;
import com.cmp.res.service.GroupService;
import com.cmp.res.service.UserGroupMappingService;

@Controller
@RequestMapping("/ugmc/")
public class UserGroupMappingController {
	
	@Autowired
	private UserGroupMappingService userGroupMappingService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private GroupService groupService;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
			@RequestParam(value = "groupid",required=false)Long groupid,
			HttpServletResponse response
			){
		if(groupid==null){
			groupid=groupService.getRoot().getId();
		}
		commonService.returnDate(response, userGroupMappingService.outline(groupid, pn, ps));
		
	}

}
