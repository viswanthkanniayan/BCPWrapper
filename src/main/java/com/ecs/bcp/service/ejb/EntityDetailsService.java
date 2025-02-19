package com.ecs.bcp.service.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecs.bcp.pojo.BankApiMasterPojo;
import com.ecs.bcp.pojo.BankLinkagePojo;
import com.ecs.bcp.pojo.BankMasterPojo;
import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.EntityDetailspojoPk;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.CountResponseXsd;
import com.ecs.bcp.xsd.Entity;
import com.ecs.bcp.xsd.EntityDetails;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.bcp.xsd.UserRequest;
import com.ecs.bcp.xsd.UserResponce;

/**
 * @author VISWANTH KANNAIYAN
 */

public class EntityDetailsService {


	public static EntityResponce createEntity(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("Creating service");

			EntityDetailsPojo pojo1 =	EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXsd.getPan(), requestXsd.getEntityType());
			if(pojo1!=null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode("1001");
				responseXsd.setErrorDescription("Entity Already Exsist");
				return responseXsd;
			}

			EntityDetailsPojo pojo = new EntityDetailsPojo();
			EntityDetailspojoPk pkPojo = new EntityDetailspojoPk();

			pkPojo.setPan(requestXsd.getPan().trim());
			pkPojo.setEntityType(requestXsd.getEntityType().trim());

			pojo.setPkPojo(pkPojo);

			pojo.setCreationDate(requestXsd.getCreationDate());
			pojo.setAuditorType(requestXsd.getAuditorType());
			pojo.setInfoProviderType(requestXsd.getInfoProviderType());
			pojo.setCin(requestXsd.getCin());
			pojo.setLei(requestXsd.getLei());
			pojo.setCkycId(requestXsd.getCkycId());
			pojo.setRegId(requestXsd.getRegId());
			pojo.setName(requestXsd.getName());
			pojo.setLegalConstitution(requestXsd.getLegalConstitution());
			pojo.setStatus("ACTIVE");
			pojo.setFirmRegno(requestXsd.getFirmRegno());
			
			
//			if(requestXsd.getEntityType().equalsIgnoreCase("ADMIN")) {
//				pojo.setStatus("ACTIVE");
//			}else {
//				pojo.setStatus("PENDING");
//			}



			if(!ServiceUtils.isEmpty(requestXsd.getDateOfIncorporation())) {


				DateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				
				System.out.println("req date--->"+requestXsd.getDateOfIncorporation());

				Date date = inputDateFormat.parse(requestXsd.getDateOfIncorporation());
				String outputDate = outputDateFormat.format(date);

				//  System.out.println("Output date: " + outputDate);
				Date date2 = outputDateFormat.parse(outputDate);
				System.out.println("Parsed Date: " + date2);

				pojo.setDateOfIncorporation(date2);
			}

			pojo.setEmailId(requestXsd.getEmailId());
			pojo.setContactNo(requestXsd.getContactNo());
			pojo.setMobileNo(requestXsd.getMobileNo());
			pojo.setRegisteredAddress(requestXsd.getRegisteredAddress());
			pojo.setRegisteredPincode(requestXsd.getRegisteredPincode());
			pojo.setCommunicationAddress(requestXsd.getCommunicationAddress());
			pojo.setCommunicationPincode(requestXsd.getCommunicationPincode());
			pojo.setGstNo(requestXsd.getGstNo());
			pojo.setBillingAddress(requestXsd.getBillingAddress());
			pojo.setBillingPincode(requestXsd.getBillingPincode());
			pojo.setConsent(requestXsd.getConsent());
			//pojo.setPassword(requestXsd.getPassword());
			pojo.setPassword("Welcome@123");
            pojo.setState(requestXsd.getState());
            
            pojo.setDob(requestXsd.getDob());
            pojo.setEntityOrigin(requestXsd.getEntityOrigin());
            pojo.setScheduleBank(requestXsd.getScheduleBank());
            pojo.setRegEntityName(requestXsd.getRegEntityName());
            pojo.setEntityCategory(requestXsd.getEntityCategory());
            pojo.setStateCode(requestXsd.getStateCode());
            pojo.setAlterEmailId(requestXsd.getAlterEmailId());
            pojo.setSecondaryMobileNo(requestXsd.getSecondaryMobileNo());
            pojo.setDigitalSignUpload(requestXsd.getDigitalSignUpload());
            pojo.setSupportingDoc(requestXsd.getSupportingDoc());
            pojo.setEntityRegState(requestXsd.getEntityRegState());
            
			EjbLookUps.getEntityDetailsRemote().create(pojo);

			System.out.println("create----->>>>");

			responseXsd.setError(false);
			responseXsd.setErrorCode("");
			responseXsd.setErrorDescription("Entity Registered Successfully");
			responseXsd.setPan(requestXsd.getPan());
			responseXsd.setName(requestXsd.getName());
			responseXsd.setRegId(requestXsd.getRegId());
			responseXsd.setEntityType(requestXsd.getEntityType());
			responseXsd.setFirmRegno(requestXsd.getFirmRegno());
			
			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occured while creating");
			return responseXsd;
		}
	}

	public static EntityResponce updateEntity(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("update service");

			EntityDetailsPojo pojo1 =	EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXsd.getPan(), requestXsd.getEntityType());
			System.out.println("pojo--> "+pojo1);
			if(pojo1!=null) {

				EntityDetailsPojo pojo = new EntityDetailsPojo();
				EntityDetailspojoPk pkPojo = new EntityDetailspojoPk();

				pkPojo.setPan(requestXsd.getPan().trim());
				pkPojo.setEntityType(requestXsd.getEntityType().trim());

				pojo.setPkPojo(pkPojo);

				pojo.setCreationDate(requestXsd.getCreationDate());
				pojo.setAuditorType(requestXsd.getAuditorType());
				pojo.setInfoProviderType(requestXsd.getInfoProviderType());
				pojo.setCin(requestXsd.getCin());
				pojo.setFirmRegno(requestXsd.getFirmRegno());
				pojo.setLei(requestXsd.getLei());
				pojo.setCkycId(requestXsd.getCkycId());
				pojo.setRegId(requestXsd.getRegId());
				pojo.setName(requestXsd.getName());
				pojo.setLegalConstitution(requestXsd.getLegalConstitution());
				pojo.setEmailId(requestXsd.getEmailId());
				pojo.setContactNo(requestXsd.getContactNo());
				pojo.setMobileNo(requestXsd.getMobileNo());
				pojo.setRegisteredAddress(requestXsd.getRegisteredAddress());
				pojo.setRegisteredAddress(requestXsd.getRegisteredPincode());
				pojo.setCommunicationAddress(requestXsd.getCommunicationAddress());
				pojo.setCommunicationPincode(requestXsd.getCommunicationPincode());
				pojo.setGstNo(requestXsd.getGstNo());
				pojo.setBillingAddress(requestXsd.getBillingAddress());
				pojo.setBillingPincode(requestXsd.getBillingPincode());
				pojo.setConsent(requestXsd.getConsent());
				pojo.setPassword(requestXsd.getPassword());
				pojo.setStatus(requestXsd.getStatus());
                pojo.setState(requestXsd.getState());
                pojo.setDob(requestXsd.getDob());
                pojo.setEntityOrigin(requestXsd.getEntityOrigin());
                pojo.setScheduleBank(requestXsd.getScheduleBank());
                pojo.setRegEntityName(requestXsd.getRegEntityName());
                pojo.setEntityCategory(requestXsd.getEntityCategory());
                pojo.setStateCode(requestXsd.getStateCode());
                pojo.setAlterEmailId(requestXsd.getAlterEmailId());
                pojo.setSecondaryMobileNo(requestXsd.getSecondaryMobileNo());
                pojo.setDigitalSignUpload(requestXsd.getDigitalSignUpload());
                pojo.setSupportingDoc(requestXsd.getSupportingDoc());
                pojo.setEntityRegState(requestXsd.getEntityRegState());

				EjbLookUps.getEntityDetailsRemote().update(pojo);

				System.out.println("updated");

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
			responseXsd.setErrorDescription("Exception occured while Updating");
			return responseXsd;


		}
	}  


	public static EntityResponce getEntityDetailsbyPan (EntityRequest requestXsd) { 

		EntityResponce responseXsd = new EntityResponce();

		EntityDetailsPojo pojoList = null;


		try {

			pojoList =	EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXsd.getPan(), requestXsd.getEntityType());
			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}else {

				EntityDetails detailsPojo = new EntityDetails();

				detailsPojo.setPan(pojoList.getPkPojo().getPan());
				detailsPojo.setEntityType(pojoList.getPkPojo().getEntityType());
				detailsPojo.setCreationDate(pojoList.getCreationDate());
				detailsPojo.setAuditorType(pojoList.getAuditorType());
				detailsPojo.setInfoProviderType(pojoList.getInfoProviderType());
				detailsPojo.setCin(pojoList.getCin());
				detailsPojo.setFirmRegno(requestXsd.getFirmRegno());
				detailsPojo.setLei(pojoList.getLei());
				detailsPojo.setCkycId(pojoList.getCkycId());
				detailsPojo.setRegId(pojoList.getRegId());
				detailsPojo.setName(pojoList.getName());
				detailsPojo.setLegalConstitution(pojoList.getLegalConstitution());
				detailsPojo.setStatus(pojoList.getStatus());
				detailsPojo.setDateOfIncorporation(pojoList.getDateOfIncorporation());
				detailsPojo.setEmailId(pojoList.getEmailId());
				detailsPojo.setContactNo(pojoList.getContactNo());
				detailsPojo.setMobileNo(pojoList.getMobileNo());
				detailsPojo.setRegisteredAddress(pojoList.getRegisteredAddress());
				detailsPojo.setRegisteredAddress(pojoList.getRegisteredPincode());
				detailsPojo.setCommunicationAddress(pojoList.getCommunicationAddress());
				detailsPojo.setCommunicationPincode(pojoList.getCommunicationPincode());
				detailsPojo.setGstNo(pojoList.getGstNo());
				detailsPojo.setBillingAddress(pojoList.getBillingAddress());
				detailsPojo.setBillingPincode(pojoList.getBillingPincode());
				detailsPojo.setConsent(pojoList.getConsent());
				detailsPojo.setPassword(pojoList.getPassword());
				detailsPojo.setState(pojoList.getState());
				detailsPojo.setDob(pojoList.getDob());
				detailsPojo.setEntityOrigin(pojoList.getEntityOrigin());
				detailsPojo.setScheduleBank(pojoList.getScheduleBank());
				detailsPojo.setRegEntityName(pojoList.getRegEntityName());
				detailsPojo.setEntityCategory(pojoList.getEntityCategory());
				detailsPojo.setStateCode(pojoList.getStateCode());
				detailsPojo.setAlterEmailId(pojoList.getAlterEmailId());
				detailsPojo.setSecondaryMobileNo(pojoList.getSecondaryMobileNo());
				detailsPojo.setDigitalSignUpload(pojoList.getDigitalSignUpload());
				detailsPojo.setSupportingDoc(pojoList.getSupportingDoc());

				
				responseXsd.setEntityRequestById(detailsPojo);
				responseXsd.setError(false);
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

	public static EntityResponce findAllEntityRequester(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<EntityDetailsPojo> pojo = null;
		try {

			pojo = EjbLookUps.getEntityDetailsRemote().findAll();

			if(pojo==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0001);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				List<EntityDetails> detailsList = new ArrayList<EntityDetails>();
				for(EntityDetailsPojo pojoList : pojo) {

					EntityDetails detailPojo = new EntityDetails();

					detailPojo.setPan(pojoList.getPkPojo().getPan());
					detailPojo.setEntityType(pojoList.getPkPojo().getEntityType());
					detailPojo.setCreationDate(pojoList.getCreationDate());
					detailPojo.setAuditorType(pojoList.getAuditorType());
					detailPojo.setInfoProviderType(pojoList.getInfoProviderType());
					detailPojo.setCin(pojoList.getCin());
					detailPojo.setFirmRegno(pojoList.getFirmRegno());
					detailPojo.setLei(pojoList.getLei());
					detailPojo.setCkycId(pojoList.getCkycId());
					detailPojo.setRegId(pojoList.getRegId());
					detailPojo.setName(pojoList.getName());
					detailPojo.setLegalConstitution(pojoList.getLegalConstitution());
					detailPojo.setEmailId(pojoList.getEmailId());
					detailPojo.setContactNo(pojoList.getContactNo());
					detailPojo.setMobileNo(pojoList.getMobileNo());
					detailPojo.setRegisteredAddress(pojoList.getRegisteredAddress());
					detailPojo.setRegisteredPincode(pojoList.getRegisteredPincode());
					detailPojo.setCommunicationAddress(pojoList.getCommunicationAddress());
					detailPojo.setCommunicationPincode(pojoList.getCommunicationPincode());
					detailPojo.setGstNo(pojoList.getGstNo());
					detailPojo.setBillingAddress(pojoList.getBillingAddress());
					detailPojo.setBillingPincode(pojoList.getBillingPincode());
					detailPojo.setConsent(pojoList.getConsent());
					detailPojo.setStatus(pojoList.getStatus());	
					detailPojo.setRemark(pojoList.getRemark());	
				    detailPojo.setState(pojoList.getState());
				    detailPojo.setDob(pojoList.getDob());
				    detailPojo.setEntityOrigin(pojoList.getEntityOrigin());
				    detailPojo.setScheduleBank(pojoList.getScheduleBank());
				    detailPojo.setRegEntityName(pojoList.getRegEntityName());
				    detailPojo.setEntityCategory(pojoList.getEntityCategory());
				    detailPojo.setStateCode(pojoList.getStateCode());
				    detailPojo.setAlterEmailId(pojoList.getAlterEmailId());
				    detailPojo.setSecondaryMobileNo(pojoList.getSecondaryMobileNo());
				    detailPojo.setDigitalSignUpload(pojoList.getDigitalSignUpload());
				    detailPojo.setSupportingDoc(pojoList.getSupportingDoc());

					
					detailsList.add(detailPojo);

				}	

				responseXsd = new EntityResponce();

				responseXsd.setEntityRequestDetails(detailsList);
				responseXsd.setError(false);
				return responseXsd;
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorDescription("Exception occured");
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
			return responseXsd;
		}
	}
	public static EntityResponce checkMemberFirmRegNo(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
	    try {

	        /*if (!ServiceUtils.isEmpty(requestXsd.getMemberRegNo())) {
	            boolean isMemberRegNoExists = EjbLookUps.getEntityDetailsRemote().isMemberRegNoExists(requestXsd.getMemberRegNo());

	            if (isMemberRegNoExists) {
	                responseXsd.setError(true);
	                responseXsd.setErrorCode("1003");
	                responseXsd.setErrorDescription("Member Registration Number already exists");
	                return responseXsd;
	            }
	        }*/

	        if (!ServiceUtils.isEmpty(requestXsd.getFirmRegno())) {
	            boolean isFirmRegnoExists = EjbLookUps.getEntityDetailsRemote().isFirmRegnoExists(requestXsd.getFirmRegno());

	            if (isFirmRegnoExists) {
	                responseXsd.setError(true);
	                responseXsd.setErrorCode("1004");
	                responseXsd.setErrorDescription("Firm Registration Number already exists");
	                return responseXsd;
	            }
	        }

	        responseXsd.setError(false);
	        return responseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0001);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	        return responseXsd;
	    }
	}
	public static EntityResponce checkEntityName(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<EntityDetailsPojo> pojo = null;
		try {

			pojo = EjbLookUps.getEntityDetailsRemote().findByProperty("name", requestXSD.getName());

			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0013);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0013));
				return responseXsd;
			}
			else {
				responseXsd = new EntityResponce();
				responseXsd.setEmpDetailsList(pojo);
				responseXsd.setError(false);
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

	public static EntityResponce panCheck(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		EntityDetailsPojo pojo = null;
		try {

			pojo = EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXSD.getPan(), requestXSD.getEntityType());

			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0013);
				responseXsd.setEntityType(requestXSD.getEntityType());
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0013));
				return responseXsd;
			}
			else {
				responseXsd = new EntityResponce();
				responseXsd.setPan(pojo.getPkPojo().getPan());
				responseXsd.setName(pojo.getName());
				responseXsd.setRegId(pojo.getRegId());
				responseXsd.setFirmRegno(pojo.getFirmRegno());
				responseXsd.setEntityType(requestXSD.getEntityType());
				responseXsd.setError(false);
				return responseXsd;
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setEntityType(requestXSD.getEntityType());
			responseXsd.setFirmRegno(pojo.getFirmRegno());
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}


	public static EntityResponce auditeePanCheck(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		EntityDetailsPojo pojo = null;
		try {

			pojo = EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXSD.getPan(), "AUDITEE");
			if(pojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {
				
				System.out.println("entity type-->"+pojo.getPkPojo().getEntityType());
				if(pojo.getPkPojo().getEntityType().equals("AUDITEE")) {


					responseXsd = new EntityResponce();
					responseXsd.setPan(pojo.getPkPojo().getPan());
					responseXsd.setName(pojo.getName());
					responseXsd.setCin(pojo.getCin());
					responseXsd.setLei(pojo.getLei());
					responseXsd.setCkycId(pojo.getCkycId());
					responseXsd.setRegId(pojo.getRegId());
					//responseXsd.setFirmRegno(pojo.getFirmRegno());
					//responseXsd.setMemeber_reg_no(pojo.getmAlterEmailId());
					
					responseXsd.setError(false);
					return responseXsd;

				}else {

					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0008);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
					return responseXsd;
				}


			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}



	public static EntityResponce getEntityByProperty (EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<EntityDetailsPojo> EntityDetailsPojo = null;
		try {

			EntityDetailsPojo = EjbLookUps.getEntityDetailsRemote().findByProperty("emailId", requestXSD.getEmailId());

			if(EntityDetailsPojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				responseXsd = new EntityResponce();
				responseXsd.setEmpDetailsList(EntityDetailsPojo);
				responseXsd.setError(false);
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


	public static EntityResponce entityLogin(EntityRequest requestXSD) throws Exception {

		EntityResponce responseXsd = new EntityResponce();

		List<EntityDetailsPojo> dataPojo = null;


		try {

			dataPojo = EjbLookUps.getEntityDetailsRemote().findByProperty("emailId", requestXSD.getEmailId());


			if(dataPojo==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0003);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0003));
				return responseXsd;

			}


			if(dataPojo.get(0).getPassword().equals(requestXSD.getPassword())) {

				if(dataPojo.get(0).getStatus().equals("PENDING")) {

					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0015);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0015));
					return responseXsd;

				} else if ((dataPojo.get(0).getStatus().equals("ACTIVE"))) {


					responseXsd.setEmailId(dataPojo.get(0).getEmailId());
					responseXsd.setContactNo(dataPojo.get(0).getContactNo());
					responseXsd.setMobileNo(dataPojo.get(0).getMobileNo());
					responseXsd.setRegisteredAddress(dataPojo.get(0).getRegisteredAddress());
					responseXsd.setRegisteredAddress(dataPojo.get(0).getRegisteredPincode());
					responseXsd.setCommunicationAddress(dataPojo.get(0).getCommunicationAddress());
					responseXsd.setCommunicationPincode(dataPojo.get(0).getCommunicationPincode());
					responseXsd.setGstNo(dataPojo.get(0).getGstNo());
					responseXsd.setBillingAddress(dataPojo.get(0).getBillingAddress());
					responseXsd.setBillingPincode(dataPojo.get(0).getBillingPincode());
					responseXsd.setConsent(dataPojo.get(0).getConsent());


					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0017));
					return responseXsd;

				} else if ((dataPojo.get(0).getStatus().equals("DEACTIVE"))) {
					responseXsd = new EntityResponce();
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0018);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0018));
					return responseXsd;

				}
				else if ((dataPojo.get(0).getStatus().equals("REJECT"))) {
					responseXsd = new EntityResponce();
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0019);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0019));
					return responseXsd;

				}else {

				}
			}else {
				System.out.println("password not match");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0003);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0003));
				return responseXsd;
			}


		}
		catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
		return responseXsd;

	}


	public static EntityResponce findAllBankMaster(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<BankMasterPojo> BankMasterPojo = null;
		try {

			BankMasterPojo = EjbLookUps.getBankMasterRemote().findAll();

			if(BankMasterPojo==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0001);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				responseXsd = new EntityResponce();

				responseXsd.setBankMasterDetails(BankMasterPojo);
				responseXsd.setError(false);
				return responseXsd;
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorDescription("Exception occured");
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
			return responseXsd;
		}
	}

	public static EntityResponce approveRejectEntity(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();

		EntityDetailsPojo pojoList = null;

		try {
			//pojoList = EjbLookUps.getEntityDetailsRemote().findById(requestXsd.getPan());

			pojoList =	EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXsd.getPan(),requestXsd.getEntityType());
			
			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {

				EntityDetailsPojo pojo = new EntityDetailsPojo();
				EntityDetailspojoPk pkPojo = new EntityDetailspojoPk();

				if (requestXsd.getStatus().equals("ACTIVE")) {

					pkPojo.setPan(requestXsd.getPan());
                    pkPojo.setEntityType(requestXsd.getEntityType());
                    
					pojo.setPkPojo(pkPojo);
					pojo.setStatus("ACTIVE");
					pojo.setRemark(requestXsd.getRemark());
					EjbLookUps.getEntityDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);
					responseXsd.setErrorDescription("Approved successfully");
					return responseXsd;
				} else if (requestXsd.getStatus().equals("REJECT"))

				{
					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setEntityType(requestXsd.getEntityType());
					
					pojo.setPkPojo(pkPojo);
					pojo.setStatus(requestXsd.getStatus());
					pojo.setRemark(requestXsd.getRemark());

					EjbLookUps.getEntityDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0019);
					responseXsd.setErrorDescription("Reject successfully");
					return responseXsd;
				}
				else if (requestXsd.getStatus().equals("DEACTIVE"))
				{
					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setEntityType(requestXsd.getEntityType());
					
					pojo.setPkPojo(pkPojo);
					pojo.setStatus(requestXsd.getStatus());
					pojo.setRemark(requestXsd.getRemark());

					EjbLookUps.getEntityDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0018);
					responseXsd.setErrorDescription("Deactive Successfully");
					return responseXsd;
				}
				else {
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0016);
					responseXsd.setErrorDescription("Invalid Status");
					return responseXsd;

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}	



	public static EntityResponce getAllBankList(EntityRequest requestXSD) throws Exception {
		EntityResponce responseXsd  = new EntityResponce();
		List<EntityDetailsPojo> pojoList = null;
		try {

			pojoList = EjbLookUps.getEntityDetailsRemote().findByEntityType("INFORMATION_PROVIDER");

			if(pojoList==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0001);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {	

				List<EntityDetails> dtlList = new ArrayList<>();

				for(EntityDetailsPojo pojo : pojoList) {

					EntityDetails dtlPojo = new EntityDetails();

					dtlPojo.setName(pojo.getName());
					dtlPojo.setEntityType(pojo.getPkPojo().getEntityType());
					dtlPojo.setPan(pojo.getPkPojo().getPan());

					dtlList.add(dtlPojo);


				}

				responseXsd = new EntityResponce();
				responseXsd.setEntityRequestDetails(dtlList);
				responseXsd.setError(false);
				return responseXsd;
			}


		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorDescription("Exception occured");
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
			return responseXsd;
		}
	}



	/*public static UserResponce distinctEntityName(UserRequest requestXsd) throws Exception {

	    UserResponce responseXsd = new UserResponce();
	    try {

	      List<String> pojo1 = EjbLookUps.getEntityDetailsRemote().findDistinctEntity("AUDITOR");

	      if(pojo1==null) {
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0014);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0014));
	        return responseXsd;
	      }
	      else {
	        responseXsd.setError(false);
	        responseXsd.setEntityNameList(pojo1);
	        
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
	*/
	
	/*public static UserResponce distinctEntityName(UserRequest requestXsd) throws Exception {
		   
		UserResponce responseXsd = new UserResponce();
	//	List<EntityDetailsPojo> pojoList = null;
		try {
        
			 List<String> pojoList = EjbLookUps.getEntityDetailsRemote().findDistinctEntity("AUDITOR");
	     
			if(pojoList==null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else 
			 {
				List<UserDetails> dtlList = new ArrayList<>();
				
				for(EntityDetailsPojo pojo : pojoList) {
					
					UserDetails addPojo = new UserDetails();
					
					addPojo.setName(pojo.getName());
					addPojo.setFirmRegno(pojo.getFirmRegno());
			         
					dtlList.add(addPojo);
					
				}
				
				responseXsd.setError(false);
				responseXsd.setUserDetailList(dtlList);
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
*/
	/*public static UserResponce distinctEntityName(UserRequest requestXsd) throws Exception {
	    UserResponce responseXsd = new UserResponce();
	    try {
	        List<String> pojo1 = EjbLookUps.getEntityDetailsRemote().findDistinctEntity("AUDITOR");

	        if (pojo1 == null || pojo1.isEmpty()) {
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0014);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0014));
	            return responseXsd;
	        } else {
	            List<Entity> entityList = new ArrayList<>();
	            for (String entityString : pojo1) {
	                // Assuming entityString is formatted as "name, firmRegNo"
	                String[] parts = entityString.split(","); // Adjust based on actual format
	                if (parts.length == 2) { // Ensure we have both name and firmRegNo
	                    String name = parts[0].trim();
	                    String firmRegNo = parts[1].trim();
	                    entityList.add(new Entity(name, firmRegNo));
	                }
	            }
	            responseXsd.setError(false);
	            responseXsd.setEntity(entityList);
	            return responseXsd;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0001);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	        return responseXsd;
	    }
	}*/

	public static UserResponce distinctEntityName(UserRequest requestXsd) throws Exception {
	    UserResponce responseXsd = new UserResponce();
	    try {
	        // Assume this returns List<Object[]>
	        List<Object[]> pojo1 = EjbLookUps.getEntityDetailsRemote().findDistinctEntity("AUDITOR");

	        if (pojo1 == null || pojo1.isEmpty()) {
	            responseXsd.setError(true);
	            responseXsd.setErrorCode(Constants.ECSV0014);
	            responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0014));
	            return responseXsd;
	        } else {
	            List<Entity> entityList = new ArrayList<>();
	            for (Object[] entityArray : pojo1) {
	                if (entityArray.length == 2) { // Ensure it has the correct number of elements
	                    String name = (String) entityArray[0]; // Cast to String
	                    String firmRegNo = (String) entityArray[1]; // Cast to String
	                    entityList.add(new Entity(name, firmRegNo)); // Create Entity and add it to the list
	                } else {
	                    // Handle case where entityArray doesn't have the expected number of elements
	                    // Log or throw an exception if necessary
	                }
	            }
	            responseXsd.setError(false);
	            responseXsd.setEntity(entityList);
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

	public static EntityResponce auditeeDetailsByName(EntityRequest reqXSD)throws Exception {
			

		EntityResponce responseXsd = new EntityResponce();
		EntityDetailsPojo pojoList = null;

		try {

			pojoList = EjbLookUps.getEntityDetailsRemote().auditeeDetailsByName(reqXSD.getName(),"AUDITEE");

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;

			}else {

				EntityDetails dtlPojo = new EntityDetails();

				dtlPojo.setCin(pojoList.getCin());
				dtlPojo.setLei(pojoList.getLei());
				dtlPojo.setName(pojoList.getName());
				dtlPojo.setPan(pojoList.getPkPojo().getPan());

				responseXsd.setError(false);
				responseXsd.setAuditeeDetailsByName(dtlPojo);

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

	public static EntityResponce getEntityDetails(EntityRequest requestXsd) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		List<EntityDetailsPojo> pojoList = null;
		try {

			pojoList = EjbLookUps.getEntityDetailsRemote().getDetailsByPan(requestXsd.getPan(), requestXsd.getEntityType());

			if(pojoList==null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<EntityDetails> dtlList = new ArrayList<>();

				for
				(EntityDetailsPojo pojo : pojoList) {

					EntityDetails dtlPojo = new EntityDetails();
					
					dtlPojo.setAuditorType(pojo.getAuditorType());
					dtlPojo.setInfoProviderType(pojo.getInfoProviderType());
					dtlPojo.setCin(pojo.getCin());
					dtlPojo.setLei(pojo.getLei());
					dtlPojo.setCkycId(pojo.getCkycId());
					dtlPojo.setRegId(pojo.getRegId());
					dtlPojo.setName(pojo.getName());
					dtlPojo.setLegalConstitution(pojo.getLegalConstitution());
					dtlPojo.setEmailId(pojo.getEmailId());
					dtlPojo.setContactNo(pojo.getContactNo());
					dtlPojo.setMobileNo(pojo.getMobileNo());
					dtlPojo.setRegisteredAddress(pojo.getRegisteredAddress());
					dtlPojo.setRegisteredAddress(pojo.getRegisteredPincode());
					dtlPojo.setCommunicationAddress(pojo.getCommunicationAddress());
					dtlPojo.setCommunicationPincode(pojo.getCommunicationPincode());
					dtlPojo.setGstNo(pojo.getGstNo());
					dtlPojo.setBillingAddress(pojo.getBillingAddress());
					dtlPojo.setBillingPincode(pojo.getBillingPincode());
					dtlPojo.setConsent(pojo.getConsent());
					dtlPojo.setStatus(pojo.getStatus());	
					dtlPojo.setPassword(pojo.getPassword());	
					dtlPojo.setRemark(pojo.getRemark());	
					dtlPojo.setState(pojo.getState());
					dtlPojo.setDob(pojo.getDob());	
					dtlPojo.setEntityOrigin(pojo.getEntityOrigin());	
					dtlPojo.setScheduleBank(pojo.getScheduleBank());	
					dtlPojo.setRegEntityName(pojo.getRegEntityName());	
					dtlPojo.setEntityCategory(pojo.getEntityCategory());	
					dtlPojo.setStateCode(pojo.getStateCode());	
					dtlPojo.setAlterEmailId(pojo.getAlterEmailId());	
					dtlPojo.setSecondaryMobileNo(pojo.getSecondaryMobileNo());	
					dtlPojo.setDigitalSignUpload(pojo.getDigitalSignUpload());	
					dtlPojo.setEntityRegState(pojo.getEntityRegState());
					dtlPojo.setPan(pojo.getPkPojo().getPan());
					dtlPojo.setEntityType(pojo.getPkPojo().getEntityType());
					dtlPojo.setFirmRegno(pojo.getFirmRegno());	
					dtlPojo.setDateOfIncorporation(pojo.getDateOfIncorporation());
					
					dtlList.add(dtlPojo);
				}

				responseXsd.setError(false);
				responseXsd.setEntityRequestDetails(dtlList);
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

	public static EntityResponce getBankOtpFlg(EntityRequest reqXSD) throws Exception {
			

		EntityResponce responseXsd = new EntityResponce();
		BankApiMasterPojo pojoList = null;


		try {

			pojoList = EjbLookUps.getBankApiMasterRemote().findById(reqXSD.getBankName());
 
			if (pojoList == null ) {

				responseXsd = new EntityResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				responseXsd.setErrorDescription("Exception occured while Updating");
				return responseXsd;
			} else {

				responseXsd = new EntityResponce();
				responseXsd.setError(false);
				responseXsd.setBankApiMaster(pojoList);
				return responseXsd;

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new EntityResponce();
			responseXsd.setError(true);
			responseXsd.setErrorDescription("BANK NOT REGISTRED");
			return responseXsd;
		}
	}
	
	
public static EntityResponce getEntitytotalCount() throws Exception {
		
	EntityResponce responseXsd = new EntityResponce();
	    
	    try {
	        long totalEntityCount = EjbLookUps.getEntityDetailsRemote().getTotalEntityCount();

	        System.out.println("Total entity: " + totalEntityCount);

	        responseXsd.setError(false);
	        
	        responseXsd.setTotalEntityCount(totalEntityCount);

	        return responseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0001);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	        return responseXsd;
	    }
	} 
public static EntityResponce getEntityStatusCount(EntityRequest reqXSD) throws Exception {

	EntityResponce responseXsd = new EntityResponce();
	try {

		//long entityTotalCount;

		//user count
		long activeUser = EjbLookUps.getEntityDetailsRemote().entityStatusCount("ACTIVE");
		long deActiveUser = EjbLookUps.getEntityDetailsRemote().entityStatusCount("DEACTIVE");

		System.out.println("activeUser-----------------------------"+activeUser);
		System.out.println("deActiveUser-----------------------------"+deActiveUser);

		//entityTotalCount =  activeUser + deActiveUser;


		responseXsd = new EntityResponce();
		responseXsd.setError(false);


		responseXsd.setActiveEntity(activeUser);
		responseXsd.setDeActiveEntity(deActiveUser);


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
public static EntityResponce getentityTypeCount(EntityRequest reqXSD) throws Exception {

	EntityResponce responseXsd = new EntityResponce();
	long entityTotalCount;
	try {


		long auditor = EjbLookUps.getEntityDetailsRemote().entityTypeCount("AUDITOR");
		long auditee = EjbLookUps.getEntityDetailsRemote().entityTypeCount("AUDITEE");
		long infoProvider = EjbLookUps.getEntityDetailsRemote().entityTypeCount("INFORMATION_PROVIDER");
		long setAuditorActiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("AUDITOR","ACTIVE"); 
        long setAuditordeActiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("AUDITOR","DEACTIVE"); 
        
        long setAuditeeActiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("AUDITEE","ACTIVE"); 
        long setAuditeeDeActiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("AUDITEE","DEACTIVE"); 
        
        long setInfoActiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("INFORMATION_PROVIDER","ACTIVE"); 
        long setInfoDeactiveEntity = EjbLookUps.getEntityDetailsRemote().entityTypeStatusCount("INFORMATION_PROVIDER","DEACTIVE"); 
       


        entityTotalCount =  setAuditorActiveEntity + setAuditordeActiveEntity+setAuditeeActiveEntity+setAuditeeDeActiveEntity+setInfoActiveEntity+setInfoDeactiveEntity;
	
		System.out.println("activeUser-----------------------------"+auditor);
		System.out.println("deActiveUser-----------------------------"+auditee);
		System.out.println("infoProvider-----------------------------"+infoProvider);



		responseXsd = new EntityResponce();
		responseXsd.setError(false);


		responseXsd.setEntityAuditorCount(auditor);
		responseXsd.setAuditeeCount(auditee);
		responseXsd.setInfoProvider(infoProvider);
		
		responseXsd.setAuditorActiveUser(setAuditorActiveEntity);
	    responseXsd.setAuditordeActiveUser(setAuditordeActiveEntity);
	    
	    responseXsd.setAuditeeActiveUser(setAuditeeActiveEntity);
	    responseXsd.setAuditeeDeActiveUser(setAuditeeDeActiveEntity);
	    
	    responseXsd.setInfoActiveUser(setInfoActiveEntity);
	    responseXsd.setInfoDeActiveUser(setInfoDeactiveEntity);
	   
	    responseXsd.setEntityTotalCount(entityTotalCount);
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


}
