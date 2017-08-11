package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 标准库
 * @author trs
 *
 */
@Entity
@Table(name="cmp_standard")
public class Standard extends ID{
	
	private String title;
	
	private Date createDate;
	
	private String createrUserName;
	
	private String createrUserNickName;
	/**
	 * 0：入库创建
	 * 1：后期创建的
	 */
	private Integer flag;
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreaterUserName() {
		return createrUserName;
	}

	public void setCreaterUserName(String createrUserName) {
		this.createrUserName = createrUserName;
	}

	public String getCreaterUserNickName() {
		return createrUserNickName;
	}

	public void setCreaterUserNickName(String createrUserNickName) {
		this.createrUserNickName = createrUserNickName;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}


	
	public Standard(){
		
	}
	public Standard(String title,Integer flag,String createrUserName,String createrUserNickName){
		this.setFlag(flag);
		this.setTitle(title);
		this.setCreaterUserName(createrUserName);
		this.setCreaterUserNickName(createrUserNickName);
		this.setCreateDate(new Date());
	}
	
	
	
	
	

}
