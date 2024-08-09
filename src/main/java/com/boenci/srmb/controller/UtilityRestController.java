package com.boenci.srmb.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.boenci.srmb.utility.DecryptionData;
import com.boenci.srmb.utility.EncryptionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UtilityRestController {
	
	
	public static String getSearchCriteria(HttpServletRequest request, int listDisplayLength, String[] columnNames, String[] columnTypes ){
		String sqlCriteria = "";
		
		if ((request.getParameter("sSearch") != null) && (request.getParameter("sSearch") != "")) {
			String strSearch = "";
			for (int ndx=0; ndx<columnNames.length; ndx++){
				if(columnTypes[ndx].equals("character")){
					if(!strSearch.isEmpty()){
						strSearch = strSearch + " or ";
					}
					strSearch = strSearch + columnNames[ndx] + " like '%" + request.getParameter("sSearch") + "%'";

				}
			}
			sqlCriteria = " WHERE " + strSearch;
		}else{
			for (int ndx=0; ndx<columnNames.length; ndx++){
				if ((request.getParameter("sSearch_" + ndx) != null) && (request.getParameter("sSearch_" + ndx) != "")){
					sqlCriteria = " WHERE " + columnNames[ndx] + " like " + request.getParameter("sSearch_" + ndx) + "";;
					break;
					
				}
			}
		}	   
		return sqlCriteria;
	}
	
	public static String getSortSql(HttpServletRequest request, String[] columnNames ){
		String sqlSort = "";
		
		int column = 0;
		String dir = "asc";
		String colIndex = request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0");
		   
			
		if (colIndex != null) {
		   column = Integer.parseInt(colIndex);
		   if (column < 0 || column > columnNames.length){
			   column = 0;
		   }
		}
		if (sortDirection != null) {
		   if (!sortDirection.equals("asc")){
			   dir = "desc";
		   }
		}
		 
		String colName = columnNames[column];

		sqlSort = " order by " + colName + " " + dir;
		return sqlSort;
	}
	
	public static String toJson(Object dt) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(dt);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doEncrypt(String strMessage){
 		EncryptionData encryptionData = new EncryptionData();
 		String encryptedMessage = encryptionData.getEncryptedData(strMessage);
 		encryptionData = null;
 		return encryptedMessage; 		
	}

	public static String doDecrypt(String encryptedMessage){
		DecryptionData decryptionData = new DecryptionData();
 		String decryptedMessage = decryptionData.getDecryptedData(encryptedMessage);
 		decryptionData = null;
 		return decryptedMessage; 		
	}

	public static String getCurrentDateTime(){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		return (dateTimeFormatter.format(now)); 
	}

	public static String getCurrentDateTime1(){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		return (dateTimeFormatter.format(now)); 
	}

	public static String getCurrentDate(){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		return (dateTimeFormatter.format(now)); 
	}

	public static String getDate(){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		return (dateTimeFormatter.format(now)); 
	}

	public static String getTime(String dateTime){

		LocalDateTime newDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		String newString = DateTimeFormatter.ofPattern("HH:mm:ss").format(newDateTime);
		return newString;
			
	}
	
	public static String getQueryString(String queryString){
		//abc,cde,efg
		String finalString = "";
		String[] arrOfStr = queryString.split(",");  
		//System.out.println(arrOfStr.length);
		String tableName = "querytable";
		String queryCondition = " where querytable.deletedflag = ?1 and querytable.status = ?2";
		for (int i = 0; i< arrOfStr.length; i++ ){
			String fieldValue = "";
			fieldValue = arrOfStr[i];
			if(i == arrOfStr.length - 1){
				finalString = finalString +" FROM "+fieldValue+" "+tableName + queryCondition;
			}else{
				finalString = finalString+","+tableName+"."+fieldValue;
			}
		}

		return finalString;
	}

	public static String generateOTP() {	
		String chars = "1234567890";
		String OTP = "";
	    for (int x = 0; x < 6; x++) {
	    	int i = (int) Math.floor(Math.random() * chars.length());
	        OTP += chars.charAt(i);
	    }
	    return OTP;

	}
	

}
