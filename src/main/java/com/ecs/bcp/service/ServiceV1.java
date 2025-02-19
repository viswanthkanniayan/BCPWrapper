package com.ecs.bcp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.io.InputStream;
import com.ecs.bcp.bank.api.canara.*;
import com.ecs.bcp.bank.api.ProformaInvoice;
import com.ecs.bcp.bank.api.TaxInvoice;
import com.ecs.bcp.pojo.AuditLogsPojo;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.FetchOrderPaymentPojo;
import com.ecs.bcp.pojo.PaymentDetailsPojo;
import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.report.excel.DailyBankReportExcel;
import com.ecs.bcp.report.excel.DailyLedgerReportExcel;
import com.ecs.bcp.report.excel.DailyReportExcel;
import com.ecs.bcp.service.ejb.AuditorAssignService;
import com.ecs.bcp.service.ejb.BalanceConfirmationService;
import com.ecs.bcp.service.ejb.BankLinkageService;
import com.ecs.bcp.service.ejb.EmailService;
import com.ecs.bcp.service.ejb.EntityDetailsService;
import com.ecs.bcp.service.ejb.SMS;
import com.ecs.bcp.service.ejb.SmsEmailService;
import com.ecs.bcp.service.ejb.SmsOtpService;
import com.ecs.bcp.service.ejb.StateMasterService;
import com.ecs.bcp.service.ejb.UserDetailsService;
import com.ecs.bcp.service.ejb.PdfCertDataSDK.src.com.ecs.certextractor.CertificateExtractorAPI;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PaymentUtils;
import com.ecs.bcp.utils.PfxToBase64;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.APIClient;
import com.ecs.bcp.xsd.CountResponseXsd;
import com.ecs.bcp.xsd.ECSPANFailureResponse;
import com.ecs.bcp.xsd.ECSPANSuccessResponse;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.bcp.xsd.PANResponse;
import com.ecs.bcp.xsd.PANValidationLogsPojo;
import com.ecs.bcp.xsd.PanValidationRequest;
import com.ecs.bcp.xsd.RequestBody;
import com.ecs.bcp.xsd.UserRequest;
import com.ecs.bcp.xsd.UserResponce;
import com.ecs.bcp.zoho.api.ZohoApiIRN;
import com.ecs.rezorpay.error.response.ErrorResponse;
import com.ecs.rezorpay.request.Notes;
import com.ecs.rezorpay.request.OrderJsonRequest;
import com.ecs.rezorpay.request.Transfer;
import com.ecs.rezorpay.response.CreateOrderResponse;
import com.ecs.rezorpay.response.FetchRazorpayResponse;
import com.ecs.rezorpay.response.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@WebServlet("/ServiceV1")


public class ServiceV1 extends HttpServlet{
	/**
	 * @author VISWANTH KANNAIYAN
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceV1() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			handleRequest(request,response);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		String requestBodyData = null;
		try {
			requestBodyData=getBody(request);

			RequestBody requestBody = new Gson().fromJson(requestBodyData, RequestBody.class);

			System.out.println("req-->"+requestBodyData);


			if(requestBody.getTxnType().equals("ENTITY_CREATE")) {
				System.out.println("ENTITY_CREATE---------------------");

				EntityRequest req = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+req.getTxnType());

				EntityResponce res = EntityDetailsService.createEntity(req);
				
				String txnStatus = "SUCCESS";
		        if (res.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(req.getPan(), "ENTITY_CREATE", txnStatus,
		        		req.getEmail() , req.getUserType(),	res.getErrorCode(), res.getErrorDescription());
		        
		        response.getWriter().write(new Gson().toJson(res));
			}
			else if(requestBody.getTxnType().equals("ENTITY_UPDATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.updateEntity(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "ENTITY_UPDATE", txnStatus, 
		        		requestXsd.getEmail() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("USER_CREATE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.createuser(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "USER_CREATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  
			else if(requestBody.getTxnType().equals("ADD_USER")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.adduser(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "ADD_USER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}
			else if(requestBody.getTxnType().equals("GET_EMAIL_MOBILE_BY_PAN")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.getEmailMobileByPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_EMAIL_MOBILE_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}
			else if(requestBody.getTxnType().equals("GET_USER_STATUS")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.getUserStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_USER_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}
			else if(requestBody.getTxnType().equals("USER_UPDATE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.updateuser(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "USER_UPDATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("USER_UNBLOCK")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.userUnblock(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "USER_UNBLOCK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("UPDATE_PASSWORD")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.updatePassword(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "UPDATE_PASSWORD", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  
			else if(requestBody.getTxnType().equals("ESIGN_UPDATE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.updateEsignStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "ESIGN_UPDATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("UPDATE_TXNID")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.updateTrnxId(requestXsd);


				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "UPDATE_TXNID", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}
			
			else if(requestBody.getTxnType().equals("USER_COUNT")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.userCount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "USER_COUNT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  


			else if(requestBody.getTxnType().equals("GENERATE_EMAILOTP")) {


				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);

				System.out.println("txn type----->"+requestXsd.getTxnType());


				EntityResponce responseXsd = SmsEmailService.genEmailOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GENERATE_EMAILOTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  

			else if(requestBody.getTxnType().equals("GENERATE_MOBILEOTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsEmailService.genMobileOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GENERATE_MOBILEOTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  
			else if(requestBody.getTxnType().equals("GEN_AUDITOR_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsEmailService.genMobileAuditorAssignOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GEN_AUDITOR_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  


			else if(requestBody.getTxnType().equals("GEN_SMS_EMAIL_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsEmailService.genSmsEmailOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GEN_SMS_EMAIL_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  

			else if(requestBody.getTxnType().equals("VERIFY_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsEmailService.verifyOTP(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "VERIFY_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			}  
			
			else if(requestBody.getTxnType().equals("SMS_SUCCESSFUL_REG")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendSuccessRegOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_SUCCESSFUL_REG", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			
			else if(requestBody.getTxnType().equals("LOGIN_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				EntityResponce resp  = SmsOtpService.sendloginOtp(requestXsd);
				
				String txnStatus = "SUCCESS";
		        saveAuditLogs(requestXsd.getPan(), "LOGIN_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUniqueTxnId(),
		        		"", "");
		        
		        

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}
			

			else if(requestBody.getTxnType().equals("SMS_AUDITOR_ASSIGN_OPT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendAuditorAssignMobileOpt(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_AUDITOR_ASSIGN_OPT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_USER_REG_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendUserRegOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_USER_REG_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_NEW_PASSWORD_OPT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				String responseXsd = SmsOtpService.sendUserPasswordSms(requestXsd.getMobileNo(), requestXsd.getPassword());

				response.getWriter().write(responseXsd);
			} 
			
			else if(requestBody.getTxnType().equals("SMS_FORGET_PASS_OPT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendForgetPassOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_FORGET_PASS_OPT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_BC_AUDITOR_ASSIGN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendbcAuditorAssignOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_BC_AUDITOR_ASSIGN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_BC_AUDITEE_ASSIGN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendbcAuditeeAssignOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_BC_AUDITEE_ASSIGN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_PAYMENT_VERF")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendbcPaymentVerfiy(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_PAYMENT_VERF", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_BANK_LINKAGE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendbankLinkageOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_BANK_LINKAGE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_ACCEPT_BANK_LINKAGE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendacceptbankLinkageOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_ACCEPT_BANK_LINKAGE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_REJECT_BANK_LINKAGE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendrejectbankLinkageOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_REJECT_BANK_LINKAGE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_DEACTIVE_BANK_LINKAGE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.senddeActivebankLinkageOtp(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_DEACTIVE_BANK_LINKAGE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("SMS_MODIFI_DETAILS_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsOtpService.sendmodifDetailsOTP(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SMS_MODIFI_DETAILS_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("VERIFY_FORGET_EMAIL_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = SmsEmailService.verifyForgetEmailOTP(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "VERIFY_FORGET_EMAIL_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
			} 
			else if(requestBody.getTxnType().equals("FORGET_PASS_EMAIL")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				EntityResponce responseXsd = EmailService.sendforgetPassEmail(requestXsd);

				response.getWriter().write(new Gson().toJson(responseXsd));
			} 

			else if (requestBody.getTxnType().equals("PAN_VERIFICATION")) {

				PanValidationRequest req = new Gson().fromJson(requestBodyData, PanValidationRequest.class);
				//System.out.println("Pan no-----------" + req.getPanNo());

				PANResponse panStatus1 = new PANResponse();


				APIClient apiClient= new APIClient();
				String panKey = "c50a8aaa-efcc-4e1b-ad4c-8969ea7473bc";

				// new pan changes
				String panReq = "{\r\n"
						+ "    \"subscriptionKey\": \""+panKey+"\",\r\n"  
						+ "    \"pan\": \""+req.getPanNo()+"\",\r\n"
						+"     \"name\": \""+req.getName()+"\",\r\n"
						+"      \"dob\": \""+req.getDob()+"\"\r\n"
						+ "}";


				// new pan URL
//				String url = "https://uat.offlinekyc.com/EcsServices/Pan4";

				PropertiesReader pro = new PropertiesReader();
				String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
				String Url =bcpWebUrl+"/BCPPanVerify";
				System.out.println("Url-----> "+Url);
				
//				String res=apiClient.postRequest(Url,panReq);
			
				HttpResponse<String> tokenResponse = Unirest.post(Url)
						.body(panReq)
						.asString();

				String res =  tokenResponse.getBody();
				
				System.out.println("pan respnose: "+res);

				PANValidationLogsPojo panPojo = new PANValidationLogsPojo();
				panPojo.setCreationDate(new Date());

				panPojo.setUserName(req.getLoggedInUser());
				panPojo.setPanNo(req.getPanNo());
				panPojo.setToken("NA");

				String txnStatus = "SUCCESS";
				
				if(res.contains("\"hasError\":true")) {
					ECSPANFailureResponse resp = new Gson().fromJson(res, ECSPANFailureResponse.class);

					panStatus1.setErr(true);
					panStatus1.setErrCode(resp.getErrorCode());
					panStatus1.setErrMsg(resp.getErrorMessage());
					
					 txnStatus = "FAILED";
				}else {

					ECSPANSuccessResponse resp = new Gson().fromJson(res, ECSPANSuccessResponse.class);
					panStatus1.setErr(false);
					panStatus1.setPan(resp.getData().getPan());
					panStatus1.setNameStatus(resp.getData().getNameStatus());
					panStatus1.setDobStatus(resp.getData().getDobStatus());
					panStatus1.setAadhaarSeedingStatus(resp.getData().getAadhaarSeedingStatus());
					panStatus1.setProvider("NSDL");
					panStatus1.setStatus(resp.getData().getStatus());

				}
				
		         
		        saveAuditLogs(req.getPan(), "VERIFY_FORGET_EMAIL_OTP", txnStatus, 
		        		req.getLoggedInUser() , "",
		        		"", "");
		        
		        
				response.getWriter().write(new Gson().toJson(panStatus1));
				return;


			}

			else if(requestBody.getTxnType().equals("CHECK_ENTITY_NAME")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.checkEntityName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CHECK_ENTITY_NAME", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}  
			else if(requestBody.getTxnType().equals("CHECK_ENTITY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.panCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CHECK_ENTITY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}  

			else if(requestBody.getTxnType().equals("AUDITEE_PAN_CHECK")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.auditeePanCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITEE_PAN_CHECK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}  
			else if(requestBody.getTxnType().equals("GET_ALL_ENTITY")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.findAllEntityRequester(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ALL_ENTITY", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("GET_ENTITY_DETAILS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getEntityDetails(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ALL_ENTITY", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("GET_ENTITY_BY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getEntityDetailsbyPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ENTITY_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("ENTITY_LOGIN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.entityLogin(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "ENTITY_LOGIN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("APPROVE_REJECT_ENTITY")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.approveRejectEntity(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "APPROVE_REJECT_ENTITY", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("FIND_BY_BANK_MASTER")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.findAllBankMaster(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "FIND_BY_BANK_MASTER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_ALL_ENTITY_BANK_LIST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getAllBankList(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ALL_ENTITY_BANK_LIST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}

			else if(requestBody.getTxnType().equals("GET_OTP_FLG_BY_BANK")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getBankOtpFlg(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_OTP_FLG_BY_BANK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}

			else if(requestBody.getTxnType().equals("AUDITEE_DETAILS_BY_NAME")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.auditeeDetailsByName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITEE_DETAILS_BY_NAME", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}

			
			//-------------------------Bank link-------------------------------------------------------------------------
			else if(requestBody.getTxnType().equals("BANK_LINKAGE_CREATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.linkagecreate(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "BANK_LINKAGE_CREATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			

			else if(requestBody.getTxnType().equals("GET_ALL_LINKAGE_DETAILS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getAlllinkageDetails(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ALL_LINKAGE_DETAILS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 


			else if(requestBody.getTxnType().equals("GET_DETAILS_BY_AUDITEE_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getDetailsByAuditeePan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_DETAILS_BY_AUDITEE_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 



			else if(requestBody.getTxnType().equals("GET_DETAILS_BY_REQUESTOR_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getDetailsByRequestorPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_DETAILS_BY_REQUESTOR_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 



			else if(requestBody.getTxnType().equals("GET_DETAILS_BY_BANK")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getDetailsByBankName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_DETAILS_BY_REQUESTOR_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 



			else if(requestBody.getTxnType().equals("GET_BANK_SUPPORT_DOC")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getSupportDocById(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_SUPPORT_DOC", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 



			else if(requestBody.getTxnType().equals("GET_BANK_DETAILS_BY_ID")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getDetailsById(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_DETAILS_BY_ID", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 


			else if(requestBody.getTxnType().equals("APPROVE_REJECT_BANK_LINKAGE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.approveRejectbanklinkage(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "APPROVE_REJECT_BANK_LINKAGE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_AUDITOR_NAME_BY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.findAuditorByPan(requestXsd);

				
				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITOR_NAME_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_AUDITEE_NAME_BY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.findAuditeeByPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITEE_NAME_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			/*
			 * else if(requestBody.getTxnType().equals("UPDATE_AUDITOR_ASSIGN")) {
			 * 
			 * EntityRequest requestXsd = new Gson().fromJson(requestBodyData,
			 * EntityRequest.class); EntityResponce responseXsd =
			 * BankLinkageService.updateAutitorDetails(requestXsd);
			 * 
			 * response.getWriter().write(new Gson().toJson(responseXsd)); return; }
			 */

			else if(requestBody.getTxnType().equals("GET_BANK_LIST_BY_AUDITEE_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getDetailsByAuditeePan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_LIST_BY_AUDITEE_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("GET_BANK_LIST_BY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getbankListByPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_LIST_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_BANK_LIST_BY_NAME")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getbankListByName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_LIST_BY_NAME", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("GET_BANK_STATUS_BY_PAN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.bankLinkageStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BANK_STATUS_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("INFO_PROVIDER_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.infoProviderStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "INFO_PROVIDER_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_BC_DETAILS_BY_LINKAGE_ID")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getBankDetailsByLinkageId(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BC_DETAILS_BY_LINKAGE_ID", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getEntityType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			//------------------------------USER SERVICE--------------------------------------------------

			else if(requestBody.getTxnType().equals("CHECK_USER_PAN_TYPE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.checkUserPanType(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CHECK_USER_PAN_TYPE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_ALL_USER_DATA")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.findAllUser(requestXsd);

				
				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ALL_USER_DATA", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("CHECK_FRN_MRN")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.checkMemberFirmRegNo(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CHECK_FRN_MRN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("CHECK_USER_PAN")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.userPanCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CHECK_USER_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("GET_USER_BY_PAN_TYPE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.getuserDetailsById(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_USER_BY_PAN_TYPE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_USER_BY_REG_NO")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.getAuditorByRegNo(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_USER_BY_REG_NO", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("USER_LOGIN_WITH_USERPAN")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.userLoginWithUserPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "USER_LOGIN_WITH_USERPAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("UPDATE_LOGOUT_TIME")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.updateLogoutTime(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "UPDATE_LOGOUT_TIME", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 
			else if(requestBody.getTxnType().equals("EMAIL_CHECK")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.emailCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "EMAIL_CHECK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("APPROVE_REJECT_USER")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = UserDetailsService.approveRejectUser(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "APPROVE_REJECT_USER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("FIND_ALL_USER_RECORDS")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());
 
				UserResponce responseXsd = UserDetailsService.findAllUser(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "FIND_ALL_USER_RECORDS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_DISTINCT_ENTITY_NAME")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				System.out.println("txn type----->"+requestXsd.getTxnType());

				UserResponce responseXsd = EntityDetailsService.distinctEntityName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_DISTINCT_ENTITY_NAME", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			} 

			else if(requestBody.getTxnType().equals("GET_AUDITOR_DTL_BY_ENTITY")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				UserResponce responseXsd = UserDetailsService.getAuditorByEntityName(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITOR_DTL_BY_ENTITY", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("DASHBORD_COUNT_FOR_AUDITEE")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				CountResponseXsd responseXsd = UserDetailsService.auditeeDashboardCounts(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "DASHBORD_COUNT_FOR_AUDITEE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			
			}

			else if(requestBody.getTxnType().equals("DASHBORD_COUNT_FOR_AUDITOR")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				CountResponseXsd responseXsd = UserDetailsService.auditorDashboardCounts(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "DASHBORD_COUNT_FOR_AUDITOR", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("DASHBORD_COUNT_FOR_TOTAL_USER")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				CountResponseXsd responseXsd = UserDetailsService.userDashboardCounts(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "DASHBORD_COUNT_FOR_TOTAL_USER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("GET_ENTITY_STATUS_COUNT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getEntityStatusCount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ENTITY_STATUS_COUNT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("GET_USER_TYPE_COUNT")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				UserResponce responseXsd = UserDetailsService.getUserTypeCount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_USER_TYPE_COUNT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("GET_ENTITY_TYPE_COUNT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = EntityDetailsService.getentityTypeCount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_ENTITY_TYPE_COUNT", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("GET_USER_STATUS_COUNT")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				UserResponce responseXsd = UserDetailsService.getUserStatusCount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "DASHBORD_COUNT_FOR_TOTAL_USER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
		
		
		else if (requestBody.getTxnType().equals("TOTAL_USER_COUNT")) {
		    
			UserResponce responseXsd = UserDetailsService.getUsertotalCount();
		    response.getWriter().write(new Gson().toJson(responseXsd));
		    return;
		}


		else if (requestBody.getTxnType().equals("TOTAL_ENTITY_COUNT")) {
		    
			EntityResponce responseXsd = EntityDetailsService.getEntitytotalCount();
		    response.getWriter().write(new Gson().toJson(responseXsd));
		    return;
		}

			else if(requestBody.getTxnType().equals("DASHBORD_COUNT_FOR_INFO_PROVIDER")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				CountResponseXsd responseXsd = UserDetailsService.infoDashboardCounts(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "DASHBORD_COUNT_FOR_INFO_PROVIDER", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}

			else if(requestBody.getTxnType().equals("GET_AUDITEE_DETAILS_BY_PAN")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				UserResponce responseXsd = UserDetailsService.getAuiteeDetailsByPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITEE_DETAILS_BY_PAN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}


			else if(requestBody.getTxnType().equals("UPDATE_STATUS")) {

				UserRequest requestXsd = new Gson().fromJson(requestBodyData, UserRequest.class);
				UserResponce responseXsd = UserDetailsService.updateStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "UPDATE_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			//----------------------------auditor assignment----------------------------------------------------------

			else if(requestBody.getTxnType().equals("CREATE_AUDITOR_ASSIGN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.auditorAssignCreateSP(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CREATE_AUDITOR_ASSIGN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("AUDITEE_NAME_AND_AUDITOR_PAN_CHECK")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.auditeeNameAndAuditorPanCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITEE_NAME_AND_AUDITOR_PAN_CHECK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("GET_AUDIT_ASSIGN_LIST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getAuditorAssignList(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDIT_ASSIGN_LIST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("GET_AUDITOR_PAN_CHECK")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.auditorPanCheck(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITOR_PAN_CHECK", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("REVOKE_AUDIOR_ASSIGN")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.RevokeAuditor(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "REVOKE_AUDIOR_ASSIGN", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("GET_AUDITOR_LIST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getAuditorList(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITOR_LIST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("AUDITOR_ASSIGN_BY_ID")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getauditorAssignById(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITOR_ASSIGN_BY_ID", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("AUDITEE_ASSIGN_LIST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getDetailsByAuditorPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITEE_ASSIGN_LIST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("AUDITEE_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.auditeeAssignStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITEE_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("AUDITOR_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.auditorAssignStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "AUDITOR_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("GET_AUDITEE_AUDITOR_DETAILS_BY_SRNO")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getDetailsBySrNo(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_AUDITEE_AUDITOR_DETAILS_BY_SRNO", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("Check_ICAI")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.checkICAI(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "Check_ICAI", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("GET_ALL_USER_MANUAL")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = AuditorAssignService.getUserManual(requestXsd);

				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
		//-----------------------------------stateMaster-----------------------------------------	
			
			
			
			else if(requestBody.getTxnType().equals("GET_STATE_CODE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = StateMasterService.getStateCode(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_STATE_CODE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}

			else if(requestBody.getTxnType().equals("GET_STATECODE_BY_STATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = StateMasterService.getStatecodeByState(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_STATECODE_BY_STATE	", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			
			
			//------- --------------balance confirmation------------------------------------

			else if(requestBody.getTxnType().equals("CREATE_BALANCE_CONFIRM_REG")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.createBalanceConfirmation(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "CREATE_BALANCE_CONFIRM_REG", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("LIST_AUDITOR_BC_REQUEST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditorbcRequestPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITOR_BC_REQUEST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("BC_PAYMENT_UPDATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.bcPaymentUpdate(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "BC_PAYMENT_UPDATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("LIST_AUDITEE_BC_REQUEST")) {
                
				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditeebcRequestPan(requestXsd);
                
				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITEE_BC_REQUEST", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
             
			else if(requestBody.getTxnType().equals("LIST_AUDITEE_PAYMENT_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditeePaymentStatus(requestXsd);
                
				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITEE_PAYMENT_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("LIST_AUDITOR_PAYMENT_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditorPaymentStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITOR_PAYMENT_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("LIST_AUDITOR_DOWNLOAD_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditorDownloadStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITOR_DOWNLOAD_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;
			}
			else if(requestBody.getTxnType().equals("LIST_AUDITEE_DOWNLOAD_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditeeDownloadStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITEE_DOWNLOAD_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("LIST_AUDITOR_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditorStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITOR_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("LIST_AUDITEE_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditeeStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_AUDITEE_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("LIST_BC_DATA_BY_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.getBcDataByStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "LIST_BC_DATA_BY_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("BC_DETAILS_BY_TXNID")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.getBcDetailsByTxnId(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "BC_DETAILS_BY_TXNID", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			else if(requestBody.getTxnType().equals("GET_BC_BANK_STATUS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankLinkageService.getBankStatus(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BC_BANK_STATUS", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			//----------------------------sign check---------------------------------------------------------------//


			else if(requestBody.getTxnType().equals("SIGN_CHECK_CREATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BalanceConfirmationService.auditeebcRequestPan(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "SIGN_CHECK_CREATE", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUserType(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
		//-----------------------------------bank count api--------------------	
			
			else if(requestBody.getTxnType().equals("GET_BALANCE_CERTIFICATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = CanaraBankApi.getBalanceCertificate(requestXsd);

				
				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_BALANCE_CERTIFICATE", txnStatus, 
		        		requestXsd.getBankname() , requestXsd.getUniqueTxnId(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}

			else if(requestBody.getTxnType().equals("GET_NUM_OF_ACCOUNT")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = CanaraBankApi.getCanaraNumberOfAccount(requestXsd);

				String txnStatus = "SUCCESS";
		        if (responseXsd.isError())
		          txnStatus = "FAILED";
		        saveAuditLogs(requestXsd.getPan(), "GET_NUM_OF_ACCOUNT", txnStatus, 
		        		requestXsd.getBankname() , requestXsd.getUniqueTxnId(),
		        		responseXsd.getErrorCode(), responseXsd.getErrorDescription());
		        
		        
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("BANK_API_TEST")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = CanaraBankApi.getNumberOfAccount(requestXsd);

				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			
			
			else if(requestBody.getTxnType().equals("TEST_SMS")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				String resp  = SMS.sendSms(requestXsd);

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}

			else if(requestBody.getTxnType().equals("TEST_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				String resp  = SMS.sendOtp(requestXsd);

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}
			
			else if(requestBody.getTxnType().equals("SEND_ENITITY_EMAIL_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				String resp  = SMS.sendEntityEmailOtp(requestXsd);
				
				String txnStatus = "SUCCESS";
		        saveAuditLogs(requestXsd.getPan(), "SEND_ENITITY_EMAIL_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUniqueTxnId(),
		        		"", "");
		        
		        

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}
		
			else if(requestBody.getTxnType().equals("SEND_USER_EMAIL_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				String resp  = SMS.sendUserEmailOtp(requestXsd);
				
				String txnStatus = "SUCCESS";
		        saveAuditLogs(requestXsd.getPan(), "SEND_USER_EMAIL_OTP", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUniqueTxnId(),
		        		"", "");
		        
		        

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}
			
			else if(requestBody.getTxnType().equals("SEND_SUCC_REG_EMAIL")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				
				String resp  = SMS.sendSuccessRegMail(requestXsd);
				
				String txnStatus = "SUCCESS";
		        saveAuditLogs(requestXsd.getPan(), "SEND_SUCC_REG_EMAIL", txnStatus, 
		        		requestXsd.getEmailId() , requestXsd.getUniqueTxnId(),
		        		"", "");
		        
		        

				response.getWriter().write(new Gson().toJson(resp));
				return;

			}
			
			
			
		//-------------------------------invoice pdf-------------------------------	
			else if(requestBody.getTxnType().equals("GEN_PROFORMA_INVOICE_PDF")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = ProformaInvoice.genProformaInvoice(requestXsd);
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("GEN_TAX_INVOICE_PDF")) {
      
				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = TaxInvoice.genTaxInvoice(requestXsd);
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			

			else if(requestBody.getTxnType().equals("QR_LINK_TO_BASE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				String responseXsd = ZohoApiIRN.getBase64FromQRCodeLink2(requestXsd.getQrLink());
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("BANK_GEN_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankOtpAPI.BankGenerateOtp(requestXsd);
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("BANK_VALIDATE_OTP")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = BankOtpAPI.BankValidateOtp(requestXsd);
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			
			
			
			else if(requestBody.getTxnType().equals("EXTRACT_DATA_FROM_CERTIFICATE")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
//				EntityResponce responseXsd = CertificateExtractorAPI.extractDataFromPdf(requestXsd);
				
				EntityResponce responseXsd = null;
				
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			
			else if(requestBody.getTxnType().equals("FETCH_ORDER")) {

				EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
				EntityResponce responseXsd = PaymentUtils.getOrderPayments(requestXsd);
				response.getWriter().write(new Gson().toJson(responseXsd));
				return;

			}
			
			else if(requestBody.getTxnType().equals("GET_SUMMARY_REPORT")) {

		        EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
		        EntityResponce responseXsd = BalanceConfirmationService.getAuditeeSummary(requestXsd);
		        response.getWriter().write(new Gson().toJson(responseXsd));
		        return;

		      }

			else if(requestBody.getTxnType().equals("GET_DAILY_REPORT")) {

		        EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
		        EntityResponce responseXsd = DailyReportExcel.agentReportExcel(requestXsd);
		        response.getWriter().write(new Gson().toJson(responseXsd));
		        return;

		      }
			

			else if(requestBody.getTxnType().equals("GET_BANK_DAILY_REPORT")) {

		        EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
		        EntityResponce responseXsd = DailyBankReportExcel.dailyBankReportExcel(requestXsd);
		        response.getWriter().write(new Gson().toJson(responseXsd));
		        System.out.println(new Gson().toJson(responseXsd));
		        return;

		      }
			else if(requestBody.getTxnType().equals("GET_DAILY_LEDGER_REPORT")) {

	            EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
	            EntityResponce responseXsd = DailyLedgerReportExcel.dailyLedgerReportExcel(requestXsd);
	            response.getWriter().write(new Gson().toJson(responseXsd));
	            return;

	          }
			
			// test method - pfx to base64
			else if(requestBody.getTxnType().equals("DEMO_PFX_TO_BASE64")) {

		        EntityRequest requestXsd = new Gson().fromJson(requestBodyData, EntityRequest.class);
		        String responseXsd = PfxToBase64.getCer(requestXsd);
		        response.getWriter().write(responseXsd);
		        return;

		      }
			
			
			//-------------------------razorpay------------------------------------------------------------------------//
			else if(requestBody.getTxnType().equals("paymentRequest")) {
				EntityResponce res = new EntityResponce();
				EntityRequest req = new Gson().fromJson(requestBodyData, EntityRequest.class);
				PaymentDetailsPojo paymentDetailsPojo = new PaymentDetailsPojo();
				PropertiesReader prop = new PropertiesReader();
				CreateOrderResponse ordresp = null;
				DecimalFormat df = new DecimalFormat("0.00");
				BalanceConfirmPojo BCPojo = new BalanceConfirmPojo();
				List<Payment> paymentList = null;
				Payment payment = null;
				Gson gson1 = new GsonBuilder().setPrettyPrinting().create();

				try {

					String razorpayKey       = prop.getUrlProperty("RAZORPAY_KEY");
					String razorpaySecret    = prop.getUrlProperty("RAZORPAY_SECRET");
					String bcpWebUrl         = prop.getUrlProperty("BCPWebURL");

					if((req.getCountOfAcc() == null) || (req.getCountOfAcc().equalsIgnoreCase("0"))) {

						res.setErrorCode("FAILED");
						res.setError(true);
						res.setErrorMessage("Count of account is empty/zero");
						String finalResp = new Gson().toJson(res);
						System.out.println("Failed Response:::\n\t"+finalResp);
						response.getWriter().write(new Gson().toJson(res));
						return;

					}

		/*			if((req.getBankCrg() == null) || (req.getBankCrg().equalsIgnoreCase("0"))) {

						res.setErrorCode("FAILED");
						res.setError(true);
						res.setErrorMessage("Bank charge is empty/zero");
						String finalResp = new Gson().toJson(res);
						System.out.println("Failed Response:::\n\t"+finalResp);
						response.getWriter().write(new Gson().toJson(res));
						return;
					}   */
					
					if((req.getBankCrg() == null)) {

						res.setErrorCode("FAILED");
						res.setError(true);
						res.setErrorMessage("Bank charge is empty");
						String finalResp = new Gson().toJson(res);
						System.out.println("Failed Response:::\n\t"+finalResp);
						response.getWriter().write(new Gson().toJson(res));
						return;
					}   


					EntityResponce revenue = TaxInvoice.RevenueCalculator(req.getCountOfAcc(), req.getBankCrg(),req.getBank());


//					int PSBA_BCP_Revenue_Share = 100;
//					int NeSL_BCP_Revenue_Share = 100;
//					int BANK_BCP_Revenue_Share =  100;
//					int PSBA_TDS_Collection = 100;
//					
					int PSBA_BCP_Revenue_Share = (int) (revenue.getPSBABCPRevenueShare() * 100);
					int NeSL_BCP_Revenue_Share = (int) (revenue.getNeSLBCPRevenue_Share() * 100);
					int BANK_BCP_Revenue_Share = (int) (revenue.getBANKBCPRevenue_Share() * 100);
					int PSBA_TDS_Collection = (int) (revenue.getPSBATDSCollection() * 100);

					System.out.println("TOTAL ------------------------------>>>>"+revenue.getPSBATDSCollection());
				//	int Total = 400;
					int Total = (int) (revenue.getTotal() * 100);



					System.out.println("PSBA_BCP_Revenue_Share      =>      "+PSBA_BCP_Revenue_Share);
					System.out.println("NeSL_BCP_Revenue_Share      =>      "+NeSL_BCP_Revenue_Share);
					System.out.println("BANK_BCP_Revenue_Share      =>      "+BANK_BCP_Revenue_Share);
					System.out.println("PSBA_TDS_Collection         =>      "+PSBA_TDS_Collection);
					System.out.println("Total                        =>     "+Total);


					//	System.out.println("razorpayKey------------->>:: "+razorpayKey);
					//    System.out.println("razorpaySecret------------->>:: "+razorpaySecret);

//					RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);


					BCPojo = EjbLookUps.getBalanceConfirmRemote().findById(req.getTxnId());


					if(BCPojo != null) {

						if((BCPojo.getOrderId() != null) && (!BCPojo.getOrderId().isEmpty())) {


						//	paymentList = razorpay.orders.fetchPayments(BCPojo.getOrderId());
							

							String url = bcpWebUrl + "/RazorpayOrderFetch"; 
				            System.out.println("URL -----> " + url);

				            HttpResponse<String> paymentListResponse = Unirest.post(url)
				                    .header("razorpayKey", razorpayKey)
				                    .header("razorpaySecret", razorpaySecret)
				                    .header("orderId", BCPojo.getOrderId())
				                    .asString();

				            String resp = paymentListResponse.getBody();

							String fetchPayments = resp;

							System.out.println("fetch Payments Response-----------"+fetchPayments);


							if (fetchPayments == null  || fetchPayments.isEmpty() || fetchPayments.equals("[]") ) {
								System.out.println(
										"No Payment information for an order  ::" + BCPojo.getOrderId());
								res.setError(false);
								res.setOrderId(BCPojo.getOrderId());
								res.setErrorDescription("There is no payment information for the order, as the Order ID has already created in the table.");
								response.getWriter().write(new Gson().toJson(res));
								return;

							} else {
								if (resp.length() == 0) {
									System.out.println(
											"No Payment found for an order  :: " + req.getOrderId());

								} else if (resp.length() == 1) {
									System.out.println(
											"Single Payment for an order  :: " + req.getOrderId());
								} else if (resp.length() > 1) {
									System.out.println(
											"Multiple Payments found for an order  :: "
													+ req.getOrderId());
								}

								fetchPayments = fetchPayments.substring(1, fetchPayments.length() - 1);

								FetchRazorpayResponse res1 = new Gson().fromJson(fetchPayments, FetchRazorpayResponse.class);
								
								

								FetchOrderPaymentPojo pojo = new FetchOrderPaymentPojo();

								Map maps = res1.getModelJson().getMap();

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
									pojo.setArn(maps.getAcquirerData().getMap().getArn());

									EjbLookUps.getFetchOrderPaymentRemote().create(pojo);


									PaymentReportDetailsPojo payRepPojo = new PaymentReportDetailsPojo();

									payRepPojo.setTxnId(req.getTxnId());
									payRepPojo.setPaymentId(maps.getId());
									String createdat = PaymentUtils.convertUnixToDate(maps.getCreatedAt());
									System.out.println("payment date : "+createdat);
									payRepPojo.setPaymentDate(createdat);
									payRepPojo.setPaymentMode(maps.getMethod());
									payRepPojo.setPaymentStatus(maps.getStatus());

									EjbLookUps.getPaymentReportDetailsRemote().update(payRepPojo);	


									res.setError(true);
									res.setState(maps.getStatus());
									res.setOrderId(maps.getOrderId());
									res.setPaymentId(maps.getId());
									res.setAmount( Integer.toString(maps.getAmount()));
									res.setErrorDescription("The Payment has already made through Razorpay for this order account :: "+maps.getOrderId());
									response.getWriter().write(new Gson().toJson(res));
									return;

								}

							}
						}

					}

					System.out.println("New Order creating.........");
					
					Notes notes1 = new Notes();
					notes1.branch = "NeSL DBCP Revenue Account";
					notes1.name = "Gaurav Kumar";


					Transfer transfer1 = new Transfer();
					transfer1.account = "acc_P64Q5AfkTV7hz2";
					transfer1.amount = NeSL_BCP_Revenue_Share;
					transfer1.currency = "INR";
					transfer1.notes = notes1;
					transfer1.linkedAccountNotes = Arrays.asList("branch");
					transfer1.onHold = true;
					//  transfer1.onHoldUntil = Instant.now().getEpochSecond() + 86400;  
					transfer1.onHoldUntil = null; 


					Notes notes2 = new Notes();
					notes2.branch = "Bank1 DBCP Revenue Account";
					notes2.name = "Saurav Kumar";

					Transfer transfer2 = new Transfer();
					transfer2.account = "acc_P611HgTgHBEqa0";
					transfer2.amount = BANK_BCP_Revenue_Share;
					transfer2.currency = "INR";
					transfer2.notes = notes2;
					transfer2.linkedAccountNotes = Arrays.asList("branch");
					transfer2.onHold = false;
					transfer2.onHoldUntil = null;  

					Notes notes3 = new Notes();
					notes3.branch = "PSBA DBCP Revenue Account";
					notes3.name = "Ravi Kumar";

					Transfer transfer3 = new Transfer();
					transfer3.account = "acc_P611BrmKIz1GJA";
					transfer3.amount = PSBA_BCP_Revenue_Share;
					transfer3.currency = "INR";
					transfer3.notes = notes3;
					transfer3.linkedAccountNotes = Arrays.asList("branch");
					transfer3.onHold = false;
					transfer3.onHoldUntil = null; 

					Notes notes4 = new Notes();
					notes4.branch = "PSBA TDS Account";
					notes4.name = "viswanth k";

					Transfer transfer4 = new Transfer();
					transfer4 .account = "acc_PC4ANa66Y6WgYc";
					transfer4 .amount = PSBA_TDS_Collection;
					transfer4 .currency = "INR";
					transfer4 .notes = notes4;
					transfer4 .linkedAccountNotes = Arrays.asList("branch");
					transfer4 .onHold = false;
					transfer4 .onHoldUntil = null;  


					OrderJsonRequest requestObject = new OrderJsonRequest();
					requestObject.amount = Total;
					System.out.println("total------111-"+Total);
//					requestObject.amount = 100;
					//   requestObject.paymentCapture = 1;
					requestObject.currency = "INR";
					requestObject.transfers = Arrays.asList(transfer1, transfer2,transfer3,transfer4 );


					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					String jsonRequest = gson.toJson(requestObject);
					System.out.println("jsonRequest  :: \n "+jsonRequest.toString());
					JSONObject jsonReq = new JSONObject(jsonRequest);

//					Order order = razorpay.orders.create(jsonReq);
					
					
					
					String url = bcpWebUrl + "/RazorpayOrder";   
		            System.out.println("URL -----> " + url);

		            HttpResponse<String> paymentListResponse = Unirest.post(url)
		                    .header("razorpayKey", razorpayKey)
		                    .header("razorpaySecret", razorpaySecret)
		                    .body(jsonReq)
		                    .asString();

		            String resp = paymentListResponse.getBody();

//					String order = gson1.toJson(resp);

				//	System.out.println("Create Order Response-----------"+order.get get("id"));

					System.out.println("Response-----------"+resp);

					CreateOrderResponse createOrderResponse = gson.fromJson(resp, CreateOrderResponse.class);

					System.out.println("createOrderResponse-----------"+createOrderResponse.getId());

					if(createOrderResponse.getId() == null) {

						ErrorResponse errorResp = gson.fromJson(resp, ErrorResponse.class);
						errorResp.getError();

						res.setErrorResponse(errorResp);
						res.setPan(req.getPan());
						res.setErrorCode("FAILED");
						res.setError(true);
						res.setErrorMessage("Creating OrderID Request Failed");
						String finalResp = new Gson().toJson(res);
						System.out.println("Failed Response:::\n\t"+finalResp);
						response.getWriter().write(new Gson().toJson(res));
						return;

					}

					res.setOrderId(createOrderResponse.getId());
					res.setErrorCode("SUCCESS");
					res.setPan(req.getPan());
					//		res.setTotalAmount(createOrderResponse.getAmount());
					//		res.setTransfersId1(createOrderResponse.transfers.get(0).id);
					//		res.setTransfersId2(createOrderResponse.transfers.get(1).id);
					//	res.setTransfersAmount_1(createOrderResponse.transfers.get(0).amount);
					//	res.setTransfersAmount_2(createOrderResponse.transfers.get(1).amount);

					String finalResp = new Gson().toJson(res);

					System.out.println("SUCCESS Response:::\n\t"+finalResp);

					paymentDetailsPojo.setOrderId(createOrderResponse.getId());
					paymentDetailsPojo.setPaymentStatus(createOrderResponse.status);
					paymentDetailsPojo.setServiceType("OrderID".toUpperCase());	
					paymentDetailsPojo.setPan(req.getPan());

					paymentDetailsPojo.setBankAmount( Double.toString(revenue.getBANKBCPRevenue_Share()));
					paymentDetailsPojo.setNeslAmount( Double.toString(revenue.getNeSLBCPRevenue_Share()));

					paymentDetailsPojo.setPsbaAmount( Double.toString(revenue.getPSBABCPRevenueShare()));
					paymentDetailsPojo.setPsbaTdsAmount( Double.toString(revenue.getPSBATDSCollection()));
					paymentDetailsPojo.setTotalAmount( Double.toString(revenue.getTotal()));

					EjbLookUps.getPaymentDetailsRemote().create(paymentDetailsPojo);


					BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();

					balancePojo.setTxnId(req.getTxnId());
					balancePojo.setOrderId(createOrderResponse.getId());

					EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

					PaymentReportDetailsPojo repoPojo = new PaymentReportDetailsPojo();

					repoPojo.setTxnId(req.getTxnId());
					repoPojo.setOrderId(createOrderResponse.getId());

					EjbLookUps.getPaymentReportDetailsRemote().update(repoPojo);


					String txnStatus = "SUCCESS";
					saveAuditLogs(req.getPan(), "paymentRequest", txnStatus, 
							req.getBank() , req.getUniqueTxnId(),
							"", "");


					response.getWriter().write(new Gson().toJson(res));
					return;

				} catch (RazorpayException e) {
					e.printStackTrace();
					System.out.println("RazorpayException Order could not created:: "+e.getMessage());
					res = new EntityResponce();
					res.setError(true);
					res.setPan(req.getPan());
					res.setErrorCode("FAILED");
					//	res.setError(true);
					res.setErrorMessage("Payment Request Failed");
					String finalResp = new Gson().toJson(res);
					System.out.println("Failed Response:::\n\t"+finalResp);
					response.getWriter().write(new Gson().toJson(res));
					return;
				}
			}
			else {
				System.out.println("Invalid status");
			}

		}catch (Exception e) {
			// TODO: handle exceptions
			e.printStackTrace();
		}
		

	}
	
	
	 public static void saveAuditLogs(String pan , String operation, String status, String email, String userType, String errorCode, String errorDesc) {
	
		 AuditLogsPojo pojo = new AuditLogsPojo();
		 
			pojo.setCreationDate(new Date());
			pojo.setOperation(operation);
			pojo.setStatus(status);
			pojo.setErrCode(errorCode);
			pojo.setErrDescription(errorDesc);
			pojo.setUserType(userType);
			pojo.setPan(pan);
			pojo.setEmail(email);
			
		    try {
		        EjbLookUps.getAuditLogsRemote().create(pojo);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }

	private String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		body = stringBuilder.toString();
		return body;
	}

}

