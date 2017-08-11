package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemCategoryMapping;

public interface ItemCategoryMappingDAO  extends PagingAndSortingRepository<ItemCategoryMapping, Long>,JpaSpecificationExecutor<ItemCategoryMapping>{

	
	public List<ItemCategoryMapping> findByCatid(Long catid);
	
	public Page<ItemCategoryMapping> findByCatid(Long catid,Pageable page);
	
	@Modifying
	@Transactional
	@Query("update ItemCategoryMapping set deleteFlag=1 where  itemid=?1")
	public void deleteByItemid(Long id);
	
	
	
	@Query("from ItemCategoryMapping as a where id =(select max(id) from ItemCategoryMapping where a.resCode=resCode and deleteFlag!=1 and catid=?1)")
	public Page<ItemCategoryMapping> findByCatidOutline(Long catid,Pageable pageable);
	
}
