package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.SubEquipmentMaster;

public interface SubEquipmentMasterService {
	
	public SubEquipmentMaster findById(long id);
	
	public SubEquipmentMaster validSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster);
	
	public SubEquipmentMaster save(SubEquipmentMaster subEquipmentMaster);

	public SubEquipmentMaster update(SubEquipmentMaster subEquipmentMaster);

	public SubEquipmentMaster delete(SubEquipmentMaster subEquipmentMaster);
	
	public String updateSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster);
	
	public String deleteSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster);
	
	public List<SubEquipmentMaster> findAllSubEquipmentMaster();
	
	public boolean isSubEquipmentMasterExist(SubEquipmentMaster subEquipmentMaster);
	
	public List<SubEquipmentMaster> findSubEquipmentMasterBySearchType(SubEquipmentMaster subEquipmentMaster, String searchType);
	
	public SubEquipmentMaster findDetailsByMobileNo(SubEquipmentMaster subEquipmentMaster) throws Exception;
	
	public SubEquipmentMaster findDetailsByEmail(SubEquipmentMaster subEquipmentMaster) throws Exception;
	
   
}
