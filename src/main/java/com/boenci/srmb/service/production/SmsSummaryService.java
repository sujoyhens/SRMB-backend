package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.SmsSummary;

public interface SmsSummaryService {
	
	public SmsSummary findById(long id);
	
	public SmsSummary validSmsSummary(SmsSummary smsSummary);
	
	public SmsSummary save(SmsSummary smsSummary);

	public SmsSummary update(SmsSummary smsSummary);
	
	public String updateSmsSummary(SmsSummary smsSummary);
	
	public void deleteSmsSummary(SmsSummary smsSummary);
	
	public List<SmsSummary> findAllSmsSummary();
	
	public boolean isSmsSummaryExist(SmsSummary smsSummary);
	
	public List<SmsSummary> findSmsSummaryBySearchType(SmsSummary smsSummary, String searchType);
	
	public SmsSummary findDetailsByMobileNo(SmsSummary smsSummary) throws Exception;
	
	public SmsSummary findDetailsByEmail(SmsSummary smsSummary) throws Exception;
	
   
}
