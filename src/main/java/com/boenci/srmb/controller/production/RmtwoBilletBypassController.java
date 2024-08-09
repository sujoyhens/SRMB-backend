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
import com.boenci.srmb.model.production.RmtwoBilletBypass;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.RmtwoBilletBypassService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rmtwobilletbypass")
@CrossOrigin(origins = "*")
public class RmtwoBilletBypassController {
	
	@Autowired
	private RmtwoBilletBypassService rmtwoBilletBypassService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchrmtwobilletbypass")
	public String fetchAllRmtwoBilletBypass(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode rmtwoBilletBypassRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = rmtwoBilletBypassRequestNode.path("caller");
				JsonNode jsonSearchType= rmtwoBilletBypassRequestNode.path("searchtype");
				JsonNode jsonSearchContent= rmtwoBilletBypassRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RmtwoBilletBypass newRmtwoBilletBypass  = new RmtwoBilletBypass();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(rmtwoBilletBypassRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newRmtwoBilletBypass.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("coldbilletbypassid")){
							newRmtwoBilletBypass.setColdbilletbypassid(Long.valueOf(strSearchContent));
						}
						List<RmtwoBilletBypass> listRmtwoBilletBypass = rmtwoBilletBypassService.findRmtwoBilletBypassBySearchType(newRmtwoBilletBypass,strSearchType);
						if((listRmtwoBilletBypass != null) && (listRmtwoBilletBypass.size()>0)) {
							srmbResponse.setResultList(listRmtwoBilletBypass);
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
	
	@PostMapping("/cudrmtwobilletbypass")
	public String rmtwoBilletBypass(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long rmtwoBilletBypassedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRmtwoBilletBypassNode = jsonNode.path("rmtwobilletbypass");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					RmtwoBilletBypass rmtwoBilletBypass =  new RmtwoBilletBypass();
					rmtwoBilletBypass = objectMapper.treeToValue(jsonRmtwoBilletBypassNode, RmtwoBilletBypass.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(rmtwoBilletBypass.getAction().equalsIgnoreCase("save")){
							RmtwoBilletBypass newRmtwoBilletBypass = rmtwoBilletBypassService.validRmtwoBilletBypass(rmtwoBilletBypass);
							if(newRmtwoBilletBypass != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("RmtwoBilletBypass Name Already Exist...");
							}else{
								rmtwoBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
								rmtwoBilletBypass.setStatus("active");
								rmtwoBilletBypass.setDeletedflag("no");
								rmtwoBilletBypass.setAction("save");
								rmtwoBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								RmtwoBilletBypass rmtwoBilletBypassForSave = rmtwoBilletBypassService.save(rmtwoBilletBypass);
								
								if(rmtwoBilletBypassForSave.getRmtwobilletbypassid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("RmtwoBilletBypass Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! RmtwoBilletBypass not saved ... Try Again");
								}
								
							}
						}else if(rmtwoBilletBypass.getAction().equalsIgnoreCase("update")){
							rmtwoBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmtwoBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmtwoBilletBypass.setAction("update");
							RmtwoBilletBypass rmtwoBilletBypassForUpdate = rmtwoBilletBypassService.update(rmtwoBilletBypass);
							if(rmtwoBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmtwoBilletBypass Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmtwoBilletBypass not Updated ... Try Again");
							}
						}else if(rmtwoBilletBypass.getAction().equalsIgnoreCase("delete")){
							rmtwoBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmtwoBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmtwoBilletBypass.setAction("delete");
							rmtwoBilletBypass.setDeletedflag("yes");
							rmtwoBilletBypass.setStatus("inactive");
							RmtwoBilletBypass rmtwoBilletBypassForUpdate = rmtwoBilletBypassService.update(rmtwoBilletBypass);
							if(rmtwoBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmtwoBilletBypass deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmtwoBilletBypass not deleted ... Try Again");
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
