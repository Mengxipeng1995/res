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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cmp_magazine_categroy")
public class MagazineCategory extends ID{

	
private String name;
	
	private Long parentid;
	
	private MagazineCategory parent;
	
	private List<MagazineCategory> sons;
	
	private Date createTime;
	
	private Date modifyTime;
	
	private Boolean flag;
	/**
     * 是否为叶子节点
     */
    private boolean leaf;

    @Transient
	public boolean getLeaf() {
		return this.sons.size()==0?true:false;
	}

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	

	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "parentid",insertable=false,updatable=false)
	public MagazineCategory getParent() {
		return parent;
	}

	public void setParent(MagazineCategory parent) {
		this.parent = parent;
	}
	
	@OneToMany(targetEntity=MagazineCategory.class,cascade={CascadeType.ALL},mappedBy = "parent")
	@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	public List<MagazineCategory> getSons() {
		return sons;
	}

	public void setSons(List<MagazineCategory> sons) {
		this.sons = sons;
	}
	@Transient
	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
	
	



}
