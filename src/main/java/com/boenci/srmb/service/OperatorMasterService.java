package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.OperatorMaster;

public interface OperatorMasterService {
	
	public OperatorMaster findById(long id);
	
	public OperatorMaster validOperatorMaster(OperatorMaster operatorMaster);
	
	public OperatorMaster save(OperatorMaster operatorMaster);

	public OperatorMaster update(OperatorMaster operatorMaster);

	public OperatorMaster delete(OperatorMaster operatorMaster);
	
	public String updateOperatorMaster(OperatorMaster operatorMaster);
	
	public void deleteOperatorMaster(OperatorMaster operatorMaster);
	
	public List<OperatorMaster> findAllOperatorMaster();
	
	public boolean isOperatorMasterExist(OperatorMaster operatorMaster);
	
	public List<OperatorMaster> findOperatorMasterBySearchType(OperatorMaster operatorMaster, String searchType);
	
	public OperatorMaster findDetailsByMobileNo(OperatorMaster operatorMaster) throws Exception;
	
	public OperatorMaster findDetailsByEmail(OperatorMaster operatorMaster) throws Exception;
	
   
}
