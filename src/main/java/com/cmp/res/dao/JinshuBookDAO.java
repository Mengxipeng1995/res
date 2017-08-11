package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.JinShuBook;
import com.cmp.res.entity.NewBook;

public interface JinshuBookDAO  extends PagingAndSortingRepository<JinShuBook, Long>,JpaSpecificationExecutor<JinShuBook>{

	
	public JinShuBook findByBarcode(String barcode);
}
