package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.User;

import javax.annotation.Resource;


public interface  UserDAO extends PagingAndSortingRepository<User, Long>,JpaSpecificationExecutor<User>{

	public User findByUserName(String userName);

}
