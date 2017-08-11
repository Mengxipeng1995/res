package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.entity.SubjectItem;

public interface SubjectItemDAO  extends PagingAndSortingRepository<SubjectItem, Long>,JpaSpecificationExecutor<SubjectItem>{
	
	public List<SubjectItem> findBySubjectidAndItemid(Long subjectid,Long itemid);
	
	@Modifying
	@Transactional
	@Query("delete from SubjectItem pi where pi.subjectid = ?1")
	public void deleteBySubjectid(Long id);
	
	public List<SubjectItem> findByItemidAndType(long itemid,int type);
	

}
