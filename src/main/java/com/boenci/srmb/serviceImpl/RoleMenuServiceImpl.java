package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.RoleMenu;
import com.boenci.srmb.repository.RoleMenuRepository;
import com.boenci.srmb.service.RoleMenuService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {

	@Autowired
	private RoleMenuRepository roleMenuRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RoleMenu findById(long id) {
		RoleMenu newRoleMenu = null;
		newRoleMenu =roleMenuRepository.findById(id).get();		
		return newRoleMenu;
	}

	@Transactional
	@Override
	public RoleMenu update(RoleMenu roleMenu) {
		long roleMenuId = roleMenu.getRolemenuid();
		RoleMenu newRoleMenu = null;
		if(roleMenuRepository.existsById(roleMenuId)){
			newRoleMenu = roleMenuRepository.save(roleMenu);
			return newRoleMenu;
		}else{
			return newRoleMenu;
		}
	}

	@Transactional
	@Override
	public RoleMenu delete(RoleMenu roleMenu) {
		long roleMenuId = roleMenu.getRolemenuid();
		RoleMenu newRoleMenu = null;
		if(roleMenuRepository.existsById(roleMenuId)){
			newRoleMenu = roleMenuRepository.save(roleMenu);
			return newRoleMenu;
		}else{
			return newRoleMenu;
		}
	}


	@Override
	public RoleMenu validRoleMenu(RoleMenu roleMenu) {
		RoleMenu newRoleMenu = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMenu> criteriaQuery = criteriaBuilder.createQuery(RoleMenu.class);
		Root<RoleMenu> roleMenuRoot = criteriaQuery.from(RoleMenu.class);
		criteriaQuery.select(roleMenuRoot);
		Predicate predicateunittype = criteriaBuilder.equal(roleMenuRoot.get("rolemasterid"), roleMenu.getRolemasterid());
		Predicate predicateEmail = criteriaBuilder.equal(roleMenuRoot.get("menumasterid"), roleMenu.getMenumasterid());
		Predicate predicateStatus = criteriaBuilder.equal(roleMenuRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(roleMenuRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RoleMenu> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RoleMenu> roleMenuList = typedQuery.getResultList();
		if(roleMenuList.size() != 0){
			newRoleMenu = (RoleMenu)roleMenuList.get(0);
		}
		return newRoleMenu;
	}

	@Override
	public RoleMenu save(RoleMenu roleMenu) {
		RoleMenu newRoleMenu = null;
		newRoleMenu = roleMenuRepository.save(roleMenu);
		return newRoleMenu;
	}

	@Transactional
	@Override
	public String updateRoleMenu(RoleMenu roleMenu) {
		
		RoleMenu validateRoleMenu = validRoleMenu(roleMenu);
		if(validateRoleMenu == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<RoleMenu> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(RoleMenu.class);
			Root<RoleMenu> roleMenuRoot = criteriaUpdate.from(RoleMenu.class);
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", roleMenu.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(roleMenuRoot.get("rolemenuid"), roleMenu.getRolemenuid()));
			int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			rowCount = 1;
			if(rowCount > 0){
				return "Success";
			}else{
				return "Failure";
			}
		}else{
			return "Failure";
		}		
	}

	@Transactional
	@Override
	public String deleteRoleMenu(RoleMenu roleMenu) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RoleMenu> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RoleMenu.class);
		Root<RoleMenu> roleMenuRoot = criteriaUpdate.from(RoleMenu.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(roleMenuRoot.get("rolemasterid"), roleMenu.getRolemasterid()));
		int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		rowCount = 1;
		if(rowCount > 0){
			return "Success";
		}else{
			return "Failure";
		}

	}


	@Transactional
	@Override
	public List<RoleMenu> findAllRoleMenu() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMenu> criteriaQuery = criteriaBuilder.createQuery(RoleMenu.class);
		Root<RoleMenu> roleMenuRoot = criteriaQuery.from(RoleMenu.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(roleMenuRoot.get("unitname")));
		List<RoleMenu> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRoleMenuExist(RoleMenu roleMenu) {
		
		return true;
	}

	@Override
	public List<RoleMenu> findRoleMenuBySearchType(RoleMenu roleMenu, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMenu> criteriaQuery = criteriaBuilder.createQuery(RoleMenu.class);
		Root<RoleMenu> roleMenuRoot = criteriaQuery.from(RoleMenu.class);
		criteriaQuery.select(roleMenuRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT rolemenu.rolemenuid,rolemenu.rolemasterid,menumaster.menumasterid,menumaster.color, "+
			" menumaster.completed, menumaster.fsize,menumaster.icon,menumaster.menuid, "+
			" menumaster.parentid, menumaster.title,menumaster.link,menumaster.arrangementid FROM RoleMenu "+
			" rolemenu	INNER JOIN MenuMaster menumaster ON rolemenu.menumasterid = menumaster.menumasterid"+
			"  WHERE rolemenu.deletedflag = ?1 and rolemenu.status = ?2  and menumaster.visibility = ?3";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"yes");
			List<RoleMenu> roleMenuList = new ArrayList<RoleMenu>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					RoleMenu newRoleMenu = new RoleMenu();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newRoleMenu.setRolemenuid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newRoleMenu.setRolemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							newRoleMenu.setMenumasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setColor(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setCompleted(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setFsize(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setIcon(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setMenuid(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setParentid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setTitle(newObject.toString());
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setLink(newObject.toString());
						}else if(objectNDX == 11) {
							newRoleMenu.setArrangementid(Long.valueOf(newObject.toString()));
						}
					}
					roleMenuList.add(newRoleMenu);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return roleMenuList;
		}else if(searchType.equalsIgnoreCase("rolemasterid")){
			String strSql = "SELECT rolemenu.rolemenuid,rolemenu.rolemasterid,menumaster.menumasterid,menumaster.color, "+
			" menumaster.completed, menumaster.fsize,menumaster.icon,menumaster.menuid, "+
			" menumaster.parentid, menumaster.title,menumaster.link,menumaster.arrangementid FROM RoleMenu "+
			" rolemenu	INNER JOIN MenuMaster menumaster ON rolemenu.menumasterid = menumaster.menumasterid"+
			"  WHERE rolemenu.deletedflag = ?1 and rolemenu.status = ?2 and rolemenu.rolemasterid = ?3   and menumaster.visibility = ?4 order by menumaster.arrangementid";
			System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,roleMenu.getRolemasterid());
			query.setParameter(4,"yes");
			List<RoleMenu> roleMenuList = new ArrayList<RoleMenu>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					RoleMenu newRoleMenu = new RoleMenu();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newRoleMenu.setRolemenuid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newRoleMenu.setRolemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							newRoleMenu.setMenumasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setColor(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setCompleted(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setFsize(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setIcon(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setMenuid(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setParentid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setTitle(newObject.toString());
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newRoleMenu.setLink(newObject.toString());
						}else if(objectNDX == 11) {
							newRoleMenu.setArrangementid(Long.valueOf(newObject.toString()));
						}
					}
					roleMenuList.add(newRoleMenu);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return roleMenuList;
		}else{
			List<RoleMenu> roleMenuList = null;
			return roleMenuList;
		}
		
	}
	
	@Override
	public RoleMenu findDetailsByMobileNo(RoleMenu roleMenu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoleMenu findDetailsByEmail(RoleMenu roleMenu) {
		RoleMenu newRoleMenu = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMenu> criteriaQuery = criteriaBuilder.createQuery(RoleMenu.class);
		Root<RoleMenu> roleMenuRoot = criteriaQuery.from(RoleMenu.class);
		criteriaQuery.select(roleMenuRoot);
			Predicate predicateStatus = criteriaBuilder.equal(roleMenuRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(roleMenuRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RoleMenu> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RoleMenu> roleMenuList = typedQuery.getResultList();
		if(roleMenuList.size() != 0){
			newRoleMenu = (RoleMenu)roleMenuList.get(0);
		}
		return newRoleMenu;
	}
	
}
