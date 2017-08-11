package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ItemPhotoMappingDAO;
import com.cmp.res.entity.ItemPhotoMapping;

@Service
public class ItemPhotoMappingService implements BaseService<ItemPhotoMapping>{
	
	@Autowired
	private ItemPhotoMappingDAO itemPhotoMappingDAO;

	@Override
	public void save(ItemPhotoMapping t) {
		// TODO Auto-generated method stub
		itemPhotoMappingDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		itemPhotoMappingDAO.delete(id);
		
	}

	@Override
	public ItemPhotoMapping findById(Long id) {
		// TODO Auto-generated method stub
		return itemPhotoMappingDAO.findOne(id);
	}

	@Override
	public void delete(ItemPhotoMapping t) {
		// TODO Auto-generated method stub
		itemPhotoMappingDAO.delete(t);
		
	}

}
