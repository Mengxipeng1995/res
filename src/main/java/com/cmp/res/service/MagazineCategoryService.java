package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.MagazineCategoryDAO;
import com.cmp.res.entity.BookCategory;
import com.cmp.res.entity.MagazineCategory;

@Service
public class MagazineCategoryService implements BaseService<MagazineCategory>{
	
	
	@Autowired
	private MagazineCategoryDAO magazineCategoryDAO;

	public MagazineCategory getRoot(){
		return magazineCategoryDAO.findByParentidIsNull();
	}
	
	@Override
	public void save(MagazineCategory t) {
		// TODO Auto-generated method stub
		magazineCategoryDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		magazineCategoryDAO.delete(id);
		
	}

	@Override
	public MagazineCategory findById(Long id) {
		// TODO Auto-generated method stub
		return magazineCategoryDAO.findOne(id);
	}

	@Override
	public void delete(MagazineCategory t) {
		// TODO Auto-generated method stub
		magazineCategoryDAO.delete(t);
		
	}
	
	public List<MagazineCategory> findByParentid(Long pid){
		return magazineCategoryDAO.findByParentid(pid);
	}
	
	public MagazineCategory findByParentidAndName(Long pid,String name){
		List<MagazineCategory> list=magazineCategoryDAO.findByParentidAndName(pid, name);
		
		return (list!=null&&list.size()>0)?list.get(0):null;
	}
	
	
	
	

}
