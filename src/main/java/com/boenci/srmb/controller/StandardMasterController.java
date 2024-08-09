package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.StandardMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.StandardMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/standardmaster")
@CrossOrigin(origins = "*")
public class StandardMasterController {
	
	@Autowired
	private StandardMasterService standardMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchstandardmaster")
	public String fetchAllStandardMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode standardMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = standardMasterRequestNode.path("caller");
				JsonNode jsonSearchType= standardMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= standardMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				StandardMaster newStandardMaster  = new StandardMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(standardMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newStandardMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("subequipmentmasterid")){
						newStandardMaster.setSubequipmentmasterid(Long.valueOf(strSearchContent));
					}else if(strSearchType.equalsIgnoreCase("equipmentmasterid")){
						newStandardMaster.setEquipmentmasterid(Long.valueOf(strSearchContent));
					}
					List<StandardMaster> listStandardMaster = standardMasterService.findStandardMasterBySearchType(newStandardMaster,strSearchType);
					if((listStandardMaster != null) && (listStandardMaster.size()>0)) {
						srmbResponse.setResultList(listStandardMaster);
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
	
	@PostMapping("/cudstandardmaster")
	public String standardMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting StandardMaster details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("StandardMaster Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long standardMasteredid = 0;
			 try {
					System.out.println("Getting StandardMaster details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonStandardMasterNode = jsonNode.path("standardmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					StandardMaster standardMaster =  new StandardMaster();
					standardMaster = objectMapper.treeToValue(jsonStandardMasterNode, StandardMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(standardMaster.getAction().equalsIgnoreCase("save")){
							StandardMaster newStandardMaster = standardMasterService.validStandardMaster(standardMaster);
							if(newStandardMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("StandardMaster Name Already Exist...");
							}else{
								standardMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								standardMaster.setStatus("active");
								standardMaster.setDeletedflag("no");
								standardMaster.setAction("save");
								standardMaster.setCaller(strCaller);
								standardMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								StandardMaster standardMasterForSave = standardMasterService.save(standardMaster);
								
								if(standardMasterForSave.getAreamasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("StandardMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! StandardMaster not saved ... Try Again");
								}
								
							}
						}else if(standardMaster.getAction().equalsIgnoreCase("update")){
							standardMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							standardMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							standardMaster.setAction("update");
							StandardMaster standardMasterForUpdate = standardMasterService.update(standardMaster);
							if(standardMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("StandardMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! StandardMaster not Updated ... Try Again");
							}
						}else if(standardMaster.getAction().equalsIgnoreCase("delete")){
							standardMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							standardMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							standardMaster.setAction("delete");
							standardMaster.setDeletedflag("yes");
							standardMaster.setStatus("inactive");
							StandardMaster standardMasterForDelete = standardMasterService.delete(standardMaster);
							if(standardMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("StandardMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! StandardMaster not Deleted ... Try Again");
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
