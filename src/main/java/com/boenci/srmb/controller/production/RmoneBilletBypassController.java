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
import com.boenci.srmb.model.production.RmoneBilletBypass;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.RmoneBilletBypassService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rmonebilletbypass")
@CrossOrigin(origins = "*")
public class RmoneBilletBypassController {
	
	@Autowired
	private RmoneBilletBypassService rmoneBilletBypassService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchrmonebilletbypass")
	public String fetchAllRmoneBilletBypass(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode rmoneBilletBypassRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = rmoneBilletBypassRequestNode.path("caller");
				JsonNode jsonSearchType= rmoneBilletBypassRequestNode.path("searchtype");
				JsonNode jsonSearchContent= rmoneBilletBypassRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RmoneBilletBypass newRmoneBilletBypass  = new RmoneBilletBypass();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(rmoneBilletBypassRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newRmoneBilletBypass.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("coldbilletbypassid")){
							newRmoneBilletBypass.setColdbilletbypassid(Long.valueOf(strSearchContent));
						}
						List<RmoneBilletBypass> listRmoneBilletBypass = rmoneBilletBypassService.findRmoneBilletBypassBySearchType(newRmoneBilletBypass,strSearchType);
						if((listRmoneBilletBypass != null) && (listRmoneBilletBypass.size()>0)) {
							srmbResponse.setResultList(listRmoneBilletBypass);
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
	
	@PostMapping("/cudrmonebilletbypass")
	public String rmoneBilletBypass(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long rmoneBilletBypassedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRmoneBilletBypassNode = jsonNode.path("rmonebilletbypass");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					RmoneBilletBypass rmoneBilletBypass =  new RmoneBilletBypass();
					rmoneBilletBypass = objectMapper.treeToValue(jsonRmoneBilletBypassNode, RmoneBilletBypass.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(rmoneBilletBypass.getAction().equalsIgnoreCase("save")){
							RmoneBilletBypass newRmoneBilletBypass = rmoneBilletBypassService.validRmoneBilletBypass(rmoneBilletBypass);
							if(newRmoneBilletBypass != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("RmoneBilletBypass Name Already Exist...");
							}else{
								rmoneBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
								rmoneBilletBypass.setStatus("active");
								rmoneBilletBypass.setDeletedflag("no");
								rmoneBilletBypass.setAction("save");
								rmoneBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								RmoneBilletBypass rmoneBilletBypassForSave = rmoneBilletBypassService.save(rmoneBilletBypass);
								
								if(rmoneBilletBypassForSave.getRmonebilletbypassid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("RmoneBilletBypass Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! RmoneBilletBypass not saved ... Try Again");
								}
								
							}
						}else if(rmoneBilletBypass.getAction().equalsIgnoreCase("update")){
							rmoneBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmoneBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmoneBilletBypass.setAction("update");
							RmoneBilletBypass rmoneBilletBypassForUpdate = rmoneBilletBypassService.update(rmoneBilletBypass);
							if(rmoneBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmoneBilletBypass Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmoneBilletBypass not Updated ... Try Again");
							}
						}else if(rmoneBilletBypass.getAction().equalsIgnoreCase("delete")){
							rmoneBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							rmoneBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							rmoneBilletBypass.setAction("delete");
							rmoneBilletBypass.setDeletedflag("yes");
							rmoneBilletBypass.setStatus("inactive");
							RmoneBilletBypass rmoneBilletBypassForUpdate = rmoneBilletBypassService.delete(rmoneBilletBypass);
							if(rmoneBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("RmoneBilletBypass Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! RmoneBilletBypass not Deleted ... Try Again");
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
