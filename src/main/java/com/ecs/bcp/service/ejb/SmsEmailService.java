package com.ecs.bcp.service.ejb;



import com.ecs.bcp.pojo.SmsEmailPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.pojo.UserDetailsPojoPk;
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

/**
 * @author VISWANTH KANNAIYAN
 */
public class SmsEmailService {



	public static EntityResponce verifyOTP(EntityRequest smsEmailRequestXsd) {

		EntityResponce resp = new EntityResponce();

		try {

			SmsEmailPojo smsDtlsList  = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

			UserDetailsPojo userPojo =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(smsEmailRequestXsd.getPan(), smsEmailRequestXsd.getUserType(),smsEmailRequestXsd.getEntityName() );
			if(smsDtlsList == null) {
				resp.setError(true);
				resp.setErrorCode(Constants.ECSV0008);
				resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return resp;
			}

			if(smsEmailRequestXsd.getType().equals("Email")){
				
				System.out.println("smsEmailRequestXsd.getEmailOtp() --------------------------->"+smsEmailRequestXsd.getEmailOtp());
				
				System.out.println("smsDtlsList.getEmailOtp() --------------------------->"+smsDtlsList.getEmailOtp());

				if(smsDtlsList.getEmailOtp().equals(smsEmailRequestXsd.getEmailOtp()))
				{
					
					smsDtlsList.setEmailOtpStatus("SUCCESS");
					EjbLookUps.getSmsEmailRemote().update(smsDtlsList);
					
					
					if(userPojo != null) {
						
						UserDetailsPojo pojo = new UserDetailsPojo();
						UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

						pkPojo.setPan(userPojo.getPkPojo().getPan());
						pkPojo.setUserType(userPojo.getPkPojo().getUserType());
						pkPojo.setEntityName(userPojo.getPkPojo().getEntityName());
						pojo.setPkPojo(pkPojo);

						pojo.setEmailOtpStatus("SUCCESS");
						EjbLookUps.getUserDetailsRemote().update(pojo);
					}
					
					resp.setError(false);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0012));
					return resp;
				}
				else {
					resp.setError(true);
					resp.setErrorCode(Constants.ECSV0007);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0007));
					return resp;
				}
			}else if(smsEmailRequestXsd.getType().equals("Mobile")){

				if(smsDtlsList.getMobileOtp().equals(smsEmailRequestXsd.getMobileOtp()))
				{
					smsDtlsList.setMobileOptStatus("SUCCESS");
					EjbLookUps.getSmsEmailRemote().update(smsDtlsList);


					if(userPojo != null) {

						UserDetailsPojo pojo = new UserDetailsPojo();
						UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

						pkPojo.setPan(userPojo.getPkPojo().getPan());
						pkPojo.setUserType(userPojo.getPkPojo().getUserType());
						pkPojo.setEntityName(userPojo.getPkPojo().getEntityName());
						pojo.setPkPojo(pkPojo);

						pojo.setMobileOptStatus("SUCCESS");
						EjbLookUps.getUserDetailsRemote().update(pojo);
					}

					resp.setError(false);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0012));
					return resp;
				}
				else {
					resp.setError(true);
					resp.setErrorCode(Constants.ECSV0007);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0007));
					return resp;
				}

			} else {

				resp.setError(true);
				resp.setErrorDescription("In-valid type");
				return resp;

			}


		} catch (Exception e) {
			e.printStackTrace();
			resp.setError(true);
			resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0010));
			return resp;
		}
	}


	
	public static EntityResponce verifyForgetEmailOTP(EntityRequest smsEmailRequestXsd) {

		EntityResponce resp = new EntityResponce();

		try {

			SmsEmailPojo smsDtlsList  = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());

			if(smsDtlsList == null) {
				resp.setError(true);
				resp.setErrorCode(Constants.ECSV0008);
				resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return resp;
			}

			System.out.println("email otp pojo --> "+smsDtlsList.getEmailOtp());
				if(smsDtlsList.getEmailOtp().equals(smsEmailRequestXsd.getEmailOtp()))
				{
					
					smsDtlsList.setEmailOtpStatus("SUCCESS");
					EjbLookUps.getSmsEmailRemote().update(smsDtlsList);
					
					resp.setError(false);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0012));
					return resp;
				}
				else {
					resp.setError(true);
					resp.setErrorCode(Constants.ECSV0007);
					resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0007));
					return resp;
				}


		} catch (Exception e) {
			e.printStackTrace();
			resp.setError(true);
			resp.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0010));
			return resp;
		}
	}


	public static String genRandNo(int digit){
		//String characters = "0123456789";
		String smsOtp = "123456";
		//String otp = RandomStringUtils.random(digit, smsOtp);
		return smsOtp;
	}
	
	
	public static EntityResponce genMobileOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();
		PropertiesReader pro = new PropertiesReader();
	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        System.out.println("pojo---->" + pojo);

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));

	            String content = smsOtp + " is OTP for login. Please do not share the OTP with anyone - PSB Alliance Private Limited";

		        
		       
	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
	                    "&sender=BCPSBA" +
	                    "&msgtext=" + content;

	            System.out.println("Request URL: " + requestUrl);

//	            OkHttpClient client = new OkHttpClient().newBuilder().build();
//	            Request request = new Request.Builder()
//	                    .url(requestUrl)
//	                    .get()
//	                    .build();
//	            
//	            Response response = client.newCall(request).execute();
//	            String responseStr = response.body().string();
//	            System.out.println("Response: " + responseStr);
//	            String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
	        
	            String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
	       		
	    		String Url =bcpWebUrl+"/BCPSmsSender";
	    		
	    		System.out.println("web url---> "+Url);
	    		
	    		System.out.println("Posting Data to Web Server------------------->>:: ");
	            
	        	HttpResponse<String> tokenResponse = Unirest.post(Url)
	    			    .body(requestUrl) // Include the JSON payload as the body
	    			    .asString();

	        	String jres = tokenResponse.getBody();
				
				System.out.println("SMS ACL Response------"+jres);
				
				
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

	public static EntityResponce genMobileAuditorAssignOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        System.out.println("pojo---->" + pojo);

	        // Generate and send OTP via API
	      //  String smsOtp = null;
	        String smsOtp = "123456";
	        try {
	            //Random random = new Random();
	        //    smsOtp = String.valueOf(100000 + random.nextInt(900000));

	          //  String content = smsOtp + " is OTP for login. Please do not share the OTP with anyone - PSB Alliance Private Limited";
	        	//String content = smsOtp + " is your OTP for Assigning Auditor on BCP. Please do not share the OTP with anyone - PSBA (BCP)";
	        	String content = "You are successfully registered on Balance Confirmation Portal - PSBA (BCP)";

		       
	            String requestUrl = "https://otp2.aclgateway.com/OTP_ACL_Web/OtpRequestListener?" +
	                    "enterpriseid=bcpsba" +
	                    "&subEnterpriseid=bcpsba" +
	                    "&pusheid=bcpsba" +
	                    "&pushepwd=bcp_05" +
	                    "&msisdn=" + smsEmailRequestXsd.getMobileNo() +
	                    "&sender=BCPSBA" +
	                    "&msgtext=" + content;

	            System.out.println("Request URL: " + requestUrl);

	            OkHttpClient client = new OkHttpClient().newBuilder().build();
	            Request request = new Request.Builder()
	                    .url(requestUrl)
	                    .get()
	                    .build();

	            Response response = client.newCall(request).execute();
	            String responseStr = response.body().string();
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
	
	public static EntityResponce genEmailOtp(EntityRequest smsEmailRequestXsd) throws Exception {

		EntityResponce smsEmailResponseXsd = new EntityResponce();

		SmsEmailPojo pojo = null;

		try {

			//	String smsOtp = genRandNo(6);
			String smsOtp = "123456";

			pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());


			System.out.println("pojo---->"+pojo);

			if(pojo == null) {


				SmsEmailPojo smsEmailPojo = new SmsEmailPojo();

				smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
				smsEmailPojo.setName(smsEmailRequestXsd.getName());
				smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
				smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setEmailOtpStatus("PENDING");
				smsEmailPojo.setEmailOtp(smsOtp);
				


				EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);

				System.out.println("OTP created!-----");


			}else {
				System.out.println("OTP updtae!-----");

				SmsEmailPojo pojo1 = new SmsEmailPojo();

				pojo1.setPan(smsEmailRequestXsd.getPan());
				pojo1.setName(smsEmailRequestXsd.getName());
				pojo1.setEmailId(smsEmailRequestXsd.getEmailId());
				pojo1.setMobileNo(smsEmailRequestXsd.getMobileNo());
				pojo1.setEmailOtp(smsOtp);
				pojo1.setMobileOptStatus(smsEmailRequestXsd.getMobileOptStatus());
				pojo1.setEmailOtpStatus(smsEmailRequestXsd.getEmailOtpStatus());

				EjbLookUps.getSmsEmailRemote().update(pojo1);

				System.out.println("updated");
			}


			// Add email API
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


	public static EntityResponce genSmsEmailOtp(EntityRequest smsEmailRequestXsd) throws Exception {
	    EntityResponce smsEmailResponseXsd = new EntityResponce();

	    try {
	        SmsEmailPojo pojo = EjbLookUps.getSmsEmailRemote().findById(smsEmailRequestXsd.getPan());
	        System.out.println("pojo---->" + pojo);
	        
	        
	       // String smsOtp = null;
	        String smsOtp = "123456"; 
	        
	        
	        
       // need to change otp -------------------------------------	        
	        
	     //  String smsOtp = String.valueOf(100000 + new Random().nextInt(900000));

	        	
	        	
	        String smsContent = smsOtp + " is OTP for login. Please do not share the OTP with anyone - PSB Alliance Private Limited";

	      //  String smsContent = smsOtp + "is your OTP  for Assigning Auditor on BCP. Please do not share the OTP with anyone- PSBA (BCP)";
	        
	       String smsUrl = "https://push3.aclgateway.com/servlet/com.aclwireless.pushconnectivity.listeners.TextListener?" +
	                "appid=bcpsbat&userId=bcpsbat&pass=bcp_01&contenttype=1&from=BCPSBA&to=" +
	                smsEmailRequestXsd.getMobileNo() + "&text=" + smsContent + "&alert=1&selfid=true";   

	        

        	
	        
	        System.out.println("SMS Request URL: " + smsUrl);
	        OkHttpClient client = new OkHttpClient().newBuilder().build();
	        Request smsRequest = new Request.Builder()
	                .url(smsUrl)
	                .get()
	                .build();

	        Response smsResponse = client.newCall(smsRequest).execute();
	        String smsResponseStr = smsResponse.body().string();
	        System.out.println("SMS Response: " + smsResponseStr);


	        if (pojo == null) {
	            SmsEmailPojo smsEmailPojo = new SmsEmailPojo();
	            smsEmailPojo.setPan(smsEmailRequestXsd.getPan());
	            smsEmailPojo.setName(smsEmailRequestXsd.getName());
	            smsEmailPojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            smsEmailPojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            smsEmailPojo.setMobileOtp(smsOtp); 
	            smsEmailPojo.setEmailOtp(smsOtp); 
	            smsEmailPojo.setEmailOtpStatus(smsEmailRequestXsd.getEmailOtpStatus());
	            smsEmailPojo.setMobileOptStatus(smsEmailRequestXsd.getMobileOptStatus());
	            

	            EjbLookUps.getSmsEmailRemote().create(smsEmailPojo);
	            System.out.println("OTP created!-----");
	        } else {
	            pojo.setName(smsEmailRequestXsd.getName());
	            pojo.setEmailId(smsEmailRequestXsd.getEmailId());
	            pojo.setMobileNo(smsEmailRequestXsd.getMobileNo());
	            pojo.setMobileOtp(smsOtp); 
	            pojo.setEmailOtp(smsOtp); 
	            pojo.setEmailOtpStatus(smsEmailRequestXsd.getEmailOtpStatus());
	            pojo.setMobileOptStatus(smsEmailRequestXsd.getMobileOptStatus());
	            
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
