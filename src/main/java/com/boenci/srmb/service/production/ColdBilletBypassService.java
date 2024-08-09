package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.ColdBilletBypass;

public interface ColdBilletBypassService {
	
	public ColdBilletBypass findById(long id);
	
	public ColdBilletBypass validColdBilletBypass(ColdBilletBypass coldBilletBypass);
	
	public ColdBilletBypass save(ColdBilletBypass coldBilletBypass);

	public ColdBilletBypass update(ColdBilletBypass coldBilletBypass);
	
	public String updateColdBilletBypass(ColdBilletBypass coldBilletBypass);
	
	public void deleteColdBilletBypass(ColdBilletBypass coldBilletBypass);
	
	public List<ColdBilletBypass> findAllColdBilletBypass();
	
	public boolean isColdBilletBypassExist(ColdBilletBypass coldBilletBypass);
	
	public List<ColdBilletBypass> findColdBilletBypassBySearchType(ColdBilletBypass coldBilletBypass, String searchType);
	
	public ColdBilletBypass findDetailsByMobileNo(ColdBilletBypass coldBilletBypass) throws Exception;
	
	public ColdBilletBypass findDetailsByEmail(ColdBilletBypass coldBilletBypass) throws Exception;
	
   
}
