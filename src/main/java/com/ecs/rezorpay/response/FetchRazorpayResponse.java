package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchRazorpayResponse {
	
	
	@SerializedName("modelJson")
	@Expose
	private ModelJson modelJson;
	@SerializedName("CREATED_AT")
	@Expose
	private String createdAt;
	@SerializedName("CAPTURED_AT")
	@Expose
	private String capturedAt;
	
	
	public ModelJson getModelJson() {
		return modelJson;
	}
	public void setModelJson(ModelJson modelJson) {
		this.modelJson = modelJson;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getCapturedAt() {
		return capturedAt;
	}
	public void setCapturedAt(String capturedAt) {
		this.capturedAt = capturedAt;
	}
	
	

}
