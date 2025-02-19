package com.ecs.bcp.icai.xsd;

import com.google.gson.annotations.SerializedName;

public class MRNDetails {
	
	
	@SerializedName("COP STATUS")
	  private String COP_STATUS;
	
	@SerializedName("EMAIL")
      private String EMAIL;
	
	@SerializedName("MOBILE NO")
      private String MOBILE_NO;
	
	@SerializedName("NAME")
      private String NAME;
      
	public String getCOP_STATUS() {
		return COP_STATUS;
	}
	public void setCOP_STATUS(String cOP_STATUS) {
		COP_STATUS = cOP_STATUS;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getMOBILE_NO() {
		return MOBILE_NO;
	}
	public void setMOBILE_NO(String mOBILE_NO) {
		MOBILE_NO = mOBILE_NO;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
      
      

}
