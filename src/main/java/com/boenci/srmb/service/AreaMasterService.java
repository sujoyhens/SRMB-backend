package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.AreaMaster;

public interface AreaMasterService {
	
	public AreaMaster findById(long id);
	
	public AreaMaster validAreaMaster(AreaMaster areaMaster);
	
	public AreaMaster save(AreaMaster areaMaster);

	public AreaMaster update(AreaMaster areaMaster);

	public AreaMaster delete(AreaMaster areaMaster);
	
	public String updateAreaMaster(AreaMaster areaMaster);
	
	public void deleteAreaMaster(AreaMaster areaMaster);
	
	public List<AreaMaster> findAllAreaMaster();
	
	public boolean isAreaMasterExist(AreaMaster areaMaster);
	
	public List<AreaMaster> findAreaMasterBySearchType(AreaMaster areaMaster, String searchType);
	
	public AreaMaster findDetailsByMobileNo(AreaMaster areaMaster) throws Exception;
	
	public AreaMaster findDetailsByEmail(AreaMaster areaMaster) throws Exception;
	
   
}
