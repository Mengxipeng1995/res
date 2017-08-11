package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.BookCategory;

public interface BookCategoryDAO  extends PagingAndSortingRepository<BookCategory, Long>,JpaSpecificationExecutor<BookCategory>{
	
	public List<BookCategory> findByParentidAndName(Long pid,String name);
	
	public List<BookCategory> findByParentid(Long pid);
	
	public BookCategory findByParentidIsNull();

}
