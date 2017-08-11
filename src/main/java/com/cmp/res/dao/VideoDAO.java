package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Video;

public interface VideoDAO  extends PagingAndSortingRepository<Video, Long>,JpaSpecificationExecutor<Video>{
	
	//public List<Video> findByPicIsNull();
	
	public List<Video> findByIdIn(Long[] ids);

}
