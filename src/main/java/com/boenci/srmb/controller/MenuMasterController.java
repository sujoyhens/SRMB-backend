package com.boenci.srmb.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.boenci.srmb.model.MenuMaster;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.MenuMasterService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/menumaster")
@CrossOrigin(origins = "*")
public class MenuMasterController {
	
	@Autowired
	private MenuMasterService menuMasterService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	private JSONArray menuidJsonArray ;
	private boolean childReturn = false;

	@PostMapping("/fetchmenumaster")
	public String fetchAllMenuMaster(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		menuidJsonArray  = new JSONArray();
		try {
				JsonNode menuMasterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = menuMasterRequestNode.path("caller");
				JsonNode jsonSearchType= menuMasterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= menuMasterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				MenuMaster newMenuMaster  = new MenuMaster();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(menuMasterRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newMenuMaster.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("menumasterid")){
							newMenuMaster.setMenumasterid(Long.valueOf(strSearchContent));
						}
						List<MenuMaster> listMenuMaster = menuMasterService.findMenuMasterBySearchType(newMenuMaster,strSearchType);
						if((listMenuMaster != null) && (listMenuMaster.size()>0)) {
							for(int ndx =0 ; ndx<listMenuMaster.size(); ndx ++){
								String strCategoryName = listMenuMaster.get(ndx).getTitle();
								long parentId = listMenuMaster.get(ndx).getParentid();
								long sortorder = listMenuMaster.get(ndx).getMenumasterid();
								String strIcon = listMenuMaster.get(ndx).getIcon();
								String strLink = listMenuMaster.get(ndx).getLink();
								String strColor = listMenuMaster.get(ndx).getColor();
								String strCompleted = listMenuMaster.get(ndx).getCompleted();
								String strFsize = listMenuMaster.get(ndx).getFsize();
								String strMenuid = listMenuMaster.get(ndx).getMenuid();
								long arrangementId = listMenuMaster.get(ndx).getArrangementid();
								createJsonBodyForCategory(strCategoryName,parentId,sortorder,strIcon,strLink,strColor,strCompleted,strFsize,strMenuid,arrangementId);
							}
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
		responseBody = responseBody.replace("\\", "");
		//ecommerceResponse.setJsonBody("");
		responseBody = responseBody.substring(1);
		responseBody = "{\"jsonbody\":" + menuidJsonArray +","+ responseBody;
		return new String(responseBody);
	}

	public void createJsonBodyForCategory(String categoryName, long parentId, long sortorder,String strIcon,String strLink,String strColor,String strCompleted,String strFsize,String strMenuid,long arrangementId){
		JSONObject catagoriJsonObject = new JSONObject();
		JSONArray newJsonArray = new JSONArray();
		childReturn = false;

		catagoriJsonObject.put("title", categoryName);
		catagoriJsonObject.put("menuid", strMenuid);
		catagoriJsonObject.put("icon", strIcon);
		catagoriJsonObject.put("link", strLink);
		catagoriJsonObject.put("completed", strCompleted);
		catagoriJsonObject.put("fsize", strFsize);
		catagoriJsonObject.put("color", strColor);		
		catagoriJsonObject.put("parentid", parentId);
		catagoriJsonObject.put("menumasterid", sortorder);
		catagoriJsonObject.put("arrangementid", arrangementId);
		long length = menuidJsonArray.length();
		if(menuidJsonArray.length() > 0){
			for(int ndx =0 ; ndx<length; ndx ++){
				if(!childReturn){
					JSONObject findJsonObject = (JSONObject) menuidJsonArray.getJSONObject(ndx);
					long currentCatgoryId = findJsonObject.getLong("arrangementid");
					String currentCatgoryName = findJsonObject.getString("menuid");
					if(currentCatgoryId == parentId){
						if(findJsonObject.has(currentCatgoryName)){
							JSONArray findJsonArray = findJsonObject.getJSONArray(currentCatgoryName);
							findJsonArray.put(catagoriJsonObject);
						}else{
							JSONArray newJsonArray1 = new JSONArray();
							newJsonArray1.put(catagoriJsonObject);
							findJsonObject.put(currentCatgoryName, newJsonArray1);
						}
						
						return;
					}else{
						createJsonBodyForCategoryChild(catagoriJsonObject, currentCatgoryName, findJsonObject);
					}
				}
			}
		}else{
			menuidJsonArray.put(catagoriJsonObject);
		}
	}

	public void createJsonBodyForCategoryChild(JSONObject catagoriJsonObject, String currentCatgoryName, JSONObject findJsonObject){
		JSONArray newJsonArray = new JSONArray();
		long parentId = catagoriJsonObject.getLong("parentid");
		if(!childReturn){
			if(findJsonObject.has(currentCatgoryName)){
				JSONArray findJsonArray = findJsonObject.getJSONArray(currentCatgoryName);
				for(int ndx =0 ; ndx<findJsonArray.length(); ndx ++){
					JSONObject findJsonObjectNew = (JSONObject) findJsonArray.getJSONObject(ndx);
					long currentCatgoryIdNew = findJsonObjectNew.getLong("arrangementid");
					String currentCatgoryNameNew = findJsonObjectNew.getString("menuid");
					if(currentCatgoryIdNew == parentId){

						if(findJsonObjectNew.has(currentCatgoryNameNew)){
							JSONArray findJsonArrayNew = findJsonObjectNew.getJSONArray(currentCatgoryNameNew);
							findJsonArrayNew.put(catagoriJsonObject);
						}else{
							JSONArray newJsonArray1 = new JSONArray();
							newJsonArray1.put(catagoriJsonObject);
							findJsonObjectNew.put(currentCatgoryNameNew, newJsonArray1);
						}
						childReturn = true;
						return;
					}else{
						createJsonBodyForCategoryChild(catagoriJsonObject, currentCatgoryNameNew, findJsonObjectNew);
					}
				}
			}else{
				if(parentId == 0){
					menuidJsonArray.put(catagoriJsonObject);
					childReturn = true;
				}
				return;
			}
		}
		
	}
	
	@PostMapping("/cudmenumaster")
	public String menuMaster(@RequestBody String jsonBody) {	
			 System.out.println("Getting Menu master details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("Menu Master Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long menuMasteredid = 0;
			 try {
					System.out.println("Getting Menu Master details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonMenuMasterNode = jsonNode.path("menumaster");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					MenuMaster menuMaster =  new MenuMaster();
					menuMaster = objectMapper.treeToValue(jsonMenuMasterNode, MenuMaster.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(menuMaster.getAction().equalsIgnoreCase("save")){
							MenuMaster newMenuMaster = menuMasterService.validMenuMaster(menuMaster);
							if(newMenuMaster != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("MenuMaster Name Already Exist...");
							}else{
								menuMaster.setCreatedat(UtilityRestController.getCurrentDateTime());
								menuMaster.setStatus("active");
								menuMaster.setDeletedflag("no");
								menuMaster.setAction("save");
								menuMaster.setCaller(strCaller);
								menuMaster.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								MenuMaster menuMasterForSave = menuMasterService.save(menuMaster);
								
								if(menuMasterForSave.getMenumasterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("MenuMaster Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! MenuMaster not saved ... Try Again");
								}
								
							}
						}else if(menuMaster.getAction().equalsIgnoreCase("update")){
							menuMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							menuMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							menuMaster.setAction("update");
							MenuMaster menuMasterForUpdate = menuMasterService.update(menuMaster);
							if(menuMasterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("MenuMaster Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! MenuMaster not Updated ... Try Again");
							}
						}else if(menuMaster.getAction().equalsIgnoreCase("delete")){
							menuMaster.setUpdatedat(UtilityRestController.getCurrentDateTime());
							menuMaster.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							menuMaster.setAction("delete");
							menuMaster.setDeletedflag("yes");
							menuMaster.setStatus("inactive");
							MenuMaster menuMasterForDelete = menuMasterService.delete(menuMaster);
							if(menuMasterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("MenuMaster Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! MenuMaster not Deleted ... Try Again");
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
				srmbResponse.setResultList(null);
				return new String(finalJsonResponse);
	}	

}
