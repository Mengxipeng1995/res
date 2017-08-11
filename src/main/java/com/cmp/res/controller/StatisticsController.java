package com.cmp.res.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmp.res.service.BookService;
import com.cmp.res.service.ChapterService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.MagazineService;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.ProductService;
import com.cmp.res.service.SubjectService;
import com.cmp.res.service.VideoService;

@Controller
@RequestMapping("/statistics/")
public class StatisticsController {
	@Autowired
	private ItemService itemConService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BookService bookService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SubjectService subjectService;
	@RequestMapping("getPieData")
	public void getPieData(
			HttpServletRequest request,HttpServletResponse response
			){
		class Data{
			String name;
			Long value;
			public Data(String name,Long value){
				this.name=name;
				this.value=value;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Long getValue() {
				return value;
			}
			public void setValue(Long value) {
				this.value = value;
			}
			
		}
		List<Data> list=new ArrayList<Data>();
		list.add(new Data("条目数量",itemConService.getCount()));
		list.add(new Data("图片数量",photoService.getCount()));
		list.add(new Data("图书数量",bookService.getCount()));
		list.add(new Data("篇章数量",chapterService.getCount()));
		list.add(new Data("期刊数量",magazineService.getCount()));
		list.add(new Data("视频数量",videoService.getCount()));
		list.add(new Data("产品数量",productService.getCount()));
		list.add(new Data("专题数量",subjectService.getCount()));
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		List<String> items=new ArrayList<String>();
		items.add("条目数量");
		items.add("图片数量");
		items.add("图书数量");
		items.add("篇章数量");
		items.add("期刊数量");
		items.add("视频数量");
		items.add("产品数量");
		items.add("专题数量");
		map.put("items",items);
		map.put("data",list);
		commonService.returnDate(response, map);
		
	}
	
	
	

}
