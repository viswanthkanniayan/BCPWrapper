package com.ecs.bcp.zoho.api;

import java.util.List;

public class ZohoResponse {

	
	private String access_token;
	private String scope;
	private String api_domain;
	private String token_type;
	private String expires_in;
	
	private List<Result> result;
	private String code;
	
	
	
	public List<Result> getResult() {
		return result;
	}
	public void setResult(List<Result> result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getApi_domain() {
		return api_domain;
	}
	public void setApi_domain(String api_domain) {
		this.api_domain = api_domain;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
	
	
}
