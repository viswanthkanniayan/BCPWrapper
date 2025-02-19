package com.ecs.bcp.service.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.pojo.SignCheckPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.BalanceConfirmXsd;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
/**
 * @author VISWANTH KANNAIYAN
 */

public class BalanceConfirmationService {


	public static boolean checkCode( String code) {
		try {
			BalanceConfirmPojo entityCheck = EjbLookUps.getBalanceConfirmRemote().findById(code);
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
	
	
	public static EntityResponce createBalanceConfirmation(EntityRequest requestXsd) throws Exception {
       
		  EntityResponce responseXsd = new EntityResponce();
		try {

			BalanceConfirmPojo pojo = new BalanceConfirmPojo();
            
			String code = "B"+genRandNo(6);
            
			while (checkCode( code)) {
				System.out.println(" code already exists, generating a new one...");
				code = "B"+genRandNo(6);
			}
           
			System.out.println("New unique  code generated: " + code);
            
			pojo.setTxnId(code);
			//dd MMMM yyyy
			 
		//	DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//	DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

			
		//	new format date
			
			DateFormat inputDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");  
			DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			pojo.setCreationDate(new Date());
			pojo.setAuditeePan(requestXsd.getAuditeePan());
			pojo.setAuditeeName(requestXsd.getAuditeeName());
			pojo.setAuditeeRegNo(requestXsd.getAuditeeRegNo());
			pojo.setAuditeeCin(requestXsd.getAuditeeCin());
			pojo.setAuditeeLei(requestXsd.getAuditeeLei());
			pojo.setPurpose(requestXsd.getPurpose());
			pojo.setRequestorName(requestXsd.getRequestorName());
			pojo.setRequestorPan(requestXsd.getRequestorPan());
			pojo.setRequestorRegNo(requestXsd.getRequestorRegNo());
			pojo.setRequestorType(requestXsd.getRequestorType());
			pojo.setInformationProvider(requestXsd.getInformationProvider());
			pojo.setBank(requestXsd.getBank());
			pojo.setRequestDate(new Date());
			pojo.setStatusDate(new Date());
			pojo.setAccountCount("1");
			pojo.setStatus("PENDING");
			pojo.setPaymentStatus("PENDING");
			pojo.setRemarks(requestXsd.getRemarks());
			pojo.setAuditorPan(requestXsd.getAuditorPan());
			pojo.setAuditorName(requestXsd.getAuditorName());
			pojo.setRequestorEmail(requestXsd.getRequestorEmail());
			pojo.setRequestorMobile(requestXsd.getRequestorMobile());
			pojo.setUserId(requestXsd.getUserId());
			pojo.setFirmName(requestXsd.getFirmName());
			pojo.setMemeberRegNo(requestXsd.getMemeberRegNo());
			pojo.setUniqueTxnId(requestXsd.getUniqueTxnId());
			pojo.setAuditeeType(requestXsd.getAuditeeType());
			pojo.setBusinessDate(new Date());
			pojo.setEntityEmail(requestXsd.getEntityEmail());
			pojo.setEntityMobile(requestXsd.getEntityMobile());
	        pojo.setEntityReqPan(requestXsd.getEntityReqPan());
			pojo.setEntityReqName(requestXsd.getEntityReqName());
			Date date3 = null;  
			String outputDate = null;  
			Date date = null;
			
			if (!ServiceUtils.isEmpty(requestXsd.getFromDate())) {
			     date = inputDateFormat.parse(requestXsd.getFromDate());
			    outputDate = outputDateFormat.format(date);
			    date3 = outputDateFormat.parse(outputDate);

			    pojo.setFromDate(date3);
			}

			System.out.println("getFromDate------------------->>>" + requestXsd.getFromDate());
			System.out.println("date3------------------->>>" + date3);
			System.out.println("outputDate------------------->>>" + outputDate);
			System.out.println("date------------------->>>" + date);
			
			if(!ServiceUtils.isEmpty(requestXsd.getToDate())) {

				 date = inputDateFormat.parse(requestXsd.getToDate());
			    outputDate = outputDateFormat.format(date);
			    date3 = outputDateFormat.parse(outputDate);

			    pojo.setToDate(date3);
			}
			
			System.out.println("getToDate------------------->>>>"+requestXsd.getToDate());
			System.out.println("date3------------------->>>" + date3);
			System.out.println("outputDate------------------->>>" + outputDate);
			System.out.println("date------------------->>>" + date);
			
			
	/*		if(!ServiceUtils.isEmpty(requestXsd.getBusinessDate())) {

				DateFormat inputDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DateFormat outputDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				System.out.println("req date--->"+requestXsd.getBusinessDate());

				Date date = inputDateFormat1.parse(requestXsd.getBusinessDate());
				String outputDate = outputDateFormat.format(date);

				  System.out.println("Output date: " + outputDate);
				Date date2 = outputDateFormat2.parse(outputDate);
				System.out.println("Parsed Date: " + date2);

				pojo.setBusinessDate(date2);
			}
*/

			EjbLookUps.getBalanceConfirmRemote().create(pojo);

			PaymentReportDetailsPojo payReport = new PaymentReportDetailsPojo();


		      payReport.setTxnId(code);
		      payReport.setAuditeePan(requestXsd.getAuditeePan());
		      payReport.setUniqueTxnId(requestXsd.getUniqueTxnId());
		      payReport.setAuditorPan(requestXsd.getAuditorPan());
		      payReport.setReqName(requestXsd.getRequestorName());
		      String formattedDate = inputDateFormat.format(new Date());
		      payReport.setDateOfReq(formattedDate);
		      payReport.setInfoProviderName(requestXsd.getInformationProvider());
		      payReport.setBankName(requestXsd.getBank());

		      EjbLookUps.getPaymentReportDetailsRemote().create(payReport);
			System.out.println("create----->>>>");

			responseXsd.setTxnId(code);
			responseXsd.setError(false);
			responseXsd.setErrorCode("");
			responseXsd.setErrorDescription("Balance Confirmation Request Submitted Successfully");
			
			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occured while creating");
			return responseXsd;
		}
	}


	public static EntityResponce bcPaymentUpdate(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("update service");

			BalanceConfirmPojo pojo1 =	EjbLookUps.getBalanceConfirmRemote().findById(requestXsd.getTxnId());

			System.out.println("pojo--> "+pojo1);
			if(pojo1!=null) {

				BalanceConfirmPojo pojo = new BalanceConfirmPojo();

				pojo.setTxnId(requestXsd.getTxnId());
				pojo.setStatusDate(new Date());
				pojo.setPaymentStatus("SUCCESS");
				pojo.setConsent("Y");

				EjbLookUps.getBalanceConfirmRemote().update(pojo);

				System.out.println("updated");


				//----------------------sign check-------------------------------------
				// create 

				SignCheckPojo signpojo = new SignCheckPojo();

				System.out.println("pan-->>> "+pojo1.getAuditeePan());
//				signpojo.setId(requestXsd.getId());
				
				signpojo.setSignedBy("CDSL - Test");
				signpojo.setLocation("Bengaluru");
				signpojo.setReason("Account Opening");
				signpojo.setSignedDate("2024-06-05 08:20:20.000000");
				signpojo.setSubject("st=Tamil Nadu");
				signpojo.setInfo(pojo1.getAuditeePan());
				signpojo.setIssuer("cn=CDSL Ventures Limited CA 2022");
				signpojo.setSerialNo("5B 76 D6 01 9B 4F 5D B2 59 46 B1 60 0D 24 86 CF");
				signpojo.setValidity("2024/06/05 08:49:47 +05'30'");
				signpojo.setCreationDate(new Date());
				
				EjbLookUps.getSignCheckRemote().create(signpojo);

				System.out.println("create");
				
				responseXsd.setError(false);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("update Successfully");
				return responseXsd;



			}else {
				System.out.println("pojo is not found");

				responseXsd.setError(true);
				responseXsd.setErrorCode("1003");
				//responseXsd.setErrorCode(Constants.ECSV0001);
				//responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
				responseXsd.setErrorDescription("No Records");
				return responseXsd;
			}


		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occured while creating");
			return responseXsd;



		}
	}  
	
	
	public static String genRandNo(int digit){
	    String characters = "0123456789";
	    String otp = RandomStringUtils.random(digit, characters);
	    return otp;
	  }

	
	public static String genTxnId() {

		LocalDateTime now = LocalDateTime.now();

		String formattedDate = now.format(DateTimeFormatter.ofPattern("MMdd"));
		String formattedTime = now.format(DateTimeFormatter.ofPattern("HHmm"));

		int milliseconds = now.getNano() / 1_000_000; // Convert nanoseconds to milliseconds

		//   Random random = new Random();
		//   int randomNumber = random.nextInt(999);
		//+ String.format("%03d", randomNumber)

		String randomNumberWithDateTime = String.format("%03d", milliseconds) +formattedDate + formattedTime   ;

		System.out.println("LinkageId :" + randomNumberWithDateTime);

		return randomNumberWithDateTime; 
	}




	public static EntityResponce createSignCheck(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		try {

			SignCheckPojo SignCheckPojo =	EjbLookUps.getSignCheckRemote().findById(requestXsd.getId());

			if(SignCheckPojo!=null) {

				SignCheckPojo signpojo = new SignCheckPojo();

				signpojo.setId(requestXsd.getId());
				signpojo.setSignedBy(requestXsd.getSignedBy());
				signpojo.setLocation(requestXsd.getLocation());
				signpojo.setReason(requestXsd.getReason());
				signpojo.setSignedDate(requestXsd.getSignedDate());
				signpojo.setInfo(requestXsd.getInfo());
				signpojo.setSubject(requestXsd.getSubject());
				signpojo.setFirmName(requestXsd.getFirmName());
				signpojo.setMemeberRegNo(requestXsd.getMemeberRegNo());
				EjbLookUps.getSignCheckRemote().create(signpojo);

				System.out.println("create");

				responseXsd.setError(false);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("create Successfully");
				return responseXsd;
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occured while creating");
			return responseXsd;
		}
		return responseXsd;
	}

	public static EntityResponce auditorbcRequestPan(EntityRequest requestXSD) throws Exception {
		
		EntityResponce responseXsd  = new EntityResponce();
		List<BalanceConfirmPojo> pojo = null;

		try {

			pojo = EjbLookUps.getBalanceConfirmRemote().findByProperty("auditorPan",requestXSD.getAuditorPan());
			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<BalanceConfirmPojo> banklist = new ArrayList<>();

				for (BalanceConfirmPojo linkbank : pojo) {

					 if (!"FAILED".equalsIgnoreCase(linkbank.getStatus())) {
					BalanceConfirmPojo doclink = new BalanceConfirmPojo();


					doclink.setTxnId(linkbank.getTxnId());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setBank(linkbank.getBank());
					doclink.setBusinessDate(linkbank.getBusinessDate());
					doclink.setRequestDate(linkbank.getRequestDate());
					doclink.setAccountCount(linkbank.getAccountCount());
					doclink.setStatus(linkbank.getStatus());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setDownloadedDate(linkbank.getDownloadedDate());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setRequestorType(linkbank.getRequestorType());
					doclink.setFromDate(linkbank.getFromDate());
					doclink.setToDate(linkbank.getToDate());
					doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					doclink.setPaymentStatus(linkbank.getPaymentStatus());
					doclink.setCertificatePath(linkbank.getCertificatePath() != null ? linkbank.getCertificatePath() :"");
					
					banklist.add(doclink);
					 }

				}
				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditoBcRequestList(banklist);
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

	

	public static EntityResponce auditeebcRequestPan(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<BalanceConfirmPojo> pojo = null;

		try {

			pojo = EjbLookUps.getBalanceConfirmRemote().findByProperty("auditeePan",requestXSD.getAuditeePan());
			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<BalanceConfirmPojo> banklist = new ArrayList<>();

				for (BalanceConfirmPojo linkbank : pojo) {
					 if (!"FAILED".equalsIgnoreCase(linkbank.getStatus())) {
					BalanceConfirmPojo doclink = new BalanceConfirmPojo();

 
					doclink.setTxnId(linkbank.getTxnId());
					doclink.setAuditeeName(linkbank.getAuditeeName());
					doclink.setRequestorName(linkbank.getRequestorName());
					doclink.setBank(linkbank.getBank());
					doclink.setBusinessDate(linkbank.getBusinessDate());
					doclink.setRequestDate(linkbank.getRequestDate());
					doclink.setAccountCount(linkbank.getAccountCount());
					doclink.setStatus(linkbank.getStatus());
					doclink.setStatusDate(linkbank.getStatusDate());
					doclink.setDownloadedDate(linkbank.getDownloadedDate());
					doclink.setPaymentStatus(linkbank.getPaymentStatus());
					doclink.setPaymentDate(linkbank.getPaymentDate());
					doclink.setPaymentId(linkbank.getPaymentId());
					doclink.setAuditorPan(linkbank.getAuditorPan());
					doclink.setAuditorName(linkbank.getAuditorName());
					doclink.setRequestorEmail(linkbank.getRequestorEmail());
					doclink.setUserId(linkbank.getUserId());
					doclink.setRequestorType(linkbank.getRequestorType());
					doclink.setFromDate(linkbank.getFromDate());;
					doclink.setToDate(linkbank.getToDate());
					doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					doclink.setTaxInvoicePath(linkbank.getTaxInvoicePath());
					doclink.setUniqueTxnId(linkbank.getUniqueTxnId());
					doclink.setRequestorPan(linkbank.getRequestorPan());
					doclink.setCertificatePath(linkbank.getCertificatePath() != null ? linkbank.getCertificatePath() :"");
					doclink.setEntityReqPan(linkbank.getEntityReqPan());
					doclink.setEntityReqName(linkbank.getEntityReqName());
					
					
					banklist.add(doclink);

					 }
				}
				responseXsd = new EntityResponce();
				responseXsd.setError(false);

				responseXsd.setAuditeeBcRequestList(banklist);
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
	

	
	public static EntityResponce auditeeStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditeeStatus(requestXSD.getAuditeePan(),requestXSD.getStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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

	public static EntityResponce getBcDataByStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().getBcDataBcStatus(requestXSD.getStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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
	public static EntityResponce auditorStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditorStatus(requestXSD.getAuditorPan(),requestXSD.getStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setBusinessDate(linkbank.getBusinessDate());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                doclink.setBank(linkbank.getBank());
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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

	public static EntityResponce auditeePaymentStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditeePaymentStatus(requestXSD.getAuditeePan(),requestXSD.getStatus(),requestXSD.getPaymentStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setDownloadStatus(linkbank.getDownloadStatus());
	                doclink.setBusinessDate(linkbank.getBusinessDate());
	                doclink.setBank(linkbank.getBank());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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
	
	

	public static EntityResponce auditorPaymentStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditorPaymentStatus(requestXSD.getAuditorPan(),requestXSD.getStatus(),requestXSD.getPaymentStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setBusinessDate(linkbank.getBusinessDate());
	                doclink.setBank(linkbank.getBank());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }
            
	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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
          
	public static EntityResponce auditorDownloadStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;
       
	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditorDownloadStatus(requestXSD.getAuditorPan(),requestXSD.getDownloadStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setBusinessDate(linkbank.getBusinessDate());
	                doclink.setBank(linkbank.getBank());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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

	public static EntityResponce auditeeDownloadStatus(EntityRequest requestXSD) throws Exception {
	    EntityResponce responseXsd = new EntityResponce();
	    List<BalanceConfirmPojo> pojo = null;

	    try {
	    	pojo= EjbLookUps.getBalanceConfirmRemote().bcAuditeeDownloadStatus(requestXSD.getAuditeePan(),requestXSD.getDownloadStatus());

	        if (pojo == null) {
	            System.out.println("No Data Found");
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0008);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	            return responseXsd;
	            
	            
	        } else {
	        	
	            List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojo) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setRequestorName(linkbank.getRequestorName());
	                doclink.setRequestDate(linkbank.getRequestDate());
	                doclink.setAccountCount(linkbank.getAccountCount());
	                doclink.setStatus(linkbank.getStatus());
	                doclink.setStatusDate(linkbank.getStatusDate());
	                doclink.setDownloadedDate(linkbank.getDownloadedDate());
	                doclink.setPaymentStatus(linkbank.getPaymentStatus());
	                doclink.setRequestorEmail(linkbank.getRequestorEmail());
	                doclink.setUserId(linkbank.getUserId());
	                doclink.setRequestorType(linkbank.getRequestorType());
	                doclink.setFromDate(linkbank.getFromDate());
	                doclink.setToDate(linkbank.getToDate());
	                doclink.setBusinessDate(linkbank.getBusinessDate());
	                doclink.setBank(linkbank.getBank());
	                doclink.setFirmName(linkbank.getFirmName());
					doclink.setMemeberRegNo(linkbank.getMemeberRegNo());
					
	                banklist.add(doclink);
	            }

	            responseXsd.setError(false);
	            responseXsd.setBcPaymentStatus(banklist);
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

	public static EntityResponce getBcDetailsByTxnId (EntityRequest requestXsd) {

		EntityResponce responseXsd = new EntityResponce();

		BalanceConfirmPojo pojoList = null;
		

		try {

			pojoList =	EjbLookUps.getBalanceConfirmRemote().getBcByTxnId(requestXsd.getTxnId());
			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
				
			}else {
		            		
		                BalanceConfirmPojo doclink = new BalanceConfirmPojo();
		                
                        
		                doclink.setTxnId(pojoList.getTxnId());
		                doclink.setAuditeeName(pojoList.getAuditeeName());
		                doclink.setRequestorName(pojoList.getRequestorName());
		                doclink.setRequestDate(pojoList.getRequestDate());
		                doclink.setAccountCount(pojoList.getAccountCount());
		                doclink.setStatus(pojoList.getStatus());
		                doclink.setStatusDate(pojoList.getStatusDate());
		                doclink.setDownloadedDate(pojoList.getDownloadedDate());
		                doclink.setPaymentStatus(pojoList.getPaymentStatus());
		                doclink.setRequestorEmail(pojoList.getRequestorEmail());
		                doclink.setUserId(pojoList.getUserId());
		                doclink.setRequestorType(pojoList.getRequestorType());
		                doclink.setFromDate(pojoList.getFromDate());
		                doclink.setToDate(pojoList.getToDate());
		                doclink.setBusinessDate(pojoList.getBusinessDate());
		                doclink.setBank(pojoList.getBank());
		                doclink.setFirmName(pojoList.getFirmName());
						doclink.setMemeberRegNo(pojoList.getMemeberRegNo());
						doclink.setBankRespDate(pojoList.getBankRespDate());
						doclink.setReqDeliveryDate(pojoList.getReqDeliveryDate());
						doclink.setPaymentDate(pojoList.getPaymentDate());
						doclink.setDownloadedDate(pojoList.getDownloadedDate());
						
		            }
                        
		                 responseXsd.setError(false);
		                 responseXsd.setBcDetails(pojoList);
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
	
	
	public static EntityResponce getAuditeeSummary(EntityRequest requestXSD) throws Exception {
	      EntityResponce responseXsd = new EntityResponce();
	      List<Object[]> pojo = null;

	      try {
	        
	          pojo = EjbLookUps.getBalanceConfirmRemote().getPaymentSummaryByDate(requestXSD.getAuditeePan());

	          if (pojo == null || pojo.isEmpty()) {
	              System.out.println("No Data Found");
	              responseXsd.setError(true);
	              responseXsd.setErrorCode(Constants.ECSV0008);
	              responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
	              return responseXsd;
	          }

	          DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	          List<BalanceConfirmXsd> banklist = new ArrayList<>();
	          for (Object[] linkbank : pojo) {
	              if (linkbank == null) {
	                  System.out.println("Null linkbank entry encountered, skipping...");
	                  continue;
	              }

	              BalanceConfirmXsd doclink = new BalanceConfirmXsd();

	              String formattedDate = null;
	              if (linkbank[0] != null) {
	                  java.util.Date date = (java.util.Date) linkbank[0];
	                  formattedDate = outputDateFormat.format(date);
	              } else {
	                  System.out.println("Null date encountered for a linkbank entry, skipping...");
	                  continue;
	              }

	              Long totalRecords = (linkbank[1] != null) ? (Long) linkbank[1] : 0L; // Default to 0 if null
	              Double totalAmountPaid = null;
	              if (linkbank[2] != null) {
	                  if (linkbank[2] instanceof String) {
	                      totalAmountPaid = Double.valueOf((String) linkbank[2]);
	                  } else if (linkbank[2] instanceof Double) {
	                      totalAmountPaid = (Double) linkbank[2];
	                  } else {
	                      throw new IllegalArgumentException("Unexpected type for totalAmountPaid: " + linkbank[2].getClass());
	                  }
	              } else {
	                  totalAmountPaid = 0.0; 
	              }

	              doclink.setDate(formattedDate);
	              doclink.setTotalRecords(totalRecords);
	              doclink.setTotalAmountPaid(totalAmountPaid);

	              banklist.add(doclink);
	          }

	          

	          responseXsd.setError(false);
	          responseXsd.setTotalSummary(banklist);
	          return responseXsd;

	      } catch (Exception e) {
	          e.printStackTrace();
	          responseXsd.setError(true);
	          responseXsd.setErrorCode(Constants.ECSV0001);
	          responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	          return responseXsd;
	      }
	}

	
	public static EntityResponce getDailyReportByDate (EntityRequest requestXsd) {

		EntityResponce responseXsd = new EntityResponce();

		List<BalanceConfirmPojo> pojoList = null;
		

		try {
			 DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = ServiceUtils.getTomorrowDate(requestXsd.getRequestDate());

			pojoList =	EjbLookUps.getBalanceConfirmRemote().getReportByDate(requestXsd.getAuditeePan(),requestXsd.getRequestDate(),endDate);
			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
				
			}else {
		            		
			    List<BalanceConfirmPojo> banklist = new ArrayList<>();

	            for (BalanceConfirmPojo linkbank : pojoList) {
	            		
	                BalanceConfirmPojo doclink = new BalanceConfirmPojo();

	                doclink.setTxnId(linkbank.getTxnId());
	                
	                doclink.setAuditeeName(linkbank.getAuditeeName());
	                doclink.setCreationDate(linkbank.getCreationDate());
	                doclink.setBank(linkbank.getBank());
	                doclink.setTotalAmount(linkbank.getTotalAmount());
	                doclink.setTaxInvNo(linkbank.getTaxInvNo());;
	                doclink.setPaymentMode(linkbank.getPaymentMode());
	            
					
	                banklist.add(doclink);
	            }
			}   
		                 responseXsd.setError(false);
		                // responseXsd.setBcDetails(pojoList);
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
	
	
}