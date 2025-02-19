package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map {
	
	
	@SerializedName("notes")
	@Expose
	private Notes notes;
	@SerializedName("fee")
	@Expose
	private Integer fee;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("created_at")
	@Expose
	private Integer createdAt;
	@SerializedName("captured_at")
	@Expose
	private Integer capturedAt;
	@SerializedName("authorized_at")
	@Expose
	private Integer authorizedAt;
	@SerializedName("amount_refunded")
	@Expose
	private Integer amountRefunded;
	@SerializedName("bank")
	@Expose
	private Bank bank;
	@SerializedName("error_reason")
	@Expose
	private ErrorReason errorReason;
	@SerializedName("auto_captured")
	@Expose
	private Boolean autoCaptured;
	@SerializedName("error_description")
	@Expose
	private ErrorDescription errorDescription;
	@SerializedName("acquirer_data")
	@Expose
	private AcquirerData acquirerData;
	@SerializedName("captured")
	@Expose
	private Boolean captured;
	@SerializedName("contact")
	@Expose
	private String contact;
	@SerializedName("invoice_id")
	@Expose
	private InvoiceId invoiceId;
	@SerializedName("currency")
	@Expose
	private String currency;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("international")
	@Expose
	private Boolean international;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("amount")
	@Expose
	private Integer amount;
	@SerializedName("refund_status")
	@Expose
	private RefundStatus refundStatus;
	@SerializedName("wallet")
	@Expose
	private Wallet wallet;
	@SerializedName("method")
	@Expose
	private String method;
	@SerializedName("vpa")
	@Expose
	private String vpa;
	@SerializedName("error_source")
	@Expose
	private ErrorSource errorSource;
	@SerializedName("error_step")
	@Expose
	private ErrorStep errorStep;
	@SerializedName("tax")
	@Expose
	private Integer tax;
	@SerializedName("card_id")
	@Expose
	private CardId cardId;
	@SerializedName("late_authorized")
	@Expose
	private Boolean lateAuthorized;
	@SerializedName("upi")
	@Expose
	private Upi upi;
	@SerializedName("error_code")
	@Expose
	private ErrorCode errorCode;
	@SerializedName("order_id")
	@Expose
	private String orderId;
	@SerializedName("entity")
	@Expose
	private String entity;
	@SerializedName("status")
	@Expose
	private String status;
	
	@SerializedName("customer_id")
	@Expose
	private String customerId;
	
	@SerializedName("token_id")
	@Expose
	private String tokenId;
	
	
	
	
	public Notes getNotes() {
		return notes;
	}
	public void setNotes(Notes notes) {
		this.notes = notes;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getCapturedAt() {
		return capturedAt;
	}
	public void setCapturedAt(Integer capturedAt) {
		this.capturedAt = capturedAt;
	}
	public Integer getAuthorizedAt() {
		return authorizedAt;
	}
	public void setAuthorizedAt(Integer authorizedAt) {
		this.authorizedAt = authorizedAt;
	}
	public Integer getAmountRefunded() {
		return amountRefunded;
	}
	public void setAmountRefunded(Integer amountRefunded) {
		this.amountRefunded = amountRefunded;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public ErrorReason getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(ErrorReason errorReason) {
		this.errorReason = errorReason;
	}
	public Boolean getAutoCaptured() {
		return autoCaptured;
	}
	public void setAutoCaptured(Boolean autoCaptured) {
		this.autoCaptured = autoCaptured;
	}
	public ErrorDescription getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(ErrorDescription errorDescription) {
		this.errorDescription = errorDescription;
	}
	public AcquirerData getAcquirerData() {
		return acquirerData;
	}
	public void setAcquirerData(AcquirerData acquirerData) {
		this.acquirerData = acquirerData;
	}
	public Boolean getCaptured() {
		return captured;
	}
	public void setCaptured(Boolean captured) {
		this.captured = captured;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public InvoiceId getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(InvoiceId invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getInternational() {
		return international;
	}
	public void setInternational(Boolean international) {
		this.international = international;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public RefundStatus getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(RefundStatus refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getVpa() {
		return vpa;
	}
	public void setVpa(String vpa) {
		this.vpa = vpa;
	}
	public ErrorSource getErrorSource() {
		return errorSource;
	}
	public void setErrorSource(ErrorSource errorSource) {
		this.errorSource = errorSource;
	}
	public ErrorStep getErrorStep() {
		return errorStep;
	}
	public void setErrorStep(ErrorStep errorStep) {
		this.errorStep = errorStep;
	}
	public Integer getTax() {
		return tax;
	}
	public void setTax(Integer tax) {
		this.tax = tax;
	}
	public CardId getCardId() {
		return cardId;
	}
	public void setCardId(CardId cardId) {
		this.cardId = cardId;
	}
	public Boolean getLateAuthorized() {
		return lateAuthorized;
	}
	public void setLateAuthorized(Boolean lateAuthorized) {
		this.lateAuthorized = lateAuthorized;
	}
	public Upi getUpi() {
		return upi;
	}
	public void setUpi(Upi upi) {
		this.upi = upi;
	}
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	
	
	

}
