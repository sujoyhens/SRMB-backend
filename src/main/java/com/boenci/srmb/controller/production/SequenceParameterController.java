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
import com.boenci.srmb.model.production.SequenceParameter;
import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.service.production.SequenceParameterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/sequenceparameter")
@CrossOrigin(origins = "*")
public class SequenceParameterController {
	
	@Autowired
	private SequenceParameterService sequenceParameterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchsequenceparameter")
	public String fetchAllSequenceParameter(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode sequenceParameterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = sequenceParameterRequestNode.path("caller");
				JsonNode jsonSearchType= sequenceParameterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= sequenceParameterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				SequenceParameter newSequenceParameter  = new SequenceParameter();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(sequenceParameterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newSequenceParameter.setStatus(strSearchContent);
						}
						List<SequenceParameter> listSequenceParameter = sequenceParameterService.findSequenceParameterBySearchType(newSequenceParameter,strSearchType);
						if((listSequenceParameter != null) && (listSequenceParameter.size()>0)) {
							srmbResponse.setResultList(listSequenceParameter);
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
	
	@PostMapping("/cudsequenceparameter")
	public String sequenceParameter(@RequestBody String jsonBody) {	
			 System.out.println("Getting unit master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Unit Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long sequenceParameteredid = 0;
			 try {
					System.out.println("Getting Unit Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonSequenceParameterNode = jsonNode.path("sequenceparameter");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					SequenceParameter sequenceParameter =  new SequenceParameter();
					sequenceParameter = objectMapper.treeToValue(jsonSequenceParameterNode, SequenceParameter.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(sequenceParameter.getAction().equalsIgnoreCase("save")){
							SequenceParameter newSequenceParameter = sequenceParameterService.validSequenceParameter(sequenceParameter);
							if(newSequenceParameter != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("SequenceParameter Name Already Exist...");
							}else{
								sequenceParameter.setCreatedat(UtilityRestController.getCurrentDateTime());
								sequenceParameter.setStatus("active");
								sequenceParameter.setDeletedflag("no");
								sequenceParameter.setAction("save");
								sequenceParameter.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								SequenceParameter sequenceParameterForSave = sequenceParameterService.save(sequenceParameter);
								
								if(sequenceParameterForSave.getSequenceparameterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("SequenceParameter Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! SequenceParameter not saved ... Try Again");
								}
								
							}
						}else if(sequenceParameter.getAction().equalsIgnoreCase("update")){
							sequenceParameter.setUpdatedat(UtilityRestController.getCurrentDateTime());
							sequenceParameter.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							sequenceParameter.setAction("update");
							SequenceParameter sequenceParameterForUpdate = sequenceParameterService.update(sequenceParameter);
							if(sequenceParameterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SequenceParameter Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SequenceParameter not Updated ... Try Again");
							}
						}else if(sequenceParameter.getAction().equalsIgnoreCase("delete")){
							sequenceParameter.setUpdatedat(UtilityRestController.getCurrentDateTime());
							sequenceParameter.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							sequenceParameter.setAction("delete");
							sequenceParameter.setDeletedflag("yes");
							sequenceParameter.setStatus("inactive");
							SequenceParameter sequenceParameterForUpdate = sequenceParameterService.update(sequenceParameter);
							if(sequenceParameterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("SequenceParameter Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! SequenceParameter not Deleted ... Try Again");
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
