package com.ecs.bcp.acl.xsd;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "RESULT")
public class Result {

	@Attribute(name = "STATUS")
    private String status;

    @Attribute(name = "TOKENID")
    private String tokenId;

    @Attribute(name = "MSG")
    private String msg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
    
    
	
	
}
