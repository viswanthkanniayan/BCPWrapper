package com.ecs.bcp.xsd;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ECSPANFailureResponse {

	@SerializedName("responseId")
	@Expose
	private String responseId;
	@SerializedName("hasError")
	@Expose
	private Boolean hasError;
	@SerializedName("errorCode")
	@Expose
	private String errorCode;
	@SerializedName("errorMessage")
	@Expose
	private String errorMessage;

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public Boolean getHasError() {
		return hasError;
	}

	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}