package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.MagazineDAO;
import com.cmp.res.entity.Book;
import com.cmp.res.entity.Magazine;
import com.cmp.res.util.CommonUtil;

@Service
public class MagazineService implements BaseService<Magazine>{
	@Autowired
	private MagazineDAO magazineDAO;

	public Long getCount(){
		return magazineDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	@Override
	public void save(Magazine t) {
		// TODO Auto-generated method stub
		magazineDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		magazineDAO.delete(id);
	}

	@Override
	public Magazine findById(Long id) {
		// TODO Auto-generated method stub
		return magazineDAO.findOne(id);
	}

	@Override
	public void delete(Magazine t) {
		// TODO Auto-generated method stub
		magazineDAO.delete(t);
		
	}

}
