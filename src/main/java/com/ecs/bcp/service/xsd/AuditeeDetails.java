package com.ecs.bcp.service.xsd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditeeDetails {
	
	@SerializedName("auditeeCompany")
	@Expose
	private String auditeeCompany;
	@SerializedName("auditeAddr")
	@Expose
	private String auditeAddr;
	@SerializedName("auditeePan")
	@Expose
	private String auditeePan;
	@SerializedName("auditeeLegalId")
	@Expose
	private String auditeeLegalId;
	@SerializedName("auditeeCkycId")
	@Expose
	private String auditeeCkycId;
	@SerializedName("TotalCountRec")
	@Expose
	private String totalCountRec;
	
	
	public String getAuditeeCompany() {
		return auditeeCompany;
	}
	public void setAuditeeCompany(String auditeeCompany) {
		this.auditeeCompany = auditeeCompany;
	}
	public String getAuditeAddr() {
		return auditeAddr;
	}
	public void setAuditeAddr(String auditeAddr) {
		this.auditeAddr = auditeAddr;
	}

	public String getAuditeePan() {
		return auditeePan;
	}
	public void setAuditeePan(String auditeePan) {
		this.auditeePan = auditeePan;
	}
	public String getAuditeeLegalId() {
		return auditeeLegalId;
	}
	public void setAuditeeLegalId(String auditeeLegalId) {
		this.auditeeLegalId = auditeeLegalId;
	}
	public String getAuditeeCkycId() {
		return auditeeCkycId;
	}
	public void setAuditeeCkycId(String auditeeCkycId) {
		this.auditeeCkycId = auditeeCkycId;
	}
	public String getTotalCountRec() {
		return totalCountRec;
	}
	public void setTotalCountRec(String totalCountRec) {
		this.totalCountRec = totalCountRec;
	}
	
	
	

}
