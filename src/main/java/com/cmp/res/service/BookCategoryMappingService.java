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

import com.cmp.res.dao.BookCategoryMappingDAO;
import com.cmp.res.entity.BookCategoryMapping;
import com.cmp.res.entity.Item;
import com.cmp.res.util.CommonUtil;

@Service
public class BookCategoryMappingService implements BaseService<BookCategoryMapping>{
	
	@Autowired
	private BookCategoryMappingDAO bookCategoryMappingDAO;
	
	
	public Page<BookCategoryMapping> outline(Long cid,int pn,int ps,String kw){
		return bookCategoryMappingDAO.findAll(getSpec(cid,kw),CommonUtil.buildPageRequest(pn, ps, null,null,null));
	}





	
	public List<BookCategoryMapping> findByBookid(Long bookid){
		return bookCategoryMappingDAO.findByBookid(bookid);
	}
	
	public void deleteByBookid(Long bookid){
		bookCategoryMappingDAO.deleteByBookid(bookid);
	}

	@Override
	public void save(BookCategoryMapping t) {
		// TODO Auto-generated method stub
		
		bookCategoryMappingDAO.save(t);
	}
	
	
	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BookCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(BookCategoryMapping t) {
		// TODO Auto-generated method stub
		
	}
	
	public Specification<BookCategoryMapping> getSpec(final Long catid,final String kw) {
		return new Specification<BookCategoryMapping>() {
			public Predicate toPredicate(Root<BookCategoryMapping> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();

				Path<Long> catidPath = root.get("catid");
				predicates.add(cb.equal(catidPath, catid));

				if(StringUtils.isNotBlank(kw)){
					Path<String> titlePath = root.get("title");
					predicates.add(cb.like(titlePath, "%"+kw+"%"));
				}


				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

	
}
