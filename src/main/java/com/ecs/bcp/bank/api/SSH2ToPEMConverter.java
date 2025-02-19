package com.ecs.bcp.bank.api;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.io.*;

public class SSH2ToPEMConverter {

	  public static void main(String[] args) throws Exception {
	        String ssh2PublicKeyPath = "E:\\New folder\\HDFC_SSH2.pub"; // Path to your SSH2 public key file
	        String pemOutputPath = "E:\\New folder\\HDFC_SSH2.pem";   // Output PEM file

	        convertSSH2ToPEM(ssh2PublicKeyPath, pemOutputPath);
	        System.out.println("PEM key saved to: " + pemOutputPath);
	    }

	    public static void convertSSH2ToPEM(String ssh2KeyPath, String pemOutputPath) throws Exception {
	        // Read SSH2 public key file
	        String ssh2KeyContent = new String(Files.readAllBytes(Paths.get(ssh2KeyPath)));

	        // Extract Base64 key (remove header, footer, and comments)
	        String base64Key = ssh2KeyContent.replaceAll("(?s)----.*?----|Comment:.*|\\s+", "");

	        // Decode the base64 key
	        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

	        // Write to PEM format
	        String pemFormattedKey = "-----BEGIN PUBLIC KEY-----\n" +
	                chunkedBase64Encoding(Base64.getEncoder().encodeToString(keyBytes)) +
	                "-----END PUBLIC KEY-----\n";

	        // Save to file
	        Files.write(Paths.get(pemOutputPath), pemFormattedKey.getBytes());
	    }

	    private static String chunkedBase64Encoding(String base64) {
	        StringBuilder sb = new StringBuilder();
	        int index = 0;
	        while (index < base64.length()) {
	            sb.append(base64, index, Math.min(index + 64, base64.length())).append("\n");
	            index += 64;
	        }
	        return sb.toString();
	    }
}
