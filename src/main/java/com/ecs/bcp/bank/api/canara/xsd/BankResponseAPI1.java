package com.ecs.bcp.bank.api.canara.xsd;

public class BankResponseAPI1 {
	
	
	
	public String uniqueTxnId;
	public String pan;
	public String cin;
	public String lei;
	public String ckycId;
	private String accountsCount;
	private String bankCharges;
	private String errorCode;
	
	
	public String getUniqueTxnId() {
		return uniqueTxnId;
	}
	public void setUniqueTxnId(String uniqueTxnId) {
		this.uniqueTxnId = uniqueTxnId;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getLei() {
		return lei;
	}
	public void setLei(String lei) {
		this.lei = lei;
	}
	public String getCkycId() {
		return ckycId;
	}
	public void setCkycId(String ckycId) {
		this.ckycId = ckycId;
	}
	public String getAccountsCount() {
		return accountsCount;
	}
	public void setAccountsCount(String accountsCount) {
		this.accountsCount = accountsCount;
	}
	public String getBankCharges() {
		return bankCharges;
	}
	public void setBankCharges(String bankCharges) {
		this.bankCharges = bankCharges;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
	

}
