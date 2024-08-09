package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.ProductionLog;

public interface ProductionLogService {
	
	public ProductionLog findById(long id);
	
	public ProductionLog validProductionLog(ProductionLog productionLog);
	
	public ProductionLog save(ProductionLog productionLog);

	public ProductionLog update(ProductionLog productionLog);
	
	public String updateProductionLog(ProductionLog productionLog);
	
	public void deleteProductionLog(ProductionLog productionLog);
	
	public List<ProductionLog> findAllProductionLog();
	
	public boolean isProductionLogExist(ProductionLog productionLog);
	
	public List<ProductionLog> findProductionLogBySearchType(ProductionLog productionLog, String searchType);
	
	public ProductionLog findDetailsByMobileNo(ProductionLog productionLog) throws Exception;
	
	public ProductionLog findDetailsByEmail(ProductionLog productionLog);
	
   
}
