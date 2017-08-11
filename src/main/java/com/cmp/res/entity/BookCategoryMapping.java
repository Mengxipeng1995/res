package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cmp_book_categroy_mapping")
public class BookCategoryMapping extends ID{
	
	private String title;
	 
	 private String keywords;
	 
	 private String pubDateStr;
	 
	 private int pageNums;
	 
	 private String publisherName;
	 
	 private String publisherAddress;
	 
	 
	 private Long bookid;
	 
	 private Long catid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPubDateStr() {
		return pubDateStr;
	}

	public void setPubDateStr(String pubDateStr) {
		this.pubDateStr = pubDateStr;
	}

	public int getPageNums() {
		return pageNums;
	}

	public void setPageNums(int pageNums) {
		this.pageNums = pageNums;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherAddress() {
		return publisherAddress;
	}

	public void setPublisherAddress(String publisherAddress) {
		this.publisherAddress = publisherAddress;
	}

	public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}
	 
	 
	 

}
