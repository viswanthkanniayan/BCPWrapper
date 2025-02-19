package com.ecs.bcp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Base64;

import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;

public class PfxToBase64 {

    /**
     * Converts a .pfx file to a Base64-encoded private key.
     *
     * @param pfxFilePath the path to the .pfx file
     * @param password    the password for the .pfx file
     * @return the Base64-encoded private key as a String
     * @throws Exception if an error occurs during processing
     */
    public static String convertPfxToBase64(String pfxFilePath, String password) throws Exception {
        // Load the .pfx file into a KeyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxFilePath)) {
            keyStore.load(fis, password.toCharArray());
        }

        // Get the alias of the first key entry
        String alias = keyStore.aliases().nextElement();

        // Extract the private key
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        if (privateKey == null) {
            throw new Exception("No private key found in the .pfx file.");
        }

        // Encode the private key in Base64
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static void main(String[] args) {
        try {
            String pfxFilePath = "d://EastConsultancyServices.pfx";
            String password = "111111";
            String base64PrivateKey = convertPfxToBase64(pfxFilePath, password);
            System.out.println("Base64-encoded private key:");
            System.out.println(base64PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getCer(EntityRequest req) throws Exception
    {
    	String path = System.getProperty("jboss.home.dir") + File.separator + "BCP" + File.separator +"PRV"+File.separator+"DK_Class3.pfx";
    	System.out.println("path : "+path);
    	String base64PrivateKey = convertPfxToBase64(path, req.getPassword());
    	System.out.println("Base64-encoded private key:");
    	System.out.println(base64PrivateKey);

    	return base64PrivateKey;

    }
}

