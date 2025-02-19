package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Upi {
	
	
	@SerializedName("map")
	@Expose
	private upiMap map;

	public upiMap getMap() {
		return map;
	}

	public void setMap(upiMap map) {
		this.map = map;
	}

	
	
}
