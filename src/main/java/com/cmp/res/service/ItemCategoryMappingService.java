package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ItemCategoryMappingDAO;
import com.cmp.res.entity.ItemCategoryMapping;
import com.cmp.res.util.CommonUtil;

@Service
public class ItemCategoryMappingService implements BaseService<ItemCategoryMapping>{
	
	
	
	@Autowired
	private ItemCategoryMappingDAO itemCategoryMappingDAO;
	
	public void deleteByItemid(Long id){
		itemCategoryMappingDAO.deleteByItemid(id);
	}
	
	
	
	public Page<ItemCategoryMapping> outline(int pn,int ps,Long catid){
		
		PageRequest pageRequest = CommonUtil.buildPageRequest(pn, ps, null, null, null);
		
		//return itemCategoryMappingDAO.findByCatid(catid,pageRequest);
		
		return itemCategoryMappingDAO.findByCatidOutline(catid,pageRequest);
		
	}

	public List<ItemCategoryMapping> findByCatid(Long catid){
		return itemCategoryMappingDAO.findByCatid(catid);
	}

	@Override
	public void save(ItemCategoryMapping t) {
		// TODO Auto-generated method stub
		itemCategoryMappingDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		itemCategoryMappingDAO.delete(id);
	}

	@Override
	public ItemCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return itemCategoryMappingDAO.findOne(id);
	}

	@Override
	public void delete(ItemCategoryMapping t) {
		// TODO Auto-generated method stub
		itemCategoryMappingDAO.delete(t);
	}

}
