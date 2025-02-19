package com.ecs.rezorpay.response;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transfer implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("id")
	@Expose
	public String id;
	@SerializedName("entity")
	@Expose
	public String entity;
	@SerializedName("status")
	@Expose
	public String status;
	@SerializedName("source")
	@Expose
	public String source;
	@SerializedName("recipient")
	@Expose
	public String recipient;
	@SerializedName("amount")
	@Expose
	public Integer amount;
	@SerializedName("currency")
	@Expose
	public String currency;
	@SerializedName("amount_reversed")
	@Expose
	public Integer amountReversed;
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
	@SerializedName("recipient_settlement_id")
	@Expose
	public Object recipientSettlementId;
	@SerializedName("created_at")
	@Expose
	public Integer createdAt;
	@SerializedName("processed_at")
	@Expose
	public Object processedAt;
	@SerializedName("error")
	@Expose
	public Error error;

	
	
}
