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

import com.boenci.srmb.model.SubEquipmentMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.SubEquipmentMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/subequipmentmaster")
@CrossOrigin(origins = "*")
public class SubEquipmentMasterController {
	
	@Autowired
	private SubEquipmentMasterService subEquipmentMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchsubequipmentmaster")
	public String fetchAllSubEquipmentMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode subEquipmentMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = subEquipmentMasterRequestNode.path("caller");
				JsonNode jsonSearchType= subEquipmentMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= subEquipmentMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				SubEquipmentMaster newSubEquipmentMaster  = new SubEquipmentMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(subEquipmentMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newSubEquipmentMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("equipmentmasterid")){
							newSubEquipmentMaster.setEquipmentmasterid(Long.valueOf(strSearchContent));
						}
						List<SubEquipmentMaster> listSubEquipmentMaster = subEquipmentMasterService.findSubEquipmentMasterBySearchType(newSubEquipmentMaster,strSearchType);
						if((listSubEquipmentMaster != null) && (listSubEquipmentMaster.size()>0)) {
							srmbResponse.setResultList(listSubEquipmentMaster);
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
	
	@PostMapping("/cudsubequipmentmaster")
	public String subEquipmentMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting SubEquipmentMaster details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("SubEquipmentMaster Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long subEquipmentMasteredid = 0;
			 try {
					System.out.println("Getting SubEquipmentMaster details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonSubEquipmentMasterNode = jsonNode.path("subequipmentmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					SubEquipmentMaster subEquipmentMaster =  new SubEquipmentMaster();
					subEquipmentMaster = objectMapper.treeToValue(jsonSubEquipmentMasterNode, SubEquipmentMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(subEquipmentMaster.getAction().equalsIgnoreCase("save")){
							SubEquipmentMaster newSubEquipmentMaster = subEquipmentMasterService.validSubEquipmentMaster(subEquipmentMaster);
							if(newSubEquipmentMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("SubEquipmentMaster Name Already Exist...");
							}else{
								subEquipmentMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								subEquipmentMaster.setStatus("active");
								subEquipmentMaster.setDeletedflag("no");
								subEquipmentMaster.setAction("save");
								subEquipmentMaster.setCaller(strCaller);
								subEquipmentMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								SubEquipmentMaster subEquipmentMasterForSave = subEquipmentMasterService.save(subEquipmentMaster);
								
								if(subEquipmentMasterForSave.getSubequipmentmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("SubEquipmentMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! SubEquipmentMaster not saved ... Try Again");
								}
								
							}
						}else if(subEquipmentMaster.getAction().equalsIgnoreCase("update")){
							subEquipmentMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							subEquipmentMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							subEquipmentMaster.setAction("update");
							SubEquipmentMaster subEquipmentMasterForUpdate = subEquipmentMasterService.update(subEquipmentMaster);
							if(subEquipmentMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SubEquipmentMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SubEquipmentMaster not Updated ... Try Again");
							}
						}else if(subEquipmentMaster.getAction().equalsIgnoreCase("delete")){
							subEquipmentMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							subEquipmentMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							subEquipmentMaster.setAction("delete");
							subEquipmentMaster.setDeletedflag("yes");
							subEquipmentMaster.setStatus("inactive");
							SubEquipmentMaster subEquipmentMasterForDelete = subEquipmentMasterService.delete(subEquipmentMaster);
							if(subEquipmentMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SubEquipmentMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SubEquipmentMaster not Deleted ... Try Again");
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
