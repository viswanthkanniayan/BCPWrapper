package com.ecs.bcp.service.xsd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountsandfacilitiesDetails {
	
	
	@SerializedName("count_of_active_acs")
	@Expose
	private String countOfActiveAcs;
	@SerializedName("total_amount_INR_of_active_acs")
	@Expose
	private String totalAmountINROfActiveAcs;
	@SerializedName("counts_of_non_active_acs")
	@Expose
	private String countsOfNonActiveAcs;
	@SerializedName("total_amount_INR_of_non_active_acs")
	@Expose
	private String totalAmountINROfNonActiveAcs;
	@SerializedName("count_of_funded_active_facilities")
	@Expose
	private String countOfFundedActiveFacilities;
	@SerializedName("total_amount_INR_funded_active_facilities")
	@Expose
	private String totalAmountINRFundedActiveFacilities;
	@SerializedName("count_of_non_funded_active_facilities")
	@Expose
	private String countOfNonFundedActiveFacilities;
	@SerializedName("total_amount_INR_non_funded_active_facilities")
	@Expose
	private String totalAmountINRNonFundedActiveFacilities;
	@SerializedName("count_of_funded_non_active_facilities")
	@Expose
	private String countOfFundedNonActiveFacilities;
	@SerializedName("total_amount_INR_funded_non_active_facilities")
	@Expose
	private String totalAmountINRFundedNonActiveFacilities;
	@SerializedName("count_of_non_funded_non_active_facilities")
	@Expose
	private String countOfNonFundedNonActiveFacilities;
	@SerializedName("total_amount_INR_non_funded_non_active_facilities")
	@Expose
	private String totalAmountINRNonFundedNonActiveFacilities;
	@SerializedName("counts_of_security_details")
	@Expose
	private String countsOfSecurityDetails;
	@SerializedName("Count_of_default_irregular_acs")
	@Expose
	private String countOfDefaultIrregularAcs;
	@SerializedName("total_outstanding_INR_default_irregular_acs")
	@Expose
	private String totalOutstandingINRDefaultIrregularAcs;
	@SerializedName("total_principal_INR_default_irregular_acs")
	@Expose
	private String totalPrincipalINRDefaultIrregularAcs;
	@SerializedName("total_Interest_INR_default_irregular_accounts")
	@Expose
	private String totalInterestINRDefaultIrregularAccounts;
	@SerializedName("Count_of_MTM_Derivatives_Acs")
	@Expose
	private String countOfMTMDerivativesAcs;
	@SerializedName("Total_ContraValueINR_MTM_Derivatives")
	@Expose
	private String totalContraValueINRMTMDerivatives;
	@SerializedName("is_wilful_defaulter")
	@Expose
	private String isWilfulDefaulter;
	
	
	
	
	public String getCountOfActiveAcs() {
		return countOfActiveAcs;
	}
	public void setCountOfActiveAcs(String countOfActiveAcs) {
		this.countOfActiveAcs = countOfActiveAcs;
	}
	public String getTotalAmountINROfActiveAcs() {
		return totalAmountINROfActiveAcs;
	}
	public void setTotalAmountINROfActiveAcs(String totalAmountINROfActiveAcs) {
		this.totalAmountINROfActiveAcs = totalAmountINROfActiveAcs;
	}
	public String getCountsOfNonActiveAcs() {
		return countsOfNonActiveAcs;
	}
	public void setCountsOfNonActiveAcs(String countsOfNonActiveAcs) {
		this.countsOfNonActiveAcs = countsOfNonActiveAcs;
	}
	public String getTotalAmountINROfNonActiveAcs() {
		return totalAmountINROfNonActiveAcs;
	}
	public void setTotalAmountINROfNonActiveAcs(String totalAmountINROfNonActiveAcs) {
		this.totalAmountINROfNonActiveAcs = totalAmountINROfNonActiveAcs;
	}
	public String getCountOfFundedActiveFacilities() {
		return countOfFundedActiveFacilities;
	}
	public void setCountOfFundedActiveFacilities(String countOfFundedActiveFacilities) {
		this.countOfFundedActiveFacilities = countOfFundedActiveFacilities;
	}
	public String getTotalAmountINRFundedActiveFacilities() {
		return totalAmountINRFundedActiveFacilities;
	}
	public void setTotalAmountINRFundedActiveFacilities(String totalAmountINRFundedActiveFacilities) {
		this.totalAmountINRFundedActiveFacilities = totalAmountINRFundedActiveFacilities;
	}
	public String getCountOfNonFundedActiveFacilities() {
		return countOfNonFundedActiveFacilities;
	}
	public void setCountOfNonFundedActiveFacilities(String countOfNonFundedActiveFacilities) {
		this.countOfNonFundedActiveFacilities = countOfNonFundedActiveFacilities;
	}
	public String getTotalAmountINRNonFundedActiveFacilities() {
		return totalAmountINRNonFundedActiveFacilities;
	}
	public void setTotalAmountINRNonFundedActiveFacilities(String totalAmountINRNonFundedActiveFacilities) {
		this.totalAmountINRNonFundedActiveFacilities = totalAmountINRNonFundedActiveFacilities;
	}
	public String getCountOfFundedNonActiveFacilities() {
		return countOfFundedNonActiveFacilities;
	}
	public void setCountOfFundedNonActiveFacilities(String countOfFundedNonActiveFacilities) {
		this.countOfFundedNonActiveFacilities = countOfFundedNonActiveFacilities;
	}
	public String getTotalAmountINRFundedNonActiveFacilities() {
		return totalAmountINRFundedNonActiveFacilities;
	}
	public void setTotalAmountINRFundedNonActiveFacilities(String totalAmountINRFundedNonActiveFacilities) {
		this.totalAmountINRFundedNonActiveFacilities = totalAmountINRFundedNonActiveFacilities;
	}
	public String getCountOfNonFundedNonActiveFacilities() {
		return countOfNonFundedNonActiveFacilities;
	}
	public void setCountOfNonFundedNonActiveFacilities(String countOfNonFundedNonActiveFacilities) {
		this.countOfNonFundedNonActiveFacilities = countOfNonFundedNonActiveFacilities;
	}
	public String getTotalAmountINRNonFundedNonActiveFacilities() {
		return totalAmountINRNonFundedNonActiveFacilities;
	}
	public void setTotalAmountINRNonFundedNonActiveFacilities(String totalAmountINRNonFundedNonActiveFacilities) {
		this.totalAmountINRNonFundedNonActiveFacilities = totalAmountINRNonFundedNonActiveFacilities;
	}
	public String getCountsOfSecurityDetails() {
		return countsOfSecurityDetails;
	}
	public void setCountsOfSecurityDetails(String countsOfSecurityDetails) {
		this.countsOfSecurityDetails = countsOfSecurityDetails;
	}
	public String getCountOfDefaultIrregularAcs() {
		return countOfDefaultIrregularAcs;
	}
	public void setCountOfDefaultIrregularAcs(String countOfDefaultIrregularAcs) {
		this.countOfDefaultIrregularAcs = countOfDefaultIrregularAcs;
	}
	public String getTotalOutstandingINRDefaultIrregularAcs() {
		return totalOutstandingINRDefaultIrregularAcs;
	}
	public void setTotalOutstandingINRDefaultIrregularAcs(String totalOutstandingINRDefaultIrregularAcs) {
		this.totalOutstandingINRDefaultIrregularAcs = totalOutstandingINRDefaultIrregularAcs;
	}
	public String getTotalPrincipalINRDefaultIrregularAcs() {
		return totalPrincipalINRDefaultIrregularAcs;
	}
	public void setTotalPrincipalINRDefaultIrregularAcs(String totalPrincipalINRDefaultIrregularAcs) {
		this.totalPrincipalINRDefaultIrregularAcs = totalPrincipalINRDefaultIrregularAcs;
	}
	public String getTotalInterestINRDefaultIrregularAccounts() {
		return totalInterestINRDefaultIrregularAccounts;
	}
	public void setTotalInterestINRDefaultIrregularAccounts(String totalInterestINRDefaultIrregularAccounts) {
		this.totalInterestINRDefaultIrregularAccounts = totalInterestINRDefaultIrregularAccounts;
	}
	public String getCountOfMTMDerivativesAcs() {
		return countOfMTMDerivativesAcs;
	}
	public void setCountOfMTMDerivativesAcs(String countOfMTMDerivativesAcs) {
		this.countOfMTMDerivativesAcs = countOfMTMDerivativesAcs;
	}
	public String getTotalContraValueINRMTMDerivatives() {
		return totalContraValueINRMTMDerivatives;
	}
	public void setTotalContraValueINRMTMDerivatives(String totalContraValueINRMTMDerivatives) {
		this.totalContraValueINRMTMDerivatives = totalContraValueINRMTMDerivatives;
	}
	public String getIsWilfulDefaulter() {
		return isWilfulDefaulter;
	}
	public void setIsWilfulDefaulter(String isWilfulDefaulter) {
		this.isWilfulDefaulter = isWilfulDefaulter;
	}
	
	
	
	
	

}
