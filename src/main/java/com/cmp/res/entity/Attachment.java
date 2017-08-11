package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 附件表
 * @author Administrator
 *
 */
@Entity
@Table(name="cmp_attachment")
public class Attachment extends ID{
	/**
	 * 资源id
	 */
	private long resouceId;;
	
	/**
	 * 附件名称
	 */
	private String fileName;
	
	/**
	 * 附件路径
	 */
	private String filePath;
	
	/**
	 * 附件类型
	 */
	private long typeId;
	
	/**
	 * 类型名称
	 */
	private String typeName;

	public long getResouceId() {
		return resouceId;
	}

	public void setResouceId(long resouceId) {
		this.resouceId = resouceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Attachment(){}
	
	public Attachment(long resrouceId,String fileName,String filePath,long typeId,String typeName){
		this.resouceId=resrouceId;
		this.fileName=fileName;
		this.filePath=filePath;
		this.typeId=typeId;
		this.typeName=typeName;
		
	}
	
	
	

}
