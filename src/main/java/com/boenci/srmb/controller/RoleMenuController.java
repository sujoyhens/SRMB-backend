package com.boenci.srmb.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

import com.boenci.srmb.model.RoleMenu;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.RoleMenuService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/rolemenu")
@CrossOrigin(origins = "*")
public class RoleMenuController {
	
	@Autowired
	private RoleMenuService roleMenuService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	private JSONArray rolemenuJsonArray ;
	//private boolean childReturn = false;

	private JSONArray categoryJsonArray ;
	private boolean childReturn = false;


	@PostMapping("/fetchrolemenu")
	public String fetchAllRoleMenu(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		categoryJsonArray  = new JSONArray();
		try {
				JsonNode roleMenuRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = roleMenuRequestNode.path("caller");
				JsonNode jsonSearchType= roleMenuRequestNode.path("searchtype");
				JsonNode jsonSearchContent= roleMenuRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				RoleMenu newRoleMenu  = new RoleMenu();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(roleMenuRequestNode);
					if(userRegister != null){
						if(strSearchType.equalsIgnoreCase("status")){
							newRoleMenu.setStatus(strSearchContent);
						}else if(strSearchType.equalsIgnoreCase("rolemasterid")){
							newRoleMenu.setRolemasterid(Long.valueOf(strSearchContent));
						}
						List<RoleMenu> listCategoriesDescription = roleMenuService.findRoleMenuBySearchType(newRoleMenu,strSearchType);
						if((listCategoriesDescription != null) && (listCategoriesDescription.size()>0)) {
							for(int ndx =0 ; ndx<listCategoriesDescription.size(); ndx ++){
								String strCategoryName = listCategoriesDescription.get(ndx).getTitle();
								long categoryDescriptionId = listCategoriesDescription.get(ndx).getRolemenuid();
								long parentId = listCategoriesDescription.get(ndx).getParentid();
								long sortorder = listCategoriesDescription.get(ndx).getMenumasterid();
								String strIcon = listCategoriesDescription.get(ndx).getIcon();
								String strLink = listCategoriesDescription.get(ndx).getLink();
								String strColor = listCategoriesDescription.get(ndx).getColor();
								String strCompleted = listCategoriesDescription.get(ndx).getCompleted();
								String strFsize = listCategoriesDescription.get(ndx).getFsize();
								String strMenuid = listCategoriesDescription.get(ndx).getMenuid();
								long arrangementId = listCategoriesDescription.get(ndx).getArrangementid();
								createJsonBodyForCategory(strCategoryName,categoryDescriptionId,parentId,sortorder,strIcon,strLink,strColor,strCompleted,strFsize,strMenuid,arrangementId);
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
		responseBody = "{\"jsonbody\":" + categoryJsonArray +","+ responseBody;
		return new String(responseBody);

	}

	public void createJsonBodyForCategory(String categoryName, long categoryId, long parentId, long sortorder,String strIcon,String strLink,String strColor,String strCompleted,String strFsize,String strMenuid,long arrangementId){
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
		catagoriJsonObject.put("rolemenuid", categoryId);
		catagoriJsonObject.put("parentid", parentId);
		catagoriJsonObject.put("menumasterid", sortorder);
		catagoriJsonObject.put("arrangementid", arrangementId);
		long length = categoryJsonArray.length();
		if(categoryJsonArray.length() > 0){
			for(int ndx =0 ; ndx<length; ndx ++){
				if(!childReturn){
					JSONObject findJsonObject = (JSONObject) categoryJsonArray.getJSONObject(ndx);
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
			categoryJsonArray.put(catagoriJsonObject);
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
					categoryJsonArray.put(catagoriJsonObject);
					childReturn = true;
				}
				return;
			}
		}
		
	}

	public void createJsonBodyForRoleMenu(long rolemenuId,  String strTitle,  long parentId, long menumasterId){
		//System.out.println(" rolemenuId " + rolemenuId+ " strTitle " + strTitle+ " parentId " + parentId+ " menumasterId " + menumasterId );
		JSONObject rolemenuJsonObject = new JSONObject();
		childReturn = false;
		rolemenuJsonObject.put("rolemenuid", rolemenuId);
		rolemenuJsonObject.put("title", strTitle);		
		rolemenuJsonObject.put("parentid", parentId);
		rolemenuJsonObject.put("menumasterid", menumasterId);
		long length = rolemenuJsonArray.length();
		if(rolemenuJsonArray.length() > 0){
			for(int ndx =0 ; ndx<length; ndx ++){
				if(!childReturn){
					JSONObject findJsonObject = (JSONObject) rolemenuJsonArray.getJSONObject(ndx);

				}
			}
		}else{
			rolemenuJsonArray.put(rolemenuJsonObject);
		}
	}	
	
	@PostMapping("/cudrolemenu")
	public String roleMenu(@RequestBody String jsonBody) {	
			 System.out.println("Getting rolemenu  details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("rolemenu Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 try {
					System.out.println("Getting rolemenu details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonRoleMenuNode = jsonNode.path("rolemenu");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						
						for(int ndx = 0; ndx<jsonRoleMenuNode.size(); ndx++){
							JsonNode roleMenuNode = jsonRoleMenuNode.get(ndx);
							RoleMenu roleMenu =  new RoleMenu();
							RoleMenu newRoleMenu =  new RoleMenu();
							roleMenu = objectMapper.treeToValue(roleMenuNode, RoleMenu.class);
							if (ndx == 0){
								newRoleMenu.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
								newRoleMenu.setRolemasterid(roleMenu.getRolemasterid());
								String roleMenuForDelete = roleMenuService.deleteRoleMenu(newRoleMenu);
								if(roleMenuForDelete == "Failure"){
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! no Role is assigned ... Try Again");
									break;
								}
							}
							roleMenu.setCreatedat(UtilityRestController.getCurrentDateTime());
							roleMenu.setStatus("active");
							roleMenu.setDeletedflag("no");
							roleMenu.setAction("save");
							roleMenu.setCaller(strCaller);
							roleMenu.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
							RoleMenu roleMenuForSave = new RoleMenu();
							roleMenuForSave = roleMenuService.save(roleMenu);

							if(ndx == jsonRoleMenuNode.size() - 1){
								if(roleMenuForSave.getRolemenuid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("RoleMenu Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! RoleMenu not saved ... Try Again");
								}
							}

						}

					}else {
						srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
						srmbResponse.setMessage("Error! not Registered ... Try Again");
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
			 
				String finalJsonResponse = UtilityRestController.toJson(srmbResponse);
				return new String(finalJsonResponse);
	}	

}
