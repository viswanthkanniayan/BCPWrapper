package com.ecs.bcp.xsd;

import java.io.Serializable;

public class CountResponseXsd implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean error;
	private String errorCode;
	private String errorDescription;	
	
	
	
	private long infoActive;
	private long infoDeActive;
	private long infoReject;
	private long infoPending;
	private long infohold;
	private long infoTotalCount;
	//---------------------------------bank---------------------------
	
	private long bankActiveCount;
	private long bankPendingCount;
	private long bankDeactiveCount;
	private long bankRejectCount;
	private long bankLinkageTotalCount;
	private long banklinkageTotalCount;
	
	//---------------------------------bc auditee--------------------------------------
	
	private long bcActiveCount;
	private long bcPendingCount;
	private long bcExpiredCount;
	private long bcPendingpaymentStatus;
	private long bcActivepaymentStatus;
    private long bcDownloadStatus;
	private long bcPendingDownloadStatus;
	private long bcTotalCount;

 
	 
	 
	 //-----------------user----------------------------------------------------------
	 
	 
	 private long activeUser;
	 private long deActiveUser;
	 private long userTotalCount;
	 

	//-------------------------------auditor assign--------------------------
		
	 private long auditeeActiveCount;
	 private long auditeeDeactiveCount;
	 private long auditeeTotalCount;
	 
	 private long auditorActiveCount;
	 private long auditorDeactiveCount;
	 private long auditorTotalCount;
	    
     //----------------=----  bc auditor-------------------------------------------------------------
	     
	     private long bcReqActiveCount;
	 	 private long bcReqPendingCount;
		 private long bcReqExpiredCount;
		 private long bcReqPendingpaymentStatus;
		 private long bcReqActivepaymentStatus;
		 private long bcReqDownloadStatus;
		 private long bcReqPendingDownloadStatus;
		 private long  bcReqTotalCount;
		 
		 
	 //---------------------------------------------------------------------------------------------
		    
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
	public long getBankActiveCount() {
		return bankActiveCount;
	}
	public void setBankActiveCount(long bankActiveCount) {
		this.bankActiveCount = bankActiveCount;
	}
	public long getBankPendingCount() {
		return bankPendingCount;
	}
	public void setBankPendingCount(long bankPendingCount) {
		this.bankPendingCount = bankPendingCount;
	}
	public long getBankDeactiveCount() {
		return bankDeactiveCount;
	}
	public void setBankDeactiveCount(long bankDeactiveCount) {
		this.bankDeactiveCount = bankDeactiveCount;
	}
	public long getBankRejectCount() {
		return bankRejectCount;
	}
	public void setBankRejectCount(long bankRejectCount) {
		this.bankRejectCount = bankRejectCount;
	}
	public long getBankLinkageTotalCount() {
		return bankLinkageTotalCount;
	}
	public void setBankLinkageTotalCount(long bankLinkageTotalCount) {
		this.bankLinkageTotalCount = bankLinkageTotalCount;
	}
	public long getAuditorActiveCount() {
		return auditorActiveCount;
	}
	public void setAuditorActiveCount(long auditorActiveCount) {
		this.auditorActiveCount = auditorActiveCount;
	}
	public long getAuditorDeactiveCount() {
		return auditorDeactiveCount;
	}
	public void setAuditorDeactiveCount(long auditorDeactiveCount) {
		this.auditorDeactiveCount = auditorDeactiveCount;
	}
	public long getAuditeeActiveCount() {
		return auditeeActiveCount;
	}
	public void setAuditeeActiveCount(long auditeeActiveCount) {
		this.auditeeActiveCount = auditeeActiveCount;
	}
	public long getAuditeeDeactiveCount() {
		return auditeeDeactiveCount;
	}
	public void setAuditeeDeactiveCount(long auditeeDeactiveCount) {
		this.auditeeDeactiveCount = auditeeDeactiveCount;
	}
	public long getBcPendingCount() {
		return bcPendingCount;
	}
	public void setBcPendingCount(long bcPendingCount) {
		this.bcPendingCount = bcPendingCount;
	}
	public long getBcActiveCount() {
		return bcActiveCount;
	}
	public void setBcActiveCount(long bcActiveCount) {
		this.bcActiveCount = bcActiveCount;
	}
	public long getBcExpiredCount() {
		return bcExpiredCount;
	}
	public void setBcExpiredCount(long bcExpiredCount) {
		this.bcExpiredCount = bcExpiredCount;
	}
	public long getBcPendingpaymentStatus() {
		return bcPendingpaymentStatus;
	}
	public void setBcPendingpaymentStatus(long bcPendingpaymentStatus) {
		this.bcPendingpaymentStatus = bcPendingpaymentStatus;
	}
	public long getBcActivepaymentStatus() {
		return bcActivepaymentStatus;
	}
	public void setBcActivepaymentStatus(long bcActivepaymentStatus) {
		this.bcActivepaymentStatus = bcActivepaymentStatus;
	}
	public long getBcDownloadStatus() {
		return bcDownloadStatus;
	}
	public void setBcDownloadStatus(long bcDownloadStatus) {
		this.bcDownloadStatus = bcDownloadStatus;
	}
	public long getBcPendingDownloadStatus() {
		return bcPendingDownloadStatus;
	}
	public void setBcPendingDownloadStatus(long bcPendingDownloadStatus) {
		this.bcPendingDownloadStatus = bcPendingDownloadStatus;
	}
	public long getBanklinkageTotalCount() {
		return banklinkageTotalCount;
	}
	public void setBanklinkageTotalCount(long banklinkageTotalCount) {
		this.banklinkageTotalCount = banklinkageTotalCount;
	}
	public long getAuditeeTotalCount() {
		return auditeeTotalCount;
	}
	public void setAuditeeTotalCount(long auditeeTotalCount) {
		this.auditeeTotalCount = auditeeTotalCount;
	}
	public long getBcTotalCount() {
		return bcTotalCount;
	}
	public void setBcTotalCount(long bcTotalCount) {
		this.bcTotalCount = bcTotalCount;
	}
	public long getAuditorTotalCount() {
		return auditorTotalCount;
	}
	public void setAuditorTotalCount(long auditorTotalCount) {
		this.auditorTotalCount = auditorTotalCount;
	}
	public long getBcReqActiveCount() {
		return bcReqActiveCount;
	}
	public void setBcReqActiveCount(long bcReqActiveCount) {
		this.bcReqActiveCount = bcReqActiveCount;
	}
	public long getBcReqPendingCount() {
		return bcReqPendingCount;
	}
	public void setBcReqPendingCount(long bcReqPendingCount) {
		this.bcReqPendingCount = bcReqPendingCount;
	}
	public long getBcReqExpiredCount() {
		return bcReqExpiredCount;
	}
	public void setBcReqExpiredCount(long bcReqExpiredCount) {
		this.bcReqExpiredCount = bcReqExpiredCount;
	}
	public long getBcReqDownloadStatus() {
		return bcReqDownloadStatus;
	}
	public void setBcReqDownloadStatus(long bcReqDownloadStatus) {
		this.bcReqDownloadStatus = bcReqDownloadStatus;
	}
	public long getBcReqPendingDownloadStatus() {
		return bcReqPendingDownloadStatus;
	}
	public void setBcReqPendingDownloadStatus(long bcReqPendingDownloadStatus) {
		this.bcReqPendingDownloadStatus = bcReqPendingDownloadStatus;
	}
	public long getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(long activeUser) {
		this.activeUser = activeUser;
	}
	
	public long getUserTotalCount() {
		return userTotalCount;
	}
	public void setUserTotalCount(long userTotalCount) {
		this.userTotalCount = userTotalCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getBcReqTotalCount() {
		return bcReqTotalCount;
	}
	public void setBcReqTotalCount(long bcReqTotalCount) {
		this.bcReqTotalCount = bcReqTotalCount;
	}
	public long getBcReqPendingpaymentStatus() {
		return bcReqPendingpaymentStatus;
	}
	public void setBcReqPendingpaymentStatus(long bcReqPendingpaymentStatus) {
		this.bcReqPendingpaymentStatus = bcReqPendingpaymentStatus;
	}
	public long getBcReqActivepaymentStatus() {
		return bcReqActivepaymentStatus;
	}
	public void setBcReqActivepaymentStatus(long bcReqActivepaymentStatus) {
		this.bcReqActivepaymentStatus = bcReqActivepaymentStatus;
	}
	public long getDeActiveUser() {
		return deActiveUser;
	}
	public void setDeActiveUser(long deActiveUser) {
		this.deActiveUser = deActiveUser;
	}
	public long getInfoActive() {
		return infoActive;
	}
	public void setInfoActive(long infoActive) {
		this.infoActive = infoActive;
	}
	public long getInfoDeActive() {
		return infoDeActive;
	}
	public void setInfoDeActive(long infoDeActive) {
		this.infoDeActive = infoDeActive;
	}
	public long getInfoReject() {
		return infoReject;
	}
	public void setInfoReject(long infoReject) {
		this.infoReject = infoReject;
	}
	public long getInfoPending() {
		return infoPending;
	}
	public void setInfoPending(long infoPending) {
		this.infoPending = infoPending;
	}
	public long getInfohold() {
		return infohold;
	}
	public void setInfohold(long infohold) {
		this.infohold = infohold;
	}
	public long getInfoTotalCount() {
		return infoTotalCount;
	}
	public void setInfoTotalCount(long infoTotalCount) {
		this.infoTotalCount = infoTotalCount;
	}
	

	 
	 
	 
	 
	
}



