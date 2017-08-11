package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.VideoCategoryMapping;

public interface VideoCategoryMappingDAO   extends PagingAndSortingRepository<VideoCategoryMapping, Long>,JpaSpecificationExecutor<VideoCategoryMapping>{
	
	public List<VideoCategoryMapping> findByVideoId(long videoId);
	
	@Modifying
	@Transactional
	@Query("delete from VideoCategoryMapping vcm where vcm.videoId = ?1")
	public void deleteNas(long videoId);
	
	
	@Modifying
	@Transactional
	@Query("update VideoCategoryMapping vcm set  deleteFlag=1 where vcm.videoId = ?1")
	public void deleteLogic(long videoId);
	
	public Page<VideoCategoryMapping> findByDeleteFlagIsNullAndCatid(Long catid,Pageable pageable);   
	

}
