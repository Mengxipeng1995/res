package com.cmp.res.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemCategoryMapping;
import com.cmp.res.entity.PageEntity;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.SubjectItem;
import com.cmp.res.entity.User;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.ItemCategoryMappingService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.ProductItemService;
import com.cmp.res.service.SearchExprService;
import com.cmp.res.service.SearchService;
import com.cmp.res.service.SubjectItemService;
import com.cmp.res.util.Log4jUtils;
import com.cmp.res.util.SearchUtils;
import com.cmp.res.util.TRSStrFmter;
import com.cmp.res.util.ZipUtil;
import com.eprobiti.trs.TRSException;

@Controller
@RequestMapping("/item/")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private SearchService searchService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ItemCategoryMappingService icms;
	
	
	public static Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@RequestMapping("download")
	public void download(
			@RequestParam(value = "catid",required=false)Long catid,
			@RequestParam(value = "ids",required=false)Long[] ids,
			HttpServletRequest request,HttpServletResponse response
			) throws DocumentException, IOException{
		String uuid=UUID.randomUUID().toString();
		File file=new File(configService.getDownlTempPath()+uuid+"\\images");
		System.out.println("===============================>"+configService.getDownlTempPath()+uuid+"\\images");
		file.mkdirs();
		Element newRoot=new BaseElement("items");
		Document newDoc = DocumentHelper.createDocument(newRoot);
		List<Item> list=new ArrayList<Item>();
		if(catid!=null){
			Category category=categoryService.findById(catid);
			
			 cascade(category, list);
			
		}else if(ids!=null&&ids.length>0){
			list=itemService.findByIdIn(ids);
		}

        // 将根节点添加到文档中；     

	
	for(Item item:list){
		String conetent=item.getContent(),tempContent=conetent;
		int imgstart=conetent.indexOf("<img");
		while(imgstart>0	){
			
			int imgend=conetent.indexOf("/>", imgstart);
			String img=conetent.substring(imgstart, imgend+2);
			Document doc=DocumentHelper.parseText(img);
			Element root=doc.getRootElement();
			Photo photo=photoService.findByBookidLinkimage(item.getBookid(), root.attributeValue("src"));
			String replace="<img src='"+photo.getPath()+"' title='"+photo.getTitle()+"' type='"+photo.getRole()+"'>";
			
			tempContent=tempContent.replaceAll(img, replace);
			
			conetent=conetent.replaceAll(img, "");
			
			imgstart=conetent.indexOf("<img");
			
			//保存图片
			File image=new File(configService.getPhotoStorePath()+photo.getPath());
			FileUtil.copyFile(image, new File(configService.getDownlTempPath()+uuid+"\\images\\"+photo.getPath()));
		}
		
		item.setContent(tempContent);
		
		Element itemXML=new BaseElement("item");
		itemXML.addAttribute("title", item.getTitle());
		itemXML.addAttribute("etitle", item.getEtitle());
		itemXML.setText(item.getContent());
		newRoot.add(itemXML);
	}

		
		
		
		
		
		
		
		 FileOutputStream fos = new FileOutputStream(configService.getDownlTempPath()+uuid+"\\"+uuid+".xml");  
	     OutputStreamWriter osw = new OutputStreamWriter(fos,"utf8");  
	     OutputFormat of = new OutputFormat();  
	     of.setEncoding("utf8");  
	     of.setIndent(true);  
	     of.setIndent("    ");  
	     of.setNewlines(true);  
	     XMLWriter writer = new XMLWriter(osw, of);  
	     writer.write(newDoc);  
	     writer.close();  
	     
	     ZipUtil.zipMultiFile(configService.getDownlTempPath()+uuid,configService.getDownlTempPath()+uuid+".zip",true);
	     File zip=new File(configService.getDownlTempPath()+uuid+".zip");
		response.setContentType("application/x-zip-compressed;charset=UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ uuid+".zip");
		response.getOutputStream()
				.write(FileUtil.readAsByteArray(zip));
		
		zip.delete();
		File deleteTempFile=new File(configService.getDownlTempPath()+uuid);
		FileUtil.deleteContents(deleteTempFile);
		deleteTempFile.delete();
		
		
	}
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
	
		Item item=itemService.findById(id);
		
		if(item!=null){
			item.setDeleteFlag(1);
			itemService.saveItem(item);
			User user=commonService.getCurrentLogin();
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					user.getUserName(), 9, id,item.getTitle()) ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"删除条目【ID:"+id+"】");
			
			//删除分类关联信息【逻辑删除】
			icms.deleteByItemid(id);
			
			
		}else{
			rj.setSuccess(false);
			rj.setMsg("删除的词条不存在");
		}
		commonService.returnDate(response,rj );
	}
	
	@RequestMapping("findById")
	public void findById(@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		Item item=itemService.findById(id);
		item.setContent(StringUtils.isBlank(item.getContent())?"":commonService.doHtmlImage(item,item.getContent(), request.getContextPath()+"/photo/getImage?"));
		commonService.returnDate(response, item);
	}
	
	@RequestMapping("findByIdForEdit")
	public void findByIdForEdit(@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		Item item=itemService.findById(id);
		commonService.returnDate(response, item);
	}
	@RequestMapping("listItemByVersion")
	public void listItemByVersion(
			@RequestParam(value = "resCode",required=false) String resCode,
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			HttpServletRequest request,HttpServletResponse response
			){
		
		commonService.returnDate(response, itemService.listItemByResCode(resCode, pn, ps));
		
		
	}
	
	
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "catid",required=false) Long catid,
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			HttpServletRequest request,HttpServletResponse response){
		
		Page<Item> page=itemService.outline(pn, ps, catid);
		for(Item item:page.getContent()){
			List<Item> items=itemService.findByResCode(item.getResCode());
			item.setVersionCount(items.size());
			if(item.getContent()!=null){
				try {
					item.setContent(StringUtils.isBlank(item.getContent())?"":TRSStrFmter.doHtmlImage(item.getContent(), request.getContextPath()+"/photo/getImage?"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		commonService.returnDate(response, page);
		
	}
	
	@RequestMapping("search")
	public void search(
			@RequestParam(value = "searchWord")String searchWord,
			@RequestParam(value = "start",required=false,defaultValue="0")int start,
			@RequestParam(value = "limit",required=false,defaultValue="20")int limit,
			HttpServletRequest request,HttpServletResponse response){
		
		SearchExprService se=SearchExprService.getInstance();
		
		if(StringUtils.isNotBlank(searchWord)){
			searchWord=SearchUtils.escapeTRS(searchWord);
			//se.setContent(searchWord);
			se.setSw(searchWord);
		}
		
		PageEntity page=PageEntity.getInstanceByLimit(start, limit);
		
		
		try {
			page=searchService.searchQuery(se, page);
			
			commonService.returnDate(response, page);
		} catch (TRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false)Long id,
			@RequestParam(value = "title",required=false)String title,
			@RequestParam(value = "etitle",required=false)String etitle,
			@RequestParam(value = "keywords")String keywords,
			@RequestParam(value = "content",required=false)String content,
			@RequestParam(value = "catid",required=false)Long catid,
			@RequestParam(value = "versionDesc",required=false)String versionDesc,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		User user=commonService.getCurrentLogin();
		
		Item editItem=null;
		
		if(id==null){
			Date date=new Date();
			//新增条目
			Item item=new Item();
			item.setCatid(catid);
			item.setContent(content);
			item.setTitle(title);
			item.setEtitle(etitle);
			item.setKeywords(keywords);
			item.setCreateDate(date);
			item.setDeleteFlag(0);//非删除状态
			item.setOriginalVersionFlag(1);//新建词条
			item.setResCode(UUID.randomUUID().toString());
			item.setVersionCode(UUID.randomUUID().toString());
			item.setVersionDesc(versionDesc);
			item.setUserName(user.getUserName());
			item.setUserNickName(user.getNickName());
			itemService.saveItem(item);
			editItem=item;
		}else{
			//编辑条目
			Item item=itemService.findById(id);
			if(item!=null){
				//Item item2=item
				Item newItem=new Item();
				BeanUtils.copyProperties(item, newItem);
				newItem.setId(null);
				newItem.setOriginalVersionFlag(-1);// 编辑产生的中间版本
				newItem.setVersionCode(UUID.randomUUID().toString());
				newItem.setVersionDesc(versionDesc);
				newItem.setUserName(user.getUserName());
				newItem.setUserNickName(user.getNickName());
				newItem.setContent(content);
				newItem.setCreateDate(new Date());
				newItem.setEtitle(etitle);
				newItem.setKeywords(keywords);
				newItem.setTitle(title);
				itemService.saveItem(newItem);
				editItem=newItem;
			}else{
				rj.setSuccess(false);
				rj.setMsg("您要修改的条目不存在");
			}
			
		}
		if(editItem!=null){
			//保存条目关联关系
			Category cat=categoryService.findById(catid);
			if(cat!=null){
				ItemCategoryMapping icm=new ItemCategoryMapping(editItem,cat.getId());
				icms.save(icm);
				cat=cat.getParent();
			}
			
		}
		
		
		commonService.returnDate(response, rj);
		
		
	}
	
	//遍历获取分类下所有条目
	public void cascade(Category categroy,List<Item> list){
		if(categroy!=null){
			List<Item> items=itemService.findByCatid(categroy.getId());
			list.addAll(items);
			
			List<Category> sons=categoryService.findByParentid(categroy.getId(), 0);
			
			if(sons!=null&&sons.size()>0){
				for(Category son:sons){
					this.cascade(son, list);
				}
			}
			
		}
	}

}
