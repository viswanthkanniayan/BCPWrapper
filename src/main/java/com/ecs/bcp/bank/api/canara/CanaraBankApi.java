package com.ecs.bcp.bank.api.canara;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.bank.api.canara.xsd.BankResponseAPI2;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.BankApiMasterPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CanaraBankApi {
	
	private static final Random RANDOM = new Random();

	public static EntityResponce getCanaraNumberOfAccount(EntityRequest reqXSD) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		List<BankApiLogPojo> bankApi = null;
		List<BalanceConfirmPojo> balancedts = null;
		SettingsPojo setPojo = null;
		EntityResponce response = new EntityResponce();
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
		UserDetailsPojo userPojo = new UserDetailsPojo();

		try {
			
			SettingsPojo NESLPublicKey = EjbLookUps.getSettingsRemote().findById("NESLPublicKey");

			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
			
			

			if (!reqXSD.getBankname().equalsIgnoreCase("CANARA BANK") && !reqXSD.getBankname().equalsIgnoreCase("UCO") && !reqXSD.getBankname().equalsIgnoreCase("PNB")) {
			
			String uniqueTxnId = generateUniqueTxnId();
			
			EntityResponce jsonRequest1 = new EntityResponce();
			
			String lei = genRandNo(8);
			String cin = genRandNo(8);
			String count = genRandNo(2);
			String bankChag = genRandNo(3);

			jsonRequest1.setUniqueTxnId(uniqueTxnId);
			jsonRequest1.setLei(lei);
			jsonRequest1.setAccountsCount(count);
			jsonRequest1.setBankCharges(bankChag);
			jsonRequest1.setCin(cin);
			jsonRequest1.setErrorCode("200");
			jsonRequest1.setError(false);
			
			
			balancePojo.setTxnId(reqXSD.getTxnId());
			balancePojo.setAccountCount(count);
			balancePojo.setBankCharges(bankChag);
			balancePojo.setApiPurpose("01");
			balancePojo.setUniqueTxnId(uniqueTxnId);

			EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

			BankApiLogPojo  banklog =  new BankApiLogPojo();

			banklog.setAccountsCount(count);
			banklog.setBankCharges(bankChag);
			banklog.setCin(cin);
			banklog.setErrorCode("200");
			banklog.setLei(lei);
			banklog.setPan(reqXSD.getPan());
			banklog.setUniqueTxnId(uniqueTxnId);
			banklog.setApiPurpose("01");
			banklog.setBank(reqXSD.getBankname());

			EjbLookUps.getBankApiLogRemote().create(banklog);
			
			response = jsonRequest1;
			
			return response;
			}
			
			
			BankApiMasterPojo BANKPublicKey = EjbLookUps.getBankApiMasterRemote().findById(reqXSD.getBankname());

			balancePojo = EjbLookUps.getBalanceConfirmRemote().findById(reqXSD.getTxnId());

			if(balancePojo != null) {
			
		//	pojo1 = EjbLookUps.getUserDetailsRemote().findByPan(reqXSD.getPan() );

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			
			String fromDate = sdf.format(balancePojo.getFromDate());
			System.out.println("fromDate) -----------=>>  "+fromDate);

			String toDate = sdf.format(balancePojo.getToDate());
			System.out.println("toDate -----------=>>  "+toDate);

		//	String uniqueTxnId = generateUniqueTxnId();

			
			String uniqueTxnId = generateUniqueTxnId();
			
			System.out.println("reqXSD.getUniqueTxnId() -----------=>>  "+reqXSD.getUniqueTxnId());
			
			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId",uniqueTxnId );
			jsonBody.addProperty("FromDate", fromDate);
			jsonBody.addProperty("ToDate", toDate);
			jsonBody.addProperty("reqName",  balancePojo.getRequestorName() != null ? balancePojo.getRequestorName() : "");

			
			if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITOR")) {
		        
		        userPojo = EjbLookUps.getUserDetailsRemote().findAuditorDetails(balancePojo.getAuditorPan(), balancePojo.getRequestorType());
		        
		        System.out.println("reqId  --------------> "+userPojo.getMemberRegNo());
		        System.out.println("reqEntityID  --------------> "+userPojo.getFirmRegno());
		        
		        jsonBody.addProperty("reqEntity", userPojo.getPkPojo().getEntityName());
		        jsonBody.addProperty("reqId", userPojo.getMemberRegNo());
		        jsonBody.addProperty("reqEntityID" ,userPojo.getFirmRegno());
		        jsonBody.addProperty("bcpurpose", "For Auditing");
		        
		      }else if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITEE")){
		        
		    	  jsonBody.addProperty("reqId", balancePojo.getRequestorPan());
					jsonBody.addProperty("reqEntityID" ,balancePojo.getAuditeePan());
					jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
					
					
//		        jsonBody.addProperty("reqId", balancePojo.getAuditeePan());
//		        jsonBody.addProperty("reqEntityID" ,balancePojo.getRequestorPan());
//		        jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
		        jsonBody.addProperty("bcpurpose", "Not for auditing /others");
		      }
			
			
			// changed 29-01-2025
						jsonBody.addProperty("reqEmail", balancePojo.getUserId()!= null ? balancePojo.getUserId() : "");
						jsonBody.addProperty("reqMob", balancePojo.getRequestorMobile()!= null ? balancePojo.getRequestorMobile() : "");
						jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");
						jsonBody.addProperty("bankId", reqXSD.getBankname());
						
			
//			jsonBody.addProperty("reqEmail", balancePojo.getEntityEmail()!= null ? balancePojo.getEntityEmail() : "");
//			jsonBody.addProperty("reqMob", balancePojo.getEntityMobile()!= null ? balancePojo.getEntityMobile() : "");
//			jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");
//			jsonBody.addProperty("bankId", reqXSD.getBankname());
		//	jsonBody.addProperty("responseURL", "https://balcon.nesl.co.in/BcpWapperService/API2/BCWebhook");
			
			String responseUrl = EjbLookUps.getSettingsRemote().findById("CALL_BACK_URL").getStringValue();


			if(!reqXSD.getBankname().equalsIgnoreCase("CANARA BANK")){
				jsonBody.addProperty("responseURL", responseUrl );
			}
			
			jsonBody.addProperty("APIpurpose", "01");
		//	jsonBody.addProperty("bank", "");

			JsonObject auditeeDetails = new JsonObject();
			
			//AUDITEE NAME
			auditeeDetails.addProperty("auditeeCompany", balancePojo.getAuditeeName());
			
			auditeeDetails.addProperty("auditeePan", reqXSD.getPan());

			jsonBody.add("auditeeDetails", auditeeDetails);
			
			

			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("encryptData", jsonBody);

			JsonObject jsonbody = new JsonObject();
			jsonbody.add("body", jsonPayload);

			JsonObject jsonRequest = new JsonObject();
			jsonRequest.add("Request", jsonbody);


			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody);

			System.out.println("RequestPayload ------->>  "+jsonPayloadString);
			String aesKey;

			try {

				aesKey = AESCanaraUtils.generateAESKey();


				System.out.println("AEC_KEY ------->>  "+aesKey);

				String encryptedPayload = AESCanaraUtils.encryptAESGCM(jsonPayloadString, aesKey);



				PublicKey neslPublicKey = AESCanaraUtils.loadPublicKeyFromCertificate(NESLPublicKey.getBinaryValue());

				//	System.out.println("BCP NESL Public Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPublicKey.getEncoded()));

				PublicKey BankPublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey.getBankPublicKey());

				//	System.out.println("Bank Public Key::------>> :: => "+Base64.getEncoder().encodeToString(BankPublicKey.getEncoded()));

				PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey.getBinaryValue());

				//System.out.println("NESL Private Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPrivateKey.getEncoded()));


				String signaturePayload = gson.toJson(jsonRequest);

				System.out.println("signaturePayload --->> "+signaturePayload);

				String signature = AESCanaraUtils.sign(signaturePayload, neslPrivateKey);

				String accsessToken = AESCanaraUtils.encryptRSA(aesKey, BankPublicKey);

				System.out.println("accsessToken ----------->> :: => "+accsessToken);

				System.out.println("signature----------->> :: => "+signature);

				System.out.println("encryptedPayload----------->> :: => "+encryptedPayload);


				response = AESCanaraUtils2.sendEncryptedRequest(jsonPayloadString,encryptedPayload, signature, accsessToken ,NESLPublicKey.getBinaryValue()
						,neslPrivateKey,reqXSD.getBankname(),reqXSD.getTxnId(),reqXSD.getPan(),"01");

				boolean isVerified = AESCanaraUtils.verify(signaturePayload, signature, neslPublicKey);

				if (isVerified) {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}else {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static EntityResponce getBalanceCertificate (EntityRequest requestXsd) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		EntityResponce response = null;
		BalanceConfirmPojo balancePojo1 = new BalanceConfirmPojo();
		UserDetailsPojo userPojo = new UserDetailsPojo();

		try {
			
			
			SettingsPojo NESLPublicKey = EjbLookUps.getSettingsRemote().findById("NESLPublicKey");

			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
			
			

		//	if(!requestXsd.getBankname().equalsIgnoreCase("CANARA BANK")) {
			if (!requestXsd.getBankname().equalsIgnoreCase("CANARA BANK") && !requestXsd.getBankname().equalsIgnoreCase("UCO")&& !requestXsd.getBankname().equalsIgnoreCase("PNB")) {
				
			
			EntityResponce jsonRequest1 = new EntityResponce();
			
			
			jsonRequest1.setUniqueTxnId(requestXsd.getUniqueTxnId());
			jsonRequest1.setBank(requestXsd.getBankname());
			jsonRequest1.setAckStatus("Accepted".toUpperCase());
			jsonRequest1.setError(false);
			
			
			balancePojo1.setTxnId(requestXsd.getTxnId());
			balancePojo1.setBank(requestXsd.getBankname());
			balancePojo1.setAckStatus("Accepted".toUpperCase());
			balancePojo1.setApiPurpose("02");

			EjbLookUps.getBalanceConfirmRemote().update(balancePojo1);

			BankApiLogPojo  banklog =  new BankApiLogPojo();

			banklog.setAckStatus("Accepted".toUpperCase());
			banklog.setBank(requestXsd.getBankname());
			banklog.setPan(requestXsd.getPan());
			banklog.setUniqueTxnId(requestXsd.getUniqueTxnId());
			banklog.setApiPurpose("02");
			

			EjbLookUps.getBankApiLogRemote().create(banklog);
			
			response = jsonRequest1;
			
			return response;
			
			}
			
			BankApiMasterPojo BANKPublicKey = EjbLookUps.getBankApiMasterRemote().findById(requestXsd.getBankname());


			BalanceConfirmPojo balancePojo = EjbLookUps.getBalanceConfirmRemote().findById(requestXsd.getTxnId());

			if(balancePojo != null) {

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String fromDate = sdf.format(balancePojo.getFromDate());
			String toDate = sdf.format(balancePojo.getToDate());

			System.out.println("reqXSD.getUniqueTxnId() -----------=>>  "+requestXsd.getUniqueTxnId());

			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId", requestXsd.getUniqueTxnId());
			
			jsonBody.addProperty("FromDate", fromDate);
			System.out.println("fromDate) -----------=>>  "+fromDate);

			jsonBody.addProperty("ToDate", toDate);
			System.out.println("toDate -----------=>>  "+toDate);

			jsonBody.addProperty("reqName",  balancePojo.getRequestorName() != null ? balancePojo.getRequestorName() : "");



			if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITOR")) {

				userPojo = EjbLookUps.getUserDetailsRemote().findAuditorDetails(balancePojo.getAuditorPan(), balancePojo.getRequestorType());

				System.out.println("reqId  --------------> "+userPojo.getMemberRegNo());
				System.out.println("reqEntityID  --------------> "+userPojo.getFirmRegno());

				jsonBody.addProperty("reqEntity", userPojo.getPkPojo().getEntityName());
				jsonBody.addProperty("reqId", userPojo.getMemberRegNo());
				jsonBody.addProperty("reqEntityID" ,userPojo.getFirmRegno());
				jsonBody.addProperty("bcpurpose", "For Auditing");

			}else if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITEE")){

//				jsonBody.addProperty("reqId", balancePojo.getAuditeePan());
//				jsonBody.addProperty("reqEntityID" ,balancePojo.getRequestorPan());
//				jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
				
				 jsonBody.addProperty("reqId", balancePojo.getRequestorPan());
					jsonBody.addProperty("reqEntityID" ,balancePojo.getAuditeePan());
					jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
					
					
				jsonBody.addProperty("bcpurpose", "Not for auditing /others");

			}

			// changed 29-01-2025
			jsonBody.addProperty("reqEmail", balancePojo.getUserId()!= null ? balancePojo.getUserId() : "");
			jsonBody.addProperty("reqMob", balancePojo.getRequestorMobile()!= null ? balancePojo.getRequestorMobile() : "");
			jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");

//			jsonBody.addProperty("reqEmail", balancePojo.getEntityEmail()!= null ? balancePojo.getEntityEmail() : "");
//			jsonBody.addProperty("reqMob", balancePojo.getEntityMobile()!= null ? balancePojo.getEntityMobile() : "");
//			jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");

			jsonBody.addProperty("bankId", requestXsd.getBankname());

			String responseUrl = EjbLookUps.getSettingsRemote().findById("CALL_BACK_URL").getStringValue();

			if(!requestXsd.getBankname().equalsIgnoreCase("CANARA BANK")){
				jsonBody.addProperty("responseURL", responseUrl);
			}
			
			jsonBody.addProperty("APIpurpose", "02");
		//	jsonBody.addProperty("bank", "CANARA BANK");

			JsonObject auditeeDetails = new JsonObject();
			auditeeDetails.addProperty("auditeeCompany", balancePojo.getAuditeeName() );
			auditeeDetails.addProperty("auditeePan", requestXsd.getPan() );

			jsonBody.add("auditeeDetails", auditeeDetails);
			
			
			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("encryptData", jsonBody);

			JsonObject jsonbody = new JsonObject();
			jsonbody.add("body", jsonPayload);

			JsonObject jsonRequest = new JsonObject();
			jsonRequest.add("Request", jsonbody);
			

			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody);

			System.out.println("RequestPayload ------->>  "+jsonPayloadString);

			String aesKey;

			try {

				aesKey = AESCanaraUtils.generateAESKey();


				System.out.println("AEC_KEY ------->>  "+aesKey);

				String encryptedPayload = AESCanaraUtils.encryptAESGCM(jsonPayloadString, aesKey);



				PublicKey neslPublicKey = AESCanaraUtils.loadPublicKeyFromCertificate(NESLPublicKey.getBinaryValue());

				//	System.out.println("BCP NESL Public Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPublicKey.getEncoded()));

				PublicKey BankPublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey.getBankPublicKey());

				//	System.out.println("Bank Public Key::------>> :: => "+Base64.getEncoder().encodeToString(BankPublicKey.getEncoded()));

				PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey.getBinaryValue());

				//			System.out.println("NESL Private Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPrivateKey.getEncoded()));


				String signaturePayload = gson.toJson(jsonRequest);

			//	System.out.println("signaturePayload --->> "+signaturePayload);

				String signature = AESCanaraUtils.sign(signaturePayload, neslPrivateKey);

				String accsessToken = AESCanaraUtils.encryptRSA(aesKey, BankPublicKey);

				System.out.println("accsessToken ----------->> :: => "+accsessToken);

				System.out.println("signature----------->> :: => "+signature);

				System.out.println("encryptedPayload----------->> :: => "+encryptedPayload);


				response = AESCanaraUtils2.sendEncryptedRequest(jsonPayloadString,encryptedPayload, signature, accsessToken ,NESLPublicKey.getBinaryValue()
						,neslPrivateKey,requestXsd.getBankname(),requestXsd.getTxnId(),requestXsd.getPan(),"02");

				boolean isVerified = AESCanaraUtils.verify(signaturePayload, signature, neslPublicKey);

				if (isVerified) {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}else {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	

    public static String generateUniqueTxnId() {
    	
    	final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	final String DIGITS = "0123456789";
    	
        String firstPart = generateRandomString(DIGITS, 5);
        String secondPart = generateRandomString(LETTERS, 3);
        String thirdPart = generateRandomString(DIGITS, 4);
        String fourthPart = generateRandomString(DIGITS, 4);
        
        String fifthPart = generateRandomString(LETTERS, 3);
        
        return firstPart + secondPart + thirdPart + fourthPart + fifthPart;
    }

    private static String generateRandomString(String chars, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    
    public static EntityResponce getNumberOfAccount(EntityRequest reqXSD) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		List<BankApiLogPojo> bankApi = null;
		List<BalanceConfirmPojo> balancedts = null;
		SettingsPojo setPojo = null;
		EntityResponce response = new EntityResponce();
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
		UserDetailsPojo userPojo = new UserDetailsPojo();
		
		try {
			
			SettingsPojo NESLPublicKey = EjbLookUps.getSettingsRemote().findById("NESLPublicKey");

			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
			
			BankApiMasterPojo BANKPublicKey = EjbLookUps.getBankApiMasterRemote().findById(reqXSD.getBankname());

			balancePojo = EjbLookUps.getBalanceConfirmRemote().findById(reqXSD.getTxnId());

			//pojo1 = EjbLookUps.getBalanceConfirmRemote().findByPan(reqXSD.getPan() );

	/*		Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String fromDate = "01-01-2023";
			String toDate = sdf.format(date);   */
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			//String fromDate = sdf.format(reqXSD.getFromDate());
		//	String toDate = sdf.format(reqXSD.getToDate());
			
		//	String uniqueTxnId = generateUniqueTxnId();
			System.out.println("reqXSD.getUniqueTxnId() -----------=>>  "+reqXSD.getUniqueTxnId());

			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId", reqXSD.getUniqueTxnId());
			jsonBody.addProperty("FromDate", reqXSD.getFromDate());
			
			jsonBody.addProperty("ToDate", reqXSD.getToDate());
			
			jsonBody.addProperty("reqName",  balancePojo.getRequestorName() != null ? balancePojo.getRequestorName() : "");



			if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITOR")) {

				userPojo = EjbLookUps.getUserDetailsRemote().findAuditorDetails(balancePojo.getAuditorPan(), balancePojo.getRequestorType());

				System.out.println("reqId  --------------> "+userPojo.getMemberRegNo());
				System.out.println("reqEntityID  --------------> "+userPojo.getFirmRegno());

				jsonBody.addProperty("reqEntity", userPojo.getPkPojo().getEntityName());
				jsonBody.addProperty("reqId", userPojo.getMemberRegNo());
				jsonBody.addProperty("reqEntityID" ,userPojo.getFirmRegno());
				jsonBody.addProperty("bcpurpose", "For Auditing");

			}else if( balancePojo.getRequestorType().equalsIgnoreCase("AUDITEE")){

//				jsonBody.addProperty("reqId", balancePojo.getAuditeePan());
//				jsonBody.addProperty("reqEntityID" ,balancePojo.getRequestorPan());
//				jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
				
				jsonBody.addProperty("reqId", balancePojo.getRequestorPan());
				jsonBody.addProperty("reqEntityID" ,balancePojo.getAuditeePan());
				jsonBody.addProperty("reqEntity", balancePojo.getAuditeeName());
				
				jsonBody.addProperty("bcpurpose", "Not for auditing /others");
			}

			
			// changed 29-01-2025
			jsonBody.addProperty("reqEmail", balancePojo.getUserId()!= null ? balancePojo.getUserId() : "");
			jsonBody.addProperty("reqMob", balancePojo.getRequestorMobile()!= null ? balancePojo.getRequestorMobile() : "");
			jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");
			jsonBody.addProperty("bankId", reqXSD.getBankname());
			
		//	String responseUrl = EjbLookUps.getSettingsRemote().findById("CALL_BACK_URL").getStringValue();
			
			String responseUrl = reqXSD.getResponseURL();

			if(!reqXSD.getBankname().equalsIgnoreCase("CANARA BANK")){
				jsonBody.addProperty("responseURL", responseUrl );
			}
			
			jsonBody.addProperty("APIpurpose", reqXSD.getAPIpurpose());
			
			
//			jsonBody.addProperty("reqEmail", balancePojo.getEntityEmail()!= null ? balancePojo.getEntityEmail() : "");
//			jsonBody.addProperty("reqMob", balancePojo.getEntityMobile()!= null ? balancePojo.getEntityMobile() : "");
//			jsonBody.addProperty("reqType", balancePojo.getRequestorType()!= null ? balancePojo.getRequestorType() : "");
//			jsonBody.addProperty("bankId", reqXSD.getBankname());
//			jsonBody.addProperty("responseURL", reqXSD.getResponseURL());
//			jsonBody.addProperty("APIpurpose", reqXSD.getAPIpurpose());
			
			
		//	jsonBody.addProperty("bank", "");

			JsonObject auditeeDetails = new JsonObject();
			auditeeDetails.addProperty("auditeeCompany", balancePojo.getAuditeeName());
			auditeeDetails.addProperty("auditeePan", reqXSD.getPan());

			jsonBody.add("auditeeDetails", auditeeDetails);
			
			

			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("encryptData", jsonBody);

			JsonObject jsonbody = new JsonObject();
			jsonbody.add("body", jsonPayload);

			JsonObject jsonRequest = new JsonObject();
			jsonRequest.add("Request", jsonbody);


			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody);

			System.out.println("RequestPayload ------->>  "+jsonPayloadString);
			String aesKey;

			try {

				aesKey = AESCanaraUtils.generateAESKey();


				System.out.println("AEC_KEY ------->>  "+aesKey);

				String encryptedPayload = AESCanaraUtils.encryptAESGCM(jsonPayloadString, aesKey);



				PublicKey neslPublicKey = AESCanaraUtils.loadPublicKeyFromCertificate(NESLPublicKey.getBinaryValue());

				//	System.out.println("BCP NESL Public Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPublicKey.getEncoded()));

				PublicKey BankPublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey.getBankPublicKey());

				//	System.out.println("Bank Public Key::------>> :: => "+Base64.getEncoder().encodeToString(BankPublicKey.getEncoded()));

				PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey.getBinaryValue());

				//System.out.println("NESL Private Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPrivateKey.getEncoded()));


				String signaturePayload = gson.toJson(jsonRequest);

				System.out.println("signaturePayload --->> "+signaturePayload);

				String signature = AESCanaraUtils.sign(signaturePayload, neslPrivateKey);

				String accsessToken = AESCanaraUtils.encryptRSA(aesKey, BankPublicKey);

				System.out.println("accsessToken ----------->> :: => "+accsessToken);

				System.out.println("signature----------->> :: => "+signature);

				System.out.println("encryptedPayload----------->> :: => "+encryptedPayload);


				response = AESCanaraUtils2.sendEncryptedTestRequest(encryptedPayload, signature, accsessToken ,NESLPublicKey.getBinaryValue()
						,neslPrivateKey,reqXSD.getBankname(),reqXSD.getTxnId(),reqXSD.getPan(),"01");

				boolean isVerified = AESCanaraUtils.verify(signaturePayload, signature, neslPublicKey);

				if (isVerified) {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}else {
					System.out.println("Signature Verified. Processing the request..."+isVerified);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
    
    
    public static String genRandNo(int digit){
		String characters = "0123456789";
		String otp = RandomStringUtils.random(digit, characters);
		return otp;
	}



	public static void main(String[] args) {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSZ");
		String fromDate = sdf.format(date);
		String toDate = sdf.format(date);

		JsonObject jsonBody1 = new JsonObject();
		jsonBody1.addProperty("uniqueTxnId", "31790GO3D2401896ISS001");
		jsonBody1.addProperty("bankId", "CANANRA");
		jsonBody1.addProperty("pan", "AAACA1111A" );
		jsonBody1.addProperty("fromDate", fromDate);
		jsonBody1.addProperty("ToDate", toDate);
		jsonBody1.addProperty("ToDate", toDate);
		jsonBody1.addProperty("APIpurpose", "02");

		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("encryptData", jsonBody1);

		JsonObject jsonbody = new JsonObject();
		jsonbody.add("body", jsonPayload);

		JsonObject jsonRequest = new JsonObject();
		jsonRequest.add("Request", jsonbody);


		Gson gson = new Gson();
		String jsonPayloadString = gson.toJson(jsonBody1);

		System.out.println("RequestPayload ------->>  "+jsonPayloadString);
		String aesKey;
		try {

			aesKey = AESCanaraUtils.generateAESKey();


			System.out.println("AEC_KEY ------->>  "+aesKey);

			String encryptedPayload = AESCanaraUtils.encryptAESGCM(jsonPayloadString, aesKey);


			String NESLPublicKey ="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";

			PublicKey neslPublicKey = AESCanaraUtils.loadPublicKeyFromCertificate(NESLPublicKey);

			//	System.out.println("BCP NESL Public Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPublicKey.getEncoded()));


			String BANKPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsGfivlMdpEaR4wz9koLwpEhGjLkvJqLfB+aicwUoIcsl8tiKLLsq20KRq5khCgoGpibhiakQVIGV7HWpdEKYp04s2mHQ0Wjz/gwp8pSvcgBMkxBlOnZkx0VDHtWnv2NxJ01krGBwAJTxz1SefqSS5LqjRyF2Z2R6sjygF83o3GN75Tg+sOqqTygr93Rr7+7KqcJ4Qeu0xmJ9MxIZu7Z43EOqGRq4zVSGWfSAJrX4jST3uYfl8rLikOrMuXoAF2LhdCQyB1tmOSWh6o7a1swN8cbp4/XwKtdBYUN+5FVRwtzfmPbF2MWB6eMx6IOfM8fttWWD8irlvCV/LsoQaY+C7QIDAQAB";
			PublicKey BankPublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey);

			//	System.out.println("Bank Public Key::------>> :: => "+Base64.getEncoder().encodeToString(BankPublicKey.getEncoded()));


			String NESLPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDzkCSV/NhygxK8LvalEZV/JrWmcPkq49hMJGQwwPlko9Y/BuYq+oXZnpFClhxL6uk74Da4nSoph4DTS4s+kcEtvPBjmBhixibWMqA4v5mb/ppKIOiBBFlDrd+2nRV0vrIMNH/d0xLb/Ed9SB/ZMmy9Aq+MHMVYLedR5Tp1ela+9n+DXnzsJNcPcI2RFzzeokvZE1qyxIIeW1FErlDYhNRK8tDhCzklNvbKaH5Esn3jOBRsg05jSyrymWu8RP9jged8Xgle11UyrPslCNgD0Kna7DbkM5lcOYbr9poqpzyG0xhfE9kVXqbirqkM3691yQMKz6ZhNwccnBMB4Kawrpi5AgMBAAECggEBAMOtTndhx9NYErk1UdqUIfMhelbZ9JTMsH2bzxKMNtDv/7RrKofk9Zw8lJ9WUi178R21qMDxI2px/sHCs3/1QaopXUfmbdENOS5lRevRout8xSBBCAp/H+gLzOu9j/tXKVw84W2pokEykYzJYSzyIgJIIhkDFFkfp/RXoXu4afDIL7QmajFMiSIJR6tzNOWS3CyLB7bIG1caCdPToaQkdwFDCicNcA9rZi4tPi1TlddYxzKMLkUJ6Bf1lGTT/OZmyxkg6ixLmvBjvYlfZNvOI1OmT9Gt909U3VvRuido/b2wW38MNU1iQ+DI+zGXetuzR1vwMcINDfNiZnUp+Yuhc/ECgYEA+7dwS6YZSjy1b1Nd9vT+O+e7ZE+GIzBWcHBVXYrWj5Rt2R0HHZ5jypEm2/eTYx3BTGjGnqMrRbT/1vwX/sUxCoY78Mvu87Et3ML+cpyOJ6DPqEPOv7GwNjXVNGpymCM5DHjbXmYBqth+MpRY39lERl5rP/Ptsx+1AfS94iFccjcCgYEA97UvVVoVTSfnRvto8wUBiuEbpsu4uuy/ijxjvLZ1cbVSt0Oyd5xlVuy/vG1vrv5Cm7HhrGs7MhKIbQF7WhHXV4ankvn+zwUmSclYpVWWBkryKX6fbJfJh90q/tL1RgMblj0yDz53kH6ViVe+Mq+RTibSh0irt8GfPAjV6e6VlI8CgYEAjvmV3lI9IS60e8RlpgVodsxcY1DMRyaIopsb0eMvliRf9KeJSCiUVBX4dY79d5oKFoWY87iItrJlc06DrqGBBpJSb6bWVjL4cuGLN7x2/klYSiIhyD58cX5IWNpxtrqjB9OJ/ud/4PPRUpdyl8tH/ZRZ5Nx/0nObE75ZMJ10bicCgYA0iY08gRq7fpcjCve7c3hcSFphChxoKQaG/z/4KorGTzr3+7fCfr1Prm8MO/nQF8Vw2E2RED1B5YRh+kp7VAVkXv7zwWo15lW0mKvghUKImyS5gE237omj81jHK18yNj6HovsXGJyrXO3Cb4W7olkjRkCoyNUC6GIpjYYxU5UOKQKBgGXDf9BSp3A0/oXCfLutftBH0/BgmuiGKVyGQ/0xmnaT9wA7wKKqg/yRbA5lfNbXR7KWCuLjZz6RJmcQTXypCEyocRSIupP9pY/aTF3MCMjbQH8919EycMHmkN9GSjljb5mngMHxETeSZP5G3pHGO0Ki5zX6pWTm9OoneN5jrrjl";
			PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey);

			//		System.out.println("NESL Private Key ::------>> :: => "+Base64.getEncoder().encodeToString(neslPrivateKey.getEncoded()));


			String signaturePayload = gson.toJson(jsonRequest);

			System.out.println("signaturePayload --->> "+signaturePayload);

			String signature = AESCanaraUtils.sign(signaturePayload, neslPrivateKey);



			String CANARAPRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDfZsbI92If0wSMwvTsNGpr/59aLfsyGoen4x+E0QSdYmwXUa8No0cSLvQH2IFBxcGFQvuP6VF1o6ZelajI9Av9T+x5BemGCy/eXutSrURXxKQtnU5k1bq1N4Z7QOW4tqUEkrh7GuegJ8KSE7VMDz3pWFLyfDDSikzSXfEEw79CocqAdaZaWGU84Iwn7WM2ZG87rCdIy5Gw23VWaOcpbZh0N055zujaQQsq3W9tOjJ4b/6qiZ9J/7PdVlJlSLtdq/u7/BcFzcbvCDnj7udlkkxwrIVZucydkfdf1zeeQOJHYodRxqZCuJJ/5VNKKj7xteTzM599mREcxGGG9yXxKbsbAgMBAAECggEAcI38m2YgNv5/LVhQQ+2IrM8Gf03pYG4DaO/Cgo04sP7UjM8nzkw0U5H6ezV2sierf7OmwPQBia6g9MicWFq067Rn5PW4mUNro7LZBzeq1jmJqj22MJGKVyEZjWG+G9lRvMMbVVLR3lwJ6MUrM53+SGGqsOcnRfxOBjM0an0hELU4DHss9JAAivJFWDbG+aWh26D4V4CLn8Kp/97uc/52GMaxCz4MOGu5Yir/EsA5jX9xahXRLUVCSlWI6B3g+Q9ccOH/vuWhoflR5X0pxAJiNe/EUh0/m7wL5XqDcpLIOz9k7bAzAFIinG9z5ECfIswUhLRzfZhNVtF9sTefcRPRgQKBgQD3qWpLWQxCujz4VYR7obbG9hWzc/SDbRS6qmxBPuo7Qts4t/cCVhnbyK4UloKETj6YIAE2hZqs/4bQr+jGaRfaMIZTyh3z6TacbY7+G3qse1BHJ5B6Z7q794XQSRwQ5VZuBeIXbEMgAz3WPc42wklkBdVFBGLj2KsiAlHqTuqfQQKBgQDm7ENMbmZHCR4+sl1/kcobeSmpOTJ8uJEiP87q8U43O1L3/lcWL7zUVPKNlbaNxEdB22lZJtccCRzxYReojId/URdXId+wGxQFgL13UzlrQBPFyoFx9Z3cpRdpM061oGqLvU/k+oB+9Q9A6tyxHVOPNcpmNhowR/ILX3t6MipfWwKBgQD3JtB25STBKo5R3TXbEyUOJOK/NUacKa14Iz7pVdn2zd9sGNqeMEKZ/QkMbq9NTbBVrArJ2EblwhNOC16nMpWlHqvOznNZyDDBpuv+VnnzyxPwDmG2ZfIKRxf5JhymF9hK8AATE+1g8xswxHkYWIdZFCEjQR9W9hDCXcJVq++kgQKBgQCXY6sXL4Vj6qlmZx7+5EBA1KUoaPQdag7UCksIIdyqPv080gSb6f38ohhGFDGTLWud1E9zgBAkNCFCaenNuRuXrz0DBZUVahstJGKSGQCavG56DDHi2Sh+H8mCUX23ewx+wkTflA6rrNMsNw0qHFTMnXIsoil3H6OiKx3TVZIL4wKBgH9/acemELAqXexez/k2BX4oZXbs40omBlT1kF5ajX+WYQTnD94nqnRUf4EMTTs8/K2sT4u21KEY72/PDB2PH9F6o/wPqa+WybcIH7IOC8mTHx6MKfaZCrM4DTcqky6oJlzhRwyvj0TWxsEVoLSuj8fL5/wndEFrefEGdRspE0I2";

			PrivateKey Canara_PRIVATE = AESCanaraUtils.loadPrivateKeyFromString(CANARAPRIVATE);

			System.out.println("CanaraBank_PRIVATE Key ::------>> :: "+Base64.getEncoder().encodeToString(Canara_PRIVATE.getEncoded()));

			//	String signature1 = AESUtils.sign(signaturePayload, Canara_PRIVATE);    


			//		System.out.println("signature1 ----------->> :: => "+signature1);   

			String accsessToken = AESCanaraUtils.encryptRSA(aesKey, BankPublicKey);

			System.out.println("accsessToken ----------->> :: => "+accsessToken);

			System.out.println("signature----------->> :: => "+signature);

			System.out.println("encryptedPayload----------->> :: => "+encryptedPayload);


			EntityResponce response = AESCanaraUtils.sendEncryptedRequest(jsonPayloadString,encryptedPayload, signature, accsessToken,NESLPublicKey,neslPrivateKey,"CANANRA","","pan","01");

			String respToken ="NUCh7/oOwUHLDvh/jeV1UoNkOGHUx3+OsaYHzQk6iyZcNtZ1EVyZgsARGfAibbqkhdXB8OGNYC7aT6HPjBADj0uyupQAsBNsnsY1gPjEV2u6rhTIMlN0XJCWTZovzUDpEF4moMkjyPDf3MpPpA8PoWt7VSniISBDg+vVosf8hasAXfYZMOshWCQomwZ2zdtNRNFy7lvp5WvGgicY/PLL6/LfH/hIFmrvCncF1BuIaAmTmm6964RXJBBTJ6pcfY6w8KSZ8zXqYMirup8Ue94MdArAR92Ul3TZWvaa0UELkgwh1+u/Y2Io/0pgfFwr90r6MXhk+bRe1oYhozoTMAgdPA==";

			String decryptedAesKey = AESCanaraUtils.decryptRSA(respToken, Canara_PRIVATE);

			System.out.println("decryptedAesKey -------------->>> :: "+decryptedAesKey);

			//		String respData ="gFAo1BCPgHq28ZXWEkq5smocJsk5zR2OiCSk1Ul469OoR01yiMbv/4eQ8qOHwZeuIS3fwvCvI5C1ohH5oqGtwxuTVDr8LcLlpghOz8aM4Zq1WYl0dyes/DbYV2TrEemcquvZHKp1hUD0FecP9u/SARHAn2VIo+J1wwpzl0dQ4tgIbE5pwMKg6OOXona30HDj64pomWflxyqGbsnZcHLdmA4EZ/LV0MGzng8Z6mQC";


			String decryptedRequest = AESCanaraUtils.decryptAESGCM(encryptedPayload, aesKey);

			System.out.println("decryptedRequest -------------->>> ::  => "+decryptedRequest);


			boolean isVerified = AESCanaraUtils.verify(signaturePayload, signature, neslPublicKey);

			if (isVerified) {
				System.out.println("Signature Verified. Processing the request..."+isVerified);
			}else {
				System.out.println("Signature Verified. Processing the request..."+isVerified);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ;

	}


}
