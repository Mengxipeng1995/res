package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.ChapterDAO;
import com.cmp.res.entity.Chapter;
import com.cmp.res.util.CommonUtil;

@Service
public class ChapterService  implements BaseService<Chapter>{
	@Autowired
	private ChapterDAO chapterDAO;

	public Long getCount(){
		return chapterDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	@Override
	public void save(Chapter t) {
		// TODO Auto-generated method stub
		chapterDAO.save(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		chapterDAO.delete(id);
	}

	@Override
	public Chapter findById(Long id) {
		// TODO Auto-generated method stub
		return chapterDAO.findOne(id);
	}

	@Override
	public void delete(Chapter t) {
		// TODO Auto-generated method stub
		chapterDAO.delete(t);
		
	}

}
