package com.cmp.res.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * @author trs
 *
 */
@Entity
@Table(name="cmp_subject")
public class Subject extends ID{
	/**
	 * 专题名称
	 */
	private String title;
	
	private Date createDate;
	
	
	private String createrNickName;
	
	private String createrName;
	
	private String categoryids;
	
	private String categorynames;
	
	private String desc;
	/**
	 * 0:编辑状态
	 * 1：发布状态
	 */
	private int status;
	
	/**
	 * 专题等级
	 */
	private Integer level;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public String getCreaterNickName() {
		return createrNickName;
	}

	public void setCreaterNickName(String createrNickName) {
		this.createrNickName = createrNickName;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	@Column(name="description")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCategoryids() {
		return categoryids;
	}

	public void setCategoryids(String categoryids) {
		this.categoryids = categoryids;
	}

	public String getCategorynames() {
		return categorynames;
	}

	public void setCategorynames(String categorynames) {
		this.categorynames = categorynames;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	

}
