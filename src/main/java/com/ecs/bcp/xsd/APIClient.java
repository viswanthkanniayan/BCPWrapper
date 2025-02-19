package com.ecs.bcp.xsd;

import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class APIClient {

	public static final okhttp3.MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	//TEST
	public static final String WRAPPER_PAN_API_URL="https://devuat.offlinekyc.com/EcsServices/Pan";
	//SERVER
	//public static final String WRAPPER_PAN_API_URL="http://localhost:8080/EcsServices/Pan";



	public String postRequest(String url,String json) throws IOException, UnirestException {
/*
		OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
          Request request = new Request.Builder()
              .url(url)
              .post(body)
              .build();
          Response response = client.newCall(request).execute();
          return response.body().string();
          
          */
		
		HttpResponse<String> tokenResponse = Unirest.post(url)
				.body(json)
				.asString();

		String token =  tokenResponse.getBody();
		return token;
		
    }



	public static void main(String[] args) throws IOException {


		/*PANRequest panRequest= new PANRequest();
		panRequest.setConsent("Y");
		panRequest.setPan("BEEPG3635Q");
		*/

		APIClient apiClient= new APIClient();
		//String req=JsonUtils.generateVoterRequestJson("AUIPM3113G", "Y", "");
		String req = "{\r\n"
				+ "    \"subscriptionKey\": \"345n93874n-bn4k-c35hu5g-35fg-m3x9857ocny3o4c3\",\r\n"
				+ "    \"pan\": \"CCYPN9764N\"\r\n"
				+ "}";
		System.out.println("PAN Req-----------"+req);
//		String res=apiClient.postRequest(WRAPPER_PAN_API_URL,req);
//		System.out.println("PAN Res-----------"+res);
//
//
//		if(res.contains("\"hasError\":true")) {
//			System.out.println("Failure Response");
//		//	ECSPANFailureResponse resp = new Gson().fromJson(res, ECSPANFailureResponse.class);
//		}
//		else {
//			System.out.println("Success Response");
//		//	ECSPANSuccessResponse resp = new Gson().fromJson(res, ECSPANSuccessResponse.class);
//		}
		
	}

}



