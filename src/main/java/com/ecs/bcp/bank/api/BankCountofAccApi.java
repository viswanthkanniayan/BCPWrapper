package com.ecs.bcp.bank.api;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Base64;
import java.util.List;

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class BankCountofAccApi {

	public static String getNumberOfAccount(EntityRequest reqXSD) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		List<BankApiLogPojo> bankApi = null;
		List<BalanceConfirmPojo> balancedts = null;
		PropertiesReader prop = new PropertiesReader();
		String response = null;
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();

		try {

			pojo1 = EjbLookUps.getUserDetailsRemote().findByPan(reqXSD.getPan() );


			Date date = new Date();
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fromDate = dt.format(date);
			String toDate = dt.format(date);


/*
			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId", "your_unique_transaction_id_here");
			jsonBody.addProperty("bankId", "your_bank_id_here");
			jsonBody.addProperty("userId", "userId");
			jsonBody.addProperty("password", "password" );
			jsonBody.addProperty("pan", reqXSD.getPan() );
			jsonBody.addProperty("fromDate", fromDate);
			jsonBody.addProperty("ToDate", toDate);
			jsonBody.addProperty("reqName", "reqName");
			jsonBody.addProperty("reqID", "reqID");
			jsonBody.addProperty("reqEntity", "reqEntity");
			jsonBody.addProperty("reqEntityID", "Requestor  Entity PAN or FRN");
			jsonBody.addProperty("reqEmail", "Requestor Email ID");
			jsonBody.addProperty("reqMob", "Requestor Mobile No");
			jsonBody.addProperty("bcpurpose", "Purpose");
			jsonBody.addProperty("reqType", "Requestor Type");
			jsonBody.addProperty("bankId", "your_bank_id_here");
			jsonBody.addProperty("responseURL", "your_response_url_here");
			jsonBody.addProperty("APIpurpose", "API Purpose");

			JsonObject auditeeDetails = new JsonObject();
			auditeeDetails.addProperty("auditeeCompany", "Name of the Company/Customer");
			auditeeDetails.addProperty("auditeAddr", "Auditee Address");
			auditeeDetails.addProperty("aduiteePan", "Auditee Pan");

			jsonBody.add("auditeeDetails", auditeeDetails);



			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("body", jsonBody);           */
			

			Gson gson = new Gson();
		//	String jsonPayloadString = gson.toJson(jsonPayload);

			
			JsonObject jsonBody1 = new JsonObject();
			jsonBody1.addProperty("uniqueTxnId", "31790GO3D2401896ISS001");
			jsonBody1.addProperty("bankId", "UNION");
			jsonBody1.addProperty("pan", "AAACA1111A" );
			jsonBody1.addProperty("fromDate", fromDate);
			jsonBody1.addProperty("ToDate", toDate);
			
			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("body", jsonBody1);
			
			
			String jsonPayloadString = gson.toJson(jsonPayload);

			System.out.println("RequestPayload ------->>  "+jsonPayloadString);
			String aesKey = AESUtils.generateAESKey();


			//	String aesKey = AESUtils.generateAESKey();

			System.out.println("AEC_KEY ------->>  "+aesKey);


			String encryptedPayload = AESUtils.encryptAESGCM(jsonPayloadString, aesKey);

			// String pfxPath = "/data/wildfly-26.1.3.Final/BCP/Keygen//ECS.pfx";

			String baseFolder       = prop.getUrlProperty("baseFolder");

			System.out.println("baseFolder -----"+baseFolder);

			String pfxPath = baseFolder + File.separator +"Keygen"+ File.separator +"Ecs.pfx";

			System.out.println("pfxPath ------->>  "+pfxPath);

			PrivateKey bcpPrivateKey = AESUtils.loadPrivateKeyFromPfx(pfxPath, "Welcome@123");

			String signature = AESUtils.sign(jsonPayloadString, bcpPrivateKey);

			String pemCertificate = "-----BEGIN CERTIFICATE-----\n" +
					"MIIGtzCCBZ+gAwIBAgIMZAIy7rkCW2c6H6QSMA0GCSqGSIb3DQEBCwUAMFAxCzAJ\n" +
					"BgNVBAYTAkJFMRkwFwYDVQQKExBHbG9iYWxTaWduIG52LXNhMSYwJAYDVQQDEx1H\n" +
					"bG9iYWxTaWduIFJTQSBPViBTU0wgQ0EgMjAxODAeFw0yNDAxMDIxMTU5MzFaFw0y\n" +
					"NTAxMjYwNjExMTBaMHUxCzAJBgNVBAYTAklOMRQwEgYDVQQIEwtNYWhhcmFzaHRy\n" +
					"YTEPMA0GA1UEBxMGTXVtYmFpMRwwGgYDVQQKExNVbmlvbiBCYW5rIG9mIEluZGlh\n" +
					"MSEwHwYDVQQDDBgqLnVuaW9uYmFua29maW5kaWEuY28uaW4wggEiMA0GCSqGSIb3\n" +
					"DQEBAQUAA4IBDwAwggEKAoIBAQC60iPh5k0pTQ6rNqQLcyCLODkM0y/6o5bbYNbf\n" +
					"48oHjr4fkKsSExquKhoZOBIZhPrC80KgJe02VMLVX9nvDeZe7NMkxSORR+YSeUF/\n" +
					"Lkq+Z2vjMaUpiMTBtDSymppWa5Gs41zz7vZb73G5i651rlVmuweEprXlfRFh7yaH\n" +
					"gJSQqApdIRzbl+jstsThDGrzPsygVL4CiRaBmu9jtr5OGsjFdr41o+uN3Xr00xUD\n" +
					"IY0SlRz5hiBcCd1oySQIz9bqnTGIjAnK7KzqNderHj3borwB0zTksNbu3TGqN/ce\n" +
					"JEopuNbIJEFsBKVycge+DJQ4YZpvKLCFLVivuXZwfZhsipjhAgMBAAGjggNqMIID\n" +
					"ZjAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH/BAIwADCBjgYIKwYBBQUHAQEEgYEw\n" +
					"fzBEBggrBgEFBQcwAoY4aHR0cDovL3NlY3VyZS5nbG9iYWxzaWduLmNvbS9jYWNl\n" +
					"cnQvZ3Nyc2FvdnNzbGNhMjAxOC5jcnQwNwYIKwYBBQUHMAGGK2h0dHA6Ly9vY3Nw\n" +
					"Lmdsb2JhbHNpZ24uY29tL2dzcnNhb3Zzc2xjYTIwMTgwVgYDVR0gBE8wTTBBBgkr\n" +
					"BgEEAaAyARQwNDAyBggrBgEFBQcCARYmaHR0cHM6Ly93d3cuZ2xvYmFsc2lnbi5j\n" +
					"b20vcmVwb3NpdG9yeS8wCAYGZ4EMAQICMD8GA1UdHwQ4MDYwNKAyoDCGLmh0dHA6\n" +
					"Ly9jcmwuZ2xvYmFsc2lnbi5jb20vZ3Nyc2FvdnNzbGNhMjAxOC5jcmwwOwYDVR0R\n" +
					"BDQwMoIYKi51bmlvbmJhbmtvZmluZGlhLmNvLmlughZ1bmlvbmJhbmtvZmluZGlh\n" +
					"LmNvLmluMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjAfBgNVHSMEGDAW\n" +
					"gBT473/yzXhnqN5vjySNiPGHAwKz6zAdBgNVHQ4EFgQUZGe21dLnK1aXVdimRLsz\n" +
					"jvP8mpcwggF+BgorBgEEAdZ5AgQCBIIBbgSCAWoBaAB2AObSMWNAd4zBEEEG13G5\n" +
					"zsHSQPaWhIb7uocyHf0eN45QAAABjMoLE+MAAAQDAEcwRQIgLWGK0/90np275SA7\n" +
					"zntLXMvxopttr/EaTsY+MoShDRsCIQDdlv9fHnYU5jpwUGlO6sSD7mbM+/rh3TGR\n" +
					"g0bThTTU8wB3AE51oydcmhDDOFts1N8/Uusd8OCOG41pwLH6ZLFimjnfAAABjMoL\n" +
					"FrsAAAQDAEgwRgIhAKCHbcR2vFtONjf/Y/LuJZCSiSxha1LLoWQ6UVaILhDqAiEA\n" +
					"sAisbopQVYyQS22zr0YaDKCCga9ZeZS7GxkwTalfrUcAdQDgkrP8DB3I52g2H95h\n" +
					"uZZNClJ4GYpy1nLEsE2lbW9UBAAAAYzKCxSDAAAEAwBGMEQCIGC99cgFv2lYuB/+ \n" +
					"tkufdbzOT8c7r5Es5UyOETkIJfeqAiAvOFuoezVEpxnYdCG9NApeuAzEf/fWA2yA\n" +
					"QcLSwAh/ZDANBgkqhkiG9w0BAQsFAAOCAQEAWrMrhvur2hoyVlw/n3lpJMxhJBKc\n" +
					"W/rcrldjyOwesph75R5QM+6FC74oSz2X8x/o5AKfy2Bw5yaBOS3CkCM36rY5tBe8\n" +
					"bGWrGu5vozDl7toVOzEOQ2loi2Lf+6VSwg0JwJ7MTub49Z/Czvh/qdWutN5fHlG6\n" +
					"L4Rzk50FL19OFSi1F7kcZLlAVLAsqmGPAdaHrPAp9MViV8ShoRe3f3BI4H856LQm\n" +
					"pwb4mb7lXQ2CekpWZE0wGxPehCqLH7h4FrDY8msCZYsqtufGg/UL0u56Ef0aKy6n\n" +
					"2sQrvl1OLA1Fqqbe+TFps0tTYB0y+Q05tLnAakt8Z9c2ebXofkBQrma4vw==\n" +
					"-----END CERTIFICATE-----";


			PublicKey publicKey = AESUtils.loadPublicKeyFromCertificate(pemCertificate);



			String accsessToken = AESUtils.encryptRSA(aesKey, publicKey);

			//	String encryptedAesKey = "8JFZs9seuRNLsHXYqAcEofY43q0rMN1/RvmsyR3x/lRN1J2vWWGCFAZ/p5nogyC+PWZHPuyCoA==";


			System.out.println("accsessToken ----------->> ::"+accsessToken);

			System.out.println("encryptedPayload----------->> ::"+encryptedPayload);

			System.out.println("signature----------->> ::"+signature);

			response = AESUtils.sendEncryptedRequest(encryptedPayload, signature, accsessToken);

			// System.out.println("Response received: " + response);
		/*	
			
			balancePojo.setTxnId(reqXSD.getTxnId());
			balancePojo.setAccountCount(response.getAccountsCount());
			balancePojo.setBankCharges(response.getBankCharges());

			EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

			BankApiLogPojo  banklog =  new BankApiLogPojo();

			banklog.setAccountsCount(response.getAccountsCount());
			banklog.setBankCharges(response.getBankCharges());
			banklog.setCin(response.getCin());
			banklog.setErrorCode(response.getErrorCode());
			banklog.setLei(response.getLei());
			banklog.setPan(reqXSD.getPan());
			banklog.setUniqueTxnId(response.getUniqueTxnId());

			EjbLookUps.getBankApiLogRemote().create(banklog);
			
			
			

			//		     if (response != null && response.contains("RESPONSE")) {
			//		            System.out.println("Response received: " + response);
			//		        } else {
			//		            System.out.println("Unexpected response: " + response);
			//		        }
			response.getUniqueTxnId();
			response.getAccountsCount(); 
			response.getBankCharges();
			response.getCin();
			response.getErrorCode();
			response.getLei();
			response.setPan(reqXSD.getPan());    */


		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static String getBalanceCertificate (EntityRequest requestXsd) throws Exception {

		List<UserDetailsPojo> pojo1 = null;
		String response = null;

		try {

			pojo1 = EjbLookUps.getUserDetailsRemote().findByPan(requestXsd.getPan() );


			Date date = new Date();
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fromDate = dt.format(requestXsd.getCreationDate());
			String toDate = dt.format(date);



			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId", "your_unique_transaction_id_here");
			jsonBody.addProperty("bankId", "your_bank_id_here");
			//	jsonBody.addProperty("pan", reqXSD.getPan() );
			jsonBody.addProperty("fromDate", fromDate);
			jsonBody.addProperty("ToDate", toDate);
			jsonBody.addProperty("reqName", "reqName");
			jsonBody.addProperty("reqID", "reqID");
			jsonBody.addProperty("reqEntity", "reqEntity");
			jsonBody.addProperty("reqEntityID", "Requestor  Entity PAN or FRN");
			jsonBody.addProperty("reqEmail", "Requestor Email ID");
			jsonBody.addProperty("reqMob", "Requestor Mobile No");
			jsonBody.addProperty("bcpurpose", "Purpose");
			jsonBody.addProperty("reqType", "Requestor Type");
			jsonBody.addProperty("bankId", "your_bank_id_here");
			jsonBody.addProperty("responseURL", "your_response_url_here");
			jsonBody.addProperty("APIpurpose", "API Purpose");

			JsonObject auditeeDetails = new JsonObject();
			auditeeDetails.addProperty("auditeeCompany", "Name of the Company/Customer");
			auditeeDetails.addProperty("aduiteePan", "Auditee Pan");

			jsonBody.add("auditeeDetails", auditeeDetails);



			JsonObject jsonPayload = new JsonObject();
			jsonPayload.add("body", jsonBody);

			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonPayload);

			String aesKey = AESUtils.generateAESKey();
			String encryptedPayload = AESUtils.encryptAESGCM(jsonPayloadString, aesKey);

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey bankPublicKey = keyPair.getPublic();
			PrivateKey bcpPrivateKey = keyPair.getPrivate();

			String signature = AESUtils.sign(jsonPayloadString, bcpPrivateKey);

			String encryptedAesKey = AESUtils.encryptRSA(aesKey, bankPublicKey);

			//	String encryptedAesKey = "8JFZs9seuRNLsHXYqAcEofY43q0rMN1/RvmsyR3x/lRN1J2vWWGCFAZ/p5nogyC+PWZHPuyCoA==";

			System.out.println("encryptedAesKey ----------->> ::"+encryptedAesKey);

			System.out.println("encryptedPayload----------->> ::"+encryptedPayload);

			System.out.println("signature----------->> ::"+signature);

			System.out.println("bcpPrivateKey----------->> ::"+bcpPrivateKey.toString());

			//	    response = AESUtils.sendEncryptedRequest(encryptedPayload, signature, encryptedAesKey);

			if (response != null && response.contains("RESPONSE")) {
				System.out.println("Response received: " + response);
			} else {
				System.out.println("Unexpected response: " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSZ");
		String fromDate = sdf.format(date);
		String toDate = sdf.format(date);


		JsonObject jsonBody = new JsonObject();
		jsonBody.addProperty("uniqueTxnId", "your_unique_transaction_id_here");
		jsonBody.addProperty("bankId", "your_bank_id_here");
		jsonBody.addProperty("pan", "PAN" );
		jsonBody.addProperty("fromDate", fromDate);
		jsonBody.addProperty("ToDate", toDate);
		jsonBody.addProperty("reqName", "reqName");
		jsonBody.addProperty("reqID", "reqID");
		jsonBody.addProperty("reqEntity", "reqEntity");
		jsonBody.addProperty("reqEntityID", "Requestor  Entity PAN or FRN");
		jsonBody.addProperty("reqEmail", "Requestor Email ID");
		jsonBody.addProperty("reqMob", "Requestor Mobile No");
		jsonBody.addProperty("bcpurpose", "Purpose");
		jsonBody.addProperty("reqType", "Requestor Type");
		jsonBody.addProperty("bankId", "your_bank_id_here");
		jsonBody.addProperty("responseURL", "your_response_url_here");
		jsonBody.addProperty("APIpurpose", "API Purpose");

		JsonObject auditeeDetails = new JsonObject();
		auditeeDetails.addProperty("auditeeCompany", "Name of the Company/Customer");
		auditeeDetails.addProperty("aduiteePan", "Auditee Pan");

		jsonBody.add("auditeeDetails", auditeeDetails);

		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("body", jsonBody);

		Gson gson = new Gson();
		String jsonPayloadString = gson.toJson(jsonPayload);

		System.out.println("RequestJson ::------>> :: \n"+jsonPayloadString);


		try {
			String payload = "{\r\n"
					+ "    \"Request\": {\r\n"
					+ "        \"body\": {\r\n"
					+ "            \"encryptData\": {\r\n"
					+ "                \"uniqueTxnId\": \"123\",\r\n"
					+ "                \"bankId\": \"CANANRA\",\r\n"
					+ "                \"pan\": \"AAACA1111A\",\r\n"
					+ "                \"fromDate\": \"01-Apr-2022\",\r\n"
					+ "                \"toDate\": \"31-Apr-2023\"\r\n"
					+ "            }\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "}";


			String aesKey = AESUtils.generateAESKey();
			
		//	String aesKey = "2b2700b804faa4084b4f539cec9f980716db4d729f2a40cbaaaf9ca012804b43";

			System.out.println("aesKey ::------>> :: "+aesKey);

			String encryptedPayload = AESUtils.encryptAESGCM(payload, aesKey);

			System.out.println("encryptedPayload "+encryptedPayload);

			// Load public key from .cer file
			PublicKey bankPublicKey = AESUtils.loadPublicKeyFromCer("C:\\Users\\91938\\Desktop\\Keygen\\ECS.cer");

			String pfxPath = "C:\\Users\\91938\\Desktop\\Keygen\\ECS.pfx";
			PrivateKey bcpPrivateKey = AESUtils.loadPrivateKeyFromPfx(pfxPath, "Welcome@123");



			String signature = AESUtils.sign(payload, bcpPrivateKey);

			System.out.println("bankPublicKey ::------>> :: "+Base64.getEncoder().encodeToString(bankPublicKey.getEncoded()));

			System.out.println("bcpPrivateKey ::------>> :: "+Base64.getEncoder().encodeToString(bcpPrivateKey.getEncoded()));

			System.out.println("signature ::------>> :: "+signature);


			String encryptedAesKey = AESUtils.encryptRSA(aesKey, bankPublicKey);

			System.out.println("encryptedAesKey ::------>> :: "+encryptedAesKey);


			String decryptedAesKey = AESUtils.decryptRSA(encryptedAesKey, bcpPrivateKey);

			String decryptedRequest = AESUtils.decryptAESGCM(encryptedPayload, decryptedAesKey);


			String finaldecry = AESUtils.decryptAESGCM(encryptedPayload, aesKey);


			System.out.println("BankCountofAccApi :::"+finaldecry);

			boolean isVerified = AESUtils.verify(decryptedRequest, signature, bankPublicKey);

			if (isVerified) {
				System.out.println("Signature Verified. Processing the request..."+isVerified);
			}else {
				System.out.println("Signature Verified. Processing the request..."+isVerified);
			}

		}catch (Exception e) {
			// TODO: handle exception
		}

		return ;




	}
}
