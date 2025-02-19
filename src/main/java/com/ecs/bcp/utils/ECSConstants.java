
package com.ecs.bcp.utils;

/**
 * 
 * @author SGupta
 *
 */

public class ECSConstants {

	// private constructor to avoid instantiation
	private ECSConstants() {

	}

	// Constants for Payment Status
	public static final String STATUS_PAYMENT_CREATED = "created";
	public static final String STATUS_PAYMENT_AUTHORISED = "authorized";
	public static final String STATUS_PAYMENT_CAPTURED = "captured";
	public static final String STATUS_PAYMENT_REFUNDED = "refunded";
	public static final String STATUS_PAYMENT_FAILED = "failed";
	public static final String STATUS_ES_PAYMENT_PENDING = "PAYMENT_PENDING" ;
	// Constants for Service Type field in Payment Detail
	public static final String SOURCE_WEBHOOK = "webhook";
	public static final String SOURCE_PAYMENT_STATUS_API = "PAYMENT_STATUS_API";
	public static final String SOURCE_PAYMENT_SCHEDULER = "PAYMENT_SCHEDULER";
	public static final String SOURCE_PAYLOAD = "payload";

	//Constants for Customer Status
	public static final String STATUS_CUSTOMER_SUCCESS = "SUCCESS";
	public static final String STATUS_CUSTOMER_FAILED = "FAILED";
	public static final String STATUS_CUSTOMER_PENDING = "PENDING";

	// Constants for Order Status
	public static final String STATUS_ORDER_CREATED = "created";
	public static final String STATUS_ORDER_ATTEMPTED = "attempted";
	public static final String STATUS_ORDER_PAID = "paid";

	//Constants for ServiceResponse Status
	public static final String SERVICE_RESPONSE_SUCCESS = "SUCCESS";
	public static final String SERVICE_RESPONSE_FAILED = "FAILED";
	public static final String SERVICE_RESPONSE_INVALID = "INVALID";

	//Constants for ServiceResponseFormType used for Payment Flow
	public static final String PAYMENT_NEXT_ACTION_PAY = "PAY";
	public static final String PAYMENT_NEXT_ACTION_SUCCESS = "SUCCESS"; // proceed with given status.
	public static final String PAYMENT_NEXT_ACTION_WAIT = "WAIT";
	public static final String PAYMENT_NEXT_ACTION_INVALID = "INVALID";


}

