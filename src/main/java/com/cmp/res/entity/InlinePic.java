package com.cmp.res.entity;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 不进入图片库的图片
 * 如：图片图注中的图片
 * @author trs
 *
 */
@Entity
@Table(name="cmp_inlinepic")
public class InlinePic extends ID{
	
	private String path;
	
	private Long objectId;
	
	private String tempCode;
	
	
	/**
	 * 1:图注
	 */
	private Integer type;


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Long getObjectId() {
		return objectId;
	}


	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}
	
	private File file;


	@Transient
	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public String getTempCode() {
		return tempCode;
	}


	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}
	
	

}
