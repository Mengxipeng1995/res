package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Chapter;

public interface ChapterDAO   extends PagingAndSortingRepository<Chapter, Long>,JpaSpecificationExecutor<Chapter>{

}
