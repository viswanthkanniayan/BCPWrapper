package com.ecs.bcp.service.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.pojo.UserDetailsPojoPk;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.CountResponseXsd;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.bcp.xsd.UserDetails;
import com.ecs.bcp.xsd.UserRequest;
import com.ecs.bcp.xsd.UserResponce;


import java.util.Random;
/**
 * @author VISWANTH KANNAIYAN
 */
public class UserDetailsService {



	public static String genRandNo(int digit){
		String characters = "0123456789";
		String otp = RandomStringUtils.random(digit, characters);
		return otp;
	}



	public static UserResponce createuser(UserRequest requestXsd) throws Exception {
		UserResponce responseXsd = new UserResponce();
		try {
			if (requestXsd == null || ServiceUtils.isEmpty(requestXsd.getPan()) 
					|| ServiceUtils.isEmpty(requestXsd.getUserType()) 
					|| ServiceUtils.isEmpty(requestXsd.getEntityName())) {
				responseXsd.setError(true);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("No Data Found");
				return responseXsd;
			}

			UserDetailsPojo pojo1 = EjbLookUps.getUserDetailsRemote()
					.getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(), requestXsd.getEntityName());
			if (pojo1 != null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode("1001");
				responseXsd.setErrorDescription("User Already Exists");
				return responseXsd;
			}

			if (!ServiceUtils.isEmpty(requestXsd.getMemberRegNo())) {
				boolean isMemberRegNoExists = EjbLookUps.getUserDetailsRemote()
						.isMemberRegNoExistsForAuditor(requestXsd.getMemberRegNo(), requestXsd.getFirmRegno());

				if (isMemberRegNoExists) {
					responseXsd.setError(true);
					responseXsd.setErrorCode("1003");
					responseXsd.setErrorDescription("Member Registration Number already exists");
					return responseXsd;
				}
			}

			if (!ServiceUtils.isEmpty(requestXsd.getFirmRegno())) {
				boolean isFirmRegnoExists = EjbLookUps.getUserDetailsRemote().isFirmRegnoExists(requestXsd.getFirmRegno());

				if (isFirmRegnoExists) {
					responseXsd.setError(true);
					responseXsd.setErrorCode("1004");
					responseXsd.setErrorDescription("Firm Registration Number already exists");
					return responseXsd;
				}
			}


			// Create new user details
			UserDetailsPojo pojo = new UserDetailsPojo();
			UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

			pkPojo.setPan(requestXsd.getPan().trim());
			pkPojo.setUserType(requestXsd.getUserType().trim());
			pkPojo.setEntityName(requestXsd.getEntityName());

			pojo.setPkPojo(pkPojo);
			pojo.setRegNo(requestXsd.getRegNo());
			pojo.setName(requestXsd.getName());
			pojo.setDesignation(requestXsd.getDesignation());

			if (!ServiceUtils.isEmpty(requestXsd.getDob())) {
				DateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = inputDateFormat.parse(requestXsd.getDob());
				pojo.setDob(new SimpleDateFormat("dd-MM-yyyy").format(date));
			}

			pojo.setEmailId(requestXsd.getEmailId());
			pojo.setMobileNo(requestXsd.getMobileNo());
			pojo.setConsent(requestXsd.getConsent());
			//pojo.setPassword("Welcome@123");
			String password = generatePassword.passwordGenerator();
			System.out.println("password : "+password);
			pojo.setPassword(password);
			pojo.setEntityPan(requestXsd.getEntityPan());
			pojo.setEntityRegNo(requestXsd.getEntityRegNo());
			pojo.setStatus("ACTIVE");
			pojo.setPasscodeCount("0"); 
			pojo.setState(requestXsd.getState());
			pojo.setEmployeecode(requestXsd.getEmployeecode());
			pojo.setMemberRegNo(requestXsd.getMemberRegNo());
			pojo.setFirmRegno(requestXsd.getFirmRegno());
			pojo.setDigitalSignatureUpload(requestXsd.getDigitalSignatureUpload());
			pojo.seteSignStatus("PENDING");
			pojo.seteSignTxnId(requestXsd.geteSignTxnId());

			if ("SUCCESS".equalsIgnoreCase(requestXsd.getMobileOptStatus())) {
				pojo.setMobileOptStatus("SUCCESS");
			} else {
				pojo.setMobileOptStatus("PENDING");
			}

			if ("SUCCESS".equalsIgnoreCase(requestXsd.getEmailOtpStatus())) {
				pojo.setEmailOtpStatus("SUCCESS");
			} else {
				pojo.setEmailOtpStatus("PENDING");
			}

			EjbLookUps.getUserDetailsRemote().create(pojo);


			// send SMS
			SmsOtpService.sendUserPasswordSms(requestXsd.getMobileNo(), password);


			System.out.println("SMS sent-----");

			responseXsd.setError(false);
			responseXsd.setErrorCode("1002");
			responseXsd.setErrorDescription("Created Successfully");
			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occurred while creating user");
			return responseXsd;
		}
	}


	public static UserResponce adduser(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {
			UserDetailsPojo pojo1 =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );


			if (requestXsd == null || ServiceUtils.isEmpty(requestXsd.getPan()) || ServiceUtils.isEmpty(requestXsd.getUserType())|| ServiceUtils.isEmpty(requestXsd.getEntityName())) {
				responseXsd.setError(true);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("No Data Found Or primary key missing");
				return responseXsd;
			}



			if (pojo1 != null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode("1001");
				responseXsd.setErrorDescription("User Already Exists");
				return responseXsd;
			}

			UserDetailsPojo pojo = new UserDetailsPojo();
			UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

			pkPojo.setPan(requestXsd.getPan().trim());
			pkPojo.setUserType(requestXsd.getUserType().trim());
			pkPojo.setEntityName(requestXsd.getEntityName());

			pojo.setPkPojo(pkPojo);


			pojo.setRegNo(requestXsd.getRegNo());
			pojo.setName(requestXsd.getName());
			pojo.setDesignation(requestXsd.getDesignation());

			if (!ServiceUtils.isEmpty(requestXsd.getDob())) {
				DateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = inputDateFormat.parse(requestXsd.getDob());
				pojo.setDob(new SimpleDateFormat("dd-MM-yyyy").format(date));
			}

			pojo.setEmailId(requestXsd.getEmailId());
			pojo.setMobileNo(requestXsd.getMobileNo());
			pojo.setConsent(requestXsd.getConsent());
			pojo.setPassword("Welcome@123");
			pojo.setEntityPan(requestXsd.getEntityPan());
			pojo.setEntityRegNo(requestXsd.getEntityRegNo());
			pojo.setStatus("ACTIVE"); // Default status
			pojo.setState(requestXsd.getState());
			pojo.setEmployeecode(requestXsd.getEmployeecode());
			pojo.setMemberRegNo(requestXsd.getMemberRegNo());
			pojo.setFirmRegno(requestXsd.getFirmRegno());
			pojo.setDigitalSignatureUpload(requestXsd.getDigitalSignatureUpload());
			pojo.seteSignStatus("PENDING");
			pojo.seteSignTxnId(requestXsd.geteSignTxnId());
			pojo.setEmailUniqueLink(requestXsd.getEmailUniqueLink());
			pojo.setPasscodeCount("0"); 



			if ("SUCCESS".equalsIgnoreCase(requestXsd.getMobileOptStatus())) {
				pojo.setMobileOptStatus("SUCCESS");
			} else {
				pojo.setMobileOptStatus("PENDING");
			}

			if ("SUCCESS".equalsIgnoreCase(requestXsd.getEmailOtpStatus())) {
				pojo.setEmailOtpStatus("SUCCESS");
			} else {
				pojo.setEmailOtpStatus("PENDING");
			}

			EjbLookUps.getUserDetailsRemote().create(pojo);

			System.out.println("EmailUniqueLink created successfully.");

			responseXsd.setError(false);
			responseXsd.setErrorCode("");
			responseXsd.setErrorDescription("User Registered Successfully");
			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occurred while creating user");
			return responseXsd;
		}
	}

	public static UserResponce updateuser(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("update service");

			UserDetailsPojo pojo1 =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );

			System.out.println("pojo--> "+pojo1);
			if(pojo1!=null) {

				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				pkPojo.setPan(requestXsd.getPan());
				pkPojo.setUserType(requestXsd.getUserType());
				pkPojo.setEntityName(requestXsd.getEntityName());
				pojo.setPkPojo(pkPojo);

				pojo.setRegNo(requestXsd.getRegNo());
				pojo.setName(requestXsd.getName());
				pojo.setDesignation(requestXsd.getDesignation());
				if(!ServiceUtils.isEmpty(requestXsd.getDob())) {

					DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

					System.out.println("req date--->"+requestXsd.getDob());

					Date date = inputDateFormat.parse(requestXsd.getDob());
					String outputDate = outputDateFormat.format(date);

					//  System.out.println("Output date: " + outputDate);
					Date date3 = outputDateFormat.parse(outputDate);
					System.out.println("Parsed Date: " + date3);

					pojo.setDob(outputDate);
				}

				pojo.setEmailId(requestXsd.getEmailId());
				pojo.setMobileNo(requestXsd.getMobileNo());
				pojo.setConsent(requestXsd.getConsent());
				pojo.setEntityPan(requestXsd.getEntityPan());
				pojo.setStatus(requestXsd.getStatus());
				pojo.setState(requestXsd.getState());
				pojo.setEmployeecode(requestXsd.getEmployeecode());
				pojo.setMemberRegNo(requestXsd.getMemberRegNo());
				pojo.setFirmRegno(requestXsd.getFirmRegno());
				pojo.setDigitalSignatureUpload(requestXsd.getDigitalSignatureUpload());
				pojo.seteSignStatus(requestXsd.geteSignStatus());

				EjbLookUps.getUserDetailsRemote().update(pojo);



				System.out.println("updated");


				responseXsd.setError(false);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("Update Successfully");
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
	public static UserResponce updateLogoutTime(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("update service");

			UserDetailsPojo pojo1 =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );

			System.out.println("pojo--> "+pojo1);
			if(pojo1!=null) {

				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				pkPojo.setPan(requestXsd.getPan());
				pkPojo.setUserType(requestXsd.getUserType());
				pkPojo.setEntityName(requestXsd.getEntityName());
				pojo.setPkPojo(pkPojo);

				pojo.setLogoutDate(new Date());


				EjbLookUps.getUserDetailsRemote().update(pojo);



				System.out.println("Logout Time updated");


				responseXsd.setError(false);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription(" Logout Time Update Successfully");
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


	public static UserResponce getuserDetailsById (UserRequest requestXsd) {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo pojoList = null;

		try {

			pojoList =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );

			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;

			}else {

				UserDetails detailPojo = new UserDetails();

				detailPojo.setPan(pojoList.getPkPojo().getPan());
				detailPojo.setUserType(pojoList.getPkPojo().getUserType());
				detailPojo.setEntityName(pojoList.getPkPojo().getEntityName());
				detailPojo.setEntityPan(pojoList.getEntityPan());

				detailPojo.setPan(pojoList.getPkPojo().getPan());
				detailPojo.setUserType(pojoList.getPkPojo().getUserType());
				detailPojo.setEntityName(pojoList.getPkPojo().getEntityName());
				detailPojo.setRegNo(pojoList.getRegNo());
				detailPojo.setName(pojoList.getName());
				detailPojo.setDesignation(pojoList.getDesignation());
				detailPojo.setDob(pojoList.getDob());
				detailPojo.setEmailId(pojoList.getEmailId());
				detailPojo.setMobileNo(pojoList.getMobileNo());
				detailPojo.setConsent(pojoList.getConsent());
				detailPojo.setEntityPan(pojoList.getEntityPan());
				detailPojo.setStatus(pojoList.getStatus());
				detailPojo.setMemberRegNo(pojoList.getMemberRegNo());
				detailPojo.setEmployeecode(pojoList.getEmployeecode());

				detailPojo.setStatus(pojoList.getStatus());
				responseXsd.setError(false);
				responseXsd.setUserDetail(detailPojo);

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


	public static UserResponce getAuditorByRegNo(UserRequest requestXsd) {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo pojoList = null;

		try {

			pojoList =	EjbLookUps.getUserDetailsRemote().getAuditorByRegNo(requestXsd.getUserType(), requestXsd.getMemberRegNo(), requestXsd.getEntityName());

			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}else {

				UserDetails detailPojo = new UserDetails();

				detailPojo.setPan(pojoList.getPkPojo().getPan());
				detailPojo.setUserType(pojoList.getPkPojo().getUserType());
				detailPojo.setEntityName(pojoList.getPkPojo().getEntityName());
				detailPojo.setRegNo(pojoList.getRegNo());
				detailPojo.setName(pojoList.getName());
				detailPojo.setDesignation(pojoList.getDesignation());
				detailPojo.setDob(pojoList.getDob());
				detailPojo.setEmailId(pojoList.getEmailId());
				detailPojo.setMobileNo(pojoList.getMobileNo());
				detailPojo.setConsent(pojoList.getConsent());
				detailPojo.setEntityPan(pojoList.getEntityPan());
				detailPojo.setStatus(pojoList.getStatus());
				detailPojo.setMemberRegNo(pojoList.getMemberRegNo());
				detailPojo.setFirmRegno(pojoList.getFirmRegno());

				responseXsd.setError(false);
				responseXsd.setUserDetail(detailPojo);

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

	public static UserResponce findAllUser(UserRequest requestXSD) throws Exception {
		UserResponce responseXsd  = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findAll();

			if(userPojo==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0001);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {

				List<UserDetails> detailsList = new ArrayList<UserDetails>();

				for(UserDetailsPojo pojoList : userPojo) {

					UserDetails detailPojo = new UserDetails();

					detailPojo.setPan(pojoList.getPkPojo().getPan());
					detailPojo.setUserType(pojoList.getPkPojo().getUserType());
					detailPojo.setEntityName(pojoList.getPkPojo().getEntityName());
					detailPojo.setRegNo(pojoList.getRegNo());
					detailPojo.setName(pojoList.getName());
					detailPojo.setDesignation(pojoList.getDesignation());
					detailPojo.setDob(pojoList.getDob());
					detailPojo.setEmailId(pojoList.getEmailId());
					detailPojo.setMobileNo(pojoList.getMobileNo());
					detailPojo.setConsent(pojoList.getConsent());
					detailPojo.setEntityPan(pojoList.getEntityPan());
					detailPojo.setStatus(pojoList.getStatus());
					detailPojo.setMemberRegNo(pojoList.getMemberRegNo());
					detailPojo.setFirmRegno(pojoList.getFirmRegno());

					detailsList.add(detailPojo);

				}

				responseXsd = new UserResponce();
				responseXsd.setUserDetailList(detailsList);
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


	public static UserResponce getfindByProperty (UserRequest requestXSD) throws Exception {
		UserResponce responseXsd  = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("emailId", requestXSD.getEmailId());

			if(userPojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				responseXsd = new UserResponce();
				responseXsd.setUserDetailsList(userPojo);
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


	public static UserResponce getAuiteeDetailsByPan (UserRequest requestXSD) throws Exception {
		UserResponce responseXsd  = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("entityPan", requestXSD.getEntityPan());

			if(userPojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				List<UserDetails> list = new ArrayList<>();
				for(UserDetailsPojo pojo : userPojo) {

					UserDetails userDetails = new UserDetails();


					userDetails.setName(pojo.getName());
					userDetails.setRegNo(pojo.getRegNo());
					userDetails.setDesignation(pojo.getDesignation());
					userDetails.setDob(pojo.getDob());
					userDetails.setEmailId(pojo.getEmailId());
					userDetails.setMobileNo(pojo.getMobileNo());
					userDetails.setStatus(pojo.getStatus());
					userDetails.setUserType(pojo.getPkPojo().getUserType());
					userDetails.setEntityName(pojo.getPkPojo().getEntityName());
					userDetails.setEntityPan(pojo.getEntityPan());
					userDetails.setPan(pojo.getPkPojo().getPan());
					userDetails.setMemberRegNo(pojo.getMemberRegNo());
					userDetails.setFirmRegno(pojo.getFirmRegno());

					list.add(userDetails);
				}

				responseXsd = new UserResponce();
				//				responseXsd.setUserDetailsList(list);
				responseXsd.setUserDetailList(list);
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



	public static UserResponce checkUserPanType(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {

			UserDetailsPojo pojo1 =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName());
			if(pojo1!=null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0014);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0014));
				return responseXsd;
			}
			else {
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

	public static UserResponce checkMemberFirmRegNo(UserRequest requestXsd) throws Exception {
		UserResponce responseXsd = new UserResponce();
		try {
			boolean isMemberRegNoExistsForAuditor = false;
			boolean isFirmRegnoExists = false;

			// Check MemberRegNo condition
			if (!ServiceUtils.isEmpty(requestXsd.getMemberRegNo())) {
				isMemberRegNoExistsForAuditor = EjbLookUps.getUserDetailsRemote()
						.isMemberRegNoExistsForAuditor(requestXsd.getMemberRegNo(), requestXsd.getFirmRegno());

				if (isMemberRegNoExistsForAuditor) {
					responseXsd.setError(true);
					responseXsd.setErrorCode("1003");
					responseXsd.setErrorDescription("Member Registration Number is already registered for this Frn Number");
					return responseXsd;
				}
			}

			// Check FirmRegno condition
			if (!ServiceUtils.isEmpty(requestXsd.getFirmRegno())) {
				isFirmRegnoExists = EjbLookUps.getUserDetailsRemote().isFirmRegnoExists(requestXsd.getFirmRegno());
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



	/*	public static UserResponce checkMemberFirmRegNo(UserRequest requestXsd) throws Exception {
	    UserResponce responseXsd = new UserResponce();
	    try {
	        boolean isMemberRegNoExists = false;
	        boolean isFirmRegnoExists = false;

	        if (!ServiceUtils.isEmpty(requestXsd.getMemberRegNo())) {
	            isMemberRegNoExists = EjbLookUps.getUserDetailsRemote().isMemberRegNoExists(requestXsd.getMemberRegNo());
	            if (isMemberRegNoExists) {
	                responseXsd.setError(true);
	                responseXsd.setErrorCode("1003");
	                responseXsd.setErrorDescription("Member Registration Number already exists");
	                return responseXsd;
	            }
	        }

	        if (!ServiceUtils.isEmpty(requestXsd.getFirmRegno())) {
	            isFirmRegnoExists = EjbLookUps.getUserDetailsRemote().isFirmRegnoExists(requestXsd.getFirmRegno());
	            if (isFirmRegnoExists) {
	                responseXsd.setError(true);
	                responseXsd.setErrorCode("1004");
	                responseXsd.setErrorDescription("Firm Registration Number already exists");
	                return responseXsd;
	            }
	        }

	        // If neither exists, allow registration
	        if (!isMemberRegNoExists && !isFirmRegnoExists) {
	            responseXsd.setError(false);
	        }

	        return responseXsd;

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseXsd.setError(true);
	        responseXsd.setErrorCode(Constants.ECSV0001);
	        responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	        return responseXsd;
	    }
	}

	 */
	public static UserResponce userLoginWithUserPan(UserRequest requestXSD) throws Exception {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo dataPojo = null;

		try {

			dataPojo = EjbLookUps.getUserDetailsRemote().loginWithUserPan( requestXSD.getPan(), requestXSD.getEntityPan());


			if(dataPojo==null) {
				System.out.println("No data");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0003);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0003));
				return responseXsd;
			}

			if(dataPojo.getStatus().equals("BLOCKED")) {

				responseXsd.setError(true);
				responseXsd.setErrorDescription("Account Blocked");
				return responseXsd;

			}
			if(dataPojo.getPasscodeCount()=="5") {
				responseXsd.setError(true);
				responseXsd.setErrorDescription("Account blocked");
				return responseXsd;
			}



			if(dataPojo.getPassword().equals(requestXSD.getPassword())) {


				System.out.println("dataPojo.geteSignStatus"+dataPojo.geteSignStatus());

				if(dataPojo.getStatus().equals("PENDING")&& dataPojo.geteSignStatus().equals("PENDING"))  {
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0015);
					responseXsd.setErrorDescription("Status Pending");
					responseXsd.setPan(dataPojo.getPkPojo().getPan());
					responseXsd.setUserType(dataPojo.getPkPojo().getUserType());
					responseXsd.setEntityName(dataPojo.getPkPojo().getEntityName());


					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0015));
					return responseXsd;



				}	else if(dataPojo.getStatus().equals("ACTIVE")&& dataPojo.geteSignStatus().equals("PENDING"))  {
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0015);
					responseXsd.setErrorDescription("eSign Pending");
					responseXsd.setPan(dataPojo.getPkPojo().getPan());
					responseXsd.setUserType(dataPojo.getPkPojo().getUserType());
					responseXsd.setEntityName(dataPojo.getPkPojo().getEntityName());

					return responseXsd;


				} else if (dataPojo.getStatus().equals("ACTIVE") && dataPojo.geteSignStatus().equals("SUCCESS")) {



					responseXsd.setEmailId(dataPojo.getEmailId());
					responseXsd.setPan(dataPojo.getPkPojo().getPan());
					responseXsd.setUserType(dataPojo.getPkPojo().getUserType());
					responseXsd.setEntityName(dataPojo.getPkPojo().getEntityName());
					responseXsd.setRegNo(dataPojo.getRegNo());
					responseXsd.setName(dataPojo.getName());
					responseXsd.setDesignation(dataPojo.getDesignation());
					responseXsd.setDob(dataPojo.getDob());
					responseXsd.setEmailId(dataPojo.getEmailId());
					responseXsd.setMobileNo(dataPojo.getMobileNo());
					responseXsd.setConsent(dataPojo.getConsent());
					responseXsd.setEntityPan(dataPojo.getEntityPan());
					responseXsd.setEntityRegNo(dataPojo.getEntityRegNo());
					responseXsd.setMemberRegNo(dataPojo.getMemberRegNo());
					responseXsd.setFirmRegno(dataPojo.getFirmRegno());
					responseXsd.seteSignStatus(dataPojo.geteSignStatus());
					responseXsd.setLogoutDate(dataPojo.getLogoutDate());
					responseXsd.setFirmRegno(dataPojo.getFirmRegno());
					responseXsd.setMemberRegNo(dataPojo.getMemberRegNo());


					List<EntityDetailsPojo> entPojo = EjbLookUps.getEntityDetailsRemote().findByPan(requestXSD.getEntityPan());

					if (entPojo != null && !entPojo.isEmpty()) {
						responseXsd.setEntityMobile(entPojo.get(0).getMobileNo());
						responseXsd.setEntityEmail(entPojo.get(0).getEmailId());
					}


					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);

					responseXsd.setUserType(dataPojo.getPkPojo().getUserType());
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0017));
					return responseXsd;


				} else if ((dataPojo.getStatus().equals("DEACTIVE"))) {
					responseXsd = new UserResponce();
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0022);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0022));
					return responseXsd;

				}
				else if ((dataPojo.getStatus().equals("REJECT"))) {
					responseXsd = new UserResponce();
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0019);
					responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0019));
					return responseXsd;

				}else {
					responseXsd.setError(true);
					responseXsd.setErrorCode(Constants.ECSV0023);
					responseXsd.setErrorDescription("invalid credential");
					responseXsd.setPan(dataPojo.getPkPojo().getPan());
					responseXsd.setUserType(dataPojo.getPkPojo().getUserType());
					responseXsd.setEntityName(dataPojo.getPkPojo().getEntityName());
					return responseXsd;


				}


			} else {
				int count = Integer.parseInt(dataPojo.getPasscodeCount());

				if (count == 4) {
					UserDetailsPojo pojo = new UserDetailsPojo();
					UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

					pkPojo.setPan(requestXSD.getPan());
					pkPojo.setUserType(dataPojo.getPkPojo().getUserType());
					pkPojo.setEntityName(dataPojo.getPkPojo().getEntityName());
					pojo.setEntityPan(requestXSD.getEntityPan());
					pojo.setPkPojo(pkPojo);
					pojo.setPasscodeCount(String.valueOf(count + 1));
					pojo.setStatus("BLOCKED");

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(true);
					responseXsd.setErrorDescription("Account blocked");
					return responseXsd;


				} else {
					UserDetailsPojo pojo = new UserDetailsPojo();
					UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

					pkPojo.setPan(requestXSD.getPan());
					pkPojo.setUserType(dataPojo.getPkPojo().getUserType());
					pkPojo.setEntityName(dataPojo.getPkPojo().getEntityName());
					pojo.setEntityPan(requestXSD.getEntityPan());
					pojo.setPkPojo(pkPojo);

					// Increment passcodeCount by 1
					pojo.setPasscodeCount(String.valueOf(count + 1));

					EjbLookUps.getUserDetailsRemote().update(pojo);
				}
			}



			System.out.println("password not match");
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0003);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0003));
			return responseXsd;

		}


		catch (Exception e) {

			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;


		}
	}

	public static UserResponce approveRejectUser(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo pojoList = null;
		try {
			pojoList  =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(),requestXsd.getUserType(),requestXsd.getEntityName());
			System.out.println("pojolist->"+pojoList );
			if (pojoList == null ) {

				responseXsd = new UserResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {


				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				if (requestXsd.getStatus().equals("ACTIVE")) {


					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setUserType(requestXsd.getUserType());
					pkPojo.setEntityName(requestXsd.getEntityName());
					pojo.setPkPojo(pkPojo);
					pojo.setStatus(requestXsd.getStatus());
					pojo.setRemark(requestXsd.getRemark());

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);
					responseXsd.setErrorDescription("Approved Successfully");
					return responseXsd;

				} else if (requestXsd.getStatus().equals("REJECT"))

				{
					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setUserType(requestXsd.getUserType());
					pkPojo.setEntityName(requestXsd.getEntityName());
					pojo.setPkPojo(pkPojo);
					pojo.setStatus(requestXsd.getStatus());
					pojo.setRemark(requestXsd.getRemark());

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0019);
					responseXsd.setErrorDescription("Reject Successfully");
					return responseXsd;

				}
				else if (requestXsd.getStatus().equals("DEACTIVE"))
				{

					// check active user 
					System.out.println("entity name-> "+pojoList.getPkPojo().getEntityName());

					long activeUsers = EjbLookUps.getUserDetailsRemote().userCountforEntityPan(pojoList.getEntityPan(), "ACTIVE");

					System.out.println("active--> "+activeUsers);

					if (activeUsers == 1) {
						responseXsd.setError(true);
						responseXsd.setErrorCode(Constants.ECSV0018);
						responseXsd.setErrorDescription("One Active User Mandatory");
						return responseXsd;

					}

					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setUserType(requestXsd.getUserType());
					pkPojo.setEntityName(requestXsd.getEntityName());
					pojo.setPkPojo(pkPojo);
					pojo.setStatus(requestXsd.getStatus());
					pojo.setRemark(requestXsd.getRemark());

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0022);
					responseXsd.setErrorDescription("Deactive Successfully");
					return responseXsd;


				}
				else {
					responseXsd.setError(false);
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



	//	
	//	public static UserResponce distinctEntityName(UserRequest requestXsd) throws Exception {
	//
	//		UserResponce responseXsd = new UserResponce();
	//		try {
	//
	//			List<String> pojo1 = EjbLookUps.getUserDetailsRemote().findDistinctEntityName("AUDITOR");
	//	
	//			if(pojo1==null) {
	//				responseXsd.setError(true);
	//				responseXsd.setErrorCode(Constants.ECSV0014);
	//				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0014));
	//				return responseXsd;
	//			}
	//			else {
	//				responseXsd.setError(false);
	//				responseXsd.setEntityNameList(pojo1);
	//				return responseXsd;
	//			}
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			responseXsd.setError(true);
	//			responseXsd.setErrorCode(Constants.ECSV0001);
	//			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
	//			return responseXsd;
	//		}
	//	}


	public static UserResponce getAuditorByEntityName(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		List<UserDetailsPojo> pojoList = null;
		try {

			pojoList = EjbLookUps.getUserDetailsRemote().getAuditorByEntityName(requestXsd.getEntityName(), requestXsd.getUserType());

			if(pojoList==null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<UserDetails> dtlList = new ArrayList<>();

				for(UserDetailsPojo pojo : pojoList) {

					UserDetails addPojo = new UserDetails();

					addPojo.setName(pojo.getName());
					addPojo.setPan(pojo.getPkPojo().getPan());
					//	addPojo.setUserType(pojo.getPkPojo().getUserType());
					//	addPojo.setEmailId(pojo.getEmailId());
					addPojo.setMobileNo(pojo.getMobileNo());
					addPojo.setRegNo(pojo.getRegNo());
					addPojo.setMemberRegNo(pojo.getMemberRegNo());
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


	public static UserResponce getAuditorDetailsByName(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		List<UserDetailsPojo> pojoList = null;
		try {

			pojoList = EjbLookUps.getUserDetailsRemote().getAuditorByEntityName(requestXsd.getEntityName(), requestXsd.getUserType());

			if(pojoList==null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<UserDetails> dtlList = new ArrayList<>();

				for(UserDetailsPojo pojo : pojoList) {

					UserDetails addPojo = new UserDetails();

					addPojo.setName(pojo.getName());
					addPojo.setPan(pojo.getPkPojo().getPan());
					//	addPojo.setUserType(pojo.getPkPojo().getUserType());
					//	addPojo.setEmailId(pojo.getEmailId());
					addPojo.setMobileNo(pojo.getMobileNo());

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



	public static UserResponce getEmailMobileByPan(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		List<UserDetailsPojo> pojoList = null;
		try {

			pojoList = EjbLookUps.getUserDetailsRemote().getDetailsByPan(requestXsd.getPan(), requestXsd.getEntityPan());

			if(pojoList==null) {
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			}
			else {

				List<UserDetails> dtlList = new ArrayList<>();

				for(UserDetailsPojo pojo : pojoList) {

					UserDetails addPojo = new UserDetails();

					addPojo.setEntityPan(pojo.getEntityPan());
					addPojo.setPan(pojo.getPkPojo().getPan());
					addPojo.setEmailId(pojo.getEmailId());
					addPojo.setMobileNo(pojo.getMobileNo());

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



	public static CountResponseXsd auditeeDashboardCounts(UserRequest reqXSD) throws Exception {

		CountResponseXsd responseXsd = new CountResponseXsd();


		try {

			// bank linkage count

			long bankActiveCount = EjbLookUps.getBankLinkageRemote().dashboardCountByAduiteePan(reqXSD.getAuditeePan(), "ACTIVE");
			long bankPendingCount = EjbLookUps.getBankLinkageRemote().dashboardCountByAduiteePan(reqXSD.getAuditeePan(), "PENDING");
			long bankDeactiveCount = EjbLookUps.getBankLinkageRemote().dashboardCountByAduiteePan(reqXSD.getAuditeePan(), "DEACTIVE");
			long bankRejectCount = EjbLookUps.getBankLinkageRemote().dashboardCountByAduiteePan(reqXSD.getAuditeePan(), "REJECT");

			// auditee assign count 
			long auditeeActiveCount = EjbLookUps.getAuditorAssignRemote().auditorAssignCountByAuditeePan(reqXSD.getAuditeePan(), "ACTIVE");
			long auditeeDeactiveCount = EjbLookUps.getAuditorAssignRemote().auditorAssignCountByAuditeePan(reqXSD.getAuditeePan(), "REVOKED");

			// BC request count
			long bcActiveCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduiteePan(reqXSD.getAuditeePan(), "ACTIVE");
			long bcPendingCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduiteePan(reqXSD.getAuditeePan(), "PENDING");
			long bcexpiredCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduiteePan(reqXSD.getAuditeePan(), "EXPIRED");
			long bcPendingpaymentStatus = EjbLookUps.getBalanceConfirmRemote().bcPaymentCountByAduiteePan(reqXSD.getAuditeePan(), "PENDING");
			long bcActivepaymentStatus = EjbLookUps.getBalanceConfirmRemote().bcPaymentCountByAduiteePan(reqXSD.getAuditeePan(), "SUCCESS");
			long bcDownloadStatus = EjbLookUps.getBalanceConfirmRemote().bcDownloadStatusAduiteePan(reqXSD.getAuditeePan(),"N");
			long bcPendingDownloadStatus = EjbLookUps.getBalanceConfirmRemote().bcDownloadStatusAduiteePan(reqXSD.getAuditeePan(),"Y");


			System.out.println("bcDownloadStatus----------------------------"+bcDownloadStatus);
			System.out.println("bcPendingDownloadStatus----------------------------"+bcPendingDownloadStatus);


			//user count
			long activeUser = EjbLookUps.getUserDetailsRemote().totalUserCount(reqXSD.getEntityPan(),reqXSD.getUserType(),"ACTIVE");
			long deActiveUser = EjbLookUps.getUserDetailsRemote().totalUserCount(reqXSD.getEntityPan(),reqXSD.getUserType(),"DEACTIVE");
			System.out.println("reqXSD.getEntityPan()-----------------------------"+reqXSD.getEntityPan());
			System.out.println("reqXSD.getUserType()-----------------------------"+reqXSD.getUserType());
			System.out.println("activeUser-----------------------------"+activeUser);
			System.out.println("deActiveUser-----------------------------"+deActiveUser);




			long bankLinkTotalCount ;
			long auditeeTotalCount ;
			long bcTotalCount ;
			long userTotalCount;

			bcTotalCount = bcActiveCount +  bcPendingCount + bcexpiredCount;
			auditeeTotalCount =  auditeeActiveCount + auditeeDeactiveCount;
			bankLinkTotalCount = bankActiveCount + bankPendingCount + bankDeactiveCount + bankRejectCount;
			userTotalCount =  activeUser + deActiveUser;


			responseXsd = new CountResponseXsd();
			responseXsd.setError(false);

			responseXsd.setBankActiveCount(bankActiveCount);
			responseXsd.setBankPendingCount(bankPendingCount);
			responseXsd.setBankDeactiveCount(bankDeactiveCount);
			responseXsd.setBankRejectCount(bankRejectCount);
			responseXsd.setBankLinkageTotalCount(bankLinkTotalCount);


			responseXsd.setAuditeeActiveCount(auditeeActiveCount);
			responseXsd.setAuditeeDeactiveCount(auditeeDeactiveCount);
			responseXsd.setAuditeeTotalCount(auditeeTotalCount);

			responseXsd.setBcActiveCount(bcActiveCount);
			responseXsd.setBcPendingCount(bcPendingCount);
			responseXsd.setBcExpiredCount(bcexpiredCount);


			responseXsd.setBcActivepaymentStatus(bcActivepaymentStatus);
			responseXsd.setBcPendingpaymentStatus(bcPendingpaymentStatus);
			responseXsd.setBcPendingDownloadStatus(bcPendingDownloadStatus);
			responseXsd.setBcDownloadStatus(bcDownloadStatus);
			responseXsd.setBcTotalCount(bcTotalCount);

			responseXsd.setActiveUser(activeUser);
			responseXsd.setDeActiveUser(deActiveUser);
			responseXsd.setUserTotalCount(userTotalCount);


			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new CountResponseXsd();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}


	public static CountResponseXsd auditorDashboardCounts(UserRequest reqXSD) throws Exception {

		CountResponseXsd responseXsd = new CountResponseXsd();


		try {
			// auditor assign count 
			long auditorActiveCount = EjbLookUps.getAuditorAssignRemote().auditeeAssignCountByAuditorPan(reqXSD.getAuditorPan(), "ACTIVE");
			long AuditorDeactiveCount = EjbLookUps.getAuditorAssignRemote().auditeeAssignCountByAuditorPan(reqXSD.getAuditorPan(), "REVOKED");

			// BC request count
			long bcReqPendingCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduitorPan(reqXSD.getAuditorPan(), "PENDING");
			long bcReqActiveCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduitorPan(reqXSD.getAuditorPan(), "ACTIVE");
			long bcReqexpiredCount = EjbLookUps.getBalanceConfirmRemote().bcCountByAduitorPan(reqXSD.getAuditorPan(), "EXPIRED");
			long bcReqPendingpaymentStatus = EjbLookUps.getBalanceConfirmRemote().bcPaymentCountByAuditorPan(reqXSD.getAuditorPan(), "PENDING");
			long bcReqActivepaymentStatus = EjbLookUps.getBalanceConfirmRemote().bcPaymentCountByAuditorPan(reqXSD.getAuditorPan(), "SUCCESS");
			long bcReqDownloadStatus = EjbLookUps.getBalanceConfirmRemote().bcDownloadStatusAuditorPan(reqXSD.getAuditorPan(), "N");
			long bcReqPendingDownloadStatus = EjbLookUps.getBalanceConfirmRemote().bcDownloadStatusAuditorPan(reqXSD.getAuditorPan(), "Y");


			long auditorTotalCount ;
			long BcReqTotalCount ;
			long userTotalCount;

			//user count


			System.out.println();
			long activeUser = EjbLookUps.getUserDetailsRemote().totalUserCount(reqXSD.getEntityPan(),reqXSD.getUserType(),"ACTIVE");
			long deActiveUser = EjbLookUps.getUserDetailsRemote().totalUserCount(reqXSD.getEntityPan(),reqXSD.getUserType(),"DEACTIVE");

			System.out.println("activeUser-----------------------------"+activeUser);
			System.out.println("deActiveUser-----------------------------"+deActiveUser);


			BcReqTotalCount = bcReqPendingCount +  bcReqActiveCount + bcReqexpiredCount ;
			auditorTotalCount =  auditorActiveCount + AuditorDeactiveCount;
			userTotalCount =  activeUser + deActiveUser;


			responseXsd = new CountResponseXsd();
			responseXsd.setError(false);

			responseXsd.setAuditorActiveCount(auditorActiveCount);
			responseXsd.setAuditorDeactiveCount(AuditorDeactiveCount);
			responseXsd.setAuditorTotalCount(auditorTotalCount);



			responseXsd.setBcReqActiveCount(bcReqActiveCount);
			responseXsd.setBcReqPendingCount(bcReqPendingCount);
			responseXsd.setBcReqExpiredCount(bcReqexpiredCount);
			responseXsd.setBcReqPendingpaymentStatus(bcReqPendingpaymentStatus);
			responseXsd.setBcReqActivepaymentStatus(bcReqActivepaymentStatus);

			responseXsd.setBcReqDownloadStatus(bcReqDownloadStatus);
			responseXsd.setBcReqPendingDownloadStatus(bcReqPendingDownloadStatus);




			responseXsd.setBcReqTotalCount(BcReqTotalCount);
			responseXsd.setActiveUser(activeUser);
			responseXsd.setDeActiveUser(deActiveUser);
			responseXsd.setUserTotalCount(userTotalCount);


			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new CountResponseXsd();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}

	public static UserResponce updateStatus(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo pojoList = null;
		try {
			pojoList  =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(),requestXsd.getUserType(),requestXsd.getEntityName());
			System.out.println("pojolist->"+pojoList );
			if (pojoList == null ) {

				responseXsd = new UserResponce();
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;
			} else {


				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				if (requestXsd.getStatus().equals("ACTIVE")) {

					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setUserType(requestXsd.getUserType());
					pkPojo.setEntityName(requestXsd.getEntityName());
					pojo.setPkPojo(pkPojo);

					pojo.setMobileNo(requestXsd.getMobileNo());
					pojo.setDesignation(requestXsd.getDesignation());
					pojo.setRegNo(requestXsd.getRegNo());
					pojo.setMemberRegNo(requestXsd.getMemberRegNo());
					pojo.setFirmRegno(requestXsd.getFirmRegno());

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0017);
					responseXsd.setErrorDescription("Approved successfully");
					return responseXsd;
				}
				else if (requestXsd.getStatus().equals("DEACTIVE"))
				{
					pkPojo.setPan(requestXsd.getPan());
					pkPojo.setUserType(requestXsd.getUserType());
					pkPojo.setEntityName(requestXsd.getEntityName());
					pojo.setPkPojo(pkPojo);

					pojo.setMobileNo(requestXsd.getMobileNo());
					pojo.setDesignation(requestXsd.getDesignation());
					pojo.setRegNo(requestXsd.getRegNo());
					pojo.setMemberRegNo(requestXsd.getMemberRegNo());
					pojo.setFirmRegno(requestXsd.getFirmRegno());

					EjbLookUps.getUserDetailsRemote().update(pojo);

					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0018);
					responseXsd.setErrorDescription("Deactive Successfully");
					return responseXsd;

				}
				else {
					responseXsd.setError(false);
					responseXsd.setErrorCode(Constants.ECSV0016);
					responseXsd.setErrorDescription("Invalid Status");
					return responseXsd;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode("100103");
			responseXsd.setErrorDescription("Exception occured while creating");
			return responseXsd;

		}
	}

	public static UserResponce userCount(UserRequest requestXSD) throws Exception {

		UserResponce responseXsd = new UserResponce();
		List<UserDetailsPojo> pojo = null;

		try {
			pojo= EjbLookUps.getUserDetailsRemote().UserCount(requestXSD.getEntityPan(),requestXSD.getStatus());

			if (pojo == null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0008);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0008));
				return responseXsd;


			} else {


				List<UserDetails> list = new ArrayList<>();
				for(UserDetailsPojo pojo1 : pojo) {

					UserDetails userDetails = new UserDetails();


					userDetails.setName(pojo1.getName());
					userDetails.setRegNo(pojo1.getRegNo());
					userDetails.setDesignation(pojo1.getDesignation());
					userDetails.setDob(pojo1.getDob());
					userDetails.setEmailId(pojo1.getEmailId());
					userDetails.setMobileNo(pojo1.getMobileNo());
					userDetails.setStatus(pojo1.getStatus());
					userDetails.setUserType(pojo1.getPkPojo().getUserType());
					userDetails.setEntityName(pojo1.getPkPojo().getEntityName());
					userDetails.setEntityPan(pojo1.getEntityPan());
					userDetails.setPan(pojo1.getPkPojo().getPan());
					userDetails.setMemberRegNo(pojo1.getMemberRegNo());
					userDetails.setFirmRegno(pojo1.getFirmRegno());

					list.add(userDetails);
				}

				responseXsd = new UserResponce();
				responseXsd.setUserDetailList(list);
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


	public static CountResponseXsd userDashboardCounts(UserRequest reqXSD) throws Exception {

		CountResponseXsd responseXsd = new CountResponseXsd();
		try {

			long userTotalCount;

			//user count
			long activeUser = EjbLookUps.getUserDetailsRemote().userCountforEntityPan(reqXSD.getEntityPan(),"ACTIVE");
			long deActiveUser = EjbLookUps.getUserDetailsRemote().userCountforEntityPan(reqXSD.getEntityPan(),"DEACTIVE");

			System.out.println("activeUser-----------------------------"+activeUser);
			System.out.println("deActiveUser-----------------------------"+deActiveUser);

			userTotalCount =  activeUser + deActiveUser;


			responseXsd = new CountResponseXsd();
			responseXsd.setError(false);


			responseXsd.setActiveUser(activeUser);
			responseXsd.setDeActiveUser(deActiveUser);
			responseXsd.setUserTotalCount(userTotalCount);


			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new CountResponseXsd();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	public static UserResponce getUsertotalCount() throws Exception {

		UserResponce responseXsd = new UserResponce();

		try {
			long totalUserCount = EjbLookUps.getUserDetailsRemote().getTotalUserCount();

			System.out.println("Total Users: " + totalUserCount);

			responseXsd.setError(false);

			responseXsd.setTotalUserCount(totalUserCount);

			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	} 
	public static CountResponseXsd infoDashboardCounts(UserRequest reqXSD) throws Exception {

		CountResponseXsd responseXsd = new CountResponseXsd();
		try {

			long InfoTotalCount;



			//user count
			long activeInfo = EjbLookUps.getBankLinkageRemote().dashboardCountForInfo(reqXSD.getBank(),"ACTIVE");
			long deActiveInfo = EjbLookUps.getBankLinkageRemote().dashboardCountForInfo(reqXSD.getBank(),"DEACTIVE");
			long rejectInfo = EjbLookUps.getBankLinkageRemote().dashboardCountForInfo(reqXSD.getBank(),"REJECT");
			long holdInfo = EjbLookUps.getBankLinkageRemote().dashboardCountForInfo(reqXSD.getBank(),"HOLD");
			long pendingInfo = EjbLookUps.getBankLinkageRemote().dashboardCountForInfo(reqXSD.getBank(),"PENDING");



			InfoTotalCount =  activeInfo + deActiveInfo + rejectInfo + holdInfo + pendingInfo;


			responseXsd = new CountResponseXsd();
			responseXsd.setError(false);


			responseXsd.setInfoActive(activeInfo);
			responseXsd.setInfoDeActive(deActiveInfo);
			responseXsd.setInfoReject(rejectInfo);
			responseXsd.setInfoPending(pendingInfo);
			responseXsd.setInfohold(holdInfo);
			responseXsd.setInfoTotalCount(InfoTotalCount);

			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new CountResponseXsd();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}



	public static UserResponce updateTrnxId(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {

			System.out.println("policy--------" + requestXsd.getPan());
			System.out.println("Creating service");

			UserDetailsPojo pojo1 =	EjbLookUps.getUserDetailsRemote().getUserDetailsPojo(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );
			if(pojo1!=null) {


				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				pkPojo.setPan(requestXsd.getPan().trim());
				pkPojo.setUserType(requestXsd.getUserType().trim());
				pkPojo.setEntityName(requestXsd.getEntityName());

				pojo.setPkPojo(pkPojo);

				pojo.seteSignTxnId(requestXsd.geteSignTxnId());

				EjbLookUps.getUserDetailsRemote().update(pojo);


				responseXsd.setError(false);
				responseXsd.setErrorCode("");
				responseXsd.setErrorDescription("TxnId updated Successfully");
				return responseXsd;


			}else {

				responseXsd.setError(true);
				responseXsd.setErrorCode("1003");
				responseXsd.setErrorDescription("No data");
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

	public static UserResponce updateEsignStatus (UserRequest requestXSD) throws Exception {
		UserResponce responseXsd  = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("eSignTxnId", requestXSD.geteSignTxnId());

			if(userPojo==null) {
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {

				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				pkPojo.setPan(userPojo.get(0).getPkPojo().getPan());
				pkPojo.setUserType(userPojo.get(0).getPkPojo().getUserType().trim());
				pkPojo.setEntityName(userPojo.get(0).getPkPojo().getEntityName());

				pojo.setPkPojo(pkPojo);

				pojo.seteSignStatus(requestXSD.geteSignStatus());

				EjbLookUps.getUserDetailsRemote().update(pojo);


				responseXsd.setError(false);
				responseXsd.setErrorCode("");
				responseXsd.setErrorDescription("Esign Status updated Successfully");
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


	public static UserResponce getUserStatus(UserRequest requestXsd) {

		UserResponce responseXsd = new UserResponce();
		//SmsEmailPojo smsPojo = new SmsEmailPojo();

		UserDetailsPojo pojoList = null;

		try {
			pojoList =	EjbLookUps.getUserDetailsRemote().getUserStatus(requestXsd.getPan(), requestXsd.getUserType(),requestXsd.getEntityName() );

			System.out.println("pojolist----------"+pojoList);

			if(pojoList==null){
				System.out.println("No Data Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}else {

				UserDetails detailPojo = new UserDetails();

				detailPojo.setPan(pojoList.getPkPojo().getPan());
				detailPojo.setUserType(pojoList.getPkPojo().getUserType());
				detailPojo.setEntityName(pojoList.getPkPojo().getEntityName());
				detailPojo.setStatus(pojoList.getStatus());
				detailPojo.seteSignStatus(pojoList.geteSignStatus());
				detailPojo.setMobileOptStatus(pojoList.getMobileOptStatus());
				detailPojo.setEmailOtpStatus(pojoList.getEmailOtpStatus());




				/*  smsPojo =	EjbLookUps.getSmsEmailRemote().findById(requestXsd.getPan());
					if (smsPojo == null) {
		                System.out.println("No SMS/Email data found for PAN: " + requestXsd.getPan());
		                responseXsd.setError(true);
		                responseXsd.setErrorCode(Constants.ECSV0100); 
		                responseXsd.setErrorDescription("SMS/Email data not found.");
		                return responseXsd;
		            }
					System.out.println("pojolist----------"+pojoList);

					    detailPojo.setStatus(pojoList.getStatus());
					    detailPojo.seteSignStatus(pojoList.geteSignStatus());
						detailPojo.setMobileOptStatus(smsPojo.getMobileOptStatus());
						detailPojo.setEmailOtpStatus(smsPojo.getEmailOtpStatus());  */



				responseXsd.setError(false);
				responseXsd.setUserDetail(detailPojo);   

				return responseXsd;  
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0100);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}

	public static UserResponce userPanCheck(UserRequest requestXSD) throws Exception {
		UserResponce responseXsd = new UserResponce();
		UserDetailsPojo pojo;

		try {
			pojo = EjbLookUps.getUserDetailsRemote().userPanCheck(requestXSD.getPan(), requestXSD.getUserType(), requestXSD.getEntityName());

			if (pojo != null) { 
				System.out.println("PAN is already registered");
				responseXsd.setError(true);
				responseXsd.setErrorDescription("PAN Already Registered");


			} else { 
				responseXsd.setError(false);
				responseXsd.setErrorDescription("PAN is available for registration");
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001); 
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
		}

		return responseXsd;
	}


	public static UserResponce emailCheck(UserRequest requestXSD) throws Exception {
		UserResponce responseXsd  = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("emailId", requestXSD.getEmailId());

			if(userPojo==null) {
				System.out.println("No Email Found");
				responseXsd.setError(true);
				responseXsd.setErrorCode(Constants.ECSV0100);
				responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return responseXsd;
			}
			else {
				responseXsd = new UserResponce();
				responseXsd.setError(false);
				responseXsd.setErrorDescription("Email Found");
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






	public static UserResponce updatePassword( UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();
		List<UserDetailsPojo> userPojo = null;
		try {

			userPojo = EjbLookUps.getUserDetailsRemote().findByProperty("emailId", requestXsd.getEmailId());

			if (userPojo == null) {

				responseXsd = new UserResponce();
				responseXsd.setError(true);
				responseXsd.setErrorDescription("No EmailId Found");
				return responseXsd;
			} else {

				for(UserDetailsPojo pojo : userPojo)
				{

					UserDetailsPojo pojo1 = new UserDetailsPojo();
					UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();


					pkPojo.setPan(pojo.getPkPojo().getPan());
					pkPojo.setUserType(pojo.getPkPojo().getUserType());
					pkPojo.setEntityName(pojo.getPkPojo().getEntityName());
					pojo1.setPkPojo(pkPojo);
					pojo1.setEmailId(requestXsd.getEmailId());
					pojo1.setPassword(requestXsd.getPassword());

					EjbLookUps.getUserDetailsRemote().update(pojo1);

				}

				responseXsd.setError(false);
				responseXsd.setErrorDescription("password Updated Succesfully");
				return responseXsd;
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001); 
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));

		}
		return responseXsd;
	}


	public static UserResponce userUnblock(UserRequest requestXsd) throws Exception {

		UserResponce responseXsd = new UserResponce();

		UserDetailsPojo dataPojo = null;

		try {


			dataPojo = EjbLookUps.getUserDetailsRemote().loginWithUserPan( requestXsd.getPan(), requestXsd.getEntityPan());

			if(dataPojo!=null) {

				UserDetailsPojo pojo = new UserDetailsPojo();
				UserDetailsPojoPk pkPojo = new UserDetailsPojoPk();

				pkPojo.setPan(requestXsd.getPan());
				pkPojo.setUserType(dataPojo.getPkPojo().getUserType());
				pkPojo.setEntityName(dataPojo.getPkPojo().getEntityName());
				pojo.setEntityPan(requestXsd.getEntityPan());
				pojo.setPkPojo(pkPojo);
				pojo.setPasscodeCount("0");
				pojo.setStatus("ACTIVE");

				EjbLookUps.getUserDetailsRemote().update(pojo);


				System.out.println("updated");


				responseXsd.setError(false);
				responseXsd.setErrorCode("1002");
				responseXsd.setErrorDescription("Unblock Successfully");
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

	public static UserResponce getUserStatusCount(UserRequest reqXSD) throws Exception {

		UserResponce responseXsd = new UserResponce();
		try {


			long activeUser = EjbLookUps.getUserDetailsRemote().userStatus("ACTIVE");
			long deActiveUser = EjbLookUps.getUserDetailsRemote().userStatus("DEACTIVE");

			System.out.println("activeUser-----------------------------"+activeUser);
			System.out.println("deActiveUser-----------------------------"+deActiveUser);



			responseXsd = new UserResponce();
			responseXsd.setError(false);


			responseXsd.setActiveUser(activeUser);
			responseXsd.setDeActiveUser(deActiveUser);


			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new UserResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}
	public static UserResponce getUserTypeCount(UserRequest reqXSD) throws Exception {

		UserResponce responseXsd = new UserResponce();
		long userTotalCount;
		try {


			long auditor = EjbLookUps.getUserDetailsRemote().userTypeCount("AUDITOR");
			long auditee = EjbLookUps.getUserDetailsRemote().userTypeCount("AUDITEE");
			long infoProvider = EjbLookUps.getUserDetailsRemote().userTypeCount("INFORMATION_PROVIDER");
			
			long setAuditorActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITOR","ACTIVE"); 
	        long setAuditordeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITOR","DEACTIVE"); 
	        
	        long setAuditeeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITEE","ACTIVE"); 
	        long setAuditeeDeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITEE","DEACTIVE"); 
	        
	        long setInfoActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("INFORMATION_PROVIDER","ACTIVE"); 
	        long setInfoDeactiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("INFORMATION_PROVIDER","DEACTIVE"); 
	        
	        
	        
	        
	        

	        userTotalCount =  setAuditorActiveUser + setAuditordeActiveUser+setAuditeeActiveUser+setAuditeeDeActiveUser+setInfoActiveUser+setInfoDeactiveUser;
			

			System.out.println("auditor-----------------------------"+auditor);
			System.out.println("auditee-----------------------------"+auditee);
			System.out.println("infoProvider-----------------------------"+infoProvider);



			responseXsd = new UserResponce();
			responseXsd.setError(false);


			responseXsd.setAuditorCount(auditor);
			responseXsd.setAuditeeCount(auditee);
			responseXsd.setInfoProvider(infoProvider);
			responseXsd.setAuditorActiveUser(setAuditorActiveUser);
		    responseXsd.setAuditordeActiveUser(setAuditordeActiveUser);
		    
		    responseXsd.setAuditeeActiveUser(setAuditeeActiveUser);
		    responseXsd.setAuditeeDeActiveUser(setAuditeeDeActiveUser);
		    
		    responseXsd.setInfoActiveUser(setInfoActiveUser);
		    responseXsd.setInfoDeActiveUser(setInfoDeactiveUser);
		   
		    
		    
		    responseXsd.setTotalUserCount(userTotalCount);
			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new UserResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}

	
	// new method in user service
	public static UserResponce demoUserType(UserRequest reqXSD) throws Exception {

		UserResponce responseXsd = new UserResponce();
		long userTotalCount;
		try {


			long auditor = EjbLookUps.getUserDetailsRemote().userTypeCount("AUDITOR");
			long auditee = EjbLookUps.getUserDetailsRemote().userTypeCount("AUDITEE");
			long infoProvider = EjbLookUps.getUserDetailsRemote().userTypeCount("INFORMATION_PROVIDER");
			
			long setAuditorActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITOR","ACTIVE"); 
	        long setAuditordeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITOR","DEACTIVE"); 
	        
	        long setAuditeeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITEE","ACTIVE"); 
	        long setAuditeeDeActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("AUDITEE","DEACTIVE"); 
	        
	        long setInfoActiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("INFORMATION_PROVIDER","ACTIVE"); 
	        long setInfoDeactiveUser = EjbLookUps.getUserDetailsRemote().userTypeStatusCount("INFORMATION_PROVIDER","DEACTIVE"); 
	        
	        userTotalCount =  setAuditorActiveUser + setAuditordeActiveUser+setAuditeeActiveUser+setAuditeeDeActiveUser+setInfoActiveUser+setInfoDeactiveUser;
			
			System.out.println("auditor-----------------------------"+auditor);
			System.out.println("auditee-----------------------------"+auditee);
			System.out.println("infoProvider-----------------------------"+infoProvider);

			responseXsd = new UserResponce();
			responseXsd.setError(false);
			responseXsd.setAuditorCount(auditor);
			responseXsd.setAuditeeCount(auditee);
			responseXsd.setInfoProvider(infoProvider);
			responseXsd.setAuditorActiveUser(setAuditorActiveUser);
		    responseXsd.setAuditordeActiveUser(setAuditordeActiveUser);
		    responseXsd.setAuditeeActiveUser(setAuditeeActiveUser);
		    responseXsd.setAuditeeDeActiveUser(setAuditeeDeActiveUser);
		    responseXsd.setInfoActiveUser(setInfoActiveUser);
		    responseXsd.setInfoDeActiveUser(setInfoDeactiveUser);
		    responseXsd.setTotalUserCount(userTotalCount);
			return responseXsd;


		} catch (Exception e) {

			e.printStackTrace();
			responseXsd = new UserResponce();
			responseXsd.setError(true);
			responseXsd.setErrorCode(Constants.ECSV0001);
			responseXsd.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0001));
			return responseXsd;
		}
	}

	
	public static void demoUserMethod()
	{
		
		System.out.println("New method added");
	}
}
