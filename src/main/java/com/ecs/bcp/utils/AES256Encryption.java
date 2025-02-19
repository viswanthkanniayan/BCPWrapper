package com.ecs.bcp.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Encryption {

    private static final String ALGORITHM = "AES";

    public static void main(String[] args) {
        try {
            String encryptionKey = "cf19b941beb84d8f87df8e1e8ceb3bfa";
            String plainText = "Hello, this is a secret message!";
            
            String enc = "c1c358ec5ff2929eb44929f845ba49d1573c09766176bcb832a4ebe3ad260fb83Id3ATO5TGdL/wHrLkU+1SE3N6WVmOYtiVmZrdz0bzfQf++sMyVKOvTRiDWLp4op0SBddsNKCIK1CQwfjjfbEaHM7c2NWIU3DOdd/ZBVW+Ut4ruUJ4MpLCyEXVpG6I6zuZcQW2fxAzB9pskleTmE+BCtYbsr56tCmuUI+WYeOPmmyhoANSu5C+llwWIzLpQRAWPPJ0u78MxqvazTgZcBdpXIJiFezhVi5R67NrDeLC4BofOsbrwJrBJuhskGqgkCbOMdWMe1lXBQ4e7zXpr8nHvH/vB8MXfg2OLQr1fBFrX3J8LLScD0QiW97DuFzWWJKT1veNNfqbGNT3prjjY5/s3rtm3imBe63KCB+2egVuRJheI9QyIdkdvrS0tnfcHAE9qajwBmGjh88ZCiJjWuQgBlXZBoj48EO8fLkVN2iWpln9qfRAaF/FhIgHZtzvmULsMktakDJTIKnz6ojVG/kdZ2r8/bZ5dWknPFmJHxR3GKnfM8WA92foJAHW1NPRO+vzWCADRq7+JlapCPB9qw5WzoYXczeOm0QA39EHpVRLBrvkjZAAtIUQBvbcFCE5XtoTRjAwivIBSg56hHtrhi7g==";

            // Encrypt the data
            String encryptedText = encrypt(plainText, encryptionKey);
            System.out.println("Encrypted Text: " + encryptedText);

            // Decrypt the data
            String decryptedText = decrypt(enc, encryptionKey);
            System.out.println("Decrypted Text: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to encrypt the data
    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(key), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt the data
    public static String decrypt(String encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(key), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    // Helper method to convert hex string to byte array
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}


