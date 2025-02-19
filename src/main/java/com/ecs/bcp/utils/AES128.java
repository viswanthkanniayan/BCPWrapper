package com.ecs.bcp.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
/**
 * @author AJAY RAJAMANICKAM
 */
public class AES128 {
	@SuppressWarnings("null")
	public static String encrypt(final String word, final String password){
		try{
			byte[] ivBytes;
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			byte[] saltBytes = bytes;
			//key should changed 
			//PBKDF2withHmacSHA1
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DKEY");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 50, 128);
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] encryptedTextBytes = cipher.doFinal(word.getBytes("UTF-8"));
            //prepend salt and vi
			byte[] buffer = new byte[saltBytes.length + ivBytes.length +
			                         encryptedTextBytes.length];
			System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
			System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
			System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length +
					ivBytes.length, encryptedTextBytes.length);
			new Base64();
			return new Base64().encodeToString(buffer);
//return Base64.encodeBase64String(buffer);
		}catch(BadPaddingException | InvalidParameterSpecException |
				NoSuchAlgorithmException |
				InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
				IllegalBlockSizeException | UnsupportedEncodingException ex2){
			final Exception ex = null;
			final Exception e = ex;
			return "ER001" + e.toString();
		}
	}
	@SuppressWarnings("null")
	public static String decrypt(final String encryptedText, final String password){
		try{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//strip off the salt and iv
			ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(encryptedText));
			byte[] saltBytes = new byte[20];
			buffer.get(saltBytes, 0, saltBytes.length);
			byte[] ivBytes1 = new byte[cipher.getBlockSize()];
			buffer.get(ivBytes1, 0, ivBytes1.length);
			byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length -
			                                     ivBytes1.length];
			buffer.get(encryptedTextBytes);
			//key should changed 
			//PBKDF2WithHmacSHA1
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DKEY");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 50, 128);
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));
			byte[] decryptedTextBytes = null;
			try {
				decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new String(decryptedTextBytes);
		}catch(NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				|
				InvalidKeyException | InvalidAlgorithmParameterException ex2){
			final Exception ex = null;
			final Exception e = ex;
			return "ER001" + e.toString();
		}
	}
	public static void main(final String[] args) throws Exception{
		String encryptedText = "TEXT_TO_ENCRYPT";
		encryptedText = AES128.encrypt(encryptedText, "password");
		System.out.println("Encrypted Text :"+ encryptedText);
		System.out.println("Decrypted Text :"+ AES128.decrypt(encryptedText,"password"));
	}

}