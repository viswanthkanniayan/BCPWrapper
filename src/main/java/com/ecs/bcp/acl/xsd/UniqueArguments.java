package com.ecs.bcp.acl.xsd;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UniqueArguments {

	@SerializedName("x-apiheader")
	@Expose
	private String xApiheader;

	public String getxApiheader() {
		return xApiheader;
	}

	public void setxApiheader(String xApiheader) {
		this.xApiheader = xApiheader;
	}

}