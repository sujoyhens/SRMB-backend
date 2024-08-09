package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.SequenceParameter;

public interface SequenceParameterService {
	
	public SequenceParameter findById(long id);
	
	public SequenceParameter validSequenceParameter(SequenceParameter sequenceParameter);
	
	public SequenceParameter save(SequenceParameter sequenceParameter);

	public SequenceParameter update(SequenceParameter sequenceParameter);
	
	public String updateSequenceParameter(SequenceParameter sequenceParameter);
	
	public void deleteSequenceParameter(SequenceParameter sequenceParameter);
	
	public List<SequenceParameter> findAllSequenceParameter();
	
	public boolean isSequenceParameterExist(SequenceParameter sequenceParameter);
	
	public List<SequenceParameter> findSequenceParameterBySearchType(SequenceParameter sequenceParameter, String searchType);
	
	public SequenceParameter findDetailsByMobileNo(SequenceParameter sequenceParameter) throws Exception;
	
	public SequenceParameter findDetailsByEmail(SequenceParameter sequenceParameter) throws Exception;
	
   
}
