package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ProductCategoryDAO;
import com.cmp.res.entity.ProductCategory;

@Service
public class ProductCategoryService implements BaseService<ProductCategory>{
	
	@Autowired
	private ProductCategoryDAO productCategoryDAO;
	
	public ProductCategory findByCatidAndProductId(long catid,long productId){
		return productCategoryDAO.findByCatidAndProductId(catid, productId);
	}

	@Override
	public void save(ProductCategory t) {
		// TODO Auto-generated method stub
		productCategoryDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		productCategoryDAO.delete(id);
		
	}

	@Override
	public ProductCategory findById(Long id) {
		// TODO Auto-generated method stub
		return productCategoryDAO.findOne(id);
	}

	@Override
	public void delete(ProductCategory t) {
		// TODO Auto-generated method stub
		productCategoryDAO.delete(t);
	}
	
	

}
