package com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor;

public class CertData {
	String cn = null;
	String houseIdentifier = null;
	String street = null;
	String locality = null;
	String state = null;
	String postalCode = null;
	String organisation = null;
	String country = null;
	String title = null;
	
	String location = null;
	String reason = null;
	String signingTime = null;
	
	public CertData()
	{
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getHouseIdentifier() {
		return houseIdentifier;
	}

	public void setHouseIdentifier(String houseIdentifier) {
		this.houseIdentifier = houseIdentifier;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSigningTime() {
		return signingTime;
	}

	public void setSigningTime(String signingTime) {
		this.signingTime = signingTime;
	}
	
}
