package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.UnitMaster;

public interface UnitMasterService {
	
	public UnitMaster findById(long id);
	
	public UnitMaster validUnitMaster(UnitMaster unitMaster);
	
	public UnitMaster save(UnitMaster unitMaster);

	public UnitMaster update(UnitMaster areaMaster);

	public UnitMaster delete(UnitMaster areaMaster);
	
	public String updateUnitMaster(UnitMaster unitMaster);
	
	public void deleteUnitMaster(UnitMaster unitMaster);
	
	public List<UnitMaster> findAllUnitMaster();
	
	public boolean isUnitMasterExist(UnitMaster unitMaster);
	
	public List<UnitMaster> findUnitMasterBySearchType(UnitMaster unitMaster, String searchType);
	
	public UnitMaster findDetailsByMobileNo(UnitMaster unitMaster) throws Exception;
	
	public UnitMaster findDetailsByEmail(UnitMaster unitMaster) throws Exception;
	
   
}
