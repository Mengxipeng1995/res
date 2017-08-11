package com.cmp.res.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemCategoryMapping;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ItemCategoryMappingService;
import com.cmp.res.service.ItemService;

@Controller
@RequestMapping("/icmsc/")
public class ItemCategoryMappingController {
	
	@Autowired
	private ItemCategoryMappingService icms;
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("findByCid")
	public void findByCategoryId(
			@RequestParam(value = "catid") Long pid,
			@RequestParam(value = "limit",required=false,defaultValue="20") Integer limit,
			@RequestParam(value = "page",required=false,defaultValue="1") Integer pn,
			HttpServletResponse response
			){
		
		Page<ItemCategoryMapping> page=icms.outline(pn, limit, pid);
		for(ItemCategoryMapping icm:page.getContent()){
			List<Item> items=itemService.findByResCode(icm.getResCode());
			icm.setVersionCount(items.size());
		}
		
		commonService.returnDate(response, page);
		
	}

}
