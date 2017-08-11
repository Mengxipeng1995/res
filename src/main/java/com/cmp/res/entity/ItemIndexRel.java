package com.cmp.res.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="cmp_Item_Index_Rel")
public class ItemIndexRel  extends IdEntity{
	
	
	
	 private Long itemid;
	 
	 private Long indexid;
	
	 private String title;
	 
	 private String etitle;
	 
	 private String indexName;

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getIndexid() {
		return indexid;
	}

	public void setIndexid(Long indexid) {
		this.indexid = indexid;
	}

	@Type(type="text")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEtitle() {
		return etitle;
	}

	public void setEtitle(String etitle) {
		this.etitle = etitle;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	 
	
	
	
	
	
	
	
	
	
	

}
