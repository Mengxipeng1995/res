package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemIndexRel;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ItemIndexRelService;
import com.cmp.res.service.ItemService;

@Controller
@RequestMapping("/itemrel/")
public class ItemIndexRelController {
	@Autowired
	private ItemIndexRelService itemIndexRelService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "page",required=false,defaultValue="1")int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20")int ps,
			@RequestParam(value = "searchWord",required=false)String searchWord,
			HttpServletRequest request,HttpServletResponse response
			){
		
		commonService.returnDate(response, itemIndexRelService.outline(searchWord,pn, ps));
		
		
	}
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false)Long id,
			@RequestParam(value = "itemId",required=false)Long itemId,
			@RequestParam(value = "indexName")String indexName,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		if(id==null){
			//新增索引词
			
			if(itemId!=null){
				Item item=itemService.findById(itemId);
				if(item!=null){
					ItemIndexRel index=new ItemIndexRel();
					index.setEtitle(item.getEtitle());
					index.setTitle(item.getTitle());
					index.setIndexName(indexName);
					index.setItemid(itemId);
					
					itemIndexRelService.saveItemIndexRel(index);
					
				}else{
					rj.setSuccess(false);
					rj.setMsg("请指定条目");
				}
			}else{
				rj.setSuccess(false);
				rj.setMsg("请指定条目");
			}
			
		}else{
			//编辑索引词
			ItemIndexRel index=itemIndexRelService.findById(id);
			if(index!=null){
				index.setIndexName(indexName);
				itemIndexRelService.saveItemIndexRel(index);
			}else{
				rj.setSuccess(false);
				rj.setMsg("条目不存在");
			}
			
		}
		
		commonService.returnDate(response, rj);
		
		
	}
	@RequestMapping("delete")
	public void delete(@RequestParam(value = "id")Long id,
			HttpServletRequest request,HttpServletResponse response){
		
		itemIndexRelService.delete(id);
		commonService.returnDate(response, new ReturnJson());
		
	}

}
