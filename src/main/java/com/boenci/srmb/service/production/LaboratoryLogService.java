package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.LaboratoryLog;

public interface LaboratoryLogService {
	
	public LaboratoryLog findById(long id);
	
	public LaboratoryLog validLaboratoryLog(LaboratoryLog laboratoryLog);
	
	public LaboratoryLog save(LaboratoryLog laboratoryLog);

	public LaboratoryLog update(LaboratoryLog laboratoryLog);
	
	public String updateLaboratoryLog(LaboratoryLog laboratoryLog);
	
	public void deleteLaboratoryLog(LaboratoryLog laboratoryLog);
	
	public List<LaboratoryLog> findAllLaboratoryLog();
	
	public boolean isLaboratoryLogExist(LaboratoryLog laboratoryLog);
	
	public List<LaboratoryLog> findLaboratoryLogBySearchType(LaboratoryLog laboratoryLog, String searchType);
	
	public LaboratoryLog findDetailsByMobileNo(LaboratoryLog laboratoryLog) throws Exception;
	
	public LaboratoryLog findDetailsByEmail(LaboratoryLog laboratoryLog);
	
   
}
