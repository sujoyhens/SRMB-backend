package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.UnitMaster;
import com.boenci.srmb.repository.UnitMasterRepository;
import com.boenci.srmb.service.UnitMasterService;

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
public class UnitMasterServiceImpl implements UnitMasterService {

	@Autowired
	private UnitMasterRepository unitMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UnitMaster findById(long id) {
		UnitMaster newUnitMaster = null;
		newUnitMaster =unitMasterRepository.findById(id).get();		
		return newUnitMaster;
	}

	@Transactional
	@Override
	public UnitMaster update(UnitMaster unitMaster) {
		long unitMasterId = unitMaster.getUnitmasterid();
		UnitMaster newUnitMaster = null;
		if(unitMasterRepository.existsById(unitMasterId)){
			newUnitMaster = unitMasterRepository.save(unitMaster);
			return newUnitMaster;
		}else{
			return newUnitMaster;
		}
	}

	@Transactional
	@Override
	public UnitMaster delete(UnitMaster unitMaster) {
		long unitMasterId = unitMaster.getUnitmasterid();
		UnitMaster newUnitMaster = null;
		if(unitMasterRepository.existsById(unitMasterId)){
			newUnitMaster = unitMasterRepository.save(unitMaster);
			return newUnitMaster;
		}else{
			return newUnitMaster;
		}
	}


	@Override
	public UnitMaster validUnitMaster(UnitMaster unitMaster) {
		UnitMaster newUnitMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnitMaster> criteriaQuery = criteriaBuilder.createQuery(UnitMaster.class);
		Root<UnitMaster> unitMasterRoot = criteriaQuery.from(UnitMaster.class);
		criteriaQuery.select(unitMasterRoot);
		Predicate predicateunittype = criteriaBuilder.equal(unitMasterRoot.get("unittype"), unitMaster.getUnittype());
		Predicate predicateEmail = criteriaBuilder.equal(unitMasterRoot.get("unitname"), unitMaster.getUnitname());
		Predicate predicateStatus = criteriaBuilder.equal(unitMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(unitMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<UnitMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<UnitMaster> unitMasterList = typedQuery.getResultList();
		if(unitMasterList.size() != 0){
			newUnitMaster = (UnitMaster)unitMasterList.get(0);
		}
		return newUnitMaster;
	}

	@Override
	public UnitMaster save(UnitMaster unitMaster) {
		UnitMaster newUnitMaster = null;
		newUnitMaster = unitMasterRepository.save(unitMaster);
		return newUnitMaster;
	}

	@Transactional
	@Override
	public String updateUnitMaster(UnitMaster unitMaster) {
		
		UnitMaster validateUnitMaster = validUnitMaster(unitMaster);
		if(validateUnitMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<UnitMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(UnitMaster.class);
			Root<UnitMaster> unitMasterRoot = criteriaUpdate.from(UnitMaster.class);
			criteriaUpdate.set("unitname", unitMaster.getUnitname());
			criteriaUpdate.set("unittype", unitMaster.getUnittype());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", unitMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(unitMasterRoot.get("unitmasterid"), unitMaster.getUnitmasterid()));
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
	public List<UnitMaster> findAllUnitMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnitMaster> criteriaQuery = criteriaBuilder.createQuery(UnitMaster.class);
		Root<UnitMaster> unitMasterRoot = criteriaQuery.from(UnitMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(unitMasterRoot.get("unitname")));
		List<UnitMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isUnitMasterExist(UnitMaster unitMaster) {
		
		return true;
	}

	@Override
	public List<UnitMaster> findUnitMasterBySearchType(UnitMaster unitMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnitMaster> criteriaQuery = criteriaBuilder.createQuery(UnitMaster.class);
		Root<UnitMaster> unitMasterRoot = criteriaQuery.from(UnitMaster.class);
		criteriaQuery.select(unitMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT um.unitmasterid,um.enterprisemasterid,(select enterprisename "+
			" from EnterpriseMaster em where em.enterprisemasterid = um.enterprisemasterid)  enterprisename,"+
			" um.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = um.plantmasterid) "+
			" as plantname, um.sitemasterid, (select sitename from SiteMaster sm where "+
			"sm.sitemasterid = um.sitemasterid) as sitename,  um.remarks, um.unitname, um.unittype "+
			" FROM UnitMaster um WHERE um.deletedflag = ?1 and um.status = ?2";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<UnitMaster> unitMasterList = new ArrayList<UnitMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					UnitMaster newUnitMaster = new UnitMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newUnitMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newUnitMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newUnitMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newUnitMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setUnittype(newObject.toString());
						}
					}
					unitMasterList.add(newUnitMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return unitMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(unitMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(unitMasterRoot.get("status"), unitMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<UnitMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<UnitMaster> unitMasterList = typedQuery.getResultList();
			return unitMasterList;
			
		}else if(searchType.equalsIgnoreCase("plantmasterid")){
			String strSql = "SELECT um.unitmasterid,um.enterprisemasterid,(select enterprisename "+
			" from EnterpriseMaster em where em.enterprisemasterid = um.enterprisemasterid)  enterprisename,"+
			" um.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = um.plantmasterid) "+
			" as plantname, um.sitemasterid, (select sitename from SiteMaster sm where "+
			"sm.sitemasterid = um.sitemasterid) as sitename,  um.remarks, um.unitname, um.unittype "+
			" FROM UnitMaster um WHERE um.deletedflag = ?1 and um.status = ?2 and plantmasterid = ?3";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,unitMaster.getPlantmasterid());
			List<UnitMaster> unitMasterList = new ArrayList<UnitMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					UnitMaster newUnitMaster = new UnitMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newUnitMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newUnitMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newUnitMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newUnitMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newUnitMaster.setUnittype(newObject.toString());
						}
					}
					unitMasterList.add(newUnitMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return unitMasterList;
		}else{
			List<UnitMaster> unitMasterList = null;
		return unitMasterList;
		}
		
	}
	@Transactional
	@Override
	public void deleteUnitMaster(UnitMaster unitMaster) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<UnitMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(UnitMaster.class);
		Root<UnitMaster> unitMasterRoot = criteriaUpdate.from(UnitMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(unitMasterRoot.get("unitmasterid"), unitMaster.getUnitmasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();

	}

	@Override
	public UnitMaster findDetailsByMobileNo(UnitMaster unitMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnitMaster findDetailsByEmail(UnitMaster unitMaster) {
		UnitMaster newUnitMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnitMaster> criteriaQuery = criteriaBuilder.createQuery(UnitMaster.class);
		Root<UnitMaster> unitMasterRoot = criteriaQuery.from(UnitMaster.class);
		criteriaQuery.select(unitMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(unitMasterRoot.get("unitname"), unitMaster.getUnitname());
		Predicate predicateStatus = criteriaBuilder.equal(unitMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(unitMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<UnitMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<UnitMaster> unitMasterList = typedQuery.getResultList();
		if(unitMasterList.size() != 0){
			newUnitMaster = (UnitMaster)unitMasterList.get(0);
		}
		return newUnitMaster;
	}
	
}
