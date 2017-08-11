package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="cmp_video_rel")
public class VideoCategoryMapping extends ID{
	
	private Long videoId;

	/**
	 * 标题
	 */
	private String title;
	
	private String createrUserName;
	
	private String createrNickName;
	
	private Date createDate;
	/**
	 * 0:转换中
	 * 1：转换成功
	 * 2：转换失败
	 */
	private Integer status;
	
	private Long length;
	

	
	/**
	 * 1:删除
	 * 其他未删除
	 */
	private Integer deleteFlag;
	
	
	
	/**
	 * 描述
	 */
	private String desc;
	
	/**
	 * 分类id
	 */
	private Long catid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	

	@Column(name="info")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}



	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	
	
	
	
	


	

}
