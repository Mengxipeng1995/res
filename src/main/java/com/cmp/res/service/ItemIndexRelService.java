package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.IndexentryDAO;
import com.cmp.res.dao.ItemDAO;
import com.cmp.res.dao.ItemIndexRelDAO;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemIndexRel;
import com.cmp.res.util.CommonUtil;


@Service
@Transactional(readOnly = true)
public class ItemIndexRelService {
	
	@Autowired
	private ItemIndexRelDAO itemIndexRelDAO;
	
	public Page<ItemIndexRel> outline(String searchWord,int pn,int ps){
		return itemIndexRelDAO.findAll(getSpec(searchWord),CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	
	
	
	@Transactional(readOnly = false)
	public void saveItemIndexRel(ItemIndexRel itemIndex){
		
		itemIndexRelDAO.save(itemIndex);
		
	}
	@Transactional(readOnly = false)
	public void delete(Long id){
		itemIndexRelDAO.delete(id);
	}
	
	public ItemIndexRel findById(Long id){
		return itemIndexRelDAO.findOne(id);
	}
	
	public Specification<ItemIndexRel> getSpec(final String searchWord) {
		return new Specification<ItemIndexRel>() {
			public Predicate toPredicate(Root<ItemIndexRel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(searchWord)){
					Path<String> indexNamePath = root.get("indexName");
					predicates.add(cb.equal(indexNamePath, searchWord));
				}
				
				
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
	
	

}
