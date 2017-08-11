package com.cmp.res.entity;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.cmp.res.dao.PicAnnotationDAO;
import com.google.common.collect.Lists;

@Entity
@Table(name="cmp_photo")
public class Photo extends IdEntity  {
	
	private String createrUserName;
	
	private String createrNickName;
	
	private Date createTime;
	
	 private String title;
	 
	 private String bookTitle;
	 
	 private Long bookid;
	 
	 private Long itemid;
	 
	 private int pdfPage;
	 
	 private int bookPage;
	 
	 private String role;
	 
	 private String annotation;
	 
	 private String path;
	 
	 private File imageFile;
	 
	 private String tempCode;
	 
	 private String linkimage;
	 
	 private String titleabbrev;
	 
	 private String desc;

	 @Type(type="text")
	 public String getTitleabbrev() {
		return titleabbrev;
	}

	public void setTitleabbrev(String titleabbrev) {
		this.titleabbrev = titleabbrev;
	}

	
	List<PicAnnotation> picAnnotation=Lists.newArrayList();
	/**
	 * 临时保存图片标题中的图片
	 */
	List<Photo> titlePhoto=Lists.newArrayList();
	@Transient
	public List<Photo> getTitlePhoto() {
		return titlePhoto;
	}

	public void setTitlePhoto(List<Photo> titlePhoto) {
		this.titlePhoto = titlePhoto;
	}


	/**
	  * 1：后期添加的图片；
	  * 其他为入库图片
	  */
	 private Integer type;
	 /**
	  * 删除标志
	  * 1：删除
	  * 其他正常
	  */
	 private Integer deleteFlag;
	 
	 
	 
	 public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

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

	public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public int getPdfPage() {
		return pdfPage;
	}

	public void setPdfPage(int pdfPage) {
		this.pdfPage = pdfPage;
	}

	public int getBookPage() {
		return bookPage;
	}

	public void setBookPage(int bookPage) {
		this.bookPage = bookPage;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Transient
	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	@Transient
	public String getTempCode() {
		return tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLinkimage() {
		return linkimage;
	}

	public void setLinkimage(String linkimage) {
		this.linkimage = linkimage;
	}

	public String getCreaterUserName() {
		return createrUserName;
	}

	public void setCreaterUserName(String createrUserName) {
		this.createrUserName = createrUserName;
	}

	public String getCreaterNickName() {
		return createrNickName;
	}

	public void setCreaterNickName(String createrNickName) {
		this.createrNickName = createrNickName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="info")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Transient
	public List<PicAnnotation> getPicAnnotation() {
		return picAnnotation;
	}

	public void setPicAnnotation(List<PicAnnotation> picAnnotation) {
		this.picAnnotation = picAnnotation;
	}
	
	
	
	 
	
	
	
	
	
	
	
	
	
	
	

}
