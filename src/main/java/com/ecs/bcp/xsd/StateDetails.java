package com.ecs.bcp.xsd;

import java.io.Serializable;

public class StateDetails implements Serializable {

	
	private int id;
	private String state;
	private String stateCode;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	
	
}
