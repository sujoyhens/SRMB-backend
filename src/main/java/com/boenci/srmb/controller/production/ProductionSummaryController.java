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
import com.boenci.srmb.model.production.ProductionSummary;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.ProductionSummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/productionsummary")
@CrossOrigin(origins = "*")
public class ProductionSummaryController {
	
	@Autowired
	private ProductionSummaryService productionSummaryService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchproductionsummary")
	public String fetchAllProductionSummary(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode productionSummaryRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = productionSummaryRequestNode.path("caller");
				JsonNode jsonSearchType= productionSummaryRequestNode.path("searchtype");
				JsonNode jsonSearchContent= productionSummaryRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				ProductionSummary newProductionSummary  = new ProductionSummary();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(productionSummaryRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newProductionSummary.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("productiondate")){
							newProductionSummary.setProductiondate(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("daterange")){
							newProductionSummary.setProductiondate(strSearchContent);
						}
						List<ProductionSummary> listProductionSummary = productionSummaryService.findProductionSummaryBySearchType(newProductionSummary,strSearchType);
						if((listProductionSummary != null) && (listProductionSummary.size()>0)) {
							srmbResponse.setResultList(listProductionSummary);
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
	
	@PostMapping("/cudproductionsummary")
	public String productionSummary(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long productionSummaryedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonProductionSummaryNode = jsonNode.path("productionsummary");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					ProductionSummary productionSummary =  new ProductionSummary();
					productionSummary = objectMapper.treeToValue(jsonProductionSummaryNode, ProductionSummary.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(productionSummary.getAction().equalsIgnoreCase("save")){
							ProductionSummary newProductionSummary = productionSummaryService.validProductionSummary(productionSummary);
							if(newProductionSummary != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("ProductionSummary Name Already Exist...");
							}else{
								productionSummary.setCreatedat(UtilityRestController.getCurrentDateTime());
								productionSummary.setStatus("active");
								productionSummary.setDeletedflag("no");
								productionSummary.setAction("save");
								productionSummary.setCaller(strCaller);
								productionSummary.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								productionSummary.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								productionSummary.setPlantmasterid(userRegister.getPlantmasterid());
								ProductionSummary productionSummaryForSave = productionSummaryService.save(productionSummary);
								
								if(productionSummaryForSave.getProductionsummaryid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("ProductionSummary Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! ProductionSummary not saved ... Try Again");
								}
								
							}
						}else if(productionSummary.getAction().equalsIgnoreCase("update")){
							productionSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							productionSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							productionSummary.setAction("update");
							ProductionSummary productionSummaryForUpdate = productionSummaryService.update(productionSummary);
							if(productionSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ProductionSummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ProductionSummary not Updated ... Try Again");
							}
						}else if(productionSummary.getAction().equalsIgnoreCase("delete")){
							productionSummary.setUpdatedat(UtilityRestController.getCurrentDateTime());
							productionSummary.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							productionSummary.setAction("delete");
							productionSummary.setDeletedflag("yes");
							productionSummary.setStatus("inactive");
							ProductionSummary productionSummaryForUpdate = productionSummaryService.update(productionSummary);
							if(productionSummaryForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ProductionSummary Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ProductionSummary not Updated ... Try Again");
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
