package com.ecs.bcp.bank.api.canara.xsd;

public class BankOtpResponse {
	
	
	/*
	 * { "uniqueTxnRefNo": "your_unique_transaction_id_here", "bankRefNo":
	 * "bank_reference_number", "bankAction": "optional_action_here", "errorCode":
	 * "optional_error_code_here" }
	 */

	
	/*
	 * { "uniqueTxnRefNo": "your_unique_transaction_id_here", "bankRefNo":
	 * "bank_reference_number", "bankLinkageStatus": "bank_linkage_statuse",
	 * "errorCode": "optional_error_code_here" }
	 */
	
	public String uniqueTxnRefNo;
	public String bankRefNo;
	public String bankAction;
	public String bankLinkageStatus;
	public String errorCode;
	
	
	
	public String getUniqueTxnRefNo() {
		return uniqueTxnRefNo;
	}
	public void setUniqueTxnRefNo(String uniqueTxnRefNo) {
		this.uniqueTxnRefNo = uniqueTxnRefNo;
	}
	public String getBankRefNo() {
		return bankRefNo;
	}
	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}
	public String getBankAction() {
		return bankAction;
	}
	public void setBankAction(String bankAction) {
		this.bankAction = bankAction;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getBankLinkageStatus() {
		return bankLinkageStatus;
	}
	public void setBankLinkageStatus(String bankLinkageStatus) {
		this.bankLinkageStatus = bankLinkageStatus;
	}
	
	
	
	

}
