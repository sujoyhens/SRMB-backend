package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.PlantMaster;

public interface PlantMasterService {
	
	public PlantMaster findById(long id);
	
	public PlantMaster validPlantMaster(PlantMaster plantMaster);
	
	public PlantMaster save(PlantMaster plantMaster);

	public PlantMaster update(PlantMaster plantMaster);

	public PlantMaster delete(PlantMaster plantMaster);
	
	public String updatePlantMaster(PlantMaster plantMaster);
	
	public void deletePlantMaster(PlantMaster plantMaster);
	
	public List<PlantMaster> findAllPlantMaster();
	
	public boolean isPlantMasterExist(PlantMaster plantMaster);
	
	public List<PlantMaster> findPlantMasterBySearchType(PlantMaster plantMaster, String searchType);
	
	public PlantMaster findDetailsByMobileNo(PlantMaster plantMaster) throws Exception;
	
	public PlantMaster findDetailsByEmail(PlantMaster plantMaster) throws Exception;
	
   
}
