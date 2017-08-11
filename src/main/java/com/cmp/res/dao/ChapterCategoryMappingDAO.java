package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.ChapterCategoryMapping;

public interface ChapterCategoryMappingDAO   extends PagingAndSortingRepository<ChapterCategoryMapping, Long>,JpaSpecificationExecutor<ChapterCategoryMapping>{

	@Modifying
	@Transactional
	@Query("delete from ChapterCategoryMapping ccm where ccm.chapterid = ?1")
	public void deleteByChapterid(Long chapterid);
	
	public List<ChapterCategoryMapping> findByChapterid(Long chapterid);
}
