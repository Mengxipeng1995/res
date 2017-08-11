package com.cmp.res.service;

import com.cmp.res.dao.BookCategoryMappingDAO;
import com.cmp.res.entity.Book;
import com.cmp.res.entity.BookCategoryMapping;
import com.cmp.res.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.NewBookDAO;
import com.cmp.res.entity.NewBook;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
*2017年7月14日
*@liao
*res
NewBookService.java
*
**/
@Service
public class NewBookService implements BaseService<NewBook>{
	
	@Autowired
	private NewBookDAO newBookDAO;




//新增查询方法
public Page<NewBook> outline(int pn, int ps){
	return newBookDAO.findAll(CommonUtil.buildPageRequest(pn, ps, null, null, null));
}


	public Iterable<NewBook> findAll(){
		return newBookDAO.findAll();
		
	}


	
	public NewBook findByIsbnAndPrintEdition(String isbn,String printEdition){
		return newBookDAO.findByIsbnAndPrintEdition(isbn, printEdition);
	//	return newBookDAO.findByIsbnAndPrintEdition(isbn);
	}


	//新增isbn查询
	public NewBook findByIsbn(String isbn){
		return newBookDAO.findByIsbn(isbn);
	}
	//新增修改版次
	public void edition(String book,long id){
		newBookDAO.increaEdition(book,id);
	}

	@Override
	public void save(NewBook t) {
		// TODO Auto-generated method stub
		newBookDAO.save(t);
		
	}


	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		newBookDAO.delete(id);
		
	}

	@Override
	public NewBook findById(Long id) {
		// TODO Auto-generated method stub
		return newBookDAO.findOne(id);
	}


	@Override
	public void delete(NewBook t) {
		// TODO Auto-generated method stub
		newBookDAO.delete(t);
		
	}



}
