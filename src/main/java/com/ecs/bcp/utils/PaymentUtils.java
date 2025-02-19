package com.ecs.bcp.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.FetchIdPaymentpojo;
import com.ecs.bcp.pojo.FetchOrderPaymentPojo;
import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.rezorpay.response.FetchRazorpayResponse;
import com.ecs.rezorpay.response.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

public class PaymentUtils {
	
	public static EntityResponce getOrderPayments(EntityRequest req) {


		EntityResponce resp = new EntityResponce();

		System.out.println("Inside getOrderPayments method::");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		List<Payment> paymentList = null;

		Payment payment = null;

		if (isEmpty(req.getOrderId())) {
			System.out.println("Invalid Order ID !!" + req.getOrderId());
		} else {
			System.out.println("Fetch Payments for the order  ::" + req.getOrderId());
			try {
				PropertiesReader prop = new PropertiesReader();

				String razorpayKey = prop.getUrlProperty("RAZORPAY_KEY");
				String razorpaySecret = prop.getUrlProperty("RAZORPAY_SECRET");
				String bcpWebUrl         = prop.getUrlProperty("BCPWebURL");

				System.out.println("razorpayKey    ------------>>:: "+razorpayKey);
				System.out.println("razorpaySecret ------------>>:: "+razorpaySecret);

	//			RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

//				
//				paymentList = razorpayClient.orders.fetchPayments(req.getOrderId());
//
//				String fetchPayments = gson.toJson(paymentList);
//
//				System.out.println("fetch Payments Response-----------"+fetchPayments);
//				
//				

				String url = bcpWebUrl + "/RazorpayOrderFetch"; 
	            System.out.println("URL -----> " + url);

	            HttpResponse<String> paymentListResponse = Unirest.post(url)
	                    .header("razorpayKey", razorpayKey)
	                    .header("razorpaySecret", razorpaySecret)
	                    .header("orderId", req.getOrderId())
	                    .asString();

	            String resp1 = paymentListResponse.getBody();

				String fetchPayments = resp1;

				System.out.println("fetch Payments Response-----------"+fetchPayments);

				
				
				if (fetchPayments == null) {
					System.out.println(
							"No Payment information for an order  ::" + req.getOrderId());

					resp.setError(true);
					resp.setErrorMessage("No Payment information for an order  ::" + req.getOrderId());
					return resp;

				} else {
					if (fetchPayments.length() == 0) {
						System.out.println(
								"No Payment found for an order  :: " + req.getOrderId());


						resp.setError(true);
						resp.setErrorMessage("No Payment found for an order  :: " + req.getOrderId());
						return resp;

					} else if (fetchPayments.length() == 1) {
						System.out.println(
								"Single Payment for an order  :: " + req.getOrderId());
					} else if (fetchPayments.length() > 1) {
						System.out.println(
								"Multiple Payments found for an order  :: "
										+ req.getOrderId());
					}
					//        }
					//        }

					fetchPayments = fetchPayments.substring(1, fetchPayments.length() - 1);

					//   PaymentResponse res = new Gson().fromJson(fetchPayments, PaymentResponse.class);

					//	              res = res.getFetchRazorpayResponse().get(0);
					FetchRazorpayResponse res = new Gson().fromJson(fetchPayments, FetchRazorpayResponse.class);


					FetchOrderPaymentPojo pojo = new FetchOrderPaymentPojo();

					//              Map maps = res.getFetchRazorpayResponse().get(0).getModelJson().getMap();

					Map maps = res.getModelJson().getMap();

					if(maps != null) {

						pojo.setAmount(maps.getAmount());
						pojo.setAmountRefunded(maps.getAmountRefunded());

						String bankValue = new Gson().toJson(maps.getBank());

						System.out.println("bankValue -----------> "+bankValue);
						pojo.setBank(bankValue);
						pojo.setBankTransactionId(maps.getAcquirerData().getMap().getBankTransactionId());
						pojo.setCaptured(Boolean.toString(maps.getCaptured()));
						pojo.setCardId(new Gson().toJson(maps.getCardId()));
						pojo.setContact(maps.getContact());
						pojo.setCount(0);
						pojo.setCreatedAt(Date.from(Instant.ofEpochSecond(maps.getCreatedAt())));
						pojo.setDescription(maps.getDescription());
						pojo.setEmail(maps.getEmail());
						pojo.setErrorCode(new Gson().toJson(maps.getErrorCode()));
						pojo.setErrorDescription(new Gson().toJson(maps.getErrorDescription()));
						pojo.setErrorReason(new Gson().toJson(maps.getErrorReason()));
						pojo.setErrorSource(new Gson().toJson(maps.getErrorSource()));
						pojo.setErrorStep(new Gson().toJson(maps.getErrorStep()));
						pojo.setFee(maps.getFee());
						pojo.setPaymentId(maps.getId());
						pojo.setInvoiceId(new Gson().toJson(maps.getInvoiceId()));
						pojo.setMethod(maps.getMethod());
						pojo.setOrderId(maps.getOrderId());
						pojo.setRefundStatus(new Gson().toJson(maps.getRefundStatus()));
						pojo.setResponseLog(fetchPayments);
						pojo.setRrn(maps.getAcquirerData().getMap().getRrn());
						pojo.setStatus(maps.getStatus());
						pojo.setTax(maps.getTax());
						pojo.setVpa(maps.getVpa());
						pojo.setWallet(new Gson().toJson(maps.getWallet()));
						pojo.setUpiTransactionId(maps.getAcquirerData().getMap().getUpiTransactionId());
						pojo.setAuthCode(maps.getAcquirerData().getMap().getAuthCode());
						pojo.setCustomerId(maps.getCustomerId());
						pojo.setTokenId(maps.getTokenId());
						pojo.setPan(req.getPan());
						
						if (maps.getAcquirerData() != null 
						        && maps.getAcquirerData().getMap() != null 
						        && maps.getAcquirerData().getMap().getTransactionId() != null) {
						    pojo.setTransactionId(maps.getAcquirerData().getMap().getTransactionId().toString());
						} else {
						    pojo.setTransactionId(null);
						}
					//	pojo.setTransactionId(maps.getAcquirerData().getMap().getTransactionId().toString());
						pojo.setArn(maps.getAcquirerData().getMap().getArn());
						
						EjbLookUps.getFetchOrderPaymentRemote().create(pojo);

						resp.setError(false);
						resp.setState(maps.getStatus());
						resp.setOrderId(maps.getOrderId());
						resp.setPaymentId(maps.getId());
						
						PaymentReportDetailsPojo payRepPojo = new PaymentReportDetailsPojo();
			            
			            payRepPojo.setTxnId(req.getTxnId());
			            payRepPojo.setPaymentId(maps.getId());
			            String createdat = convertUnixToDate(maps.getCreatedAt());
			            payRepPojo.setPaymentDate(createdat);
			            payRepPojo.setPaymentMode(maps.getMethod());
			            payRepPojo.setPaymentStatus(maps.getStatus());

			            EjbLookUps.getPaymentReportDetailsRemote().update(payRepPojo);						

						if(req.getPaymentId() != null) {

//							
//							RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
//
//							payment = razorpayClient.payments.fetch(req.getPaymentId());
//
//							String fetchPaymentId = gson.toJson(payment);
//							
							

							String url1 = bcpWebUrl + "/RazorpayPayIDFetch"; 
				            System.out.println("URL -----> " + url1);

				            HttpResponse<String> paymentListResponse1 = Unirest.post(url1)
				                    .header("razorpayKey", razorpayKey)
				                    .header("razorpaySecret", razorpaySecret)
				                    .header("paymentId", req.getPaymentId())
				                    .asString();

				            String resp2 = paymentListResponse1.getBody();

							String fetchPaymentId = resp2;
							

							System.out.println("fetch Payment by Id  ----->>  "+ fetchPaymentId);

							FetchRazorpayResponse resPaymentId = new Gson().fromJson(fetchPaymentId, FetchRazorpayResponse.class);


							FetchIdPaymentpojo pojoId = new FetchIdPaymentpojo();


							Map maps1 = resPaymentId.getModelJson().getMap();

							if(maps1 != null) {

								pojoId.setAmount(maps1.getAmount());
								pojoId.setAmountRefunded(maps1.getAmountRefunded());
								String bankValue1 = new Gson().toJson(maps1.getBank());

								System.out.println("bankValue -----------> "+bankValue1);
								pojoId.setBank(bankValue1);
								pojoId.setBankTransactionId(maps1.getAcquirerData().getMap().getBankTransactionId());
								pojoId.setCaptured(Boolean.toString(maps1.getCaptured()));
								pojoId.setCardId(new Gson().toJson(maps1.getCardId()));
								pojoId.setContact(maps1.getContact());
								pojoId.setCreatedAt(Date.from(Instant.ofEpochSecond(maps1.getCreatedAt())));
								pojoId.setDescription(maps1.getDescription());
								pojoId.setEmail(maps1.getEmail());
								pojoId.setErrorCode(new Gson().toJson(maps1.getErrorCode()));
								pojoId.setErrorDescription(new Gson().toJson(maps1.getErrorDescription()));
								pojoId.setErrorReason(new Gson().toJson(maps1.getErrorReason()));
								pojoId.setErrorSource(new Gson().toJson(maps1.getErrorSource()));
								pojoId.setErrorStep(new Gson().toJson(maps1.getErrorStep()));
								pojoId.setFee(maps1.getFee());
								pojoId.setPaymentId(maps1.getId());
								pojoId.setInvoiceId(new Gson().toJson(maps1.getInvoiceId()));
								pojoId.setMethod(maps1.getMethod());
								pojoId.setOrderId(maps1.getOrderId());
								pojoId.setRefundStatus(new Gson().toJson(maps1.getRefundStatus()));
								pojoId.setResponseLog(fetchPaymentId);
								pojoId.setRrn(maps1.getAcquirerData().getMap().getRrn());
								pojoId.setStatus(maps1.getStatus());
								pojoId.setTax(maps1.getTax());
								pojoId.setVpa(maps1.getVpa());
								pojoId.setWallet(new Gson().toJson(maps1.getWallet()));
								pojoId.setUpiTransactionId(maps1.getAcquirerData().getMap().getUpiTransactionId());
								pojoId.setAuthCode(maps1.getAcquirerData().getMap().getAuthCode());
								pojoId.setCustomerId(maps1.getCustomerId());
								pojoId.setTokenId(maps1.getTokenId());
								pojoId.setPan(req.getPan());
								
								if (maps1.getAcquirerData() != null 
								        && maps1.getAcquirerData().getMap() != null 
								        && maps1.getAcquirerData().getMap().getTransactionId() != null) {
									pojoId.setTransactionId(maps1.getAcquirerData().getMap().getTransactionId().toString());
								} else {
									pojoId.setTransactionId(null);
								}
							//	pojoId.setTransactionId(maps1.getAcquirerData().getMap().getTransactionId().toString());
								pojoId.setArn(maps1.getAcquirerData().getMap().getArn());

								EjbLookUps.getFetchIdPaymentRemote().create(pojoId);

								System.out.println("Create in fetch Payment by Id  Table----->>  "+ req.getPaymentId());
								
								BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();
								DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								
								
								balancePojo.setTxnId(req.getTxnId());
								balancePojo.setPaymentId(maps1.getId());
								String created = convertUnixToDate(maps1.getCreatedAt());
								balancePojo.setPaymentDate(inputDateFormat.parse(created));
								balancePojo.setPaymentMode(maps1.getMethod());

								EjbLookUps.getBalanceConfirmRemote().update(balancePojo);
								
							}
						}

						return resp;



					}

					resp.setError(true);
					resp.setErrorDescription("No Payment found for an order ");
					resp.setOrderId(req.getOrderId());
					return resp;



				}

			} catch (Exception e) {
				System.out.println("Exception Occurred:" + e.getMessage());
				e.printStackTrace();
			}
		}

		resp.setError(true);
		resp.setErrorDescription("Invalid Order ID !!");

		return resp;
	}

	


	public static boolean isEmpty(String data) {
		if (data == null)
			return true;

		if (data.trim().length() == 0)
			return true;

		return false;
	}
	
	 public static String convertUnixToDate(Integer timestamp) {
		 
	        Date date = Date.from(Instant.ofEpochSecond(timestamp));
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH for 24-hour format
	        return dateFormat.format(date);
	    }
	
	public static void main(String[] args) {
		
		 long date = 1733375739;

	        // Convert UNIX timestamp to Date
	        Date conv = Date.from(Instant.ofEpochSecond(date));

	        // Format the Date to a specific pattern
	        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String formattedDate = inputDateFormat.format(conv);

	        // Output
	        System.out.println("Converted Date: " + conv);
	        System.out.println("Formatted Date: " + formattedDate);
	}
	
	
}
