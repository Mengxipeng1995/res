package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.RoleDAO;
import com.cmp.res.entity.Role;
import com.cmp.res.entity.User;

@Service
public class RoleService {
	
	@Autowired
	private RoleDAO roleDAO;
	
	public void saveRole(Role role){
		roleDAO.save(role);
	}
	
	public Role findById(Long id){
		return roleDAO.findOne(id);
	}
	
	public Role findByName(String roleName){
		return roleDAO.findByRoleName(roleName);
	}
	
	public List<Role> getRole(Integer roleLevel){
		
		return roleDAO.findAll(getSpec(roleLevel));
		
	}
	
	public Specification<Role> getSpec(final Integer roleLevel) {
		return new Specification<Role>() {
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Integer> roleLevelPath = root.get("roleLevel");
				predicates.add(cb.greaterThan(roleLevelPath,roleLevel));
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
