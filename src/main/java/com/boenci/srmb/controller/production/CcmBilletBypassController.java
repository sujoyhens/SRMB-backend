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
import com.boenci.srmb.model.production.CcmBilletBypass;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.CcmBilletBypassService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/ccmbilletbypass")
@CrossOrigin(origins = "*")
public class CcmBilletBypassController {
	
	@Autowired
	private CcmBilletBypassService ccmBilletBypassService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchccmbilletbypass")
	public String fetchAllCcmBilletBypass(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode ccmBilletBypassRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = ccmBilletBypassRequestNode.path("caller");
				JsonNode jsonSearchType= ccmBilletBypassRequestNode.path("searchtype");
				JsonNode jsonSearchContent= ccmBilletBypassRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				CcmBilletBypass newCcmBilletBypass  = new CcmBilletBypass();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(ccmBilletBypassRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newCcmBilletBypass.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("coldbilletbypassid")){
							newCcmBilletBypass.setColdbilletbypassid(Long.valueOf(strSearchContent));
						}
						List<CcmBilletBypass> listCcmBilletBypass = ccmBilletBypassService.findCcmBilletBypassBySearchType(newCcmBilletBypass,strSearchType);
						if((listCcmBilletBypass != null) && (listCcmBilletBypass.size()>0)) {
							srmbResponse.setResultList(listCcmBilletBypass);
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
	
	@PostMapping("/cudccmbilletbypass")
	public String ccmBilletBypass(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long ccmBilletBypassedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonCcmBilletBypassNode = jsonNode.path("ccmbilletbypass");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					CcmBilletBypass ccmBilletBypass =  new CcmBilletBypass();
					ccmBilletBypass = objectMapper.treeToValue(jsonCcmBilletBypassNode, CcmBilletBypass.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(ccmBilletBypass.getAction().equalsIgnoreCase("save")){
							CcmBilletBypass newCcmBilletBypass = ccmBilletBypassService.validCcmBilletBypass(ccmBilletBypass);
							if(newCcmBilletBypass != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("CcmBilletBypass Name Already Exist...");
							}else{
								ccmBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
								ccmBilletBypass.setStatus("active");
								ccmBilletBypass.setDeletedflag("no");
								ccmBilletBypass.setAction("save");
								ccmBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								CcmBilletBypass ccmBilletBypassForSave = ccmBilletBypassService.save(ccmBilletBypass);
								if(ccmBilletBypassForSave.getCcmbilletbypassid()>0){


									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("CcmBilletBypass Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! CcmBilletBypass not saved ... Try Again");
								}
								
							}
						}else if(ccmBilletBypass.getAction().equalsIgnoreCase("update")){
							ccmBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							ccmBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							ccmBilletBypass.setAction("update");
							CcmBilletBypass ccmBilletBypassForUpdate = ccmBilletBypassService.update(ccmBilletBypass);
							if(ccmBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("CcmBilletBypass Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! CcmBilletBypass not Updated ... Try Again");
							}
						}else if(ccmBilletBypass.getAction().equalsIgnoreCase("delete")){
							ccmBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							ccmBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							ccmBilletBypass.setAction("delete");
							ccmBilletBypass.setDeletedflag("yes");
							ccmBilletBypass.setStatus("inactive");
							CcmBilletBypass ccmBilletBypassForUpdate = ccmBilletBypassService.delete(ccmBilletBypass);
							if(ccmBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("CcmBilletBypass Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! CcmBilletBypass not Deleted ... Try Again");
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
