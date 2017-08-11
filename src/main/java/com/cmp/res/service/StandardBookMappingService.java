package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.StandardBookMappingDAO;
import com.cmp.res.entity.StandardBookMapping;

@Service
public class StandardBookMappingService implements BaseService<StandardBookMapping>{
	@Autowired
	private StandardBookMappingDAO standardBookMappingDAO;

	@Override
	public void save(StandardBookMapping t) {
		// TODO Auto-generated method stub
		standardBookMappingDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		standardBookMappingDAO.delete(id);
	}

	@Override
	public StandardBookMapping findById(Long id) {
		// TODO Auto-generated method stub
		return standardBookMappingDAO.findOne(id);
	}

	@Override
	public void delete(StandardBookMapping t) {
		// TODO Auto-generated method stub
		standardBookMappingDAO.delete(t);
	}

}
