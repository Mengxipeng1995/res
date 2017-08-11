package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.KeyWordType;

public interface KeyWordTypeDAO  extends PagingAndSortingRepository<KeyWordType, Long>,JpaSpecificationExecutor<KeyWordType>{

}
