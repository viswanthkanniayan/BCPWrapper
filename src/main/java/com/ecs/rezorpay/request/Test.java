package com.ecs.rezorpay.request;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test {
	public static void main(String[] args) {
		
		Notes notes1 = new Notes();
		notes1.branch = "Acme Corp Bangalore North";
		notes1.name = "Gaurav Kumar";

		Notes notes2 = new Notes();
		notes2.branch = "Acme Corp Bangalore South";
		notes2.name = "Saurav Kumar";

		Transfer transfer1 = new Transfer();
		transfer1.account = "acc_id";
		transfer1.amount = 100;
		transfer1.currency = "INR";
		transfer1.notes = notes1;
		transfer1.linkedAccountNotes = Arrays.asList("branch");
		transfer1.onHold = true;
		transfer1.onHoldUntil = 1691222870;  

		Transfer transfer2 = new Transfer();
		transfer2.account = "acc_id";
		transfer2.amount = 100;
		transfer2.currency = "INR";
		transfer2.notes = notes2;
		transfer2.linkedAccountNotes = Arrays.asList("branch");
		transfer2.onHold = false;
		transfer2.onHoldUntil = 0;  

		OrderJsonRequest requestObject = new OrderJsonRequest();
		requestObject.amount = 200;
		requestObject.paymentCapture = 1;
		requestObject.currency = "INR";
		requestObject.transfers = Arrays.asList(transfer1, transfer2);

		//    requestObject.put("transfers", new JSONObject[]{transfer1, transfer2});

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonRequest = gson.toJson(requestObject);
		System.out.println("jsonRequest  ::  "+jsonRequest);
	
	}

}
