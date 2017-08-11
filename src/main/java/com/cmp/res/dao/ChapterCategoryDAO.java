package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ChapterCategory;

public interface ChapterCategoryDAO   extends PagingAndSortingRepository<ChapterCategory, Long>,JpaSpecificationExecutor<ChapterCategory>{

	public List<ChapterCategory> findByParentidAndName(Long pid,String name);
	
	public List<ChapterCategory> findByParentid(Long pid);
	
	public ChapterCategory findByParentidIsNull();
}
