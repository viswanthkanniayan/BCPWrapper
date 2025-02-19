package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ecs.bcp.icai.xsd.ErrorCode;
import com.ecs.bcp.pojo.AuditorAssignPojo;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiMasterPojo;
import com.ecs.bcp.pojo.BankLinkagePojo;
import com.ecs.bcp.pojo.BankMasterPojo;
import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.StateMasterPojo;
import com.ecs.rezorpay.error.response.ErrorResponse;

/**
 * 
 */
public class EntityResponce implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean error;
	private String errorCode;
	private String errorDescription;


    private String auditeePan;
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
	private String password;
	private String statusDate;
	private String linkageId;
	private String supportingDoc;
	private String txnId;
	private String auditorName;
	private String auditorPan;
	private String auditeeRegNo;
	private String auditorRegno;
	private String userType;
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
	private String token;
	private String paymentId;
	private String paymentStatus;
	private String linkageType;
	private String userId;
	private Date fromDate;
	private Date toDate;
	private int id;
	private String signedBy;
	private String location;
	private String reason;
	private Date signedDate;
	private String info;
	private String subject;
	/*	private int totalAmount;
	private int transfersAmount_1;
	private int transfersAmount_2;
	private String transfersId1;
	private String transfersId2; */
	private String email;
	private String mobile;

	private String status;
	private String tokenId;

	private String StatusFRN ;
	private String StatusMRN ;
	private String StatusMAPPING ;

	private String  firmRegno;
	private String  memeber_reg_no;
	private String MRNDtlsStatus ;
	private String MRNDtlsEmail ;

	private String MRNDtlsMobileNo ;
	private String MRNDtlsName;

	private String UTID ;
	private String FRNDtlsFirmName ;

	private String FRNDtlsEmail ;
	private String FRNDtlsFRN ;

	private ErrorCode errorCodeIcai;
	private String entityRegState;


	private String CommomName;
	private String HouseIdentifier;
	private String Street;
	private String Locality;
	private String Postalcode;
	private String Organisation;
	private String Country;
	private String Title;


	private Double PSBABCPRevenueShare;
	private Double NeSLBCPRevenue_Share;
	private Double BANKBCPRevenue_Share;
	private Double PSBATDSCollection;
	private Double Total;

	
	
	private String uniqueTxnId;
	private String accountsCount;
	private String bankCharges;
	private String pdfPath;
	private String bank;
	private String ackStatus;
	
	 private Long auditorActiveUser;
	 private Long auditordeActiveUser;
	 private Long auditeeActiveUser;
	 private Long auditeeDeActiveUser;
	 private Long infoActiveUser;
	 private Long infoDeActiveUser;
	 
	public Long getAuditorActiveUser() {
		return auditorActiveUser;
	}
	public void setAuditorActiveUser(Long auditorActiveUser) {
		this.auditorActiveUser = auditorActiveUser;
	}
	public Long getAuditordeActiveUser() {
		return auditordeActiveUser;
	}
	public void setAuditordeActiveUser(Long auditordeActiveUser) {
		this.auditordeActiveUser = auditordeActiveUser;
	}
	public Long getAuditeeActiveUser() {
		return auditeeActiveUser;
	}
	public void setAuditeeActiveUser(Long auditeeActiveUser) {
		this.auditeeActiveUser = auditeeActiveUser;
	}
	public Long getAuditeeDeActiveUser() {
		return auditeeDeActiveUser;
	}
	public void setAuditeeDeActiveUser(Long auditeeDeActiveUser) {
		this.auditeeDeActiveUser = auditeeDeActiveUser;
	}
	public Long getInfoActiveUser() {
		return infoActiveUser;
	}
	public void setInfoActiveUser(Long infoActiveUser) {
		this.infoActiveUser = infoActiveUser;
	}
	public Long getInfoDeActiveUser() {
		return infoDeActiveUser;
	}
	public void setInfoDeActiveUser(Long infoDeActiveUser) {
		this.infoDeActiveUser = infoDeActiveUser;
	}


	public String bankRefNo;
	public String bankAction;
	public String bankLinkageStatus;
	
	 private Long totalEntityCount;
	
	public String source;
	public String step;
	public String errorReason;
	public String downloadPath;
	
	private String umAuditee01;
	private String umAuditee02;
	private String umAuditee03;
	private String umAuditee04;
	private String umAuditee05;
	private String umAuditee06;
	
	private String umAuditor01;
	private String umAuditor02;
	private String umAuditor03;
	private String umAuditor04;
	
	private String umInfo01;
	private String umInfo02;
	
	 private Long entityAuditorCount;
	 private Long auditeeCount;
	 private Long infoProvider;
	
	 private Long activeEntity;
	 private Long deActiveEntity;
	 
	 

	 private Long entityTotalCount;
	 
	//----------------------------------stateMaster---------------------------------------------------------------------------




	public String getCommomName() {
		return CommomName;
	}
	public Long getEntityTotalCount() {
		return entityTotalCount;
	}
	public void setEntityTotalCount(Long entityTotalCount) {
		this.entityTotalCount = entityTotalCount;
	}
	public Long getEntityAuditorCount() {
		return entityAuditorCount;
	}
	public void setEntityAuditorCount(Long entityAuditorCount) {
		this.entityAuditorCount = entityAuditorCount;
	}
	public Long getAuditeeCount() {
		return auditeeCount;
	}
	public void setAuditeeCount(Long auditeeCount) {
		this.auditeeCount = auditeeCount;
	}
	public Long getInfoProvider() {
		return infoProvider;
	}
	public void setInfoProvider(Long infoProvider) {
		this.infoProvider = infoProvider;
	}
	public void setTotalEntityCount(Long totalEntityCount) {
		this.totalEntityCount = totalEntityCount;
	}
	public void setActiveEntity(Long activeEntity) {
		this.activeEntity = activeEntity;
	}
	public void setDeActiveEntity(Long deActiveEntity) {
		this.deActiveEntity = deActiveEntity;
	}
	public long getActiveEntity() {
		return activeEntity;
	}
	public void setActiveEntity(long activeEntity) {
		this.activeEntity = activeEntity;
	}
	public long getDeActiveEntity() {
		return deActiveEntity;
	}
	public void setDeActiveEntity(long deActiveEntity) {
		this.deActiveEntity = deActiveEntity;
	}
	public String getUmAuditee01() {
		return umAuditee01;
	}
	public void setUmAuditee01(String umAuditee01) {
		this.umAuditee01 = umAuditee01;
	}
	public String getUmAuditee02() {
		return umAuditee02;
	}
	public void setUmAuditee02(String umAuditee02) {
		this.umAuditee02 = umAuditee02;
	}
	public String getUmAuditee03() {
		return umAuditee03;
	}
	public void setUmAuditee03(String umAuditee03) {
		this.umAuditee03 = umAuditee03;
	}
	public String getUmAuditee04() {
		return umAuditee04;
	}
	public void setUmAuditee04(String umAuditee04) {
		this.umAuditee04 = umAuditee04;
	}
	public String getUmAuditee05() {
		return umAuditee05;
	}
	public void setUmAuditee05(String umAuditee05) {
		this.umAuditee05 = umAuditee05;
	}
	public String getUmAuditee06() {
		return umAuditee06;
	}
	public void setUmAuditee06(String umAuditee06) {
		this.umAuditee06 = umAuditee06;
	}
	public String getUmAuditor01() {
		return umAuditor01;
	}
	public void setUmAuditor01(String umAuditor01) {
		this.umAuditor01 = umAuditor01;
	}
	public String getUmAuditor02() {
		return umAuditor02;
	}
	public void setUmAuditor02(String umAuditor02) {
		this.umAuditor02 = umAuditor02;
	}
	public String getUmAuditor03() {
		return umAuditor03;
	}
	public void setUmAuditor03(String umAuditor03) {
		this.umAuditor03 = umAuditor03;
	}
	public String getUmAuditor04() {
		return umAuditor04;
	}
	public void setUmAuditor04(String umAuditor04) {
		this.umAuditor04 = umAuditor04;
	}
	public String getUmInfo01() {
		return umInfo01;
	}
	public void setUmInfo01(String umInfo01) {
		this.umInfo01 = umInfo01;
	}
	public String getUmInfo02() {
		return umInfo02;
	}
	public void setUmInfo02(String umInfo02) {
		this.umInfo02 = umInfo02;
	}
	public void setCommomName(String commomName) {
		CommomName = commomName;
	}
	public String getHouseIdentifier() {
		return HouseIdentifier;
	}
	public void setHouseIdentifier(String houseIdentifier) {
		HouseIdentifier = houseIdentifier;
	}
	public String getStreet() {
		return Street;
	}
	public void setStreet(String street) {
		Street = street;
	}
	public String getLocality() {
		return Locality;
	}
	public void setLocality(String locality) {
		Locality = locality;
	}
	public String getPostalcode() {
		return Postalcode;
	}
	public void setPostalcode(String postalcode) {
		Postalcode = postalcode;
	}
	public String getOrganisation() {
		return Organisation;
	}
	public void setOrganisation(String organisation) {
		Organisation = organisation;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}


	//private int id;
	private String state;
	private String stateCode;

	private List<StateMasterPojo> statemasterDetails;


	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}


	public List<StateMasterPojo> getStatemasterDetails() {
		return statemasterDetails;
	}
	public void setStatemasterDetails(List<StateMasterPojo> statemasterDetails) {
		this.statemasterDetails = statemasterDetails;
	}


	//-----------------------------------------------------------------------------------------------------------------------------	
	private String requestorName;
	private String requestorPan;
	//	private long id;
	private String bankname;
	private String code;

	private List<BankMasterPojo> BankMasterDetails;

	private List<BankLinkagePojo> banklink;
	private EntityDetails AuditeeDetailsByName;
	private EntityDetails entityRequestById;
	private List<EntityDetails> entityRequestDetails;
	private List<BankLinkagePojo> linkageRequestDetails;
	private EntityDetailsPojo empDetails;
	private List<EntityDetailsPojo> empDetailsList;
	private List<BankLinkageDetails> bankLinkageDetails;
	private List<EntityDetails> auditeelist;
	private BankLinkagePojo bankLinkDetail;
    private BankApiMasterPojo BankApiMaster;

	private List<BalanceConfirmPojo> bcPaymentStatus;
	private BalanceConfirmPojo bcDetails;

	private List<AuditorAssignPojo> auditorCount;
	private BankLinkagePojo bankDetails;
	private AuditorAssignPojo auditorauditeeDetails;
	
	
	private List<BalanceConfirmXsd> totalSummary;
	//private BalanceConfirmPojo 
	
	


	public List<AuditorAssignPojo> getAuditorCount() {
		return auditorCount;
	}

	public BankApiMasterPojo getBankApiMaster() {
		return BankApiMaster;
	}
	public void setBankApiMaster(BankApiMasterPojo bankApiMaster) {
		BankApiMaster = bankApiMaster;
	}
	public List<BalanceConfirmXsd> getTotalSummary() {
		return totalSummary;
	}
	public void setTotalSummary(List<BalanceConfirmXsd> totalSummary) {
		this.totalSummary = totalSummary;
	}
	public void setAuditorCount(List<AuditorAssignPojo> auditorCount) {
		this.auditorCount = auditorCount;
	}
	public List<BalanceConfirmPojo> getBcPaymentStatus() {
		return bcPaymentStatus;
	}
	public void setBcPaymentStatus(List<BalanceConfirmPojo> bcPaymentStatus) {
		this.bcPaymentStatus = bcPaymentStatus;
	}


	private List<ErrorResponse> ErrorResponse ;

	public EntityDetails getAuditeeDetailsByName() {
		return AuditeeDetailsByName;
	}
	public void setAuditeeDetailsByName(EntityDetails auditeeDetailsByName) {
		AuditeeDetailsByName = auditeeDetailsByName;
	}
	public List<EntityDetails> getAuditeelist() {
		return auditeelist;
	}
	public void setAuditeelist(List<EntityDetails> auditeelist) {
		this.auditeelist = auditeelist;
	}


	private List<AuditorAssignPojo> auditorList;

	private String BalanceConfirmPojo;
	private List<BalanceConfirmPojo> auditoBcRequestList ;
	private List<BalanceConfirmPojo> auditeeBcRequestList ;

	private AuditorAssignPojo auditorDetails;


	private List<AuditorAssignPojo> auditeeList ;

	public List<AuditorAssignPojo> getAuditeeList() {
		return auditeeList;
	}
	public void setAuditeeList(List<AuditorAssignPojo> auditeeList) {
		this.auditeeList = auditeeList;
	}
	public AuditorAssignPojo getAuditorDetails() {
		return auditorDetails;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
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
	public void setAuditorDetails(AuditorAssignPojo auditorDetails) {
		this.auditorDetails = auditorDetails;
	}
	public List<BalanceConfirmPojo> getAuditeeBcRequestList() {
		return auditeeBcRequestList;
	}
	public void setAuditeeBcRequestList(List<BalanceConfirmPojo> auditeeBcRequestList) {
		this.auditeeBcRequestList = auditeeBcRequestList;
	}
	public String getBalanceConfirmPojo() {
		return BalanceConfirmPojo;
	}
	public void setBalanceConfirmPojo(String balanceConfirmPojo) {
		BalanceConfirmPojo = balanceConfirmPojo;
	}

	public List<BalanceConfirmPojo> getAuditoBcRequestList() {
		return auditoBcRequestList;
	}
	public void setAuditoBcRequestList(List<BalanceConfirmPojo> auditoBcRequestList) {
		this.auditoBcRequestList = auditoBcRequestList;
	}
	public List<AuditorAssignPojo> getAuditorList() {
		return auditorList;
	}
	public void setAuditorList(List<AuditorAssignPojo> auditorList) {
		this.auditorList = auditorList;
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
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public String getAuditorPan() {
		return auditorPan;
	}
	public void setAuditorPan(String auditorPan) {
		this.auditorPan = auditorPan;
	}
	public String getAuditeeRegNo() {
		return auditeeRegNo;
	}
	public void setAuditeeRegNo(String auditeeRegNo) {
		this.auditeeRegNo = auditeeRegNo;
	}






	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public BankLinkagePojo getBankLinkDetail() {
		return bankLinkDetail;
	}
	public void setBankLinkDetail(BankLinkagePojo bankLinkDetail) {
		this.bankLinkDetail = bankLinkDetail;
	}
	public List<BankLinkageDetails> getBankLinkageDetails() {
		return bankLinkageDetails;
	}
	public void setBankLinkageDetails(List<BankLinkageDetails> bankLinkageDetails) {
		this.bankLinkageDetails = bankLinkageDetails;
	}

	public String getLinkageId() {
		return linkageId;
	}
	public void setLinkageId(String linkageId) {
		this.linkageId = linkageId;
	}
	public String getSupportingDoc() {
		return supportingDoc;
	}
	public void setSupportingDoc(String supportingDoc) {
		this.supportingDoc = supportingDoc;
	}
	public List<BankLinkagePojo> getLinkageRequestDetails() {
		return linkageRequestDetails;
	}
	public void setLinkageRequestDetails(List<BankLinkagePojo> linkageRequestDetails) {
		this.linkageRequestDetails = linkageRequestDetails;
	}
	public EntityDetailsPojo getEmpDetails() {
		return empDetails;
	}
	public void setEmpDetails(EntityDetailsPojo empDetails) {
		this.empDetails = empDetails;
	}
	public List<EntityDetailsPojo> getEmpDetailsList() {
		return empDetailsList;
	}
	public void setEmpDetailsList(List<EntityDetailsPojo> empDetailsList) {
		this.empDetailsList = empDetailsList;
	}



	public EntityDetails getEntityRequestById() {
		return entityRequestById;
	}
	public void setEntityRequestById(EntityDetails entityRequestById) {
		this.entityRequestById = entityRequestById;
	}





	public List<EntityDetails> getEntityRequestDetails() {
		return entityRequestDetails;
	}
	public void setEntityRequestDetails(List<EntityDetails> entityRequestDetails) {
		this.entityRequestDetails = entityRequestDetails;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public List<BankMasterPojo> getBankMasterDetails() {
		return BankMasterDetails;
	}
	public void setBankMasterDetails(List<BankMasterPojo> bankMasterDetails) {
		BankMasterDetails = bankMasterDetails;
	}
	public List<BankLinkagePojo> getBanklink() {
		return banklink;
	}
	public void setBanklink(List<BankLinkagePojo> banklink) {
		this.banklink = banklink;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAuditorRegno() {
		return auditorRegno;
	}
	public void setAuditorRegno(String auditorRegno) {
		this.auditorRegno = auditorRegno;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<ErrorResponse> getErrorResponse() {
		return ErrorResponse;
	}
	public void setErrorResponse(ErrorResponse errorResp) {
		ErrorResponse = (List<com.ecs.rezorpay.error.response.ErrorResponse>) errorResp;
	}
	public void setErrorResponse(List<ErrorResponse> errorResponse) {
		ErrorResponse = errorResponse;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public BalanceConfirmPojo getBcDetails() {
		return bcDetails;
	}
	public void setBcDetails(BalanceConfirmPojo bcDetails) {
		this.bcDetails = bcDetails;
	}
	public BankLinkagePojo getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(BankLinkagePojo bankDetails) {
		this.bankDetails = bankDetails;
	}
	public AuditorAssignPojo getAuditorauditeeDetails() {
		return auditorauditeeDetails;
	}
	public void setAuditorauditeeDetails(AuditorAssignPojo auditorauditeeDetails) {
		this.auditorauditeeDetails = auditorauditeeDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getStatusFRN() {
		return StatusFRN;
	}
	public void setStatusFRN(String statusFRN) {
		StatusFRN = statusFRN;
	}
	public String getStatusMRN() {
		return StatusMRN;
	}
	public void setStatusMRN(String statusMRN) {
		StatusMRN = statusMRN;
	}
	public String getStatusMAPPING() {
		return StatusMAPPING;
	}
	public void setStatusMAPPING(String statusMAPPING) {
		StatusMAPPING = statusMAPPING;
	}
	public String getFirmRegno() {
		return firmRegno;
	}
	public void setFirmRegno(String firmRegno) {
		this.firmRegno = firmRegno;
	}
	public String getMemeber_reg_no() {
		return memeber_reg_no;
	}
	public void setMemeber_reg_no(String memeber_reg_no) {
		this.memeber_reg_no = memeber_reg_no;
	}
	public String getMRNDtlsStatus() {
		return MRNDtlsStatus;
	}
	public void setMRNDtlsStatus(String mRNDtlsStatus) {
		MRNDtlsStatus = mRNDtlsStatus;
	}
	public String getMRNDtlsEmail() {
		return MRNDtlsEmail;
	}
	public void setMRNDtlsEmail(String mRNDtlsEmail) {
		MRNDtlsEmail = mRNDtlsEmail;
	}
	public String getMRNDtlsMobileNo() {
		return MRNDtlsMobileNo;
	}
	public void setMRNDtlsMobileNo(String mRNDtlsMobileNo) {
		MRNDtlsMobileNo = mRNDtlsMobileNo;
	}
	public String getMRNDtlsName() {
		return MRNDtlsName;
	}
	public void setMRNDtlsName(String mRNDtlsName) {
		MRNDtlsName = mRNDtlsName;
	}
	public String getUTID() {
		return UTID;
	}
	public void setUTID(String uTID) {
		UTID = uTID;
	}
	public String getFRNDtlsFirmName() {
		return FRNDtlsFirmName;
	}
	public void setFRNDtlsFirmName(String fRNDtlsFirmName) {
		FRNDtlsFirmName = fRNDtlsFirmName;
	}
	public String getFRNDtlsEmail() {
		return FRNDtlsEmail;
	}
	public void setFRNDtlsEmail(String fRNDtlsEmail) {
		FRNDtlsEmail = fRNDtlsEmail;
	}
	public String getFRNDtlsFRN() {
		return FRNDtlsFRN;
	}
	public void setFRNDtlsFRN(String fRNDtlsFRN) {
		FRNDtlsFRN = fRNDtlsFRN;
	}
	public ErrorCode getErrorCodeIcai() {
		return errorCodeIcai;
	}
	public String getEntityRegState() {
		return entityRegState;
	}
	public void setEntityRegState(String entityRegState) {
		this.entityRegState = entityRegState;
	}
	public void setErrorCodeIcai(ErrorCode errorCodeIcai) {
		this.errorCodeIcai = errorCodeIcai;
	}
	
	public Double getPSBABCPRevenueShare() {
		return PSBABCPRevenueShare;
	}
	public void setPSBABCPRevenueShare(Double pSBABCPRevenueShare) {
		PSBABCPRevenueShare = pSBABCPRevenueShare;
	}
	public Double getNeSLBCPRevenue_Share() {
		return NeSLBCPRevenue_Share;
	}
	public void setNeSLBCPRevenue_Share(Double neSLBCPRevenue_Share) {
		NeSLBCPRevenue_Share = neSLBCPRevenue_Share;
	}
	public Double getBANKBCPRevenue_Share() {
		return BANKBCPRevenue_Share;
	}
	public void setBANKBCPRevenue_Share(Double bANKBCPRevenue_Share) {
		BANKBCPRevenue_Share = bANKBCPRevenue_Share;
	}
	public Double getPSBATDSCollection() {
		return PSBATDSCollection;
	}
	public void setPSBATDSCollection(Double pSBATDSCollection) {
		PSBATDSCollection = pSBATDSCollection;
	}
	public Double getTotal() {
		return Total;
	}
	public void setTotal(Double total) {
		Total = total;
	}
	public String getUniqueTxnId() {
		return uniqueTxnId;
	}
	public void setUniqueTxnId(String uniqueTxnId) {
		this.uniqueTxnId = uniqueTxnId;
	}
	public String getAccountsCount() {
		return accountsCount;
	}
	public void setAccountsCount(String accountsCount) {
		this.accountsCount = accountsCount;
	}
	public String getBankCharges() {
		return bankCharges;
	}
	public void setBankCharges(String bankCharges) {
		this.bankCharges = bankCharges;
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
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getLinkageType() {
		return linkageType;
	}
	public void setLinkageType(String linkageType) {
		this.linkageType = linkageType;
	}
	public String getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(String ackStatus) {
		this.ackStatus = ackStatus;
	}
	public String getBankRefNo() {
		return bankRefNo;
	}
	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}
	public String getBankAction() {
		return bankAction;
	}
	public void setBankAction(String bankAction) {
		this.bankAction = bankAction;
	}
	public String getBankLinkageStatus() {
		return bankLinkageStatus;
	}
	public void setBankLinkageStatus(String bankLinkageStatus) {
		this.bankLinkageStatus = bankLinkageStatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public String getAuditeePan() {
		return auditeePan;
	}
	public void setAuditeePan(String auditeePan) {
		this.auditeePan = auditeePan;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public long getTotalEntityCount() {
		return totalEntityCount;
	}
	public void setTotalEntityCount(long totalEntityCount) {
		this.totalEntityCount = totalEntityCount;
	}



}
