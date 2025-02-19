package com.razorpay;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemClient extends ApiClient{

	ItemClient(String auth) {
		super(auth);
	}

	public Item create(JSONObject request) throws RazorpayException, JSONException {
	    return post(Constants.VERSION, Constants.ITEMS, request);
	}

	public Item fetch(String id) throws RazorpayException , JSONException{
	    return get(Constants.VERSION, String.format(Constants.ITEM, id), null);
	}

	public List<Item> fetchAll() throws RazorpayException, JSONException {
	    return fetchAll(null);
	}

	public Item edit(String id, JSONObject request) throws RazorpayException , JSONException{
		return patch(Constants.VERSION, String.format(Constants.ITEM, id), request);
	}

	public List<Item> fetchAll(JSONObject request) throws RazorpayException, JSONException {
	    return getCollection(Constants.VERSION, Constants.ITEMS, request);
	}

	public List<Item> delete(String id) throws RazorpayException, JSONException {
	   return delete(Constants.VERSION, String.format(Constants.ITEM, id), null);
	}
}