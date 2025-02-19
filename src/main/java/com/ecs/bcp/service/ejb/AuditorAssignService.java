package com.ecs.bcp.service.ejb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.acl.xsd.TokenResponseXsd;
import com.ecs.bcp.icai.xsd.ICAIResponse;
import com.ecs.bcp.pojo.AuditorAssignPojo;
import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.ICAILogMasterPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
/**
 * @author VISWANTH KANNAIYAN
 */

public class AuditorAssignService {
	
	static Gson gson = new Gson();


	public static String genRandNo(int digit){
	    String characters = "0123456789";
	    String otp = RandomStringUtils.random(digit, characters);
	    return otp;
	  }


	public static boolean checkCode( String code) {
		try {
			AuditorAssignPojo entityCheck = EjbLookUps.getAuditorAssignRemote().findById(code);
			if(entityCheck!=null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	
	public static EntityResponce auditorAssignCreateSP(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();

			  // Database connection parameters
	        String jdbcUrl = EjbLookUps.getSettingsRemote().findById("JDBC_URL").getStringValue(); // Replace with your DB URL
	        String dbUser = EjbLookUps.getSettingsRemote().findById("DB_USERNAME").getStringValue();  // Replace with your DB username
	        String dbPassword = EjbLookUps.getSettingsRemote().findById("DB_PASSWORD").getStringValue();  // Replace with your DB password
	        
	        // Stored Procedure input parameters
	        
			//DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//	DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

          //   new format date
			DateFormat inputDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");  
			DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
	    	AuditorAssignPojo pojo = new AuditorAssignPojo();
	    	
	    	
	    	String code = "A"+genRandNo(6)+"R";
			while (checkCode( code)) {
				System.out.println(" code already exists, generating a new one...");
				code = "A"+genRandNo(6)+"R";
			}

			System.out.println("New unique  code generated: " + code);

			
			
	   /*     String pAuditeeName = "Test Auditee";
	        String pAuditeePan = "ABCDE1234A";
	        String pAuditeeRegNo = null;
	        String pAuditorName = "Test Auditor";
	        String pAuditorPan = "ABCDE1234F";
	        String pAuditorRegNo = "047166";
	        Integer pBcCount = null;
	        Timestamp pCreationDate = Timestamp.valueOf("2024-11-26 11:50:00");
	        Timestamp pFromDate = Timestamp.valueOf("2024-10-15 00:00:00");
	        String pRemarks = null;
	        String pStatus = "ACTIVE";
	        Timestamp pStatusDate = null;
	        Timestamp pToDate = Timestamp.valueOf("2024-11-01 00:00:00");
	        String pRequestorName = "Requestor Name";
	        String pRequestorPan = "ABCDE1234G";
	        String pFirmName = "Test Firm";
	        String pMemberRegNo = "047167";        */

	        // SQL Connection and CallableStatement
	        Connection conn = null;
	        CallableStatement stmt = null;

	        try {
	            // Step 1: Establish the connection
	            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

	            // Step 2: Prepare the stored procedure call
	            String sql = "{CALL AuditoAssign(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	            stmt = conn.prepareCall(sql);

	            // Step 3: Set the input parameters for the stored procedure
	            
	            Date fromDate = null;
	            Date toDate = null;
	            Date statusDate = null;
	            if(!ServiceUtils.isEmpty(requestXsd.getFromDate())) {

					Date date = inputDateFormat.parse(requestXsd.getFromDate());
					String outputDate = outputDateFormat.format(date);
					fromDate = outputDateFormat.parse(outputDate);

				}
				if(!ServiceUtils.isEmpty(requestXsd.getToDate())) {

					Date date = inputDateFormat.parse(requestXsd.getToDate());
					String outputDate = outputDateFormat.format(date);
					toDate = outputDateFormat.parse(outputDate);

				}

				if(!ServiceUtils.isEmpty(requestXsd.getStatusDate())) {

					Date date = inputDateFormat.parse(requestXsd.getStatusDate());
					String outputDate = outputDateFormat.format(date);
					statusDate = outputDateFormat.parse(outputDate);

				}
	
	            stmt.setString(1, code);
	            stmt.setString(2, requestXsd.getAuditeeName());
	            stmt.setString(3, requestXsd.getAuditeePan());
	            stmt.setString(4, requestXsd.getAuditeeRegNo());
	            stmt.setString(5, requestXsd.getAuditorName());
	            stmt.setString(6, requestXsd.getAuditorPan());
	            stmt.setString(7, requestXsd.getAuditorRegNo());
	            stmt.setObject(8, requestXsd.getBcCount());
	            stmt.setTimestamp(9, new Timestamp(new Date().getTime()));
	            stmt.setTimestamp(10, new Timestamp(fromDate.getTime()));
	            stmt.setString(11, requestXsd.getRemark());
//	            pojo.setStatus("ACTIVE");
	            stmt.setString(12, "ACTIVE");
	            stmt.setTimestamp(13, statusDate != null ? new Timestamp(statusDate.getTime()) : null);
	            stmt.setTimestamp(14, new Timestamp(toDate.getTime()));
	            stmt.setString(15, requestXsd.getRequestorName());
	            stmt.setString(16, requestXsd.getRequestorPan());
	            stmt.setString(17, requestXsd.getFirmName());
	            stmt.setString(18, requestXsd.getMemeberRegNo());
//	            stmt.setString(19, requestXsd.getRequestorMobile());


	            // Step 4: Execute the stored procedure
	            stmt.executeUpdate();

	            System.out.println("Stored procedure executed successfully.");
	            
	            responseXsd.setError(false);
	            responseXsd.setErrorDescription("Auditor Assigned Successfully");
				responseXsd.setPan(requestXsd.getPan());
				responseXsd.setName(requestXsd.getName());
				return responseXsd;

	        } catch (SQLException e) {
	            String sqlState = e.getSQLState();
	            String errorMessage = e.getMessage();

	            if ("45000".equals(sqlState) && errorMessage.contains("Date range conflicts")) {
	                // Handle custom SIGNAL error from the stored procedure
	                System.out.println("Error: Date range conflicts with an existing entry for the same auditee_pan.");
	                responseXsd.setError(true);
	                responseXsd.setErrorDescription("Auditor Already Assigned for this period");
	                responseXsd.setPan(requestXsd.getPan());
	                responseXsd.setName(requestXsd.getName());
	                return responseXsd;
	            } else {
	                // Handle other SQL errors
	                System.out.println("Error executing stored procedure: " + e.getMessage());
	                responseXsd.setError(true);
	                responseXsd.setErrorDescription("Error executing stored procedure");
	                return responseXsd;
	            }
	        } finally {
	            try {
	                // Step 5: Close resources
	                if (stmt != null) stmt.close();
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                System.out.println("Error closing resources: " + e.getMessage());
	            }
	        }
		
	     
			  
	}
	
	
	public static EntityResponce auditeeNameAndAuditorPanCheck(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();

		try {
			AuditorAssignPojo entityCheck = EjbLookUps.getAuditorAssignRemote().auditeeNameAndAuditorPanCheck(requestXsd.getAuditeeName(), requestXsd.getAuditorPan());

			if(entityCheck != null) {

				System.out.println("already auditor assigned");
				responseXsd.setError(true);
				responseXsd.setErrorCode("100104");
				responseXsd.setErrorDescription("Auditor Already Assigned for this Auditee");
				return responseXsd;
			} else {
				System.out.println("new");
				responseXsd.setError(false);
				return responseXsd;
			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}



	public static EntityResponce getAuditorAssignList(EntityRequest reqXSD) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		List<AuditorAssignPojo> pojoList = null;

		try {

			pojoList = EjbLookUps.getAuditorAssignRemote().findByProperty("auditeePan", reqXSD.getAuditeePan());

			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojoList) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setSrNo(linkbank.getSrNo());
					doclink.setAuditeePan(linkbank.getAuditeePan());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());
					doclink.setStatus(linkbank.getStatus());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setRemark(linkbank.getRemark());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setBcCount(linkbank.getBcCount());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
					
					banklink.add(doclink);
				}


				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditorList(banklink);
				return responseXsd;

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	public static EntityResponce auditorPanCheck(EntityRequest requestXSD) throws Exception {
		
		EntityResponce responseXsd  = new EntityResponce();
		List<AuditorAssignPojo> pojo = null;

		try {

			pojo = EjbLookUps.getAuditorAssignRemote().findByProperty("auditeePan",requestXSD.getAuditeePan());
			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0013);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0013));
				return responseXsd;
			}
			else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojo) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());

					banklink.add(doclink);

				}
				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditorList(banklink);
				return responseXsd;
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}




	public static EntityResponce getAuditorList(EntityRequest reqXSD) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		List<AuditorAssignPojo> pojoList = null;

		try {

			pojoList = EjbLookUps.getAuditorAssignRemote().findByProperty("auditorPan", reqXSD.getAuditorPan());

			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojoList) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setSrNo(linkbank.getSrNo());
					doclink.setAuditeePan(linkbank.getAuditeePan());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());
					doclink.setStatus(linkbank.getStatus());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setRemark(linkbank.getRemark());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setBcCount(linkbank.getBcCount());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					banklink.add(doclink);
				}


				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditorList(banklink);
				return responseXsd;

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}


	public static EntityResponce RevokeAuditor(EntityRequest reqXSD) throws Exception {

		EntityResponce responseXsd = new EntityResponce();

		AuditorAssignPojo pojoList = null;

		try {
			pojoList = EjbLookUps.getAuditorAssignRemote().findById(reqXSD.getSrNo());



			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0021);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0021));
				return responseXsd;
			} else {

				if(pojoList.getStatus().equals("REVOKED")) {
					
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0021);
					responseXsd.setErrorDescription("Already Revoked!");
					return responseXsd;
				}
				

				AuditorAssignPojo pojo = new AuditorAssignPojo();

					pojo.setSrNo(reqXSD.getSrNo());
					pojo.setStatus("REVOKED");
					pojo.setRemark(reqXSD.getRemark());
					pojo.setStatusDate(new Date());
                    
					EjbLookUps.getAuditorAssignRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);
					responseXsd.setErrorDescription("Revoked successfully");
					return responseXsd;

			}
		} catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}



	public static EntityResponce getauditorAssignById (EntityRequest requestXsd) {

		EntityResponce responseXsd = new EntityResponce();

		AuditorAssignPojo pojo = null;

		
		
		try {

			pojo = EjbLookUps.getAuditorAssignRemote().findById(requestXsd.getSrNo());


			System.out.println("pojolist----------"+pojo);

			if(pojo==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;

			}else {

				responseXsd.setError(false);
				responseXsd.setAuditorDetails(pojo);
				return responseXsd;
			}


		}
		catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}

	}


	public static EntityResponce getDetailsByAuditorPan(EntityRequest reqXSD)
			throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		List<AuditorAssignPojo> pojoList = null;


		try {

			pojoList = EjbLookUps.getAuditorAssignRemote().findByProperty("auditorPan", reqXSD.getAuditorPan());
 
			 
			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

					for (AuditorAssignPojo linkbank : pojoList) {
						if(linkbank.getStatus().equals("ACTIVE")) {
						AuditorAssignPojo doclink = new AuditorAssignPojo();
						
						doclink.setAuditeePan(linkbank.getAuditeePan());
						doclink.setAuditeeName(linkbank.getAuditeeName());
						doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
						doclink.setFromDate(linkbank.getFromDate());
						doclink.setToDate(linkbank.getToDate());
						
						banklink.add(doclink);
						}
					}		
						
				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditeeList(banklink);
				return responseXsd;

			      }
			
		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	

	public static EntityResponce auditeeAssignStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<AuditorAssignPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getAuditorAssignRemote().bcAuditeePaymentStatus(requestXSD.getAuditeePan(),requestXSD.getStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	        } else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojo) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setSrNo(linkbank.getSrNo());
					doclink.setAuditeePan(linkbank.getAuditeePan());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());
					doclink.setStatus(linkbank.getStatus());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setRemark(linkbank.getRemark());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setBcCount(linkbank.getBcCount());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					banklink.add(doclink);
				}


				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditorCount(banklink);
				return responseXsd;

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}

	public static EntityResponce auditorAssignStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<AuditorAssignPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getAuditorAssignRemote().bcAuditorPaymentStatus(requestXSD.getAuditorPan(),requestXSD.getStatus());
	    	
	    	System.out.println("reqXSD.getAuditorPan()-----------------------------"+requestXSD.getAuditorPan());
	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	        } else {

				List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojo) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setSrNo(linkbank.getSrNo());
					doclink.setAuditeePan(linkbank.getAuditeePan());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());
					doclink.setStatus(linkbank.getStatus());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setRemark(linkbank.getRemark());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setBcCount(linkbank.getBcCount());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					banklink.add(doclink);
				}


				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditorCount(banklink);
				return responseXsd;

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	public static EntityResponce getDetailsBySrNo (EntityRequest requestXsd) {

		EntityResponce responseXsd = new EntityResponce();

		AuditorAssignPojo pojoList = null;
		

		try {

			pojoList =	EjbLookUps.getAuditorAssignRemote().getDetailsBySrNo(requestXsd.getSrNo());
			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
				
			}else {


		            		
				AuditorAssignPojo doclink = new AuditorAssignPojo();

				doclink.setSrNo(pojoList.getSrNo());
				doclink.setAuditeePan(pojoList.getAuditeePan());
				doclink.setAuditeeName(pojoList.getAuditeeName());
				doclink.setAuditeeRegNo(pojoList.getAuditeeRegNo());
				doclink.setAuditorName(pojoList.getAuditorName());
				doclink.setAuditorPan(pojoList.getAuditorPan());
				doclink.setAuditorRegNo(pojoList.getAuditorRegNo());
				doclink.setStatus(pojoList.getStatus());
				doclink.setFromDate(pojoList.getFromDate());
				doclink.setToDate(pojoList.getToDate());
				doclink.setRemark(pojoList.getRemark());
				doclink.setStatusDate(pojoList.getStatusDate());
				doclink.setBcCount(pojoList.getBcCount());
				doclink.setRequestorName(pojoList.getRequestorName());
				doclink.setRequestorPan(pojoList.getRequestorPan());
				doclink.setMemeberRegNo(pojoList.getMemeberRegNo());
				doclink.setFirmName(pojoList.getFirmName());
		            }

		            responseXsd.setError(false);
		            responseXsd.setAuditorauditeeDetails(pojoList);
		            return responseXsd;
		        

		}
		catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}

	}
	
	public static EntityResponce getAuditorAssignDataByStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<AuditorAssignPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getAuditorAssignRemote().getAuditorAssignDataByStatus(requestXSD.getStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	        	List<AuditorAssignPojo> banklink = new ArrayList<>();

				for (AuditorAssignPojo linkbank : pojo) {

					AuditorAssignPojo doclink = new AuditorAssignPojo();

					doclink.setSrNo(linkbank.getSrNo());
					doclink.setAuditeePan(linkbank.getAuditeePan());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setAuditeeRegNo(linkbank.getAuditeeRegNo());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorRegNo(linkbank.getAuditorRegNo());
					doclink.setStatus(linkbank.getStatus());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setRemark(linkbank.getRemark());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setBcCount(linkbank.getBcCount());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					banklink.add(doclink);
				}



				responseXsd = new EntityResponce();
	            responseXsd.setError(false);
	            responseXsd.setAuditorList(banklink);
	            return responseXsd;
	        }
	        
	        
	               
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0001);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	        return responseXsd;
	    }
	
	}
	
	public static EntityResponce checkICAI (EntityRequest requestXsd) {

		EntityResponce responseXsd = new EntityResponce();


		String tokenId = "";
		String status = "";
		int utId = 0;
		Serializer serializer = new Persister();
		ICAILogMasterPojo logPojo = new ICAILogMasterPojo();
		PropertiesReader pro = new PropertiesReader();
		try {

			Unirest.setTimeouts(5000, 5000);
			System.out.println("Posting Data to Web Server------ ICAI Token");
			//String Url ="https://eservices.icai.org/iONBizServices/Authenticate?usrloginid=aasb.psba.api%40icai.in&usrpassword=Temp%401234";
		
			// UAT - 172.19.255.50 
			// PROD WEB - 172.19.241.11
			
//			String Url ="http://localhost:8080/DBCPWebWrapperService/BCPICAITokenWrapper";
//			String Url ="http://172.19.241.11:8080/DBCPWebWrapperService/BCPICAITokenWrapper";
			
			String bcpWebUrl = pro.getUrlProperty("BCPWebURL");
			
			String Url =bcpWebUrl+"/BCPICAITokenWrapper";
			
			System.out.println("Url-----> "+Url);
			
			HttpResponse<String> tokenResponse = Unirest.post(Url)
					.body("")
					.asString();

			String token =  tokenResponse.getBody();
			System.out.println("Posting Data to Web Server------ ICAI Token------------->>:: "+token);

			Random random = new Random();

			for (int i = 0; i < 7; i++) {
				int digit = random.nextInt(10);  
				utId = utId * 10 + digit;
			}
			System.out.println("Random Number ------------>>" + ": " + utId);


			logPojo.setApiRequest(Url);
			logPojo.setApiResponse(token);
			logPojo.setUTID(String.valueOf(utId));


			if(token != null) {

				TokenResponseXsd response = serializer.read(TokenResponseXsd.class, token);

				tokenId =  response.getResult().getTokenId();
				status = response.getResult().getStatus();

				System.out.println("Status ::------------->>:: "+ status);
				System.out.println("tokenId ::------------>>:: "+ tokenId);

				logPojo.setReqType("Token_Generate".toUpperCase());
				logPojo.setToken(tokenId);


				if(status.contains("1")) {

					ICAILogMasterPojo logPojo1 = new ICAILogMasterPojo();
					Unirest.setTimeouts(0, 0);
					String URLToPOst ="https://eservices.icai.org/EForms/CustomWebserviceServlet?"
							+ "serviceCalled="+"WebserviceDataFetch"+"&orgId="+"1666"+"&tokenid="+tokenId+"&actionId="
							+ "WebServiceCustonJson"+"&getDetail="+"ValidateFirmMember"+"&UTID="+utId+"&memberid="
							+requestXsd.getMemeberRegNo()+"&firmRegNo="+requestXsd.getFirmRegno();
				
				//	String URL ="http://localhost:8080/DBCPWebWrapperService/BCPICAICheckAPI";
				//	String URL ="http://172.19.241.11:8080/DBCPWebWrapperService/BCPICAICheckAPI";
					
					String URL = bcpWebUrl+"/BCPICAICheckAPI";
					
					
					HttpResponse<String> resp = Unirest.post(URL).body(URLToPOst)
							.asString();

					String ValidateResp =  resp.getBody();
					System.out.println("ValidateResp------>>::: "+ValidateResp);

					logPojo1.setToken(tokenId);
					logPojo1.setApiRequest(URL);
					logPojo1.setApiResponse(ValidateResp);

					logPojo1.setReqType("Transactional_DATA".toUpperCase());
					logPojo1.setToken(tokenId);
					logPojo1.setUTID(String.valueOf(utId));

					ICAIResponse icaiResp = gson.fromJson(ValidateResp.toString(), ICAIResponse.class);

					if (icaiResp.getStatus() != null) {

						String FRNStatus = icaiResp.getStatus().getFRN();
						String MRNStatus = icaiResp.getStatus().getMRN();

						System.out.println("FRNStatus ----------------- :: "+FRNStatus+"\n MRNStatus ------------------:: "+MRNStatus);

						logPojo1.setStatusFRN(FRNStatus);
						logPojo1.setStatusMRN(MRNStatus);


						responseXsd.setStatusFRN(FRNStatus);
						responseXsd.setStatusMRN(MRNStatus);
						responseXsd.setStatusMAPPING(icaiResp.getStatus().getMRN_FRN_MAPPING());

						responseXsd.setFirmRegno(icaiResp.getFRN());
						responseXsd.setMemeber_reg_no(icaiResp.getMRN());
						responseXsd.setUTID(icaiResp.getUT_ID());
						responseXsd.setErrorCodeIcai(icaiResp.getErrorCode());


						if (icaiResp.getMrnDetails() != null && !ServiceUtils.isEmpty(icaiResp.getMrnDetails().getCOP_STATUS())) {
							responseXsd.setMRNDtlsStatus(icaiResp.getMrnDetails().getCOP_STATUS());
							responseXsd.setMRNDtlsEmail(icaiResp.getMrnDetails().getEMAIL());
							responseXsd.setMRNDtlsMobileNo(icaiResp.getMrnDetails().getMOBILE_NO());
							responseXsd.setMRNDtlsName(icaiResp.getMrnDetails().getNAME());
						}

						if (icaiResp.getFrnDetails() != null && !ServiceUtils.isEmpty(icaiResp.getFrnDetails().getFIRM_NAME())) {
							responseXsd.setFRNDtlsFirmName(icaiResp.getFrnDetails().getFIRM_NAME());
							responseXsd.setFRNDtlsEmail(icaiResp.getFrnDetails().getEMAIL());
							responseXsd.setFRNDtlsFRN(icaiResp.getFrnDetails().getFRN());
						}

						EjbLookUps.getICALLogMasterRemote().create(logPojo);
						EjbLookUps.getICALLogMasterRemote().create(logPojo1);
						responseXsd.setError(false);
						return responseXsd;


					} else {

						logPojo1.setErrCode(Constants.ECSV0005);
						logPojo1.setErrMsg("Status is null");
						EjbLookUps.getICALLogMasterRemote().create(logPojo1);
						System.out.println("Status is null");
					}
				}

				logPojo.setErrCode(response.getResult().getStatus());
				logPojo.setErrMsg(response.getResult().getMsg());
				EjbLookUps.getICALLogMasterRemote().create(logPojo);


				responseXsd.setStatus(response.getResult().getStatus());
				responseXsd.setTokenId(response.getResult().getTokenId());
				responseXsd.setErrorMessage(response.getResult().getMsg());
				responseXsd.setError(true);
				return responseXsd;

			}else {
				logPojo.setErrCode(Constants.ECSV0005);
				logPojo.setErrMsg("Access Denied");
				EjbLookUps.getICALLogMasterRemote().create(logPojo);
				responseXsd.setError(true);
				return responseXsd;

			}
		}catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));

			logPojo.setErrCode(Constants.ECSV0100);
			logPojo.setErrMsg(e.getMessage());
			try {
				EjbLookUps.getICALLogMasterRemote().create(logPojo);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			return responseXsd;
		}

	}
	
	
	
	
	public static EntityResponce getUserManual(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();

	    try {
	    	String auditee01 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_01").getStringValue();
	    	String auditee02 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_02").getStringValue();
	    	String auditee03 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_03").getStringValue();
	    	String auditee04 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_04").getStringValue();
	    	String auditee05 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_05").getStringValue();
	    	String auditee06 = EjbLookUps.getSettingsRemote().findById("UM_AUDITEE_06").getStringValue();
	    	String auditor01 = EjbLookUps.getSettingsRemote().findById("UM_AUDITOR_01").getStringValue();
	    	String auditor02 = EjbLookUps.getSettingsRemote().findById("UM_AUDITOR_02").getStringValue();
	    	String auditor03 = EjbLookUps.getSettingsRemote().findById("UM_AUDITOR_03").getStringValue();
	    	String auditor04 = EjbLookUps.getSettingsRemote().findById("UM_AUDITOR_04").getStringValue();
	    	String info01 = EjbLookUps.getSettingsRemote().findById("UM_INFO_01").getStringValue();
	    	String info02 = EjbLookUps.getSettingsRemote().findById("UM_INFO_02").getStringValue();
	    	
	    	responseXsd.setUmAuditee01(auditee01);
	    	responseXsd.setUmAuditee02(auditee02);
	    	responseXsd.setUmAuditee03(auditee03);
	    	responseXsd.setUmAuditee04(auditee04);
	    	responseXsd.setUmAuditee05(auditee05);
	    	responseXsd.setUmAuditee06(auditee06);
	    	responseXsd.setUmAuditor01(auditor01);
	    	responseXsd.setUmAuditor02(auditor02);
	    	responseXsd.setUmAuditor03(auditor03);
	    	responseXsd.setUmAuditor04(auditor04);
	    	responseXsd.setUmInfo01(info01);
	    	responseXsd.setUmInfo02(info02);
	    	
	    	responseXsd.setError(false);
	    	return responseXsd;
	    	

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	
	
	
	
	
	
	
public static void main(String[] args) {
	

		
		String value = "100";
		String PsbaCrg ="";

		
		
		if(Integer.getInteger(value) <= 25) {
			
			System.out.println("value-------A--------->> "+value);
			
			PsbaCrg = "1-25";
			
		}else if((Integer.getInteger(value) >= 26) && (Integer.getInteger(value) <= 100) ) {
			
			System.out.println("value-------B--------->> "+value);
			
			PsbaCrg = "26-100";
			
		}else if(Integer.getInteger(value) >= 101) {
			
			System.out.println("value-------C--------->> "+value);
			
			PsbaCrg = "ABOVE 100";
		}else {
			System.out.println("value-------D--------->> "+value);
			
			PsbaCrg = "";
		}
	
}
	        
		
}		