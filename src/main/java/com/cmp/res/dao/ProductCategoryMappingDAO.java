package com.cmp.res.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ProductCategoryMapping;

public interface ProductCategoryMappingDAO    extends PagingAndSortingRepository<ProductCategoryMapping, Long>,JpaSpecificationExecutor<ProductCategoryMapping>{

	
	public Page<ProductCategoryMapping> findByProudctidAndCatid(Long  productid, Long catid,Pageable pageable);   
}
