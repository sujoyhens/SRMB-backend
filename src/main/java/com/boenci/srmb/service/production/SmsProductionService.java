package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.SmsProduction;

public interface SmsProductionService {
	
	public SmsProduction findById(long id);
	
	public SmsProduction validSmsProduction(SmsProduction smsProduction);
	
	public SmsProduction save(SmsProduction smsProduction);

	public SmsProduction update(SmsProduction smsProduction);
	
	public String updateSmsProduction(SmsProduction smsProduction);
	
	public void deleteSmsProduction(SmsProduction smsProduction);
	
	public List<SmsProduction> findAllSmsProduction();
	
	public boolean isSmsProductionExist(SmsProduction smsProduction);
	
	public List<SmsProduction> findSmsProductionBySearchType(SmsProduction smsProduction, String searchType);
	
	public SmsProduction findDetailsByMobileNo(SmsProduction smsProduction) throws Exception;
	
	public SmsProduction findDetailsByEmail(SmsProduction smsProduction);
	
   
}
