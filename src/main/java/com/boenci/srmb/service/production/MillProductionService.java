package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.MillProduction;

public interface MillProductionService {
	
	public MillProduction findById(long id);
	
	public MillProduction validMillProduction(MillProduction millProduction);
	
	public MillProduction save(MillProduction millProduction);

	public MillProduction update(MillProduction millProduction);
	
	public String updateMillProduction(MillProduction millProduction);
	
	public void deleteMillProduction(MillProduction millProduction);
	
	public List<MillProduction> findAllMillProduction();
	
	public boolean isMillProductionExist(MillProduction millProduction);
	
	public List<MillProduction> findMillProductionBySearchType(MillProduction millProduction, String searchType);
	
	public MillProduction findDetailsByMobileNo(MillProduction millProduction) throws Exception;
	
	public MillProduction findDetailsByEmail(MillProduction millProduction) throws Exception;
	
   
}
