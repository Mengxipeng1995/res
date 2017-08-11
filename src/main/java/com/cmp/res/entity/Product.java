package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 产品
 * @author trs
 *
 */
@Entity
@Table(name="cmp_product")
public class Product extends ID{
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 创建者
	 */
	private String createrName;
	/**
	 *创建者昵称
	 */
	private String createrNickName;
	/**
	 * 状态
	 * 0：编辑状态；
	 * 1:发布状态
	 * 2：创建中
	 */
	private Integer status;
	
	/**
	 * 产品类型
	 * 0：条目；
	 * 1：图片
	 * 2:视频
	 * 3：混合型
	 */
	private Integer type;
	
	/**
	 * 分类id
	 * 只有依据分类树创建的才有分类id
	 */
	private Long catid;
	
	private Date createDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreaterNickName() {
		return createrNickName;
	}

	public void setCreaterNickName(String createrNickName) {
		this.createrNickName = createrNickName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	

}
