package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.KeyWordTypeDAO;
import com.cmp.res.entity.KeyWordType;

@Service
public class KeyWordTypeService implements BaseService<KeyWordType>{
	@Autowired
	private KeyWordTypeDAO keyWordTypeDAO;

	@Override
	public void save(KeyWordType t) {
		// TODO Auto-generated method stub
		keyWordTypeDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		keyWordTypeDAO.delete(id);
	}

	@Override
	public KeyWordType findById(Long id) {
		// TODO Auto-generated method stub
		return keyWordTypeDAO.findOne(id);
	}

	@Override
	public void delete(KeyWordType t) {
		// TODO Auto-generated method stub
		keyWordTypeDAO.delete(t);
	}

}
