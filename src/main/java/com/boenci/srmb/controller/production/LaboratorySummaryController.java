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
import com.boenci.srmb.model.production.LaboratorySummary;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.LaboratorySummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/laboratorysummary")
@CrossOrigin(origins = "*")
public class LaboratorySummaryController {
	
	@Autowired
	private LaboratorySummaryService laboratorySummaryService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchlaboratorysummary")
	public String fetchAllLaboratorySummary(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode laboratorySummaryRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = laboratorySummaryRequestNode.path("caller");
				JsonNode jsonSearchType= laboratorySummaryRequestNode.path("searchtype");
				JsonNode jsonSearchContent= laboratorySummaryRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				LaboratorySummary newLaboratorySummary  = new LaboratorySummary();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(laboratorySummaryRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newLaboratorySummary.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productiondate")){
							newLaboratorySummary.setProductiondate(strSearchContent);
						}
						List<LaboratorySummary> listLaboratorySummary = laboratorySummaryService.findLaboratorySummaryBySearchType(newLaboratorySummary,strSearchType);
						if((listLaboratorySummary != null) && (listLaboratorySummary.size()>0)) {
							srmbResponse.setResultList(listLaboratorySummary);
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
	
	@PostMapping("/cudlaboratorysummary")
	public String laboratorySummary(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long laboratorySummaryedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonLaboratorySummaryNode = jsonNode.path("laboratorysummary");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					LaboratorySummary laboratorySummary =  new LaboratorySummary();
					laboratorySummary = objectMapper.treeToValue(jsonLaboratorySummaryNode, LaboratorySummary.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(laboratorySummary.getAction().equalsIgnoreCase("save")){
							LaboratorySummary newLaboratorySummary = laboratorySummaryService.validLaboratorySummary(laboratorySummary);
							if(newLaboratorySummary != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("LaboratorySummary Name Already Exist...");
							}else{
								laboratorySummary.setCreatedat(UtilityRestController.getCurrentDateTime());
								laboratorySummary.setStatus("active");
								laboratorySummary.setDeletedflag("no");
								laboratorySummary.setAction("save");
								laboratorySummary.setCaller(strCaller);
								laboratorySummary.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								laboratorySummary.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								laboratorySummary.setPlantmasterid(userRegister.getPlantmasterid());
								LaboratorySummary laboratorySummaryForSave = laboratorySummaryService.save(laboratorySummary);
								
								if(laboratorySummaryForSave.getLaboratorysummaryid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("LaboratorySummary Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! LaboratorySummary not saved ... Try Again");
								}
								
							}
						}else if(laboratorySummary.getAction().equalsIgnoreCase("update")){
							laboratorySummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							laboratorySummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							laboratorySummary.setAction("update");
							LaboratorySummary laboratorySummaryForUpdate = laboratorySummaryService.update(laboratorySummary);
							if(laboratorySummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("LaboratorySummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! LaboratorySummary not Updated ... Try Again");
							}
						}else if(laboratorySummary.getAction().equalsIgnoreCase("delete")){
							laboratorySummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							laboratorySummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							laboratorySummary.setAction("delete");
							laboratorySummary.setDeletedflag("yes");
							laboratorySummary.setStatus("inactive");
							LaboratorySummary laboratorySummaryForUpdate = laboratorySummaryService.update(laboratorySummary);
							if(laboratorySummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("LaboratorySummary Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! LaboratorySummary not Deleted ... Try Again");
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
