package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.LaboratorySummary;

public interface LaboratorySummaryService {
	
	public LaboratorySummary findById(long id);
	
	public LaboratorySummary validLaboratorySummary(LaboratorySummary laboratorySummary);
	
	public LaboratorySummary save(LaboratorySummary laboratorySummary);

	public LaboratorySummary update(LaboratorySummary laboratorySummary);
	
	public String updateLaboratorySummary(LaboratorySummary laboratorySummary);
	
	public void deleteLaboratorySummary(LaboratorySummary laboratorySummary);
	
	public List<LaboratorySummary> findAllLaboratorySummary();
	
	public boolean isLaboratorySummaryExist(LaboratorySummary laboratorySummary);
	
	public List<LaboratorySummary> findLaboratorySummaryBySearchType(LaboratorySummary laboratorySummary, String searchType);
	
	public LaboratorySummary findDetailsByMobileNo(LaboratorySummary laboratorySummary) throws Exception;
	
	public LaboratorySummary findDetailsByEmail(LaboratorySummary laboratorySummary) throws Exception;
	
   
}
