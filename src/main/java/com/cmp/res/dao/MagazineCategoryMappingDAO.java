package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import com.cmp.res.entity.MagazineCategoryMapping;

public interface MagazineCategoryMappingDAO  extends PagingAndSortingRepository<MagazineCategoryMapping, Long>,JpaSpecificationExecutor<MagazineCategoryMapping>{

	@Modifying
	@Transactional
	@Query("delete from MagazineCategoryMapping mcm where mcm.magid = ?1")
	public void deleteByMagid(Long magid);
	
	public List<MagazineCategoryMapping> findByMagid(Long mcm);
}
