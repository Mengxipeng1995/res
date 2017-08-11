package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.InlinePicDAO;
import com.cmp.res.entity.InlinePic;

@Service
public class InlinePicService implements BaseService<InlinePic>{
	
	@Autowired
	private InlinePicDAO inlinePicDAO;

	@Override
	public void save(InlinePic t) {
		// TODO Auto-generated method stub
		inlinePicDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		inlinePicDAO.delete(id);
		
	}

	@Override
	public InlinePic findById(Long id) {
		// TODO Auto-generated method stub
		return inlinePicDAO.findOne(id);
	}

	@Override
	public void delete(InlinePic t) {
		// TODO Auto-generated method stub
		inlinePicDAO.delete(t);
	}

}
