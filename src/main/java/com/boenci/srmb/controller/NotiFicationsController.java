package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.NotiFications;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.NotiFicationsService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotiFicationsController {
	
	@Autowired
	private NotiFicationsService notiFicationsService;

	@Autowired
	private UserRegisterService userRegisterService;

	
	@PostMapping("/fetchnotifications")
	public String fetchAllNotiFications(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode notiFicationsRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = notiFicationsRequestNode.path("caller");
				JsonNode jsonSearchType= notiFicationsRequestNode.path("searchtype");
				JsonNode jsonSearchContent= notiFicationsRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				NotiFications newNotiFications  = new NotiFications();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(notiFicationsRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newNotiFications.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("notificationdate")){
							newNotiFications.setNotificationdate(strSearchContent);
							newNotiFications.setUserregisterid(userRegister.getUserregisterid());
						}else if(strSearchType.equalsIgnoreCase("all")){
							newNotiFications.setUserregisterid(userRegister.getUserregisterid());
						}
						List<NotiFications> listNotiFications = notiFicationsService.findNotiFicationsBySearchType(newNotiFications,strSearchType);
						if((listNotiFications != null) && (listNotiFications.size()>0)) {
							srmbResponse.setResultList(listNotiFications);
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
	
	@PostMapping("/cudnotifications")
	public String notiFications(@RequestBody String jsonBody) {	
			SrmbResponse srmbResponse = new SrmbResponse();
			 System.out.println("Getting notification  details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("notification Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long notiFicationsedid = 0;
			 try {
					System.out.println("Getting notification details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonNotiFicationsNode = jsonNode.path("notifications");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					NotiFications notiFications =  new NotiFications();
					notiFications = objectMapper.treeToValue(jsonNotiFicationsNode, NotiFications.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(notiFications.getAction().equalsIgnoreCase("save")){
							NotiFications newNotiFications = notiFicationsService.validNotiFications(notiFications);
							if(newNotiFications != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("NotiFications Name Already Exist...");
							}else{
								notiFications.setCreatedat(UtilityRestController.getCurrentDateTime());
								notiFications.setStatus("active");
								notiFications.setDeletedflag("no");
								notiFications.setAction("save");
								notiFications.setCaller(strCaller);
								notiFications.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								NotiFications notiFicationsForSave = notiFicationsService.save(notiFications);
								
								if(notiFicationsForSave.getNotificationsid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("NotiFications Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! NotiFications not saved ... Try Again");
								}
								
							}
						}else if(notiFications.getAction().equalsIgnoreCase("update")){
							notiFications.setUpdatedat(UtilityRestController.getCurrentDateTime());
							notiFications.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							notiFications.setAction("update");
							NotiFications notiFicationsForUpdate = notiFicationsService.update(notiFications);
							if(notiFicationsForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("NotiFications Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! NotiFications not Updated ... Try Again");
							}
						}else if(notiFications.getAction().equalsIgnoreCase("delete")){
							notiFications.setUpdatedat(UtilityRestController.getCurrentDateTime());
							notiFications.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							notiFications.setAction("delete");
							notiFications.setDeletedflag("yes");
							notiFications.setStatus("inactive");
							NotiFications notiFicationsForDelete = notiFicationsService.delete(notiFications);
							if(notiFicationsForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("NotiFications Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! NotiFications not Deleted ... Try Again");
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
