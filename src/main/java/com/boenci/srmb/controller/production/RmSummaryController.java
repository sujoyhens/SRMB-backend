package com.boenci.srmb.controller.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.production.RmSummary;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.RmSummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rmsummary")
@CrossOrigin(origins = "*")
public class RmSummaryController {
	
	@Autowired
	private RmSummaryService rmSummaryService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchrmsummary")
	public String fetchAllRmSummary(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode rmSummaryRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = rmSummaryRequestNode.path("caller");
				JsonNode jsonSearchType= rmSummaryRequestNode.path("searchtype");
				JsonNode jsonSearchContent= rmSummaryRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RmSummary newRmSummary  = new RmSummary();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(rmSummaryRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newRmSummary.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productiondate")){
							newRmSummary.setProductiondate(strSearchContent);
						}
						List<RmSummary> listRmSummary = rmSummaryService.findRmSummaryBySearchType(newRmSummary,strSearchType);
						if((listRmSummary != null) && (listRmSummary.size()>0)) {
							srmbResponse.setResultList(listRmSummary);
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
	
	@PostMapping("/cudrmsummary")
	public String rmSummary(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long rmSummaryedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRmSummaryNode = jsonNode.path("rmsummary");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					RmSummary rmSummary =  new RmSummary();
					rmSummary = objectMapper.treeToValue(jsonRmSummaryNode, RmSummary.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(rmSummary.getAction().equalsIgnoreCase("save")){
							RmSummary newRmSummary = rmSummaryService.validRmSummary(rmSummary);
							if(newRmSummary != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("RmSummary Name Already Exist...");
							}else{
								rmSummary.setCreatedat(UtilityRestController.getCurrentDateTime());
								rmSummary.setStatus("active");
								rmSummary.setDeletedflag("no");
								rmSummary.setAction("save");
								rmSummary.setCaller(strCaller);
								rmSummary.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								rmSummary.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								rmSummary.setPlantmasterid(userRegister.getPlantmasterid());
								RmSummary rmSummaryForSave = rmSummaryService.save(rmSummary);
								
								if(rmSummaryForSave.getRmsummaryid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("RmSummary Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! RmSummary not saved ... Try Again");
								}
								
							}
						}else if(rmSummary.getAction().equalsIgnoreCase("update")){
							rmSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmSummary.setAction("update");
							RmSummary rmSummaryForUpdate = rmSummaryService.update(rmSummary);
							if(rmSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmSummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmSummary not Updated ... Try Again");
							}
						}else if(rmSummary.getAction().equalsIgnoreCase("delete")){
							rmSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmSummary.setAction("delete");
							rmSummary.setDeletedflag("yes");
							rmSummary.setStatus("inactive");
							RmSummary rmSummaryForUpdate = rmSummaryService.update(rmSummary);
							if(rmSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmSummary Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmSummary not Deleted ... Try Again");
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
