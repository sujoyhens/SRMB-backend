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
import com.boenci.srmb.model.production.SmsProduction;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.SmsProductionService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/smsproduction")
@CrossOrigin(origins = "*")
public class SmsProductionController {
	
	@Autowired
	private SmsProductionService smsProductionService;

	@Autowired
	private UserRegisterService userRegisterService;

	

	@PostMapping("/fetchsmsproduction")
	public String fetchAllSmsProduction(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode smsProductionRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = smsProductionRequestNode.path("caller");
				JsonNode jsonSearchType= smsProductionRequestNode.path("searchtype");
				JsonNode jsonSearchContent= smsProductionRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				SmsProduction newSmsProduction  = new SmsProduction();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(smsProductionRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newSmsProduction.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("smsproductiondate")){
							newSmsProduction.setSmsproductiondate(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("laboratorylog")){
							newSmsProduction.setUnitmasterid(Long.valueOf(strSearchContent));
						}else if(strSearchType.equalsIgnoreCase("productionlog")){
							newSmsProduction.setUnitmasterid(Long.valueOf(strSearchContent));
						}else if(strSearchType.equalsIgnoreCase("millproduction")){
							newSmsProduction.setUnitmasterid(Long.valueOf(strSearchContent));
						}else if(strSearchType.equalsIgnoreCase("totalweight")){
							newSmsProduction.setSmsproductiondate(strSearchContent);
						}
						List<SmsProduction> listSmsProduction = smsProductionService.findSmsProductionBySearchType(newSmsProduction,strSearchType);
						if((listSmsProduction != null) && (listSmsProduction.size()>0)) {
							srmbResponse.setResultList(listSmsProduction);
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
	
	@PostMapping("/cudsmsproduction")
	public String smsProduction(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 SrmbResponse srmbResponse = new SrmbResponse();
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long smsProductionedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonSmsProductionNode = jsonNode.path("smsproduction");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					SmsProduction smsProduction =  new SmsProduction();
					smsProduction = objectMapper.treeToValue(jsonSmsProductionNode, SmsProduction.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(smsProduction.getAction().equalsIgnoreCase("save")){
							SmsProduction newSmsProduction = smsProductionService.validSmsProduction(smsProduction);
							if(newSmsProduction != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("SmsProduction Name Already Exist...");
							}else{
								smsProduction.setCreatedat(UtilityRestController.getCurrentDateTime());
								smsProduction.setStatus("active");
								smsProduction.setDeletedflag("no");
								smsProduction.setAction("save");
								smsProduction.setCaller(strCaller);
								smsProduction.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								smsProduction.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								smsProduction.setPlantmasterid(userRegister.getPlantmasterid());
								smsProduction.setIslaboratorylogupdated("no");
								smsProduction.setIsmillproductionupdated("no");
								smsProduction.setIsproductionlogupdated("no");
								smsProduction.setLaboratoryname("notdecided");
								smsProduction.setCcmname("notdecided");
								smsProduction.setRmname("notdecided");
								smsProduction.setUserregisterid(userRegister.getUserregisterid());
								SmsProduction smsProductionForSave = smsProductionService.save(smsProduction);
								
								if(smsProductionForSave.getSmsproductionid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("SmsProduction Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! SmsProduction not saved ... Try Again");
								}
								
							}
						}else if(smsProduction.getAction().equalsIgnoreCase("update")){
							smsProduction.setUpdatedat(UtilityRestController.getCurrentDateTime());
							smsProduction.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							smsProduction.setAction("update");
							SmsProduction smsProductionForUpdate = smsProductionService.update(smsProduction);
							if(smsProductionForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SmsProduction Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SmsProduction not Updated ... Try Again");
							}
						}else if(smsProduction.getAction().equalsIgnoreCase("delete")){
							
							srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
							srmbResponse.setMessage("Error! SmsProduction Deletion not possible");
							
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
