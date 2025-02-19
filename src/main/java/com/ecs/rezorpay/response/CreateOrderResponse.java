package com.ecs.rezorpay.response;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {;
	
	@SerializedName("id")
	@Expose
	public String id;
	
	@SerializedName("entity")
	@Expose
	public String entity;
	
	@SerializedName("amount")
	@Expose
	public Integer amount;
	
	@SerializedName("amount_paid")
	@Expose
	public Integer amountPaid;
	
	@SerializedName("amount_due")
	@Expose
	public Integer amountDue;
	
	@SerializedName("currency")
	@Expose
	public String currency;
	
	@SerializedName("receipt")
	@Expose
	public Object receipt;
	
	@SerializedName("offer_id")
	@Expose
	public Object offerId;
	
	@SerializedName("offers")
	@Expose
	public Offers offers;
	
	@SerializedName("status")
	@Expose
	public String status;
	
	@SerializedName("attempts")
	@Expose
	public Integer attempts;
	
	@SerializedName("notes")
	@Expose
	public List<Object> notes;
	
	@SerializedName("created_at")
	@Expose
	public Integer createdAt;
	
	@SerializedName("transfers")
	@Expose
	public List<Transfer> transfers;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(Integer amountPaid) {
		this.amountPaid = amountPaid;
	}
	public Integer getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(Integer amountDue) {
		this.amountDue = amountDue;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Object getReceipt() {
		return receipt;
	}
	public void setReceipt(Object receipt) {
		this.receipt = receipt;
	}
	public Object getOfferId() {
		return offerId;
	}
	public void setOfferId(Object offerId) {
		this.offerId = offerId;
	}
	public Offers getOffers() {
		return offers;
	}
	public void setOffers(Offers offers) {
		this.offers = offers;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public List<Object> getNotes() {
		return notes;
	}
	public void setNotes(List<Object> notes) {
		this.notes = notes;
	}
	public Integer getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}
	public List<Transfer> getTransfers() {
		return transfers;
	}
	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}

	
}
