package com.cmp.res.listener;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemCategoryMapping;
import com.cmp.res.entity.Product;
import com.cmp.res.entity.ProductCategory;
import com.cmp.res.entity.ProductCategoryMapping;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.event.CreatProductEvent;
import com.cmp.res.event.CreatPubProductEvent;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.ItemCategoryMappingService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.ProductCategoryMappingService;
import com.cmp.res.service.ProductCategoryService;
import com.cmp.res.service.ProductItemService;
import com.cmp.res.service.ProductService;



public class CreateProductListener  implements ApplicationListener{

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ItemCategoryMappingService itemCategoryMappingService;
	@Autowired
	private ProductCategoryMappingService pcms;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	


	public void onApplicationEvent(ApplicationEvent event) {
		
		// TODO Auto-generated method stub
		if(event instanceof CreatProductEvent){
			//废弃
			
			Product product=(Product) event.getSource();
			Category cat=categoryService.findById(product.getCatid());
			saveProudctItems(cat,product.getId(),product.getType());
			
			
	       }else if(event instanceof CreatPubProductEvent){
	    	   //创建发布端需要的产品
	    	   Product product=(Product) event.getSource();
	    	   Category cat=categoryService.findById(product.getCatid());
	    	   
	    	   savePubProudctItems(cat,product.getId(),true);
	    	   //创建完成修改状态为编辑状态
	    	   product.setStatus(0);
	    	   
	    	   productService.save(product);
	       }
		
	}
	
	public void savePubProudctItems(Category cat,Long productid,boolean rootFlag){
		if(cat!=null){
			
			ProductCategory productCategory=new ProductCategory(cat,productid);
			if(rootFlag){
				productCategory.setParentid(null);
			}else {
				productCategory.setParentid(productCategoryService.findByCatidAndProductId(cat.getParentid(), productid).getId());
			}
			
			productCategoryService.save(productCategory);
			/**
			 * 后期需要调整
			 */
			List<ItemCategoryMapping> icms= itemCategoryMappingService.findByCatid(cat.getId());
			
			if(icms!=null&&icms.size()>0){
				for(ItemCategoryMapping icm:icms){
					ProductCategoryMapping pcm=new ProductCategoryMapping();
					pcm.setCatid(icm.getCatid());
					pcm.setCreateDate(icm.getCreateDate());
					pcm.setCreater(pcm.getCreater());
					pcm.setItemid(icm.getItemid());
					pcm.setProudctid(productid);
					pcm.setTitle(icm.getTitle());
					
					pcms.save(pcm);
				}
			}
			
			List<Category> sons=categoryService.findByParentid(cat.getId(), 0);
			if(sons!=null&&sons.size()>0){
				for(Category son:sons){
					savePubProudctItems(son,productid,false);
				}
			}
			
		}
	}
	
	
	public void saveProudctItems(Category cat,Long productid,Integer type){
		if(cat!=null){
			//查询该分类下的条目
			List<ItemCategoryMapping> icms= itemCategoryMappingService.findByCatid(cat.getId());
			if(icms!=null&&icms.size()>0){
				for(ItemCategoryMapping icm:icms){
					ProductCategoryMapping pcm=new ProductCategoryMapping();
					pcm.setCatid(icm.getCatid());
					pcm.setCreateDate(icm.getCreateDate());
					pcm.setCreater(pcm.getCreater());
					pcm.setItemid(icm.getItemid());
					pcm.setProudctid(productid);
					pcm.setTitle(icm.getTitle());
					
					pcms.save(pcm);
				}
			}
			
			//List<Item> items=itemService.findByCatid(cat.getId());
//			if(items!=null&&items.size()>0){
//				for(Item item:items){
//					ProductItem pi=new ProductItem();
//					pi.setCatid(cat.getId());
//					pi.setItemid(item.getId());
//					pi.setItemKeywords(item.getKeywords());
//					pi.setItemTitle(item.getTitle());
//					pi.setProductid(productid);
//					productItemService.save(pi);
//				}
//			}
			List<Category> sons=categoryService.findByParentid(cat.getId(), type);
			if(sons!=null&&sons.size()>0){
				for(Category son:sons){
					saveProudctItems(son,productid,type);
				}
			}
			
			
		}
	}



}
