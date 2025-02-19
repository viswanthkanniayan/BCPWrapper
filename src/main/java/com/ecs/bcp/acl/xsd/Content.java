package com.ecs.bcp.acl.xsd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {
	
	
	@SerializedName("text")
	@Expose
	private String text;
	@SerializedName("html")
	@Expose
	private String html;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	

}
