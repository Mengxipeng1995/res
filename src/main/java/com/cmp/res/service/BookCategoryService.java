package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.BookCategoryDAO;
import com.cmp.res.entity.BookCategory;

@Service
public class BookCategoryService implements BaseService<BookCategory>{
	
	@Autowired
	private BookCategoryDAO bookCategoryDAO;
	
	public BookCategory getRoot(){
		return bookCategoryDAO.findByParentidIsNull();
	}


	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		bookCategoryDAO.delete(id);
		
	}

	@Override
	public BookCategory findById(Long id) {
		// TODO Auto-generated method stub
		
		return bookCategoryDAO.findOne(id);
	}

	@Override
	public void save(BookCategory bookCategory) {
		// TODO Auto-generated method stub
		
		bookCategoryDAO.save(bookCategory);
		
	}
	
	public List<BookCategory> findByParentid(Long pid){
		return bookCategoryDAO.findByParentid(pid);
	}
	
	public BookCategory findByParentidAndName(Long pid,String name){
		List<BookCategory> list=bookCategoryDAO.findByParentidAndName(pid, name);
		
		return (list!=null&&list.size()>0)?list.get(0):null;
	}

	@Override
	public void delete(BookCategory bookCategory) {
		// TODO Auto-generated method stub
		bookCategoryDAO.delete(bookCategory);
		
	}
}
