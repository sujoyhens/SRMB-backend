package com.boenci.srmb.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.boenci.srmb.model.UserRegister;

public interface UserRegisterService {
	
	public UserRegister findById(long id);
	
	public UserRegister validUserRegister(UserRegister userRegister);
	
	public UserRegister save(UserRegister userRegister);

	public UserRegister update(UserRegister userRegister);

	public UserRegister delete(UserRegister userRegister);
	
	public String updateUserRegister(UserRegister userRegister);
	
	public void deleteUserRegister(UserRegister userRegister);
	
	public List<UserRegister> findAllUserRegister();
	
	public boolean isUserRegisterExist(UserRegister userRegister);
	
	public List<UserRegister> findUserRegisterBySearchType(UserRegister userRegister, String searchType);
	
	public UserRegister findDetailsByMobileNo(UserRegister userRegister) throws Exception;
	
	public UserRegister findDetailsByEmail(UserRegister userRegister) throws Exception;

	public UserRegister validateUserFromAPI(JsonNode jsonNode) ;
	
   
}
