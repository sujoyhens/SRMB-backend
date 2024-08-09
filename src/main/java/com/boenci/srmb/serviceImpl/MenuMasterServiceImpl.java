package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.MenuMaster;
import com.boenci.srmb.repository.MenuMasterRepository;
import com.boenci.srmb.service.MenuMasterService;

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
public class MenuMasterServiceImpl implements MenuMasterService {

	@Autowired
	private MenuMasterRepository menuMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MenuMaster findById(long id) {
		MenuMaster newMenuMaster = null;
		newMenuMaster =menuMasterRepository.findById(id).get();		
		return newMenuMaster;
	}

	@Transactional
	@Override
	public MenuMaster update(MenuMaster menuMaster) {
		long menuMasterId = menuMaster.getMenumasterid();
		MenuMaster newMenuMaster = null;
		if(menuMasterRepository.existsById(menuMasterId)){
			newMenuMaster = menuMasterRepository.save(menuMaster);
			return newMenuMaster;
		}else{
			return newMenuMaster;
		}
	}

	@Transactional
	@Override
	public MenuMaster delete(MenuMaster menuMaster) {
		long menuMasterId = menuMaster.getMenumasterid();
		MenuMaster newMenuMaster = null;
		if(menuMasterRepository.existsById(menuMasterId)){
			newMenuMaster = menuMasterRepository.save(menuMaster);
			return newMenuMaster;
		}else{
			return newMenuMaster;
		}
	}


	@Override
	public MenuMaster validMenuMaster(MenuMaster menuMaster) {
		MenuMaster newMenuMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MenuMaster> criteriaQuery = criteriaBuilder.createQuery(MenuMaster.class);
		Root<MenuMaster> menuMasterRoot = criteriaQuery.from(MenuMaster.class);
		criteriaQuery.select(menuMasterRoot);
		Predicate predicateunittype = criteriaBuilder.equal(menuMasterRoot.get("parentid"), menuMaster.getParentid());
		Predicate predicateEmail = criteriaBuilder.equal(menuMasterRoot.get("link"), menuMaster.getLink());
		Predicate predicateStatus = criteriaBuilder.equal(menuMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(menuMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MenuMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MenuMaster> menuMasterList = typedQuery.getResultList();
		if(menuMasterList.size() != 0){
			newMenuMaster = (MenuMaster)menuMasterList.get(0);
		}
		return newMenuMaster;
	}

	@Override
	public MenuMaster save(MenuMaster menuMaster) {
		MenuMaster newMenuMaster = null;
		newMenuMaster = menuMasterRepository.save(menuMaster);
		return newMenuMaster;
	}

	@Transactional
	@Override
	public String updateMenuMaster(MenuMaster menuMaster) {
		
		MenuMaster validateMenuMaster = validMenuMaster(menuMaster);
		if(validateMenuMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<MenuMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(MenuMaster.class);
			Root<MenuMaster> menuMasterRoot = criteriaUpdate.from(MenuMaster.class);
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", menuMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(menuMasterRoot.get("menumasterid"), menuMaster.getMenumasterid()));
			int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
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
	public List<MenuMaster> findAllMenuMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MenuMaster> criteriaQuery = criteriaBuilder.createQuery(MenuMaster.class);
		Root<MenuMaster> menuMasterRoot = criteriaQuery.from(MenuMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(menuMasterRoot.get("title")));
		List<MenuMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isMenuMasterExist(MenuMaster menuMaster) {
		
		return true;
	}

	@Override
	public List<MenuMaster> findMenuMasterBySearchType(MenuMaster menuMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MenuMaster> criteriaQuery = criteriaBuilder.createQuery(MenuMaster.class);
		Root<MenuMaster> menuMasterRoot = criteriaQuery.from(MenuMaster.class);
		criteriaQuery.select(menuMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			
			String strSql = "SELECT menumaster.menumasterid,menumaster.color, menumaster.completed,"+
			" menumaster.fsize,menumaster.icon,menumaster.menuid, menumaster.parentid,"+
			" menumaster.title,menumaster.link,menumaster.arrangementid FROM  MenuMaster menumaster "+
			" WHERE menumaster.deletedflag = ?1 AND menumaster.status = ?2 ORDER BY menumaster.arrangementid ASC";
			System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<MenuMaster> menuMasterList = new ArrayList<MenuMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MenuMaster newMenuMaster = new MenuMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newMenuMaster.setMenumasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setColor(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setCompleted(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setFsize(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setIcon(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setMenuid(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setParentid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setTitle(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newMenuMaster.setLink(newObject.toString());
						}else if(objectNDX == 9) {
							newMenuMaster.setArrangementid(Long.valueOf(newObject.toString()));
						}
					}
					menuMasterList.add(newMenuMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return menuMasterList;
		}else{
			return null;
		}
		
	}
	@Transactional
	@Override
	public void deleteMenuMaster(MenuMaster menuMaster) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<MenuMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(MenuMaster.class);
		Root<MenuMaster> menuMasterRoot = criteriaUpdate.from(MenuMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(menuMasterRoot.get("menumasterid"), menuMaster.getMenumasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();

	}

	@Override
	public MenuMaster findDetailsByMobileNo(MenuMaster menuMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuMaster findDetailsByEmail(MenuMaster menuMaster) {
		MenuMaster newMenuMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MenuMaster> criteriaQuery = criteriaBuilder.createQuery(MenuMaster.class);
		Root<MenuMaster> menuMasterRoot = criteriaQuery.from(MenuMaster.class);
		criteriaQuery.select(menuMasterRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(menuMasterRoot.get("unitname"), menuMaster.getUnitname());
		Predicate predicateStatus = criteriaBuilder.equal(menuMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(menuMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MenuMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MenuMaster> menuMasterList = typedQuery.getResultList();
		if(menuMasterList.size() != 0){
			newMenuMaster = (MenuMaster)menuMasterList.get(0);
		}
		return newMenuMaster;
	}
	
}
