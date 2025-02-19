package com.razorpay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FundAccountClient extends ApiClient{

	FundAccountClient(String auth) {
		super(auth);
	}

	public FundAccount create(JSONObject request) throws RazorpayException, JSONException {
	    return post(Constants.VERSION, Constants.FUND_ACCOUNT_CREATE, request);
	}

	public FundAccount fetch(String id) throws RazorpayException , JSONException{
	    return get(Constants.VERSION, String.format(Constants.FUND_ACCOUNT_FETCH, id), null);
	}

	public List<FundAccount> fetchAll() throws RazorpayException, JSONException {
		return fetchAll(null);
	}

	/**
	 * This method get list of fundaccounts filtered by parameters @request
	 * @throws RazorpayException
	 * @throws JSONException 
	 */
	public List<FundAccount> fetchAll(JSONObject request) throws RazorpayException, JSONException {
		return getCollection(Constants.VERSION, Constants.FUND_ACCOUNT_LIST, request);
	}
}