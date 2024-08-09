package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.EquipmentImages;

public interface EquipmentImagesService {
	
	public EquipmentImages findById(long id);
	
	public EquipmentImages validEquipmentImages(EquipmentImages equipmentImages);
	
	public EquipmentImages save(EquipmentImages equipmentImages);

	public EquipmentImages update(EquipmentImages equipmentImages);

	public EquipmentImages delete(EquipmentImages equipmentImages);
	
	public String updateEquipmentImages(EquipmentImages equipmentImages);
	
	public void deleteEquipmentImages(EquipmentImages equipmentImages);
	
	public List<EquipmentImages> findAllEquipmentImages();
	
	public boolean isEquipmentImagesExist(EquipmentImages equipmentImages);
	
	public List<EquipmentImages> findEquipmentImagesBySearchType(EquipmentImages equipmentImages, String searchType);
	
	public EquipmentImages findDetailsByMobileNo(EquipmentImages equipmentImages) throws Exception;
	
	public EquipmentImages findDetailsByEmail(EquipmentImages equipmentImages) throws Exception;
	
   
}
