package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.CcmBilletBypass;

public interface CcmBilletBypassService {
	
	public CcmBilletBypass findById(long id);
	
	public CcmBilletBypass validCcmBilletBypass(CcmBilletBypass ccmBilletBypass);
	
	public CcmBilletBypass save(CcmBilletBypass ccmBilletBypass);

	public CcmBilletBypass update(CcmBilletBypass ccmBilletBypass);

	public CcmBilletBypass delete(CcmBilletBypass ccmBilletBypass);
	
	public String updateCcmBilletBypass(CcmBilletBypass ccmBilletBypass);
	
	public void deleteCcmBilletBypass(CcmBilletBypass ccmBilletBypass);
	
	public List<CcmBilletBypass> findAllCcmBilletBypass();
	
	public boolean isCcmBilletBypassExist(CcmBilletBypass ccmBilletBypass);
	
	public List<CcmBilletBypass> findCcmBilletBypassBySearchType(CcmBilletBypass ccmBilletBypass, String searchType);
	
	public CcmBilletBypass findDetailsByMobileNo(CcmBilletBypass ccmBilletBypass) throws Exception;
	
	public CcmBilletBypass findDetailsByEmail(CcmBilletBypass ccmBilletBypass) throws Exception;
	
   
}
