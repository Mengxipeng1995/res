package com.cmp.res.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Magazine;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.MagazineCategoryMappingService;
import com.cmp.res.service.MagazineCategoryService;
import com.cmp.res.service.MagazineService;

@Controller
@RequestMapping("magazine")
public class MagazineController {
	
	@Autowired
	private MagazineService magazineService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MagazineCategoryService magazineCategoryService;
	@Autowired
	private MagazineCategoryMappingService magazineCategoryMappingService;

	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false)Long  id,
			@RequestParam(value = "title") String title,
			@RequestParam(value = "keywords",required=false) String keywords,
			@RequestParam(value = "pageNums",required=false,defaultValue="0") int pageNums,
			@RequestParam(value = "publisherName",required=false) String publisherName,
			@RequestParam(value = "pubDateStr",required=false) String pubDateStr,
			@RequestParam(value = "publisherAddress",required=false) String publisherAddress,
			@RequestParam(value = "cid",required=false)String cids,
			HttpServletResponse response
			){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<Long> ids=getCids(cids);
		if(id==null){
			Magazine magazine=new Magazine();
			magazine.setTitle(title);
			magazine.setKeywords(keywords);
			magazine.setPageNums(pageNums);
			magazine.setPublisherName(publisherName);
			magazine.setPublisherAddress(publisherAddress);
			try{
				magazine.setPubDate(sdf.parse(pubDateStr));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			magazineService.save(magazine);
			
			if(ids!=null&&ids.size()>0){
				for(Long cid:ids){
					MagazineCategory mc= magazineCategoryService.findById(cid);
					if(mc!=null){
						MagazineCategoryMapping mcm=createMagazineCategoryMappingEntry(magazine,mc);
						magazineCategoryMappingService.save(mcm);
					}
					
				}
			}
		}else{
			//编辑
			magazineCategoryMappingService.deleteByMagid(id);
			Magazine magazine=magazineService.findById(id);
			
			magazine.setTitle(title);
			magazine.setKeywords(keywords);
			magazine.setPageNums(pageNums);
			magazine.setPublisherName(publisherName);
			magazine.setPublisherAddress(publisherAddress);
			try{
				magazine.setPubDate(sdf.parse(pubDateStr));
			}catch (Exception e) {
				// TODO: handle exception
			}
			magazineService.save(magazine);
			
			if(ids!=null&&ids.size()>0){
				for(Long cid:ids){
					MagazineCategory mc= magazineCategoryService.findById(cid);
					if(mc!=null){
						MagazineCategoryMapping mcm=createMagazineCategoryMappingEntry(magazine,mc);
						magazineCategoryMappingService.save(mcm);
					}
					
				}
			}
			
		}
		
		commonService.returnDate(response,new ReturnJson());
		
	}
	public MagazineCategoryMapping createMagazineCategoryMappingEntry(Magazine magazine,MagazineCategory mc){
		MagazineCategoryMapping mcm=new MagazineCategoryMapping();
		mcm.setMagid(magazine.getId());
		mcm.setCatid(mc.getId());
		mcm.setKeywords(magazine.getKeywords());
		mcm.setPageNums(magazine.getPageNums());
		mcm.setPublisherAddress(magazine.getPublisherAddress());
		mcm.setPublisherName(magazine.getPublisherName());
		mcm.setTitle(magazine.getTitle());
		mcm.setPubDate(magazine.getPubDate());
		
		return mcm;
	}
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		magazineCategoryMappingService.deleteByMagid(id);
		magazineService.delete(id);
		commonService.returnDate(response,new ReturnJson());
		
	}
	
	public List<Long> getCids(String idsStr){
		if(StringUtils.isNotBlank(idsStr)&&idsStr.split(";").length>0){
			List<Long> list=new ArrayList<Long>();
			for(String id:idsStr.split(";")){
				try{
					list.add(Long.parseLong(id));
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			return list;
		}else{
			return null;
		}
	}

}
