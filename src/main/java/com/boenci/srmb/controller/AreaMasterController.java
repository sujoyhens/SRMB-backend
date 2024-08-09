package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.AreaMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.AreaMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/areamaster")
@CrossOrigin(origins = "*")
public class AreaMasterController {
	
	@Autowired
	private AreaMasterService areaMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchareamaster")
	public String fetchAllAreaMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode areaMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = areaMasterRequestNode.path("caller");
				JsonNode jsonSearchType= areaMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= areaMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				AreaMaster newAreaMaster  = new AreaMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(areaMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newAreaMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("unitmasterid")){
						newAreaMaster.setUnitmasterid(Long.valueOf(strSearchContent));
					}
					List<AreaMaster> listAreaMaster = areaMasterService.findAreaMasterBySearchType(newAreaMaster,strSearchType);
					if((listAreaMaster != null) && (listAreaMaster.size()>0)) {
						srmbResponse.setResultList(listAreaMaster);
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
	
	@PostMapping("/cudareamaster")
	public String areaMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Area Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long areaMasteredid = 0;
			 try {
					System.out.println("Getting Area Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonAreaMasterNode = jsonNode.path("areamaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					AreaMaster areaMaster =  new AreaMaster();
					areaMaster = objectMapper.treeToValue(jsonAreaMasterNode, AreaMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(areaMaster.getAction().equalsIgnoreCase("save")){
							AreaMaster newAreaMaster = areaMasterService.validAreaMaster(areaMaster);
							if(newAreaMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("AreaMaster Name Already Exist...");
							}else{
								areaMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								areaMaster.setStatus("active");
								areaMaster.setDeletedflag("no");
								areaMaster.setAction("save");
								areaMaster.setCaller(strCaller);
								areaMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								AreaMaster areaMasterForSave = areaMasterService.save(areaMaster);
								
								if(areaMasterForSave.getAreamasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Area Master Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Area Master not saved ... Try Again");
								}
								
							}
						}else if(areaMaster.getAction().equalsIgnoreCase("update")){
							areaMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							areaMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							areaMaster.setAction("update");
							AreaMaster areaMasterForUpdate = areaMasterService.update(areaMaster);
							if(areaMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("AreaMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! AreaMaster not Updated ... Try Again");
							}
						}else if(areaMaster.getAction().equalsIgnoreCase("delete")){
							areaMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							areaMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							areaMaster.setAction("delete");
							areaMaster.setDeletedflag("yes");
							areaMaster.setStatus("inactive");
							AreaMaster areaMasterForDelete = areaMasterService.delete(areaMaster);
							if(areaMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("AreaMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! AreaMaster not Deleted ... Try Again");
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
