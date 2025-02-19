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

import com.ecs.bcp.bank.api.canara.AESCanaraUtils;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


public class PSB_Alliance {

	public static String certPath = "/home/aceuser/generic/nesl_public_cer.cer";

	public static final int GCM_TAG_LENGTH = 16;
	public static final String TRANSFORMATION = "AES/GCM/NoPadding";
	public static final String ALGO_AES = "AES";
	public static final String ALGO_RSA = "RSA";
	public static final String ENCODING = "UTF-8";
	public static final String HEADER_ENC_ALGO = "RSA/ECB/OAEPPADDING";
	public static final String SIGNATURE = "SHA256withRSA";
	public static final String SEPARATOR = ":";

//AES ENCRYPTION
	public static String AESEncrypt(String Data, String Key) {
		String stEncodedString = null;

		try {
			byte[] key = Base64.getDecoder().decode(Key);
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			byte[] iv = new byte[12];
			new SecureRandom().nextBytes(iv);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
			byte[] encryptedBytes = cipher.doFinal(Data.getBytes());

			byte[] ivAndEncrypted = new byte[iv.length + encryptedBytes.length];
			System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
			System.arraycopy(encryptedBytes, 0, ivAndEncrypted, iv.length, encryptedBytes.length);

			return Base64.getEncoder().encodeToString(ivAndEncrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stEncodedString;
	}

//AES DECRYPTION
	public static String AESDecrypt(String Data, String Key) {
		String data = "";
		byte[] iv = new byte[12];
		try {
			byte[] dataToencrypt = Base64.getDecoder().decode(Data);
			byte[] aesKey = Base64.getDecoder().decode(Key);
			SecretKeySpec secretKey = new SecretKeySpec(aesKey, "AES");

			byte[] encryptedBytes = new byte[dataToencrypt.length - 12];
			System.arraycopy(dataToencrypt, 0, iv, 0, 12);
			System.arraycopy(dataToencrypt, 12, encryptedBytes, 0, encryptedBytes.length);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return new String(decryptedBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

//SHA256WITHRSA SIGN
	public static String signSHA256RSA(String Data) {

		String data = "";

		try {
			byte[] input = Data.getBytes();
			PrivateKey key = getPrivateKey();
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(key);
			signature.update(input);
			byte[] signedData = signature.sign();
			byte[] encodedSign = Base64.getEncoder().encode(signedData);
			return new String(encodedSign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

//SHA256WITHRSA VERIFY
	public static String decSignSHA256RSA(String encryptionRequestBody, String digitalSignature) {
		String Data = "";

		try {
			PublicKey key = getPublickey();
			byte[] input = encryptionRequestBody.getBytes();
			byte[] signatureToVerify = Base64.getDecoder().decode(digitalSignature);
			Signature signature = Signature.getInstance(SIGNATURE);
			signature.initVerify(key);
			signature.update(input);
			boolean isSignValid = signature.verify(signatureToVerify);

			if (isSignValid == true) {
				return "verified";

			} else {
				return "Not verified";

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;

	}

	public static byte[] getIVFromAESKey(byte[] encoded) {
		return Arrays.copyOfRange(encoded, 0, 12);
	}

//AES KEY ENCRYPT
	public static String SymmetricKeyEncryption(String DatatoEncrypt,PublicKey pubKey ) {
		try {

			byte[] input = Base64.getDecoder().decode(DatatoEncrypt);
			Cipher ci = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			ci.init(Cipher.ENCRYPT_MODE, pubKey);
			String encMessage = Base64.getEncoder().encodeToString(ci.doFinal(input));

			return encMessage;

		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}

	}
//AES KEY DECRYPT	
	public static String DecKey(String DataToDecrypt) {
		String Data = "";
		try {
			PrivateKey privkey = getPrivateKey();
			String DecData = DecryptionKey(DataToDecrypt,privkey);
			return DecData;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}

//AES KEY DECRYPT
	public static String DecryptionKey (String DatatoDecrypt,PrivateKey privateKey) {
		String strDecodedString = "";
		try {
			byte[] input = Base64.getDecoder().decode(DatatoDecrypt); 
			Cipher ci = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			ci.init(Cipher.DECRYPT_MODE, privateKey); 
			byte decMsg[] = ci.doFinal(input);
			strDecodedString = Base64.getEncoder().encodeToString(decMsg);
		} catch (Exception e) {

			e.printStackTrace();
			return "Error";
		}
		 return strDecodedString;
		//return alias;
	}

	public static PublicKey getPublickey() {

		try {
			FileInputStream is = new FileInputStream("<Path for public cert>");
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) f.generateCertificate(is);
			PublicKey pk = cert.getPublicKey();
			return pk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey getPrivateKey() {

		String alias = "";
		PrivateKey privateKey = null;
		try {
			// of your private key
			String dscPath = "<Path for private key .jks>";
			String dscPassword = "<password>"; // Password of your private key
			KeyStore keyStore = KeyStore.getInstance("jks");
			keyStore.load(new FileInputStream(dscPath), dscPassword.toCharArray());
			final Enumeration<?> aliasesEnum = keyStore.aliases();
			// PrivateKeyEntry keyEntry = null;
			if (aliasesEnum != null) {
				while (aliasesEnum.hasMoreElements()) {
					String n = (String) aliasesEnum.nextElement();
					if (keyStore.isKeyEntry(n)) {
						alias = n;

					}
				}
			}
			KeyStore.PrivateKeyEntry PrivateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias,
					new KeyStore.PasswordProtection(dscPassword.toCharArray()));
			privateKey = PrivateKeyEntry.getPrivateKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static void main(String[] args) throws Exception {

//		String input = "{\"uniqueTxnId\":\"61552IXD0281784744496\",\"FromDate\":\"01-11-2024\",\"ToDate\":\"30-11-2024\",\"reqName\":\"kaviyan rajendran\",\"reqId\":\"AAICG7897C\",\"reqEntityID\":\"GPHPR7558A\",\"reqEntity\":\"GOLD PLUS FLOAT GLASS PRIVATE LIMITED\",\"bcpurpose\":\"Not for auditing /others\",\"reqEmail\":\"saravinthan28@gmail.com\",\"reqMob\":\"9003412802\",\"reqType\":\"AUDITEE\",\"bankId\":\"CBI\",\"responseURL\":\"\",\"APIpurpose\":\"01\",\"auditeeDetails\":{\"auditeeCompany\":\"GOLD PLUS FLOAT GLASS PRIVATE LIMITED\",\"auditeePan\":\"AAICG7897C\"}}";
//
//		String encData = AESEncrypt(input, "12345678123456781234567812345678");
//		System.out.println("encdata: " + encData);
//
	//	String DecData = AESDecrypt("G1791Pq3liwpzIOhTcRfC8IEjqA9f8bB1nARsBiTElUXqzwK+0e/htE8vT9NcRHDqKwj5ndJ/swvNM0SxFupBJ/xo2lcInHCTq8IrgYqI1x0q8Gtm6PYl6rHPMVG3FA21bMWpQ5n/noTxHXtm595gR4BG1Cz9nsTXYKPUeOxXhO1JnVo2sdPnh2wH42bWrQO9zbLpvXOzWP5g6MpMMww0LUPpZu8EdeNVLUd8UeiBvD3caM/xKtOwVQDM8EGxHjNp6iJ0M9AfNOhU7NfeYTmdLUYc3Q0acIKFty3Gi09Hm6+LcPoAajFFpqgAY0dZxzREZy4iLbTohhha5sQeUaqSBpp9RnYqVuOdUYpMrHgKyfXYQJIFDoA/9COjj4Kwz9VCu3vlHrsArfb8SsuzddCJE//SlIlbsU2jP/FjaT2uJVcJZN6lStG/Funk4wswI63un3vQMqrMz1F2fy1Ii81wSUapN59zYYxF7NlQ0V83QoJwPUGT3LRSs/qi7W0HOeoQI9UZ9a7hc76SojKi10zCgxFnyuRB3z+lLJoZHcRhde7ACOJoZfKwRK833QQ5PEfrXuDfK5VD/1pWNaF6sAxcDX9e2sbdR+dod6/7AcG7Kop0RUs5HHQ+xxOgS//RzQEGhP6CEozNY6aRw1Pw/EJsNonsQ6hPAH0YQ4BpaJLI7oufc/PMlgmczm1V2/1IN0sqPtn9FcrUxLUELDGKiUaHYUQZSVo/GU=", "MyT2ZXCBJ42haStiCTJZHTLvJsGMV/pIdkJsWkvmAL4=");
	//	System.out.println("decdata: " + DecData);
//
//		String SIGN = signSHA256RSA(input);
//		System.out.println("SIGN VALUE : " + SIGN);
//
//		String VERIFY_SIGN = decSignSHA256RSA(input, SIGN);
//		System.out.println("DEC SIGN VALUE : " + VERIFY_SIGN);

		//String RSA_SIGN = SymmetricKeyEncryption("12345678123456781234567812345678");
		//System.out.println("RSA_SIGN : " + RSA_SIGN);
		
//		PrivateKey privkey = getPrivateKey();

//		String RSA_SIGN_DEC = DecryptionKey("YTGW2nNizpGGlVP8T6I3e/3DY376HHkTv1jIBec1Ko6avE6TyAix2FMoxLPCn3shnvsKIPcqOwYE6d4BWl0LeRYbJ/uh6EduYAiofF1K7ID0UueXH1qPyOK5nvNl32v8CPWIRhnZw17ixX4tKW88+p8ZVyKELyG5H6BgwyYoBHmF+aru7dqpzbU+1qx/um2GA0LGuqT/S5nJ42fgtJvMAv9ULtSbOWldrkRJfnOh8yL7W4uPZJI5FMwuTZJsh0RVfp7RRsMXIjBkKthQPhVZvmkZH5sjvW4P0GCwuxB/WXjUr619sY7cArbIMFWDS2aPuagDK5+zTKnDHCpYnDeoHg==",privkey);
	
		
		// encrypt
		
       String NESLPublicKey ="-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----";
		
		PublicKey neslPublicKey = AESCanaraUtils.loadPublicKey(NESLPublicKey);
		
		String aesKey = AESCanaraUtils.generateAESKey();
		
	//	System.out.println("AesKey ------------->> "+aesKey);
		
	//	String accessToken = SymmetricKeyEncryption(aesKey,neslPublicKey);
		
		
			// NESL private key
		String accessToken = "XjHH8tOZwG8Itjq+vTsp6nHQgQEqnIep0q3/GqeUP13+bAISSthQp4S+DPZj+IlbyMDyo7PoSqIJCgcqYLx58/4E7YO/CjAxuF98XsLxhXeXCelNv/PhofvZBd0bLwFZqPT9zP6hMHz14zNurf/9Jt4d+AGHPrJxRwk6pc1FTS6rOpi+6/vfqFJeVc2iULNgVMud8ZGJTTCIq0qU4Si6DlzayqyWJM8PChViqYUxsbjdANBBj+OYvophHvNst+XNL56gODP4jCAZHkMLBL+olMoX7TUtRXROTnBZTS/EHrKAbgrXEg3KBD++LR4tqsOkmyE4xs6/sTT/K87RYBaTjw==";
		String NESLPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDzkCSV/NhygxK8LvalEZV/JrWmcPkq49hMJGQwwPlko9Y/BuYq+oXZnpFClhxL6uk74Da4nSoph4DTS4s+kcEtvPBjmBhixibWMqA4v5mb/ppKIOiBBFlDrd+2nRV0vrIMNH/d0xLb/Ed9SB/ZMmy9Aq+MHMVYLedR5Tp1ela+9n+DXnzsJNcPcI2RFzzeokvZE1qyxIIeW1FErlDYhNRK8tDhCzklNvbKaH5Esn3jOBRsg05jSyrymWu8RP9jged8Xgle11UyrPslCNgD0Kna7DbkM5lcOYbr9poqpzyG0xhfE9kVXqbirqkM3691yQMKz6ZhNwccnBMB4Kawrpi5AgMBAAECggEBAMOtTndhx9NYErk1UdqUIfMhelbZ9JTMsH2bzxKMNtDv/7RrKofk9Zw8lJ9WUi178R21qMDxI2px/sHCs3/1QaopXUfmbdENOS5lRevRout8xSBBCAp/H+gLzOu9j/tXKVw84W2pokEykYzJYSzyIgJIIhkDFFkfp/RXoXu4afDIL7QmajFMiSIJR6tzNOWS3CyLB7bIG1caCdPToaQkdwFDCicNcA9rZi4tPi1TlddYxzKMLkUJ6Bf1lGTT/OZmyxkg6ixLmvBjvYlfZNvOI1OmT9Gt909U3VvRuido/b2wW38MNU1iQ+DI+zGXetuzR1vwMcINDfNiZnUp+Yuhc/ECgYEA+7dwS6YZSjy1b1Nd9vT+O+e7ZE+GIzBWcHBVXYrWj5Rt2R0HHZ5jypEm2/eTYx3BTGjGnqMrRbT/1vwX/sUxCoY78Mvu87Et3ML+cpyOJ6DPqEPOv7GwNjXVNGpymCM5DHjbXmYBqth+MpRY39lERl5rP/Ptsx+1AfS94iFccjcCgYEA97UvVVoVTSfnRvto8wUBiuEbpsu4uuy/ijxjvLZ1cbVSt0Oyd5xlVuy/vG1vrv5Cm7HhrGs7MhKIbQF7WhHXV4ankvn+zwUmSclYpVWWBkryKX6fbJfJh90q/tL1RgMblj0yDz53kH6ViVe+Mq+RTibSh0irt8GfPAjV6e6VlI8CgYEAjvmV3lI9IS60e8RlpgVodsxcY1DMRyaIopsb0eMvliRf9KeJSCiUVBX4dY79d5oKFoWY87iItrJlc06DrqGBBpJSb6bWVjL4cuGLN7x2/klYSiIhyD58cX5IWNpxtrqjB9OJ/ud/4PPRUpdyl8tH/ZRZ5Nx/0nObE75ZMJ10bicCgYA0iY08gRq7fpcjCve7c3hcSFphChxoKQaG/z/4KorGTzr3+7fCfr1Prm8MO/nQF8Vw2E2RED1B5YRh+kp7VAVkXv7zwWo15lW0mKvghUKImyS5gE237omj81jHK18yNj6HovsXGJyrXO3Cb4W7olkjRkCoyNUC6GIpjYYxU5UOKQKBgGXDf9BSp3A0/oXCfLutftBH0/BgmuiGKVyGQ/0xmnaT9wA7wKKqg/yRbA5lfNbXR7KWCuLjZz6RJmcQTXypCEyocRSIupP9pY/aTF3MCMjbQH8919EycMHmkN9GSjljb5mngMHxETeSZP5G3pHGO0Ki5zX6pWTm9OoneN5jrrjl";

			System.out.println("accessToken ------------->> "+accessToken);
		PrivateKey PrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey);

		String RSA_SIGN_DEC = DecryptionKey(accessToken,PrivateKey);
		System.out.println("RSA_SIGN_DEC : " + RSA_SIGN_DEC);
		
		 
	}

}

