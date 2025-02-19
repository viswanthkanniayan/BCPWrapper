package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class acquirerMap {
	
	
	@SerializedName("upi_transaction_id")
	@Expose
	private String upiTransactionId;
	@SerializedName("bank_transaction_id")
	@Expose
	private String bankTransactionId;
	@SerializedName("auth_code")
	@Expose
	private String authCode;
	@SerializedName("arn")
	@Expose
	private String arn;
	@SerializedName("transaction_id")
	@Expose
	private Object transactionId;
	@SerializedName("rrn")
	@Expose
	private String rrn;
	
	
	public String getUpiTransactionId() {
		return upiTransactionId;
	}
	public void setUpiTransactionId(String upiTransactionId) {
		this.upiTransactionId = upiTransactionId;
	}
	public String getBankTransactionId() {
		return bankTransactionId;
	}
	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getArn() {
		return arn;
	}
	public void setArn(String arn) {
		this.arn = arn;
	}
	public Object getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Object transactionId) {
		this.transactionId = transactionId;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	
	
	

}
