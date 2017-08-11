package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cmp_item_book_mapping")
public class StandardBookMapping extends ID{
	
	private Long bookId;
	
	private Long StandardId;
	
	private String title;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getStandardId() {
		return StandardId;
	}

	public void setStandardId(Long standardId) {
		StandardId = standardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
