package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;

public class UserResponce implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean error;
	private String errorCode;
	private String errorDescription;
	

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
	private String entityName;
	private String entityPan;
	private String entityRegNo;
	private String firmRegno;
	private String memberRegNo;
	private String emailUniqueLink;
	private String entityType;
	
	private String entityMobile;
	private String entityEmail;
//-----------------------------------------BankLikage-------------------------------------------
	private String linkageId;
	private String auditeePan;
	private String auditeeName;
	private String requestorName;
	private String requestorPan;
	private String status;
	private Date fromDate;
	private Date toDate;
	private String remark;
	
	private Date eSignDate;
	private String eSignStatus;
	
	 private Long totalUserCount;
	 private Long activeUser;
	 private Long deActiveUser;
	 
	 
	 private Long auditorCount;
	 private Long auditeeCount;
	 private Long infoProvider;
	 
	 
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
	public Long getAuditorCount() {
		return auditorCount;
	}
	public void setAuditorCount(Long auditorCount) {
		this.auditorCount = auditorCount;
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
	public Long getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(Long activeUser) {
		this.activeUser = activeUser;
	}
	public Long getDeActiveUser() {
		return deActiveUser;
	}
	public void setDeActiveUser(Long deActiveUser) {
		this.deActiveUser = deActiveUser;
	}
	public void setTotalUserCount(Long totalUserCount) {
		this.totalUserCount = totalUserCount;
	}
	//-------------------------------------------------------------------------------------------------
	private UserDetailsPojo userDetails;
	private List<UserDetailsPojo> userDetailsList;
	
	private Date logoutDate;
	private String  passcodeCount;
	
	
	private UserDetails userDetail;
	private List<UserDetails> userDetailList;
	
	
	private List<EntityDetailsPojo> entityDetailsList;
	private List<String> entityNameList;
	private String entityNameDetails;
//	private UserDetailsPojo empDetails;
//	private List<UserDetailsPojo> empDetailsList;
//	
//----------------------------------------------------------------------------------	
	
	
	private List<com.ecs.bcp.xsd.Entity> entity;
	
	
	
	
	
	
	
	public List<com.ecs.bcp.xsd.Entity> getEntity() {
		return entity;
	}
	public void setEntity(List<com.ecs.bcp.xsd.Entity> entity) {
		this.entity = entity;
	}
	public boolean isError() {
		return error;
	}
	public String getEntityNameDetails() {
		return entityNameDetails;
	}
	public void setEntityNameDetails(String entityList) {
		this.entityNameDetails = entityList;
	}
	public List<String> getEntityNameList() {
		return entityNameList;
	}
	public void setEntityNameList(List<String> entityNameList) {
		this.entityNameList = entityNameList;
	}
	public List<EntityDetailsPojo> getEntityDetailsList() {
		return entityDetailsList;
	}
	public void setEntityDetailsList(List<EntityDetailsPojo> entityDetailsList) {
		this.entityDetailsList = entityDetailsList;
	}
	
	
	public String getEntityRegNo() {
		return entityRegNo;
	}
	public void setEntityRegNo(String entityRegNo) {
		this.entityRegNo = entityRegNo;
	}

	
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityPan() {
		return entityPan;
	}
	public void setEntityPan(String entityPan) {
		this.entityPan = entityPan;
	}
	public List<UserDetails> getUserDetailList() {
		return userDetailList;
	}
	public void setUserDetailList(List<UserDetails> userDetailList) {
		this.userDetailList = userDetailList;
	}
	public UserDetails getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserDetails userDetail) {
		this.userDetail = userDetail;
	}
	public UserDetailsPojo getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetailsPojo userDetails) {
		this.userDetails = userDetails;
	}
	public List<UserDetailsPojo> getUserDetailsList() {
		return userDetailsList;
	}
	public void setUserDetailsList(List<UserDetailsPojo> userDetailsList) {
		this.userDetailsList = userDetailsList;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public void setEntityNameDetails(List<Entity> entityList) {
		// TODO Auto-generated method stub
		
	}
	public String getEmailUniqueLink() {
		return emailUniqueLink;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public void setEmailUniqueLink(String emailUniqueLink) {
		this.emailUniqueLink = emailUniqueLink;
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
	public long getTotalUserCount() {
		return totalUserCount;
	}
	public void setTotalUserCount(long totalUserCount) {
		this.totalUserCount = totalUserCount;
	}
	
	
}
