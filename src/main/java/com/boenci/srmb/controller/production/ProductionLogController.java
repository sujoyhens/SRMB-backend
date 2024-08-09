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
import com.boenci.srmb.model.production.ProductionLog;
import com.boenci.srmb.model.production.SmsProduction;
import com.boenci.srmb.model.production.SequenceParameter;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.LaboratoryLogService;
import com.boenci.srmb.service.production.ProductionLogService;
import com.boenci.srmb.service.production.SequenceParameterService;
import com.boenci.srmb.service.production.SmsProductionService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.boenci.srmb.model.SrmbResponse;



@RestController
@RequestMapping("/productionlog")
@CrossOrigin(origins = "*")
public class ProductionLogController {
	
	@Autowired
	private ProductionLogService productionLogService;

	@Autowired
	private SmsProductionService smsProductionService;

	@Autowired
	private LaboratoryLogService laboratoryLogService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	private SequenceParameterService sequenceParameterService;

	

	@PostMapping("/fetchproductionlog")
	public String fetchAllProductionLog(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode productionLogRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = productionLogRequestNode.path("caller");
				JsonNode jsonSearchType= productionLogRequestNode.path("searchtype");
				JsonNode jsonSearchContent= productionLogRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				ProductionLog newProductionLog  = new ProductionLog();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(productionLogRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newProductionLog.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productionlogdate")){
							newProductionLog.setProductionlogdate(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("millproduction")){
							newProductionLog.setUnitmasterid(Long.valueOf(strSearchContent));
						}
						List<ProductionLog> listProductionLog = productionLogService.findProductionLogBySearchType(newProductionLog,strSearchType);
						if((listProductionLog != null) && (listProductionLog.size()>0)) {
							srmbResponse.setResultList(listProductionLog);
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
	
	@PostMapping("/cudproductionlog")
	public String productionLog(@RequestBody String jsonBody) {	
			 System.out.println("Getting Production Log details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 SrmbResponse srmbResponse = new SrmbResponse();
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Production Log Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long productionLogedid = 0;
			 try {
					System.out.println("Getting Production Log details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonProductionLogNode = jsonNode.path("productionlog");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonSequenceParameterNode = jsonNode.path("sequenceparameter");					
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					ProductionLog productionLog =  new ProductionLog();
					productionLog = objectMapper.treeToValue(jsonProductionLogNode, ProductionLog.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(productionLog.getAction().equalsIgnoreCase("save")){
							ProductionLog newProductionLog = productionLogService.validProductionLog(productionLog);
							if(newProductionLog != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("ProductionLog Name Already Exist...");
							}else{
								productionLog.setCreatedat(UtilityRestController.getCurrentDateTime());
								productionLog.setStatus("active");
								productionLog.setDeletedflag("no");
								productionLog.setAction("save");
								productionLog.setCaller(strCaller);
								productionLog.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								productionLog.setIsmillproductionupdated("no");	
								productionLog.setRmname("notdecided");	
								productionLog.setUserregisterid(userRegister.getUserregisterid());						
								ProductionLog productionLogForSave = productionLogService.save(productionLog);
								
								if(productionLogForSave.getProductionlogid()>0){

									String productionId = productionLogForSave.getProductionid();
									SmsProduction newSmsProduction =  new SmsProduction();
									newSmsProduction.setProductionid(productionId);
									SmsProduction smsProduction =  new SmsProduction();
									smsProduction = smsProductionService.findDetailsByEmail(newSmsProduction);
									if(smsProduction != null){
										smsProduction.setIsproductionlogupdated("yes");
										smsProduction.setCcmname(productionLogForSave.getCcmname());
										SmsProduction smsProductionForUpdate = smsProductionService.save(smsProduction);
									}									

									LaboratoryLog newLaboratoryLog =  new LaboratoryLog();
									newLaboratoryLog.setProductionid(productionId);	
									LaboratoryLog laboratoryLog =  new LaboratoryLog();
									laboratoryLog = laboratoryLogService.findDetailsByEmail(newLaboratoryLog);
									if(laboratoryLog != null){
										laboratoryLog.setIsproductionlogupdated("yes");
										laboratoryLog.setCcmname(productionLogForSave.getCcmname());
										LaboratoryLog laboratoryLogForUpdate = laboratoryLogService.save(laboratoryLog);
									}							
									

									//Sequence parameters added
									for(int ndx = 0; ndx<jsonSequenceParameterNode.size(); ndx++){
										JsonNode firnode = jsonSequenceParameterNode.get(ndx);
										SequenceParameter sequenceParameter =  new SequenceParameter();
										sequenceParameter = objectMapper.treeToValue(firnode, SequenceParameter.class);
										sequenceParameter.setCreatedat(UtilityRestController.getCurrentDateTime());
										sequenceParameter.setStatus("active");
										sequenceParameter.setDeletedflag("no");
										sequenceParameter.setAction("save");
										sequenceParameter.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
										sequenceParameter.setProductionlogid(productionLogForSave.getProductionlogid());																	
										SequenceParameter sequenceParameterForSave = sequenceParameterService.save(sequenceParameter);
									}
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("ProductionLog Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! ProductionLog not saved ... Try Again");
								}
								
							}
						}else if(productionLog.getAction().equalsIgnoreCase("update")){
							productionLog.setUpdatedat(UtilityRestController.getCurrentDateTime());
							productionLog.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							productionLog.setAction("update");
							ProductionLog productionLogForUpdate = productionLogService.update(productionLog);
							if(productionLogForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ProductionLog Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ProductionLog not Updated ... Try Again");
							}
						}else if(productionLog.getAction().equalsIgnoreCase("delete")){
							srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
							srmbResponse.setMessage("Error! ProductionLog Deletion not possible");
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
