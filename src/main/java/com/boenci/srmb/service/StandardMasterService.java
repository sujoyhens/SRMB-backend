package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.StandardMaster;

public interface StandardMasterService {
	
	public StandardMaster findById(long id);
	
	public StandardMaster validStandardMaster(StandardMaster standardMaster);
	
	public StandardMaster save(StandardMaster standardMaster);

	public StandardMaster update(StandardMaster standardMaster);

	public StandardMaster delete(StandardMaster standardMaster);
	
	public String updateStandardMaster(StandardMaster standardMaster);
	
	public void deleteStandardMaster(StandardMaster standardMaster);
	
	public List<StandardMaster> findAllStandardMaster();
	
	public boolean isStandardMasterExist(StandardMaster standardMaster);
	
	public List<StandardMaster> findStandardMasterBySearchType(StandardMaster standardMaster, String searchType);
	
	public StandardMaster findDetailsByMobileNo(StandardMaster standardMaster) throws Exception;
	
	public StandardMaster findDetailsByEmail(StandardMaster standardMaster) throws Exception;
	
   
}
