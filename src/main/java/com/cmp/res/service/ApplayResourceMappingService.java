package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ApplayResourceMappingDAO;
import com.cmp.res.entity.ApplayResourceMapping;
@Service
public class ApplayResourceMappingService implements BaseService<ApplayResourceMapping> {
	@Autowired
	private ApplayResourceMappingDAO applayResourceMappingDAO;
	
	public List<ApplayResourceMapping> findByApplayId(Long applayId){
		return applayResourceMappingDAO.findByApplayId(applayId);
	}

	@Override
	public void save(ApplayResourceMapping t) {
		// TODO Auto-generated method stub
		applayResourceMappingDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		applayResourceMappingDAO.delete(id);
	}

	@Override
	public ApplayResourceMapping findById(Long id) {
		// TODO Auto-generated method stub
		return applayResourceMappingDAO.findOne(id);
	}

	@Override
	public void delete(ApplayResourceMapping t) {
		// TODO Auto-generated method stub
		applayResourceMappingDAO.delete(t);
	}

}
