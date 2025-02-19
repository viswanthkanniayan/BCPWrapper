package com.ecs.bcp.xsd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class PANResponse implements Serializable{

	private String pan;
	private String name;
	private String status;
	private String nameOnCardStatus;
	private String dobStatus;
	private String nameStatus;
	private String aadhaarSeedingStatus;
	private String nsdlResponse;

	private boolean err;
	private String errCode;
	private String errMsg;
    private String provider;




	
	public String getProvider() {
	return provider;
    }
    public void setProvider(String provider) {
	this.provider = provider;
    }
	public boolean isErr() {
		return err;
	}
	public void setErr(boolean err) {
		this.err = err;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNameOnCardStatus() {
		return nameOnCardStatus;
	}
	public void setNameOnCardStatus(String nameOnCardStatus) {
		this.nameOnCardStatus = nameOnCardStatus;
	}
	public String getDobStatus() {
		return dobStatus;
	}
	public void setDobStatus(String dobStatus) {
		this.dobStatus = dobStatus;
	}
	public String getNameStatus() {
		return nameStatus;
	}
	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}
	public String getAadhaarSeedingStatus() {
		return aadhaarSeedingStatus;
	}
	public void setAadhaarSeedingStatus(String aadhaarSeedingStatus) {
		this.aadhaarSeedingStatus = aadhaarSeedingStatus;
	}
	public String getNsdlResponse() {
		return nsdlResponse;
	}
	public void setNsdlResponse(String nsdlResponse) {
		this.nsdlResponse = nsdlResponse;
	}



}
