package com.cmp.res.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.Product;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.User;
import com.cmp.res.entity.Video;
import com.cmp.res.event.CreatProductEvent;
import com.cmp.res.event.CreatPubProductEvent;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.ProductItemService;
import com.cmp.res.service.ProductService;
import com.cmp.res.service.VideoService;
import com.cmp.res.shiro.ShiroDBRealm.ShiroUser;
import com.cmp.res.util.Log4jUtils;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	public static Logger logger = LoggerFactory.getLogger(TestFormController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProductItemService productItemService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private VideoService videoService;
	
	@RequestMapping("list")
	public void list(
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			@RequestParam(value = "type",required=false)Integer type,
			HttpServletRequest request,HttpServletResponse response
			){
		
		commonService.returnDate(response, productService.list(type,pn, ps));
		
		
		
		
	}
	
	@RequestMapping("savePubProduct")
	public void savePubProduct(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "catid",required=false) Long catid,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		if(id==null){
			Product product=new Product();
			ShiroUser user=(ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if(catid!=null){
				//根据分类创建产品
				product.setCatid(catid);
				product.setCreateDate(new Date());
				product.setName(name);
				product.setCreaterName(user.getUserName());
				product.setCreaterNickName(user.getNickName());
				product.setStatus(2);//创建中
				
				productService.save(product);
				
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
				CreatPubProductEvent subjectEvent = new CreatPubProductEvent(product);
				webApplicationContext.publishEvent(subjectEvent);
				
			}
			
		}else{
			Product product=productService.findById(id);
			if(product!=null){
				product.setName(name);
				productService.save(product);
			}
			
		
			
		}
		
		commonService.returnDate(response, rj);
		
	}
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "catid",required=false) Long catid,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		if(id==null){
			Product product=new Product();
			ShiroUser user=(ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if(type!=3&&catid!=null){
				//根据分类创建产品
				product.setCatid(catid);
				product.setCreateDate(new Date());
				product.setName(name);
				product.setType(type);
				product.setCreaterName(user.getUserName());
				product.setCreaterNickName(user.getNickName());
				
				productService.save(product);
				
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
				CreatProductEvent subjectEvent = new CreatProductEvent(product);
				webApplicationContext.publishEvent(subjectEvent);
				
			}else{
				//创建混合型产品
				product.setCreateDate(new Date());
				product.setCreaterName(user.getNickName());
				product.setCreaterNickName(user.getNickName());
				product.setName(name);
				product.setType(3);
				
				productService.save(product);
			}
			
		}else{
			Product product=productService.findById(id);
			if(product!=null){
				product.setName(name);
				productService.save(product);
			}
			
		
			
		}
		
		commonService.returnDate(response, rj);
		
	}
	/**
	 * 只有混合类型的产品才可以添加
	 * @param pid
	 * @param items
	 * @param type
	 * @param request
	 * @param response
	 */
	@RequestMapping("addItemToProduct")
	public void addItemToProduct(
			@RequestParam(value = "pid",required=false) Long pid,
			@RequestParam(value = "items")Long[] ids,
			@RequestParam(value = "type")Integer type,//要添加条目的类型  0：文字，1：图片；2：视频
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		StringBuffer sb=new StringBuffer();
		Product product=productService.findById(pid);
		if(product!=null&&product.getType()==3){
			switch (type) {
			case 0:
				//文字
				List<Item> items=itemService.findByIdIn(ids);
				if(items!=null&&items.size()>0){
					for(Item item:items){
						if(productItemService.findByProductidAndItemid(product.getId(), item.getId())!=null){
							sb.append("条目("+item.getId()+":"+item.getTitle()+")在该产品中已经存在;");
							rj.setSuccess(false);
							continue;
						}
						ProductItem pi=new ProductItem();
						pi.setItemid(item.getId());
						pi.setItemKeywords(item.getKeywords());
						pi.setItemTitle(item.getTitle());
						pi.setProductid(product.getId());
						pi.setType(type);
						
						productItemService.save(pi);
					}
				}
				break;
			case 1:
				//图片
				List<Photo> photos= photoService.findByIdIn(ids);
				if(photos!=null&&photos.size()>0){
					for(Photo photo:photos){
						ProductItem pi=new ProductItem();
						pi.setItemid(photo.getId());
						pi.setItemTitle(photo.getTitleabbrev());
						pi.setProductid(product.getId());
						pi.setType(1);
						productItemService.save(pi);
					}
				}
				
				break;
			case 2:
				//视频
				List<Video> videos= videoService.findByIdIn(ids);
				if(videos!=null&&videos.size()>0){
					for(Video video:videos){
						ProductItem pi=new ProductItem();
						pi.setItemid(video.getId());
						pi.setItemTitle(video.getTitle());
						pi.setProductid(product.getId());
						pi.setType(2);
						productItemService.save(pi);
					}
				}
				break;
			}
			
		}else{
			rj.setSuccess(false);
			rj.setMsg("产品不存在或");
		}
		
		rj.setMsg(sb.toString());
		
		commonService.returnDate(response, rj);
		
	}
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response
			){
		productService.delete(id);
		
		productItemService.deleteByProductId(id);
		
		commonService.returnDate(response, new ReturnJson());
		
	}
	@RequestMapping("pubProduct")
	public void pubProduct(
			@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response
			){
		User user=commonService.getCurrentLogin();
		ReturnJson rj=new ReturnJson();
		try{
			Product product=productService.findById(id);
			product.setStatus(1);
			
			productService.save(product);
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					user.getUserName(), 10, product.getId(), user.getNickName()) ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"产品发布：【"+product.getName()+"】");
		}catch (Exception e) {
			// TODO: handle exception
			rj.setSuccess(false);
			rj.setMsg("发布失败");
		}
		
		commonService.returnDate(response, rj);
	}
	
	

}
