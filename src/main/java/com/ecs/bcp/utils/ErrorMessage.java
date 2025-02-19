package com.ecs.bcp.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {

	
	private static Map<String, String> codeToMessageMap = new HashMap<String, String>();

	static {		
		codeToMessageMap.put(Constants.ECSV0001, "Exception Occured");
		codeToMessageMap.put(Constants.ECSV0002, "Invalid Request");
		codeToMessageMap.put(Constants.ECSV0003, "Invalid Credential");
		codeToMessageMap.put(Constants.ECSV0004, "Data Processor Created!");
		codeToMessageMap.put(Constants.ECSV0005, "Input Data Missing");
		codeToMessageMap.put(Constants.ECSV0006, "Given Email ID Already Exists");		
		codeToMessageMap.put(Constants.ECSV0007, "Invalid OTP");
		codeToMessageMap.put(Constants.ECSV0008, "No Records");
		codeToMessageMap.put(Constants.ECSV0009, "Exception while creating sms table");
		codeToMessageMap.put(Constants.ECSV0010, "Exception Occured at validating OTP");
		codeToMessageMap.put(Constants.ECSV0011, "OTP Sent Successfully");
		codeToMessageMap.put(Constants.ECSV0012, "OTP Verified");
		codeToMessageMap.put(Constants.ECSV0013, "Company name not registered");
		codeToMessageMap.put(Constants.ECSV0014, "User with same pan and user type already registered");
		codeToMessageMap.put(Constants.ECSV0015, "Awaiting approval for the account");
		codeToMessageMap.put(Constants.ECSV0016, "Update Successfully ");
		codeToMessageMap.put(Constants.ECSV0017, "Approved Successfully");
		codeToMessageMap.put(Constants.ECSV0018, "Deactive Successfully");
		codeToMessageMap.put(Constants.ECSV0019, "Reject Successfully");
		codeToMessageMap.put(Constants.ECSV0020, "hold Successfully");
		codeToMessageMap.put(Constants.ECSV0021, "Revoke Successfully");		
		codeToMessageMap.put(Constants.ECSV0100, "No Data");
		codeToMessageMap.put(Constants.ECSV0022, "Account Deactived");
		codeToMessageMap.put(Constants.ECSV0023, "Invalid Status");
		
	}
	
	
	public static String getErrorMessage(String code) 
	{
		if(code == null)
			return "Error code is NULL";
		String description = codeToMessageMap.get(code);
		if (description == null) 
		{
			description = "Invalid Error Code!";
		}
		return description;
	}
	
}
