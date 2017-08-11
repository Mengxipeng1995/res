package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.UserGroupMappingDAO;
import com.cmp.res.entity.UserGroupMapping;
import com.cmp.res.util.CommonUtil;

@Service
public class UserGroupMappingService implements BaseService<UserGroupMapping>{
	@Autowired
	private UserGroupMappingDAO userGroupMappingDAO;
	
	public Page<UserGroupMapping> outline(Long groupid,int pn,int ps){
		return userGroupMappingDAO.findAll(getSpec(groupid),CommonUtil.buildPageRequest(pn, ps, null,null,null));
	}
	
	public List<UserGroupMapping> findByUserid(Long userid){
		return userGroupMappingDAO.findByUserId(userid);
	}
	
	public void deleteByUserid(Long userid){
		userGroupMappingDAO.deleteByUserid(userid);
	}
	
	

	@Override
	public void save(UserGroupMapping t) {
		// TODO Auto-generated method stub
		userGroupMappingDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		userGroupMappingDAO.delete(id);
		
	}

	@Override
	public UserGroupMapping findById(Long id) {
		// TODO Auto-generated method stub
		return userGroupMappingDAO.findOne(id);
	}

	@Override
	public void delete(UserGroupMapping t) {
		// TODO Auto-generated method stub
		
		userGroupMappingDAO.delete(t);
		
	}
	public Specification<UserGroupMapping> getSpec(final Long groupid) {
		return new Specification<UserGroupMapping>() {
			public Predicate toPredicate(Root<UserGroupMapping> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Long> groupidPath = root.get("groupId");
				predicates.add(cb.equal(groupidPath, groupid));
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
	 

}
