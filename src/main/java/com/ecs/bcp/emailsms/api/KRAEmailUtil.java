package com.ecs.bcp.emailsms.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;

import com.ecs.bcp.acl.xsd.ACLEmailRequestXsd;
import com.ecs.bcp.acl.xsd.Attributes;
import com.ecs.bcp.acl.xsd.From;
import com.ecs.bcp.acl.xsd.Recipient;
import com.ecs.bcp.acl.xsd.ReplyTo;
import com.ecs.bcp.acl.xsd.To;
import com.ecs.bcp.utils.EjbLookUps;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KRAEmailUtil {
	

	
	public static void sendACLEmail(String pan, String toEmail, String otp) {
		// Karix Email
	//	String apiUrl = "https:// api-uat.aclemails.com/v1/mail/send";
		String apiUrl = "";
		String reqJson = "";
		
		try {
			

			apiUrl = "";
			
	//		System.out.println("---api url----"+apiUrl);

			String fromEmail = EjbLookUps.getSettingsRemote().findById("FROM_EMAILID").getStringValue();
			String fromEmailName = EjbLookUps.getSettingsRemote().findById("FROM_EMAIL_NAME").getStringValue();
			String templateId = EjbLookUps.getSettingsRemote().findById("ACL_EMAIL_TEMPLATEID").getStringValue();
			String time = EjbLookUps.getSettingsRemote().findById("OTP_VALID_TIME").getStringValue();

			
		/*	{'subject': 'Email Address Validation', 'from': {'email': 'noreply@cvlindia.in'},
				'reply_to': {'email': 'noreply@cvlindia.in'},
				'recipients': [{'to': [{'email': 'nithinmatrix82@gmail.com'}], 
					'attributes': {'{var1}': '004781', '{var2}': '30'}}],
				'template_id': 'Email_OTP'}
			*/
			

			ACLEmailRequestXsd aclEmailRequestXsd = new ACLEmailRequestXsd();
			
			From from = new From();
			from.setEmail(fromEmail);
			from.setName(fromEmailName);
			
		//	Headers headers = new Headers();
		//	headers.setXExample("");
			
			List<Recipient> lstRecipients = new ArrayList<Recipient>();
			Recipient recipient = new Recipient();
			
			Attributes attributes = new Attributes();
		//	attributes.setVar1(otp);
		//	attributes.setVar2(time);
			//attributes.setField3(null);
			recipient.setAttributes(attributes);
			
			List<To> lstTo = new ArrayList<To>();
			To to = new To();
			to.setEmail(toEmail);
		//	to.setName("Name");
			lstTo.add(to);
		
			recipient.setTo(lstTo);
			recipient.setUniqueArguments(null);
			lstRecipients.add(recipient);
			
			ReplyTo replyTo = new ReplyTo();
			replyTo.setEmail(fromEmail);
	//		replyTo.setName(fromEmailName);
			
			aclEmailRequestXsd.setFrom(from);
	//		aclEmailRequestXsd.setHeaders(headers);
			aclEmailRequestXsd.setRecipients(lstRecipients);
			aclEmailRequestXsd.setReplyTo(replyTo);
			aclEmailRequestXsd.setSubject("Email Address Validation");
			aclEmailRequestXsd.setTemplateId(templateId);	
			
			reqJson = new Gson().toJson(aclEmailRequestXsd);
//			System.out.println("Email Payload-----------"+reqJson);
			OkHttpClient client = new OkHttpClient().newBuilder()
					.build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,reqJson);

			Request request1 = new Request.Builder()
					.url(apiUrl)
					.method("POST", body)
					.addHeader("Authorization", "Bearer UVY2bjhLOW02VFl3dnhzanJMUVg4cG5XUTl3PQ")
					.addHeader("Content-Type", "application/json")
					.build();
//			System.out.println("request final --> "+request1);
			Response response1 = client.newCall(request1).execute();

			String jres = response1.body().string();
			System.out.println("Email ACL Response------"+jres);
			
			// create email transaction log
			if(jres.contains("\"request_id\": \"")) 
			{
//				emailPojo.setStatus("SUCCESS");
			}
			else {
//				emailPojo.setStatus("FAILED");
			}
			
			System.out.println("email transaction log created");
			
			
			return;
		}catch(Exception ex) {
			ex.printStackTrace();
		//	sendKarixEmail(toEmail, otp);
		}

	}
	
	public static int getRanNo(int min, int max) {
		Random random = new Random();
		int sNo= random.nextInt(max - min + 1) + min;
		return sNo;
	}
	
	
	
	
	
	

}