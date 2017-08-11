package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Book;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemIndexRel;
import com.cmp.res.entity.PhotoCategoryRel;
import com.cmp.res.entity.User;



public interface  PhotoCategoryRelDAO extends PagingAndSortingRepository<PhotoCategoryRel, Long>,JpaSpecificationExecutor<PhotoCategoryRel>{
	
	
	
	

}
