package com.cmp.res.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cmp.res.entity.NewBook;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NewBookDAO  extends PagingAndSortingRepository<NewBook, Long>,JpaSpecificationExecutor<NewBook>{
	public NewBook findByIsbnAndPrintEdition(String isbn,String printEdition);


	public NewBook findByIsbn(String isbn);



//	@Transactional
//	@Modifying
//	@Query("update NewBook set printEdition = :printEdition where id = :id")
//	public void updateUsableFlag(@Param("id") Long id,@Param("printEdition") String printEdition);
//

	@Transactional(readOnly = false)
	@Modifying()
	@Query(value = "update NewBook book set   book.revision=?1 where book.id=?2")
	public void increaEdition(String printEdition,long id);


	//public void saveAndFlush(String printEdition);


}
