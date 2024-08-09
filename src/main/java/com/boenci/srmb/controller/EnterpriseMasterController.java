package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.EnterpriseMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.EnterpriseMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/enterprisemaster")
@CrossOrigin(origins = "*")
public class EnterpriseMasterController {
	
	@Autowired
	private EnterpriseMasterService enterpriseMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchenterprisemaster")
	public String fetchAllEnterpriseMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonNode enterpriseMasterRequestNode = objectMapper.readTree(jsonBody);
			JsonNode jsonCaller = enterpriseMasterRequestNode.path("caller");
			JsonNode jsonSearchType= enterpriseMasterRequestNode.path("searchtype");
			JsonNode jsonSearchContent= enterpriseMasterRequestNode.path("searchcontent");
			String strSearchType = jsonSearchType.asText();
			String strSearchContent = jsonSearchContent.asText();
			EnterpriseMaster newEnterpriseMaster  = new EnterpriseMaster();
			UserRegister userRegister = userRegisterService.validateUserFromAPI(enterpriseMasterRequestNode);
			if(userRegister != null){
				if(strSearchType.equalsIgnoreCase("status")){
					newEnterpriseMaster.setStatus(strSearchContent);
				}else if(strSearchType.equalsIgnoreCase("enterprisemasterid")){
					newEnterpriseMaster.setEnterprisemasterid(Long.valueOf(strSearchContent));
				}
				List<EnterpriseMaster> listEnterpriseMaster = enterpriseMasterService.findEnterpriseMasterBySearchType(newEnterpriseMaster,strSearchType);
				if((listEnterpriseMaster != null) && (listEnterpriseMaster.size()>0)) {
					srmbResponse.setResultList(listEnterpriseMaster);
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
	
	@PostMapping("/cudenterprisemaster")
	public String enterpriseMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Area Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long enterpriseMasteredid = 0;
			 try {
					System.out.println("Getting Area Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonEnterpriseMasterNode = jsonNode.path("enterprisemaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					EnterpriseMaster enterpriseMaster =  new EnterpriseMaster();
					enterpriseMaster = objectMapper.treeToValue(jsonEnterpriseMasterNode, EnterpriseMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(enterpriseMaster.getAction().equalsIgnoreCase("save")){
							EnterpriseMaster newEnterpriseMaster = enterpriseMasterService.validEnterpriseMaster(enterpriseMaster);
							if(newEnterpriseMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("EnterpriseMaster Name Already Exist...");
							}else{
								enterpriseMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								enterpriseMaster.setStatus("active");
								enterpriseMaster.setDeletedflag("no");
								enterpriseMaster.setAction("save");
								enterpriseMaster.setCaller(strCaller);
								enterpriseMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								EnterpriseMaster enterpriseMasterForSave = enterpriseMasterService.save(enterpriseMaster);
								
								if(enterpriseMasterForSave.getEnterprisemasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("EnterpriseMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! EnterpriseMaster not saved ... Try Again");
								}
								
							}
						}else if(enterpriseMaster.getAction().equalsIgnoreCase("update")){
							enterpriseMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							enterpriseMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							enterpriseMaster.setAction("update");
							EnterpriseMaster enterpriseMasterForUpdate = enterpriseMasterService.update(enterpriseMaster);
							if(enterpriseMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("EnterpriseMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! EnterpriseMaster not Updated ... Try Again");
							}
						}else if(enterpriseMaster.getAction().equalsIgnoreCase("delete")){
							enterpriseMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							enterpriseMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							enterpriseMaster.setAction("delete");
							enterpriseMaster.setDeletedflag("yes");
							enterpriseMaster.setStatus("inactive");
							EnterpriseMaster enterpriseMasterForDelete = enterpriseMasterService.delete(enterpriseMaster);
							if(enterpriseMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("EnterpriseMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! EnterpriseMaster not Deleted ... Try Again");
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
