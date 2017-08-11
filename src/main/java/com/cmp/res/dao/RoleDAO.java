package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Role;


public interface RoleDAO extends PagingAndSortingRepository<Role, Long>,JpaSpecificationExecutor<Role>{
	
	public Role findByRoleName(String roleName);

}
