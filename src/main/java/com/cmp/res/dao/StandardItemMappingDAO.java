package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.StandardItemMapping;

public interface StandardItemMappingDAO   extends PagingAndSortingRepository<StandardItemMapping, Long>,JpaSpecificationExecutor<StandardItemMapping>{

}
