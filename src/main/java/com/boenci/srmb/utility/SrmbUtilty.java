package com.boenci.srmb.utility;

import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.ui.ModelMap;

import jakarta.servlet.http.HttpSession;




public class SrmbUtilty {

	
  public static String checkSession_OLD(HttpSession httpSession, ModelMap model){
		
		if(httpSession != null){
			//UserDetails userDetails = (UserDetails)httpSession.getAttribute("userDetails");
			//if(userDetails == null){
			//	return "redirect:/userDetails";
			//}
		}else{
			//	return "redirect:/userDetails";		
			}
		return "";
	}
  
  public static String checkSession1(HttpSession httpSession, ModelMap model){
		String userID = "";
		String userDetailsid = "";
		String loginType = "";
		String loginRole = "";
		
				
		if(httpSession != null){
			String loginMode = (String)httpSession.getAttribute("loginmode");
			if(loginMode == null){
				return "";
			}
			if(loginMode.equals("user")){
				/*UserDetails userDetails = (UserDetails)httpSession.getAttribute("user");
				
					if(userDetails == null){
						return "redirect:/userDetails";
					}else{
						//loginRole = userDetails.getUserrolename();
						//userDetails.setRolename(loginRole);
						userID = userDetails.getUserid();
						if((userID == null) || (userID.isEmpty())){
							return "";
						}
						userDetailsid = String.valueOf(userDetails.getUserdetailsid());						
						model.addAttribute("loginType", "user");
						model.addAttribute("loginUserDetails", userDetails);
						return "user";*/
						
					//	return "";
					//}
			}else /*if(loginMode.equals("superuser"))*/{
			/*	UserDetails userDetails = (UserDetails)httpSession.getAttribute("superuser");
					if(userDetails == null){
						return "";
					}else{
					//	loginRole = userDetails.getUserrole().getRolename();
					//	userDetails.setRolename(loginRole);
						userID = userDetails.getUserid();
																		
						if((userID == null) || (userID.isEmpty())){
							return "";
						}
						//loginType = userDetails.getUserrole();
						model.addAttribute("loginType", "superuser");						
						model.addAttribute("loginUserDetails", userDetails);
						
						return "superuser";*/
					//}
			}
			
		}else{
			//return "redirect:/userDetails";
			return "";	
		}
		return "";
	}
  
  
  public static String getCurrentDateTime() {
	  String pattern = "dd-MM-yyyy HH:mm:ss";
	  SimpleDateFormat simpleDateFormat =
	          new SimpleDateFormat(pattern);

	  String dateTime = simpleDateFormat.format(new Date());
	  return dateTime;
  }

  public static Date convertStringToDate(String dateString) {
	// Assuming dateString has the format "yyyy-MM-dd"
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	try {
		return dateFormat.parse(dateString);
	} catch (Exception e) {
		// Handle parse exception
		e.printStackTrace();
	}
	return null;
}
  
  

}
