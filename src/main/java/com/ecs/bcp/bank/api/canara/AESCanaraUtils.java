package com.ecs.bcp.bank.api.canara;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.bank.api.AESUtils;
import com.ecs.bcp.bank.api.canara.xsd.BankOtpResponse;
import com.ecs.bcp.bank.api.canara.xsd.BankResponseAPI1;
import com.ecs.bcp.bank.api.canara.xsd.BankResponseAPI2;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.BankApiMasterPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.xsd.EntityResponce;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class AESCanaraUtils {
	
	private static final Random RANDOM = new Random();

	// Key Generation for AES
	public static String generateAESKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		SecretKey secretKey = keyGenerator.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	// AES Encryption
	public static String encryptAESGCM(String plaintext, String base64Key) throws Exception {
		byte[] key = Base64.getDecoder().decode(base64Key);
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		byte[] iv = new byte[12];
		new SecureRandom().nextBytes(iv);
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
		byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

		byte[] ivAndEncrypted = new byte[iv.length + encryptedBytes.length];
		System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
		System.arraycopy(encryptedBytes, 0, ivAndEncrypted, iv.length, encryptedBytes.length);

		return Base64.getEncoder().encodeToString(ivAndEncrypted);
	}

	// AES Decryption
	public static String decryptAESGCM(String encryptedRequestBase64, String base64Key) throws Exception {
		byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedRequestBase64);
		byte[] key = Base64.getDecoder().decode(base64Key);
		// System.out.println("Decoded AES Key : " + Base64.getEncoder().encodeToString(key));

		byte[] iv = new byte[12];
		byte[] encryptedBytes = new byte[ivAndEncrypted.length - 12];
		System.arraycopy(ivAndEncrypted, 0, iv, 0, 12);
		System.arraycopy(ivAndEncrypted, 12, encryptedBytes, 0, encryptedBytes.length);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		//	 System.out.println("Decrypted Bytes: " + new String(decryptedBytes));

		return new String(decryptedBytes);
	}

	// RSA Key Encryption
	public static String encryptRSA(String aesKeyBase64, PublicKey publicKey) throws Exception {
		byte[] aesKey = Base64.getDecoder().decode(aesKeyBase64);

		//	Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedKey = cipher.doFinal(aesKey);
		return Base64.getEncoder().encodeToString(encryptedKey);
	}

	// RSA Key Decryption
	public static String decryptRSA(String encryptedKeyBase64, PrivateKey privateKey) throws Exception {
		byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		//	Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedKey = cipher.doFinal(encryptedKey);
		return Base64.getEncoder().encodeToString(decryptedKey);
	}

	// Digital Signature
	public static String sign(String data, PrivateKey privateKey) throws Exception {
		Signature signer = Signature.getInstance("SHA256withRSA");
		signer.initSign(privateKey);
		signer.update(data.getBytes());
		byte[] signature = signer.sign();
		return Base64.getEncoder().encodeToString(signature);
	}

	// Verify Digital Signature
	public static boolean verify(String data, String signatureBase64, PublicKey publicKey) throws Exception {
		Signature verifier = Signature.getInstance("SHA256withRSA");
		verifier.initVerify(publicKey);
		verifier.update(data.getBytes());
		byte[] signature = Base64.getDecoder().decode(signatureBase64);

		//	System.out.println("VERIFY Signature ::------>> :: "+Base64.getEncoder().encodeToString(signature));
		return verifier.verify(signature);
	}

	// key fetching
	public static PublicKey loadPublicKeyFromCertificate(String pemCertificate) throws Exception {
		String base64Certificate = pemCertificate
				.replace("-----BEGIN CERTIFICATE-----", "")
				.replace("-----END CERTIFICATE-----", "")
				.replaceAll("\\s+", "").replaceAll("=+$", "");

		byte[] certBytes = Base64.getDecoder().decode(base64Certificate);

		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes)) {
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
			return certificate.getPublicKey();
		}
	}


	// Load Public Key from .cer
	public static PublicKey loadPublicKeyFromCer(String cerPath) throws Exception {
		FileInputStream fis = new FileInputStream(cerPath);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);
		fis.close();
		return certificate.getPublicKey();
	}

	// Load Private Key from .pfx
	public static PrivateKey loadPrivateKeyFromPfx(String pfxPath, String pfxPassword) throws Exception {
		System.out.println("pfxpath inside method => " + pfxPath);
		FileInputStream fis = new FileInputStream(pfxPath);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(fis, pfxPassword.toCharArray());
		fis.close();

		String alias = keyStore.aliases().nextElement();
		return (PrivateKey) keyStore.getKey(alias, pfxPassword.toCharArray());
	}

/*	public static PublicKey loadPublicKeyFromString(String base64PublicKey) throws Exception {
		byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
		return keyFactory.generatePublic(keySpec);
	}  */
	
	
	
	public static PublicKey loadPublicKey(String input) throws Exception {
	    // Check if the input is in PEM format (contains BEGIN CERTIFICATE)
	    if (input.contains("-----BEGIN CERTIFICATE-----")) {
	        // Handle PEM certificate input
	        String base64Certificate = input
	                .replace("-----BEGIN CERTIFICATE-----", "")
	                .replace("-----END CERTIFICATE-----", "")
	                .replaceAll("\\s+", "");
//	                .replaceAll("=+$", "");
	        
	        

	    //    System.out.println("Base64 Certificate: " + base64Certificate);
	       
	        byte[] certBytes = Base64.getDecoder().decode(base64Certificate);
	        

	        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes)) {
	            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
	            return certificate.getPublicKey();
	        }
	    } else {
	        // Handle Base64 encoded public key input
	        byte[] decodedKey = Base64.getDecoder().decode(input);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
	        return keyFactory.generatePublic(keySpec);
	    }
	}
	
	public static PublicKey getRSAPublicKeyFromPem(String base64Key) throws Exception {
		
		
        String base64KeyCleaned = base64Key.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                           .replaceAll("-----END PUBLIC KEY-----", "")
                                           .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(base64KeyCleaned);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

	  public static PublicKey loadPublicKey2(String input) throws Exception {
	        String key = input.replaceAll("-----BEGIN PUBLIC KEY-----", "")
	                        .replaceAll("-----END PUBLIC KEY-----", "")
	                        .replaceAll("\\s+", "");

	        byte[] decoded = Base64.getDecoder().decode(key);
	        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        return keyFactory.generatePublic(spec);
	    }

	public static PrivateKey loadPrivateKeyFromString(String base64PrivateKey) throws Exception {
		byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
		return keyFactory.generatePrivate(keySpec);
	}

	public static String genRandNo(int digit){
		String characters = "0123456789";
		String otp = RandomStringUtils.random(digit, characters);
		return otp;
	}

	@SuppressWarnings("deprecation")
	public static EntityResponce sendEncryptedRequest(String PayloadString,String encryptedPayload, String signature, String accsessToken,String publicKey , PrivateKey privateKey ,String bank ,String reqXSD ,String pan ,String APIpurpose) throws Exception {

		EntityResponce res = new EntityResponce() ;
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
		BankApiMasterPojo Bank = EjbLookUps.getBankApiMasterRemote().findById(bank);

		System.out.println(Bank.getBankName());

		//	OkHttpClient client = new OkHttpClient().newBuilder().build();

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
				.readTimeout(60, TimeUnit.SECONDS)    // Read timeout
				.writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
				.build();

		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("encryptData",  new JsonPrimitive(encryptedPayload));

		JsonObject jsonbody = new JsonObject();
		jsonbody.add("body", jsonPayload);

		JsonObject jsonRequest = new JsonObject();
		jsonRequest.add("Request", jsonbody);

		Gson gson = new Gson();
		String jsonPayloadString = gson.toJson(jsonRequest);   



		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, jsonPayloadString );

		String clientId =Bank.getClientId();

		System.out.println("clientId   --------- "+ clientId);

		String ClientSecret =Bank.getClientSecret();

		System.out.println("ClientSecret   --------- "+ ClientSecret);

		String URL =Bank.getCountUrl();

		System.out.println("URL   ---------  ==>  "+ Bank.getCountUrl());

		Request request = new Request.Builder()
				.url(URL)
				.method("POST", body)
				.addHeader("x-client-id", clientId != null ? clientId : "")
				.addHeader("x-client-secret", ClientSecret != null ? ClientSecret : "")
				.addHeader("x-client-certificate", publicKey)
				.addHeader("JWS",signature )
				.addHeader("Content-Type", "application/json")
				.addHeader("x-api-interaction-id", "123")
				.addHeader("x-timestamp", "abc")
				.addHeader("AccessToken", accsessToken)
				.build();

		System.out.println("Request URL: " + request.url());
		System.out.println("Request Method: " + request.method());

		System.out.println("Request Headers: ");
		Headers headers = request.headers();
		for (String name : headers.names()) {
			System.out.println(name + ": " + headers.get(name));
		}


		System.out.println("Request Body: ");
		try {
			if (request.body() != null) {
				Buffer buffer = new Buffer();
				request.body().writeTo(buffer);
				System.out.println(buffer.readUtf8());
			} else {
				System.out.println("No request body.");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Response response = client.newCall(request).execute()) {

			if (response.body() != null) {
				String responseBody = response.body().string();
				System.out.println("Response Body: " + responseBody);

				String accessToken = response.header("AccessToken");
				if (accessToken == null) {
					//	throw new Exception("AccessToken not found in response header");
					System.out.println("Access token not found");

					res.setTxnId(reqXSD);
					res.setStatus("FAILED");
					res.setError(true);

					balancePojo.setTxnId(reqXSD);
					balancePojo.setStatus("FAILED");

					EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
					return res;

				}
				System.out.println("AccessToken: " + accessToken);

				String Jws = response.header("JMS");
				if (Jws == null) {
					System.out.println("JMS not found");
				}else {
					System.out.println("JMS: " + Jws);
				}

				if(accessToken!=null) {
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
					JsonObject responseObj = jsonObject.getAsJsonObject("Response");
					JsonObject bodyObj = responseObj.getAsJsonObject("body");
					String encryptedDataBase64 = bodyObj.get("encryptData").getAsString();

					String decryptedAESKeyBase64 = decryptRSA(accessToken, privateKey);
					System.out.println("Decrypted AES Key: " + decryptedAESKeyBase64);

					String decryptedResponse = decryptAESGCM(encryptedDataBase64, decryptedAESKeyBase64);

					System.out.println("Decrypted Response: " + decryptedResponse);

					JsonObject decryptedJson = jsonParser.parse(decryptedResponse).getAsJsonObject();


					if(APIpurpose.equalsIgnoreCase("01")) {


						BankResponseAPI1 resp = gson.fromJson(decryptedResponse, BankResponseAPI1.class);

						if(resp != null) {

							if (resp.getAccountsCount() == null || resp.getAccountsCount().isEmpty() || resp.getAccountsCount().equalsIgnoreCase("0")) {
								res.setTxnId(reqXSD);
								res.setStatus("FAILED");
								res.setError(true);

								balancePojo.setTxnId(reqXSD);
								balancePojo.setStatus("FAILED");

								EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
								return res;
							}
							if (resp.getBankCharges() == null || resp.getBankCharges().isEmpty() || resp.getBankCharges().equalsIgnoreCase("0")) {
								res.setTxnId(reqXSD);
								res.setStatus("FAILED");
								res.setError(true);

								balancePojo.setTxnId(reqXSD);
								balancePojo.setStatus("FAILED");

								EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
								return res;
							}
							String bankCha =resp.getBankCharges();
							String accCount =resp.getAccountsCount();

							res.setUniqueTxnId(resp.getUniqueTxnId());
							res.setLei(resp.getLei());
							res.setAccountsCount(accCount);
							res.setBankCharges(bankCha);
							res.setCin(resp.getCin());
							res.setErrorCode(resp.getErrorCode());
							res.setError(false);


							balancePojo.setTxnId(reqXSD);
							balancePojo.setAccountCount(accCount);
							balancePojo.setBankCharges(bankCha);
							balancePojo.setApiPurpose(APIpurpose);
							balancePojo.setUniqueTxnId(resp.getUniqueTxnId());

							EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

							BankApiLogPojo  banklog =  new BankApiLogPojo();

							banklog.setAccountsCount(accCount);
							banklog.setBankCharges(bankCha);
							banklog.setBank(bank);
							banklog.setCin(resp.getCin());
							banklog.setErrorCode(resp.getErrorCode());
							banklog.setLei(resp.getLei());
							banklog.setPan(pan);
							banklog.setUniqueTxnId(resp.getUniqueTxnId());
							banklog.setApiPurpose(APIpurpose);
							banklog.setBankApiRequest(PayloadString);
							banklog.setBankApiResponse(decryptedResponse);

							EjbLookUps.getBankApiLogRemote().create(banklog);
							
							return res;

						}else {

							res.setTxnId(reqXSD);
							res.setStatus("FAILED");
							res.setError(true);

							balancePojo.setTxnId(reqXSD);
							balancePojo.setStatus("FAILED");

							EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
							return res;
						}

					}else {


						BankResponseAPI2 resp = gson.fromJson(decryptedResponse, BankResponseAPI2.class);


						res.setUniqueTxnId(resp.getUniqueTxnId());
						res.setBank(resp.getBank());
						res.setAckStatus(resp.getAckStatus());
						res.setError(false);


						balancePojo.setTxnId(reqXSD);
						balancePojo.setBank(resp.getBank());
						balancePojo.setAckStatus(resp.getAckStatus());
						balancePojo.setApiPurpose(APIpurpose);

						EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

						BankApiLogPojo  banklog =  new BankApiLogPojo();

						banklog.setAckStatus(resp.getAckStatus());
						banklog.setBank(resp.getBank());
						banklog.setPan(pan);
						banklog.setUniqueTxnId(resp.getUniqueTxnId());
						banklog.setApiPurpose(APIpurpose);
						banklog.setBank(bank);
						banklog.setBankApiRequest(PayloadString);
						banklog.setBankApiResponse(decryptedResponse);

						EjbLookUps.getBankApiLogRemote().create(banklog);
						
						return res;
					}


				}
			} else {

				res.setTxnId(reqXSD);
				res.setStatus("FAILED");
				res.setErrorDescription("No response body from API at URL: " + request.url());
				res.setError(true);

				balancePojo.setTxnId(reqXSD);
				balancePojo.setStatus("FAILED");

				EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
				return res;
			}                                                     

		} catch (Exception e) {

			res.setTxnId(reqXSD);
			res.setStatus("FAILED");
			res.setErrorDescription("Error processing response from API at URL: " + request.url() + " - " + e.getMessage());
			res.setError(true);

			balancePojo.setTxnId(reqXSD);
			balancePojo.setStatus("FAILED");

			EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
		}
		return res;

	}
	
	
	@SuppressWarnings("deprecation")
	public static EntityResponce sendOtpEncryptedRequest(String encryptedPayload, String signature, String accsessToken,String publicKey , PrivateKey privateKey ,String bank ,String reqXSD ,String pan ,String APIpurpose ,String uniqueTxnId) throws Exception {
	
		EntityResponce res = null ;
		String URL = null;
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
		BankApiMasterPojo Bank = EjbLookUps.getBankApiMasterRemote().findById(bank);
	
		System.out.println(Bank.getBankName());
	
	//	OkHttpClient client = new OkHttpClient().newBuilder().build();
		
	
		  OkHttpClient client = new OkHttpClient.Builder()
	                .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
	                .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
	                .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
	                .build();
		
		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("encryptData",  new JsonPrimitive(encryptedPayload));
	
		JsonObject jsonbody = new JsonObject();
		jsonbody.add("body", jsonPayload);
	
		JsonObject jsonRequest = new JsonObject();
		jsonRequest.add("Request", jsonbody);
		
		Gson gson = new Gson();
		String jsonPayloadString = gson.toJson(jsonRequest);
	
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, jsonPayloadString );
	
		String clientId =Bank.getClientId();
	
		String ClientSecret =Bank.getClientSecret();
		
		if(APIpurpose.equalsIgnoreCase("GenerateOTP")) {
			
		   URL =Bank.getBankGenerateOtpURL();
		}else {
			URL =Bank.getBankvalidateOtpURL();
		}
	
	    if(URL != null || isEmpty(URL)) {
	    	
		System.out.println(APIpurpose +"   URL   ---------  ==>  "+URL);
	
		Request request = new Request.Builder()
				.url(URL)
				.method("POST", body)
				.addHeader("x-client-id", clientId != null ? clientId : "")
				.addHeader("x-client-secret", ClientSecret != null ? ClientSecret : "")
				.addHeader("x-client-certificate", publicKey)
				.addHeader("JWS",signature )
				.addHeader("Content-Type", "application/json")
				.addHeader("x-api-interaction-id", "123")
				.addHeader("x-timestamp", "abc")
				.addHeader("AccessToken", accsessToken)
				.build();
		
		System.out.println("Request URL: " + request.url());
		System.out.println("Request Method: " + request.method());
	
		System.out.println("Request Headers: ");
		Headers headers = request.headers();
		for (String name : headers.names()) {
			System.out.println(name + ": " + headers.get(name));
		}
	
	
		System.out.println("Request Body: ");
		try {
			if (request.body() != null) {
				Buffer buffer = new Buffer();
				request.body().writeTo(buffer);
				System.out.println(buffer.readUtf8());
			} else {
				System.out.println("No request body.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		try (Response response = client.newCall(request).execute()) {
	
			if (response.body() != null) {
				String responseBody = response.body().string();
				System.out.println("Response Body: " + responseBody);
				
				
				 if (response.code() == 429 || responseBody.contains("\"httpCode\":\"429\"")) {
			            System.out.println("Rate limit exceeded. Returning error response.");
	
			            EntityResponce errorResponse = new EntityResponce();
			            errorResponse.setError(true);
			            errorResponse.setErrorCode("429");
			            errorResponse.setErrorMessage("Rate Limit exceeded");
			            errorResponse.setErrorDescription("Too Many Requests");
	
			            return errorResponse; 
			        }
	
				String accessToken = response.header("AccessToken");
				if (accessToken == null) {
				//	throw new Exception("AccessToken not found in response header");
					System.out.println("access token not found");
				}
				System.out.println("AccessToken: " + accessToken);
	
				if(accessToken!=null) {
				JsonParser jsonParser = new JsonParser();
				JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
				JsonObject responseObj = jsonObject.getAsJsonObject("Response");
				JsonObject bodyObj = responseObj.getAsJsonObject("body");
				String encryptedDataBase64 = bodyObj.get("encryptData").getAsString();
	
				String decryptedAESKeyBase64 = decryptRSA(accessToken, privateKey);
				System.out.println("Decrypted AES Key: " + decryptedAESKeyBase64);
	
				String decryptedResponse = decryptAESGCM(encryptedDataBase64, decryptedAESKeyBase64);
	
				System.out.println("Decrypted Response: " + decryptedResponse);
	
				JsonObject decryptedJson = jsonParser.parse(decryptedResponse).getAsJsonObject();
	
				EntityResponce jsonRequest1 = new EntityResponce();
				
				
				if(APIpurpose.equalsIgnoreCase("GenerateOTP")) {
				
				BankOtpResponse resp = gson.fromJson(decryptedResponse, BankOtpResponse.class);
				
				jsonRequest1.setUniqueTxnId(resp.getUniqueTxnRefNo());
				jsonRequest1.setBankRefNo(resp.getBankRefNo());
				jsonRequest1.setBank(bank);
				jsonRequest1.setBankAction(resp.getBankAction());
				
				jsonRequest1.setErrorCode(resp.getErrorCode());
				
		/*		if(resp.getErrorCode() != null) {
					
					jsonRequest1.setError(true);
				}else {
					jsonRequest1.setError(false);
				}   */
				
				
				if (resp.getBankAction() == null || 
			            (!resp.getBankAction().equalsIgnoreCase("03") && 
			             !resp.getBankAction().equalsIgnoreCase("02") && 
			             !resp.getBankAction().equalsIgnoreCase("01"))) {
			            jsonRequest1.setError(true);
			        } else {
			            jsonRequest1.setError(false);
			        }
				
				/*
				 * balancePojo.setTxnId(reqXSD); balancePojo.setApiPurpose(APIpurpose);
				 * balancePojo.setUniqueTxnId(resp.getUniqueTxnRefNo());
				 * 
				 * EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
				 */
	
				BankApiLogPojo  banklog =  new BankApiLogPojo();
				
	
				banklog.setBankRefNo(resp.getBankRefNo());
				banklog.setBankLinkageStatus(resp.getBankLinkageStatus());
				banklog.setErrorCode(resp.getErrorCode());
				banklog.setBankAction(resp.getBankAction());
				banklog.setPan(pan);
				banklog.setBank(bank);
				banklog.setUniqueTxnId(resp.getUniqueTxnRefNo());
				banklog.setApiPurpose(APIpurpose);
	
				EjbLookUps.getBankApiLogRemote().create(banklog);
				
				}else {
					
					
					BankOtpResponse resp = gson.fromJson(decryptedResponse, BankOtpResponse.class);
					
					
					
					jsonRequest1.setUniqueTxnId(resp.getUniqueTxnRefNo());
					jsonRequest1.setBankRefNo(resp.getBankRefNo());
					jsonRequest1.setBank(bank);
					jsonRequest1.setBankAction(resp.getBankAction());
					
					if((resp.getBankLinkageStatus() == null) || (!resp.getBankLinkageStatus().equalsIgnoreCase("01")) ) {
						jsonRequest1.setError(true);
					}else {
						jsonRequest1.setError(false);
					}
					jsonRequest1.setBankLinkageStatus(resp.getBankLinkageStatus() != null ? resp.getBankLinkageStatus() : "null");
					jsonRequest1.setErrorCode(resp.getErrorCode());
					
	
					BankApiLogPojo  banklog =  new BankApiLogPojo();
					
	
					banklog.setBankRefNo(resp.getBankRefNo());
					banklog.setBankLinkageStatus(resp.getBankLinkageStatus());
					banklog.setErrorCode(resp.getErrorCode());
					banklog.setBankAction(resp.getBankAction());
					banklog.setPan(pan);
					banklog.setBank(bank);
					banklog.setUniqueTxnId(resp.getUniqueTxnRefNo());
					banklog.setApiPurpose(APIpurpose);
	
					EjbLookUps.getBankApiLogRemote().create(banklog);
					
				}
				
				
				res = jsonRequest1;
				return res;
				
				
				}
				else {
					System.out.println("failed response");
				}
	
			} else {
				throw new Exception("No response body from API at URL: " + request.url());
			}
	
		} catch (Exception e) {
			throw new Exception("Error processing response from API at URL: " + request.url() + " - " + e.getMessage(), e);
		}
	    }
		return res;
	}

	@SuppressWarnings("deprecation")
	public static EntityResponce sendEncryptedTestRequest(String encryptedPayload, String signature, String accsessToken,String publicKey , PrivateKey privateKey ,String bank ,String reqXSD ,String pan ,String APIpurpose) throws Exception {

		EntityResponce res = null ;
		BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
		BankApiMasterPojo Bank = EjbLookUps.getBankApiMasterRemote().findById(bank);

		System.out.println(Bank.getBankName());

//		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		  OkHttpClient client = new OkHttpClient.Builder()
	                .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
	                .readTimeout(60, TimeUnit.SECONDS)    // Read timeout
	                .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
	                .build();
		  
		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("encryptData",  new JsonPrimitive(encryptedPayload));

		JsonObject jsonbody = new JsonObject();
		jsonbody.add("body", jsonPayload);

		JsonObject jsonRequest = new JsonObject();
		jsonRequest.add("Request", jsonbody);
		
		Gson gson = new Gson();
		String jsonPayloadString = gson.toJson(jsonRequest);

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, jsonPayloadString );

		String clientId =Bank.getClientId();

		System.out.println("clientId   --------- "+ clientId);

		String ClientSecret =Bank.getClientSecret();

		System.out.println("ClientSecret   --------- "+ ClientSecret);

		String URL =Bank.getCountUrl();

		System.out.println("URL   ---------  ==>  "+ Bank.getCountUrl());

		Request request = new Request.Builder()
				.url(URL)
				.method("POST", body)
				.addHeader("x-client-id", clientId != null ? clientId : "")
				.addHeader("x-client-secret", ClientSecret != null ? ClientSecret : "")
				.addHeader("x-client-certificate", publicKey)
				.addHeader("JWS",signature )
				.addHeader("Content-Type", "application/json")
				.addHeader("x-api-interaction-id", "123")
				.addHeader("x-timestamp", "abc")
				.addHeader("AccessToken", accsessToken)
				.build();
		
		System.out.println("Request URL: " + request.url());
		System.out.println("Request Method: " + request.method());

		System.out.println("Request Headers: ");
		Headers headers = request.headers();
		for (String name : headers.names()) {
			System.out.println(name + ": " + headers.get(name));
		}


		System.out.println("Request Body: ");
		try {
			if (request.body() != null) {
				Buffer buffer = new Buffer();
				request.body().writeTo(buffer);
				System.out.println(buffer.readUtf8());
			} else {
				System.out.println("No request body.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Response response = client.newCall(request).execute()) {

			if (response.body() != null) {
				String responseBody = response.body().string();
				System.out.println("Response Body: " + responseBody);

				String accessToken = response.header("AccessToken");
				if (accessToken == null) {
				//	throw new Exception("AccessToken not found in response header");
					System.out.println("access token not found");
				}
				
				Headers headers1 = response.headers();
				for (String name : headers1.names()) {
					System.out.println("Response  Headers  :"+name + ": " + headers1.get(name));
				}
				
				System.out.println("AccessToken: " + accessToken);

				if(accessToken!=null) {
				JsonParser jsonParser = new JsonParser();
				JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
				JsonObject responseObj = jsonObject.getAsJsonObject("Response");
				JsonObject bodyObj = responseObj.getAsJsonObject("body");
				String encryptedDataBase64 = bodyObj.get("encryptData").getAsString();

				String decryptedAESKeyBase64 = decryptRSA(accessToken, privateKey);
				System.out.println("Decrypted AES Key: " + decryptedAESKeyBase64);

				String decryptedResponse = decryptAESGCM(encryptedDataBase64, decryptedAESKeyBase64);

				System.out.println("Decrypted Response: " + decryptedResponse);

				JsonObject decryptedJson = jsonParser.parse(decryptedResponse).getAsJsonObject();

				EntityResponce jsonRequest1 = new EntityResponce();
				
				if(APIpurpose.equalsIgnoreCase("01")) {
					
				
				BankResponseAPI1 resp = gson.fromJson(decryptedResponse, BankResponseAPI1.class);
				
				
				String accCount =resp.getAccountsCount();
				String bankCha =resp.getBankCharges();
		
				jsonRequest1.setUniqueTxnId(resp.getUniqueTxnId());
				jsonRequest1.setLei(resp.getLei());
				jsonRequest1.setAccountsCount(accCount);
				jsonRequest1.setBankCharges(bankCha);
				jsonRequest1.setCin(resp.getCin());
				jsonRequest1.setErrorCode(resp.getErrorCode());
				
				
				balancePojo.setTxnId(reqXSD);
				balancePojo.setAccountCount(accCount);
				balancePojo.setBankCharges(bankCha);
				balancePojo.setApiPurpose(APIpurpose);

				EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

				BankApiLogPojo  banklog =  new BankApiLogPojo();

				banklog.setAccountsCount(accCount);
				banklog.setBankCharges(bankCha);
				banklog.setCin(resp.getCin());
				banklog.setErrorCode(resp.getErrorCode());
				banklog.setLei(resp.getLei());
				banklog.setPan(pan);
				banklog.setUniqueTxnId(resp.getUniqueTxnId());
				banklog.setApiPurpose(APIpurpose);

				EjbLookUps.getBankApiLogRemote().create(banklog);
				
				}else {
					
					
					BankResponseAPI2 resp = gson.fromJson(decryptedResponse, BankResponseAPI2.class);
					
					
					jsonRequest1.setUniqueTxnId(resp.getUniqueTxnId());
					jsonRequest1.setBank(resp.getBank());
					jsonRequest1.setAckStatus(resp.getAckStatus());
					
					
					balancePojo.setTxnId(reqXSD);
					balancePojo.setBank(resp.getBank());
					balancePojo.setAckStatus(resp.getAckStatus());
					balancePojo.setApiPurpose(APIpurpose);

					EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

					BankApiLogPojo  banklog =  new BankApiLogPojo();

					banklog.setAckStatus(resp.getAckStatus());
					banklog.setBank(resp.getBank());
					banklog.setPan(pan);
					banklog.setUniqueTxnId(resp.getUniqueTxnId());
					banklog.setApiPurpose(APIpurpose);

					EjbLookUps.getBankApiLogRemote().create(banklog);
				}
				
				
				res = jsonRequest1;
				return res;
				
				
				}
				else {
					System.out.println("failed response");
				}

			} else {
				throw new Exception("No response body from API at URL: " + request.url());
			}

		} catch (Exception e) {
			throw new Exception("Error processing response from API at URL: " + request.url() + " - " + e.getMessage(), e);
		}

		return res;

	}
	
	
	public static boolean isEmpty(String data) {
		if (data == null)
			return true;

		if (data.trim().length() == 0)
			return true;

		return false;
	}
	public static String handleEncryptedRequest(String requestBody, String encryptedAesKeyBase64,
			PrivateKey bankPrivateKey, PublicKey bcpPublicKey) throws Exception {

		String decryptedAesKey = AESCanaraUtils.decryptRSA(encryptedAesKeyBase64, bankPrivateKey);



		JsonObject requestJson = new Gson().fromJson(requestBody, JsonObject.class);
		String base64EncryptedRequest = requestJson.get("REQUEST").getAsString();
		String base64Signature = requestJson.get("DIGI_SIGN").getAsString();

		// Decrypt REQUEST using the AES Key
		String decryptedRequest = AESCanaraUtils.decryptAESGCM(base64EncryptedRequest, decryptedAesKey);

		boolean isVerified = AESCanaraUtils.verify(decryptedRequest, base64Signature, bcpPublicKey);

		if (isVerified) {
			System.out.println("Signature Verified. Processing the request...");

			return decryptedRequest;
		} else {
			throw new SecurityException("Signature verification failed.");
		}
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




	public static void main(String[] args) {
		
		
		
		
		String NESLPublicKey ="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";
		
		try {
			
			
			PublicKey neslPublicKey = AESCanaraUtils.loadPublicKeyFromCertificate(NESLPublicKey);
		
		
		String BANKPublicKey ="-----BEGIN CERTIFICATE----- MIIGtzCCBZ+gAwIBAgIMZAIy7rkCW2c6H6QSMA0GCSqGSIb3DQEBCwUAMFAxCzAJ BgNVBAYTAkJFMRkwFwYDVQQKExBHbG9iYWxTaWduIG52LXNhMSYwJAYDVQQDEx1H bG9iYWxTaWduIFJTQSBPViBTU0wgQ0EgMjAxODAeFw0yNDAxMDIxMTU5MzFaFw0y NTAxMjYwNjExMTBaMHUxCzAJBgNVBAYTAklOMRQwEgYDVQQIEwtNYWhhcmFzaHRy YTEPMA0GA1UEBxMGTXVtYmFpMRwwGgYDVQQKExNVbmlvbiBCYW5rIG9mIEluZGlh MSEwHwYDVQQDDBgqLnVuaW9uYmFua29maW5kaWEuY28uaW4wggEiMA0GCSqGSIb3 DQEBAQUAA4IBDwAwggEKAoIBAQC60iPh5k0pTQ6rNqQLcyCLODkM0y/6o5bbYNbf 48oHjr4fkKsSExquKhoZOBIZhPrC80KgJe02VMLVX9nvDeZe7NMkxSORR+YSeUF/ Lkq+Z2vjMaUpiMTBtDSymppWa5Gs41zz7vZb73G5i651rlVmuweEprXlfRFh7yaH gJSQqApdIRzbl+jstsThDGrzPsygVL4CiRaBmu9jtr5OGsjFdr41o+uN3Xr00xUD IY0SlRz5hiBcCd1oySQIz9bqnTGIjAnK7KzqNderHj3borwB0zTksNbu3TGqN/ce JEopuNbIJEFsBKVycge+DJQ4YZpvKLCFLVivuXZwfZhsipjhAgMBAAGjggNqMIID ZjAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH/BAIwADCBjgYIKwYBBQUHAQEEgYEw fzBEBggrBgEFBQcwAoY4aHR0cDovL3NlY3VyZS5nbG9iYWxzaWduLmNvbS9jYWNl cnQvZ3Nyc2FvdnNzbGNhMjAxOC5jcnQwNwYIKwYBBQUHMAGGK2h0dHA6Ly9vY3Nw Lmdsb2JhbHNpZ24uY29tL2dzcnNhb3Zzc2xjYTIwMTgwVgYDVR0gBE8wTTBBBgkr BgEEAaAyARQwNDAyBggrBgEFBQcCARYmaHR0cHM6Ly93d3cuZ2xvYmFsc2lnbi5j b20vcmVwb3NpdG9yeS8wCAYGZ4EMAQICMD8GA1UdHwQ4MDYwNKAyoDCGLmh0dHA6 Ly9jcmwuZ2xvYmFsc2lnbi5jb20vZ3Nyc2FvdnNzbGNhMjAxOC5jcmwwOwYDVR0R BDQwMoIYKi51bmlvbmJhbmtvZmluZGlhLmNvLmlughZ1bmlvbmJhbmtvZmluZGlh LmNvLmluMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjAfBgNVHSMEGDAW gBT473/yzXhnqN5vjySNiPGHAwKz6zAdBgNVHQ4EFgQUZGe21dLnK1aXVdimRLsz jvP8mpcwggF+BgorBgEEAdZ5AgQCBIIBbgSCAWoBaAB2AObSMWNAd4zBEEEG13G5 zsHSQPaWhIb7uocyHf0eN45QAAABjMoLE+MAAAQDAEcwRQIgLWGK0/90np275SA7 zntLXMvxopttr/EaTsY+MoShDRsCIQDdlv9fHnYU5jpwUGlO6sSD7mbM+/rh3TGR g0bThTTU8wB3AE51oydcmhDDOFts1N8/Uusd8OCOG41pwLH6ZLFimjnfAAABjMoL FrsAAAQDAEgwRgIhAKCHbcR2vFtONjf/Y/LuJZCSiSxha1LLoWQ6UVaILhDqAiEA sAisbopQVYyQS22zr0YaDKCCga9ZeZS7GxkwTalfrUcAdQDgkrP8DB3I52g2H95h uZZNClJ4GYpy1nLEsE2lbW9UBAAAAYzKCxSDAAAEAwBGMEQCIGC99cgFv2lYuB/+ tkufdbzOT8c7r5Es5UyOETkIJfeqAiAvOFuoezVEpxnYdCG9NApeuAzEf/fWA2yA QcLSwAh/ZDANBgkqhkiG9w0BAQsFAAOCAQEAWrMrhvur2hoyVlw/n3lpJMxhJBKc W/rcrldjyOwesph75R5QM+6FC74oSz2X8x/o5AKfy2Bw5yaBOS3CkCM36rY5tBe8 bGWrGu5vozDl7toVOzEOQ2loi2Lf+6VSwg0JwJ7MTub49Z/Czvh/qdWutN5fHlG6 L4Rzk50FL19OFSi1F7kcZLlAVLAsqmGPAdaHrPAp9MViV8ShoRe3f3BI4H856LQm pwb4mb7lXQ2CekpWZE0wGxPehCqLH7h4FrDY8msCZYsqtufGg/UL0u56Ef0aKy6n 2sQrvl1OLA1Fqqbe+TFps0tTYB0y+Q05tLnAakt8Z9c2ebXofkBQrma4vw== -----END CERTIFICATE-----";
		
		PublicKey BankPublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey);
		
		String decryptedRequest ="{\"Request\":{\"body\":{\"encryptData\":{\"uniqueTxnRefNo\":\"31790GO3D2401896ISS008585\",\"auditeePan\":\"AAACE5555A\",\"bankRegisteredMobileNo\":\"9500650190\",\"bankRegisteredEmailId\":\"viswanthkannaiyan@gmail.com\"}}}}";
		String signature = "tviK0rIO5SV18Npxdo7unxR5yxpQ9ZATm1AJdA9f9kcN+Pxw3n3T9gERdvNDjpVP2USewjTXA8baX3KkuiI1MbzBibkCcdyAuyHI5FOnKYYwMwiGntaFZtLD58wIlVJoksQqNOZqjYBHc4aXM4pM7FZ+Mwwl7zUwCHq6SrplS9zir4kmPMVvEW9pvR6PRDUwguvIAVBefAQ0ycQq8e5NjzDwMVaPMgVelTanmnz8AR7FLy9PUyMfNtaqu7oambpgrbpZuaQRTiSAuAm4sXWeru9jP0feEi6ZQ4BV14rLHqFyTAm11/qw3Ar9fshszOOEQYhq8X+ndJFqbXmIi6bgoA==";
		
//		String decryptedRequest ="{\"Request\":{\"body\":{\"encryptData\":{\"uniqueTxnId\":\"01782024NNF72302DY6\",\"FromDate\":\"01-11-2024\",\"ToDate\":\"30-11-2024\",\"reqName\":\"Ajay\",\"reqEntity\":\"V.K. INDUSTRIAL CORPORATION LIMITED\",\"reqId\":\"100201\",\"reqEntityID\":\"106237W/W100829\",\"bcpurpose\":\"For Auditing\",\"reqEmail\":\"vishu@gmail.com\",\"reqMob\":\"9000000001\",\"reqType\":\"AUDITOR\",\"bankId\":\"PNB\",\"responseURL\":\"\",\"APIpurpose\":\"01\",\"auditeeDetails\":{\"auditeeCompany\":\"GOLD PLUS FLOAT GLASS PRIVATE LIMITED\",\"auditeePan\":\"AAAAA4534C\"}}}}}";
//		String signature = "P7M8WjJiUcrG8PCg+Ib/5g1KQSV+a/hW50BTQBbaNVxA4KSZnjbh/RSk2yi0K1/w/qYHipa6NUfAaYai+QCx1UYkuDtH42kHWq3zbESxb16BbpRcWvYASG8NjJcXQ8RnVGfiOxbTst+eu0evk/JkvoLgphDN+bBg0htOzrs0DaWFJ7TQ0eqocI4ILch+5Ipk4cgVK+yuqzPUJCGVvOEG4rOYGh/egZWxw4K17d7goZoPOnnXH2khaDysWhOESz9HCgSBr2O2wLoJnJ9g+BX63FEzxvR67COUZUpkrrArbzkiRc+JUFg8jakb4Y6NTQqtfaayITsl0fRLj0lt+hxDMQ==";
		
		System.out.println("request: "+decryptedRequest);

		System.out.println("signature: "+signature);
		
		boolean isVerified = AESCanaraUtils.verify(decryptedRequest, signature, neslPublicKey);

		if (isVerified) {
			System.out.println("Signature Verified. Processing the request..."+isVerified);
		}else {
			System.out.println("Signature Verified. Processing the request..."+isVerified);
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		
		
	}}

