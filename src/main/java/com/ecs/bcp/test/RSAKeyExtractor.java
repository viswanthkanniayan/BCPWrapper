package com.ecs.bcp.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyExtractor {
	
public static PublicKey getRSAPublicKeyFromPem(String base64Key) throws Exception {
		
		
        String base64KeyCleaned = base64Key.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                           .replaceAll("-----END PUBLIC KEY-----", "")
                                           .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(base64KeyCleaned);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }
//
//public static void convertPemToCer(String pemFilePath, String cerFilePath) throws Exception {
//    // Read PEM file
//    byte[] pemBytes = Files.readAllBytes(Paths.get(pemFilePath));
//    String pemContent = new String(pemBytes);
//
//    // Extract Base64-encoded key
//    String base64Key = pemContent.replaceAll("-----BEGIN PUBLIC KEY-----", "")
//                                 .replaceAll("-----END PUBLIC KEY-----", "")
//                                 .replaceAll("\\s", "");
//
//    byte[] keyBytes = Base64.getDecoder().decode(base64Key);
//
//    // Convert to RSA PublicKey
//    PublicKey publicKey = getRSAPublicKey(keyBytes);
//
//    // Generate self-signed X.509 certificate from RSA key
//    X509Certificate cert = generateCertificate(publicKey);
//
//    // Write to .cer file
//    try (FileOutputStream fos = new FileOutputStream(cerFilePath)) {
//        fos.write(cert.getEncoded());
//    }
//
//    System.out.println("Conversion successful: " + cerFilePath + " created.");
//}


public static void main(String[] args) throws Exception {
	
	
	 String pemKey = "-----BEGIN PUBLIC KEY----- MIIEIjANBgkqhkiG9w0BAQEFAAOCBA8AMIIECgKCBAEAnY3Fr+hghssdaLK4bWrO YW4MJbRcn04PEsRYnE9R6wsXegKCudvY86tEI5Wo4N25ekFSoleCOORChLDtlhAY YfOaW7ZfQdHoJhRUbgTz7xYdO+IkBoRDKhqVylJGJfX2cJrNkoHWEcswqTtwudeX Nv2EVLHQU5+caWvPpiTJeXA0kNqxIVEyheuMj/xE6xY0fDHLCAOQxv76U1VKdzNo enTPUh7QNumWprDSsg6xQRyjgBB6pqWBs7WuIkz052aA1h/pzo0jY+9dJngv1/K7 HzU9o5V38caM2phQgs5+E750iHfTizWLS456X1/zAM4SPkdtI2lyNHHQLgXbPpil fqqdnPujCtqeWxdTsDOLBtf10o30e9fUGUkS1aZhQuzbI/3hSNgm9x6muhoxGUDx k+iuWSTmp0Qx9xdF0E1lnbYjgMhunUYyeu+rAim8K8VJIVeOCjrdJIzi1b472M16 rB0wnouXwD7ocUjV5LX05+237J5CQ2ECJv8m0Rk3i/ArzJgKY+I/5QiKzeAO/Lkb Sss1mvKostnc1VQQx2BOZsBfctPc53C5LOlsG2TNJ+91ZC6InpzTmZMD2PqcDEiJ XkELSkjj/AVI6LODTaC/pziLNVgcQYKazdAYGqzDeOBh/kcWFPanr5EWXDnbzfPf yn81McUsebMp2yKN0ht2a0Ki1v/Gow+40LTTl6BWGXmUi5F/kSaCxpHyQ2o7yRCf gMUbwFf52AN/OWfn4NnV3KXvqb8XdcXxyh71LVvUwgl5WZ+7YVAfSXYkPijSSx5P GM8fnGNsr7Ih0+Yejja0Y0rvupOkIJhzaURxmysZhgBKbq8GEXHlxfB91BUFMKX2 O8NFzlECXojmA/NjL/fMmVBimIWOb7jZuEIFwcdCHKwWyZ9Utuh9jD8jfB1Js7Nz 2Gx2s0aVB6FJhb9F8gqsqxdTlorPKBKftJ0/34yck04uxNZYSqc3M2X4uft3bo/e T9Cn+zsqKtWpfg2WgF+CduEu2ulqtfDuXrNGQhRmV89FYRhk7aAZmWzxm/D4j9K/ 5a33xPXToyQySUQRZ6ZA+vVWpZZpdXig5mlzA8rpg3P2BRGPcU4NCfOMe9MjZClN OoUk0kfpLCYuKz9SpLth+SDoJNpQzM+FJZ9AkqqnQqdoNExkZ548i0S6YZwrfQqg 9md4KkpxtSQhWWsumV8Zv1/re0piNqLlDbtsy7mo1HMDgTqjijkHoR01J5/1JuHf lj6nC2EWCdQwU2OjTsX/1OTsV5gwe+T/llxFPFIDb9r85mvglAwo/fGJbsR4/+ba ay2cMl0SIdB6QWQO+SeXiw+61isMQUY2R4Zpl5sYIZKXS75mtyp8mnawgyK0Ebex ZQIDAQAB -----END PUBLIC KEY-----";
	
	 PublicKey publicKey = getRSAPublicKeyFromPem(pemKey);
	 System.out.println("Public Key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
//     System.out.println("RSA Public Key: " + publicKey);
}

}
