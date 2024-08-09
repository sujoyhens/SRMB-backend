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
import java.util.List;

import com.boenci.srmb.model.EquipmentImages;
import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.model.SrmbResponse;
import com.boenci.srmb.service.EquipmentImagesService;
import com.boenci.srmb.service.UserRegisterService;
import com.boenci.srmb.utility.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/equipmentimages")
@CrossOrigin(origins = "*")
public class EquipmentImagesController {
	
	@Autowired
	private EquipmentImagesService equipmentImagesService;

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	SrmbResponse srmbResponse;

	@PostMapping("/fetchequipmentimages")
	public String fetchAllEquipmentImages(@RequestBody String jsonBody){
		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
	    srmbResponse.setResultList(null);
	    srmbResponse.setMessage("Successfull");
		System.out.println("Json Body " + jsonBody);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
				JsonNode equipmentImagesRequestNode = objectMapper.readTree(jsonBody);
				JsonNode jsonCaller = equipmentImagesRequestNode.path("caller");
				JsonNode jsonSearchType= equipmentImagesRequestNode.path("searchtype");
				JsonNode jsonSearchContent= equipmentImagesRequestNode.path("searchcontent");
				String strSearchType = jsonSearchType.asText();
				String strSearchContent = jsonSearchContent.asText();
				EquipmentImages newEquipmentImages  = new EquipmentImages();
				UserRegister userRegister = userRegisterService.validateUserFromAPI(equipmentImagesRequestNode);
					if(userRegister != null){
					if(strSearchType.equalsIgnoreCase("status")){
						newEquipmentImages.setStatus(strSearchContent);
					}else if(strSearchType.equalsIgnoreCase("equipmentmasterid")){
						newEquipmentImages.setEquipmentmasterid(Long.valueOf(strSearchContent));
					}
					List<EquipmentImages> listEquipmentImages = equipmentImagesService.findEquipmentImagesBySearchType(newEquipmentImages,strSearchType);
					if((listEquipmentImages != null) && (listEquipmentImages.size()>0)) {
						srmbResponse.setResultList(listEquipmentImages);
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
	
	@RequestMapping(value = { "/uploadequipmentimages" }, method = RequestMethod.POST)
    public String uploadBookImage( @RequestParam("file") MultipartFile mPartfile, @RequestParam("email") String useremail,
	@RequestParam("caller") String caller,@RequestParam("usermode") String usermode
	,@RequestParam("password") String password,@RequestParam("enterprisemasterid") long enterprisemasterid
	,@RequestParam("sitemasterid") long sitemasterid,@RequestParam("plantmasterid") long plantmasterid,
	@RequestParam("unitmasterid") long unitmasterid,@RequestParam("areamasterid") long areamasterid,
	@RequestParam("equipmentmasterid") long equipmentmasterid) {

		srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
		srmbResponse.setResultList(null);
		srmbResponse.setMessage("Image Upload Successfull");
		UserRegister newUserRegister  = new UserRegister();
		newUserRegister.setUseremail(useremail);
		newUserRegister.setPassword(password);
		newUserRegister.setUsermode(usermode);
		String imageFileName = "";
		UserRegister userRegister = userRegisterService.validUserRegister(newUserRegister);
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
					File imgFile=new File(imagePath,"equipmentimages"); 
					if(!imgFile.exists()){ 
						imgFile.mkdirs(); 
					} 
					File attemptsImgTemp=new File(imgFile,imageFileName); 
					System.out.println("Path : "+attemptsImgTemp); 
					FileOutputStream imgFos= new FileOutputStream(attemptsImgTemp); 
					imgFos.write(imageBytes); 
					imgFos.close();  
				}
				EquipmentImages newEquipmentImages  = new EquipmentImages();
				newEquipmentImages.setImagepath("/equipmentimages/"+ imageFileName);
				newEquipmentImages.setEnterprisemasterid(enterprisemasterid);
				newEquipmentImages.setSitemasterid(sitemasterid);
				newEquipmentImages.setPlantmasterid(plantmasterid);
				newEquipmentImages.setUnitmasterid(unitmasterid);
				newEquipmentImages.setAreamasterid(areamasterid);
				newEquipmentImages.setEquipmentmasterid(equipmentmasterid);
				newEquipmentImages.setCreatedat(UtilityRestController.getCurrentDateTime());
				newEquipmentImages.setStatus("active");
				newEquipmentImages.setDeletedflag("no");
				newEquipmentImages.setAction("save");
				newEquipmentImages.setCreatedby(String.valueOf(userRegister.getUserregisterid()));
				EquipmentImages equipmentImagesForSave = equipmentImagesService.save(newEquipmentImages);				
				if(equipmentImagesForSave.getEquipmentmasterid()>0){
					srmbResponse.setStatus(AppConstants.SUCCESS_RESPONSE);
					srmbResponse.setMessage("EquipmentImages Added Successfully...");
				}else{
					srmbResponse.setStatus(AppConstants.FAILURE_RESPONSE);
					srmbResponse.setMessage("Error! EquipmentImages not saved ... Try Again");
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
