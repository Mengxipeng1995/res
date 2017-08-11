package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Book;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.User;



public interface  PhotoDAO extends PagingAndSortingRepository<Photo, Long>,JpaSpecificationExecutor<Photo>{
	
	public List<Photo> findByIdIn(Long[] ids);
	
	public Photo findByBookidAndLinkimage(Long bookid,String linkimage);
	
	

}
