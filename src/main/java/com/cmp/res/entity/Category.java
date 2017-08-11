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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@Table(name="cmp_category")
public class Category extends IdEntity{
	
	private String name;
	
	private int type;
	
	private Long parentid;
	
	private Category parent;
	
	private List<Category> sons;
	
	private Date createTime;
	
	private Date modifyTime;
	/**
	 * 资源类型
	 * 
	 * 默认值为：0
	 * 0  公共知识体系
	 * 1    图片库知识体系
	 * 2    视频库知识体系
	 */
	private Integer resourcesType;
	
	
	
	public Integer getResourcesType() {
		return resourcesType;
	}

	public void setResourcesType(Integer resourcesType) {
		this.resourcesType = resourcesType;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	

	
//	@ManyToOne(fetch=FetchType.LAZY)
//	 @JoinColumn(name = "parentid",insertable=false,updatable=false)
//	 @NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "parentid",insertable=false,updatable=false)
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	@OneToMany(targetEntity=Category.class,cascade={CascadeType.ALL},mappedBy = "parent")
	@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	public List<Category> getSons() {
		return sons;
	}

	public void setSons(List<Category> sons) {
		this.sons = sons;
	}
	
	
	
	
	

}
