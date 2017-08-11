package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.UserGroupMapping;

public interface UserGroupMappingDAO   extends PagingAndSortingRepository<UserGroupMapping, Long>,JpaSpecificationExecutor<UserGroupMapping> {

	@Modifying
	@Transactional
	@Query("delete from UserGroupMapping ugm where ugm.userId = ?1")
	public void deleteByUserid(Long userid);
	
	public List<UserGroupMapping> findByUserId(Long userid);
}
