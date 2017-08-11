package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.ChapterCategory;
import com.cmp.res.entity.Group;

public interface GroupDAO extends PagingAndSortingRepository<Group, Long>,JpaSpecificationExecutor<Group>{
	public List<Group> findByParentid(Long id);
	
	public List<Group> findByParentidAndName(Long id,String name);
	
	public Group findByParentidIsNull();
}
