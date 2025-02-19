package com.ecs.bcp.acl.xsd;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ACLEmailRequestXsd {

	@SerializedName("subject")
	@Expose
	private String subject;
	@SerializedName("from")
	@Expose
	private From from;
	@SerializedName("reply_to")
	@Expose
	private ReplyTo replyTo;
	@SerializedName("recipients")
	@Expose
	private List<Recipient> recipients;
	@SerializedName("template_id")
	@Expose
	private String templateId;
	@SerializedName("headers")
	@Expose
	private Headers headers;
	
	@SerializedName("content")
	@Expose
	private Content content;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public ReplyTo getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(ReplyTo replyTo) {
		this.replyTo = replyTo;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}
