package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.CcmSummary;

public interface CcmSummaryService {
	
	public CcmSummary findById(long id);
	
	public CcmSummary validCcmSummary(CcmSummary ccmSummary);
	
	public CcmSummary save(CcmSummary ccmSummary);

	public CcmSummary update(CcmSummary ccmSummary);
	
	public String updateCcmSummary(CcmSummary ccmSummary);
	
	public void deleteCcmSummary(CcmSummary ccmSummary);
	
	public List<CcmSummary> findAllCcmSummary();
	
	public boolean isCcmSummaryExist(CcmSummary ccmSummary);
	
	public List<CcmSummary> findCcmSummaryBySearchType(CcmSummary ccmSummary, String searchType);
	
	public CcmSummary findDetailsByMobileNo(CcmSummary ccmSummary) throws Exception;
	
	public CcmSummary findDetailsByEmail(CcmSummary ccmSummary) throws Exception;
	
   
}
