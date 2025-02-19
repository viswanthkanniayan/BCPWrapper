package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;

public class EntityRequest implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pan;
	private Date creationDate;
	private String entityType;
	private String auditorType;
	private String infoProviderType;
	private String cin;
	private String lei;
	private String ckycId;
	private String regId;
	private String name;
	private String legalConstitution;
	private String dateOfIncorporation;
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
	private String userType;
	private String txnType;
	private String password;
	private String bank;
	private String type;
	private String emailOtp;
	private String mobileOtp;
	private String status;
	private String remark;
	private String passwordChangeDate;
    private String state;
	private String auditeeRegNo;
	private String firmRegno;
	private long id;
	private String bankname;
	private String code;
	private String bcCount;
	private String firmName;
	private String downloadStatus;
	private String memeberRegNo;
	private String entityOrigin;
	private String dob;
	private String scheduleBank;
	private String regEntityName;
	private String entityCategory;
	private String alterEmailId;
	private String SecondaryMobileNo;
	private String digitalSignUpload;
	private String entityRegState;
	private String email;
	private String mobile;
	private String linkageType;
	private String entityName;
	private String emailOtpStatus;
	private String mobileOptStatus;
	private String auditeeType;
	private String paymentMode;
	private String otpFlg;
	
	private String entityReqName;
	private String entityReqPan;
	/*private String pAuditeeName;
	private String pAuditeePan;
	private String pAuditeeRegNo;
	
	private String pAuditorName;
	private String pAuditorPan;
	private String pAuditorRegNo;
	private String pBcCount;
	private String pFromDate;
	private String pRemarks;
	private String pStatus;
	private String pStatusDate;
	private String pToDate;
	private String pRequestorName;
	private String pRequestorPan;
	private String pFirmName;
	private String pMemberRegNo;
	
	
	*/

	private String bankName;
	private String qrLink;
	
	
	private String entityMobile;
	  private String entityEmail;
	
	//bankLinkage---------------------------------------------------------------------------------------------------------

	
	
	public String getOtpFlg() {
		return otpFlg;
	}
	public void setOtpFlg(String otpFlg) {
		this.otpFlg = otpFlg;
	}
	public String getQrLink() {
		return qrLink;
	}
	public String getAuditeeType() {
		return auditeeType;
	}
	public void setAuditeeType(String auditeeType) {
		this.auditeeType = auditeeType;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public void setQrLink(String qrLink) {
		this.qrLink = qrLink;
	}
	public String getLinkageType() {
		return linkageType;
	}
	public void setLinkageType(String linkageType) {
		this.linkageType = linkageType;
	}
	private String linkageId;
	private String auditeePan;
	private String auditeeName;
	private String requestorName;
	private String requestorMobile;
	private String requestorPan;
	//private String status;
	private String fromDate;
	private String toDate;
	//private String remark;
	//private Date creationDate;
	private String supportingDoc;
	private String statusDate;
	
	private String userId;
	private String auditorName;
	private String auditorRegNo;
	private String auditorPan;
	//-----------------------------------------------------------------------------------------------
	
	private String entityPan;
	private String reqId;
	private String orderId;
	private Date reqTime;
	private Date resTime;
	private String txnStatus;
	private String errorMessage;
	private String amount;
	private String transRefNo;
	private String serviceType;
	private String paymentId;
	private String paymentStatus;
	private String srNo;
	
	//------------ balance confirmatio 
	
	private String auditeeCin;
	private String auditeeLei;
	private String purpose;
	private String requestorRegNo;
	private String requestorType;
	private String informationProvider;
	private String businessDate;
	private String requestDate;
	private String remarks;
	private String txnId;
	private String requestorEmail;
	//-------------------------------Sign Check------------------

//	private int id;
	private String signedBy;
	private String location;
	private String reason;
	private String signedDate;
	private String info;
	private String subject;
	private String issuer;
	private String serialNo;
	private String validity;
	
	private String countOfAcc;
	private String bankCrg;
	private String uniqueTxnId;
	
	private String bankRefNo;
	private String otpValue;
	
	private String responseURL;
	private String APIpurpose;
	
	//---------------------------------------------------statemaster--------------------------------------------------------------
	

	private String stateCode;
	

	
	
	//--------------------------------------------------------------------------------------------
	
	
	public String getStateCode() {
		return stateCode;
	}
	public String getRequestorMobile() {
		return requestorMobile;
	}
	public void setRequestorMobile(String requestorMobile) {
		this.requestorMobile = requestorMobile;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getState() {
		return state;
	}
	public String getRequestorEmail() {
		return requestorEmail;
	}
	public void setRequestorEmail(String requestorEmail) {
		this.requestorEmail = requestorEmail;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSignedBy() {
		return signedBy;
	}
	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(String signedDate) {
		this.signedDate = signedDate;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAuditeeCin() {
		return auditeeCin;
	}
	public void setAuditeeCin(String auditeeCin) {
		this.auditeeCin = auditeeCin;
	}
	public String getAuditeeLei() {
		return auditeeLei;
	}
	public void setAuditeeLei(String auditeeLei) {
		this.auditeeLei = auditeeLei;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getRequestorRegNo() {
		return requestorRegNo;
	}
	public void setRequestorRegNo(String requestorRegNo) {
		this.requestorRegNo = requestorRegNo;
	}
	public String getRequestorType() {
		return requestorType;
	}
	public void setRequestorType(String requestorType) {
		this.requestorType = requestorType;
	}
	public String getInformationProvider() {
		return informationProvider;
	}
	public void setInformationProvider(String informationProvider) {
		this.informationProvider = informationProvider;
	}
	
	public String getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getBcCount() {
		return bcCount;
	}
	public void setBcCount(String bcCount) {
		this.bcCount = bcCount;
	}
	public String getRemark() {
		return remark;
	}
	public String getAuditorRegNo() {
		return auditorRegNo;
	}
	public void setAuditorRegNo(String auditorRegNo) {
		this.auditorRegNo = auditorRegNo;
	}
	public String getAuditorPan() {
		return auditorPan;
	}
	public void setAuditorPan(String auditorPan) {
		this.auditorPan = auditorPan;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public String getAuditeeRegNo() {
		return auditeeRegNo;
	}
	public void setAuditeeRegNo(String auditeeRegNo) {
		this.auditeeRegNo = auditeeRegNo;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getSupportingDoc() {
		return supportingDoc;
	}
	public void setSupportingDoc(String supportingDoc) {
		this.supportingDoc = supportingDoc;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEmailOtp() {
		return emailOtp;
	}
	public void setEmailOtp(String emailOtp) {
		this.emailOtp = emailOtp;
	}
	public String getMobileOtp() {
		return mobileOtp;
	}
	public void setMobileOtp(String mobileOtp) {
		this.mobileOtp = mobileOtp;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
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
	
	public String getDateOfIncorporation() {
		return dateOfIncorporation;
	}
	public void setDateOfIncorporation(String dateOfIncorporation) {
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
	
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getLinkageId() {
		return linkageId;
	}
	public void setLinkageId(String linkageId) {
		this.linkageId = linkageId;
	}
	public String getAuditeePan() {
		return auditeePan;
	}
	public void setAuditeePan(String auditeePan) {
		this.auditeePan = auditeePan;
	}
	public String getAuditeeName() {
		return auditeeName;
	}
	public void setAuditeeName(String auditeeName) {
		this.auditeeName = auditeeName;
	}
	public String getRequestorName() {
		return requestorName;
	}
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	public String getRequestorPan() {
		return requestorPan;
	}
	public void setRequestorPan(String requestorPan) {
		this.requestorPan = requestorPan;
	
	}
	

	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getEntityPan() {
		return entityPan;
	}
	public void setEntityPan(String entityPan) {
		this.entityPan = entityPan;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getReqTime() {
		return reqTime;
	}
	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}
	public Date getResTime() {
		return resTime;
	}
	public void setResTime(Date resTime) {
		this.resTime = resTime;
	}
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransRefNo() {
		return transRefNo;
	}
	public void setTransRefNo(String transRefNo) {
		this.transRefNo = transRefNo;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	public String getFirmRegno() {
		return firmRegno;
	}
	public void setFirmRegno(String firmRegno) {
		this.firmRegno = firmRegno;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
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
	public String getMemeberRegNo() {
		return memeberRegNo;
	}
	public void setMemeberRegNo(String memeberRegNo) {
		this.memeberRegNo = memeberRegNo;
	}
	public String getEntityRegState() {
		return entityRegState;
	}
	public void setEntityRegState(String entityRegState) {
		this.entityRegState = entityRegState;
	}
	public String getCountOfAcc() {
		return countOfAcc;
	}
	public void setCountOfAcc(String countOfAcc) {
		this.countOfAcc = countOfAcc;
	}
	public String getBankCrg() {
		return bankCrg;
	}
	public void setBankCrg(String bankCrg) {
		this.bankCrg = bankCrg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getUniqueTxnId() {
		return uniqueTxnId;
	}
	public void setUniqueTxnId(String uniqueTxnId) {
		this.uniqueTxnId = uniqueTxnId;
	}
	public String getBankRefNo() {
		return bankRefNo;
	}
	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}
	public String getOtpValue() {
		return otpValue;
	}
	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}
	public String getResponseURL() {
		return responseURL;
	}
	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}
	public String getAPIpurpose() {
		return APIpurpose;
	}
	public void setAPIpurpose(String aPIpurpose) {
		APIpurpose = aPIpurpose;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getEntityMobile() {
		return entityMobile;
	}
	public void setEntityMobile(String entityMobile) {
		this.entityMobile = entityMobile;
	}
	public String getEntityEmail() {
		return entityEmail;
	}
	public void setEntityEmail(String entityEmail) {
		this.entityEmail = entityEmail;
	}
	public String getEntityReqPan() {
		return entityReqPan;
	}
	public void setEntityReqPan(String entityReqPan) {
		this.entityReqPan = entityReqPan;
	}
	public String getEntityReqName() {
		return entityReqName;
	}
	public void setEntityReqName(String entityReqName) {
		this.entityReqName = entityReqName;
	}
	
	
	
	
}