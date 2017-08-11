package com.cmp.res.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;

@Entity
@Table(name="cmp_photo_annotation")
public class PicAnnotation extends ID{
	
	private Long photoId;
	
	private String content;
	
	private Integer order;
	
//	private String tempCode;
	
	List<InlinePic> ips=Lists.newArrayList();

	@Column(name="photo_id")
	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	@Lob
	@Column(name = "content", columnDefinition = "BLOB",nullable=true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="seqencing")
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Transient
	public List<InlinePic> getIps() {
		return ips;
	}

	public void setIps(List<InlinePic> ips) {
		this.ips = ips;
	}
	
	

}
