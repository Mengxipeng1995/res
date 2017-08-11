package com.cmp.res.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.KeyWord;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.User;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.KeyWordService;

@Controller
@RequestMapping("/keyword/")
public class KeyWordController {
	@Autowired
	private KeyWordService keyWordService;
	@Autowired
	private CommonService commonService;
	@RequestMapping("search")
	public void search(
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
			@RequestParam(value = "keyword",required=false) String keyword,
			@RequestParam(value = "creater",required=false) String creater,
			HttpServletResponse response
			){
		
		
		commonService.returnDate(response, keyWordService.search(pn, ps, keyword, creater));
		
	}
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "keyword",required=false) String keyword,
			@RequestParam(value = "type",required=false,defaultValue="0") Long type,
			HttpServletResponse response
			){
		User user=commonService.getCurrentLogin();
		ReturnJson rj=new ReturnJson();
		if(id==null){
			KeyWord kw=new KeyWord();
			kw.setCreateDate(new Date());
			kw.setCreateUser(user.getUserName());
			kw.setKeyWordsType(type);
			kw.setKeywords(keyword);
			kw.setTypeName("其他");
			keyWordService.save(kw);
			
		}else{
			KeyWord kw=keyWordService.findById(id);
			if(kw!=null){
				kw.setKeywords(keyword);
				kw.setModifyDate(new Date());
				//kw.setTypeName(typeName);
				keyWordService.save(kw);
			}else{
				rj.setSuccess(false);
				rj.setMsg("关键词不存在");
			}
		}
		
		commonService.returnDate(response, rj);
	}
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response
			){
		keyWordService.delete(id);
		commonService.returnDate(response, new ReturnJson());
	}
	

}
