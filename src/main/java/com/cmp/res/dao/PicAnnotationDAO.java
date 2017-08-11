package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Apply;
import com.cmp.res.entity.PicAnnotation;

public interface PicAnnotationDAO   extends PagingAndSortingRepository<PicAnnotation, Long>,JpaSpecificationExecutor<PicAnnotation>{

}
