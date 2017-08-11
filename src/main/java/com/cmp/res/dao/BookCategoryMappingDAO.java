package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.BookCategoryMapping;

public interface BookCategoryMappingDAO   extends PagingAndSortingRepository<BookCategoryMapping, Long>,JpaSpecificationExecutor<BookCategoryMapping>{
	
	@Modifying
	@Transactional
	@Query("delete from BookCategoryMapping bcm where bcm.bookid = ?1")
	public void deleteByBookid(Long bookid);
	
	public List<BookCategoryMapping> findByBookid(Long bookid);



}
