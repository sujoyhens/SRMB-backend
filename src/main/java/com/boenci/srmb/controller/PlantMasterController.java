package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.PlantMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.PlantMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/plantmaster")
@CrossOrigin(origins = "*")
public class PlantMasterController {
	
	@Autowired
	private PlantMasterService plantMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchplantmaster")
	public String fetchAllPlantMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode plantMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = plantMasterRequestNode.path("caller");
				JsonNode jsonSearchType= plantMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= plantMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				PlantMaster newPlantMaster  = new PlantMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(plantMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newPlantMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("sitemasterid")){
						newPlantMaster.setSitemasterid(Long.valueOf(strSearchContent));
					}
					List<PlantMaster> listPlantMaster = plantMasterService.findPlantMasterBySearchType(newPlantMaster,strSearchType);
					if((listPlantMaster != null) && (listPlantMaster.size()>0)) {
						srmbResponse.setResultList(listPlantMaster);
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
	
	@PostMapping("/cudplantmaster")
	public String plantMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting Plant master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Plant Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long plantMasteredid = 0;
			 try {
					System.out.println("Getting Plant Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonPlantMasterNode = jsonNode.path("plantmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					PlantMaster plantMaster =  new PlantMaster();
					plantMaster = objectMapper.treeToValue(jsonPlantMasterNode, PlantMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(plantMaster.getAction().equalsIgnoreCase("save")){
							PlantMaster newPlantMaster = plantMasterService.validPlantMaster(plantMaster);
							if(newPlantMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("PlantMaster Name Already Exist...");
							}else{
								plantMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								plantMaster.setStatus("active");
								plantMaster.setDeletedflag("no");
								plantMaster.setAction("save");
								plantMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								
								PlantMaster plantMasterForSave = plantMasterService.save(plantMaster);
								
								if(plantMasterForSave.getPlantmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("PlantMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! PlantMaster not saved ... Try Again");
								}
								
							}
						}else if(plantMaster.getAction().equalsIgnoreCase("update")){
							plantMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							plantMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							plantMaster.setAction("update");
							PlantMaster plantMasterForUpdate = plantMasterService.update(plantMaster);
							if(plantMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("PlantMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! PlantMaster not Updated ... Try Again");
							}
						}else if(plantMaster.getAction().equalsIgnoreCase("delete")){
							plantMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							plantMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							plantMaster.setAction("delete");
							plantMaster.setDeletedflag("yes");
							plantMaster.setStatus("inactive");
							PlantMaster plantMasterForDelete = plantMasterService.delete(plantMaster);
							if(plantMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("PlantMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! PlantMaster not Deleted ... Try Again");
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
