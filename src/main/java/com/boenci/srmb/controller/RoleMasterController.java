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

import com.boenci.srmb.model.RoleMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.RoleMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rolemaster")
@CrossOrigin(origins = "*")
public class RoleMasterController {
	
	@Autowired
	private RoleMasterService roleMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchrolemaster")
	public String fetchAllRoleMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode roleMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = roleMasterRequestNode.path("caller");
				JsonNode jsonSearchType= roleMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= roleMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RoleMaster newRoleMaster  = new RoleMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(roleMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newRoleMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("rolemasterid")){
							newRoleMaster.setRolemasterid(Long.valueOf(strSearchContent));
						}
						List<RoleMaster> listRoleMaster = roleMasterService.findRoleMasterBySearchType(newRoleMaster,strSearchType);
						if((listRoleMaster != null) && (listRoleMaster.size()>0)) {
							srmbResponse.setResultList(listRoleMaster);
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
	
	@PostMapping("/cudrolemaster")
	public String roleMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting Roll master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long roleMasteredid = 0;
			 try {
					System.out.println("Getting Roll Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRoleMasterNode = jsonNode.path("rolemaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					RoleMaster roleMaster =  new RoleMaster();
					roleMaster = objectMapper.treeToValue(jsonRoleMasterNode, RoleMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(roleMaster.getAction().equalsIgnoreCase("save")){
							RoleMaster newRoleMaster = roleMasterService.validRoleMaster(roleMaster);
							if(newRoleMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("RoleMaster Name Already Exist...");
							}else{
								roleMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								roleMaster.setStatus("active");
								roleMaster.setDeletedflag("no");
								roleMaster.setAction("save");
								roleMaster.setCaller(strCaller);
								roleMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								RoleMaster roleMasterForSave = roleMasterService.save(roleMaster);
								
								if(roleMasterForSave.getRolemasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("RoleMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! RoleMaster not saved ... Try Again");
								}
								
							}
						}else if(roleMaster.getAction().equalsIgnoreCase("update")){
							roleMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							roleMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							roleMaster.setAction("update");
							RoleMaster roleMasterForUpdate = roleMasterService.update(roleMaster);
							if(roleMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RoleMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RoleMaster not Updated ... Try Again");
							}
						}else if(roleMaster.getAction().equalsIgnoreCase("delete")){
							roleMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							roleMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							roleMaster.setAction("delete");
							roleMaster.setDeletedflag("yes");
							roleMaster.setStatus("inactive");
							RoleMaster roleMasterForDelete = roleMasterService.delete(roleMaster);
							if(roleMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RoleMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RoleMaster not Deleted ... Try Again");
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
				srmbResponse.setResultList(null);
				return new String(finalJsonResponse);
	}	

}
