package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.MechanicalMaintenance;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.MechanicalMaintenanceService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/mechanicalmaintenance")
@CrossOrigin(origins = "*")
public class MechanicalMaintenanceController {
	
	@Autowired
	private MechanicalMaintenanceService mechanicalMaintenanceService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchmechanicalmaintenance")
	public String fetchAllMechanicalMaintenance(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode mechanicalMaintenanceRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = mechanicalMaintenanceRequestNode.path("caller");
				JsonNode jsonSearchType= mechanicalMaintenanceRequestNode.path("searchtype");
				JsonNode jsonSearchContent= mechanicalMaintenanceRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				MechanicalMaintenance newMechanicalMaintenance  = new MechanicalMaintenance();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(mechanicalMaintenanceRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newMechanicalMaintenance.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("equipmentmasterid")){
						newMechanicalMaintenance.setEquipmentmasterid(Long.valueOf(strSearchContent));
					}else if(strSearchType.equalsIgnoreCase("subequipmentmasterid")){
						newMechanicalMaintenance.setSubequipmentmasterid(Long.valueOf(strSearchContent));
					}
					List<MechanicalMaintenance> listMechanicalMaintenance = mechanicalMaintenanceService.findMechanicalMaintenanceBySearchType(newMechanicalMaintenance,strSearchType);
					if((listMechanicalMaintenance != null) && (listMechanicalMaintenance.size()>0)) {
						srmbResponse.setResultList(listMechanicalMaintenance);
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
	
	@PostMapping("/cudmechanicalmaintenance")
	public String mechanicalMaintenance(@RequestBody String jsonBody) {	
			 System.out.println("Getting area master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Mechanical Maintenance Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long mechanicalMaintenanceedid = 0;
			 try {
					System.out.println("Getting Mechanical Maintenance details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonMechanicalMaintenanceNode = jsonNode.path("mechanicalmaintenance");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					MechanicalMaintenance mechanicalMaintenance =  new MechanicalMaintenance();
					mechanicalMaintenance = objectMapper.treeToValue(jsonMechanicalMaintenanceNode, MechanicalMaintenance.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(mechanicalMaintenance.getAction().equalsIgnoreCase("save")){
							MechanicalMaintenance newMechanicalMaintenance = mechanicalMaintenanceService.validMechanicalMaintenance(mechanicalMaintenance);
							if(newMechanicalMaintenance != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("MechanicalMaintenance Name Already Exist...");
							}else{
								mechanicalMaintenance.setCreatedat(UtilityRestController.getCurrentDateTime());
								mechanicalMaintenance.setStatus("active");
								mechanicalMaintenance.setDeletedflag("no");
								mechanicalMaintenance.setAction("save");
								mechanicalMaintenance.setCaller(strCaller);
								mechanicalMaintenance.setCreatedby(String.valueOf(userRegister.getUserregisterid()));								
								MechanicalMaintenance mechanicalMaintenanceForSave = mechanicalMaintenanceService.save(mechanicalMaintenance);
								
								if(mechanicalMaintenanceForSave.getMechanicalmaintenanceid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("Mechanical Maintenance Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! Mechanical Maintenance not saved ... Try Again");
								}
								
							}
						}else if(mechanicalMaintenance.getAction().equalsIgnoreCase("update")){
							mechanicalMaintenance.setUpdatedat(UtilityRestController.getCurrentDateTime());
							mechanicalMaintenance.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							mechanicalMaintenance.setAction("update");
							MechanicalMaintenance mechanicalMaintenanceForUpdate = mechanicalMaintenanceService.update(mechanicalMaintenance);
							if(mechanicalMaintenanceForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("MechanicalMaintenance Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! MechanicalMaintenance not Updated ... Try Again");
							}
						}else if(mechanicalMaintenance.getAction().equalsIgnoreCase("delete")){
							mechanicalMaintenance.setUpdatedat(UtilityRestController.getCurrentDateTime());
							mechanicalMaintenance.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							mechanicalMaintenance.setAction("delete");
							mechanicalMaintenance.setDeletedflag("yes");
							mechanicalMaintenance.setStatus("inactive");
							MechanicalMaintenance mechanicalMaintenanceForDelete = mechanicalMaintenanceService.delete(mechanicalMaintenance);
							if(mechanicalMaintenanceForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("MechanicalMaintenance Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! MechanicalMaintenance not Deleted ... Try Again");
							}
						}
					}else {
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! user not Registered ... Try Again");
					}
					
				  }catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! user not Registered ... Try Again");
					} catch (IOException e) {
						e.printStackTrace();
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error!  user not Registered ... Try Again");
					}
			 
				String finalJsonResponse = UtilityRestController.toJson(srmbResponse);
				return new String(finalJsonResponse);
	}	

}
