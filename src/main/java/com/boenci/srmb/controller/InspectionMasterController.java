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

import com.boenci.srmb.model.InspectionMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.InspectionMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/inspectionmaster")
@CrossOrigin(origins = "*")
public class InspectionMasterController {
	
	@Autowired
	private InspectionMasterService inspectionMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchinspectionmaster")
	public String fetchAllInspectionMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode inspectionMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = inspectionMasterRequestNode.path("caller");
				JsonNode jsonSearchType= inspectionMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= inspectionMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				InspectionMaster newInspectionMaster  = new InspectionMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(inspectionMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newInspectionMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("unitmasterid")){
						newInspectionMaster.setUnitmasterid(Long.valueOf(strSearchContent));
					}
					List<InspectionMaster> listInspectionMaster = inspectionMasterService.findInspectionMasterBySearchType(newInspectionMaster,strSearchType);
					if((listInspectionMaster != null) && (listInspectionMaster.size()>0)) {
						srmbResponse.setResultList(listInspectionMaster);
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
	
	@PostMapping("/cudinspectionmaster")
	public String inspectionMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Area Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long inspectionMasteredid = 0;
			 try {
					System.out.println("Getting Area Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonInspectionMasterNode = jsonNode.path("inspectionmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					InspectionMaster inspectionMaster =  new InspectionMaster();
					inspectionMaster = objectMapper.treeToValue(jsonInspectionMasterNode, InspectionMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(inspectionMaster.getAction().equalsIgnoreCase("save")){
							InspectionMaster newInspectionMaster = inspectionMasterService.validInspectionMaster(inspectionMaster);
							if(newInspectionMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("InspectionMaster Name Already Exist...");
							}else{
								inspectionMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								inspectionMaster.setStatus("active");
								inspectionMaster.setDeletedflag("no");
								inspectionMaster.setAction("save");
								inspectionMaster.setCaller(strCaller);
								inspectionMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								InspectionMaster inspectionMasterForSave = inspectionMasterService.save(inspectionMaster);
								
								if(inspectionMasterForSave.getInspectionmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Area Master Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Area Master not saved ... Try Again");
								}
								
							}
						}else if(inspectionMaster.getAction().equalsIgnoreCase("update")){
							inspectionMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							inspectionMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							inspectionMaster.setAction("update");
							InspectionMaster inspectionMasterForUpdate = inspectionMasterService.update(inspectionMaster);
							if(inspectionMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("InspectionMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! InspectionMaster not Updated ... Try Again");
							}
						}else if(inspectionMaster.getAction().equalsIgnoreCase("delete")){
							inspectionMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							inspectionMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							inspectionMaster.setAction("delete");
							inspectionMaster.setDeletedflag("yes");
							inspectionMaster.setStatus("inactive");
							InspectionMaster inspectionMasterForDelete = inspectionMasterService.delete(inspectionMaster);
							if(inspectionMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("InspectionMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! InspectionMaster not Deleted ... Try Again");
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
