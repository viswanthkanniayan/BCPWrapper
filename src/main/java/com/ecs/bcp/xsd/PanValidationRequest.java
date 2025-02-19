package com.ecs.bcp.xsd;

import java.io.Serializable;

public class PanValidationRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String panNo;
	private String txnType;
	private String loggedInUser;
	private String nameOnPAN;
	private String nameAsPerIT;
	private String dob;

	private String subscriptionKey;
	private String pan;
	private String firstName;
	private String middleName;
	private String lastName;
	private String name;
	


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubscriptionKey() {
		return subscriptionKey;
	}
	public void setSubscriptionKey(String subscriptionKey) {
		this.subscriptionKey = subscriptionKey;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNameOnPAN() {
		return nameOnPAN;
	}
	public void setNameOnPAN(String nameOnPAN) {
		this.nameOnPAN = nameOnPAN;
	}
	public String getNameAsPerIT() {
		return nameAsPerIT;
	}
	public void setNameAsPerIT(String nameAsPerIT) {
		this.nameAsPerIT = nameAsPerIT;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}


}
