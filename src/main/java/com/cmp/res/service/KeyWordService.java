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

import com.cmp.res.dao.KeyWordDAO;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.KeyWord;
import com.cmp.res.util.CommonUtil;

@Service
public class KeyWordService implements BaseService<KeyWord>{
	@Autowired
	private KeyWordDAO keyWordDAO;
	
	public Page<KeyWord> search(int pn,int ps,String word,String creater){
		return keyWordDAO.findAll(getSpec(word,creater),CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	
	public Specification<KeyWord> getSpec(final String word,String creater) {
		return new Specification<KeyWord>() {
			public Predicate toPredicate(Root<KeyWord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(word)){
					Path<String> keywordsPath = root.get("keywords");
					predicates.add(cb.equal(keywordsPath, word));
				}
				
				if(StringUtils.isNotBlank(creater)){
					Path<String> createrPath = root.get("createUser");
					predicates.add(cb.equal(createrPath, creater));
				}
				
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

	@Override
	public void save(KeyWord t) {
		// TODO Auto-generated method stub
		
		keyWordDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		keyWordDAO.delete(id);
		
	}

	@Override
	public KeyWord findById(Long id) {
		// TODO Auto-generated method stub
		return keyWordDAO.findOne(id);
	}

	@Override
	public void delete(KeyWord t) {
		// TODO Auto-generated method stub
		keyWordDAO.delete(t);
		
	}

}
