package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Product;

public interface  ProductDAO extends PagingAndSortingRepository<Product, Long>,JpaSpecificationExecutor<Product>{
	
	

}
