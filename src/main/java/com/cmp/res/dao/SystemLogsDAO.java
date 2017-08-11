package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.SystemLogs;



public interface  SystemLogsDAO extends PagingAndSortingRepository<SystemLogs, Long>,JpaSpecificationExecutor<SystemLogs>{
	

}
