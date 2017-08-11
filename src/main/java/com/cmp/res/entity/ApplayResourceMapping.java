package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 申请表单与资源关联表
 * @author trs
 *
 */
@Entity
@Table(name="cmp_apply_resource_mapping")
public class ApplayResourceMapping extends ID{

	private Long applayId;
	
	private Long resourceId;
	
	private String resourceName;
	
	private String typeName;
	
	private String uniqueIdentifierString;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Long getApplayId() {
		return applayId;
	}

	public void setApplayId(Long applayId) {
		this.applayId = applayId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getUniqueIdentifierString() {
		return uniqueIdentifierString;
	}

	public void setUniqueIdentifierString(String uniqueIdentifierString) {
		this.uniqueIdentifierString = uniqueIdentifierString;
	}
	
	
}
