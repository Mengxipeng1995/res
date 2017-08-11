package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.BookCategory;
import com.cmp.res.entity.Magazine;
import com.cmp.res.entity.MagazineCategory;

public interface MagazineCategoryDAO  extends PagingAndSortingRepository<MagazineCategory, Long>,JpaSpecificationExecutor<MagazineCategory>{

	public List<MagazineCategory> findByParentidAndName(Long pid,String name);
	
	public List<MagazineCategory> findByParentid(Long pid);
	
	public MagazineCategory findByParentidIsNull();
}
