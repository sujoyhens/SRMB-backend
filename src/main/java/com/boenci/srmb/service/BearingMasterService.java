package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.BearingMaster;

public interface BearingMasterService {
	
	public BearingMaster findById(long id);
	
	public BearingMaster validBearingMaster(BearingMaster bearingMaster);
	
	public BearingMaster save(BearingMaster bearingMaster);

	public BearingMaster update(BearingMaster bearingMaster);

	public BearingMaster delete(BearingMaster bearingMaster);
	
	public String updateBearingMaster(BearingMaster bearingMaster);
	
	public void deleteBearingMaster(BearingMaster bearingMaster);
	
	public List<BearingMaster> findAllBearingMaster();
	
	public boolean isBearingMasterExist(BearingMaster bearingMaster);
	
	public List<BearingMaster> findBearingMasterBySearchType(BearingMaster bearingMaster, String searchType);
	
	public BearingMaster findDetailsByMobileNo(BearingMaster bearingMaster) throws Exception;
	
	public BearingMaster findDetailsByEmail(BearingMaster bearingMaster) throws Exception;
	
   
}
