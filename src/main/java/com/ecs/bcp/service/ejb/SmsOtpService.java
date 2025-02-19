package com.ecs.bcp.service.ejb;

import javax.mail.internet.MimeBodyPart;

import com.ecs.bcp.pojo.SmsEmailPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SmsOtpService {

	public static String genRandNo(int digit){
		//String characters = "0123456789";
		String smsOtp = "123456";
		//String otp = RandomStringUtils.random(digit, smsOtp);
		return smsOtp;
	}
	
	
	
	public static EntityResponce sendSuccessRegOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        System.out.println("pojo---->" + pojo);
	        PropertiesReader pro = new PropertiesReader();
	        try {

	        	
	        	
	        	MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_SUCCESSFUL_REG").getStringValue();
			        textPart.setText(content);


	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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
	      		

	            
	                System.out.println(" Response: " + responseStr);
	            System.out.println("Response: " + responseStr);

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }



	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendAuditorAssignMobileOpt(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        System.out.println("pojo---->" + pojo);
	        PropertiesReader pro = new PropertiesReader();
	        
	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));

	        	MimeBodyPart textPart = new MimeBodyPart();
	        	String content = EjbLookUps.getEmailTemplateRemote().findById("SMS_AUDITOR_ASSIGN_OPT").getStringValue();
	        	content = content.replace("%OTP%", smsOtp);
	        	textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	
	public static EntityResponce sendUserRegOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        PropertiesReader pro = new PropertiesReader();
	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_USER_REG_OTP").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%USER_VERFICATION%",smsEmailRequestXsd.getPurpose());

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}
	public static String sendUserPasswordSms(String mobileNo , String password) throws Exception {

	    try {

	        // Generate and send OTP via API
	        PropertiesReader pro = new PropertiesReader();
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_NEW_PASSWORD_OPT").getStringValue();
				content = content.replace("%PASSWORD%",password);
				
				

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + mobileNo +
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

	        return "success : "+responseStr;

	    } catch (Exception e) {
	        e.printStackTrace();
	        
	        return "failed";
	    }
	}
	public static EntityResponce sendForgetPassOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        PropertiesReader pro = new PropertiesReader();
	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_FORGET_PASS_OPT").getStringValue();
				content = content.replace("%OTP%", smsOtp);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendbcAuditorAssignOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        PropertiesReader pro = new PropertiesReader();
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_BC_AUDITOR_ASSIGN").getStringValue();
				content = content.replace("%OTP%", smsOtp);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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


	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendbcAuditeeAssignOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_BC_AUDITEE_ASSIGN").getStringValue();
				content = content.replace("%OTP%", smsOtp);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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


	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendbcPaymentVerfiy(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_PAYMENT_VERF").getStringValue();
				content = content.replace("%OTP%", smsOtp);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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


	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendbankLinkageOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_BANK_LINKAGE").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%BANK_NAME%", smsEmailRequestXsd.getBank());

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendacceptbankLinkageOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_ACCEPT_BANK_LINKAGE").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%BANK_NAME%", smsEmailRequestXsd.getBank());

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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


	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}

	public static EntityResponce sendrejectbankLinkageOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_REJECT_BANK_LINKAGE").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%BANK_NAME%", smsEmailRequestXsd.getBank());

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}
	public static EntityResponce senddeActivebankLinkageOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_DEACTIVE_BANK_LINKAGE").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%BANK_NAME%", smsEmailRequestXsd.getBank());

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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


	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}
	public static EntityResponce sendmodifDetailsOTP(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("SMS_MODIFI_DETAILS_OTP").getStringValue();

				content = content.replace("%OTP%", smsOtp);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}
	
	
	public static EntityResponce sendloginOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    PropertiesReader pro = new PropertiesReader();
	    
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        String login ="login";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
			
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("LOGIN_OTP").getStringValue();

				content = content.replace("%OTP%", smsOtp);
				content = content.replace("%LOGIN%", login);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Failed to generate or send OTP");
	        }

	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOptStatus("PENDING");
	            smsEmailPojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 

	            EjbLookUps.getSmsEmailRemote().update(pojo);
	            System.out.println("OTP updated!-----");
	        }

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0011);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0011));
	        return smsEmailResponseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        smsEmailResponseXsd.setError(true);
	        smsEmailResponseXsd.setErrorCode(Constants.ECSV0009);
	        smsEmailResponseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0009));
	        return smsEmailResponseXsd;
	    }
	}
}
