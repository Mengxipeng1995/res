package com.cmp.res.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 权限申请表
 * @author trs
 *
 */
@Entity
@Table(name="cmp_apply")
public class Apply extends ID{
	/**
	 * 申请者id
	 */
	private Long applicantId;
	/**
	 * 申请者名称
	 */
	private String applicantUserName;
	/**
	 * 申请者昵称
	 */
	private String applicantNickName;
	
	private Date createDate;
	/**
	 * 审批者id
	 */
	private Long approverId;
	
	private String approverUserName;
	
	private String approverNickName;
	/**
	 * 有效期
	 */
	private Date validDate;
	
	private String describeInfo;
	/**
	 * 审批者批注
	 */
	private String responseText;
	
	
	
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	/**
	 * 请求受理状态
	 * 0:审核中
	 * 1：审核通过
	 * 2：审核拒绝
	 * 3：过期
	 */
	private Integer status;
	public Long getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	public String getApplicantNickName() {
		return applicantNickName;
	}
	public void setApplicantNickName(String applicantNickName) {
		this.applicantNickName = applicantNickName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getApproverId() {
		return approverId;
	}
	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}
	public String getApproverUserName() {
		return approverUserName;
	}
	public void setApproverUserName(String approverUserName) {
		this.approverUserName = approverUserName;
	}
	public String getApproverNickName() {
		return approverNickName;
	}
	public void setApproverNickName(String approverNickName) {
		this.approverNickName = approverNickName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	public String getDescribeInfo() {
		return describeInfo;
	}
	public void setDescribeInfo(String describeInfo) {
		this.describeInfo = describeInfo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
