package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;


public class UserRequest implements Serializable{

	
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
	private String password;
    private String txnType;
    private String entityName;
    private String entityPan;
	private String status;
	private String remark;
	private String passwordChangeDate;
	private String entityRegNo;
	private String auditeePan;
	private String auditorPan;
	private String state;
	private String requestorPan;
	private String paymentStatus;
	private String bank;
	private String employeecode;
	private String memberRegNo;
	private String firmRegno;
	private String digitalSignatureUpload;
	private String userPan;
	private String eSignTxnId;
	private Date eSignDate;
	private String eSignStatus;
	private String emailUniqueLink;
	
	private Date logoutDate;
	private String  passcodeCount;
	private String emailOtpStatus;
	private String mobileOptStatus;
	
	
	
	
	
	
	public String getEmailUniqueLink() {
		return emailUniqueLink;
	}
	public void setEmailUniqueLink(String emailUniqueLink) {
		this.emailUniqueLink = emailUniqueLink;
	}
	public String geteSignTxnId() {
		return eSignTxnId;
	}
	public void seteSignTxnId(String eSignTxnId) {
		this.eSignTxnId = eSignTxnId;
	}
	public String getDigitalSignatureUpload() {
		return digitalSignatureUpload;
	}
	public void setDigitalSignatureUpload(String digitalSignatureUpload) {
		this.digitalSignatureUpload = digitalSignatureUpload;
	}
	public String getRequestorPan() {
		return requestorPan;
	}
	public void setRequestorPan(String requestorPan) {
		this.requestorPan = requestorPan;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAuditorPan() {
		return auditorPan;
	}
	public void setAuditorPan(String auditorPan) {
		this.auditorPan = auditorPan;
	}
	public String getAuditeePan() {
		return auditeePan;
	}
	public void setAuditeePan(String auditeePan) {
		this.auditeePan = auditeePan;
	}
	public String getEntityRegNo() {
		return entityRegNo;
	}
	public void setEntityRegNo(String entityRegNo) {
		this.entityRegNo = entityRegNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPasswordChangeDate() {
		return passwordChangeDate;
	}
	public void setPasswordChangeDate(String passwordChangeDate) {
		this.passwordChangeDate = passwordChangeDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEntityPan() {
		return entityPan;
	}
	public void setEntityPan(String entityPan) {
		this.entityPan = entityPan;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getEmployeecode() {
		return employeecode;
	}
	public void setEmployeecode(String employeecode) {
		this.employeecode = employeecode;
	}
	
	public String getFirmRegno() {
		return firmRegno;
	}
	public void setFirmRegno(String firmRegno) {
		this.firmRegno = firmRegno;
	}
	public String getMemberRegNo() {
		return memberRegNo;
	}
	public void setMemberRegNo(String memberRegNo) {
		this.memberRegNo = memberRegNo;
	}
	public String getUserPan() {
		return userPan;
	}
	public void setUserPan(String userPan) {
		this.userPan = userPan;
	}
	public Date geteSignDate() {
		return eSignDate;
	}
	public void seteSignDate(Date eSignDate) {
		this.eSignDate = eSignDate;
	}
	public String geteSignStatus() {
		return eSignStatus;
	}
	public void seteSignStatus(String eSignStatus) {
		this.eSignStatus = eSignStatus;
	}
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
	public Date getLogoutDate() {
		return logoutDate;
	}
	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}
	public String getPasscodeCount() {
		return passcodeCount;
	}
	public void setPasscodeCount(String passcodeCount) {
		this.passcodeCount = passcodeCount;
	}
	
	
}
