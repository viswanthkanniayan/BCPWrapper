package com.ecs.bcp.icai.xsd;

import com.google.gson.annotations.SerializedName;

public class Status {
	
	
	@SerializedName("FRN")
	 private String FRN;
	
	@SerializedName("MRN")
     private String MRN;
	
	@SerializedName("MRN_FRN_MAPPING")
     private String MRN_FRN_MAPPING;
     
     
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
	public String getMRN_FRN_MAPPING() {
		return MRN_FRN_MAPPING;
	}
	public void setMRN_FRN_MAPPING(String mRN_FRN_MAPPING) {
		MRN_FRN_MAPPING = mRN_FRN_MAPPING;
	}
     
     

}
