package com.ecs.bcp.service.ejb;

import java.util.List;

import javax.mail.internet.MimeBodyPart;

import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class EmailService {

	
	public static EntityResponce sendforgetPassEmail(EntityRequest requestXSD) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
	    List<UserDetailsPojo> userPojo = null;
	    try {
	        
	        userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("emailId", requestXSD.getEmailId());
	        if (userPojo == null) {
	            smsEmailResponseXsd.setError(true);
	            smsEmailResponseXsd.setErrorCode(Constants.ECSV0010); 
	            smsEmailResponseXsd.setErrorDescription("Email not found");
	            return smsEmailResponseXsd;
	        }

	        PropertiesReader pro = new PropertiesReader();
	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String forgetpassLink = "https://balcon.nesl.co.in/BCP_V1/forgetPasswordAction";
	        String helpdeskLink = "helpdesk@digitalbalanceconfirmation.com";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));


				MimeBodyPart textPart = new MimeBodyPart();
	        	String	content = EjbLookUps.getEmailTemplateRemote().findById("FORGET_PASSWORD_EMAIL").getStringValue();
				content = content.replace("%LINK%", forgetpassLink);
				content = content.replace("%HELPDESK%", helpdeskLink);

			        textPart.setText(content);

	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + requestXSD.getMobileNo() +
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

	       

	        smsEmailResponseXsd.setError(false);
	        smsEmailResponseXsd.setErrorDescription("Email sent Successfully");
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
