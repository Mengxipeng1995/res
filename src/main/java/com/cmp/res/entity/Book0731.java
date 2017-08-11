package com.cmp.res.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="cmp_book")
public class Book0731 extends IdEntity {
	
	
	 private String title;
	 
	 private String keywords;
	 
	 private String pubDateStr;
	 
	 private int pageNums;
	 
	 private String publisherName;
	 
	 private String publisherAddress;
	 
	 
	 private List<Item> items=Lists.newArrayList();
	 
	 private List<Indexentry> indexs=Lists.newArrayList();
	 
	 private Map<String, Bibliography> biblios=Maps.newHashMap();
	 
	 private Map<String,Object> standards=Maps.newHashMap();

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

	@Transient
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	@Transient
	public Map<String, Bibliography> getBiblios() {
		return biblios;
	}

	public void setBiblios(Map<String, Bibliography> biblios) {
		this.biblios = biblios;
	}
	@Transient
	public List<Indexentry> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Indexentry> indexs) {
		this.indexs = indexs;
	}
	@Transient
	public Map<String, Object> getStandards() {
		return standards;
	}

	public void setStandards(Map<String, Object> standards) {
		this.standards = standards;
	}
	
	
	
	
	

}
