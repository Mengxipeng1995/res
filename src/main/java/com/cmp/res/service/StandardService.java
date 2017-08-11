package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.StandardDAO;
import com.cmp.res.entity.Standard;
import com.cmp.res.util.CommonUtil;

@Service
public class StandardService implements BaseService<Standard>{
	@Autowired
	private StandardDAO standardDAO;
	
	public Page<Standard> outline(int page,int limit){
		return standardDAO.findAll(CommonUtil.buildPageRequest(page, limit, null, null, null));
	}
	
	public Standard findByTitle(String title){
		List<Standard> standards=standardDAO.findByTitle(title);
		if(standards!=null&&standards.size()>0){
			return standards.get(0);
		}
		return null;
	}

	@Override
	public void save(Standard t) {
		// TODO Auto-generated method stub
		standardDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		standardDAO.delete(id);
		
	}

	@Override
	public Standard findById(Long id) {
		// TODO Auto-generated method stub
		return standardDAO.findOne(id);
	}

	@Override
	public void delete(Standard t) {
		// TODO Auto-generated method stub
		standardDAO.delete(t);
		
	}

}
