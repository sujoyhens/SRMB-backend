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
import com.boenci.srmb.model.production.MillProduction;
import com.boenci.srmb.model.production.ProductionLog;
import com.boenci.srmb.model.production.SmsProduction;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.LaboratoryLogService;
import com.boenci.srmb.service.production.MillProductionService;
import com.boenci.srmb.service.production.ProductionLogService;
import com.boenci.srmb.service.production.SmsProductionService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/millproduction")
@CrossOrigin(origins = "*")
public class MillProductionController {
	
	@Autowired
	private MillProductionService millProductionService;

	@Autowired
	private SmsProductionService smsProductionService;

	@Autowired
	private LaboratoryLogService laboratoryLogService;

	@Autowired
	private ProductionLogService productionLogService;

	@Autowired
	private UserRegisterService userRegisterService;

	

	@PostMapping("/fetchmillproduction")
	public String fetchAllMillProduction(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode millProductionRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = millProductionRequestNode.path("caller");
				JsonNode jsonSearchType= millProductionRequestNode.path("searchtype");
				JsonNode jsonSearchContent= millProductionRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				MillProduction newMillProduction  = new MillProduction();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(millProductionRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newMillProduction.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("millproductiondate")){
							newMillProduction.setMillproductiondate(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("daterange")){
							newMillProduction.setMillproductiondate(strSearchContent);
						}
						List<MillProduction> listMillProduction = millProductionService.findMillProductionBySearchType(newMillProduction,strSearchType);
						if((listMillProduction != null) && (listMillProduction.size()>0)) {
							srmbResponse.setResultList(null);
							srmbResponse.setResultList(listMillProduction);
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
		srmbResponse.setResultList(null);
		return new String(responseBody);
	}
	
	@PostMapping("/cudmillproduction")
	public String millProduction(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 SrmbResponse srmbResponse = new SrmbResponse();
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long millProductionedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonMillProductionNode = jsonNode.path("millproduction");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					MillProduction millProduction =  new MillProduction();
					millProduction = objectMapper.treeToValue(jsonMillProductionNode, MillProduction.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(millProduction.getAction().equalsIgnoreCase("save")){
							MillProduction newMillProduction = millProductionService.validMillProduction(millProduction);
							if(newMillProduction != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("MillProduction Name Already Exist...");
							}else{
								millProduction.setCreatedat(UtilityRestController.getCurrentDateTime());
								millProduction.setStatus("active");
								millProduction.setDeletedflag("no");
								millProduction.setAction("save");
								millProduction.setCaller(strCaller);
								millProduction.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								millProduction.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								millProduction.setPlantmasterid(userRegister.getPlantmasterid());
								millProduction.setUserregisterid(userRegister.getUserregisterid());
								MillProduction millProductionForSave = millProductionService.save(millProduction);
								
								if(millProductionForSave.getMillproductionid()>0){
									String productionId = millProductionForSave.getProductionid();
									SmsProduction newSmsProduction =  new SmsProduction();
									newSmsProduction.setProductionid(productionId);
									SmsProduction smsProduction =  new SmsProduction();
									smsProduction = smsProductionService.findDetailsByEmail(newSmsProduction);
									if(smsProduction != null){
										smsProduction.setIsmillproductionupdated("yes");
										smsProduction.setRmname(millProductionForSave.getRmname());
										SmsProduction smsProductionForUpdate = smsProductionService.save(smsProduction);
									}									

									LaboratoryLog newLaboratoryLog =  new LaboratoryLog();
									newLaboratoryLog.setProductionid(productionId);	
									LaboratoryLog laboratoryLog =  new LaboratoryLog();
									laboratoryLog = laboratoryLogService.findDetailsByEmail(newLaboratoryLog);
									if(laboratoryLog != null){
										laboratoryLog.setIsmillproductionupdated("yes");
										laboratoryLog.setRmname(millProductionForSave.getRmname());
										LaboratoryLog laboratoryLogForUpdate = laboratoryLogService.save(laboratoryLog);
									}	

									ProductionLog newProductionLog =  new ProductionLog();
									newProductionLog.setProductionid(productionId);
									ProductionLog productionLog =  new ProductionLog();
									productionLog = productionLogService.findDetailsByEmail(newProductionLog);
									if(productionLog != null){
										productionLog.setIsmillproductionupdated("yes");
										productionLog.setRmname(millProductionForSave.getRmname());
										ProductionLog productionLogForUpdate = productionLogService.save(productionLog);
									}									

									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("MillProduction Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! MillProduction not saved ... Try Again");
								}
								
							}
						}else if(millProduction.getAction().equalsIgnoreCase("update")){
							millProduction.setUpdatedat(UtilityRestController.getCurrentDateTime());
							millProduction.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							millProduction.setAction("update");
							MillProduction millProductionForUpdate = millProductionService.update(millProduction);
							if(millProductionForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("MillProduction Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! MillProduction not Updated ... Try Again");
							}
						}else if(millProduction.getAction().equalsIgnoreCase("delete")){
							srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
							srmbResponse.setMessage("Error! MillProduction Deletion not possible");
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
