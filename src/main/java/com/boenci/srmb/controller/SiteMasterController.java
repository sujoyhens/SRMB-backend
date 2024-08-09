package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SiteMaster;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.SiteMasterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/sitemaster")
@CrossOrigin(origins = "*")
public class SiteMasterController {
	
	@Autowired
	private SiteMasterService siteMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchsitemaster")
	public String fetchAllSiteMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode siteMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = siteMasterRequestNode.path("caller");
				JsonNode jsonSearchType= siteMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= siteMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				SiteMaster newSiteMaster  = new SiteMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(siteMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newSiteMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("enterprisemasterid")){
							newSiteMaster.setEnterprisemasterid(Long.valueOf(strSearchContent));
						}else if(strSearchType.equalsIgnoreCase("enterprisemaster")){
							newSiteMaster.setEnterprisemasterid(Long.valueOf(strSearchContent));
						}
						List<SiteMaster> listSiteMaster = siteMasterService.findSiteMasterBySearchType(newSiteMaster,strSearchType);
						if((listSiteMaster != null) && (listSiteMaster.size()>0)) {
							srmbResponse.setResultList(listSiteMaster);
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
	
	@PostMapping("/cudsitemaster")
	public String siteMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long siteMasteredid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonSiteMasterNode = jsonNode.path("sitemaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					SiteMaster siteMaster =  new SiteMaster();
					siteMaster = objectMapper.treeToValue(jsonSiteMasterNode, SiteMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(siteMaster.getAction().equalsIgnoreCase("save")){
							SiteMaster newSiteMaster = siteMasterService.validSiteMaster(siteMaster);
							if(newSiteMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("SiteMaster Name Already Exist...");
							}else{
								siteMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								siteMaster.setStatus("active");
								siteMaster.setDeletedflag("no");
								siteMaster.setAction("save");
								siteMaster.setCaller(strCaller);
								siteMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								SiteMaster siteMasterForSave = siteMasterService.save(siteMaster);
								
								if(siteMasterForSave.getSitemasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("SiteMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! SiteMaster not saved ... Try Again");
								}
								
							}
						}else if(siteMaster.getAction().equalsIgnoreCase("update")){
							siteMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							siteMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							siteMaster.setAction("update");
							SiteMaster siteMasterForUpdate = siteMasterService.update(siteMaster);
							if(siteMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SiteMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SiteMaster not Updated ... Try Again");
							}
						}else if(siteMaster.getAction().equalsIgnoreCase("delete")){
							siteMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							siteMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							siteMaster.setAction("delete");
							siteMaster.setDeletedflag("yes");
							siteMaster.setStatus("inactive");
							SiteMaster siteMasterForDelete = siteMasterService.delete(siteMaster);
							if(siteMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SiteMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SiteMaster not Deleted ... Try Again");
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
