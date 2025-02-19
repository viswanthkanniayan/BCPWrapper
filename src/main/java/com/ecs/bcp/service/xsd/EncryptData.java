package com.ecs.bcp.service.xsd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EncryptData {
	
	
/*	@SerializedName("uniqueTxnId")
	@Expose
	private String uniqueTxnId;
	@SerializedName("pan")
	@Expose
	private String pan;
	@SerializedName("document")
	@Expose
	private String document;
	@SerializedName("bank")
	@Expose
	private String bank;
	@SerializedName("reqType")
	@Expose
	private String reqType;  */
	
	
	@SerializedName("uniqueTxnId")
	@Expose
	private String uniqueTxnId;
	@SerializedName("reqName")
	@Expose
	private String reqName;
	@SerializedName("reqID")
	@Expose
	private String reqID;
	@SerializedName("reqEntity")
	@Expose
	private String reqEntity;
	@SerializedName("reqEntityID")
	@Expose
	private String reqEntityID;
	@SerializedName("reqEmail")
	@Expose
	private String reqEmail;
	@SerializedName("reqMob")
	@Expose
	private String reqMob;
	@SerializedName("purpose")
	@Expose
	private String purpose;
	@SerializedName("reqType")
	@Expose
	private String reqType;
	@SerializedName("FromDate")
	@Expose
	private String fromDate;
	@SerializedName("ToDate")
	@Expose
	private String toDate;
	@SerializedName("balanceCertificatePDF")
	@Expose
	private String balanceCertificatePDF;
	@SerializedName("bank")
	@Expose
	private String bank;
	@SerializedName("auditeeDetails")
	@Expose
	private AuditeeDetails auditeeDetails;
	@SerializedName("account sandfacilities Details")
	@Expose
	private AccountsandfacilitiesDetails accountsandfacilitiesDetails;
	
	
	
	public String getUniqueTxnId() {
		return uniqueTxnId;
	}
	public void setUniqueTxnId(String uniqueTxnId) {
		this.uniqueTxnId = uniqueTxnId;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getReqID() {
		return reqID;
	}
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	public String getReqEntity() {
		return reqEntity;
	}
	public void setReqEntity(String reqEntity) {
		this.reqEntity = reqEntity;
	}
	public String getReqEntityID() {
		return reqEntityID;
	}
	public void setReqEntityID(String reqEntityID) {
		this.reqEntityID = reqEntityID;
	}
	public String getReqEmail() {
		return reqEmail;
	}
	public void setReqEmail(String reqEmail) {
		this.reqEmail = reqEmail;
	}
	public String getReqMob() {
		return reqMob;
	}
	public void setReqMob(String reqMob) {
		this.reqMob = reqMob;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
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
	public String getBalanceCertificatePDF() {
		return balanceCertificatePDF;
	}
	public void setBalanceCertificatePDF(String balanceCertificatePDF) {
		this.balanceCertificatePDF = balanceCertificatePDF;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public AuditeeDetails getAuditeeDetails() {
		return auditeeDetails;
	}
	public void setAuditeeDetails(AuditeeDetails auditeeDetails) {
		this.auditeeDetails = auditeeDetails;
	}
	public AccountsandfacilitiesDetails getAccountsandfacilitiesDetails() {
		return accountsandfacilitiesDetails;
	}
	public void setAccountsandfacilitiesDetails(AccountsandfacilitiesDetails accountsandfacilitiesDetails) {
		this.accountsandfacilitiesDetails = accountsandfacilitiesDetails;
	}
	
	
	
	

	
	
}
