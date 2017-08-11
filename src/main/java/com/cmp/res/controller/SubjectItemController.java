package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.SubjectItemService;

@Controller
@RequestMapping("/subjectItem/")
public class SubjectItemController {
	@Autowired
	private SubjectItemService  subjectItemService;
	@Autowired
	private CommonService commonService;

	@RequestMapping("findBySubjectid")
	public void findBySubjectid(
			@RequestParam(value = "subjectid")Long  subjectid,
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			HttpServletRequest request,HttpServletResponse response
			){
		commonService.returnDate(response, subjectItemService.findBySubjectid(subjectid, pn, ps));
		
	}
	
}
