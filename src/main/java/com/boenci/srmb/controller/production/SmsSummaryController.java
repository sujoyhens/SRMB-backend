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
import com.boenci.srmb.model.production.SmsSummary;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.SmsSummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/smssummary")
@CrossOrigin(origins = "*")
public class SmsSummaryController {
	
	@Autowired
	private SmsSummaryService smsSummaryService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchsmssummary")
	public String fetchAllSmsSummary(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode smsSummaryRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = smsSummaryRequestNode.path("caller");
				JsonNode jsonSearchType= smsSummaryRequestNode.path("searchtype");
				JsonNode jsonSearchContent= smsSummaryRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				SmsSummary newSmsSummary  = new SmsSummary();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(smsSummaryRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newSmsSummary.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productiondate")){
							newSmsSummary.setProductiondate(strSearchContent);
						}
						List<SmsSummary> listSmsSummary = smsSummaryService.findSmsSummaryBySearchType(newSmsSummary,strSearchType);
						if((listSmsSummary != null) && (listSmsSummary.size()>0)) {
							srmbResponse.setResultList(listSmsSummary);
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
	
	@PostMapping("/cudsmssummary")
	public String smsSummary(@RequestBody String jsonBody) {	
			 System.out.println("Getting SMS Summary details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("SMS Summary Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long smsSummaryedid = 0;
			 try {
					System.out.println("Getting SMS Summary details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonSmsSummaryNode = jsonNode.path("smssummary");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					SmsSummary smsSummary =  new SmsSummary();
					smsSummary = objectMapper.treeToValue(jsonSmsSummaryNode, SmsSummary.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(smsSummary.getAction().equalsIgnoreCase("save")){
							SmsSummary newSmsSummary = smsSummaryService.validSmsSummary(smsSummary);
							if(newSmsSummary != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("SmsSummary Name Already Exist...");
							}else{
								smsSummary.setCreatedat(UtilityRestController.getCurrentDateTime());
								smsSummary.setStatus("active");
								smsSummary.setDeletedflag("no");
								smsSummary.setAction("save");
								smsSummary.setCaller(strCaller);
								smsSummary.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								smsSummary.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								smsSummary.setPlantmasterid(userRegister.getPlantmasterid());
								SmsSummary smsSummaryForSave = smsSummaryService.save(smsSummary);
								
								if(smsSummaryForSave.getSmssummaryid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("SmsSummary Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! SmsSummary not saved ... Try Again");
								}
								
							}
						}else if(smsSummary.getAction().equalsIgnoreCase("update")){
							smsSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							smsSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							smsSummary.setAction("update");
							SmsSummary smsSummaryForUpdate = smsSummaryService.update(smsSummary);
							if(smsSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SmsSummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SmsSummary not Updated ... Try Again");
							}
						}else if(smsSummary.getAction().equalsIgnoreCase("delete")){
							smsSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							smsSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							smsSummary.setAction("delete");
							smsSummary.setDeletedflag("yes");
							smsSummary.setStatus("inactive");
							SmsSummary smsSummaryForUpdate = smsSummaryService.update(smsSummary);
							if(smsSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SmsSummary Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SmsSummary not Deleted ... Try Again");
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
