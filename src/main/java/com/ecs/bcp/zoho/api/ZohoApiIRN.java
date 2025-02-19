package com.ecs.bcp.zoho.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZohoApiIRN {


	public static String genToken() {
		PropertiesReader pro = new PropertiesReader();
		try {

			String bcpWebUrl = pro.getUrlProperty("BCPWebURL");

			String Url =bcpWebUrl+"/ZohoGenToken";

			String zohoUrl = EjbLookUps.getSettingsRemote().findById("ZOHO_IRN_TOKEN_URL").getStringValue();
			
//			String zohoUrl = "https://accounts.zoho.in/oauth/v2/token?client_id=1000.HLYQGELJGV94XU3TU26QU0GJAOSYYL&client_secret=7edb9126a197f78f8b68314cadc1d2c09b5eb72d1b&redirect_uri=http://www.zoho.in/einvoice&grant_type=refresh_token&refresh_token=1000.0fcc94c129c9f9b19566c72ca99223cc.d95ce37ef4370b1893d9c053aab4dc7a";
			
			HttpResponse<String> tokenResponse = Unirest.post(Url)
					.body(zohoUrl)
					.asString();

			String resp =  tokenResponse.getBody();
			
			
//			Unirest.setTimeouts(0, 0);
//			HttpResponse<String> response = Unirest.post("https://accounts.zoho.in/oauth/v2/token?client_id=1000.HLYQGELJGV94XU3TU26QU0GJAOSYYL&client_secret=7edb9126a197f78f8b68314cadc1d2c09b5eb72d1b&redirect_uri=http://www.zoho.in/einvoice&grant_type=refresh_token&refresh_token=1000.0fcc94c129c9f9b19566c72ca99223cc.d95ce37ef4370b1893d9c053aab4dc7a")
//					.asString();
//
//			String resp = response.getBody().toString();
//
//			System.out.println("response: " + resp);

			ZohoResponse finalResp  = new Gson().fromJson(resp, ZohoResponse.class);

			String token = finalResp.getAccess_token();
			System.out.println("token -  >> "+token);

			return token;


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	public static String getBase64FromQRCodeLink(String qrLink) {
		String base64Image = null;
		HttpURLConnection connection = null;
		 PropertiesReader pro = new PropertiesReader();
		try {
			
			String bcpWebUrl = pro.getUrlProperty("BCPWebURL");

	    	String Url =bcpWebUrl+"/ZohoLinkToImage";
			
			System.out.println("Url-----> "+Url);
			
			HttpResponse<String> tokenResponse = Unirest.post(Url)
					.body(qrLink)
					.asString();

			base64Image =  tokenResponse.getBody();
			System.out.println("Posting Data to Web Server------zoho image------------->>:: ");

	    	
			
		/*	 URL url = new URL(qrLink);

		        // Open a connection
		        connection = (HttpURLConnection) url.openConnection();
		        connection.setRequestMethod("GET");
		        connection.setConnectTimeout(5000); // Set timeout (optional)
		        connection.setReadTimeout(5000);

		        // Check if the response code is 200 (OK)
		        int responseCode = connection.getResponseCode();
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		            // Read the image bytes
		            try (InputStream inputStream = connection.getInputStream();
		                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
		                byte[] buffer = new byte[1024];
		                int bytesRead;

		                while ((bytesRead = inputStream.read(buffer)) != -1) {
		                    byteArrayOutputStream.write(buffer, 0, bytesRead);
		                }

		                byte[] imageBytes = byteArrayOutputStream.toByteArray();

		                // Encode to Base64
		                base64Image = Base64.getEncoder().encodeToString(imageBytes);
		            }
		        } else {
		            System.out.println("Failed to fetch image. HTTP response code: " + responseCode);
		        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

*/
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return base64Image;
		
	}
	public static void main(String[] args) throws IOException, JSONException {

		// Create root JSON object
		JSONObject root = new JSONObject();

		// Create Contact object
		JSONObject contact = new JSONObject();
		contact.put("contact_name", "Nithin");   // M
		contact.put("company_name", "Nithin");	// M
		contact.put("currency_code", "INR");

		// Create Billing Address object
		JSONObject billingAddress = new JSONObject();  // M
		billingAddress.put("attention", "Mr.John");
		billingAddress.put("address", "4900 Hopyard Rd");
		billingAddress.put("street2", "Suite 310");
		billingAddress.put("state_code", "CG");
		billingAddress.put("city", "Raipur");
		billingAddress.put("state", "Maharashtra");
		billingAddress.put("zip", 400001);
		billingAddress.put("country", "India");
		billingAddress.put("phone", 8903738252L);
		billingAddress.put("fax", 98765432);

		// Add billing and shipping addresses
		contact.put("billing_address", billingAddress);

		//		contact.put("shipping_address", billingAddress);   // NOT mandate

		contact.put("gst_no", "27AAECC2628R1Z7");			// M
		contact.put("gst_treatment", "business_gst");		// M

		// Add Contact object to root
		root.put("contact", contact);

		// Create Invoice object
		JSONObject invoice = new JSONObject();
		invoice.put("place_of_supply", "KA");			// M
		invoice.put("invoice_number", "INV-Test-002");		// M

		// Dispatch Address
		//		JSONObject dispatchAddress = new JSONObject(billingAddress.toMap());  //NOT MANDATE
		//		invoice.put("dispatch_address", dispatchAddress);

		invoice.put("date", "2024-12-17");			// M
		//		invoice.put("discount", 0);
		//		invoice.put("is_discount_before_tax", true);
		//		invoice.put("discount_type", "item_level");
		//		invoice.put("shipping_charge", 0);

		// Line Items Array
		JSONArray lineItems = new JSONArray();			// M
		JSONObject lineItem = new JSONObject();
		lineItem.put("description", "Hard Drive");
		lineItem.put("rate", 1200);
		lineItem.put("quantity", 1);
		//		lineItem.put("unit", "kgs");
		lineItem.put("product_type", "goods");
		lineItem.put("hsn_or_sac", 39089010);
		lineItem.put("tax_name", "GST18");  // IGST18, GST18	
		lineItems.put(lineItem);

		invoice.put("line_items", lineItems);

		//		        invoice.put("adjustment", 0);
		//		        invoice.put("adjustment_description", " ");
		//		        invoice.put("notes", "Looking forward for your business.");
		//		        invoice.put("terms", "Terms & Conditions apply");
		//		        invoice.put("subject_content", "");
		invoice.put("seller_gstin", "29AAFCN2842P1ZV");				// M
		//		invoice.put("is_inclusive_tax", false);
		//		invoice.put("tax_rounding", "");
		invoice.put("shipping_charge_tax_name", "GST18");		
		invoice.put("shipping_charge_sac_code", 39089010);				// M
		//		        invoice.put("is_reverse_charge_applied", false);
		//		        invoice.put("is_customer_liable_for_tax", false);
		//		        invoice.put("is_export_with_payment", false);

		// Add Invoice object to root
		root.put("invoice", invoice);

		// Print the JSON string
		System.out.println("ZOHO: "+root); 

		String token = genToken();
		
		
		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, root.toString() );
		Request request = new Request.Builder()
				.url("https://einvoice.zoho.in/api/v3/einvoices/invoices")
				.method("POST", body)
				.addHeader("Authorization", "Zoho-oauthtoken "+token)
				.addHeader("X-com-zoho-invoice-organizationid", "60034984569")
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();

		String resp  = response.body().string();

		JSONObject responseJson = new JSONObject(resp);

		// Check the code to determine success or failure
		int code = responseJson.getInt("code");
		if (code == 0) { // Success case
			System.out.println("API call successful:");

			// Extract 'einvoice_details' if present
			if (responseJson.has("invoice")) {
				JSONObject invoiceRes = responseJson.getJSONObject("invoice");

				if (invoiceRes.has("einvoice_details")) {
					JSONObject einvoiceDetails = invoiceRes.getJSONObject("einvoice_details");

					// Print extracted einvoice_details
					System.out.println("E-Invoice Details:");
					System.out.println("Is Cancellable: " + einvoiceDetails.getBoolean("is_cancellable"));
					System.out.println("Invoice Reference Number: " + einvoiceDetails.getString("inv_ref_num"));
					System.out.println("Status: " + einvoiceDetails.getString("status"));
					System.out.println("Ack Number: " + einvoiceDetails.getString("ack_number"));
					String link = einvoiceDetails.getString("qr_link");
					System.out.println("QR Link: " + link);
					System.out.println("Ack Date: " + einvoiceDetails.getString("ack_date"));
					
					System.out.println("----------covert image--LINK TO Base64--------");
					
//					String base64 = getBase64FromQRCodeLink(link);
					
//					System.out.println("image base64 : "+base64);
					
				} else {
					System.out.println("E-Invoice Details not found in the response.");
				}
			} else {
				System.out.println("Invoice details not found in the response.");
			}
		} else { // Failure case
			System.out.println("API call failed with code: " + code);
			System.out.println("Message: " + responseJson.getString("message"));
		}
	}


	
	public static String getBase64FromQRCodeLink2(String qrLink) {
	    String base64Image = null;
	    HttpURLConnection connection = null;
	    PropertiesReader pro = new PropertiesReader();
	    try {
	    
	    	String bcpWebUrl = pro.getUrlProperty("BCPWebURL");

	    	String Url =bcpWebUrl+"/ZohoLinkToImage";
			
			System.out.println("Url-----> "+Url);
			
			HttpResponse<String> tokenResponse = Unirest.post(Url)
					.body(qrLink)
					.asString();

			base64Image =  tokenResponse.getBody();
			System.out.println("Posting Data to Web Server------zoho image------------->>:: ");

	    	
	  /*  	// Ensure the URL contains a protocol
	        if (!qrLink.startsWith("http://") && !qrLink.startsWith("https://")) {
	            qrLink = "https://" + qrLink; // Default to HTTPS if protocol is missing
	        }

	        // Create URL object
	        URL url = new URL(qrLink);

	        // Open a connection
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setConnectTimeout(5000); // Set timeout (optional)
	        connection.setReadTimeout(5000);

	        // Check if the response code is 200 (OK)
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            // Read the image bytes
	            try (InputStream inputStream = connection.getInputStream();
	                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	                byte[] buffer = new byte[1024];
	                int bytesRead;

	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    byteArrayOutputStream.write(buffer, 0, bytesRead);
	                }

	                byte[] imageBytes = byteArrayOutputStream.toByteArray();

	                // Encode to Base64
	                base64Image = Base64.getEncoder().encodeToString(imageBytes);
	            }
	        } else {
	            System.out.println("Failed to fetch image. HTTP response code: " + responseCode);
	        }
	        
	       */
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
//	        finally {
//	        if (connection != null) {
//	            connection.disconnect();
//	        }
//	    }
		return base64Image;

	  
	}

	
	public static void main2(String[] args) {
		
		String qr = "https://einvoice.zoho.in/einvoice/qrcode?eInvoiceID=2-a69c13cc8bc2adc2ee4f44c2cb8fad8240efeba1a0f815953b23118df4ffbfa732e33e4f42e69ca2fe8fab2a620ac06cb99d89dc70c1399a";
		
		String base64 = getBase64FromQRCodeLink(qr);
		
		System.out.println(base64);
		
	}

}
