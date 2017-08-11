package com.cmp.res.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;



@Entity
@Table(name="cmp_photo_categroy_rel")
public class PhotoCategoryRel  extends IdEntity{
	
    private Long catid;
    
    private Long photoid;
    
	 private String title;
	 
	 private String bookTitle;

	 private String ceaterUser;
	 
	 private String createrNickName;
	 
	 private Date createTime;
	 
	 private String role;
	 
	 private String desc;
	 
	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}

	public Long getPhotoid() {
		return photoid;
	}

	public void setPhotoid(Long photoid) {
		this.photoid = photoid;
	}
	@Type(type="text")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getCeaterUser() {
		return ceaterUser;
	}

	public void setCeaterUser(String ceaterUser) {
		this.ceaterUser = ceaterUser;
	}

	public String getCreaterNickName() {
		return createrNickName;
	}

	public void setCreaterNickName(String createrNickName) {
		this.createrNickName = createrNickName;
	}

	

	@Column(name="info")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	
	
	
	
	

}
