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

import com.cmp.res.entity.Chapter;
import com.cmp.res.entity.ChapterCategory;
import com.cmp.res.entity.ChapterCategoryMapping;
import com.cmp.res.entity.Magazine;
import com.cmp.res.entity.MagazineCategory;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.ChapterCategoryMappingService;
import com.cmp.res.service.ChapterCategoryService;
import com.cmp.res.service.ChapterService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.MagazineCategoryMappingService;
import com.cmp.res.service.MagazineCategoryService;
import com.cmp.res.service.MagazineService;

@Controller
@RequestMapping("/chapter/")
public class ChapterController {
	

	
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ChapterCategoryService chapterCategoryService;
	@Autowired
	private ChapterCategoryMappingService chapterCategoryMappingService;

	
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
			Chapter chapter=new Chapter();
			chapter.setTitle(title);
			chapter.setKeywords(keywords);
			chapter.setPageNums(pageNums);
			chapter.setPublisherName(publisherName);
			chapter.setPublisherAddress(publisherAddress);
			try{
				chapter.setPubDate(sdf.parse(pubDateStr));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			chapterService.save(chapter);
			
			if(ids!=null&&ids.size()>0){
				for(Long cid:ids){
					ChapterCategory cc= chapterCategoryService.findById(cid);
					if(cc!=null){
						ChapterCategoryMapping mcm=createChapterCategoryMappingEntry(chapter,cc);
						chapterCategoryMappingService.save(mcm);
					}
					
				}
			}
		}else{
			//编辑
			chapterCategoryMappingService.deleteByChapterid(id);
			Chapter chapter=chapterService.findById(id);
			
			chapter.setTitle(title);
			chapter.setKeywords(keywords);
			chapter.setPageNums(pageNums);
			chapter.setPublisherName(publisherName);
			chapter.setPublisherAddress(publisherAddress);
			try{
				chapter.setPubDate(sdf.parse(pubDateStr));
			}catch (Exception e) {
				// TODO: handle exception
			}
			chapterService.save(chapter);
			
			if(ids!=null&&ids.size()>0){
				for(Long cid:ids){
					ChapterCategory cc= chapterCategoryService.findById(cid);
					if(cc!=null){
						ChapterCategoryMapping mcm=createChapterCategoryMappingEntry(chapter,cc);
						chapterCategoryMappingService.save(mcm);
					}
					
				}
			}
			
		}
		
		commonService.returnDate(response,new ReturnJson());
		
	}
	public ChapterCategoryMapping createChapterCategoryMappingEntry(Chapter chapter,ChapterCategory cc){
		ChapterCategoryMapping ccm=new ChapterCategoryMapping();
		ccm.setChapterid(chapter.getId());
		ccm.setCatid(cc.getId());
		ccm.setKeywords(chapter.getKeywords());
		ccm.setPageNums(chapter.getPageNums());
		ccm.setPublisherAddress(chapter.getPublisherAddress());
		ccm.setPublisherName(chapter.getPublisherName());
		ccm.setTitle(chapter.getTitle());
		ccm.setPubDate(chapter.getPubDate());
		
		return ccm;
	}
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		chapterCategoryMappingService.deleteByChapterid(id);
		chapterService.delete(id);
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
