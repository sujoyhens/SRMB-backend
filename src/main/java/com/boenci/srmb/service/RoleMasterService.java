package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.RoleMaster;

public interface RoleMasterService {
	
	public RoleMaster findById(long id);
	
	public RoleMaster validRoleMaster(RoleMaster roleMaster);
	
	public RoleMaster save(RoleMaster roleMaster);

	public RoleMaster update(RoleMaster areaMaster);

	public RoleMaster delete(RoleMaster areaMaster);
	
	public String updateRoleMaster(RoleMaster roleMaster);
	
	public void deleteRoleMaster(RoleMaster roleMaster);
	
	public List<RoleMaster> findAllRoleMaster();
	
	public boolean isRoleMasterExist(RoleMaster roleMaster);
	
	public List<RoleMaster> findRoleMasterBySearchType(RoleMaster roleMaster, String searchType);
	
	public RoleMaster findDetailsByMobileNo(RoleMaster roleMaster) throws Exception;
	
	public RoleMaster findDetailsByEmail(RoleMaster roleMaster) throws Exception;
	
   
}
