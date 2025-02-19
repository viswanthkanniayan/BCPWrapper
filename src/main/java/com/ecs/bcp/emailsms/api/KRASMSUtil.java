package com.ecs.bcp.emailsms.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.ecs.bcp.acl.xsd.AclSmsRequestXsd;
import com.ecs.bcp.utils.EjbLookUps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KRASMSUtil {
	

	public static void sendACLSMS(String pan, String mobileNo, String otpValue) {
		
		
		System.out.println("KRASMSUtil: sendACLSMS: Start");
		try
		{
			String apiUrl = "";
			
			String enterpriseid = EjbLookUps.getSettingsRemote().findById("ACL_SMS_ENTERPRISE_ID").getStringValue();
			String subEnterpriseid = EjbLookUps.getSettingsRemote().findById("ACL_SMS_SUB_ENTERPRISE_ID").getStringValue();
			String pusheid = EjbLookUps.getSettingsRemote().findById("ACL_SMS_PUSHE_ID").getStringValue();
			String pushepwd = EjbLookUps.getSettingsRemote().findById("ACL_SMS_PUSHE_PWD").getStringValue();
			String sender = EjbLookUps.getSettingsRemote().findById("ACL_SMS_SENDER").getStringValue();
			String dpi = EjbLookUps.getSettingsRemote().findById("ACL_SMS_DPI").getStringValue();
			String dtm = EjbLookUps.getSettingsRemote().findById("ACL_SMS_DTM").getStringValue();
			String msgId = EjbLookUps.getSettingsRemote().findById("ACL_SMS_MSGID").getStringValue();

			String content = EjbLookUps.getSettingsRemote().findById("SMS_OTP_TEMPLATE").getStringValue();
			String time = EjbLookUps.getSettingsRemote().findById("OTP_VALID_TIME").getStringValue();

			content = content.replace("%OTP%", otpValue);
			content = content.replace("%TIME%", time);
			
			AclSmsRequestXsd aclSmsRequestXsd = new AclSmsRequestXsd();
			
			
			aclSmsRequestXsd.setEnterpriseid(subEnterpriseid);
			aclSmsRequestXsd.setSubEnterpriseid(subEnterpriseid);
			aclSmsRequestXsd.setPusheid(pusheid);
			aclSmsRequestXsd.setPushepwd(pushepwd);
			aclSmsRequestXsd.setContenttype("1");
			aclSmsRequestXsd.setSender(sender);
			aclSmsRequestXsd.setAlert("1");
			aclSmsRequestXsd.setMsisdn(mobileNo);
			aclSmsRequestXsd.setIntflag("false");
			aclSmsRequestXsd.setLanguage("en");
			aclSmsRequestXsd.setMsgtext(content);
			aclSmsRequestXsd.setDpi(dpi);
			aclSmsRequestXsd.setDtm(dtm);
			aclSmsRequestXsd.setMsgid(msgId);
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String reqJson = gson.toJson(aclSmsRequestXsd);
			
	//		System.out.println("reqJson--> "+reqJson);
			
			String requestUrl = reqJson;

			String response = postRequest(apiUrl, requestUrl);


		//	{"respid":"xxx", "accepted":true }
			
			// create sms transaction log

			if(response.contains("\"accepted\":true")) 
			{
//				smsPojo.setStatus("SUCCESS");
			}
			else {
//				smsPojo.setStatus("FAILED");
			}

			System.out.println("sms transaction log created");


			
		}catch(Exception ex) {
			ex.printStackTrace();
			// sendKarixSMS(mobileNo, otpValue);
		}
		System.out.println("KRASMSUtil: sendACLSMS: End");
	}
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static String postRequest(String url,String json) throws Exception {


	    OkHttpClient client = new OkHttpClient();
	   // client.setConnectTimeout(120, TimeUnit.SECONDS); // connect timeout
	   // client.setReadTimeout(120, TimeUnit.SECONDS);

//	    System.out.println("URL------------"+url);
	    RequestBody body = RequestBody.create(JSON, json);
	    //Request request = new Request.Builder().header("fip_api_key", "eyJhbGciOiJIUzI1NiJ9.eyJhcGlLZXlEZXNjIjoiQVBJIGtleSBmb3IgQ3VzdG9tZXJJZGVudGl0eU1pY3Jvc2VydmljZSAtIFdlYiIsImNvbnRleHQiOm51bGwsImFwaUtleUlkIjoiMDFEV0NBRFdEQjhNQUowRlFIQ1NNQUgwWFoiLCJleHAiOjE2NTM5NzgyNTgsInVzZXJJZCI6IkN1c3RvbWVySWRlbnRpdHlXZWIifQ.Yg19qPt4e2w_scX4I6DNZeaEEd9Ut16TnwFupiffLSc")
	    Request request = new Request.Builder()
	        .url(url)
	        .post(body)
	        .build();
	    Response response = client.newCall(request).execute();
	    String resp =  response.body().string();
	    System.out.println("resp-----------"+resp);
	    return resp;
	    
	  }
	
	
	public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
	public static String postXmlRequest(String url,String xml) throws Exception {

	    OkHttpClient client = new OkHttpClient();

//	    System.out.println("URL------------"+url);
	    RequestBody body = RequestBody.create(XML, xml);
	    Request request = new Request.Builder()
	        .url(url)
	        .post(body)
	        .build();
	    Response response = client.newCall(request).execute();
	    String resp =  response.body().string();
	    System.out.println("resp-----------"+resp);
	    return resp;
	    
	  }
	
	
	
	public static void sendUrl(String requestUrl) throws Exception{
	
			URL url = new URL(requestUrl);
			System.out.println(url);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("POST");
			System.out.println("before connecting **************");
			System.err.println("*********************" + uc.getResponseCode());
			System.out.println("my msg : " + uc.getResponseMessage());
			System.out.println("***********************after Connecting");
			uc.disconnect();
		
	}
	
	public static int getRanNo(int min, int max) {
		Random random = new Random();
		int sNo= random.nextInt(max - min + 1) + min;
		return sNo;
	}

}
