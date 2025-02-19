package com.ecs.bcp.acl.xsd;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Recipient {

	@SerializedName("to")
	@Expose
	private List<To> to;
	@SerializedName("attributes")
	@Expose
	private Attributes attributes;
	@SerializedName("unique_arguments")
	@Expose
	private UniqueArguments uniqueArguments;

	public List<To> getTo() {
		return to;
	}

	public void setTo(List<To> to) {
		this.to = to;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public UniqueArguments getUniqueArguments() {
		return uniqueArguments;
	}

	public void setUniqueArguments(UniqueArguments uniqueArguments) {
		this.uniqueArguments = uniqueArguments;
	}

}