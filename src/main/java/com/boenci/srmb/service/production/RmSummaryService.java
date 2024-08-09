package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.RmSummary;

public interface RmSummaryService {
	
	public RmSummary findById(long id);
	
	public RmSummary validRmSummary(RmSummary rmSummary);
	
	public RmSummary save(RmSummary rmSummary);

	public RmSummary update(RmSummary rmSummary);
	
	public String updateRmSummary(RmSummary rmSummary);
	
	public void deleteRmSummary(RmSummary rmSummary);
	
	public List<RmSummary> findAllRmSummary();
	
	public boolean isRmSummaryExist(RmSummary rmSummary);
	
	public List<RmSummary> findRmSummaryBySearchType(RmSummary rmSummary, String searchType);
	
	public RmSummary findDetailsByMobileNo(RmSummary rmSummary) throws Exception;
	
	public RmSummary findDetailsByEmail(RmSummary rmSummary) throws Exception;
	
   
}
