package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.boenci.srmb.model.UnitMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UnitMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/unitmaster")
@CrossOrigin(origins = "*")
public class UnitMasterController {
	
	@Autowired
	private UnitMasterService unitMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchunitmaster")
	public String fetchAllUnitMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode unitMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = unitMasterRequestNode.path("caller");
				JsonNode jsonSearchType= unitMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= unitMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				UnitMaster newUnitMaster  = new UnitMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(unitMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newUnitMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("plantmasterid")){
							newUnitMaster.setPlantmasterid(Long.valueOf(strSearchContent));
						}
						List<UnitMaster> listUnitMaster = unitMasterService.findUnitMasterBySearchType(newUnitMaster,strSearchType);
						if((listUnitMaster != null) && (listUnitMaster.size()>0)) {
							srmbResponse.setResultList(listUnitMaster);
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
	
	@PostMapping("/cudunitmaster")
	public String unitMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long unitMasteredid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonUnitMasterNode = jsonNode.path("unitmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					UnitMaster unitMaster =  new UnitMaster();
					unitMaster = objectMapper.treeToValue(jsonUnitMasterNode, UnitMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(unitMaster.getAction().equalsIgnoreCase("save")){
							UnitMaster newUnitMaster = unitMasterService.validUnitMaster(unitMaster);
							if(newUnitMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("UnitMaster Name Already Exist...");
							}else{
								unitMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								unitMaster.setStatus("active");
								unitMaster.setDeletedflag("no");
								unitMaster.setAction("save");
								unitMaster.setCaller(strCaller);
								unitMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								UnitMaster unitMasterForSave = unitMasterService.save(unitMaster);
								
								if(unitMasterForSave.getUnitmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("UnitMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! UnitMaster not saved ... Try Again");
								}
								
							}
						}else if(unitMaster.getAction().equalsIgnoreCase("update")){
							unitMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							unitMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							unitMaster.setAction("update");
							UnitMaster unitMasterForUpdate = unitMasterService.update(unitMaster);
							if(unitMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("UnitMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! UnitMaster not Updated ... Try Again");
							}
						}else if(unitMaster.getAction().equalsIgnoreCase("delete")){
							unitMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							unitMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							unitMaster.setAction("delete");
							unitMaster.setDeletedflag("yes");
							unitMaster.setStatus("inactive");
							UnitMaster unitMasterForDelete = unitMasterService.delete(unitMaster);
							if(unitMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("UnitMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! UnitMaster not Deleted ... Try Again");
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
