package com.ecs.rezorpay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcquirerData {
	
	
	@SerializedName("map")
	@Expose
	private acquirerMap map;

	public acquirerMap getMap() {
		return map;
	}

	public void setMap(acquirerMap map) {
		this.map = map;
	}

	
	
}
