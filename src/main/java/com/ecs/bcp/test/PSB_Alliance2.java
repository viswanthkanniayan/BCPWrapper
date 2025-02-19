package com.ecs.bcp.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class PSB_Alliance2 {

	public static String certPath = "/home/aceuser/generic/nesl_public_cer.cer";

	public static final int GCM_TAG_LENGTH = 16;
	public static final String TRANSFORMATION = "AES/GCM/NoPadding";
	public static final String ALGO_AES = "AES";
	public static final String ALGO_RSA = "RSA";
	public static final String ENCODING = "UTF-8";
	public static final String HEADER_ENC_ALGO = "RSA/ECB/OAEPPADDING";
	public static final String SIGNATURE = "SHA256withRSA";
	public static final String SEPARATOR = ":";


//AES KEY ENCRYPT
	public static String SymmetricKeyEncryption(String DatatoEncrypt) {
		try {

			byte[] input = Base64.getDecoder().decode(DatatoEncrypt);
			PublicKey pubkey = loadPublicKey();
			Cipher ci = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//			Cipher ci = Cipher.getInstance("RSA/ECB/OAEPPadding");
			ci.init(Cipher.ENCRYPT_MODE, pubkey);
			String encMessage = Base64.getEncoder().encodeToString(ci.doFinal(input));

			return encMessage;

		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}

	}
	

//AES KEY DECRYPT
	public static String DecryptionKey (String DatatoDecrypt) {
		String strDecodedString = "";
		String NESLPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDzkCSV/NhygxK8LvalEZV/JrWmcPkq49hMJGQwwPlko9Y/BuYq+oXZnpFClhxL6uk74Da4nSoph4DTS4s+kcEtvPBjmBhixibWMqA4v5mb/ppKIOiBBFlDrd+2nRV0vrIMNH/d0xLb/Ed9SB/ZMmy9Aq+MHMVYLedR5Tp1ela+9n+DXnzsJNcPcI2RFzzeokvZE1qyxIIeW1FErlDYhNRK8tDhCzklNvbKaH5Esn3jOBRsg05jSyrymWu8RP9jged8Xgle11UyrPslCNgD0Kna7DbkM5lcOYbr9poqpzyG0xhfE9kVXqbirqkM3691yQMKz6ZhNwccnBMB4Kawrpi5AgMBAAECggEBAMOtTndhx9NYErk1UdqUIfMhelbZ9JTMsH2bzxKMNtDv/7RrKofk9Zw8lJ9WUi178R21qMDxI2px/sHCs3/1QaopXUfmbdENOS5lRevRout8xSBBCAp/H+gLzOu9j/tXKVw84W2pokEykYzJYSzyIgJIIhkDFFkfp/RXoXu4afDIL7QmajFMiSIJR6tzNOWS3CyLB7bIG1caCdPToaQkdwFDCicNcA9rZi4tPi1TlddYxzKMLkUJ6Bf1lGTT/OZmyxkg6ixLmvBjvYlfZNvOI1OmT9Gt909U3VvRuido/b2wW38MNU1iQ+DI+zGXetuzR1vwMcINDfNiZnUp+Yuhc/ECgYEA+7dwS6YZSjy1b1Nd9vT+O+e7ZE+GIzBWcHBVXYrWj5Rt2R0HHZ5jypEm2/eTYx3BTGjGnqMrRbT/1vwX/sUxCoY78Mvu87Et3ML+cpyOJ6DPqEPOv7GwNjXVNGpymCM5DHjbXmYBqth+MpRY39lERl5rP/Ptsx+1AfS94iFccjcCgYEA97UvVVoVTSfnRvto8wUBiuEbpsu4uuy/ijxjvLZ1cbVSt0Oyd5xlVuy/vG1vrv5Cm7HhrGs7MhKIbQF7WhHXV4ankvn+zwUmSclYpVWWBkryKX6fbJfJh90q/tL1RgMblj0yDz53kH6ViVe+Mq+RTibSh0irt8GfPAjV6e6VlI8CgYEAjvmV3lI9IS60e8RlpgVodsxcY1DMRyaIopsb0eMvliRf9KeJSCiUVBX4dY79d5oKFoWY87iItrJlc06DrqGBBpJSb6bWVjL4cuGLN7x2/klYSiIhyD58cX5IWNpxtrqjB9OJ/ud/4PPRUpdyl8tH/ZRZ5Nx/0nObE75ZMJ10bicCgYA0iY08gRq7fpcjCve7c3hcSFphChxoKQaG/z/4KorGTzr3+7fCfr1Prm8MO/nQF8Vw2E2RED1B5YRh+kp7VAVkXv7zwWo15lW0mKvghUKImyS5gE237omj81jHK18yNj6HovsXGJyrXO3Cb4W7olkjRkCoyNUC6GIpjYYxU5UOKQKBgGXDf9BSp3A0/oXCfLutftBH0/BgmuiGKVyGQ/0xmnaT9wA7wKKqg/yRbA5lfNbXR7KWCuLjZz6RJmcQTXypCEyocRSIupP9pY/aTF3MCMjbQH8919EycMHmkN9GSjljb5mngMHxETeSZP5G3pHGO0Ki5zX6pWTm9OoneN5jrrjl";
		
		try {
			byte[] input = Base64.getDecoder().decode(DatatoDecrypt);
			Cipher ci = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			//Cipher ci = Cipher.getInstance("RSA/ECB/OAEPPadding");
			PrivateKey privateKey = loadPrivateKeyFromString(NESLPrivateKey);
			ci.init(Cipher.DECRYPT_MODE, privateKey); 
			/*
			 * System.out.println("algorithm:-  "+ ci.getAlgorithm());
			 * System.out.println("algorithm:-  "+ ci.getBlockSize());
			 * System.out.println("algorithm:-  "+ ci.getParameters());
			 * System.out.println("algorithm:-  "+ ci.getProvider());
			 */
			
			byte decMsg[] = ci.doFinal(input);
			strDecodedString = Base64.getEncoder().encodeToString(decMsg);
		} catch (Exception e) {

			e.printStackTrace();
			return "Error";
		}
		 return strDecodedString;
		//return alias;
	}

	
	///////
	
	public static PublicKey loadPublicKey() throws Exception {
	    // Check if the input is in PEM format (contains BEGIN CERTIFICATE)
		String NESLPublicKey ="-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----";
		
	    if (NESLPublicKey.contains("-----BEGIN CERTIFICATE-----")) {
	        // Handle PEM certificate input
	        String base64Certificate = NESLPublicKey
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
	        byte[] decodedKey = Base64.getDecoder().decode(NESLPublicKey);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
	        return keyFactory.generatePublic(keySpec);
	    }
	}

	
	
	public static PrivateKey loadPrivateKeyFromString(String base64PrivateKey) throws Exception {
		byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
		return keyFactory.generatePrivate(keySpec);
	}
	
	

	public static void main(String[] args) {

		String RSA_SIGN = SymmetricKeyEncryption("12345678123456781234567812345678");
		System.out.println("RSA_SIGN : " + RSA_SIGN);
		
		//PrivateKey privkey = getPrivateKey();

		String RSA_SIGN_DEC = DecryptionKey(RSA_SIGN);
		System.out.println("RSA_SIGN_DEC : " + RSA_SIGN_DEC);
	}

}

