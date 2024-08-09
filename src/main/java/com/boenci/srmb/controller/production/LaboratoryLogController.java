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
import com.boenci.srmb.model.production.LaboratoryLog;
import com.boenci.srmb.model.production.SmsProduction;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.LaboratoryLogService;
import com.boenci.srmb.service.production.SmsProductionService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/laboratorylog")
@CrossOrigin(origins = "*")
public class LaboratoryLogController {
	
	@Autowired
	private LaboratoryLogService laboratoryLogService;

	@Autowired
	private SmsProductionService smsProductionService;

	@Autowired
	private UserRegisterService userRegisterService;

	

	@PostMapping("/fetchlaboratorylog")
	public String fetchAllLaboratoryLog(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode laboratoryLogRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = laboratoryLogRequestNode.path("caller");
				JsonNode jsonSearchType= laboratoryLogRequestNode.path("searchtype");
				JsonNode jsonSearchContent= laboratoryLogRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				LaboratoryLog newLaboratoryLog  = new LaboratoryLog();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(laboratoryLogRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newLaboratoryLog.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productionid")){
							newLaboratoryLog.setProductionid(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("laboratorylogdate")){
							newLaboratoryLog.setLaboratorylogdate(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productionlog")){
							newLaboratoryLog.setUnitmasterid(Long.valueOf(strSearchContent));
						}else if(strSearchType.equalsIgnoreCase("millproduction")){
							newLaboratoryLog.setUnitmasterid(Long.valueOf(strSearchContent));
						}
						List<LaboratoryLog> listLaboratoryLog = laboratoryLogService.findLaboratoryLogBySearchType(newLaboratoryLog,strSearchType);
						if((listLaboratoryLog != null) && (listLaboratoryLog.size()>0)) {
							srmbResponse.setResultList(listLaboratoryLog);
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
	
	@PostMapping("/cudlaboratorylog")
	public String laboratoryLog(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 SrmbResponse srmbResponse = new SrmbResponse();
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long laboratoryLogedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonLaboratoryLogNode = jsonNode.path("laboratorylog");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					LaboratoryLog laboratoryLog =  new LaboratoryLog();
					laboratoryLog = objectMapper.treeToValue(jsonLaboratoryLogNode, LaboratoryLog.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(laboratoryLog.getAction().equalsIgnoreCase("save")){
							LaboratoryLog newLaboratoryLog = laboratoryLogService.validLaboratoryLog(laboratoryLog);
							if(newLaboratoryLog != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("LaboratoryLog Name Already Exist...");
							}else{
								laboratoryLog.setCreatedat(UtilityRestController.getCurrentDateTime());
								laboratoryLog.setStatus("active");
								laboratoryLog.setDeletedflag("no");
								laboratoryLog.setAction("save");
								laboratoryLog.setCaller(strCaller);
								laboratoryLog.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								laboratoryLog.setIsmillproductionupdated("no");
								laboratoryLog.setIsproductionlogupdated("no");
								laboratoryLog.setCcmname("notdecided");
								laboratoryLog.setRmname("notdecided");
								laboratoryLog.setUserregisterid(userRegister.getUserregisterid());
								LaboratoryLog laboratoryLogForSave = laboratoryLogService.save(laboratoryLog);
								
								if(laboratoryLogForSave.getLaboratorylogid()>0){
									String productionId = laboratoryLogForSave.getProductionid();																		
									SmsProduction newSmsProduction =  new SmsProduction();
									newSmsProduction.setProductionid(productionId);
									SmsProduction smsProduction =  new SmsProduction();
									smsProduction = smsProductionService.findDetailsByEmail(newSmsProduction);
									if(smsProduction != null){
										smsProduction.setIslaboratorylogupdated("yes");
										smsProduction.setLaboratoryname(laboratoryLogForSave.getLaboratoryname());
										SmsProduction smsProductionForUpdate = smsProductionService.save(smsProduction);
									}
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("LaboratoryLog Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! LaboratoryLog not saved ... Try Again");
								}
								
							}
						}else if(laboratoryLog.getAction().equalsIgnoreCase("update")){
							laboratoryLog.setUpdatedat(UtilityRestController.getCurrentDateTime());
							laboratoryLog.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							laboratoryLog.setAction("update");
							LaboratoryLog laboratoryLogForUpdate = laboratoryLogService.update(laboratoryLog);
							if(laboratoryLogForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("LaboratoryLog Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! LaboratoryLog not Updated ... Try Again");
							}
						}else if(laboratoryLog.getAction().equalsIgnoreCase("delete")){
							
							srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
							srmbResponse.setMessage("Error! LaboratoryLog Deletion not possible");
							
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
