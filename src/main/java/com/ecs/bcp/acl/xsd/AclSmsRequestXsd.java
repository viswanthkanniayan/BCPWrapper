package com.ecs.bcp.acl.xsd;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AclSmsRequestXsd {

@SerializedName("enterpriseid")
@Expose
private String enterpriseid;
@SerializedName("subEnterpriseid")
@Expose
private String subEnterpriseid;
@SerializedName("pusheid")
@Expose
private String pusheid;
@SerializedName("pushepwd")
@Expose
private String pushepwd;
@SerializedName("contenttype")
@Expose
private String contenttype;
@SerializedName("sender")
@Expose
private String sender;
@SerializedName("alert")
@Expose
private String alert;
@SerializedName("msisdn")
@Expose
private String msisdn;
@SerializedName("intflag")
@Expose
private String intflag;
@SerializedName("language")
@Expose
private String language;
@SerializedName("msgtext")
@Expose
private String msgtext;
@SerializedName("dpi")
@Expose
private String dpi;
@SerializedName("dtm")
@Expose
private String dtm;
@SerializedName("msgid")
@Expose
private String msgid;


public String getMsgid() {
	return msgid;
}

public void setMsgid(String msgid) {
	this.msgid = msgid;
}

public String getEnterpriseid() {
return enterpriseid;
}

public void setEnterpriseid(String enterpriseid) {
this.enterpriseid = enterpriseid;
}

public String getSubEnterpriseid() {
return subEnterpriseid;
}

public void setSubEnterpriseid(String subEnterpriseid) {
this.subEnterpriseid = subEnterpriseid;
}

public String getPusheid() {
return pusheid;
}

public void setPusheid(String pusheid) {
this.pusheid = pusheid;
}

public String getPushepwd() {
return pushepwd;
}

public void setPushepwd(String pushepwd) {
this.pushepwd = pushepwd;
}

public String getContenttype() {
return contenttype;
}

public void setContenttype(String contenttype) {
this.contenttype = contenttype;
}

public String getSender() {
return sender;
}

public void setSender(String sender) {
this.sender = sender;
}

public String getAlert() {
return alert;
}

public void setAlert(String alert) {
this.alert = alert;
}

public String getMsisdn() {
return msisdn;
}

public void setMsisdn(String msisdn) {
this.msisdn = msisdn;
}

public String getIntflag() {
return intflag;
}

public void setIntflag(String intflag) {
this.intflag = intflag;
}

public String getLanguage() {
return language;
}

public void setLanguage(String language) {
this.language = language;
}

public String getMsgtext() {
return msgtext;
}

public void setMsgtext(String msgtext) {
this.msgtext = msgtext;
}

public String getDpi() {
return dpi;
}

public void setDpi(String dpi) {
this.dpi = dpi;
}

public String getDtm() {
return dtm;
}

public void setDtm(String dtm) {
this.dtm = dtm;
}

}