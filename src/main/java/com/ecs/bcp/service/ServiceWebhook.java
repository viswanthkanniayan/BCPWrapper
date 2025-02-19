package com.ecs.bcp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/ServiceWebhook/Settlement")
public class ServiceWebhook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceWebhook() {
		super();
		// TODO Auto-generated constructor stub
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


	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		HttpSession session = request.getSession(true);
		String signature = request.getHeader("X-Razorpay-Signature");
		System.out.println("Service Webhook Triggered :: Session ID : " + session.getId());
		response.setStatus(HttpServletResponse.SC_OK);
		
		try {
			
			
			System.out.println("Request Headers:");
		    Enumeration<String> headerNames = request.getHeaderNames(); 
		    while (headerNames.hasMoreElements()) {
		        String headerName = headerNames.nextElement();
		        String headerValue = request.getHeader(headerName); 
		        System.out.println(headerName + ": " + headerValue);
		    }
		    
		    

			StringBuilder jsonPayload = new StringBuilder();
			try (BufferedReader reader = request.getReader()) {
				String line;
				while ((line = reader.readLine()) != null) {
					jsonPayload.append(line);
				}
			}
			
			
			 System.out.println("Incoming Webhook Request: \n" + jsonPayload.toString());
			 
				/*
				 * boolean isValidSignature = isValidWebhook(signature, request); // currently
				 * setting it to true for testing. isValidSignature = true;
				 */
		
				
	/*		 JsonParser parser = new JsonParser(); // Deprecated in newer versions
			 JsonObject payloadJson = parser.parse(jsonPayload.toString()).getAsJsonObject();
			 
			 
	        // Extract details
	        String event = payloadJson.get("event").getAsString();
	        JsonObject settlementDetails = payloadJson.getAsJsonObject("payload")
	                                                 .getAsJsonObject("settlement")
	                                                 .getAsJsonObject("entity");

	        String settlementId = settlementDetails.get("id").getAsString();
	        String status = settlementDetails.get("status").getAsString();
	        int amount = settlementDetails.get("amount").getAsInt();
	        String utr = settlementDetails.get("utr").getAsString();
	        long createdAt = settlementDetails.get("created_at").getAsLong();

	        System.out.println("Webhook Event: " + event);
	        System.out.println("Settlement ID: " + settlementId);
	        System.out.println("Status: " + status);
	        System.out.println("Amount: " + amount);
	        System.out.println("UTR: " + utr);
	        System.out.println("Created At: " + createdAt);   */
			 
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().write("Webhook received");
		}catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid payload");
		}
	}

}
