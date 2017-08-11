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

import com.cmp.res.dao.ProductDAO;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.Product;
import com.cmp.res.util.CommonUtil;

@Service
public class ProductService {
	
	@Autowired
	private ProductDAO productDAO;
	
	public Long getCount(){
		return productDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	public Page<Product> list(Integer type,Integer page,Integer limit){
		
		return productDAO.findAll(getSpec(type),CommonUtil.buildPageRequest(page, limit, null, null, null));
	}
	
	public void save(Product product){
		productDAO.save(product);
		
	}
	
	public Product findById(Long pid){
		return productDAO.findOne(pid);
	}
	public void delete(Long id){
		productDAO.delete(id);
	}
	
	public Specification<Product> getSpec(final Integer type) {
		return new Specification<Product>() {
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(type!=null){
					Path<Integer> typePath = root.get("type");
					predicates.add(cb.equal(typePath, type));
				}
				
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	

}
