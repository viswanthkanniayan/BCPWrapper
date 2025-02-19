package com.ecs.bcp.bank.api.canara;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Random;

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

public class BankOtpAPI {
	
	private static final Random RANDOM = new Random();
	
	
	public static EntityResponce BankGenerateOtp(EntityRequest reqXSD) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		List<BankApiLogPojo> bankApi = null;
		List<BalanceConfirmPojo> balancedts = null;
		SettingsPojo setPojo = null;
		EntityResponce response = new EntityResponce();
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();

		try {
			
			SettingsPojo NESLPublicKey = EjbLookUps.getSettingsRemote().findById("NESLPublicKey");

			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
			
			BankApiMasterPojo BANKPublicKey = EjbLookUps.getBankApiMasterRemote().findById(reqXSD.getBankname());


		//	pojo1 = EjbLookUps.getUserDetailsRemote().findByPan(reqXSD.getPan() );
			
			String uniqueTxnId = generateUniqueTxnId();

			JsonObject jsonBody2 = new JsonObject();
			jsonBody2.addProperty("uniqueTxnRefNo", uniqueTxnId);
			jsonBody2.addProperty("auditeePan", reqXSD.getPan());
			jsonBody2.addProperty("bankRegisteredMobileNo", reqXSD.getMobileNo() );
			jsonBody2.addProperty("bankRegisteredEmailId", reqXSD.getEmailId());
			
			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("encryptData", jsonBody2);

			JsonObject jsonbody = new JsonObject();
			jsonbody.add("body", jsonPayload);

			JsonObject jsonRequest = new JsonObject();
			jsonRequest.add("Request", jsonbody);


			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody2);

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


				response = AESCanaraUtils2.sendOtpEncryptedRequest(encryptedPayload, signature, accsessToken ,NESLPublicKey.getBinaryValue()
						,neslPrivateKey,reqXSD.getBankname(),reqXSD.getTxnId(),reqXSD.getPan(),"GenerateOTP",uniqueTxnId);

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
	
	public static EntityResponce BankValidateOtp(EntityRequest reqXSD) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		List<BankApiLogPojo> bankApi = null;
		List<BalanceConfirmPojo> balancedts = null;
		SettingsPojo setPojo = null;
		EntityResponce response = new EntityResponce();
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();

		try {
			
			SettingsPojo NESLPublicKey = EjbLookUps.getSettingsRemote().findById("NESLPublicKey");

			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
			
			BankApiMasterPojo BANKPublicKey = EjbLookUps.getBankApiMasterRemote().findById(reqXSD.getBankname());


		//	pojo1 = EjbLookUps.getUserDetailsRemote().findByPan(reqXSD.getPan() );
			
			
			JsonObject jsonBody3 = new JsonObject();
			jsonBody3.addProperty("uniqueTxnRefNo", reqXSD.getUniqueTxnId());
			jsonBody3.addProperty("bankRefNo", reqXSD.getBankRefNo());
			jsonBody3.addProperty("otpValue", reqXSD.getOtpValue() );
			
			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("encryptData", jsonBody3);

			JsonObject jsonbody = new JsonObject();
			jsonbody.add("body", jsonPayload);

			JsonObject jsonRequest = new JsonObject();
			jsonRequest.add("Request", jsonbody);


			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody3);

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


				response = AESCanaraUtils2.sendOtpEncryptedRequest(encryptedPayload, signature, accsessToken ,NESLPublicKey.getBinaryValue()
						,neslPrivateKey,reqXSD.getBankname(),reqXSD.getTxnId(),reqXSD.getPan(),"ValidateOTP",reqXSD.getUniqueTxnId());

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


}
