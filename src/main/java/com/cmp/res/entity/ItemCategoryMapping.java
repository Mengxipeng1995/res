package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="cmp_item_rel")
public class ItemCategoryMapping extends IdEntity {
	

	
	
	 private String title;
	 
	 private String etitle;
	 
	 private String userName;
	 
	 private String userNickName;
	 
	 
	 
	 /**
	  * 知识体系中的分类id
	  */
	 private Long catid;
	 
	 private String keywords;
	 
	
	 /**
	  * 不同资源不重复的唯一值
	  */
	 private String resCode;
	 /**
	  * 默认值为：0
	  * 0：原始入库的词条
	  * 1：系统创建的词条
	  * -1:编辑产生的中间版本词条
	  */
	 private Integer originalVersionFlag;

	 /**
	  * 1:删除；
	  * 其他未删
	  */
	 private Integer deleteFlag;
	 
	 private String versionDesc;
	 
	 private String versionCode;
	 
	 private Integer versionCount;
	 

	private Date createDate;

	 private Long itemid;

	@Type(type="text")
	public String getTitle() {
		return title;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEtitle() {
		return etitle;
	}

	public void setEtitle(String etitle) {
		this.etitle = etitle;
	}



	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	

	
	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}
	
	public Integer getOriginalVersionFlag() {
		return originalVersionFlag;
	}

	public void setOriginalVersionFlag(Integer originalVersionFlag) {
		this.originalVersionFlag = originalVersionFlag;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	
	
	 public String getVersionCode() {
			return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	
	

	@Transient
	public Integer getVersionCount() {
		return versionCount;
	}
	

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	
	
	public ItemCategoryMapping(Item item,Long catid){
		this.setItemid(item.getId());
		this.setCatid(catid);
		this.setCreateDate(item.getCreateDate());
		this.setDeleteFlag(item.getDeleteFlag());
		this.setEtitle(item.getEtitle());
		this.setKeywords(item.getKeywords());
		this.setOriginalVersionFlag(item.getOriginalVersionFlag());
		this.setResCode(item.getResCode());
		this.setTitle(item.getTitle());
		this.setUserName(item.getUserName());
		this.setUserNickName(item.getUserNickName());
		this.setVersionCode(item.getVersionCode());
		this.setVersionDesc(item.getVersionDesc());
	}
	
	public ItemCategoryMapping(){
		
	}
 
	

}
