package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.InspectionMaster;

public interface InspectionMasterService {
	
	public InspectionMaster findById(long id);
	
	public InspectionMaster validInspectionMaster(InspectionMaster inspectionMaster);
	
	public InspectionMaster save(InspectionMaster inspectionMaster);

	public InspectionMaster update(InspectionMaster inspectionMaster);

	public InspectionMaster delete(InspectionMaster inspectionMaster);
	
	public String updateInspectionMaster(InspectionMaster inspectionMaster);
	
	public void deleteInspectionMaster(InspectionMaster inspectionMaster);
	
	public List<InspectionMaster> findAllInspectionMaster();
	
	public boolean isInspectionMasterExist(InspectionMaster inspectionMaster);
	
	public List<InspectionMaster> findInspectionMasterBySearchType(InspectionMaster inspectionMaster, String searchType);
	
	public InspectionMaster findDetailsByMobileNo(InspectionMaster inspectionMaster) throws Exception;
	
	public InspectionMaster findDetailsByEmail(InspectionMaster inspectionMaster) throws Exception;
	
   
}
