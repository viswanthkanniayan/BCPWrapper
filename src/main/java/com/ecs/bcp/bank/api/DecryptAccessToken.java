package com.ecs.bcp.bank.api;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.OAEPParameterSpec;

import com.ecs.bcp.bank.api.canara.AESCanaraUtils;

public class DecryptAccessToken {


	public static void main(String[] args) throws Exception {


		String aesKey = AESCanaraUtils.generateAESKey();
		System.out.println("aesKey--> "+aesKey);
		
		// NESL public key
		String NESLPublicKey ="-----BEGIN CERTIFICATE----- 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 -----END CERTIFICATE-----";
		PublicKey neslPublicKey = AESCanaraUtils.loadPublicKey(NESLPublicKey);
		// encrypt AES key
		
		
		//CBI
		String BANKPublicKey ="-----BEGIN CERTIFICATE----- MIIFQzCCBCugAwIBAgIKY2AZ8IjMOAKy6DANBgkqhkiG9w0BAQsFADCB9zELMAkGA1UEBhMCSU4x RTBDBgNVBAoTPEluc3RpdHV0ZSBmb3IgRGV2ZWxvcG1lbnQgYW5kIFJlc2VhcmNoIGluIEJhbmtp bmcgVGVjaG5vbG9neTEdMBsGA1UECxMUQ2VydGlmeWluZyBBdXRob3JpdHkxDzANBgNVBBETBjUw MDA1NzESMBAGA1UECBMJVGVsYW5nYW5hMSowKAYDVQQJEyFSb2FkIE5vLiAxLCBNYXNhYiBUYW5r LCBIeWRlcmFiYWQxFTATBgNVBDMTDENhc3RsZSBIaWxsczEaMBgGA1UEAxMRSURSQlQgU3ViIENB IDIwMjIwHhcNMjQwMTE3MDY1ODAxWhcNMjYwMTE2MTgyOTU5WjAWMRQwEgYDVQQDEwtDQklOMDI4 MjY4NDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALki5H+Dv7PD9JFPnGiN/teKzxai fJc2fwTJ/ecMxte/Q6FTSeYSQUidIkLS9SWpZ3TFcNQsItSAVrrP0Tkney5PetFWSGaScugNr5GZ YPHrs2AfNtBvLs71S42dvEnobA1ANEHe6xKZVpFlkL68HoEEZw2nCtXne60t/xxDrv8GOpg/JuoG By2OufonJns+aA/IGvFMDbK66CcJK7TZyDMAyn3GNnZ2gTEgte4hxNWmUgLZpSKf2C68XNdiXUbO LPd/zhb3pRK1FpUgxEwwB0oSzZr0DJXxq95cnSmBIcrWtvC+LcRaWei6ByqakUdqyV7SsH0BYKPR 71HWnx+eZNECAwEAAaOCAa8wggGrMAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgWgMBMGA1Ud JQQMMAoGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFGbDtB1WM9GA7doOpEcW0w63hQHDMB0GA1UdDgQW BBQ1RPCeeYT7gD9KAqOhoW2FivsXPDBxBggrBgEFBQcBAQRlMGMwOQYIKwYBBQUHMAKGLWh0dHA6 Ly9pZHJidGNhLm9yZy5pbi9jZXJ0L0lEUkJUU3ViQ0EyMDIyLmNlcjAmBggrBgEFBQcwAYYaaHR0 cDovL29jc3AuaWRyYnRjYS5vcmcuaW4wDwYDVR0RBAgwBocECgUBKTBlBgNVHR8EXjBcMCqgKKAm hiRodHRwOi8vMTAuMC42NS42NS9JRFJCVFN1YkNBMjAyMi5jcmwwLqAsoCqGKGh0dHA6Ly9pZHJi dGNhLm9yZy5pbi9JRFJCVFN1YkNBMjAyMi5jcmwwSwYDVR0gBEQwQjBABgZggmRkAgMwNjA0Bggr BgEFBQcCAjAoHiYAQwBsAGEAcwBzACAAMwAgAEMAZQByAHQAaQBmAGkAYwBhAHQAZTANBgkqhkiG 9w0BAQsFAAOCAQEApgBZfTjJypwPFuwY/zbEwQxAdqU7IaGhFA441Ji5UkNV/uLGU61eD13GV+7R TBpYgqrX0fM2DShPe3iQhj73Yng745UOYd0mJL44mnXZIhcgjrkdxUM8NE/FAAgfw+CnmLFezJrV bJk7ORqhGH5q4CQK3MffT3vqvE52fIGU1uM58+vt9T5+zhVr+bMK3T38WkY/vTh6TOyE8rRZGK1w S2nSDdtPVA2F2RNMhU7HntXvo8FT/2uZ1xbbrqfY2X5he5v2+lqetFXpn3Wkx8qYPEezgG5ylmWx L0SkTbJG18VBrKUIEu/DKu2sip0YOk65cV/hPCv2/HLFdKmlVpavTA== -----END CERTIFICATE-----";
				

		PublicKey PublicKey = AESCanaraUtils.loadPublicKey(BANKPublicKey);

		System.out.println("BankPublicKey : "+PublicKey);
		
		//String accsessToken = encryptRSA(aesKey, PublicKey);

	//	System.out.println("accsessToken -------with bank public key---->> :: => "+accsessToken);
		
	
	
		//String accsessToken = encryptRSA(aesKey, neslPublicKey);
		
    	String accsessToken = "WvsaNqJjl+JXyDcjjEjpinN4++KsVqIDpj6lNhVoLH+cTJN2JbJ5ot0lf1uiW1x7iNjAlsZOunoJm7qKBu0mpT+t4NJYsmGzqCqFiV5SHYgWJw5eUuUNqmp4KCc/Ej/hp7qTSc78DeCiFzr+howNEA6Kt5B24D5AejLHxLT8ze1k2tm6Mey1YpAuHkWaN6uJCyK10412KAjjYuICg7ThDlpED5abxsSrZZWPR6bX/GshAx4h2HYUBairvXEDIuhaTK11MLKUXzPpMFassdmNBbEpevrLWIFjBOErp9rjsuHsTv7gaGrnsUWa0GDqNdnt5gMY+pml64Ai/2fPwSQKzA==";
//		String accsessToken = "lmFwCVUG63LeaNPJ42L96AZi+g2ziqZzuS0RCDv9g2RY8ptg3n3nECLnuxm2xAlWT8wa45qA1Eas6vWqxVDVWCgHto+Ld6dGAj8Cv46VfF9GYj5OkcoYOqnJHwJPFWz+khNL1iFXKg9kKyTlbKAbAZ3sO/odaowQ26uy8dzgK7t34YOEpSxkbKQtxUHOOLBbIJsfSUp7FYGbFabgP8AJC/9Uc7ZAogFqJiGxOFQWfLYQ/wq1hohX8YeuD2goPa2XZyswsMahyAyN2pk+tT/SHV8WeCJ38s6N8xuimTZfev1QdtCswfA3VHn9XZ0D4jmjVq5WEWFx0dGguUNl2t5JMw==";

//		System.out.println("accsessToken -------public key---->> :: => "+accsessToken);


		// NESL private key - UAT
		String NESLPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDzkCSV/NhygxK8LvalEZV/JrWmcPkq49hMJGQwwPlko9Y/BuYq+oXZnpFClhxL6uk74Da4nSoph4DTS4s+kcEtvPBjmBhixibWMqA4v5mb/ppKIOiBBFlDrd+2nRV0vrIMNH/d0xLb/Ed9SB/ZMmy9Aq+MHMVYLedR5Tp1ela+9n+DXnzsJNcPcI2RFzzeokvZE1qyxIIeW1FErlDYhNRK8tDhCzklNvbKaH5Esn3jOBRsg05jSyrymWu8RP9jged8Xgle11UyrPslCNgD0Kna7DbkM5lcOYbr9poqpzyG0xhfE9kVXqbirqkM3691yQMKz6ZhNwccnBMB4Kawrpi5AgMBAAECggEBAMOtTndhx9NYErk1UdqUIfMhelbZ9JTMsH2bzxKMNtDv/7RrKofk9Zw8lJ9WUi178R21qMDxI2px/sHCs3/1QaopXUfmbdENOS5lRevRout8xSBBCAp/H+gLzOu9j/tXKVw84W2pokEykYzJYSzyIgJIIhkDFFkfp/RXoXu4afDIL7QmajFMiSIJR6tzNOWS3CyLB7bIG1caCdPToaQkdwFDCicNcA9rZi4tPi1TlddYxzKMLkUJ6Bf1lGTT/OZmyxkg6ixLmvBjvYlfZNvOI1OmT9Gt909U3VvRuido/b2wW38MNU1iQ+DI+zGXetuzR1vwMcINDfNiZnUp+Yuhc/ECgYEA+7dwS6YZSjy1b1Nd9vT+O+e7ZE+GIzBWcHBVXYrWj5Rt2R0HHZ5jypEm2/eTYx3BTGjGnqMrRbT/1vwX/sUxCoY78Mvu87Et3ML+cpyOJ6DPqEPOv7GwNjXVNGpymCM5DHjbXmYBqth+MpRY39lERl5rP/Ptsx+1AfS94iFccjcCgYEA97UvVVoVTSfnRvto8wUBiuEbpsu4uuy/ijxjvLZ1cbVSt0Oyd5xlVuy/vG1vrv5Cm7HhrGs7MhKIbQF7WhHXV4ankvn+zwUmSclYpVWWBkryKX6fbJfJh90q/tL1RgMblj0yDz53kH6ViVe+Mq+RTibSh0irt8GfPAjV6e6VlI8CgYEAjvmV3lI9IS60e8RlpgVodsxcY1DMRyaIopsb0eMvliRf9KeJSCiUVBX4dY79d5oKFoWY87iItrJlc06DrqGBBpJSb6bWVjL4cuGLN7x2/klYSiIhyD58cX5IWNpxtrqjB9OJ/ud/4PPRUpdyl8tH/ZRZ5Nx/0nObE75ZMJ10bicCgYA0iY08gRq7fpcjCve7c3hcSFphChxoKQaG/z/4KorGTzr3+7fCfr1Prm8MO/nQF8Vw2E2RED1B5YRh+kp7VAVkXv7zwWo15lW0mKvghUKImyS5gE237omj81jHK18yNj6HovsXGJyrXO3Cb4W7olkjRkCoyNUC6GIpjYYxU5UOKQKBgGXDf9BSp3A0/oXCfLutftBH0/BgmuiGKVyGQ/0xmnaT9wA7wKKqg/yRbA5lfNbXR7KWCuLjZz6RJmcQTXypCEyocRSIupP9pY/aTF3MCMjbQH8919EycMHmkN9GSjljb5mngMHxETeSZP5G3pHGO0Ki5zX6pWTm9OoneN5jrrjl";
		
		// NESL private key - PROD
//		String NESLPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCHYMfZTrtVl/44kRUXDefpM22PTOxJiwj9et6FGieoBMIouBvWV274eO1uAU2KBMAbia9XunvtacpiBeOX5ikonZglSVWpURy8F7JEuBAw2Aox1WAi87zwvbNthl5/ICCq7uid971CwSGOKhWccJUTNwv2zud0gvEy7/yB7uJl/RxWwDEOKZwjRvWVk3Y+HmYAijbQlV4frKQDf3rmra9JOjqUv5y+3PRNDY55dby3c80v0qadsVCJuXQMBucO8EHxKk4BZJdaFLQmIc1t2TY2bAT9n++DvJrRKmsjzqJsmiroSQECX5XddL5k6RLViUHTK10CkoZ2i8mubtAj7uz3AgMBAAECggEAbNP0VFDib/B7wWWAGsRnBiT9khE4cfZ/B6Zac42PC5anrqwyVKB86OntlF0fxDAFbx3EAzY6oaRFuX8acuXneGsYAAFwB2jZPKuf93YwLdO+fkkVu4NNBAq7zvQRywg8uqzL+xFI5/zxyKM0NEqczJGX4JR7pEIPO++IgbtT7L/bZTjgFh/BqYbE7ikAOR+yOoKC4pXRgUZZVQQ++uX9ACGeUuYGkQW3oxiCYyzFAhT7AW4H4sFEAc3Q0DVLhED4IBRg0duGHHc50JA3gBa+fCSck19I1ovDCs2QR+ICKr1W+hGBzON8m/6p2qyZEQCrc2VP79fO1q+SBeBRZP494QKBgQDQkX2O7bedPlrsqfWQpZfAorV2/apCjYAB5ZPQ8nEFpfZJvPdXNSelzlrkOz9bvMyvRhoR2+DOmUObOEAz/LPH4XWADoc1QOjgFUdE87ccejBgzD79xhwvGqXhlt0obAmgEejF3PYlXkyz/gsFBOpflXemXki8DuZuFUyU7t4tCwKBgQCmKkVtAuIHjoYnzPmqQ81SG9IUaCZFu8T8A7Ffl/duCOMJhmFFDn97vU14fJuuhHfmY+ULtJyFRHngiKSvzGDrLXEfKd5p2oU50VMoFtv/3tqhLicjglKRvO3KiicXoTX6iuumlItZXp99PxaOuvquSVw7fE7tppvjgvbuE4n7RQKBgQCs8nfzNRu0wCgVZznBoA6eJk3jRaURWtRyvjQ8Z01hKaV4r8FKB0HBM8QazSZUo9H25WJovLNFncF5zMMwWLObY1FTxrhHaHOynPJkMtP8QTYsjjENrmFgjfBPiBILmsrAeRlUOU3C3tcrydTJbLIMYJy+0B/wEUryraa7OPfy0QKBgB8/K7cIJ8EMCCZL4VcnUODz4ll8DFZorO5HM3+qcBj9CtSrtHbr9wUp3/ucUxpKRtHITzYYTKay7chPaci1QjR3YyJdGToGoa7xshWD1vMS0UImD64Zv7UwNfc0a42B/0iR/yZgIKQ5qvw+dNt6RdGocfOtuJnwDriF1DGI8xWRAoGBAJsO2NBHpFPGHGYHTU5WdRYMtDCDNXqdbXFgrLtOvAYScOTUutreVCGWbPaXaGcF4IsZ9UnXx0aHl73lu8o9Z3GSWGkFC9uosRbhYdSfmsG6Md2zPhSM2oMJj/9B4xUq2Y++fiRaMy3yw9Yk7plxc9LsPMhw4hO3bntvP4CMQl+a";

		// canara private key
//		String NESLPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCq23mPiYkZXDz7pGhLglf6OkdU9RoK6oBxKSNnPj9AkQLB80rmnH2ABvjrJCwS6kWVKIbC0tVAZq0twavN2lL2HgDgxNoR/jjG2lqDngBfcvqoqtw/TYXF2XqG3i+TqeSnHXqRDJLvyJ5hFpfDmYWkhEZnmrrLMcBJ679/hCKQ+VKGPhgh6p+EWC6MYQN9UqR9PeZswWVBHhNC2CCW0cms7u1Vg7ONU+Eld1vX8b6YPrCm6M5rs7mJnMOvP1KjuBIogYpTiN8ad9CdWioO+GjthV0OiSB0G0wtVTgSZAJ137rHDXC/f3JeUbZ1PrTV+3P+ECi9vN1bq40qXGNv7cU5AgMBAAECggEAciAoErjL2yTBl1U6VTW7jmlfPGB8BM6e/Pd9rnjlxzsVnHfmkCP3sIz3uBlxVzElSXzIh1pNF7d99hJnsBklUUC5VWooP9yQEEPKMiBYH2muBPwcIiymLoIWVgATx460LOyD8l9zxrc7E58DwKPLoRi57npsS03wI3BVedfJPwwzK4gSKQ2B50kl7d28yU+5xjQFfGpE7hDip7iCGd1J9KdOOGDCgA+c+dOrtJtCfTVRgCuFmugJwRs4kqrpGQYDJAJmgihn3gR6j6lveYcgJuy/2FfP5O1YSzWxYSk/9QJfLPLRGKqZ1Q2HwzYjrGc6KIDZmdxstnYUZUVXWgUAAQKBgQDeJAhmUwVCgQ66yHM4ekgkXD6Jva1Me5hxPSJ2WtiYt5EaRcKHCCKwxl94gKSzYrFfdzMK8LklspUgJHUnz1+oUYHD9lrbp+gOKma/oSyOZF2KwSQ0Wkl87EBocXBOxZLCKWPYvlMOAOSXBwPVRue1DFFGtFDqVIk2JaN/alqheQKBgQDE5lrCvL7vyrUqFj8iXMrbUoHp39QWwgaobZj52JvJ7feh2NYptV3uceWEOnEGkyrMYeElIINYdwAwv1qo72Q5sArxAQW4jl9VyxFcwDDBkJDDrRGZaA7U6WupUdNSM6fM7IcsJeB+kNhelYpdfEzIlVSUCFtxa5vp8juHxKkRwQKBgGGreCH9WK+v7NkFL1vir/uEr16CjkK1iQYo+hENynQDKgJiL7CNTCtrXSivbzsMJq6xyziBNPa1OUojnXs+e3GXPLX7iGFRxv8Ld5fD9sI4sMREPXXjSLJrcbo6PkX0Kp3B8wumSwA6NkeNJwtMHK6G1ul+eKE/QAEVtXhdO7IJAoGAPnAFq+cKbSPMmxH+5zihozbgt3cg3C6eyd4nOmMz067AbfRCxWsb5Db93xZhLQKR+cm4CXvr6quhxdOIPjXbhGeMsKvwUnvjQ2NnmA/h5h+fqE1DXXAf8+gpGcI2zsROm68NljRUog3wbt3gyJBYEOs5rM+Rn7/N6zvDIOR98IECgYEApSPuREX5A/1q/EkadWUg20M5zO4+utdiHsTTXJJnaaLC0PU0TzeqA1NBRKTdhDDEhrToRYNkCEM3fWOqAcKfa/zZo9eRvRX4g/wzJsbXOTzkCpehE82QXhINTEhlBqLxOQjvW799tU5+39r3n4VIQ/CK0wA1uScO23JWrhYnEe8=";

		
		//decrypt
		
		PrivateKey PrivateKey = loadPrivateKeyFromString(NESLPrivateKey);

		// decrypt access token key
		String decryptedAesKey = decryptRSA(accsessToken, PrivateKey);


		System.out.println("decryptedAesKey : "+decryptedAesKey);
		
//		printSecurityProviders();

	}


	
	
	public static PublicKey loadPublicKey(String input) throws Exception {
	    // Check if the input is in PEM format (contains BEGIN CERTIFICATE)
	    if (input.contains("-----BEGIN CERTIFICATE-----")) {
	        // Handle PEM certificate input
	        String base64Certificate = input
	                .replace("-----BEGIN CERTIFICATE-----", "")
	                .replace("-----END CERTIFICATE-----", "")
	                .replaceAll("\\s+", "");

	        byte[] certBytes = Base64.getDecoder().decode(base64Certificate);

	        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes)) {
	            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
	            return certificate.getPublicKey();
	        }
	    } else {
	        // Handle Base64 encoded public key input
	        byte[] decodedKey = Base64.getDecoder().decode(input);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
	        return keyFactory.generatePublic(keySpec);
	    }
	}

		
		// base64 private key without begin and end text
		public static PrivateKey loadPrivateKeyFromString(String base64PrivateKey) throws Exception {
			byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change if needed for other algorithms
			return keyFactory.generatePrivate(keySpec);
		}

		
		
//		// RSA Key Encryption
//		public static String encryptRSA(String aesKeyBase64, PublicKey publicKey) throws Exception {
//			byte[] aesKey = Base64.getDecoder().decode(aesKeyBase64);
//			
//			//	Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
//			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			byte[] encryptedKey = cipher.doFinal(aesKey);
//			return Base64.getEncoder().encodeToString(encryptedKey);
//		}
//		
//		// RSA Key Decryption
//		public static String decryptRSA(String encryptedKeyBase64, PrivateKey privateKey) throws Exception {
//			byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);
//			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//			//	Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			byte[] decryptedKey = cipher.doFinal(encryptedKey);
//			return Base64.getEncoder().encodeToString(decryptedKey);
//		}

		
		// RSA Key Encryption
	    public static String encryptRSA(String aesKeyBase64, PublicKey publicKey) throws Exception {
	        byte[] aesKey = Base64.getDecoder().decode(aesKeyBase64);
	        
	        // Define explicit OAEP parameters
	        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);

	        // Encrypt using OAEP
	        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
	        System.out.println("encryptRSA Algorithm: " + cipher.getAlgorithm());
	        System.out.println("encryptRSA Provider: " + cipher.getProvider());
	        System.out.println("encryptRSA Block Size: " + cipher.getBlockSize());
	        System.out.println("encryptRSA Parameters: " + cipher.getParameters());
	        
	        if (publicKey instanceof RSAPublicKey) {
	            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
	            System.out.println("encryptRSA Key Format: " + rsaPublicKey.getFormat());
	            System.out.println("encryptRSA Modulus Size: " + rsaPublicKey.getModulus().bitLength());
	            System.out.println("encryptRSA Public Exponent: " + rsaPublicKey.getPublicExponent());
	        }
	        
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] encryptedKey = cipher.doFinal(aesKey);
	        return Base64.getEncoder().encodeToString(encryptedKey);
	    }

	    // RSA Key Decryption
	    public static String decryptRSA(String encryptedKeyBase64, PrivateKey privateKey) throws Exception {
	        byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);
	        
	        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
	        System.out.println("decryptRSA Algorithm: " + cipher.getAlgorithm());
	        System.out.println("decryptRSA Provider: " + cipher.getProvider());
	        System.out.println("decryptRSA Block Size: " + cipher.getBlockSize());
	        System.out.println("decryptRSA Parameters: " + cipher.getParameters());
	        
	        if (privateKey instanceof RSAPrivateKey) {
	            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
	            System.out.println("decryptRSA Key Format: " + rsaPrivateKey.getFormat());
	            System.out.println("decryptRSA Modulus Size: " + rsaPrivateKey.getModulus().bitLength());
	        }
	        
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        byte[] decryptedKey = cipher.doFinal(encryptedKey);
	        return Base64.getEncoder().encodeToString(decryptedKey);
	    }
	    
	    // Print all security providers and algorithms
	    public static void printSecurityProviders() {
	        for (Provider provider : Security.getProviders()) {
	            System.out.println("Provider: " + provider.getName());
	            for (Provider.Service service : provider.getServices()) {
	                if (service.getType().equalsIgnoreCase("Cipher")) {
	                    System.out.println("  Algorithm: " + service.getAlgorithm());
	                }
	            }
	        }
	    }
	    
	    
	    
}
