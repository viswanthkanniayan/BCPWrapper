package com.ecs.bcp.icai.xsd;

import com.google.gson.annotations.SerializedName;

public class FRNDetails {
	
	
	@SerializedName("EMAIL")
	  private String EMAIL;
	
	@SerializedName("FIRM NAME")
      private String FIRM_NAME;
	
	@SerializedName("FRN")
      private String FRN;
      
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getFIRM_NAME() {
		return FIRM_NAME;
	}
	public void setFIRM_NAME(String fIRM_NAME) {
		FIRM_NAME = fIRM_NAME;
	}
	public String getFRN() {
		return FRN;
	}
	public void setFRN(String fRN) {
		FRN = fRN;
	}
      
      

}
