package com.cmp.res.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmp.res.entity.*;
import com.cmp.res.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;


//@RestController
@Controller
@RequestMapping("/book/")
public class BookController {
	
	public static Logger logger = LoggerFactory.getLogger(BookController.class);
	@Autowired
	private BookService bookService;




	@Autowired
	private NewBookService newBookService;

	@Autowired
	private CommonService commonService;
	@Autowired
	private BookCategoryService bookCategoryService;
	@Autowired
	private BookCategoryMappingService bookCategoryMappingService;

	
	
	@RequestMapping("importbook")
	public void bachImportBook(@RequestParam(value = "path") String path){
		System.out.println(path);
		bookService.importBook(path);
	}
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false)Long  id,
			@RequestParam(value = "title") String title,
			@RequestParam(value = "keywords",required=false) String keywords,
			@RequestParam(value = "pageNums",required=false,defaultValue="0") int pageNums,
			@RequestParam(value = "publisherName",required=false) String publisherName,
			@RequestParam(value = "revision",required=false) String revision,
			//出版社
			@RequestParam(value = "publisherAddress",required=false) String publisherAddress,
			@RequestParam(value = "cip",required = false) String cip,
			@RequestParam(value = "copyrightNum",required = false) String copyrightNum,
			@RequestParam(value = "copyrightInfo",required = false) String copyrightInfo,
			@RequestParam(value = "printEdition",required = false) String printEdition,
			@RequestParam(value = "editorialRecommendation",required = false) String editorialRecommendation,
			@RequestParam(value = "publishYm",required = false) String publishYm,
			//出版国别
			@RequestParam(value = "publisherCountry",required = false) String publisherCountry,
			@RequestParam(value = "seriesName",required = false) String seriesName,
			@RequestParam(value = "price",required = false) String price,
			@RequestParam(value = "readerObject",required = false) String readerObject,
			@RequestParam(value = "category",required = false) String category,
			@RequestParam(value = "categoryNum",required = false) String categoryNum,
			@RequestParam(value = "branch",required = false) String branch,
			@RequestParam(value = "advertising",required = false) String advertising,
			@RequestParam(value = "postscript",required = false) String postscript,
			@RequestParam(value = "thick",required = false) String thick,
			@RequestParam(value = "awardSituation",required = false) String awardSituation,
			@RequestParam(value = "recommendedShelfCategories",required = false) String recommendedShelfCategories,
			@RequestParam(value = "medium",required = false) String medium,
			@RequestParam(value = "folio",required = false) String folio,
			@RequestParam(value = "mediaReview",required = false) String mediaReview,
			@RequestParam(value = "celebrityRecommendation",required = false) String celebrityRecommendation,
			@RequestParam(value = "catalog",required = false) String catalog,
			@RequestParam(value = "contentValidity",required = false) String contentValidity,
			@RequestParam(value = "preface",required = false) String preface,
			@RequestParam(value = "commodityType",required = false) String commodityType,
			@RequestParam(value = "barcode",required = false) String barcode,
			@RequestParam(value = "size",required = false) String size,
			@RequestParam(value = "level",required = false) String level,
			@RequestParam(value = "brand",required = false) String brand,
			@RequestParam(value = "status",required = false) String status,
			@RequestParam(value = "preface2",required = false) String preface2,
			@RequestParam(value = "serviceClassification",required = false) String serviceClassification,
			@RequestParam(value = "serviceClassificationName",required = false) String serviceClassificationName,
			@RequestParam(value = "translator",required = false) String translator,
			@RequestParam(value = "translatorProfiles",required = false) String translatorProfiles,
			@RequestParam(value = "translatorPreface",required = false) String translatorPreface,
			@RequestParam(value = "sheet",required = false) String sheet,
			@RequestParam(value = "marketingClassification",required = false) String marketingClassification,
			@RequestParam(value = "paper",required = false) String paper,
			@RequestParam(value = "languages",required = false) String languages,
			@RequestParam(value = "readOnline",required = false) String readOnline,
			@RequestParam(value = "editorCharge",required = false) String editorCharge,
			@RequestParam(value = "graphClassification",required = false) String graphClassification,
			@RequestParam(value = "weight",required = false) String weight,
			//@RequestParam(value = "keywords",required = false) String keywords,
			@RequestParam(value = "binding",required = false) String binding,
			@RequestParam(value = "wordcount",required = false) String wordcount,
			@RequestParam(value = "author",required = false) String author,
			@RequestParam(value = "authorBrief",required = false) String authorBrief,
			@RequestParam(value = "authorPreface",required = false) String authorPreface,
			@RequestParam(value = "keySentences",required = false) String keySentences,
			@RequestParam(value = "shelvesDate",required = false) String shelvesDate,
			@RequestParam(value = "categoryCode",required = false) String categoryCode,
			@RequestParam(value = "shelfSuggestions",required = false) String shelfSuggestions,
			@RequestParam(value = "chapterCount",required = false) String chapterCount,
			@RequestParam(value = "originalTitle",required = false) String originalTitle,
			@RequestParam(value = "original_publisher",required = false) String original_publisher,
			@RequestParam(value = "cdCount",required = false) String cdCount,
			@RequestParam(value = "diskCount",required = false) String diskCount,
			@RequestParam(value = "magneticCount",required = false) String magneticCount,
			@RequestParam(value = "markData",required = false) String markData,
			@RequestParam(value = "author_nationality",required = false) String author_nationality,
			@RequestParam(value = "pressRecommendation",required = false) String pressRecommendation,
			@RequestParam(value = "cid",required=false)String cids,
			HttpServletResponse response
			){
		//转换日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			System.out.println(shelvesDate);
			date =	format.parse(shelvesDate);
		}catch (Exception e){
			e.printStackTrace();
		}

		List<Long> ids=getCids(cids);
		if(id==null){
			NewBook book=new NewBook();
			book.setTitle(title);
			//关键词
			book.setKeySentences(keySentences);
			//页数
			book.setPageSize(pageNums);
			//出版社
			book.setPublish(publisherAddress);
			//isbn号
			book.setIsbn(publisherName);
			//版次
			book.setRevision(revision);
			//cip
			book.setCip(cip);
			if (copyrightNum!=null) {
				//版权登记号
				book.setCopyrightNum(Integer.valueOf(copyrightNum));
			}
			//版权信息
			book.setCopyrightInfo(copyrightInfo);
			//版印次
			book.setPrintEdition(printEdition);
			//编辑推荐
			book.setEditorialRecommendation(editorialRecommendation);
			//出版年月
			book.setPublishYm(publishYm);
			//出版国别
			book.setPublisherCountry(publisherCountry);
			//丛书名
			book.setSeriesName(seriesName);
			if (price!=null) {
				//定价
				book.setPrice(Float.valueOf(price));
			}
			//读者对象
			book.setReaderObject(readerObject);
			//分类
			book.setCategory(category);
			//分类号
			book.setCategoryNum(categoryNum);
			//分社
			book.setBranch(branch);
			//广告语
			book.setAdvertising(advertising);
			//后记
			book.setPostscript(postscript);
			if (thick != null) {
				//厚
				book.setThick(Integer.valueOf(thick));
			}
			//获奖情况
			book.setAwardSituation(awardSituation);
			//建议上架类别
			book.setRecommendedShelfCategories(recommendedShelfCategories);
			//介子
			book.setMedium(medium);
			//开本
			book.setFolio(folio);
			//媒体评论
			book.setMediaReview(mediaReview);
			//名人推荐
			book.setCelebrityRecommendation(celebrityRecommendation);
			//目录
			book.setCatalog(catalog);
			//内容简介
			book.setContentValidity(contentValidity);
			//前言
			book.setPreface(preface);
			//商品类型
			book.setCommodityType(commodityType);
			//条码书号
			book.setBarcode(barcode);
			//图书尺寸
			book.setSize(size);
			//图书等级
			book.setLevel(level);
			//图书品牌
			book.setBrand(brand);
			//图书状态
			book.setStatus(status);
			//序言
			book.setPreface2(preface2);
			//业务分类
			book.setServiceClassification(serviceClassification);
			//业务分类名称
			book.setServiceClassificationName(serviceClassificationName);
			//翻译者
			book.setTranslator(translator);
			//翻译者简介
			book.setTranslatorProfiles(translatorProfiles);
			//翻译者序
			book.setTranslatorPreface(translatorPreface);
			if (sheet!=null) {
				//印章
				book.setSheet(Float.valueOf(sheet));
			}
			//营销分类
			book.setMarketingClassification(marketingClassification);
			//用纸
			book.setPaper(paper);
			//语种
			book.setLanguages(languages);
			//在线阅读
			book.setReadOnline(readOnline);
			//责任编辑
			book.setEditorCharge(editorCharge);
			//中图书法分类
			book.setGraphClassification(graphClassification);
			if (weight!=null) {
				//重量
				book.setWeight(Integer.valueOf(weight));
			}
			//主题词
			book.setKeywords(keywords);
			//装帧
			book.setBinding(binding);
			if (wordcount != null) {
				//字数
				book.setWordcount(Long.valueOf(wordcount));
			}
			//作者
			book.setAuthor(author);
			//作者简介
			book.setAuthorBrief(authorBrief);
			//作者序
			book.setAuthorPreface(authorPreface);

			//上架日期
			book.setShelvesDate(date);

			//分类code
			book.setCategoryCode(categoryCode);
			//上架建议
			book.setShelfSuggestions(shelfSuggestions);
			if (chapterCount != null) {
				//章节数
				book.setChapterCount(Integer.valueOf(chapterCount));
			}
			//原书名
			book.setOriginalTitle(originalTitle);
			//原出版社
			book.setOriginal_publisher(original_publisher);
			if (cdCount != null) {
				//有无cd
				book.setCdCount(Integer.valueOf(cdCount));
			}
			if (diskCount != null) {
				//有无磁盘
				book.setDiskCount(Integer.valueOf(diskCount));
			}
			if (magneticCount != null) {
				//有无磁带
				book.setMagneticCount(Integer.valueOf(magneticCount));
			}
			//mark数据
			book.setMarkData(markData);
			//作者国籍
			book.setAuthor_nationality(author_nationality);
			//出版社推荐
			book.setPressRecommendation(pressRecommendation);


//			try {
//				//date = sdf.parse(pubDateStr);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
			//book.setShelvesDate(date);



			//根据isbn去重如果isbn存在不进行创建图书
			NewBook newBook = newBookService.findByIsbn(publisherName);
			if (newBook == null){
				bookService.saveNewBook(book);
				System.out.println(book.getIsbn());

				if(ids!=null&&ids.size()>0){
					for(Long cid:ids){
						BookCategory bc= bookCategoryService.findById(cid);
						if(bc!=null){
							BookCategoryMapping bcm=createNewBookCategoryMappingEntry(book,bc);
							bookCategoryMappingService.save(bcm);
						}

					}
				}
			}else {
				commonService.returnDate(response,"ISBN号已经存在");
			}
		}else{
			//编辑
			bookCategoryMappingService.deleteByBookid(id);
			NewBook book=newBookService.findById(id);

			book.setTitle(title);
			//关键词
			book.setKeySentences(keySentences);
			//页数
			book.setPageSize(pageNums);
			//出版社
			book.setPublish(publisherName);
			//isbn号
			book.setIsbn(publisherAddress);
			//版次
			System.out.println(revision);
			book.setRevision(revision);
			//cip
			book.setCip(cip);
			if (copyrightNum!=null) {
				//版权登记号
				book.setCopyrightNum(Integer.valueOf(copyrightNum));
			}
			//版权信息
			book.setCopyrightInfo(copyrightInfo);
			//版印次
			book.setPrintEdition(printEdition);
			//编辑推荐
			book.setEditorialRecommendation(editorialRecommendation);
			//出版年月
			book.setPublishYm(publishYm);
			//出版国别
			book.setPublisherCountry(publisherCountry);
			//丛书名
			book.setSeriesName(seriesName);
			if (price!=null) {
				//定价
				book.setPrice(Float.valueOf(price));
			}
			//读者对象
			book.setReaderObject(readerObject);
			//分类
			book.setCategory(category);
			//分类号
			book.setCategoryNum(categoryNum);
			//分社
			book.setBranch(branch);
			//广告语
			book.setAdvertising(advertising);
			//后记
			book.setPostscript(postscript);
			if (thick != null) {
				//厚
				book.setThick(Integer.valueOf(thick));
			}
			//获奖情况
			book.setAwardSituation(awardSituation);
			//建议上架类别
			book.setRecommendedShelfCategories(recommendedShelfCategories);
			//介子
			book.setMedium(medium);
			//开本
			book.setFolio(folio);
			//媒体评论
			book.setMediaReview(mediaReview);
			//名人推荐
			book.setCelebrityRecommendation(celebrityRecommendation);
			//目录
			book.setCatalog(catalog);
			//内容简介
			book.setContentValidity(contentValidity);
			//前言
			book.setPreface(preface);
			//商品类型
			book.setCommodityType(commodityType);
			//条码书号
			book.setBarcode(barcode);
			//图书尺寸
			book.setSize(size);
			//图书等级
			book.setLevel(level);
			//图书品牌
			book.setBrand(brand);
			//图书状态
			book.setStatus(status);
			//序言
			book.setPreface2(preface2);
			//业务分类
			book.setServiceClassification(serviceClassification);
			//业务分类名称
			book.setServiceClassificationName(serviceClassificationName);
			//翻译者
			book.setTranslator(translator);
			//翻译者简介
			book.setTranslatorProfiles(translatorProfiles);
			//翻译者序
			book.setTranslatorPreface(translatorPreface);
			if (sheet!=null) {
				//印章
				book.setSheet(Float.valueOf(sheet));
			}
			//营销分类
			book.setMarketingClassification(marketingClassification);
			//用纸
			book.setPaper(paper);
			//语种
			book.setLanguages(languages);
			//在线阅读
			book.setReadOnline(readOnline);
			//责任编辑
			book.setEditorCharge(editorCharge);
			//中图书法分类
			book.setGraphClassification(graphClassification);
			if (weight!=null) {
				//重量
				book.setWeight(Integer.valueOf(weight));
			}
			//主题词
			book.setKeywords(keywords);
			//装帧
			book.setBinding(binding);
			if (wordcount != null) {
				//字数
				book.setWordcount(Long.valueOf(wordcount));
			}
			//作者
			book.setAuthor(author);
			//作者简介
			book.setAuthorBrief(authorBrief);
			//作者序
			book.setAuthorPreface(authorPreface);


			//上架日期
			book.setShelvesDate(date);

			//分类code
			book.setCategoryCode(categoryCode);
			//上架建议
			book.setShelfSuggestions(shelfSuggestions);
			if (chapterCount != null) {
				//章节数
				book.setChapterCount(Integer.valueOf(chapterCount));
			}
			//原书名
			book.setOriginalTitle(originalTitle);
			//原出版社
			book.setOriginal_publisher(original_publisher);
			if (cdCount != null) {
				//有无cd
				book.setCdCount(Integer.valueOf(cdCount));
			}
			if (diskCount != null) {
				//有无磁盘
				book.setDiskCount(Integer.valueOf(diskCount));
			}
			if (magneticCount != null) {
				//有无磁带
				book.setMagneticCount(Integer.valueOf(magneticCount));
			}
			//mark数据
			book.setMarkData(markData);
			//作者国籍
			book.setAuthor_nationality(author_nationality);
			//出版社推荐
			book.setPressRecommendation(pressRecommendation);
			
			if(ids!=null&&ids.size()>0){
				for(Long cid:ids){
					BookCategory bc= bookCategoryService.findById(cid);
					if(bc!=null){
						BookCategoryMapping bcm=createNewBookCategoryMappingEntry(book,bc);
						bookCategoryMappingService.save(bcm);
					}
					
				}
			}
			
		}
		
		commonService.returnDate(response,new ReturnJson());
		
	}
	
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		bookCategoryMappingService.deleteByBookid(id);
		bookService.delete(id);
		commonService.returnDate(response,new ReturnJson());
		
	}

	@RequestMapping("deletenew")
	public void deletenew(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
	){
		bookCategoryMappingService.deleteByBookid(id);
		bookService.deletenew(id);
		commonService.returnDate(response,new ReturnJson());

	}
	
	@RequestMapping("list")
	public void list(@RequestParam(value = "attributeName") String attributeName,
			@RequestParam(value = "attributeValue") String attributeValue,
			HttpServletResponse response) throws IOException{
		
	}
	

//	@RequestMapping("outline")
//	public void outline(
//			@RequestParam(value = "pn",required=false,defaultValue="1") int pn,
//			@RequestParam(value = "ps",required=false,defaultValue="20") int ps,
//			HttpServletResponse response) throws IOException{
//
//		commonService.returnDate(response, bookService.outline(pn, ps));
//
//	}
	
	public BookCategoryMapping createBookCategoryMappingEntry(NewBook book,BookCategory bc){
		BookCategoryMapping bcm=new BookCategoryMapping();
		bcm.setBookid(book.getId());
		bcm.setCatid(bc.getId());
		bcm.setKeywords(book.getKeySentences());
		bcm.setPageNums(book.getPageSize());
		bcm.setPublisherAddress(book.getIsbn());
		bcm.setPublisherName(book.getPublish());
		bcm.setTitle(book.getTitle());
		bcm.setPubDateStr(book.getRevision());
		
		return bcm;
	}






	public BookCategoryMapping createNewBookCategoryMappingEntry(NewBook book,BookCategory bc){
		BookCategoryMapping bcm=new BookCategoryMapping();
		bcm.setBookid(book.getId());
		bcm.setCatid(bc.getId());
		bcm.setKeywords(book.getKeySentences());
		bcm.setPageNums(book.getPageSize());
		bcm.setPublisherAddress(book.getIsbn());
		bcm.setPublisherName(book.getPublish());
		bcm.setTitle(book.getTitle());
		bcm.setPubDateStr(book.getRevision());

		return bcm;
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
