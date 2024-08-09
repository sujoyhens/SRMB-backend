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
import com.boenci.srmb.model.production.ColdBilletBypass;
import com.boenci.srmb.model.production.RmoneBilletBypass;
import com.boenci.srmb.model.production.RmtwoBilletBypass;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.CcmBilletBypassService;
import com.boenci.srmb.service.production.ColdBilletBypassService;
import com.boenci.srmb.service.production.RmoneBilletBypassService;
import com.boenci.srmb.service.production.RmtwoBilletBypassService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/coldbilletbypass")
@CrossOrigin(origins = "*")
public class ColdBilletBypassController {
	
	@Autowired
	private ColdBilletBypassService coldBilletBypassService;

	@Autowired
	private RmoneBilletBypassService rmoneBilletBypassService;

	@Autowired
	private RmtwoBilletBypassService rmtwoBilletBypassService;

	@Autowired
	private CcmBilletBypassService ccmBilletBypassService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchcoldbilletbypass")
	public String fetchAllColdBilletBypass(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode coldBilletBypassRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = coldBilletBypassRequestNode.path("caller");
				JsonNode jsonSearchType= coldBilletBypassRequestNode.path("searchtype");
				JsonNode jsonSearchContent= coldBilletBypassRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				ColdBilletBypass newColdBilletBypass  = new ColdBilletBypass();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(coldBilletBypassRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newColdBilletBypass.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("plantmasterid")){
							newColdBilletBypass.setPlantmasterid(Long.valueOf(strSearchContent));
						}
						List<ColdBilletBypass> listColdBilletBypass = coldBilletBypassService.findColdBilletBypassBySearchType(newColdBilletBypass,strSearchType);
						if((listColdBilletBypass != null) && (listColdBilletBypass.size()>0)) {
							srmbResponse.setResultList(listColdBilletBypass);
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
	
	@PostMapping("/cudcoldbilletbypass")
	public String coldBilletBypass(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long coldBilletBypassedid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonColdBilletBypassNode = jsonNode.path("coldbilletbypass");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					ColdBilletBypass coldBilletBypass =  new ColdBilletBypass();
					coldBilletBypass = objectMapper.treeToValue(jsonColdBilletBypassNode, ColdBilletBypass.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(coldBilletBypass.getAction().equalsIgnoreCase("save")){
							JsonNode jsonRmoneBilletBypassNode = jsonNode.path("rmonebilletbypass");
							JsonNode jsonRmtwoBilletBypassNode = jsonNode.path("rmtwobilletbypass");
							JsonNode jsonCcmBilletBypassNode = jsonNode.path("ccmbilletbypass");
							ColdBilletBypass newColdBilletBypass = coldBilletBypassService.validColdBilletBypass(coldBilletBypass);
							if(newColdBilletBypass != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("ColdBilletBypass Name Already Exist...");
							}else{
								coldBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
								coldBilletBypass.setStatus("active");
								coldBilletBypass.setDeletedflag("no");
								coldBilletBypass.setAction("save");
								coldBilletBypass.setCaller(strCaller);
								coldBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								coldBilletBypass.setEnterprisemasterid(userRegister.getEnterprisemasterid());
								coldBilletBypass.setPlantmasterid(userRegister.getPlantmasterid());
								ColdBilletBypass coldBilletBypassForSave = coldBilletBypassService.save(coldBilletBypass);
								
								if(coldBilletBypassForSave.getColdbilletbypassid()>0){

									for(int ndx = 0; ndx<jsonRmoneBilletBypassNode.size(); ndx++){
										JsonNode firnode = jsonRmoneBilletBypassNode.get(ndx);
										RmoneBilletBypass rmoneBilletBypass =  new RmoneBilletBypass();
										rmoneBilletBypass = objectMapper.treeToValue(firnode, RmoneBilletBypass.class);
										rmoneBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
										rmoneBilletBypass.setStatus("active");
										rmoneBilletBypass.setDeletedflag("no");
										rmoneBilletBypass.setAction("save");
										rmoneBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
										rmoneBilletBypass.setColdbilletbypassid(coldBilletBypassForSave.getColdbilletbypassid());;								
										RmoneBilletBypass rmoneBilletBypassForSave = rmoneBilletBypassService.save(rmoneBilletBypass);
									}
									for(int ndx = 0; ndx<jsonRmtwoBilletBypassNode.size(); ndx++){
										JsonNode firnode = jsonRmtwoBilletBypassNode.get(ndx);
										RmtwoBilletBypass rmtwoBilletBypass =  new RmtwoBilletBypass();
										rmtwoBilletBypass = objectMapper.treeToValue(firnode, RmtwoBilletBypass.class);
										rmtwoBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
										rmtwoBilletBypass.setStatus("active");
										rmtwoBilletBypass.setDeletedflag("no");
										rmtwoBilletBypass.setAction("save");
										rmtwoBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
										rmtwoBilletBypass.setColdbilletbypassid(coldBilletBypassForSave.getColdbilletbypassid());;								
										RmtwoBilletBypass rmtwoBilletBypassForSave = rmtwoBilletBypassService.save(rmtwoBilletBypass);
									}

									for(int ndx = 0; ndx<jsonCcmBilletBypassNode.size(); ndx++){
										JsonNode firnode = jsonCcmBilletBypassNode.get(ndx);
										CcmBilletBypass ccmBilletBypass =  new CcmBilletBypass();
										ccmBilletBypass = objectMapper.treeToValue(firnode, CcmBilletBypass.class);
										ccmBilletBypass.setCreatedat(UtilityRestController.getCurrentDateTime());
										ccmBilletBypass.setStatus("active");
										ccmBilletBypass.setDeletedflag("no");
										ccmBilletBypass.setAction("save");
										ccmBilletBypass.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
										ccmBilletBypass.setColdbilletbypassid(coldBilletBypassForSave.getColdbilletbypassid());;								
										CcmBilletBypass ccmBilletBypassForSave = ccmBilletBypassService.save(ccmBilletBypass);
									}
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("ColdBilletBypass Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! ColdBilletBypass not saved ... Try Again");
								}
								
							}
						}else if(coldBilletBypass.getAction().equalsIgnoreCase("update")){
							coldBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							coldBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							coldBilletBypass.setAction("update");
							ColdBilletBypass coldBilletBypassForUpdate = coldBilletBypassService.update(coldBilletBypass);
							if(coldBilletBypassForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ColdBilletBypass Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ColdBilletBypass not Updated ... Try Again");
							}
						}else if(coldBilletBypass.getAction().equalsIgnoreCase("delete")){
							coldBilletBypass.setUpdatedat(UtilityRestController.getCurrentDateTime());
							coldBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							coldBilletBypass.setAction("delete");
							coldBilletBypass.setDeletedflag("yes");
							coldBilletBypass.setStatus("inactive");
							ColdBilletBypass coldBilletBypassForUpdate = coldBilletBypassService.update(coldBilletBypass);
							if(coldBilletBypassForUpdate != null){
								// RmtwoBilletBypass rmtwoBilletBypass =  new RmtwoBilletBypass();
								// rmtwoBilletBypass.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
								// rmtwoBilletBypass.setColdbilletbypassid(coldBilletBypassForUpdate.getColdbilletbypassid());
								// rmtwoBilletBypassService.deleteRmtwoBilletBypass(rmtwoBilletBypass);
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("ColdBilletBypass Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! ColdBilletBypass not Deleted ... Try Again");
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
