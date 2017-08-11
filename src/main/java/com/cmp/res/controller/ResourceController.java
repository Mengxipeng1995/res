package com.cmp.res.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmp.res.entity.Resource;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ResourceService;

@Controller
@RequestMapping("/resource/")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("getAllResource")
	public  void getAllResource(HttpServletResponse response){
		Iterator<Resource> it=resourceService.findAll();
		Map<String,List<Resource>> map=new HashMap<String,List<Resource>>();
		while(it.hasNext()){
			Resource resource=it.next();
			if(map.get(resource.getTypeName())==null){
				List<Resource> list=new ArrayList<Resource>();
				list.add(resource);
				map.put(resource.getTypeName(), list);
			}else{
				map.get(resource.getTypeName()).add(resource);
			}
		}
		commonService.returnDate(response, map);
		
	}

}
