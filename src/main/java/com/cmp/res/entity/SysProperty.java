package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cmp_property")
public class SysProperty extends ID{
	
	/**
	 * 创建者
	 */
	private String creater;
	/**
	 * 参数名
	 */
	private String proNmae;
	
	/**
	 * 参数key
	 */
	private String proKey;
	
	/**
	 * 参数值
	 */
	private String proValue;
	/**
	 * 描述
	 */
	private String desc;
	
	/**
	 * 创建时间
	 */
	private Date createDate;

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getProNmae() {
		return proNmae;
	}

	public void setProNmae(String proNmae) {
		this.proNmae = proNmae;
	}

	public String getProValue() {
		return proValue;
	}

	public void setProValue(String proValue) {
		this.proValue = proValue;
	}

	@Column(name="info")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(unique=true)
	public String getProKey() {
		return proKey;
	}

	public void setProKey(String proKey) {
		this.proKey = proKey;
	}
	
	

}
