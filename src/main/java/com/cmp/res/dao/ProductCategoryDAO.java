package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ProductCategory;

public interface ProductCategoryDAO   extends PagingAndSortingRepository<ProductCategory, Long>,JpaSpecificationExecutor<ProductCategory>{

	public ProductCategory findByCatidAndProductId(long catid,long productId);
}
