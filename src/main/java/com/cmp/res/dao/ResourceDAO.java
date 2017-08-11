package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Resource;

public interface ResourceDAO    extends PagingAndSortingRepository<Resource, Long>,JpaSpecificationExecutor<Resource>{

}
