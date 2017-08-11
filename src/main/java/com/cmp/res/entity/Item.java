package com.cmp.res.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Entity
@Table(name="cmp_item")
public class Item extends IdEntity {
	
	
	 private String title;
	 
	 private String etitle;
	 
	 private String bookTitle;
	 
	 private Long bookid;
	 
	 private String linkend;
	 
	 private String tittleLink;
	 
	 
	 private String userName;
	 
	 private String userNickName;
	 /**
	  * 存储标题中引用的文献标签
	  */
	 private String titleBibliography;
	 
	 /**
	  * 发布展示标记   发布端用到的一个标记 ,对资源库没用
	  */
	 private Integer pubShowFlag;
	 
	 private String source;
	 
	 /**
	  * 知识体系中的分类id
	  */
	 private Long catid;
	 
	 private String keywords;
	 
	 private String content;
	 
	 private Long parentid;
	 /**
	  * 参考文献的id串
	  */
	 private String biblioIds;
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
	 
	 private Date modifyDate;
	 
	 
	 private List<Item> items=Lists.newArrayList();
	 
	 private List<Photo> photos=Lists.newArrayList();
	 
	 private List<Photo> titlePhotos=Lists.newArrayList();
	 
	 private List<Bibliography> biblios=Lists.newArrayList();
	 
	 private List<Indexentry> indexs=Lists.newArrayList();
	 
	 private Map<String,Object> stands=Maps.newHashMap();
	 
	 private Category cat;

	@Type(type="text")
	public String getTitle() {
		return title;
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

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	@Type(type="text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
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

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	 
	@Transient
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}
	@Transient
	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}
	@Transient
	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
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

	@Type(type="text")
	public String getBiblioIds() {
		return biblioIds;
	}

	public void setBiblioIds(String biblioIds) {
		this.biblioIds = biblioIds;
	}
	@Transient
	public List<Bibliography> getBiblios() {
		return biblios;
	}

	public void setBiblios(List<Bibliography> biblios) {
		this.biblios = biblios;
	}
	 public String getVersionCode() {
			return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	@Transient
	public List<Indexentry> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Indexentry> indexs) {
		this.indexs = indexs;
	}
	
	
	

	@Transient
	public Integer getVersionCount() {
		return versionCount;
	}
	@Transient
	public Map<String, Object> getStands() {
		return stands;
	}

	public void setStands(Map<String, Object> stands) {
		this.stands = stands;
	}

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	public String getLinkend() {
		return linkend;
	}

	public void setLinkend(String linkend) {
		this.linkend = linkend;
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

	public Integer getPubShowFlag() {
		return pubShowFlag;
	}

	public void setPubShowFlag(Integer pubShowFlag) {
		this.pubShowFlag = pubShowFlag;
	}

	public String getTittleLink() {
		return tittleLink;
	}

	public void setTittleLink(String tittleLink) {
		this.tittleLink = tittleLink;
	}

	@Type(type="text")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Type(type="text")
	public String getTitleBibliography() {
		return titleBibliography;
	}

	public void setTitleBibliography(String titleBibliography) {
		this.titleBibliography = titleBibliography;
	}

	@Transient
	public List<Photo> getTitlePhotos() {
		return titlePhotos;
	}

	public void setTitlePhotos(List<Photo> titlePhotos) {
		this.titlePhotos = titlePhotos;
	}
	
	
	
	
	/**
	 * 解析时临时使用
	 */
	private String catName;

	@Transient
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	
	

}
