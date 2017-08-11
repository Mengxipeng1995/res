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

import com.cmp.res.dao.ProductItemDAO;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.util.CommonUtil;

@Service
public class ProductItemService {
	@Autowired
	private ProductItemDAO productItemDAO;
	
	public void save(ProductItem productItem){
		productItemDAO.save(productItem);
	}
	
	public List<ProductItem> findByItemIdAndType(long id,int type){
		return productItemDAO.findByItemidAndType(id, type);
	}
	
	public ProductItem findByProductidAndItemid(Long pid,Long itemid){
		return productItemDAO.findByProductidAndItemid(pid, itemid);
	}
	
	public void deleteByProductId(Long id){
		productItemDAO.deleteByProductId(id);
	}

	
	public Page<ProductItem> list(Long productid,Integer page,Integer limit){
		
		//return productItemDAO.findAll(CommonUtil.buildPageRequest(page, limit, null, null, null));
		return productItemDAO.findAll(getSpec(productid), CommonUtil.buildPageRequest(page, limit, null, null, null));
	}
	
	public Specification<ProductItem> getSpec(final Long productid) {
		return new Specification<ProductItem>() {
			public Predicate toPredicate(Root<ProductItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(productid!=null){
					Path<Long> productidPath = root.get("productid");
					predicates.add(cb.equal(productidPath, productid));
				}
				
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
}
