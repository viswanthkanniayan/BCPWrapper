package com.ecs.bcp.utils;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class Data_Encrypt_payload_sample {

	public static void main(String[] args) throws Exception {
		
	
	String request_json_body="{\"uniqueTxnId\": \"31790GO3D2401896ISS0085\", " + 
			"\"reqName\":\"AB55285\", " + 
			"\" reqID \":\"s000000000001\", " + 
			"\"reqEntity\":\"ABCD\", " + 
			"\" reqEntityID \":\"BGOPB5803P\", " + 
			"\"reqEmail\":\"shubo1986.banerjee@gmail.com\","+ 
			"\"reqMob\":\"8240190159\", " + 
			"\"purpose\":\"BAG\", " + 
			"\"reqType\":\"TT\", " + 
			"\"FromDate\": \"01-jan-2024\", " + 
			"\"ToDate\": \"17-dec-2024\",  " + 
			"\"balanceCertificatePDF\":\"JVBERi0xLjQKJeLjz9MKMSAwIG9iago8PC9Db2xvclNwYWNlL0RldmljZVJHQi9TdWJ0eXBlL0ltYWdlL0hlaWdodCA1MC9GaWx0ZXIvRENURGVjb2RlL1R5cGUvWE9iamVjdC9XaWR0aCA1MC9CaXRzUGVyQ29tcG9uZW50IDgvTGVuZ3RoIDQ1ODU+PnN0cmVhbQr/2P/gABBKRklGAAECAQBIAEgAAP/tCzpQaG90b3Nob3AgMy4wADhCSU0D7QpSZXNvbHV0aW9uAAAAABAASAAAAAEAAQBIAAAAAQABOEJJTQQNGEZYIEdsb2JhbCBMaWdodGluZyBBbmdsZQAAAAAEAAAAeDhCSU0EGRJGWCBHbG9iYWwgQWx0aXR1ZGUAAAAABAAAAB44QklNA/MLUHJpbnQgRmxhZ3MAAAAJAAAAAAAAAAABADhCSU0ECg5Db3B5cmlnaHQgRmxhZwAAAAABAAA4QklNJxAUSmFwYW5lc2UgUHJpbnQgRmxhZ3MAAAAACgABAAAAAAAAAAI4QklNA/UXQ29sb3IgSGFsZnRvbmUgU2V0dGluZ3MAAABIAC9mZgABAGxmZgAGAAAAAAABAC9mZgABAKGZmgAGAAAAAAABADIAAAABAFoAAAAGAAAAAAABADUAAAABAC0AAAAGAAAAAAABOEJJTQP4F0NvbG9yIFRyYW5zZmVyIFNldHRpbmdzAAAAcAAA/////////////////////////////wPoAAAAAP////////////////////////////8D6AAAAAD/////////////////////////////A+gAAAAA/////////////////////////////wPoAAA4QklNBAALTGF5ZXIgU3RhdGUAAAACAAA4QklNBAIMTGF5ZXIgR3JvdXBzAAAAAAIAADhCSU0ECAZHdWlkZXMAAAAAEAAAAAEAAAJAAAACQAAAAAA4QklNBB4NVVJMIG92ZXJyaWRlcwAAAAQAAAAAOEJJTQQaBlNsaWNlcwAAAAB5AAAABgAAAAAAAAAAAAAAMgAAADIAAAAMAGwAbwBnAG8AXwAyADUAMABfADIANQAwAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAyAAAAMgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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  \", \r\n" + 
			"\"bank\": \"UCO\",  " + 
			"\"auditeeCompany\":\"MS Vincent\", " + 
			"\"auditeAddr\":\"MUMBAI\", " + 
			"\"aduiteePan\":\"BGOPB5803P\", " + 
			"\"auditeeLegalId\":\"S00001\",  " + 
			"\"auditeeCkycId\":\"C00000002\", " + 
			"\"TotalCountRec\":\"105\" " + 
			"\"count_of_active_acs\":\"10\", " + 
			"\"total_amount_INR_of_active_acs\":\"100.58\", " + 
			"\"counts_of_non_active_acs\":\"100\", " + 
			"\"total_amount_INR_of_non_active_acs\":\"500.68\", " + 
			"\"count_of_funded_active_facilities\":\"1000.58\",  " + 
			"\"total_amount_INR_funded_active_facilities\":\"1000.58\", " + 
			"\"count_of_non_funded_active_facilities\":\"100\", \r\n" + 
			"\"total_amount_INR_non_funded_active_facilities\":\"500.58\", " + 
			"\"count_of_funded_non_active_facilities\":\"100\", " + 
			"\"total_amount_INR_funded_non_active_facilities\":\"500.58\", " + 
			"\"count_of_non_funded_non_active_facilities\":\"100\", " + 
			"\"total_amount_INR_non_funded_non_active_facilities\":\"100\", " + 
			"\"counts_of_security_details\":\"10\", " + 
			"\"Count_of_default_irregular_acs\":\"10\", " + 
			"\"total_outstanding_INR_default_irregular_acs\":\"5000.68\", " + 
			"\"total_principal_INR_default_irregular_acs\":\"20\",  " + 
			"\"total_Interest_INR_default_irregular_accounts\":\"0\", " + 
			"\"Count_of_MTM_Derivatives_Acs\":\"5\", " + 
			"\"Total_ContraValueINR_MTM_Derivatives\": \"5\",  " + 
			" \"is_wilful_defaulter\":\"5\" " + 
			" }" ;
	
	System.out.println("Request Json Body: " +request_json_body);
	
	//Declaration of Symmetric Key
	String Key="9Lbd8WXNi5G/yadYPIvf9upfMcdwkuseTPFfQeGdWWo=";
	System.out.println("Key" +Key);
	
	//Request Body encrypted with Symmteric Key(AES)
	
	String url="https://balcon.nesl.co.in/BcpWapperService/API2/BCWebhook";
	
	String encryptedBody=encryptAESGCM(request_json_body,Key);
	System.out.println("Request json Body Encrypted With Public Key:" +encryptedBody);
	
	/*  sample request payload
	{
	    "Request": {
	        "body": {
	            "encryptData": "encryptedBody"
	        }
	    }
	} 
	 */
	
	
	//String Key encrypted with Public Certificate of Vendor(RSA)
	
	String encryptionPath = "C:/Certin/nesl_public_1.cer"; 
	
   		
//	String encryptedAccessToken=encryptRSA(Key,loadPublicKeyFromCertificate(encryptionPath));
	
	
	// signature
	
	
	String signaturePayload="{ \"Request\": { " + 
			" \"body\": { " + 
			" \"encryptData\": " + 
			" \"uniqueTxnId\": \"31790GO3D2401896ISS0085\", " + 
			"\"reqName\":\"AB55285\", " + 
			"\" reqID \":\"s000000000001\", " + 
			"\"reqEntity\":\"ABCD\", " + 
			"\" reqEntityID \":\"BGOPB5803P\", " + 
			"\"reqEmail\":\"shubo1986.banerjee@gmail.com\","+ 
			"\"reqMob\":\"8240190159\", " + 
			"\"purpose\":\"BAG\", " + 
			"\"reqType\":\"TT\", " + 
			"\"FromDate\": \"01-jan-2024\", " + 
			"\"ToDate\": \"17-dec-2024\",  " + 
			"\"balanceCertificatePDF\":\"JVBERi0xLjQKJeLjz9MKMSAwIG9iago8PC9Db2xvclNwYWNlL0RldmljZVJHQi9TdWJ0eXBlL0ltYWdlL0hlaWdodCA1MC9GaWx0ZXIvRENURGVjb2RlL1R5cGUvWE9iamVjdC9XaWR0aCA1MC9CaXRzUGVyQ29tcG9uZW50IDgvTGVuZ3RoIDQ1ODU+PnN0cmVhbQr/2P/gABBKRklGAAECAQBIAEgAAP/tCzpQaG90b3Nob3AgMy4wADhCSU0D7QpSZXNvbHV0aW9uAAAAABAASAAAAAEAAQBIAAAAAQABOEJJTQQNGEZYIEdsb2JhbCBMaWdodGluZyBBbmdsZQAAAAAEAAAAeDhCSU0EGRJGWCBHbG9iYWwgQWx0aXR1ZGUAAAAABAAAAB44QklNA/MLUHJpbnQgRmxhZ3MAAAAJAAAAAAAAAAABADhCSU0ECg5Db3B5cmlnaHQgRmxhZwAAAAABAAA4QklNJxAUSmFwYW5lc2UgUHJpbnQgRmxhZ3MAAAAACgABAAAAAAAAAAI4QklNA/UXQ29sb3IgSGFsZnRvbmUgU2V0dGluZ3MAAABIAC9mZgABAGxmZgAGAAAAAAABAC9mZgABAKGZmgAGAAAAAAABADIAAAABAFoAAAAGAAAAAAABADUAAAABAC0AAAAGAAAAAAABOEJJTQP4F0NvbG9yIFRyYW5zZmVyIFNldHRpbmdzAAAAcAAA/////////////////////////////wPoAAAAAP////////////////////////////8D6AAAAAD/////////////////////////////A+gAAAAA/////////////////////////////wPoAAA4QklNBAALTGF5ZXIgU3RhdGUAAAACAAA4QklNBAIMTGF5ZXIgR3JvdXBzAAAAAAIAADhCSU0ECAZHdWlkZXMAAAAAEAAAAAEAAAJAAAACQAAAAAA4QklNBB4NVVJMIG92ZXJyaWRlcwAAAAQAAAAAOEJJTQQaBlNsaWNlcwAAAAB5AAAABgAAAAAAAAAAAAAAMgAAADIAAAAMAGwAbwBnAG8AXwAyADUAMABfADIANQAwAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAyAAAAMgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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  \", \r\n" + 
			"\"bank\": \"UCO\",  " + 
			"\"auditeeCompany\":\"MS Vincent\", " + 
			"\"auditeAddr\":\"MUMBAI\", " + 
			"\"aduiteePan\":\"BGOPB5803P\", " + 
			"\"auditeeLegalId\":\"S00001\",  " + 
			"\"auditeeCkycId\":\"C00000002\", " + 
			"\"TotalCountRec\":\"105\" " + 
			"\"count_of_active_acs\":\"10\", " + 
			"\"total_amount_INR_of_active_acs\":\"100.58\", " + 
			"\"counts_of_non_active_acs\":\"100\", " + 
			"\"total_amount_INR_of_non_active_acs\":\"500.68\", " + 
			"\"count_of_funded_active_facilities\":\"1000.58\",  " + 
			"\"total_amount_INR_funded_active_facilities\":\"1000.58\", " + 
			"\"count_of_non_funded_active_facilities\":\"100\", \r\n" + 
			"\"total_amount_INR_non_funded_active_facilities\":\"500.58\", " + 
			"\"count_of_funded_non_active_facilities\":\"100\", " + 
			"\"total_amount_INR_funded_non_active_facilities\":\"500.58\", " + 
			"\"count_of_non_funded_non_active_facilities\":\"100\", " + 
			"\"total_amount_INR_non_funded_non_active_facilities\":\"100\", " + 
			"\"counts_of_security_details\":\"10\", " + 
			"\"Count_of_default_irregular_acs\":\"10\", " + 
			"\"total_outstanding_INR_default_irregular_acs\":\"5000.68\", " + 
			"\"total_principal_INR_default_irregular_acs\":\"20\",  " + 
			"\"total_Interest_INR_default_irregular_accounts\":\"0\", " + 
			"\"Count_of_MTM_Derivatives_Acs\":\"5\", " + 
			"\"Total_ContraValueINR_MTM_Derivatives\": \"5\",  " + 
			" \"is_wilful_defaulter\":\"5\" " + 
			" }  " + 
			" }  " + 
			" } ";
	
	System.out.println("signaturePayload: " +signaturePayload);
	
	
	PrivateKey BankPrivateKey = null;    //   UCO bank private key here
	
	String signature = sign(signaturePayload, BankPrivateKey);  // sign the full request with bank private key
	
	
	String JWS = signature; // in output, you will get a encrypted data - set this value in header JWS param
	
	
	
	
	}
	
	
	
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
	
	// Load Private Key
	public static PublicKey loadPublicKey(String input) throws Exception {
	    // Check if the input is in PEM format (contains BEGIN CERTIFICATE)
	    if (input.contains("-----BEGIN CERTIFICATE-----")) {
	        // Handle PEM certificate input
	        String base64Certificate = input
	                .replace("-----BEGIN CERTIFICATE-----", "")
	                .replace("-----END CERTIFICATE-----", "")
	                .replaceAll("\\s+", "");

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

	
	
	
}
