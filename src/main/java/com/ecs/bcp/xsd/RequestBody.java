package com.ecs.bcp.xsd;

import java.io.Serializable;

public class RequestBody implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String txnType;

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	
}
