package com.ecs.bcp.xsd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PANResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PanValidationResponse implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "err", required=true)
    protected boolean err;

    @XmlAttribute(name = "errCode", required=false)
    protected String errCode;

    @XmlAttribute(name = "errMsg", required=false)
    protected String errMsg;

    @XmlAttribute(name = "provider", required=false)
    protected String provider;

    @XmlAttribute(name = "name", required=false)
    protected String name;
    
    @XmlAttribute(name = "status", required=false)
    protected String status;
    
    @XmlAttribute(name = "token", required=false)
    protected String token;


	public boolean isErr() {
		return err;
	}

	public void setErr(boolean err) {
		this.err = err;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}