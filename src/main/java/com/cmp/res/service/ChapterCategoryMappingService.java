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

import com.cmp.res.dao.ChapterCategoryMappingDAO;
import com.cmp.res.entity.ChapterCategoryMapping;
import com.cmp.res.util.CommonUtil;

@Service
public class ChapterCategoryMappingService implements BaseService<ChapterCategoryMapping>{

	@Autowired
	private ChapterCategoryMappingDAO chapterCategoryMappingDAO;
	
	
	public Page<ChapterCategoryMapping> outline(Long cid,int pn,int ps){
		return chapterCategoryMappingDAO.findAll(getSpec(cid),CommonUtil.buildPageRequest(pn, ps, null,null,null));
	}
	
	public List<ChapterCategoryMapping> findByChapterid(Long chapterid){
		return chapterCategoryMappingDAO.findByChapterid(chapterid);
	}
	
	public void deleteByChapterid(Long magid){
		chapterCategoryMappingDAO.deleteByChapterid(magid);
	}
	
	
	@Override
	public void save(ChapterCategoryMapping t) {
		// TODO Auto-generated method stub
		chapterCategoryMappingDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		chapterCategoryMappingDAO.delete(id);
	}

	@Override
	public ChapterCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return chapterCategoryMappingDAO.findOne(id);
	}

	@Override
	public void delete(ChapterCategoryMapping t) {
		// TODO Auto-generated method stub
		chapterCategoryMappingDAO.delete(t);
	}
	
	public Specification<ChapterCategoryMapping> getSpec(final Long catid) {
		return new Specification<ChapterCategoryMapping>() {
			public Predicate toPredicate(Root<ChapterCategoryMapping> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Long> catidPath = root.get("catid");
				predicates.add(cb.equal(catidPath, catid));
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
