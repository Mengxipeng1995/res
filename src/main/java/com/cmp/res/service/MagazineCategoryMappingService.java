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

import com.cmp.res.dao.MagazineCategoryMappingDAO;
import com.cmp.res.entity.MagazineCategoryMapping;
import com.cmp.res.util.CommonUtil;

@Service
public class MagazineCategoryMappingService implements BaseService<MagazineCategoryMapping>{
	
	@Autowired
	private MagazineCategoryMappingDAO magazineCategoryMappingDAO;
	
	
	public Page<MagazineCategoryMapping> outline(Long cid,int pn,int ps){
		return magazineCategoryMappingDAO.findAll(getSpec(cid),CommonUtil.buildPageRequest(pn, ps, null,null,null));
	}
	
	public List<MagazineCategoryMapping> findByMagid(Long magid){
		return magazineCategoryMappingDAO.findByMagid(magid);
	}
	
	public void deleteByMagid(Long magid){
		magazineCategoryMappingDAO.deleteByMagid(magid);
	}
	
	
	@Override
	public void save(MagazineCategoryMapping t) {
		// TODO Auto-generated method stub
		magazineCategoryMappingDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		magazineCategoryMappingDAO.delete(id);
		
	}

	@Override
	public MagazineCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return magazineCategoryMappingDAO.findOne(id);
	}

	@Override
	public void delete(MagazineCategoryMapping t) {
		// TODO Auto-generated method stub
		magazineCategoryMappingDAO.delete(t);
		
	}
	
	public Specification<MagazineCategoryMapping> getSpec(final Long catid) {
		return new Specification<MagazineCategoryMapping>() {
			public Predicate toPredicate(Root<MagazineCategoryMapping> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Long> catidPath = root.get("catid");
				predicates.add(cb.equal(catidPath, catid));
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
