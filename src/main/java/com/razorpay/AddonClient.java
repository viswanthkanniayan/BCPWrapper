package com.razorpay;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class AddonClient extends ApiClient {

  AddonClient(String auth) {
    super(auth);
  }

  // To create an Addon, use the createAddon method of SubscriptionClient
  public Addon fetch(String id) throws RazorpayException, JSONException {
    return get(Constants.VERSION, String.format(Constants.ADDON_GET, id), null);
  }
  
  /**
   * It is wrapper of fetchAll with parameter here sending null defines fetchAll
   * with a default values without filteration
   * @throws RazorpayException
 * @throws JSONException 
   */
  public List<Addon> fetchAll() throws RazorpayException, JSONException {
      return fetchAll(null);
    }
  
  /**
   * This method get list of Addons filtered by parameters @request
   * @throws RazorpayException
 * @throws JSONException 
   */  
  public List<Addon> fetchAll(JSONObject request) throws RazorpayException, JSONException {
      return getCollection(Constants.VERSION, Constants.ADDON_LIST, request);
    }

  public List<Addon> delete(String id) throws RazorpayException, JSONException {
    return delete(Constants.VERSION, String.format(Constants.ADDON_DELETE, id), null);
  }
}