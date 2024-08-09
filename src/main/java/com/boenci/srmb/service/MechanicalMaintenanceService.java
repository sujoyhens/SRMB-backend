package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.MechanicalMaintenance;

public interface MechanicalMaintenanceService {
	
	public MechanicalMaintenance findById(long id);
	
	public MechanicalMaintenance validMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance);
	
	public MechanicalMaintenance save(MechanicalMaintenance mechanicalMaintenance);

	public MechanicalMaintenance update(MechanicalMaintenance mechanicalMaintenance);

	public MechanicalMaintenance delete(MechanicalMaintenance mechanicalMaintenance);
	
	public String updateMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance);
	
	public void deleteMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance);
	
	public List<MechanicalMaintenance> findAllMechanicalMaintenance();
	
	public boolean isMechanicalMaintenanceExist(MechanicalMaintenance mechanicalMaintenance);
	
	public List<MechanicalMaintenance> findMechanicalMaintenanceBySearchType(MechanicalMaintenance mechanicalMaintenance, String searchType);
	
	public MechanicalMaintenance findDetailsByMobileNo(MechanicalMaintenance mechanicalMaintenance) throws Exception;
	
	public MechanicalMaintenance findDetailsByEmail(MechanicalMaintenance mechanicalMaintenance) throws Exception;
	
   
}
