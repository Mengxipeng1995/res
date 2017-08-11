package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.JinshuBookDAO;
import com.cmp.res.entity.JinShuBook;

/**
*2017年7月14日
*@liao
*res
NewBookService.java
*
**/
@Service
public class JinshuwBookService implements BaseService<JinShuBook>{
	
	@Autowired
	private JinshuBookDAO jinshwBookDAO;
	
	public JinShuBook findByBarcode(String barcode){
		return jinshwBookDAO.findByBarcode(barcode);
	}

	@Override
	public void save(JinShuBook t) {
		// TODO Auto-generated method stub
		jinshwBookDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		jinshwBookDAO.delete(id);
	}

	@Override
	public JinShuBook findById(Long id) {
		// TODO Auto-generated method stub
		return jinshwBookDAO.findOne(id);
	}

	@Override
	public void delete(JinShuBook t) {
		// TODO Auto-generated method stub
		jinshwBookDAO.delete(t);
	}

	
	

}
