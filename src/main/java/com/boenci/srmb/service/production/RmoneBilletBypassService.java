package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.RmoneBilletBypass;

public interface RmoneBilletBypassService {
	
	public RmoneBilletBypass findById(long id);
	
	public RmoneBilletBypass validRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass);
	
	public RmoneBilletBypass save(RmoneBilletBypass rmoneBilletBypass);

	public RmoneBilletBypass update(RmoneBilletBypass rmoneBilletBypass);

	public RmoneBilletBypass delete(RmoneBilletBypass rmoneBilletBypass);
	
	public String updateRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass);
	
	public void deleteRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass);
	
	public List<RmoneBilletBypass> findAllRmoneBilletBypass();
	
	public boolean isRmoneBilletBypassExist(RmoneBilletBypass rmoneBilletBypass);
	
	public List<RmoneBilletBypass> findRmoneBilletBypassBySearchType(RmoneBilletBypass rmoneBilletBypass, String searchType);
	
	public RmoneBilletBypass findDetailsByMobileNo(RmoneBilletBypass rmoneBilletBypass) throws Exception;
	
	public RmoneBilletBypass findDetailsByEmail(RmoneBilletBypass rmoneBilletBypass) throws Exception;
	
   
}
