package com.ecs.bcp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class CerToBase64 {

	
	
//	public static void main(String[] args) {
//		
//		String path = "";
////		
////		FileInputStream fis = new FileInputStream(path);
////        byte[] certBytes = fis.readAllBytes();
////
////        // Encode the byte array to Base64
////        String bs = java.util.Base64.getEncoder().encodeToString(certBytes);
//        
//       // String bs = Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of(path)));
//
//        // Wrap with PEM format headers (optional, for readability in some cases)
//        String pemFormatCert = "-----BEGIN CERTIFICATE-----\n"
//                + bs.replaceAll("(.{64})", "$1\n")  // Break into lines of 64 characters
//                + "\n-----END CERTIFICATE-----";
//
//        // Print the Base64-encoded certificate
//        System.out.println("Base64 Encoded Certificate:");
//        System.out.println(pemFormatCert);
//        
//        
//	}
}
