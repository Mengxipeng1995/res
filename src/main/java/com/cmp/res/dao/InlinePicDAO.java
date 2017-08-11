package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.InlinePic;


public interface InlinePicDAO  extends PagingAndSortingRepository<InlinePic, Long>,JpaSpecificationExecutor<InlinePic>{

}
