package com.ecs.bcp.bank.api;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class VerifySignature {

	public static void main(String[] args) throws Exception {	

		String signaturePayload = "{\"Request\":{\"body\":{\"encryptData\":{\"uniqueTxnId\":\"08546ZTY128768ABC070220250004\",\"FromDate\":\"01-07-2024\",\"ToDate\":\"30-09-2024\",\"reqName\":\"PNB USER\",\"reqId\":\"CWAPA0340N\",\"reqEntityID\":\"AAAAA4534C\",\"reqEntity\":\"PNB Auditee Test\",\"bcpurpose\":\"Not for auditing /others\",\"reqEmail\":\"sanjithsharma29@gmail.com\",\"reqMob\":\"\",\"reqType\":\"AUDITEE\",\"bankId\":\"PNB\",\"responseURL\":\"\",\"APIpurpose\":\"01\",\"auditeeDetails\":{\"auditeeCompany\":\"PNB Auditee Test\",\"auditeePan\":\"AAAAA4534C\"}}}}}";
		String signature = "4MFGC0eXKwsSkOXky661UGo7LNJxGbg6/u3vz847pY+o5dJ6o1VeOEsLd90laGhMOwd/GGg02Bs0h0yq+9jCVibY+RHi4I1U5i662bO7c5+SaW49JHQ5U6DpKZyjmTg/9wpPn6IiEA4U/beli6kHO0jfykuUcaYhF+vZCOGtpWEMnukxu+DisNfj//oBSURZhGYw+wtBcn8WNvR2AoXAOQYSRqX4OvYm02tdf9P5ZNp0AOtkj/YKslb0Ce9XoVdjq+W2VG92U4OIxJzxID65yr4buMlYDLgktrLsNcVthL2Lridwb4Q2u7jBWPedYNYpRx8m85MHHWny5mzy44g7nA==";
		String NESLPublicKey ="-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----";
		
		PublicKey neslPublicKey = loadPublicKey(NESLPublicKey);


		boolean isVerified = verify(signaturePayload, signature, neslPublicKey);

		if (isVerified) {
			System.out.println("Signature Verified. Processing the request..."+isVerified);
		}else {
			System.out.println("Signature Verified. Processing the request..."+isVerified);
		}
	}

	// Verify Digital Signature
	public static boolean verify(String data, String signatureBase64, PublicKey publicKey) throws Exception {
		Signature verifier = Signature.getInstance("SHA256withRSA");
		verifier.initVerify(publicKey);
		verifier.update(data.getBytes());
		byte[] signature = java.util.Base64.getDecoder().decode(signatureBase64);

		//	System.out.println("VERIFY Signature ::------>> :: "+Base64.getEncoder().encodeToString(signature));
		return verifier.verify(signature);
	}

	
	public static PublicKey loadPublicKey(String input) throws Exception {
	   
	    if (input.contains("-----BEGIN CERTIFICATE-----")) {
	       
	        String base64Certificate = input
	                .replace("-----BEGIN CERTIFICATE-----", "")
	                .replace("-----END CERTIFICATE-----", "")
	                .replaceAll("\\s+", "").replaceAll("=+$", "");

	        byte[] certBytes = Base64.getDecoder().decode(base64Certificate);

	        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes)) {
	            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
	            return certificate.getPublicKey();
	        }
	    } else {
	      
	        byte[] decodedKey = Base64.getDecoder().decode(input);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        return keyFactory.generatePublic(keySpec);
	    }
	}



}
