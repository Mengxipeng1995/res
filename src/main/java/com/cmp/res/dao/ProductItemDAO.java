package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.ProductItem;

public interface ProductItemDAO extends PagingAndSortingRepository<ProductItem, Long>,JpaSpecificationExecutor<ProductItem>{

	public ProductItem findByProductidAndItemid(Long pid,Long itemid);
	@Modifying
	@Transactional
	@Query("delete from ProductItem pi where pi.productid = ?1")
	public void deleteByProductId(Long id);
	
	public List<ProductItem> findByItemidAndType(long itemid,int type);
}
