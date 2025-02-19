package com.ecs.bcp.xsd;

import java.io.Serializable;

public class UpdatePasswordRequestXsd  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String emailId;
	private String password;
	private String txnType;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
