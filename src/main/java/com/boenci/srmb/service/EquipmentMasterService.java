package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.EquipmentMaster;

public interface EquipmentMasterService {
	
	public EquipmentMaster findById(long id);
	
	public EquipmentMaster validEquipmentMaster(EquipmentMaster equipmentMaster);
	
	public EquipmentMaster save(EquipmentMaster equipmentMaster);

	public EquipmentMaster update(EquipmentMaster equipmentMaster);

	public EquipmentMaster delete(EquipmentMaster equipmentMaster);
	
	public String updateEquipmentMaster(EquipmentMaster equipmentMaster);
	
	public void deleteEquipmentMaster(EquipmentMaster equipmentMaster);
	
	public List<EquipmentMaster> findAllEquipmentMaster();
	
	public boolean isEquipmentMasterExist(EquipmentMaster equipmentMaster);
	
	public List<EquipmentMaster> findEquipmentMasterBySearchType(EquipmentMaster equipmentMaster, String searchType);
	
	public EquipmentMaster findDetailsByMobileNo(EquipmentMaster equipmentMaster) throws Exception;
	
	public EquipmentMaster findDetailsByEmail(EquipmentMaster equipmentMaster) throws Exception;
	
   
}
