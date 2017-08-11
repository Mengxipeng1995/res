package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ApplyDAO;
import com.cmp.res.entity.Apply;
import com.cmp.res.entity.Item;
import com.cmp.res.util.CommonUtil;

@Service
public class ApplyService implements BaseService<Apply> {
	
	@Autowired
	private ApplyDAO applyDAO;
	
	public Page<Apply> outline(String userName,Integer status,int pn,int ps){
		return applyDAO.findAll(getSpec(userName,status),CommonUtil.buildPageRequest(pn, ps, null, null, null));
		
	}
	
	public List<Apply> list(String userName,Integer status){
		
		return applyDAO.findAll(getSpec(userName,status));
	}
	
	
	

	@Override
	public void save(Apply t) {
		// TODO Auto-generated method stub
		applyDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		applyDAO.delete(id);
	}

	@Override
	public Apply findById(Long id) {
		// TODO Auto-generated method stub
		return applyDAO.findOne(id);
	}

	@Override
	public void delete(Apply t) {
		// TODO Auto-generated method stub
		applyDAO.delete(t);
	}
	
	public Specification<Apply> getSpec(String userName,Integer status) {
		return new Specification<Apply>() {
			public Predicate toPredicate(Root<Apply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(userName)){
					Path<String> applicantUserNamePath = root.get("applicantUserName");
					predicates.add(cb.equal(applicantUserNamePath, userName));
				}
				if(status!=null){
					Path<Integer> statusPath = root.get("status");
					predicates.add(cb.equal(statusPath, status));
				}
				
//				Path<Integer> statusPath = root.get("status");
//				predicates.add(cb.notEqual(statusPath, 0));
//				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
