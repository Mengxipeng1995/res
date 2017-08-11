package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cmp_keywordtype")
public class KeyWordType extends ID{
	
	private String type;
	
	private String info;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	
	
	

}
