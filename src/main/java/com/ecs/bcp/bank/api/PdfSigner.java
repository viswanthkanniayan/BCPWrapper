package com.ecs.bcp.bank.api;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.Certificate;

public class PdfSigner {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Signs a PDF using the connected USB token.
     *
     * @param inputPdfPath Path of the input PDF to be signed.
     * @param signedPdfPath Path where the signed PDF will be saved.
     * @param usbTokenPin PIN for accessing the USB token.
     */
    public static void signPdfWithUsbToken(String inputPdfPath, String signedPdfPath, String usbTokenPin) throws Exception {
        // Configure the PKCS#11 provider with the ePass token library path.
        String pkcs11Config = "name=ePass\nlibrary=C:\\Windows\\System32\\eps2003csp11.dll"; // Update this path based on your OS
        Provider pkcs11Provider = Security.getProvider("SunPKCS11");

        // Set configuration for the provider
        if (pkcs11Provider == null) {
            pkcs11Provider = Security.getProvider("SunPKCS11");
            
            
            
            
            
            
            
        } else {
           // pkcs11Provider = pkcs11Provider.configure(pkcs11Configsww);
        }
        Security.addProvider(pkcs11Provider);

        // Load the token key store with the PIN
        KeyStore keyStore = KeyStore.getInstance("PKCS11", pkcs11Provider);
        keyStore.load(null, usbTokenPin.toCharArray());

        // Retrieve the private key and certificate chain
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, usbTokenPin.toCharArray());
        Certificate[] chain = keyStore.getCertificateChain(alias);

        // Read the input PDF
        PdfReader reader = new PdfReader(inputPdfPath);
        FileOutputStream os = new FileOutputStream(signedPdfPath);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');

        // Create the signature appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason("Document signed by USB Token");
        appearance.setLocation("Location");
        appearance.setVisibleSignature("Signature1");

        // Sign the PDF with the USB token's private key
        ExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, pkcs11Provider.getName());
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

        // Close resources
        stamper.close();
        reader.close();
        os.close();
    }

    public static boolean isTokenConnected(String usbTokenPin) {
        try {
            // PKCS#11 Configuration for the ePass token library
            String pkcs11Config = "--name=ePass\nlibrary=D://ePass//eps2003csp11.dll"; // Update path as needed
            Provider pkcs11Provider = Security.getProvider("SunPKCS11");

            // Set configuration for the provider
            if (pkcs11Provider == null) {
                pkcs11Provider = Security.getProvider("SunPKCS11");
                
                
                
                
                
                
                
                
            } else {
               // pkcs11Provider = pkcs11Provider.configure(pkcs11Config);
            }
            Security.addProvider(pkcs11Provider);

            // Attempt to load the token keystore with the PIN
            KeyStore keyStore = KeyStore.getInstance("PKCS11", pkcs11Provider);
            keyStore.load(null, usbTokenPin.toCharArray());

            // If no exception is thrown, the token is connected
            return true;
        } catch (Exception e) {
            // If an exception occurs, the token is likely not connected
            System.err.println("Token not connected or accessible: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
    	
    	String usbTokenPin = "1234"; // Replace with the actual PIN for your USB token
        if (isTokenConnected(usbTokenPin)) {
            System.out.println("USB token is connected.");
            String inputPdfPath = "/path/to/input.pdf";
            String signedPdfPath = "/path/to/signed.pdf";

            try {
                signPdfWithUsbToken(inputPdfPath, signedPdfPath, usbTokenPin);
                System.out.println("PDF has been signed successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("USB token is not connected.");
        }
    	
       
    }
}
