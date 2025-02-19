package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class upiMap {
	
	@SerializedName("vpa")
	@Expose
	private String vpa;

	public String getVpa() {
		return vpa;
	}

	public void setVpa(String vpa) {
		this.vpa = vpa;
	}

	
}
