package com.cmp.res.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;

/**
 * 参考文献
 * @author zhangxue
 *
 */
@Entity
@Table(name="cmp_bibliography")
public class Bibliography extends IdEntity {
	
	private String title;
	
	private String author;
	
	private String descp;
	
	private Long bookid;
	
	private String xmlId;
	
	private List<Photo> photos=Lists.newArrayList();

	@Type(type="text")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Type(type="text")
	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	@Transient
	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	
	
	
	
	
	
	
	
	

}
