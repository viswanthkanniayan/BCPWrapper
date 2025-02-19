package com.ecs.bcp.icai.xsd;

import com.google.gson.annotations.SerializedName;

public class ErrorCode {
	
	@SerializedName("E00")
	 private String E00;
	
	@SerializedName("E02")
     private String E02;
	
	@SerializedName("E01")
     private String E01;
     
     
	public String getE00() {
		return E00;
	}
	public void setE00(String e00) {
		E00 = e00;
	}
	public String getE02() {
		return E02;
	}
	public void setE02(String e02) {
		E02 = e02;
	}
	public String getE01() {
		return E01;
	}
	public void setE01(String e01) {
		E01 = e01;
	}

     

}
