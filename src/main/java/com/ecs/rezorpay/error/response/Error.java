package com.ecs.rezorpay.error.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {
	
	@SerializedName("code")
	@Expose
	public String code;
	@SerializedName("description")
	@Expose
	public String description;
	@SerializedName("source")
	@Expose
	public String source;
	@SerializedName("step")
	@Expose
	public String step;
	@SerializedName("reason")
	@Expose
	public String reason;
	@SerializedName("metadata")
	@Expose
	public Metadata metadata;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	

}
