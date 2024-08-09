package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.ProductMaster;

public interface ProductMasterService {
	
	public ProductMaster findById(long id);
	
	public ProductMaster validProductMaster(ProductMaster productMaster);
	
	public ProductMaster save(ProductMaster productMaster);

	public ProductMaster update(ProductMaster areaMaster);

	public ProductMaster delete(ProductMaster areaMaster);
	
	public String updateProductMaster(ProductMaster productMaster);
	
	public void deleteProductMaster(ProductMaster productMaster);
	
	public List<ProductMaster> findAllProductMaster();
	
	public boolean isProductMasterExist(ProductMaster productMaster);
	
	public List<ProductMaster> findProductMasterBySearchType(ProductMaster productMaster, String searchType);
	
	public ProductMaster findDetailsByMobileNo(ProductMaster productMaster) throws Exception;
	
	public ProductMaster findDetailsByEmail(ProductMaster productMaster) throws Exception;
	
   
}
