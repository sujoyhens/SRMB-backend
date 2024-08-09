package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.RollDetails;

public interface RollDetailsService {
	
	public RollDetails findById(long id);
	
	public RollDetails validRollDetails(RollDetails rollDetails);
	
	public RollDetails save(RollDetails rollDetails);

	public RollDetails update(RollDetails rollDetails);

	public RollDetails delete(RollDetails rollDetails);
	
	public String updateRollDetails(RollDetails rollDetails);
	
	public void deleteRollDetails(RollDetails rollDetails);
	
	public List<RollDetails> findAllRollDetails();
	
	public boolean isRollDetailsExist(RollDetails rollDetails);
	
	public List<RollDetails> findRollDetailsBySearchType(RollDetails rollDetails, String searchType);
	
	public RollDetails findDetailsByMobileNo(RollDetails rollDetails) throws Exception;
	
	public RollDetails findDetailsByEmail(RollDetails rollDetails) throws Exception;
	
   
}
