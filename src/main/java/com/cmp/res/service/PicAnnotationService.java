package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.PicAnnotationDAO;
import com.cmp.res.entity.PicAnnotation;

@Service
public class PicAnnotationService implements BaseService<PicAnnotation>{

	@Autowired
	private PicAnnotationDAO picAnnotationDAO;
	@Override
	public void save(PicAnnotation t) {
		// TODO Auto-generated method stub
		picAnnotationDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		picAnnotationDAO.delete(id);
	}

	@Override
	public PicAnnotation findById(Long id) {
		// TODO Auto-generated method stub
		return picAnnotationDAO.findOne(id);
	}

	@Override
	public void delete(PicAnnotation t) {
		// TODO Auto-generated method stub
		picAnnotationDAO.delete(t);
		
	}

}
