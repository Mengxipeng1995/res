package com.cmp.res.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="cmp_resource")
public class Resource extends ID{
	
	/**
	 * 资源唯一标识串
	 */
	private String uniqueIdentifierString;
	/**
	 * 操作名称    如：发布、删除、下载
	 */
	private String name;
	/**
	 * 资源类型     
	 * 1、产品
	 * 2、专题
	 */
	private Integer type;
	/**
	 * 资源类型名称
	 * 产品
	 * 专题
	 */
	private String typeName;
	
	
	
	

	public String getUniqueIdentifierString() {
		return uniqueIdentifierString;
	}

	public void setUniqueIdentifierString(String uniqueIdentifierString) {
		this.uniqueIdentifierString = uniqueIdentifierString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	

}
