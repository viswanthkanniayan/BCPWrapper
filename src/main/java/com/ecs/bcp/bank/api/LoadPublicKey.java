package com.ecs.bcp.bank.api;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import com.ecs.bcp.bank.api.canara.AESCanaraUtils;

public class LoadPublicKey {
	
	public static void main(String[] args) throws Exception {
		
	
	//	String pathName = "D:\\AxisFiles\\BCP_FILES\\BCP_PROD\\LOAD_KEY\\NESL.cer";
		
		String pathName = "E:\\HDFC_Public_11Feb2025\\HDFC_Public_11Feb2025\\HDFCPublicKey.cer";
		
	//	PublicKey bankPublicKey = AESCanaraUtils.loadPublicKeyFromCer("D:\\AxisFiles\\BCP_FILES\\BCP_PROD\\LOAD_KEY\\NESL.cer");

		
		 String certBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(pathName)));
        System.out.println("certBase64 value => " + certBase64);

	/*	
		String pfxPath = "D:\\AxisFiles\\BCP_FILES\\BCP_PROD\\LOAD_KEY\\NESL.pfx";
		PrivateKey bcpPrivateKey = AESUtils.loadPrivateKeyFromPfx(pfxPath, "BcpProd@2012");
		
		
		 String certBase6 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(pfxPath)));
	        System.out.println("PrivateKey value => " + certBase6);   */
		
	}

}
