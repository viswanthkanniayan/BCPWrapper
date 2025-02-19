package com.ecs.bcp.service.xsd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallBackResponse {

	@SerializedName("Request")
	@Expose
	public Request request;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	
	
}
