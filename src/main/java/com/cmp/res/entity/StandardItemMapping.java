package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 标准条目关联表
 * @author trs
 *
 */
@Entity
@Table(name="cmp_standard_itme_mapping")
public class StandardItemMapping extends ID{
	
	private Long standardId;
	
	private String tittle;
	
	private Long itemId;

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	
	
	

}
