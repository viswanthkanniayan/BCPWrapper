package com.ecs.rezorpay.request;

import java.io.Serializable;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transfer implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@SerializedName("account")
	@Expose
	public String account;
	
	@SerializedName("amount")
	@Expose
	public Integer amount;
	
	@SerializedName("currency")
	@Expose
	public String currency;
	
	@SerializedName("notes")
	@Expose
	public Notes notes;
	
	@SerializedName("linked_account_notes")
	@Expose
	public List<String> linkedAccountNotes;
	
	@SerializedName("on_hold")
	@Expose
	public Boolean onHold;
	
	@SerializedName("on_hold_until")
	@Expose
	public Object onHoldUntil;


	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Notes getNotes() {
		return notes;
	}
	public void setNotes(Notes notes) {
		this.notes = notes;
	}
	public List<String> getLinkedAccountNotes() {
		return linkedAccountNotes;
	}
	public void setLinkedAccountNotes(List<String> linkedAccountNotes) {
		this.linkedAccountNotes = linkedAccountNotes;
	}
	public Boolean getOnHold() {
		return onHold;
	}
	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}
	public Object getOnHoldUntil() {
		return onHoldUntil;
	}
	public void setOnHoldUntil(Object onHoldUntil) {
		this.onHoldUntil = onHoldUntil;
	}

}
