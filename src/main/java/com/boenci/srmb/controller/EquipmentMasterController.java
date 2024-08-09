package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.boenci.srmb.model.EquipmentMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.EquipmentMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/equipmentmaster")
@CrossOrigin(origins = "*")
public class EquipmentMasterController {
	
	@Autowired
	private EquipmentMasterService equipmentMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	

	@PostMapping("/fetchequipmentmaster")
	public String fetchAllEquipmentMaster(@RequestBody String jsonBody){
		SrmbResponse srmbResponse = new SrmbResponse();
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode equipmentMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = equipmentMasterRequestNode.path("caller");
				JsonNode jsonSearchType= equipmentMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= equipmentMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				EquipmentMaster newEquipmentMaster  = new EquipmentMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(equipmentMasterRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newEquipmentMaster.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("areamasterid")){
						newEquipmentMaster.setAreamasterid(Long.valueOf(strSearchContent));
					}else if(strSearchType.equalsIgnoreCase("frequency")){
						newEquipmentMaster.setFrequency(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("unitmasterid")){
						newEquipmentMaster.setUnitmasterid(Long.valueOf(strSearchContent));
					}
					List<EquipmentMaster> listEquipmentMaster = equipmentMasterService.findEquipmentMasterBySearchType(newEquipmentMaster,strSearchType);
					if((listEquipmentMaster != null) && (listEquipmentMaster.size()>0)) {
						srmbResponse.setResultList(listEquipmentMaster);
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
	
	@PostMapping("/cudequipmentmaster")
	public String equipmentMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting EquipmentMaster details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 SrmbResponse srmbResponse = new SrmbResponse();
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("EquipmentMaster Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long equipmentMasteredid = 0;
			 try {
					System.out.println("Getting EquipmentMaster details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonEquipmentMasterNode = jsonNode.path("equipmentmaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					EquipmentMaster equipmentMaster =  new EquipmentMaster();
					equipmentMaster = objectMapper.treeToValue(jsonEquipmentMasterNode, EquipmentMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(equipmentMaster.getAction().equalsIgnoreCase("save")){
							EquipmentMaster newEquipmentMaster = equipmentMasterService.validEquipmentMaster(equipmentMaster);
							if(newEquipmentMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("EquipmentMaster Name Already Exist...");
							}else{
								equipmentMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								equipmentMaster.setStatus("active");
								equipmentMaster.setDeletedflag("no");
								equipmentMaster.setAction("save");
								equipmentMaster.setCaller(strCaller);
								equipmentMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								EquipmentMaster equipmentMasterForSave = equipmentMasterService.save(equipmentMaster);
								List<EquipmentMaster> equipmentMasterList = new ArrayList<EquipmentMaster>();
								equipmentMasterList.add(equipmentMasterForSave);
								if(equipmentMasterForSave.getEquipmentmasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setResultList(equipmentMasterList);
									srmbResponse.setMessage("EquipmentMaster Added Successfully with Id:" + equipmentMasterForSave.getEquipmentmasterid());
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! EquipmentMaster not saved ... Try Again");
								}
								
							}
						}else if(equipmentMaster.getAction().equalsIgnoreCase("update")){
							equipmentMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							equipmentMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							equipmentMaster.setAction("update");
							EquipmentMaster equipmentMasterForUpdate = equipmentMasterService.update(equipmentMaster);
							if(equipmentMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("EquipmentMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! EquipmentMaster not Updated ... Try Again");
							}
						}else if(equipmentMaster.getAction().equalsIgnoreCase("delete")){
							equipmentMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							equipmentMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							equipmentMaster.setAction("delete");
							equipmentMaster.setDeletedflag("yes");
							equipmentMaster.setStatus("inactive");
							EquipmentMaster equipmentMasterForDelete = equipmentMasterService.delete(equipmentMaster);
							if(equipmentMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("EquipmentMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! EquipmentMaster not Deleted ... Try Again");
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
