package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="cmp_logs")
public class SystemLogs extends ID{
	
	private String classType;
	
	private Date logDate;
	/**
	 * 0:登录日志
	 * 1:流程日志
	 * 2：专题日志
	 * 3：视频日志
	 * 4：图片日志
	 * 5：图书日志
	 * 6、索引词日志
	 * 7、参考文献
	 * 8、标准
	 * 9、条目
	 * 10、产品
	 * 11、分类
	 */
	private Integer logType;
	
	private String message;
	
	private String objectName;
	
	private Long objectid;
	
	private String operator;
	
	private String priority;

	/**
	 * @return the classType
	 */
	@Column(name="class_type")
	public String getClassType() {
		return classType;
	}

	/**
	 * @param classType the classType to set
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	/**
	 * @return the logDate
	 */
	@Column(name="log_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getLogDate() {
		return logDate;
	}

	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	/**
	 * @return the logType
	 */
	@Column(name="log_type")
	public Integer getLogType() {
		return logType;
	}

	/**
	 * @param logType the logType to set
	 */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the objectName
	 */
	@Column(name="object_name")
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the objectid
	 */
	public Long getObjectid() {
		return objectid;
	}

	/**
	 * @param objectid the objectid to set
	 */
	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	

}
