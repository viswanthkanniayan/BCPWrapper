package com.ecs.bcp.test;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.*;
import java.util.Date;
import java.util.Base64;

import org.bouncycastle.x509.*;
import org.bouncycastle.asn1.x500.*;
import org.bouncycastle.cert.*;
import org.bouncycastle.cert.jcajce.*;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.jcajce.*;

public class PemToCerConverter {
    public static void convertPemToCer(String pemFilePath, String cerFilePath) throws Exception {
        // Read PEM file
        byte[] pemBytes = Files.readAllBytes(Paths.get(pemFilePath));
        String pemContent = new String(pemBytes);

        // Extract Base64-encoded key
        String base64Key = pemContent.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                     .replaceAll("-----END PUBLIC KEY-----", "")
                                     .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        // Convert to RSA PublicKey
        PublicKey publicKey = getRSAPublicKey(keyBytes);

        // Generate a self-signed X.509 certificate
        X509Certificate cert = generateCertificate(publicKey);

        // Write to .cer file
        try (FileOutputStream fos = new FileOutputStream(cerFilePath)) {
            fos.write(cert.getEncoded());
        }

        System.out.println("Conversion successful: " + cerFilePath + " created.");
    }

    private static PublicKey getRSAPublicKey(byte[] keyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    private static X509Certificate generateCertificate(PublicKey publicKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date expiryDate = new Date(now + 365 * 24 * 60 * 60 * 1000L); // 1 Jahr gültig
        BigInteger serialNumber = new BigInteger(64, new SecureRandom());

        X500Name issuer = new X500Name("CN=SelfSigned, O=MyOrg, C=DE");
        X500Name subject = issuer;

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
            issuer,
            serialNumber,
            startDate,
            expiryDate,
            subject,
            publicKey
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption")
            .setProvider("BC")
            .build(generatePrivateKey());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter()
            .setProvider("BC")
            .getCertificate(certHolder);
    }

    private static PrivateKey generatePrivateKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair().getPrivate();
    }

    public static void main(String[] args) {
        try {
            convertPemToCer("E:\\HDFC_Public_11Feb2025\\HDFC_Public_11Feb2025\\HDFCPublicKey_UAT02_8192.pem", "E:\\HDFC_Public_11Feb2025\\HDFC_Public_11Feb2025\\HDFCPublicKey.cer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
