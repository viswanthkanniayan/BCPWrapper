package com.razorpay;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class DisputeClient extends ApiClient {

    DisputeClient(String auth) {
        super(auth);
    }

    public Dispute fetch(String id) throws RazorpayException, JSONException {
        return get(Constants.VERSION, String.format(Constants.DISPUTE_FETCH, id), null);
    }

    /**
     * It is wrapper of fetchAll with parameter here sending null defines fetchAll
     * with a default values without filteration
     * @throws RazorpayException
     * @throws JSONException 
     */
    public List<Dispute> fetchAll() throws RazorpayException, JSONException {
        return fetchAll(null);
    }

    /**
     * This method get list of disputes filtered by parameters @request
     * @throws RazorpayException
     * @throws JSONException 
     */
    public List<Dispute> fetchAll(JSONObject request) throws RazorpayException, JSONException {
        return getCollection(Constants.VERSION, Constants.DISPUTE, request);
    }

    public Dispute accept(String id) throws RazorpayException , JSONException{
        return post(Constants.VERSION, String.format(Constants.DISPUTE_ACCEPT, id), null);
    }

    public Dispute contest(String id, JSONObject request) throws RazorpayException , JSONException{
        return patch(Constants.VERSION, String.format(Constants.DISPUTE_CONTEST, id), request);
    }
}
