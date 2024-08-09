package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.ProductMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.ProductMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/productmaster")
@CrossOrigin(origins = "*")
public class ProductMasterController {
	
	@Autowired
	private ProductMasterService productMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchproductmaster")
	public String fetchAllProductMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode productMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = productMasterRequestNode.path("caller");
				JsonNode jsonSearchType= productMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= productMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				ProductMaster newProductMaster  = new ProductMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(productMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newProductMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("plantmasterid")){
							newProductMaster.setPlantmasterid(Long.valueOf(strSearchContent));
						}
						List<ProductMaster> listProductMaster = productMasterService.findProductMasterBySearchType(newProductMaster,strSearchType);
						if((listProductMaster != null) && (listProductMaster.size()>0)) {
							srmbResponse.setResultList(listProductMaster);
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
	
	@PostMapping("/cudproductmaster")
	public String productMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Product Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long productMasteredid = 0;
			 try {
					System.out.println("Getting Product Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonProductMasterNode = jsonNode.path("productmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					ProductMaster productMaster =  new ProductMaster();
					productMaster = objectMapper.treeToValue(jsonProductMasterNode, ProductMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(productMaster.getAction().equalsIgnoreCase("save")){
							ProductMaster newProductMaster = productMasterService.validProductMaster(productMaster);
							if(newProductMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("ProductMaster Name Already Exist...");
							}else{
								productMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								productMaster.setStatus("active");
								productMaster.setDeletedflag("no");
								productMaster.setAction("save");
								productMaster.setCaller(strCaller);
								productMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								ProductMaster productMasterForSave = productMasterService.save(productMaster);
								
								if(productMasterForSave.getProductmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("ProductMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! ProductMaster not saved ... Try Again");
								}
								
							}
						}else if(productMaster.getAction().equalsIgnoreCase("update")){
							productMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							productMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							productMaster.setAction("update");
							ProductMaster productMasterForUpdate = productMasterService.update(productMaster);
							if(productMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ProductMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ProductMaster not Updated ... Try Again");
							}
						}else if(productMaster.getAction().equalsIgnoreCase("delete")){
							productMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							productMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							productMaster.setAction("delete");
							productMaster.setDeletedflag("yes");
							productMaster.setStatus("inactive");
							ProductMaster productMasterForDelete = productMasterService.delete(productMaster);
							if(productMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ProductMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ProductMaster not Deleted ... Try Again");
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
