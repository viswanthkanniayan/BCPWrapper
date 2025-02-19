package com.ecs.bcp.bank.api;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.xsd.EntityResponce;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class AESUtils {

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
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
       // System.out.println("Decoded AES Key : " + Base64.getEncoder().encodeToString(key));

		byte[] iv = new byte[12];
		byte[] encryptedBytes = new byte[ivAndEncrypted.length - 12];
		System.arraycopy(ivAndEncrypted, 0, iv, 0, 12);
		System.arraycopy(ivAndEncrypted, 12, encryptedBytes, 0, encryptedBytes.length);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	
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
                .replaceAll("\\s+", "");

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
    
    public static PublicKey loadPublicKeyFromString(String base64PublicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
        return keyFactory.generatePublic(keySpec);
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
	
	public static String sendEncryptedRequest(String encryptedPayload, String signature, String accsessToken) throws Exception {
		 
	        Map<String, String> headers = new HashMap<>();
	        headers.put("Content-Type", "application/json");
	        headers.put("AccessToken", accsessToken);

	        JsonObject jsonRequest = new JsonObject();
	        jsonRequest.addProperty("REQUEST_REFERENCE_NUMBER", "345678900");
	        jsonRequest.addProperty("REQUEST", encryptedPayload);
	        jsonRequest.addProperty("DIGI_SIGN", signature);

	        String requestJsonString = new Gson().toJson(jsonRequest);
	        
	        String bankaesKey = "cf19b941beb84d8f87df8e1e8ceb3bfa";
	        	        
	        System.out.println("headers ------->>>  "+headers);
	        System.out.println("requestJsonString ------->>>  "+requestJsonString);
	        
	        
	   /*     String encryptedPayloadForBank = AESUtils.encryptAESGCM(requestJsonString, bankaesKey);
	        
	        JsonObject jsonRequest1 = new JsonObject();
	        jsonRequest1.addProperty("reqdata",encryptedPayloadForBank);
	        jsonRequest1.addProperty("msgid", "0");
	        
	        
	        System.out.println("jsonRequest1 ----:: -->> "+jsonRequest1);  */

	        HttpResponse<String> response = Unirest.post("https://apimuat.unionbankofindia.co.in/BankServices/handlersb/1/NeSLBCPChannel/NeSLBCPServiceGroup/bcpCountOfAccounts")
	                .headers(headers)
	                .body(requestJsonString)
	                .asString();

	        System.out.println("Response: " + response.getBody().toString());  
	        
	        String res = response.getBody().toString();   
	        
	        
	        
			String TxnId = genRandNo(8);
			String TxnId1 = genRandNo(8);
			String cin = genRandNo(8);
			String count = genRandNo(3);
			String bank = genRandNo(3);
	        
	        EntityResponce jsonRequest1 = new EntityResponce();
	        jsonRequest1.setUniqueTxnId(TxnId.toString());
	        jsonRequest1.setLei(TxnId1.toString());
	        jsonRequest1.setAccountsCount(count.toString());
	        jsonRequest1.setBankCharges(bank.toString());
	        jsonRequest1.setCin(cin);
	        jsonRequest1.setErrorCode("200");
	        
	     //   EntityResponce res = jsonRequest1 ;
	        
	        
	        return res;   
	    }

	public static String handleEncryptedRequest(String requestBody, String encryptedAesKeyBase64,
			PrivateKey bankPrivateKey, PublicKey bcpPublicKey) throws Exception {

		String decryptedAesKey = AESUtils.decryptRSA(encryptedAesKeyBase64, bankPrivateKey);
		
		

		 JsonObject requestJson = new Gson().fromJson(requestBody, JsonObject.class);
		String base64EncryptedRequest = requestJson.get("REQUEST").getAsString();
		String base64Signature = requestJson.get("DIGI_SIGN").getAsString();

		// Decrypt REQUEST using the AES Key
		String decryptedRequest = AESUtils.decryptAESGCM(base64EncryptedRequest, decryptedAesKey);

		boolean isVerified = AESUtils.verify(decryptedRequest, base64Signature, bcpPublicKey);

		if (isVerified) {
			System.out.println("Signature Verified. Processing the request...");
			
			return decryptedRequest;
		} else {
			throw new SecurityException("Signature verification failed.");
		}
	}
	
	
	
	  public static void main(String[] args) {
		  
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSZ");
			String fromDate = sdf.format(date);
			String toDate = sdf.format(date);
			
			
			JsonObject jsonBody = new JsonObject();
			jsonBody.addProperty("uniqueTxnId", "31790GO3D2401896ISS001");
			jsonBody.addProperty("bankId", "1251215");
			jsonBody.addProperty("pan", "AAACA1111A" );
			jsonBody.addProperty("fromDate", fromDate);
			jsonBody.addProperty("ToDate", toDate);
		

			Gson gson = new Gson();
			String jsonPayloadString = gson.toJson(jsonBody);
			
			System.out.println("RequestJson ::------>> :: \n"+jsonPayloadString);
			
			
			try {
				
				// generate the AES key 
				String aesKey = generateAESKey();
				
				System.out.println("Aes genereted key  :: "+ aesKey);
				
				// BCP encryption of payload : 
				
				String encryptedPayload= encryptAESGCM(jsonPayloadString,aesKey);
				
				//BCP signature 
				
				  String pfxPath = "C:\\Users\\91938\\Desktop\\Keygen\\ECS.pfx";
		            PrivateKey bcpPrivateKey = AESUtils.loadPrivateKeyFromPfx(pfxPath, "Welcome@123");
		            
				
				  String signature= sign(jsonPayloadString,bcpPrivateKey);
				
				  
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
					  
					  System.out.println("Public Key ::------>> :: "+Base64.getEncoder().encodeToString(publicKey.getEncoded()));

				 // encrypt the generated Aes key with banks public rsa key 
			      String accsessToken=encryptRSA(aesKey,publicKey);

			      
			      // this has to be added in the header 
			      System.out.println("accsessToken :: "+ accsessToken);
				  
				// BCP request generation :: 
				  JsonObject jsonRequest = new JsonObject();
			        jsonRequest.addProperty("REQUEST_REFERENCE_NUMBER", "31790GO3D2401896ISS001");
			        jsonRequest.addProperty("REQUEST", encryptedPayload);
			        jsonRequest.addProperty("DIGI_SIGN", signature);

			        
			        // this is BCps final request 
			        String requestJsonString = new Gson().toJson(jsonRequest);
			        
			        System.out.println("request body :: "+ requestJsonString);
			        System.out.println("header :: "+ accsessToken);
				
//===================================================================================================				
			        
			        
				// Final Bank encryption detail:: 
		     	String aesKeyFromBank = "cf19b941beb84d8f87df8e1e8ceb3bfa";
				
				System.out.println("aesKey ::------>> :: "+aesKeyFromBank);
				
				String encryptedPayloadForBank = AESUtils.encryptAESGCM(requestJsonString, aesKeyFromBank);
				
				System.out.println("encryptedPayloadForBank :: "+encryptedPayloadForBank);
				
				System.out.println("final request :: encryptedPayload "+encryptedPayload);
				
				
				
				
				
				
				
				
				// this is the final request bank will recive 
		/*		{
				     "reqdata": " encryptedPayload",
				     "msgid": "0"
				}*/
				
				
			//	String  response = AESUtils.sendEncryptedRequest(encryptedPayloadForBank, signature, accsessToken);
				
				//System.out.println("final request :: encryptedPayload "+response);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
				
	  }}

