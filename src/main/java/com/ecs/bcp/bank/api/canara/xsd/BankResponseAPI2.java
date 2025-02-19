package com.ecs.bcp.bank.api.canara.xsd;

public class BankResponseAPI2 {
	
	
/*     {
		"uniqueTxnId": "your_unique_transaction_id_here",
		"ackStatus": "accepted/rejected",
		"bank": "name of the bank"
		}                      */
	
	
	public String uniqueTxnId;
	public String ackStatus;
	public String bank;
	
	
	public String getUniqueTxnId() {
		return uniqueTxnId;
	}
	public void setUniqueTxnId(String uniqueTxnId) {
		this.uniqueTxnId = uniqueTxnId;
	}
	public String getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(String ackStatus) {
		this.ackStatus = ackStatus;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	
	
	
}
