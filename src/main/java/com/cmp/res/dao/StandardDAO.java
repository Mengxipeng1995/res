package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Standard;

public interface StandardDAO  extends PagingAndSortingRepository<Standard, Long>,JpaSpecificationExecutor<Standard>{

	public List<Standard> findByTitle(String title);
}
