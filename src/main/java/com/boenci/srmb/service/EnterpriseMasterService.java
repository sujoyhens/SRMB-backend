package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.EnterpriseMaster;

public interface EnterpriseMasterService {
	
	public EnterpriseMaster findById(long id);
	
	public EnterpriseMaster validEnterpriseMaster(EnterpriseMaster enterpriseMaster);
	
	public EnterpriseMaster save(EnterpriseMaster enterpriseMaster);

	public EnterpriseMaster update(EnterpriseMaster enterpriseMaster);

	public EnterpriseMaster delete(EnterpriseMaster enterpriseMaster);
	
	public String updateEnterpriseMaster(EnterpriseMaster enterpriseMaster);
	
	public void deleteEnterpriseMaster(EnterpriseMaster enterpriseMaster);
	
	public List<EnterpriseMaster> findAllEnterpriseMaster();
	
	public boolean isEnterpriseMasterExist(EnterpriseMaster enterpriseMaster);
	
	public List<EnterpriseMaster> findEnterpriseMasterBySearchType(EnterpriseMaster enterpriseMaster, String searchType);
	
	public EnterpriseMaster findDetailsByMobileNo(EnterpriseMaster enterpriseMaster) throws Exception;
	
	public EnterpriseMaster findDetailsByEmail(EnterpriseMaster enterpriseMaster) throws Exception;
	
   
}
