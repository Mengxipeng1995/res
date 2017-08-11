package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Menu;
import com.cmp.res.entity.User;



public interface  MenuDAO extends PagingAndSortingRepository<Menu, Long>,JpaSpecificationExecutor<User>{
	public List<Menu> findByParentid(Long id);

}
