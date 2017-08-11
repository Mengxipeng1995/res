package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.SysProperty;

public interface SysPropertyDAO extends PagingAndSortingRepository<SysProperty, Long>,JpaSpecificationExecutor<SysProperty>{

	public SysProperty findByProKey(String key);
}
