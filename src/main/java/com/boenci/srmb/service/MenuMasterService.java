package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.MenuMaster;

public interface MenuMasterService {
	
	public MenuMaster findById(long id);
	
	public MenuMaster validMenuMaster(MenuMaster menuMaster);
	
	public MenuMaster save(MenuMaster menuMaster);

	public MenuMaster update(MenuMaster areaMaster);

	public MenuMaster delete(MenuMaster areaMaster);
	
	public String updateMenuMaster(MenuMaster menuMaster);
	
	public void deleteMenuMaster(MenuMaster menuMaster);
	
	public List<MenuMaster> findAllMenuMaster();
	
	public boolean isMenuMasterExist(MenuMaster menuMaster);
	
	public List<MenuMaster> findMenuMasterBySearchType(MenuMaster menuMaster, String searchType);
	
	public MenuMaster findDetailsByMobileNo(MenuMaster menuMaster) throws Exception;
	
	public MenuMaster findDetailsByEmail(MenuMaster menuMaster) throws Exception;
	
   
}
