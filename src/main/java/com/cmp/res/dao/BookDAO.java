package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Book;
import com.cmp.res.entity.User;



public interface  BookDAO extends PagingAndSortingRepository<Book, Long>,JpaSpecificationExecutor<Book>{
	
	

}
