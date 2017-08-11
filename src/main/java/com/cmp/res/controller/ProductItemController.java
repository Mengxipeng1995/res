package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.service.CommonService;
import com.cmp.res.service.ProductItemService;
@Controller
@RequestMapping("/productItem/")
public class ProductItemController {
	@Autowired
	private ProductItemService productItemService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("list")
	public void list(
			@RequestParam(value = "productid")Long  productid,
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			HttpServletRequest request,HttpServletResponse response
			){
		
		commonService.returnDate(response, productItemService.list(productid,pn, ps));
		
	}

}
