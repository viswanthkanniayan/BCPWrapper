package com.ecs.bcp.xsd;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data {

	@SerializedName("pan")
	@Expose
	private String pan;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("middleName")
	@Expose
	private String middleName;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("nameOnCard")
	@Expose
	private String nameOnCard;
	@SerializedName("lastUpdatedDate")
	@Expose
	private String lastUpdatedDate;
	@SerializedName("aadhaarSeedingStatus")
	@Expose
	private String aadhaarSeedingStatus;

	@SerializedName("nameOnCardStatus")
	@Expose
	private String nameOnCardStatus;

	@SerializedName("dobStatus")
	@Expose
	private String dobStatus;

	@SerializedName("nameStatus")
	@Expose
	private String nameStatus;


	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getAadhaarSeedingStatus() {
		return aadhaarSeedingStatus;
	}

	public void setAadhaarSeedingStatus(String aadhaarSeedingStatus) {
		this.aadhaarSeedingStatus = aadhaarSeedingStatus;
	}

	public String getNameOnCardStatus() {
		return nameOnCardStatus;
	}

	public void setNameOnCardStatus(String nameOnCardStatus) {
		this.nameOnCardStatus = nameOnCardStatus;
	}

	public String getDobStatus() {
		return dobStatus;
	}

	public void setDobStatus(String dobStatus) {
		this.dobStatus = dobStatus;
	}

	public String getNameStatus() {
		return nameStatus;
	}

	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}
	
	

}
