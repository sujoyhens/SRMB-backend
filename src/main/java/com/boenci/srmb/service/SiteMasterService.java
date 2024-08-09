package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.SiteMaster;

public interface SiteMasterService {
	
	public SiteMaster findById(long id);
	
	public SiteMaster validSiteMaster(SiteMaster siteMaster);
	
	public SiteMaster save(SiteMaster siteMaster);

	public SiteMaster update(SiteMaster siteMaster);

	public SiteMaster delete(SiteMaster siteMaster);
	
	public String updateSiteMaster(SiteMaster siteMaster);
	
	public void deleteSiteMaster(SiteMaster siteMaster);
	
	public List<SiteMaster> findAllSiteMaster();
	
	public boolean isSiteMasterExist(SiteMaster siteMaster);
	
	public List<SiteMaster> findSiteMasterBySearchType(SiteMaster siteMaster, String searchType);
	
	public SiteMaster findDetailsByMobileNo(SiteMaster siteMaster) throws Exception;
	
	public SiteMaster findDetailsByEmail(SiteMaster siteMaster) throws Exception;
	
   
}
