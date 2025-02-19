package com.ecs.bcp.acl.xsd;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Headers {

	@SerializedName("X-EXAMPLE")
	@Expose
	private String xExample;

	public String getXExample() {
		return xExample;
	}

	public void setXExample(String xExample) {
		this.xExample = xExample;
	}

}