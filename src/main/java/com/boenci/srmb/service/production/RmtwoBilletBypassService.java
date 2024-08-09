package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.RmtwoBilletBypass;

public interface RmtwoBilletBypassService {
	
	public RmtwoBilletBypass findById(long id);
	
	public RmtwoBilletBypass validRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass);
	
	public RmtwoBilletBypass save(RmtwoBilletBypass rmtwoBilletBypass);

	public RmtwoBilletBypass update(RmtwoBilletBypass rmtwoBilletBypass);

	public RmtwoBilletBypass delete(RmtwoBilletBypass rmtwoBilletBypass);
	
	public String updateRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass);
	
	public void deleteRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass);
	
	public List<RmtwoBilletBypass> findAllRmtwoBilletBypass();
	
	public boolean isRmtwoBilletBypassExist(RmtwoBilletBypass rmtwoBilletBypass);
	
	public List<RmtwoBilletBypass> findRmtwoBilletBypassBySearchType(RmtwoBilletBypass rmtwoBilletBypass, String searchType);
	
	public RmtwoBilletBypass findDetailsByMobileNo(RmtwoBilletBypass rmtwoBilletBypass) throws Exception;
	
	public RmtwoBilletBypass findDetailsByEmail(RmtwoBilletBypass rmtwoBilletBypass) throws Exception;
	
   
}
