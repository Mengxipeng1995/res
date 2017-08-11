package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Subject;
import com.cmp.res.entity.SubjectCategory;

public interface SubjectCategoryDAO   extends PagingAndSortingRepository<SubjectCategory, Long>,JpaSpecificationExecutor<SubjectCategory>{

	public List<SubjectCategory> findByParentid(Long pid);
	
	public List<SubjectCategory> findByParentidAndName(Long pid,String name);
	
	public List<SubjectCategory> findByIdIn(Long[] ids);
}
