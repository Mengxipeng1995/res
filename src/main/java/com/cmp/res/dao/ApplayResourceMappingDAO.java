package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ApplayResourceMapping;
import com.cmp.res.entity.Apply;

public interface ApplayResourceMappingDAO   extends PagingAndSortingRepository<ApplayResourceMapping, Long>,JpaSpecificationExecutor<ApplayResourceMapping>{

	public List<ApplayResourceMapping> findByApplayId(Long applayId);
}
