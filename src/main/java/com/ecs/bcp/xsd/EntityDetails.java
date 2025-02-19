package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;

public class EntityDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private String pan;
	private String entityType;
	private Date creationDate;
	private String auditorType;
	private String infoProviderType;
	private String cin;
	private String lei;
	private String ckycId;
	private String regId;
	private String name;
	private String legalConstitution;
	private Date dateOfIncorporation;
	private String emailId;
	private String contactNo;
	private String mobileNo;
	private String registeredAddress;
	private String registeredPincode;
	private String communicationAddress;
	private String communicationPincode;
	private String gstNo;
	private String billingAddress;
	private String billingPincode;
	private String consent;
	private String status;
	private String password;
	private String firmRegno;
	private String state;
	private String entityOrigin;
	private String dob;
	private String scheduleBank;
	private String regEntityName;
	private String entityCategory;
	private String stateCode;
	private String alterEmailId;
	private String SecondaryMobileNo;
	private String digitalSignUpload;
	private String supportingDoc;
	private String remark;
	private Date passwordChangeDate;
	private String entityRegState;
	private String otpFlg;
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getAuditorType() {
		return auditorType;
	}
	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}
	public String getInfoProviderType() {
		return infoProviderType;
	}
	public void setInfoProviderType(String infoProviderType) {
		this.infoProviderType = infoProviderType;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	
	public String getFirmRegno() {
		return firmRegno;
	}
	public void setFirmRegno(String firmRegno) {
		this.firmRegno = firmRegno;
	}
	public String getLei() {
		return lei;
	}
	public void setLei(String lei) {
		this.lei = lei;
	}
	public String getCkycId() {
		return ckycId;
	}
	public void setCkycId(String ckycId) {
		this.ckycId = ckycId;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegalConstitution() {
		return legalConstitution;
	}
	public void setLegalConstitution(String legalConstitution) {
		this.legalConstitution = legalConstitution;
	}
	public Date getDateOfIncorporation() {
		return dateOfIncorporation;
	}
	public void setDateOfIncorporation(Date dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContactNo() {
		return contactNo;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	public String getRegisteredPincode() {
		return registeredPincode;
	}
	public void setRegisteredPincode(String registeredPincode) {
		this.registeredPincode = registeredPincode;
	}
	public String getCommunicationAddress() {
		return communicationAddress;
	}
	public void setCommunicationAddress(String communicationAddress) {
		this.communicationAddress = communicationAddress;
	}
	public String getCommunicationPincode() {
		return communicationPincode;
	}
	public void setCommunicationPincode(String communicationPincode) {
		this.communicationPincode = communicationPincode;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getBillingPincode() {
		return billingPincode;
	}
	public void setBillingPincode(String billingPincode) {
		this.billingPincode = billingPincode;
	}
	public String getConsent() {
		return consent;
	}
	public void setConsent(String consent) {
		this.consent = consent;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getPasswordChangeDate() {
		return passwordChangeDate;
	}
	public void setPasswordChangeDate(Date passwordChangeDate) {
		this.passwordChangeDate = passwordChangeDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEntityOrigin() {
		return entityOrigin;
	}
	public void setEntityOrigin(String entityOrigin) {
		this.entityOrigin = entityOrigin;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getScheduleBank() {
		return scheduleBank;
	}
	public void setScheduleBank(String scheduleBank) {
		this.scheduleBank = scheduleBank;
	}
	public String getRegEntityName() {
		return regEntityName;
	}
	public void setRegEntityName(String regEntityName) {
		this.regEntityName = regEntityName;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getAlterEmailId() {
		return alterEmailId;
	}
	public void setAlterEmailId(String alterEmailId) {
		this.alterEmailId = alterEmailId;
	}
	public String getSecondaryMobileNo() {
		return SecondaryMobileNo;
	}
	public void setSecondaryMobileNo(String secondaryMobileNo) {
		SecondaryMobileNo = secondaryMobileNo;
	}
	public String getDigitalSignUpload() {
		return digitalSignUpload;
	}
	public void setDigitalSignUpload(String digitalSignUpload) {
		this.digitalSignUpload = digitalSignUpload;
	}
	public String getSupportingDoc() {
		return supportingDoc;
	}
	public void setSupportingDoc(String supportingDoc) {
		this.supportingDoc = supportingDoc;
	}
	public String getEntityRegState() {
		return entityRegState;
	}
	public void setEntityRegState(String entityRegState) {
		this.entityRegState = entityRegState;
	}
	public String getOtpFlg() {
		return otpFlg;
	}
	public void setOtpFlg(String otpFlg) {
		this.otpFlg = otpFlg;
	}
	
	
	
}
