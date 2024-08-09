package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.BearingMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.BearingMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/bearingmaster")
@CrossOrigin(origins = "*")
public class BearingMasterController {
	
	@Autowired
	private BearingMasterService bearingMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchbearingmaster")
	public String fetchAllBearingMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode bearingMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = bearingMasterRequestNode.path("caller");
				JsonNode jsonSearchType= bearingMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= bearingMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				BearingMaster newBearingMaster  = new BearingMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(bearingMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newBearingMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("unitmasterid")){
						newBearingMaster.setUnitmasterid(Long.valueOf(strSearchContent));
					}
					List<BearingMaster> listBearingMaster = bearingMasterService.findBearingMasterBySearchType(newBearingMaster,strSearchType);
					if((listBearingMaster != null) && (listBearingMaster.size()>0)) {
						srmbResponse.setResultList(listBearingMaster);
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
	
	@PostMapping("/cudbearingmaster")
	public String bearingMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting bearing master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Bearing Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long bearingMasteredid = 0;
			 try {
					System.out.println("Getting Bearing Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonBearingMasterNode = jsonNode.path("bearingmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					BearingMaster bearingMaster =  new BearingMaster();
					bearingMaster = objectMapper.treeToValue(jsonBearingMasterNode, BearingMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(bearingMaster.getAction().equalsIgnoreCase("save")){
							BearingMaster newBearingMaster = bearingMasterService.validBearingMaster(bearingMaster);
							if(newBearingMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("BearingMaster Name Already Exist...");
							}else{
								bearingMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								bearingMaster.setStatus("active");
								bearingMaster.setDeletedflag("no");
								bearingMaster.setAction("save");
								bearingMaster.setCaller(strCaller);
								bearingMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								BearingMaster bearingMasterForSave = bearingMasterService.save(bearingMaster);
								
								if(bearingMasterForSave.getBearingmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Bearing Master Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Bearing Master not saved ... Try Again");
								}
								
							}
						}else if(bearingMaster.getAction().equalsIgnoreCase("update")){
							bearingMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							bearingMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							bearingMaster.setAction("update");
							BearingMaster bearingMasterForUpdate = bearingMasterService.update(bearingMaster);
							if(bearingMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("BearingMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! BearingMaster not Updated ... Try Again");
							}
						}else if(bearingMaster.getAction().equalsIgnoreCase("delete")){
							bearingMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							bearingMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							bearingMaster.setAction("delete");
							bearingMaster.setDeletedflag("yes");
							bearingMaster.setStatus("inactive");
							BearingMaster bearingMasterForDelete = bearingMasterService.delete(bearingMaster);
							if(bearingMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("BearingMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! BearingMaster not Deleted ... Try Again");
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
