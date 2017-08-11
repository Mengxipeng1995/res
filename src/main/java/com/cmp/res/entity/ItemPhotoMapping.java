package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cmp_item_photo_mapping")
public class ItemPhotoMapping extends ID{
	
	private Long phtotoid;
	
	
	private Long itemid;


	public Long getPhtotoid() {
		return phtotoid;
	}


	public void setPhtotoid(Long phtotoid) {
		this.phtotoid = phtotoid;
	}


	public Long getItemid() {
		return itemid;
	}


	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	
	
	
	
	

}
