package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Magazine;

public interface MagazineDAO  extends PagingAndSortingRepository<Magazine, Long>,JpaSpecificationExecutor<Magazine>{

}
