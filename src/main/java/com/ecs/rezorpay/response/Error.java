package com.ecs.rezorpay.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("code")
	@Expose
	private String code;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("source")
	@Expose
	private String source;
	@SerializedName("step")
	@Expose
	private String step;
	@SerializedName("reason")
	@Expose
	private String reason;
	@SerializedName("metadata")
	@Expose
	private Metadata metadata;
	
	
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
