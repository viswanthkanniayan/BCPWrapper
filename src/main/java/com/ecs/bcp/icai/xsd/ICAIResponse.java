package com.ecs.bcp.icai.xsd;

import com.google.gson.annotations.SerializedName;

public class ICAIResponse {

	
	@SerializedName("Status")
	private Status status;
	
	@SerializedName("FRN")
	private String FRN;
	
	@SerializedName("MRN")
	private String MRN;
	
	@SerializedName("UT_ID")
	private String UT_ID;
	
	@SerializedName("MRN_Details")
	private MRNDetails mrnDetails;
	
	@SerializedName("ErrorCode")
	private ErrorCode errorCode;
	
	@SerializedName("FRN_Details")
	private FRNDetails frnDetails;
	
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getFRN() {
		return FRN;
	}
	public void setFRN(String fRN) {
		FRN = fRN;
	}
	public String getMRN() {
		return MRN;
	}
	public void setMRN(String mRN) {
		MRN = mRN;
	}
	public String getUT_ID() {
		return UT_ID;
	}
	public void setUT_ID(String uT_ID) {
		UT_ID = uT_ID;
	}
	public MRNDetails getMrnDetails() {
		return mrnDetails;
	}
	public void setMrnDetails(MRNDetails mrnDetails) {
		this.mrnDetails = mrnDetails;
	}
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public FRNDetails getFrnDetails() {
		return frnDetails;
	}
	public void setFrnDetails(FRNDetails frnDetails) {
		this.frnDetails = frnDetails;
	}




}
