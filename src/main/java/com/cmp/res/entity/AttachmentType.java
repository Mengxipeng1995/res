package com.cmp.res.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 附件类型
 * @author Administrator
 *
 */
@Entity
@Table(name="cmp_attachment_type")
public class AttachmentType extends ID{
	
	@Column(name="type",unique=true,nullable=false)
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
