package com.cmp.res.entity;

import java.util.List;

import com.google.common.collect.Lists;



public class PageEntity {
	
	//number从零开始
	private int number;
	
	private int size;
	
	private int start;
	
	private int limit;
	
	private int totalPages;
	
	private int numberOfElements;
	
	private int totalElements;
	
	private boolean firstPage;
	
	private boolean lastPage;
	
	private List content=Lists.newArrayList();
	
	private String searchExpr;
	//当前分类下的稿件类型，用于前端的稿件展示方式
	private String productType;
	//检索词，用于前台的再次传入进行细览标红
	private String caption;
	
	private String sortColumn;
	
	private String direction;

	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		int i=0;
		if(getTotalElements()%getSize()>0){
			i=getTotalElements()/getSize()+1;
		}else{
			i=getTotalElements()/getSize();
		}
		return i;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return getContent().size();
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public boolean isFirstPage() {
		if(getNumber()==0){
			return true;
		}
		return false;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		if(getTotalPages()==0||getNumber()==getTotalPages()-1){
			return true;
		}
		return false;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public List getContent() {
		return content;
	}

	public void setContent(List content) {
		this.content = content;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getLimit() {
		return limit;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
	public String getSearchExpr() {
		return searchExpr;
	}

	public void setSearchExpr(String searchExpr) {
		this.searchExpr = searchExpr;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public static PageEntity getInstanceByNumber(int pn,int ps){
		PageEntity page=new PageEntity();
		page.setNumber(pn);
		page.setSize(ps);
		page.setStart(pn*ps);
		page.setLimit(pn*ps+ps);
		return page;
	}
	
	public static PageEntity getInstanceByLimit(int start,int limit){
		PageEntity page=new PageEntity();
		page.setNumber(start / limit);
		page.setSize(limit);
		page.setStart(start);
		page.setLimit(limit);
		return page;
		
		
	}
	
	
	

}
