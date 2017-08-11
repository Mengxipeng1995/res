package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.SubjectCategoryDAO;
import com.cmp.res.entity.Subject;
import com.cmp.res.entity.SubjectCategory;
@Service
public class SubjectCategoryService implements BaseService<SubjectCategory>{
	@Autowired
	private SubjectCategoryDAO subjectCategoryDAO;
	
	public List<SubjectCategory> findByParentid(Long pid){
		return subjectCategoryDAO.findByParentid(pid);
	}
	
	public SubjectCategory findByParentidAndName(Long pid,String name){
		List<SubjectCategory> subjectCategorys=subjectCategoryDAO.findByParentidAndName(pid, name);
		return subjectCategorys!=null&&subjectCategorys.size()==1?subjectCategorys.get(0):null;
	}
	
	public List<SubjectCategory> findByIdIn(Long[] ids){
		return subjectCategoryDAO.findByIdIn(ids);
	}

	@Override
	public void save(SubjectCategory subjectCategory) {
		// TODO Auto-generated method stub
		subjectCategoryDAO.save(subjectCategory);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SubjectCategory findById(Long id) {
		// TODO Auto-generated method stub
		return subjectCategoryDAO.findOne(id);
	}

	@Override
	public void delete(SubjectCategory subjectCategory) {
		// TODO Auto-generated method stub
		subjectCategoryDAO.delete(subjectCategory);
		
	}

}
