package com.boenci.srmb.service;

import java.util.List;

import com.boenci.srmb.model.RoleMenu;

public interface RoleMenuService {
	
	public RoleMenu findById(long id);
	
	public RoleMenu validRoleMenu(RoleMenu roleMenu);
	
	public RoleMenu save(RoleMenu roleMenu);

	public RoleMenu update(RoleMenu areaMaster);

	public RoleMenu delete(RoleMenu areaMaster);
	
	public String updateRoleMenu(RoleMenu roleMenu);
	
	public String deleteRoleMenu(RoleMenu roleMenu);
	
	public List<RoleMenu> findAllRoleMenu();
	
	public boolean isRoleMenuExist(RoleMenu roleMenu);
	
	public List<RoleMenu> findRoleMenuBySearchType(RoleMenu roleMenu, String searchType);
	
	public RoleMenu findDetailsByMobileNo(RoleMenu roleMenu) throws Exception;
	
	public RoleMenu findDetailsByEmail(RoleMenu roleMenu) throws Exception;
	
   
}
