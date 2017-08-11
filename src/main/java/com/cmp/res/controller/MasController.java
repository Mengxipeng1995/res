package com.cmp.res.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cmp.res.entity.Category;
import com.cmp.res.service.CategoryService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/mas/")
public class MasController {
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("saveVideo")
	public ModelAndView returnMethod(HttpServletRequest request){

		Map<String,String> map =getParms(request.getParameter("queryString"));
		//masvid=[{"masId":15,"type":"vod"}]
		JSONArray jArray=JSONArray.fromObject(request.getParameter("masvid"));  
		JSONObject object = (JSONObject)jArray.get(0);  
		
		ModelAndView modelAndView = new ModelAndView("video");  
        modelAndView.addObject("masId", object.get("masId"));  
        modelAndView.addObject("type", object.get("type"));
        //获取视频分类
        String catidStr=map.get("catid");
        try{
        	Category category=categoryService.findById(Long.parseLong(catidStr));
        	if(category==null)
        		throw new Exception();
        	
        	modelAndView.addObject("catid",category.getId());
        	modelAndView.addObject("catname",category.getName());
        }catch (Exception e) {
			// TODO: handle exception
        	Category category=categoryService.findById(2l);
        	modelAndView.addObject("catid",category.getId());
        	modelAndView.addObject("catname",category.getName());
		}
        return modelAndView;  
		
		
	}
	
	public Map<String,String> getParms(String queryString){
		Map<String,String> map=new HashMap<String,String>();
		
		if(StringUtils.isNotBlank(queryString)){
			for(String par:queryString.split("&")){
				if(StringUtils.isNotBlank(par)&&par.split("=").length==2){
					String[] parInfo=par.split("=");
					map.put(parInfo[0], parInfo[1]);
				}
			}
		}
		
		return map;
		
	}

}
