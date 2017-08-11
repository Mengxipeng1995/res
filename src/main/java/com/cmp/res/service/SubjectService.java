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

import com.cmp.res.dao.SubjectDAO;
import com.cmp.res.entity.Subject;
import com.cmp.res.util.CommonUtil;
@Service
public class SubjectService implements BaseService<Subject>{
	
	@Autowired
	private SubjectDAO subjectDAO;
	
	public Page<Subject> outline(Long cid,Integer pn,Integer ps){
		return subjectDAO.findAll(getSpec(cid), CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	
	public Long getCount(){
		return subjectDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	

	@Override
	public void save(Subject subject) {
		// TODO Auto-generated method stub
		subjectDAO.save(subject);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
		subjectDAO.delete(id);
	}

	@Override
	public Subject findById(Long id) {
		// TODO Auto-generated method stub
		return subjectDAO.findOne(id);
	}

	@Override
	public void delete(Subject subject) {
		// TODO Auto-generated method stub
		subjectDAO.delete(subject);
		
		
	}
	
	public Specification<Subject> getSpec(final Long cid) {
		return new Specification<Subject>() {
			public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(cid!=null){
					Path<String> categoryidsPath = root.get("categoryids");
					predicates.add(cb.like(categoryidsPath, "%;"+cid+";%"));
				}
				
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
