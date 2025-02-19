package com.ecs.bcp.xsd;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class PANValidationLogsPojo implements Serializable{

	private long id;

	private String userName;
	private Date creationDate;
	private String panNo;
	private String status;
	private String errCode;
	private String errDescription;
	private String token;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrDescription() {
		return errDescription;
	}
	public void setErrDescription(String errDescription) {
		this.errDescription = errDescription;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	

}
