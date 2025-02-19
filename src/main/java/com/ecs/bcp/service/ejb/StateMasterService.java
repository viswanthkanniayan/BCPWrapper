package com.ecs.bcp.service.ejb;

import java.util.ArrayList;
import java.util.List;

import com.ecs.bcp.pojo.StateMasterPojo;
import com.ecs.bcp.pojo.UserDetailsPojo;
import com.ecs.bcp.utils.Constants;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.ErrorMessage;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.bcp.xsd.StateDetails;
import com.ecs.bcp.xsd.UserDetails;

public class StateMasterService {

	

	public static EntityResponce getStateCode(EntityRequest reqXsd) throws Exception {

		EntityResponce response = new EntityResponce();

		List<StateMasterPojo> pojoList = null;

		try {

			pojoList = EjbLookUps.getStateMasterRemote().findAll();
			if (pojoList == null || pojoList.size() == 0) {
				response = new EntityResponce();
				response.setError(true);
				response.setErrorMessage("no data found");
				return response;
			}

		 else {
			 
			response = new EntityResponce();
			response.setError(false);
			response.setStatemasterDetails(pojoList);
			return response;

			 }		
		
		} catch (Exception e) {

			e.printStackTrace();
			response.setError(true);
			////response.setErrorCode(Constants.C4SRA0053);
			//response.setErrorDescription(ErrorMessage.getErrorMessage(Constants.C4SRA0053));
			return response;
		

	}
	
	}

	public static EntityResponce getStatecodeByState(EntityRequest reqXsd) throws Exception {

		EntityResponce response = new EntityResponce();

		List<StateMasterPojo> pojoList = null;

		try {

			pojoList = EjbLookUps.getStateMasterRemote().findByProperty("state", reqXsd.getState());

			if(pojoList==null) {
				System.out.println("No Data Found");
				response.setError(true);
				response.setErrorCode(Constants.ECSV0100);
				response.setErrorDescription(ErrorMessage.getErrorMessage(Constants.ECSV0100));
				return response;
			}

		 else {
		
			/*	List<StateDetails> list = new ArrayList<>();
				for(StateMasterPojo pojo : pojoList) {

					StateDetails pojo1 = new StateDetails();


					pojo1.setState(pojo.getState());
					pojo1.setStateCode(pojo.getStateCode());
					
					list.add(pojo1);
				}*/
			response = new EntityResponce();
			response.setError(false);
			response.setStatemasterDetails(pojoList);
			return response;

			 }		
		
		} catch (Exception e) {

			e.printStackTrace();
			response.setError(true);
			////response.setErrorCode(Constants.C4SRA0053);
			//response.setErrorDescription(ErrorMessage.getErrorMessage(Constants.C4SRA0053));
			return response;
		

	}
	
	}
}
