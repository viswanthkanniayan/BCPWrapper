package com.ecs.rezorpay.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {
	
	@SerializedName("branch")
	@Expose
	public String branch;
	
	@SerializedName("name")
	@Expose
	public String name;
	
	
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
