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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.ItemDAO;
import com.cmp.res.dao.PhotoDAO;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.Photo;
import com.cmp.res.util.CommonUtil;


@Service
@Transactional(readOnly = true)
public class PhotoService implements BaseService<Photo>{
	
	@Autowired
	private PhotoDAO photoDAO;
	
	public Long getCount(){
		return photoDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	@Transactional(readOnly = false)
	public void savePhoto(Photo photo){
		photoDAO.save(photo);
		
	}
	
	public Photo findByBookidLinkimage(Long bookid,String linkimage){
		return photoDAO.findByBookidAndLinkimage(bookid, linkimage);
	}
	
	
	public Photo findById(Long id){
		
		return photoDAO.findOne(id);
		
	}
	
	public List<Photo>  findByIdIn(Long[] ids){
		return photoDAO.findByIdIn(ids);
	}
	
	public Page<Photo> list(Integer pn,Integer ps){
		return photoDAO.findAll(CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}

	@Override
	public void save(Photo t) {
		// TODO Auto-generated method stub
		this.savePhoto(t);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		photoDAO.delete(id);
	}

	@Override
	public void delete(Photo t) {
		// TODO Auto-generated method stub
		photoDAO.delete(t);
	}
	

}
