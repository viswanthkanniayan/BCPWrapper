package com.ecs.bcp.utils;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ECSAESEncDecUtilNew
{

  private static final String SECRET_KEY = "cf19b941beb84d8f87df8e1e8ceb3bfa";
  private static final String SALT = "salt123RBL";

  public static String encrypt(String strToEncrypt) {
    try {
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      System.out.println("Error while encrypting: " + e.toString());
    }
    return null;
  }

  public static String decrypt(String strToDecrypt) throws Exception{
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
  }

  public static void main(String[] args) {
    String originalString = "123456";

    String encryptedString = ECSAESEncDecUtilNew.encrypt(originalString);
    
    // AvzPmepptvrrwUyqFPWZ1A==     123456
    String decryptedString = null;
    try {
      decryptedString = ECSAESEncDecUtilNew.decrypt("b2626f52fdb1403f4918c7a976aba1d5e5886855a1a07d772f9f1ff63e4490e76evKCjb/A94bCGVH21gPJmuQCgn1F0F6z8so7VbEw0e6f6GwI2I1TfO0LBNAxYsvhpxnq3hyo0W2G1lhRfRyZiz5aWbF9w+nRJTnxyP3Ufg2EFp8hYnCUFwA+fVX28OY2+Pr7E8pwLB1vYHmTvdL+/geZ0oRPWtn32tYQlLkU/oFz5ldHjr/A3uOwfBsoBr9N4NbnyJmSQPcmxszBxTVrUskqK/2h4iSBlmbv/SCvBc=");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(originalString);
    System.out.println(encryptedString);
    System.out.println(decryptedString);
  }
}