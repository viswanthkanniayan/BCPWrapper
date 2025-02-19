package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;

public class UserDetails implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pan;
	private String userType;
	private String regNo;
	private String name;
	private String designation;
	private String dob;
	private String emailId;
	private String mobileNo;
	private String consent;
	private Date creationDate;
	private String txnType;
	private String status;
	private String password;
	private String entityName;
	private String entityPan;
	private String firmRegno;
	private String memberRegNo;
	private String employeecode;
	private String emailOtpStatus;
	private String mobileOptStatus;
	private String eSignStatus;
	


	
	public String getEmailOtpStatus() {
		return emailOtpStatus;
	}
	public void setEmailOtpStatus(String emailOtpStatus) {
		this.emailOtpStatus = emailOtpStatus;
	}
	public String getMobileOptStatus() {
		return mobileOptStatus;
	}
	public void setMobileOptStatus(String mobileOptStatus) {
		this.mobileOptStatus = mobileOptStatus;
	}
	public String getEntityPan() {
		return entityPan;
	}
	public void setEntityPan(String entityPan) {
		this.entityPan = entityPan;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getConsent() {
		return consent;
	}
	public void setConsent(String consent) {
		this.consent = consent;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getFirmRegno() {
		return firmRegno;
	}
	public void setFirmRegno(String firmRegno) {
		this.firmRegno = firmRegno;
	}

	public String getEmployeecode() {
		return employeecode;
	}
	public void setEmployeecode(String employeecode) {
		this.employeecode = employeecode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMemberRegNo() {
		return memberRegNo;
	}
	public void setMemberRegNo(String memberRegNo) {
		this.memberRegNo = memberRegNo;
	}
	public String geteSignStatus() {
		return eSignStatus;
	}
	public void seteSignStatus(String eSignStatus) {
		this.eSignStatus = eSignStatus;
	}
	
	
	
}
