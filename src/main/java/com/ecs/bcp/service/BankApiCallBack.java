package com.ecs.bcp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.JSONObject;

import com.ecs.bcp.bank.api.canara.AESCanaraUtils;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.BankApiMasterPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor.CertData;
import com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor.CertificateExtractException;
import com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor.CertificateExtractorAPI;
import com.ecs.bcp.service.xsd.Body;
import com.ecs.bcp.service.xsd.CallBackResponse;
import com.ecs.bcp.service.xsd.EncryptData;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.FileUtils;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.PanValidationRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import okhttp3.Response;

@WebServlet("/API2/BCWebhook")
public class BankApiCallBack extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public BankApiCallBack() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("An error occurred while processing the request.");
		}
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Retrieve private key from settings

			HttpSession session = request.getSession(true);
			System.out.println("Service API2 BCWebhook Triggered :: Session ID : "+session);
			SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");

			Enumeration<String> headerNames = request.getHeaderNames();
	        if (headerNames != null) {
	            System.out.println("Request Headers:");
	            while (headerNames.hasMoreElements()) {
	                String headerName = headerNames.nextElement();
	                String headerValue = request.getHeader(headerName);
	                System.out.println(headerName + ": " + headerValue);
	            }
	        }
			
			
			// Get AccessToken header
			String accessToken = request.getHeader("AccessToken");
		
			
			
			if (accessToken == null || accessToken.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
				JsonObject jsonBody1 = new JsonObject();
				jsonBody1.addProperty("status", "FAILED");
				jsonBody1.addProperty("message", "AccessToken header is missing or invalid.");

				System.out.println("response json : "+new Gson().toJson(jsonBody1));
				response.getWriter().write(new Gson().toJson(jsonBody1));
				
				return;
			}

			// Log AccessToken
			System.out.println("AccessToken: " + accessToken);

			String jws = request.getHeader("JWS");
			System.out.println("callback jws: "+jws);
			
			// Read and parse the request body
			String requestBodyData = getBody(request);
			if (requestBodyData == null || requestBodyData.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
				JsonObject jsonBody1 = new JsonObject();
				jsonBody1.addProperty("status", "FAILED");
				jsonBody1.addProperty("message", "Request body is empty.");
				System.out.println("response json : "+new Gson().toJson(jsonBody1));
				response.getWriter().write(new Gson().toJson(jsonBody1));
				return;
			}

			//	System.out.println("Request Body: " + requestBodyData);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(requestBodyData);
			JsonNode encryptDataNode = rootNode.path("Request").path("body").path("encryptData");

			// Validate encryptData
			String encryptData1 = encryptDataNode.asText();
			if (encryptData1 == null || encryptData1.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
				JsonObject jsonBody1 = new JsonObject();
				jsonBody1.addProperty("status", "FAILED");
				jsonBody1.addProperty("message", "encryptData is missing in the JSON body.");

				System.out.println("response json : "+new Gson().toJson(jsonBody1));
				response.getWriter().write(new Gson().toJson(jsonBody1));
				return;
			}

			//		System.out.println("Encrypted Data: " + encryptData1);

			// Decrypt AccessToken and response
			PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey.getBinaryValue());
			String decryptedAESKeyBase64 = AESCanaraUtils.decryptRSA(accessToken, neslPrivateKey);
			System.out.println("Decrypted Call Back AES Key: " + decryptedAESKeyBase64);

			String decryptedResponse = AESCanaraUtils.decryptAESGCM(encryptData1, decryptedAESKeyBase64);
			System.out.println("Decrypted Call_Back Response: " + decryptedResponse);







			//17122024
			/*			  	EncryptData res = new Gson().fromJson(decryptedResponse, EncryptData.class);

			  if (res.getUniqueTxnId() == null || res.getUniqueTxnId().isEmpty()) {
			        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			        response.getWriter().write("Unique Transaction ID is missing or empty.");
			        return;
			    } 
				JsonObject jsonBody1 = new JsonObject();
				jsonBody1.addProperty("uniqueTxnId", res.getUniqueTxnId());
				jsonBody1.addProperty("status", "SUCCESS");
				jsonBody1.addProperty("message", "Transaction processed successfully" );

				JsonObject jsonPayload = new JsonObject();
				jsonPayload.add("encryptData", jsonBody1);


				Gson gson = new Gson();
				String jsonPayloadString = gson.toJson(jsonBody1);



				BankApiLogPojo  banklog =  new BankApiLogPojo();

				banklog.setAccountsCount("");
				banklog.setBankCharges("");
				banklog.setCin("");
				banklog.setErrorCode("");
				banklog.setLei("");
				banklog.setPan("");
				banklog.setUniqueTxnId(res.getUniqueTxnId());
				banklog.setPdfPath("");
				banklog.setBankApiRequest(decryptedResponse);
				banklog.setBankApiResponse(jsonPayloadString);

				EjbLookUps.getBankApiLogRemote().create(banklog);


				System.out.println("RequestPayload ------->>  "+jsonPayloadString);

				sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
				return;

			 */
			// 02122024			
			// Respond with success
			if (decryptedResponse != null) {


				EncryptData res = new Gson().fromJson(decryptedResponse, EncryptData.class);

				BankApiLogPojo  banklog =  new BankApiLogPojo();

				banklog.setUniqueTxnId(res.getUniqueTxnId());
				banklog.setBank(res.getBank());
				banklog.setBankApiRequest(decryptedResponse);
				banklog.setApiPurpose("CALL_BACK_REQUEST");
				EjbLookUps.getBankApiLogRemote().create(banklog);
				
				System.out.println("CALL_BACK_REQUEST      : SUCCESS");



				if (res.getUniqueTxnId() == null || res.getUniqueTxnId().isEmpty()) {

					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "ERR006");
					jsonBody1.addProperty("message", "Unique Transaction ID is missing or empty.");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);
					System.out.println("RequestPayload ------->>   "+jsonPayloadString);
					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );	
					return;
				}

				if (res.getBank() == null || res.getBank().isEmpty()) {

					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "ERR005");
					jsonBody1.addProperty("message", "Bank field is missing or empty.");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
					return;
				}

				if (res.getAuditeeDetails() != null) {
					System.out.println("AduiteePan: " + res.getAuditeeDetails().getAuditeePan());
				}

				if (res.getAuditeeDetails() == null || 
						res.getAuditeeDetails().getAuditeePan() == null || 
						res.getAuditeeDetails().getAuditeePan().isEmpty()) {

					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "ERR007");
					jsonBody1.addProperty("message", "The AuditeePan field is missing or empty.");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
					return;
				}   

				// Validate fields
				if (res == null || res.getBalanceCertificatePDF() == null || res.getBalanceCertificatePDF().isEmpty()) {
//					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

					response.getWriter().write("Document field is missing or empty.");

					System.out.println("Decrypted Response: " + decryptedResponse);

					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("uniqueTxnId", res.getUniqueTxnId());
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "ERR001");
					jsonBody1.addProperty("message", "Balance Certificate PDF field is missing or empty" );

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
					return;

				}

				String uniqueTxnId = res.getUniqueTxnId();

				List<BalanceConfirmPojo> balancePojos = EjbLookUps.getBalanceConfirmRemote().findByProperty("uniqueTxnId", uniqueTxnId);

				if (balancePojos == null) {
					System.out.println("balancePojos is null for uniqueTxnId: " + uniqueTxnId);
					// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "200");
					jsonBody1.addProperty("message", "UniqueTxnId not found");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
					return;
		
				}
				System.out.println("AuditeePan from response: " + res.getAuditeeDetails().getAuditeePan());
				System.out.println("getEntityReqPan BC: " + balancePojos.get(0).getAuditeePan());
				
				if (!res.getAuditeeDetails().getAuditeePan().equals(balancePojos.get(0).getAuditeePan())) {
					
					JsonObject jsonBody1 = new JsonObject();
					jsonBody1.addProperty("status", "FAILED");
					jsonBody1.addProperty("errorCode", "ERR002");
					jsonBody1.addProperty("message", "AuditeePan mismatch");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

					sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase() );
					
				    return;
				}
				
				System.out.println("UniqueTxnId from response: " + res.getUniqueTxnId());
				
				if (!res.getUniqueTxnId().equals(balancePojos.get(0).getUniqueTxnId())) {
				    JsonObject jsonBody1 = new JsonObject();
				    jsonBody1.addProperty("uniqueTxnId", res.getUniqueTxnId());
				    jsonBody1.addProperty("status", "FAILED");
				    jsonBody1.addProperty("errorCode", "ERR003");
				    jsonBody1.addProperty("message", "UniqueTxnId mismatch");

					Gson gson = new Gson();
					String jsonPayloadString = gson.toJson(jsonBody1);

					System.out.println("RequestPayload ------->>   "+jsonPayloadString);

				    sendEncryptedResponse(response, jsonPayloadString, res.getBank().toUpperCase());
				    
				    return;
				}
				
				// Extract fields
				String base64 = res.getBalanceCertificatePDF();
				String pan = res.getAuditeeDetails().getAuditeePan();
				
				//24/01/2025
		//		String uniqueTxnId = res.getUniqueTxnId();
				String bank = res.getBank();

				System.out.println("uniqueTxnId ----------------->   "+uniqueTxnId);


				if (base64 != null) {

					PropertiesReader pro = new PropertiesReader();

					String baseFolder = pro.getUrlProperty("baseFolder");
					String url = pro.getUrlProperty("DomainURL");


					SimpleDateFormat folderDate = new SimpleDateFormat("yyyy-MM-dd");
					String folderDateFormat = folderDate.format(new Date());

					String path = baseFolder + File.separator +"BalanceCertificate"+ File.separator +folderDateFormat;

					
					// check signature 
					byte[] decodedBytes = Base64.getDecoder().decode(base64);
					PDDocument pdfDoc = Loader.loadPDF(decodedBytes);
					if(pdfDoc.getSignatureDictionaries() == null || pdfDoc.getSignatureDictionaries().isEmpty())
					{
						System.out.println("Signature not found!");
						
						JsonObject jsonBody1 = new JsonObject();
						jsonBody1.addProperty("uniqueTxnId", uniqueTxnId);
						jsonBody1.addProperty("status", "FAILED");
						jsonBody1.addProperty("errorCode", "ERR004");
						jsonBody1.addProperty("message", "Signature not found" );

						Gson gson = new Gson();
						String jsonPayloadString = gson.toJson(jsonBody1);
						System.out.println("RequestPayload ------->>  "+jsonPayloadString);
						sendEncryptedResponse(response, jsonPayloadString, bank.toUpperCase() );
						return;
					}

					// store signature details in bank API log tables
					CertData cd =  CertificateExtractorAPI.extractCertificateData(decodedBytes,null );

					String certificateDetails = new Gson().toJson(cd, CertData.class);
					
					System.out.println("cer details : "+certificateDetails);
					
					

					File file1 =new File(path);
					if(!file1.exists())
					{
						file1.mkdirs();
					}

					String invoicePath = path+ File.separator +"BalanceCer_"+pan+uniqueTxnId+".pdf";


					String pdfPath1 = url + File.separator +"BalanceCertificate" + File.separator +folderDateFormat+ File.separator +"BalanceCer_"+pan+uniqueTxnId+".pdf";


					String filePath = FileUtils.saveBase64ToPdf(base64, invoicePath);

					if (filePath != null) {
						System.out.println("File successfully saved at: " + filePath);


						JsonObject jsonBody1 = new JsonObject();
						jsonBody1.addProperty("uniqueTxnId", uniqueTxnId);
						jsonBody1.addProperty("status", "SUCCESS");
						jsonBody1.addProperty("message", "Transaction processed successfully" );


						Gson gson = new Gson();
						String jsonPayloadString = gson.toJson(jsonBody1);

						System.out.println("RequestPayload ------->>  "+jsonPayloadString);

						sendEncryptedResponse(response, jsonPayloadString, bank.toUpperCase() );

						BankApiLogPojo  banklog1 =  new BankApiLogPojo();

						banklog1.setAccountsCount("");
						banklog1.setBankCharges("");
						banklog1.setCin("");
						banklog1.setErrorCode("");
						banklog1.setLei("");
						banklog1.setPan(pan);
						banklog1.setUniqueTxnId(uniqueTxnId);
						banklog1.setPdfPath(pdfPath1);
						banklog1.setApiPurpose("CALL_BACK_SUCCESS_RES");
						banklog1.setBankApiRequest(decryptedResponse);
						banklog1.setBankApiResponse(jsonPayloadString);
						banklog1.setBank(bank.toUpperCase());
						banklog1.setCerDetails(certificateDetails);

						EjbLookUps.getBankApiLogRemote().create(banklog1);
						
						
						List<BalanceConfirmPojo> balancePojo = EjbLookUps.getBalanceConfirmRemote().findByProperty("uniqueTxnId", uniqueTxnId);
						if(balancePojo != null) {
							
							
							BalanceConfirmPojo pojo = new BalanceConfirmPojo();
							pojo.setTxnId(balancePojo.get(0).getTxnId());
							pojo.setBankRespDate(new Date());
							pojo.setCertificatePath(pdfPath1);
							pojo.setStatus("ACTIVE");

							EjbLookUps.getBalanceConfirmRemote().update(pojo);
							
						}
						
						return;
					} else {
						System.out.println("Failed not save the file.");

						JsonObject jsonBody1 = new JsonObject();
						jsonBody1.addProperty("uniqueTxnId", uniqueTxnId);
						jsonBody1.addProperty("status", "FAILED");
						jsonBody1.addProperty("message", "Base64  not able to processed" );

						Gson gson = new Gson();
						String jsonPayloadString = gson.toJson(jsonBody1);

						System.out.println("RequestPayload ------->>  "+jsonPayloadString);

						sendEncryptedResponse(response, jsonPayloadString, bank.toUpperCase() );
						return;
					}

				}

			}      

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				
				JsonObject jsonBody1 = new JsonObject();
				jsonBody1.addProperty("status", "FAILED");
				jsonBody1.addProperty("message", "An error occurred during decryption.");

				response.getWriter().write(new Gson().toJson(jsonBody1));
				return;
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private String getBody(HttpServletRequest request) throws IOException {
		StringBuilder body = new StringBuilder();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				body.append(line);
			}
		}
		return body.toString();
	}

	public static boolean isEmpty(String data) {
		return data == null || data.trim().isEmpty();
	}

	private void sendEncryptedResponse(HttpServletResponse response, String jsonPayloadString, String bankName) throws Exception {

		String aesKey = AESCanaraUtils.generateAESKey();

		String encryptedPayload = AESCanaraUtils.encryptAESGCM(jsonPayloadString, aesKey);

		System.out.println("bank name  call back-------> "+bankName);
		
		BankApiMasterPojo bankPublicKeyPojo = EjbLookUps.getBankApiMasterRemote().findById(bankName);

		System.out.println("bank public certificate ---> : "+bankPublicKeyPojo.getBankPublicKey());
		PublicKey bankPublicKey = AESCanaraUtils.loadPublicKey(bankPublicKeyPojo.getBankPublicKey());

		System.out.println("bank public key : "+ Base64.getEncoder().encodeToString(bankPublicKey.getEncoded()));
		
		
		String accessToken = AESCanaraUtils.encryptRSA(aesKey, bankPublicKey);

		System.out.println("call back response access token : "+accessToken);
		
		SettingsPojo NESLPrivateKey = EjbLookUps.getSettingsRemote().findById("NESLPrivateKey");
		
		PrivateKey neslPrivateKey = AESCanaraUtils.loadPrivateKeyFromString(NESLPrivateKey.getBinaryValue());

	//-- preparing whole request for signature	
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(jsonPayloadString);
		JsonObject reqJson = jsonElement.getAsJsonObject();
//--
		JsonObject jsonPayload = new JsonObject();
		jsonPayload.add("encryptData", reqJson);
//---
		JsonObject jsonbody = new JsonObject();
		jsonbody.add("body", jsonPayload);
//--
		JsonObject jsonRequest = new JsonObject();
		jsonRequest.add("Request", jsonbody);
		
//		String signaturePayload = "{ \"Response\": { \"body\": { \"encryptData\":"+jsonPayloadString+" } } }";
		String signaturePayload = new Gson().toJson(jsonRequest);
		
		System.out.println("call back signaturepayload---> "+signaturePayload);
		String signature = AESCanaraUtils.sign(signaturePayload, neslPrivateKey);
		
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode bodyNode = objectMapper.createObjectNode();
		bodyNode.put("encryptData", encryptedPayload);
		
		System.out.println("encrypted payload : "+encryptedPayload);

		ObjectNode responseNode = objectMapper.createObjectNode();
		responseNode.set("body", bodyNode);

		ObjectNode rootResponseNode = objectMapper.createObjectNode();
		rootResponseNode.set("Response", responseNode);

		response.setHeader("AccessToken", accessToken);
		response.setHeader("JWS", signature);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		response.getWriter().write(objectMapper.writeValueAsString(rootResponseNode));
	}


}
