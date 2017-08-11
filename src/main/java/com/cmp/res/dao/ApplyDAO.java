package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Apply;
import com.cmp.res.entity.Book;

public interface ApplyDAO  extends PagingAndSortingRepository<Apply, Long>,JpaSpecificationExecutor<Apply>{

}
