package com.cmp.res.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.Item;



public interface  ItemDAO extends PagingAndSortingRepository<Item, Long>,JpaSpecificationExecutor<Item>{
	
	public List<Item> findByCatid(Long id);
	
	public List<Item> findByIdIn(Long[] ids);
	
	public List<Item> findByResCode(String resCode);
//	@Query("select t1.* from (select * from Item where deleteFlag!=1 and catid=?1 order by createDate desc) t1 group by t1.resCode limit ?2,?3")
	@Query("from Item as a where createDate =(select max(createDate) from Item where a.resCode=resCode and deleteFlag!=1 and catid=?1)")
	public Page<Item> outline(Long catid,Pageable pageable);
	@Query("from Item as a where bookid=?1 and linkend like 'chapter%'")
	public List<Item> findChapter(Long bookid);
	@Query("from Item as a where bookid=?1 and linkend like ?2")
	public List<Item> findSect(Long bookid,String linkend);
	
	
	

}
