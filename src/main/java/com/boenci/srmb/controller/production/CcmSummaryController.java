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
import com.boenci.srmb.model.production.CcmSummary;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.CcmSummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/ccmsummary")
@CrossOrigin(origins = "*")
public class CcmSummaryController {
	
	@Autowired
	private CcmSummaryService ccmSummaryService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchccmsummary")
	public String fetchAllCcmSummary(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode ccmSummaryRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = ccmSummaryRequestNode.path("caller");
				JsonNode jsonSearchType= ccmSummaryRequestNode.path("searchtype");
				JsonNode jsonSearchContent= ccmSummaryRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				CcmSummary newCcmSummary  = new CcmSummary();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(ccmSummaryRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newCcmSummary.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productiondate")){
							newCcmSummary.setProductiondate(strSearchContent);
						}
						List<CcmSummary> listCcmSummary = ccmSummaryService.findCcmSummaryBySearchType(newCcmSummary,strSearchType);
						if((listCcmSummary != null) && (listCcmSummary.size()>0)) {
							srmbResponse.setResultList(listCcmSummary);
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
	
	@PostMapping("/cudccmsummary")
	public String ccmSummary(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long ccmSummaryedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonCcmSummaryNode = jsonNode.path("ccmsummary");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					CcmSummary ccmSummary =  new CcmSummary();
					ccmSummary = objectMapper.treeToValue(jsonCcmSummaryNode, CcmSummary.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(ccmSummary.getAction().equalsIgnoreCase("save")){
							CcmSummary newCcmSummary = ccmSummaryService.validCcmSummary(ccmSummary);
							if(newCcmSummary != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("CcmSummary Name Already Exist...");
							}else{
								ccmSummary.setCreatedat(UtilityRestController.getCurrentDateTime());
								ccmSummary.setStatus("active");
								ccmSummary.setDeletedflag("no");
								ccmSummary.setAction("save");
								ccmSummary.setCaller(strCaller);
								ccmSummary.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								ccmSummary.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								ccmSummary.setPlantmasterid(userRegister.getPlantmasterid());
								CcmSummary ccmSummaryForSave = ccmSummaryService.save(ccmSummary);
								
								if(ccmSummaryForSave.getCcmsummaryid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("CcmSummary Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! CcmSummary not saved ... Try Again");
								}
								
							}
						}else if(ccmSummary.getAction().equalsIgnoreCase("update")){
							ccmSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							ccmSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							ccmSummary.setAction("update");
							CcmSummary ccmSummaryForUpdate = ccmSummaryService.update(ccmSummary);
							if(ccmSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("CcmSummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! CcmSummary not Updated ... Try Again");
							}
						}else if(ccmSummary.getAction().equalsIgnoreCase("delete")){
							ccmSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							ccmSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							ccmSummary.setAction("delete");
							ccmSummary.setDeletedflag("yes");
							ccmSummary.setStatus("inactive");
							CcmSummary ccmSummaryForUpdate = ccmSummaryService.update(ccmSummary);
							if(ccmSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("CcmSummary Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! CcmSummary not Deleted ... Try Again");
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
