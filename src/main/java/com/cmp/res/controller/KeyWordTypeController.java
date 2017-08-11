package com.cmp.res.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmp.res.service.KeyWordTypeService;

@Controller
@RequestMapping("/keywordtype/")
public class KeyWordTypeController {
	
	@Autowired
	private KeyWordTypeService keyWordTypeService;
	
	

}
