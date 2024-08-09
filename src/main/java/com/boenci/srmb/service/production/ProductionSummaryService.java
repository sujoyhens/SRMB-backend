package com.boenci.srmb.service.production;

import java.util.List;

import com.boenci.srmb.model.production.ProductionSummary;

public interface ProductionSummaryService {
	
	public ProductionSummary findById(long id);
	
	public ProductionSummary validProductionSummary(ProductionSummary productionSummary);
	
	public ProductionSummary save(ProductionSummary productionSummary);

	public ProductionSummary update(ProductionSummary productionSummary);
	
	public String updateProductionSummary(ProductionSummary productionSummary);
	
	public void deleteProductionSummary(ProductionSummary productionSummary);
	
	public List<ProductionSummary> findAllProductionSummary();
	
	public boolean isProductionSummaryExist(ProductionSummary productionSummary);
	
	public List<ProductionSummary> findProductionSummaryBySearchType(ProductionSummary productionSummary, String searchType);
	
	public ProductionSummary findDetailsByMobileNo(ProductionSummary productionSummary) throws Exception;
	
	public ProductionSummary findDetailsByEmail(ProductionSummary productionSummary) throws Exception;
	
   
}
