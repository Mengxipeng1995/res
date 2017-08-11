package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ChapterCategoryDAO;
import com.cmp.res.dao.MagazineCategoryDAO;
import com.cmp.res.entity.BookCategory;
import com.cmp.res.entity.Chapter;
import com.cmp.res.entity.ChapterCategory;
import com.cmp.res.entity.MagazineCategory;

@Service
public class ChapterCategoryService implements BaseService<ChapterCategory>{
	
	
	@Autowired
	private ChapterCategoryDAO chapterCategoryDAO;

	public ChapterCategory getRoot(){
		return chapterCategoryDAO.findByParentidIsNull();
	}
	
	@Override
	public void save(ChapterCategory t) {
		// TODO Auto-generated method stub
		chapterCategoryDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		chapterCategoryDAO.delete(id);
		
	}

	@Override
	public ChapterCategory findById(Long id) {
		// TODO Auto-generated method stub
		return chapterCategoryDAO.findOne(id);
	}

	@Override
	public void delete(ChapterCategory t) {
		// TODO Auto-generated method stub
		chapterCategoryDAO.delete(t);
		
	}
	
	public List<ChapterCategory> findByParentid(Long pid){
		return chapterCategoryDAO.findByParentid(pid);
	}
	
	public ChapterCategory findByParentidAndName(Long pid,String name){
		List<ChapterCategory> list=chapterCategoryDAO.findByParentidAndName(pid, name);
		
		return (list!=null&&list.size()>0)?list.get(0):null;
	}
	
	
	
	

}
