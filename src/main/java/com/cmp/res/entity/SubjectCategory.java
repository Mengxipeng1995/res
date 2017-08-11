package com.cmp.res.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

@Entity
@Table(name="cmp_subject_category")
public class SubjectCategory extends ID{
	/**
	 * 分类名称
	 */
	private String name;
	
	private Date createDate;
	
	private String createrUserName;
	
	private String createrUserNickName;
	
	private SubjectCategory parent;
	
	private Long parentid;
	
	private List<SubjectCategory> sons;
	
	private Boolean leaf;
	
	private boolean flag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreaterUserName() {
		return createrUserName;
	}

	public void setCreaterUserName(String createrUserName) {
		this.createrUserName = createrUserName;
	}

	public String getCreaterUserNickName() {
		return createrUserNickName;
	}

	public void setCreaterUserNickName(String createrUserNickName) {
		this.createrUserNickName = createrUserNickName;
	}
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "parentid",insertable=false,updatable=false)
	public SubjectCategory getParent() {
		return parent;
	}

	public void setParent(SubjectCategory parent) {
		this.parent = parent;
	}

	@OneToMany(targetEntity=SubjectCategory.class,cascade={CascadeType.ALL},mappedBy = "parent")
	@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	public List<SubjectCategory> getSons() {
		return sons;
	}

	public void setSons(List<SubjectCategory> sons) {
		this.sons = sons;
	}
	@Transient
	public Boolean getLeaf(){
		return this.sons!=null&&this.sons.size()>0?false:true;
	}

	@Transient
	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	
	

	
	
	
	
	
	

}
