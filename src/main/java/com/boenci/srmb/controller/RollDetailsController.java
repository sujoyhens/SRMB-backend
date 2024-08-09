package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.RollDetails;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.RollDetailsService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rolldetails")
@CrossOrigin(origins = "*")
public class RollDetailsController {
	
	@Autowired
	private RollDetailsService rollDetailsService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchrolldetails")
	public String fetchAllRollDetails(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode rollDetailsRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = rollDetailsRequestNode.path("caller");
				JsonNode jsonSearchType= rollDetailsRequestNode.path("searchtype");
				JsonNode jsonSearchContent= rollDetailsRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RollDetails newRollDetails  = new RollDetails();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(rollDetailsRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newRollDetails.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("areamasterid")){
						newRollDetails.setAreamasterid(Long.valueOf(strSearchContent));
					}
					List<RollDetails> listRollDetails = rollDetailsService.findRollDetailsBySearchType(newRollDetails,strSearchType);
					if((listRollDetails != null) && (listRollDetails.size()>0)) {
						srmbResponse.setResultList(listRollDetails);
						srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
						srmbResponse.setMessage("List fetch successfully...");
					}else{
						srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
						srmbResponse.setMessage("List not Found...");
					}
					}else{
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! User not valid ...");
					}

		}catch (JsonProcessingException e) {
			e.printStackTrace();
			srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
			srmbResponse.setMessage("Error! not Registered ... Try Again");
		} catch (IOException e) {
			e.printStackTrace();
			srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
			srmbResponse.setMessage("Error! not Registered ... Try Again");
		}
		
		String responseBody = UtilityRestController.toJson(srmbResponse);
		return new String(responseBody);
	}
	
	@PostMapping("/cudrolldetails")
	public String rollDetails(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Roll Details Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long rollDetailsedid = 0;
			 try {
					System.out.println("Getting Roll Details details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRollDetailsNode = jsonNode.path("rolldetails");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					RollDetails rollDetails =  new RollDetails();
					rollDetails = objectMapper.treeToValue(jsonRollDetailsNode, RollDetails.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(rollDetails.getAction().equalsIgnoreCase("save")){
							RollDetails newRollDetails = rollDetailsService.validRollDetails(rollDetails);
							if(newRollDetails != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("RollDetails Name Already Exist...");
							}else{
								rollDetails.setCreatedat(UtilityRestController.getCurrentDateTime());
								rollDetails.setStatus("active");
								rollDetails.setDeletedflag("no");
								rollDetails.setAction("save");
								rollDetails.setCaller(strCaller);
								rollDetails.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								RollDetails rollDetailsForSave = rollDetailsService.save(rollDetails);
								
								if(rollDetailsForSave.getRolldetailsid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Roll Details Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Roll Details not saved ... Try Again");
								}
								
							}
						}else if(rollDetails.getAction().equalsIgnoreCase("update")){
							rollDetails.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rollDetails.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rollDetails.setAction("update");
							RollDetails rollDetailsForUpdate = rollDetailsService.update(rollDetails);
							if(rollDetailsForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RollDetails Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RollDetails not Updated ... Try Again");
							}
						}else if(rollDetails.getAction().equalsIgnoreCase("delete")){
							rollDetails.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rollDetails.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rollDetails.setAction("delete");
							rollDetails.setDeletedflag("yes");
							rollDetails.setStatus("inactive");
							RollDetails rollDetailsForDelete = rollDetailsService.delete(rollDetails);
							if(rollDetailsForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RollDetails Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RollDetails not Deleted ... Try Again");
							}
						}
					}else {
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! not Registered ... Try Again");
					}
					
				  }catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! not Registered ... Try Again");
					} catch (IOException e) {
						e.printStackTrace();
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! not Registered ... Try Again");
					}
			 
				String finalJsonResponse = UtilityRestController.toJson(srmbResponse);
				return new String(finalJsonResponse);
	}	

}
