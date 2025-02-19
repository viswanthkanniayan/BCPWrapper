package com.ecs.bcp.service.xsd;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

	    @SerializedName("encryptData")
	    @Expose
	    private String encryptData; 
	    
	    public String getEncryptData() {
	        return encryptData;
	    }

	    public void setEncryptData(String encryptData) {
	        this.encryptData = encryptData;
	    }
	    
	    public EncryptData getParsedEncryptData() {
	        return new Gson().fromJson(encryptData, EncryptData.class);
	    }
	
	
}
