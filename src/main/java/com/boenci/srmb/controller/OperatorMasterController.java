package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.OperatorMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.OperatorMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/operatormaster")
@CrossOrigin(origins = "*")
public class OperatorMasterController {
	
	@Autowired
	private OperatorMasterService operatorMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchoperatormaster")
	public String fetchAllOperatorMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode operatorMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = operatorMasterRequestNode.path("caller");
				JsonNode jsonSearchType= operatorMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= operatorMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				OperatorMaster newOperatorMaster  = new OperatorMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(operatorMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newOperatorMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("designation")){
						newOperatorMaster.setDesignation(strSearchContent);
					}
					List<OperatorMaster> listOperatorMaster = operatorMasterService.findOperatorMasterBySearchType(newOperatorMaster,strSearchType);
					if((listOperatorMaster != null) && (listOperatorMaster.size()>0)) {
						srmbResponse.setResultList(null);
						srmbResponse.setResultList(listOperatorMaster);
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
	
	@PostMapping("/cudoperatormaster")
	public String operatorMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Area Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long operatorMasteredid = 0;
			 try {
					System.out.println("Getting Area Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonOperatorMasterNode = jsonNode.path("operatormaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					OperatorMaster operatorMaster =  new OperatorMaster();
					operatorMaster = objectMapper.treeToValue(jsonOperatorMasterNode, OperatorMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(operatorMaster.getAction().equalsIgnoreCase("save")){
							OperatorMaster newOperatorMaster = operatorMasterService.validOperatorMaster(operatorMaster);
							if(newOperatorMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("OperatorMaster Name Already Exist...");
							}else{
								operatorMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								operatorMaster.setStatus("active");
								operatorMaster.setDeletedflag("no");
								operatorMaster.setAction("save");
								operatorMaster.setCaller(strCaller);
								operatorMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								OperatorMaster operatorMasterForSave = operatorMasterService.save(operatorMaster);
								
								if(operatorMasterForSave.getOperatormasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Area Master Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Area Master not saved ... Try Again");
								}
								
							}
						}else if(operatorMaster.getAction().equalsIgnoreCase("update")){
							operatorMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							operatorMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							operatorMaster.setAction("update");
							OperatorMaster operatorMasterForUpdate = operatorMasterService.update(operatorMaster);
							if(operatorMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("OperatorMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! OperatorMaster not Updated ... Try Again");
							}
						}else if(operatorMaster.getAction().equalsIgnoreCase("delete")){
							operatorMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							operatorMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							operatorMaster.setAction("delete");
							operatorMaster.setDeletedflag("yes");
							operatorMaster.setStatus("inactive");
							OperatorMaster operatorMasterForDelete = operatorMasterService.delete(operatorMaster);
							if(operatorMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("OperatorMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! OperatorMaster not Deleted ... Try Again");
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
