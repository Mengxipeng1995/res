package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cmp.res.entity.ProductCategoryMapping;
import com.cmp.res.util.CommonUtil;
import com.cmp.res.dao.ProductCategoryMappingDAO;

@Service
public class ProductCategoryMappingService implements BaseService<ProductCategoryMapping>{
	@Autowired
	private ProductCategoryMappingDAO productCategoryMappingDAO;

	public Page<ProductCategoryMapping> outline(Long productid,Long catid,int pn,int ps){
		
		return productCategoryMappingDAO.findByProudctidAndCatid(productid, catid, CommonUtil.buildPageRequest(pn, ps, null, null, null));
		
	}
	
	
	@Override
	public void save(ProductCategoryMapping t) {
		// TODO Auto-generated method stub
		productCategoryMappingDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		productCategoryMappingDAO.delete(id);
		
	}

	@Override
	public ProductCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return productCategoryMappingDAO.findOne(id);
	}

	@Override
	public void delete(ProductCategoryMapping t) {
		// TODO Auto-generated method stub
		productCategoryMappingDAO.delete(t);
		
	}

}
