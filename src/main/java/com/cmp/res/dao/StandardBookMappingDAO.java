package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Book;
import com.cmp.res.entity.StandardBookMapping;

public interface StandardBookMappingDAO  extends PagingAndSortingRepository<StandardBookMapping, Long>,JpaSpecificationExecutor<StandardBookMapping>{

}
