package com.ecs.bcp.bank.api;

import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.io.ByteArrayInputStream;

public class ExtractPublicKey {

	 public static void main(String[] args) throws Exception {
		 
	    //    String certBase64 = "-----BEGIN CERTIFICATE----- MIIFQzCCBCugAwIBAgIKY2AZ8IjMOAKy6DANBgkqhkiG9w0BAQsFADCB9zELMAkGA1UEBhMCSU4x RTBDBgNVBAoTPEluc3RpdHV0ZSBmb3IgRGV2ZWxvcG1lbnQgYW5kIFJlc2VhcmNoIGluIEJhbmtp bmcgVGVjaG5vbG9neTEdMBsGA1UECxMUQ2VydGlmeWluZyBBdXRob3JpdHkxDzANBgNVBBETBjUw MDA1NzESMBAGA1UECBMJVGVsYW5nYW5hMSowKAYDVQQJEyFSb2FkIE5vLiAxLCBNYXNhYiBUYW5r LCBIeWRlcmFiYWQxFTATBgNVBDMTDENhc3RsZSBIaWxsczEaMBgGA1UEAxMRSURSQlQgU3ViIENB IDIwMjIwHhcNMjQwMTE3MDY1ODAxWhcNMjYwMTE2MTgyOTU5WjAWMRQwEgYDVQQDEwtDQklOMDI4 MjY4NDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALki5H+Dv7PD9JFPnGiN/teKzxai fJc2fwTJ/ecMxte/Q6FTSeYSQUidIkLS9SWpZ3TFcNQsItSAVrrP0Tkney5PetFWSGaScugNr5GZ YPHrs2AfNtBvLs71S42dvEnobA1ANEHe6xKZVpFlkL68HoEEZw2nCtXne60t/xxDrv8GOpg/JuoG By2OufonJns+aA/IGvFMDbK66CcJK7TZyDMAyn3GNnZ2gTEgte4hxNWmUgLZpSKf2C68XNdiXUbO LPd/zhb3pRK1FpUgxEwwB0oSzZr0DJXxq95cnSmBIcrWtvC+LcRaWei6ByqakUdqyV7SsH0BYKPR 71HWnx+eZNECAwEAAaOCAa8wggGrMAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgWgMBMGA1Ud JQQMMAoGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFGbDtB1WM9GA7doOpEcW0w63hQHDMB0GA1UdDgQW BBQ1RPCeeYT7gD9KAqOhoW2FivsXPDBxBggrBgEFBQcBAQRlMGMwOQYIKwYBBQUHMAKGLWh0dHA6 Ly9pZHJidGNhLm9yZy5pbi9jZXJ0L0lEUkJUU3ViQ0EyMDIyLmNlcjAmBggrBgEFBQcwAYYaaHR0 cDovL29jc3AuaWRyYnRjYS5vcmcuaW4wDwYDVR0RBAgwBocECgUBKTBlBgNVHR8EXjBcMCqgKKAm hiRodHRwOi8vMTAuMC42NS42NS9JRFJCVFN1YkNBMjAyMi5jcmwwLqAsoCqGKGh0dHA6Ly9pZHJi dGNhLm9yZy5pbi9JRFJCVFN1YkNBMjAyMi5jcmwwSwYDVR0gBEQwQjBABgZggmRkAgMwNjA0Bggr BgEFBQcCAjAoHiYAQwBsAGEAcwBzACAAMwAgAEMAZQByAHQAaQBmAGkAYwBhAHQAZTANBgkqhkiG 9w0BAQsFAAOCAQEApgBZfTjJypwPFuwY/zbEwQxAdqU7IaGhFA441Ji5UkNV/uLGU61eD13GV+7R TBpYgqrX0fM2DShPe3iQhj73Yng745UOYd0mJL44mnXZIhcgjrkdxUM8NE/FAAgfw+CnmLFezJrV bJk7ORqhGH5q4CQK3MffT3vqvE52fIGU1uM58+vt9T5+zhVr+bMK3T38WkY/vTh6TOyE8rRZGK1w S2nSDdtPVA2F2RNMhU7HntXvo8FT/2uZ1xbbrqfY2X5he5v2+lqetFXpn3Wkx8qYPEezgG5ylmWx L0SkTbJG18VBrKUIEu/DKu2sip0YOk65cV/hPCv2/HLFdKmlVpavTA== -----END CERTIFICATE-----"; // truncated for readability

	     //   String certBase64 = "-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----"; // truncated for readability

	        String hdfcCer = "-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----";
	        
	        loadPublicKeyFromCertificate(hdfcCer);
	        
	        //	        certBase64 =   certBase64.replaceAll("\\s+", "");
	
	        
	        // Decode and parse the certificate
//	        byte[] certBytes = Base64.getDecoder().decode(certBase64);
//	        CertificateFactory factory = CertificateFactory.getInstance("X.509");
//	        X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
//
//	        // Extract public key
//	        PublicKey publicKey = cert.getPublicKey();
//	        System.out.println("Public Key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
	  
	 
	 }
	 
	 
	
	


public static PublicKey loadPublicKeyFromCertificate(String pemCertificate) throws Exception {
	String base64Certificate = pemCertificate
			.replace("-----BEGIN CERTIFICATE-----", "")
			.replace("-----END CERTIFICATE-----", "")
			.replaceAll("\\s+", "");
	
	byte[] certBytes = Base64.getDecoder().decode(base64Certificate);
	
	CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	
	try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes)) {
		X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
		 PublicKey publicKey = certificate.getPublicKey();
		 System.out.println("Public Key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		  
		return certificate.getPublicKey();
	}
}

}