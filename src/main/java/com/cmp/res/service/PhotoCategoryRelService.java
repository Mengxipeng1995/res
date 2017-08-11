package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.CategoryDAO;
import com.cmp.res.dao.IndexentryDAO;
import com.cmp.res.dao.ItemDAO;
import com.cmp.res.dao.ItemIndexRelDAO;
import com.cmp.res.dao.PhotoCategoryRelDAO;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemIndexRel;
import com.cmp.res.entity.PhotoCategoryRel;
import com.cmp.res.entity.Product;
import com.cmp.res.util.CommonUtil;


@Service
@Transactional(readOnly = true)
public class PhotoCategoryRelService {
	
	@Autowired
	private PhotoCategoryRelDAO photoCategoryRelDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	
	
	@Transactional(readOnly = false)
	public void savePhotoCategoryRel(PhotoCategoryRel photoCategoryRel){
		
		photoCategoryRelDAO.save(photoCategoryRel);
		
	}
	
	public Page<PhotoCategoryRel> outline(Long catid,int pn,int ps){
		return photoCategoryRelDAO.findAll(getSpec(catid), CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	public Specification<PhotoCategoryRel> getSpec(final Long catid) {
		return new Specification<PhotoCategoryRel>() {
			public Predicate toPredicate(Root<PhotoCategoryRel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Path<Long> catidPath = root.get("catid");
				if(catid!=null){
					predicates.add(cb.equal(catidPath, catid));
				}else{
					predicates.add(cb.equal(catidPath, 2));//知识体系节点
				}
				
				Path<String> rolePath=root.get("role");
				predicates.add(cb.or(cb.equal(rolePath, "线条图"),cb.equal(rolePath, "表格"),cb.equal(rolePath, "图表")));
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
	
	

}
