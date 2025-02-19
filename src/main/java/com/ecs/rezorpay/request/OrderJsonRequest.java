package com.ecs.rezorpay.request;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderJsonRequest {
	
	@SerializedName("amount")
	@Expose
	public Integer amount;
	@SerializedName("payment_capture")
	@Expose
	public Integer paymentCapture;
	@SerializedName("currency")
	@Expose
	public String currency;
	@SerializedName("transfers")
	@Expose
	public List<Transfer> transfers;
	
	
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getPaymentCapture() {
		return paymentCapture;
	}
	public void setPaymentCapture(Integer paymentCapture) {
		this.paymentCapture = paymentCapture;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<Transfer> getTransfers() {
		return transfers;
	}
	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}
}
	
	