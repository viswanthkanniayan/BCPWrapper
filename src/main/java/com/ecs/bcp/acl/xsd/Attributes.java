package com.ecs.bcp.acl.xsd;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Attributes {

	
	  @SerializedName(":fiedl1")
	  
	  @Expose private String fiedl1;
	  
	  @SerializedName(":field2")
	  
	  @Expose private String field2;
	  
	  @SerializedName(":field3")
	  
	  @Expose private String field3;

	public String getFiedl1() {
		return fiedl1;
	}

	public void setFiedl1(String fiedl1) {
		this.fiedl1 = fiedl1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}
	  
	  
	 

/*	@SerializedName("{var1}")
	@Expose
	private String var1;
	@SerializedName("{var2}")
	@Expose
	private String var2;   */
	

	
	

}