package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Category;




public interface  CategoryDAO extends PagingAndSortingRepository<Category, Long>,JpaSpecificationExecutor<Category>{
	
	public List<Category> findByParentidAndNameAndResourcesType(Long pid,String name,Integer resourcesType);
	
	public List<Category> findByParentidAndResourcesType(Long pid,Integer resourcesType);

	public List<Category> findByName(String name);
	
	

}
