package com.ecs.bcp.service.ejb;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;

import com.ecs.bcp.acl.xsd.ACLEmailRequestXsd;
import com.ecs.bcp.acl.xsd.Attributes;
import com.ecs.bcp.acl.xsd.Content;
import com.ecs.bcp.acl.xsd.From;
import com.ecs.bcp.acl.xsd.Recipient;
import com.ecs.bcp.acl.xsd.ReplyTo;
import com.ecs.bcp.acl.xsd.To;
import com.ecs.bcp.acl.xsd.UniqueArguments;
import com.ecs.bcp.pojo.SmsEmailPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class SMS {


	public static String genRandNo(int digit){
	    String characters = "0123456789";
	    String otp = RandomStringUtils.random(digit, characters);
	    return otp;
	  }
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String plaintext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, "UTF-8");
    }

    public static String sendSms(EntityRequest req) throws IOException, JSONException {

        try {
        	  PropertiesReader pro = new PropertiesReader();
        	String content = "You are successfully registered on Balance Confirmation Portal- PSB Alliance Private Limited";
        	// Correct the mobile number in the URL
      	  

            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
                    "enterpriseid=bcpsba" +
                    "&subEnterpriseid=bcpsba" +
                    "&pusheid=bcpsba" +  
                    "&pushepwd=bcp_05" +  
                    "&msisdn=" + req.getMobile() +  
                    "&sender=BCPSBA" +
                    "&msgtext=" + content;   

        	 System.out.println("request : "+requestUrl);
             
        	  String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
         		
      		String Url =bcpWebUrl+"/BCPSmsSender";
      		
      		System.out.println("web url---> "+Url);
      		
      		System.out.println("Posting Data to Web Server------------------->>:: ");
              
          	HttpResponse<String> tokenResponse = Unirest.post(Url)
      			    .body(requestUrl) // Include the JSON payload as the body
      			    .asString();

          	String responseStr = tokenResponse.getBody();
      		

            
                System.out.println(" Response: " + responseStr);

                return responseStr;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   public static String sendOtp(EntityRequest req) throws IOException, JSONException {

    try {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        PropertiesReader pro = new PropertiesReader();
        
        
       // String content = "Your OTP is:"+otp+". Please do not share it with anyone - PSB Alliance Private Limited";
        
        String content = otp + " is OTP for " + "login" + ". Please do not share the OTP with anyone- PSB Alliance Private Limited";

        String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
                "enterpriseid=bcpsba" +
                "&subEnterpriseid=bcpsba" +
                "&pusheid=bcpsba" +  
                "&pushepwd=bcp_05" +  
                "&msisdn=" + req.getMobile() +  
                "&sender=BCPSBA" +
                "&msgtext=" + content;   

        System.out.println("Request URL: " + requestUrl);
        
        String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
   		
		String Url =bcpWebUrl+"/BCPSmsSender";
		
		System.out.println("web url---> "+Url);
		
		System.out.println("Posting Data to Web Server------------------->>:: ");
        
    	HttpResponse<String> tokenResponse = Unirest.post(Url)
			    .body(requestUrl) // Include the JSON payload as the body
			    .asString();

    	String responseStr = tokenResponse.getBody();

        System.out.println("Response: " + responseStr);

        return String.valueOf(otp);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}    

   
   public static String sendEntityEmailOtp(EntityRequest req) {
		// Karix Email
		
		
		String reqJson = "";
		PropertiesReader pro = new PropertiesReader();
		try {
			

			
			 Random random = new Random();
		       // int otp = 100000 + random.nextInt(900000);
			 String EmailOtp = "123456";


			ACLEmailRequestXsd aclEmailRequestXsd = new ACLEmailRequestXsd();
			
			From from = new From();
			from.setEmail("noreply@digitalbalanceconfirmation.com");
			from.setName("DIGITAL BALANCE CONFIRMATION PORTAL");
			
			
			List<Recipient> lstRecipients = new ArrayList<Recipient>();
			Recipient recipient = new Recipient();
			
			Attributes attributes = new Attributes();
			attributes.setFiedl1(req.getEmailId());
			attributes.setField2(EmailOtp);
			recipient.setAttributes(attributes);
			
			Content content = new Content();
			content.setText ("You have initiated an entity registration request on the Balance Confirmation Portal. " +
                    "You have provided " + req.getEmailId() + " as the entity's email ID. " +
                    "For verification, please use " + EmailOtp + " as your OTP.\n" +
                    "Please do not share the OTP with anyone - PSB Alliance Private Limited.");


			UniqueArguments uniqArg = new UniqueArguments();
			uniqArg.setxApiheader("SL1589999999999");
			
			List<To> lstTo = new ArrayList<To>();
			To to = new To();
			to.setEmail(req.getEmailId());
			to.setName(req.getName());
			lstTo.add(to);
		
			recipient.setTo(lstTo);
			lstRecipients.add(recipient);
			
			ReplyTo replyTo = new ReplyTo();
			replyTo.setEmail("noreply@digitalbalanceconfirmation.com");
			replyTo.setName("Reply To");
			
			aclEmailRequestXsd.setFrom(from);
	//		aclEmailRequestXsd.setHeaders(headers);
			aclEmailRequestXsd.setRecipients(lstRecipients);
			aclEmailRequestXsd.setReplyTo(replyTo);
			aclEmailRequestXsd.setSubject("Balance Confirmation Portal | User Email ID Verification");
			aclEmailRequestXsd.setTemplateId("E01");
			aclEmailRequestXsd.setContent(content);
			
			reqJson = new Gson().toJson(aclEmailRequestXsd);
   		System.out.println("Email Payload-----------"+reqJson);
   		
   		
   		
   		String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
   		
		String Url =bcpWebUrl+"/BCPEmailSender";
		
//		HttpResponse<String> tokenResponse = Unirest.post(Url)
//				.body("")
//				.asString();
//
//		String token =  tokenResponse.getBody();
		
		System.out.println("WEB url : "+Url);
		
		String apiUrl =	"https://api.pinchappmails.com/v1/mail/send";
		
		HttpResponse<String> tokenResponse = Unirest.post(Url)
			    .header("Authorization", "Bearer NU1UM3FjN20rdTZtRmdBaTdKeUE5SFF0SCtzPQ")
			    .header("Content-Type", "application/json")
			    .header("api_url", apiUrl) // Forward the `api_url` header
			    .body(reqJson) // Include the JSON payload as the body
			    .asString();
   		
//			OkHttpClient client = new OkHttpClient().newBuilder()
//					.build();
//			MediaType mediaType = MediaType.parse("application/json");
//			RequestBody body = RequestBody.create(mediaType,reqJson);
//
//			Request request1 = new Request.Builder()
//					.url(Url)
//					.method("POST", body)
//					.addHeader("Authorization", "Bearer NU1UM3FjN20rdTZtRmdBaTdKeUE5SFF0SCtzPQ")
//					.addHeader("Content-Type", "application/json")
//					.addHeader("api_url", apiUrl)
//					.build();
			
			System.out.println("Posting Data to Web Server------ ICAI Token------------->>:: ");

			
//			Headers headers = request1.headers();
//			for (String name : headers.names()) {
//				System.out.println(name + ": " + headers.get(name));
//			}

//			Response response1 = client.newCall(request1).execute();
//
//			String jres = response1.body().string();
			
			String jres = tokenResponse.getBody();
			
			System.out.println("Email ACL Response------"+jres);
			
	/*		// create email transaction log
			if(jres.contains("\"request_id\": \"")) 
			{
//				emailPojo.setStatus("SUCCESS");
			}
			else {
//				emailPojo.setStatus("FAILED");
			}    */
			
			 // Store OTP in the database
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(req.getPan());

	        if (pojo == null) {
	        	
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(req.getPan());
	            smsEmailPojo.setEmailId(req.getEmailId());
	            smsEmailPojo.setEmailOtp(EmailOtp); 


	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");

	        } else {
	        	 SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	        	smsEmailPojo.setPan(req.getPan());
	        	smsEmailPojo.setEmailId(req.getEmailId());
	        	smsEmailPojo.setEmailOtp(EmailOtp); 
	            
	        	System.out.println("OTP updated!-----"+EmailOtp);
	            EjbLookUps.getSmsEmailRemote().update(smsEmailPojo);
	        }

	        
	       
			System.out.println("email transaction log created");   
		

			return jres;
		}catch(Exception ex) {
			ex.printStackTrace();
		//	sendKarixEmail(toEmail, otp);
			return null;
		}
   }	
		public static String sendUserEmailOtp(EntityRequest req) {
			
			String apiUrl =	"https://api.pinchappmails.com/v1/mail/send";
			String reqJson = "";
			
			try {
				

				
				// Random random = new Random();
			       // int otp = 100000 + random.nextInt(900000);
				 String EmailOtp = "123456";


				ACLEmailRequestXsd aclEmailRequestXsd = new ACLEmailRequestXsd();
				
				From from = new From();
				from.setEmail("noreply@digitalbalanceconfirmation.com");
				from.setName("DIGITAL BALANCE CONFIRMATION PORTAL");
				
				
				List<Recipient> lstRecipients = new ArrayList<Recipient>();
				Recipient recipient = new Recipient();
				
				Attributes attributes = new Attributes();
				attributes.setFiedl1(req.getEmailId());
				attributes.setField2(EmailOtp);
				recipient.setAttributes(attributes);
				
				Content content = new Content();
				content.setText("You have initiated an entity registration request on the Balance Confirmation Portal and provided your user email ID as " +
		                req.getEmailId() + ". For verification, please use " + EmailOtp + " as the OTP.");


				UniqueArguments uniqArg = new UniqueArguments();
				uniqArg.setxApiheader("SL1589999999999");
				
				List<To> lstTo = new ArrayList<To>();
				To to = new To();
				to.setEmail(req.getEmailId());
				to.setName(req.getName());
				lstTo.add(to);
			
				recipient.setTo(lstTo);
				lstRecipients.add(recipient);
				
				ReplyTo replyTo = new ReplyTo();
				replyTo.setEmail("noreply@digitalbalanceconfirmation.com");
				replyTo.setName("Reply To");
				
				aclEmailRequestXsd.setFrom(from);
				aclEmailRequestXsd.setRecipients(lstRecipients);
				aclEmailRequestXsd.setReplyTo(replyTo);
				aclEmailRequestXsd.setSubject("Balance Confirmation Portal | User Email ID Verification");
				aclEmailRequestXsd.setTemplateId("E01");
				aclEmailRequestXsd.setContent(content);
				
				PropertiesReader pro = new PropertiesReader();
				String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
				String Url =bcpWebUrl+"/BCPEmailSender";
				
				System.out.println("WEB url : "+Url);
				
				
				reqJson = new Gson().toJson(aclEmailRequestXsd);
	   		System.out.println("Email Payload-----------"+reqJson);
				OkHttpClient client = new OkHttpClient().newBuilder()
						.build();
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType,reqJson);

				Request request1 = new Request.Builder()
						.url(Url)
						.method("POST", body)
						.addHeader("Authorization", "Bearer NU1UM3FjN20rdTZtRmdBaTdKeUE5SFF0SCtzPQ")
						.addHeader("Content-Type", "application/json")
						 .addHeader("api_url", apiUrl) 
						.build();
				
				Headers headers = request1.headers();
				for (String name : headers.names()) {
					System.out.println(name + ": " + headers.get(name));
				}

				Response response1 = client.newCall(request1).execute();

				String jres = response1.body().string();
				System.out.println("Email ACL Response------"+jres);
				 // Store OTP in the database
		        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(req.getPan());

		        if (pojo == null) {
		            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
		            smsEmailPojo.setPan(req.getPan());
		            smsEmailPojo.setEmailId(req.getEmailId());
		            smsEmailPojo.setEmailOtp(EmailOtp); 


		            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
		            System.out.println("OTP created!-----");
		    
		        } else {
		        	 SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
		        	smsEmailPojo.setPan(req.getPan());
		        	smsEmailPojo.setEmailId(req.getEmailId());
		        	smsEmailPojo.setEmailOtp(EmailOtp); 
		            
		            EjbLookUps.getSmsEmailRemote().update(smsEmailPojo);
		            System.out.println("OTP updated!-----");
		        }

		        
		       
				System.out.println("email transaction log created");   
			

				return jres;
			}catch(Exception ex) {
				ex.printStackTrace();
			//	sendKarixEmail(toEmail, otp);
				return null;
			}
			

		}
	



		public static String sendSuccessRegMail(EntityRequest req) {
			// Karix Email
		//	String apiUrl = "https:// api-uat.aclemails.com/v1/mail/send";
//			String apiUrl = "https://api-uat.aclemails.com/v1/mail/send";
			
			String apiUrl =	"https://api.pinchappmails.com/v1/mail/send";
			String reqJson = "";
			
			try {
				

				
				// Random random = new Random();
			       // int otp = 100000 + random.nextInt(900000);
				 String EmailOtp = "123456";


				ACLEmailRequestXsd aclEmailRequestXsd = new ACLEmailRequestXsd();
				
				From from = new From();
				from.setEmail("noreply@digitalbalanceconfirmation.com");
				from.setName("DIGITAL BALANCE CONFIRMATION PORTAL");
				
			//	Headers headers = new Headers();
			//	headers.setXExample("");
				
				List<Recipient> lstRecipients = new ArrayList<Recipient>();
				Recipient recipient = new Recipient();
				
				Attributes attributes = new Attributes();
				attributes.setFiedl1(req.getEmailId());
				attributes.setField2(EmailOtp);
				//attributes.setField3(null);
				recipient.setAttributes(attributes);
				
				String helpdeskLink = "https://balcon.nesl.co.in/BCP_V1/";
				Content content = new Content();
				content.setText("We are glad to inform you that " +  req.getAuditeeName() + " has been successfully registered on the Balance Confirmation Portal. " +
						 req.getAuditorName() + " has been registered as the Primary User. The User ID for logging into the Balance Confirmation Portal " +
		                "has been sent to the registered email ID and mobile number of the Primary User. For further inquiries, please visit the following link: " + helpdeskLink);


				UniqueArguments uniqArg = new UniqueArguments();
				uniqArg.setxApiheader("SL1589999999999");
				
				List<To> lstTo = new ArrayList<To>();
				To to = new To();
				to.setEmail(req.getEmailId());
				to.setName(req.getName());
				lstTo.add(to);
			
				recipient.setTo(lstTo);
			//	recipient.setUniqueArguments(uniqArg);
				lstRecipients.add(recipient);
				
				ReplyTo replyTo = new ReplyTo();
				replyTo.setEmail("noreply@digitalbalanceconfirmation.com");
				replyTo.setName("Reply To");
				
				aclEmailRequestXsd.setFrom(from);
		//		aclEmailRequestXsd.setHeaders(headers);
				aclEmailRequestXsd.setRecipients(lstRecipients);
				aclEmailRequestXsd.setReplyTo(replyTo);
				aclEmailRequestXsd.setSubject("Balance Confirmation Portal | User Email ID Verification");
				aclEmailRequestXsd.setTemplateId("E01");
				aclEmailRequestXsd.setContent(content);
				
				reqJson = new Gson().toJson(aclEmailRequestXsd);
	   		System.out.println("Email Payload-----------"+reqJson);
	   		
	   		PropertiesReader pro = new PropertiesReader();
			String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
			String Url =bcpWebUrl+"/BCPEmailSender";
			
			System.out.println("WEB url : "+Url);
	   		
				OkHttpClient client = new OkHttpClient().newBuilder()
						.build();
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType,reqJson);

				Request request1 = new Request.Builder()
						.url(Url)
						.method("POST", body)
						.addHeader("Authorization", "Bearer NU1UM3FjN20rdTZtRmdBaTdKeUE5SFF0SCtzPQ")
						.addHeader("Content-Type", "application/json")
						 .addHeader("api_url", apiUrl) 
						.build();
				
				Headers headers = request1.headers();
				for (String name : headers.names()) {
					System.out.println(name + ": " + headers.get(name));
				}

				Response response1 = client.newCall(request1).execute();

				String jres = response1.body().string();
				System.out.println("Email ACL Response------"+jres);
				
		/*		// create email transaction log
				if(jres.contains("\"request_id\": \"")) 
				{
//					emailPojo.setStatus("SUCCESS");
				}
				else {
//					emailPojo.setStatus("FAILED");
				}    */
				
				 // Store OTP in the database
		        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(req.getPan());

		        if (pojo == null) {
		            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
		            smsEmailPojo.setPan(req.getPan());
		            smsEmailPojo.setEmailId(req.getEmailId());
		            smsEmailPojo.setEmailOtp(EmailOtp); 


		            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
		            System.out.println("OTP created!-----");
		    
		        } else {
		        	 SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
		        	smsEmailPojo.setPan(req.getPan());
		        	smsEmailPojo.setEmailId(req.getEmailId());
		        	smsEmailPojo.setEmailOtp(EmailOtp); 
		            
		            EjbLookUps.getSmsEmailRemote().update(smsEmailPojo);
		            System.out.println("OTP updated!-----");
		        }

		        
		       
				System.out.println("email transaction log created");   
			

				return jres;
			}catch(Exception ex) {
				ex.printStackTrace();
			//	sendKarixEmail(toEmail, otp);
				return null;
			}
			
		}


    
    public static void main(String[] args) throws Exception {
    	EntityRequest req = new EntityRequest();
    	
    	req.setMobile("918903738252");
    	sendOtp(req);
    	
    	
	}
}
