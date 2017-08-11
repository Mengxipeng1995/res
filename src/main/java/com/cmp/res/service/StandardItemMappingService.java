package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.StandardItemMappingDAO;
import com.cmp.res.entity.StandardItemMapping;

@Service
public class StandardItemMappingService implements BaseService<StandardItemMapping>{
	@Autowired
	private StandardItemMappingDAO standardItemMappingDAO;

	@Override
	public void save(StandardItemMapping t) {
		// TODO Auto-generated method stub
		standardItemMappingDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		standardItemMappingDAO.delete(id);
		
	}

	@Override
	public StandardItemMapping findById(Long id) {
		// TODO Auto-generated method stub
		return standardItemMappingDAO.findOne(id);
	}

	@Override
	public void delete(StandardItemMapping t) {
		// TODO Auto-generated method stub
		standardItemMappingDAO.delete(t);
		
	}
	
	

}
