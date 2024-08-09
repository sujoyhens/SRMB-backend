package com.boenci.srmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;



@RestController
@RequestMapping("/userregister")
@CrossOrigin(origins = "*")
public class UserRegisterController {
	
	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchuserregister")
	public String fetchAllUserRegister(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode userRegisterRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = userRegisterRequestNode.path("caller");
				JsonNode jsonSearchType= userRegisterRequestNode.path("searchtype");
				JsonNode jsonSearchContent= userRegisterRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				UserRegister newUserRegister  = new UserRegister();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(userRegisterRequestNode);
				if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newUserRegister.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("usermode")){
						newUserRegister.setUsermode(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("userregisterid")){
						newUserRegister.setUserregisterid(Long.valueOf(strSearchContent));
					}else if(strSearchType.equalsIgnoreCase("unitmasterid")){
						newUserRegister.setUnitmasterid(Long.valueOf(strSearchContent));
					}
					List<UserRegister> listUserRegister = userRegisterService.findUserRegisterBySearchType(newUserRegister,strSearchType);
					if((listUserRegister != null) && (listUserRegister.size()>0)) {
						
						srmbResponse.setResultList(listUserRegister);
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

	@PostMapping("/cuduserregister")
	public String userRegister(@RequestBody String jsonBody) {	
			 System.out.println("Getting UserRegister details (Encrypted) " + jsonBody);
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			 srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
			 srmbResponse.setResultList(null);
			 srmbResponse.setMessage("UserRegister Successfully Saved");
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 long userRegisteredid = 0;
			 try {
					System.out.println("Getting UserRegister details " + jsonBody);
					JsonNode jsonNode = objectMapper.readTree(jsonBody);
					JsonNode jsonUserRegisterNode = jsonNode.path("userregister");
					JsonNode jsonUpdateType = jsonNode.path("updatetype");
					JsonNode jsonUpdateContent = jsonNode.path("updatecontent");
					JsonNode jsonCaller = jsonNode.path("caller");
					String strCaller = jsonCaller.asText();
					UserRegister newUserRegisterMap =  new UserRegister();
					newUserRegisterMap = objectMapper.treeToValue(jsonUserRegisterNode, UserRegister.class);
					UserRegister userRegister = userRegisterService.validateUserFromAPI(jsonNode);
					if(userRegister != null){
						if(newUserRegisterMap.getAction().equalsIgnoreCase("save")){
							UserRegister newUserRegister = userRegisterService.validUserRegister(newUserRegisterMap);
							if(newUserRegister != null){
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("UserRegister Name Already Exist...");
							}else{
								newUserRegisterMap.setCreatedat(UtilityRestController.getCurrentDateTime());
								newUserRegisterMap.setStatus("active");
								newUserRegisterMap.setDeletedflag("no");
								newUserRegisterMap.setAction("save");
								newUserRegisterMap.setCaller(strCaller);
								newUserRegisterMap.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
								
								UserRegister userRegisterForSave = userRegisterService.save(newUserRegisterMap);
								
								if(userRegisterForSave.getUserregisterid()>0){
									srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
									srmbResponse.setMessage("UserRegister Added Successfully...");
								}else{
									srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
									srmbResponse.setMessage("Error! UserRegister not saved ... Try Again");
								}
								
							}
						}else if(newUserRegisterMap.getAction().equalsIgnoreCase("update")){
							newUserRegisterMap.setUpdatedat(UtilityRestController.getCurrentDateTime());
							newUserRegisterMap.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							newUserRegisterMap.setAction("update");
							UserRegister userRegisterForUpdate = userRegisterService.update(newUserRegisterMap);
							if(userRegisterForUpdate != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("UserRegister Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! UserRegister not Updated ... Try Again");
							}
						}else if(newUserRegisterMap.getAction().equalsIgnoreCase("updatetheme")){
							newUserRegisterMap.setUpdatedat(UtilityRestController.getCurrentDateTime());
							newUserRegisterMap.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							newUserRegisterMap.setAction("update");
							newUserRegisterMap.setUseremail(userRegister.getUseremail());
							newUserRegisterMap.setPassword(userRegister.getPassword());
							newUserRegisterMap.setStatus(userRegister.getStatus());
							newUserRegisterMap.setDeletedflag(userRegister.getDeletedflag());
							newUserRegisterMap.setUsermode(userRegister.getUsermode());

							String updateResponse = userRegisterService.updateUserRegister(newUserRegisterMap);
							if(updateResponse == "Success"){
								srmbResponse.setResultList(null);
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("theme Updated Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! theme not Updated ... Try Again");
							}
						}else if(newUserRegisterMap.getAction().equalsIgnoreCase("delete")){
							newUserRegisterMap.setUpdatedat(UtilityRestController.getCurrentDateTime());
							newUserRegisterMap.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
							newUserRegisterMap.setAction("delete");
							newUserRegisterMap.setDeletedflag("yes");
							newUserRegisterMap.setStatus("inactive");
							UserRegister userRegisterForDelete = userRegisterService.delete(newUserRegisterMap);
							if(userRegisterForDelete != null){
								srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
								srmbResponse.setMessage("UserRegister Deleted Successfully...");
							}else{
								srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
								srmbResponse.setMessage("Error! UserRegister not Deleted ... Try Again");
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

	@PostMapping("/userlogin")
	public String adminLogin(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("userlogin Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode serviceBookingRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonEmail = serviceBookingRequestNode.path("email");
				JsonNode jsonPassword= serviceBookingRequestNode.path("password");
				String strUserEmail = jsonEmail.asText();
				String strPassword = jsonPassword.asText();
				UserRegister userregister = new UserRegister();
				userregister.setUseremail(strUserEmail);
				userregister.setPassword(strPassword);
				UserRegister newUserregister = new UserRegister();
				newUserregister = userRegisterService.findDetailsByEmail(userregister);
				if(newUserregister == null){
					srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
					srmbResponse.setMessage("Error! MailId or Password Not Matched ... Try Again");
				}else{
					String strSearchType = "userregisterid";
					List<UserRegister> listUserRegister = userRegisterService.findUserRegisterBySearchType(newUserregister,strSearchType);
					srmbResponse.setResultList(listUserRegister);
					srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
					srmbResponse.setMessage("Login successfully...");
				}
				
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
			srmbResponse.setMessage("Error! not Registered ... Try Again");
		} catch (IOException e) {
			e.printStackTrace();
			srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
			srmbResponse.setMessage("Error! not Registered ... Try Again");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String responseBody = UtilityRestController.toJson(srmbResponse);
		return new String(responseBody);
	}

	@RequestMapping(value = { "/uploadprofileimages" }, method = RequestMethod.POST)
    public String uploadBookImage( @RequestParam("file") MultipartFile mPartfile, @RequestParam("email") String useremail,
	@RequestParam("phoneumber") String phoneumber ,@RequestParam("password") String password,
	@RequestParam("username") String username,@RequestParam("userregisterid") long userregisterid)
	{
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
		srmbResponse.setResultList(null);
		srmbResponse.setMessage("Image Upload Successfull");
		UserRegister newUserRegister  = new UserRegister();
		newUserRegister.setUseremail(useremail);
		newUserRegister.setPassword(password);
		String imageFileName = "";
		UserRegister userRegister = userRegisterService.findById(userregisterid);
		if(userRegister != null){
			try{
				MultipartFile file = (MultipartFile) mPartfile;
				if (!file.isEmpty()) {
					byte[] imageBytes = file.getBytes(); 
					double imgSize = (double) (file.getSize() * 0.0009765625); //Convert to KB
					System.out.println(imgSize); 
					String originamFileName = file.getOriginalFilename(); 
					String imageFileExt= originamFileName.substring(originamFileName.lastIndexOf("."),originamFileName.length()); 
					imageFileName=""+System.currentTimeMillis()+imageFileExt;
					String imagePath="/var/www/html/assets/uploads/"; //demo server snapshot path
					//String imagePath="/images"; 
					System.out.println(imagePath); 
					File imgFile=new File(imagePath,"userprofileimages"); 
					if(!imgFile.exists()){ 
						imgFile.mkdirs(); 
					} 
					File attemptsImgTemp=new File(imgFile,imageFileName); 
					System.out.println("Path : "+attemptsImgTemp); 
					FileOutputStream imgFos= new FileOutputStream(attemptsImgTemp); 
					imgFos.write(imageBytes); 
					imgFos.close();  
					userRegister.setImagepath("/userprofileimages/"+ imageFileName);
				}				
				userRegister.setUpdatedat(UtilityRestController.getCurrentDateTime());
				userRegister.setStatus("active");
				userRegister.setDeletedflag("no");
				userRegister.setAction("update");
				userRegister.setUpdatedby(String.valueOf(userRegister.getUserregisterid()));
				userRegister.setUseremail(useremail);
				userRegister.setPassword(password);
				userRegister.setUsername(username);
				userRegister.setPhoneumber(phoneumber);
				UserRegister userRegisterForSave = userRegisterService.update(userRegister);				
				if(userRegisterForSave.getUserregisterid()>0){
					List<UserRegister> userRegisterList = new ArrayList<UserRegister>();
					userRegisterList.add(userRegisterForSave);
					srmbResponse.setResultList(userRegisterList);
					srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
					srmbResponse.setMessage("Profile Updated Successfully...");
				}else{
					srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
					srmbResponse.setMessage("Error! ProfileImages not Updated ... Try Again");
				}

			}catch (IOException e) {
				srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
				srmbResponse.setStatuscode("102");//failed to File Create
				srmbResponse.setMessage("Image Upload Failed, Please Try Again..." + e.toString());
				e.printStackTrace();
			}
		}else {
			srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
			srmbResponse.setMessage("Error! not Registered ... Try Again");
		}
		String finalJsonResponse = UtilityRestController.toJson(srmbResponse);
		return new String(finalJsonResponse);
		
	}

}
