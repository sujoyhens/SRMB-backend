package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.AreaMaster;
import com.boenci.srmb.repository.AreaMasterRepository;
import com.boenci.srmb.service.AreaMasterService;

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
public class AreaMasterServiceImpl implements AreaMasterService {

	@Autowired
	private AreaMasterRepository areaMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AreaMaster findById(long id) {
		AreaMaster newAreaMaster = null;
		newAreaMaster =areaMasterRepository.findById(id).get();		
		return newAreaMaster;
	}

	@Override
	public AreaMaster validAreaMaster(AreaMaster areaMaster) {
		AreaMaster newAreaMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AreaMaster> criteriaQuery = criteriaBuilder.createQuery(AreaMaster.class);
		Root<AreaMaster> areaMasterRoot = criteriaQuery.from(AreaMaster.class);
		criteriaQuery.select(areaMasterRoot);
		Predicate predicateUnitmasterid = criteriaBuilder.equal(areaMasterRoot.get("unitmasterid"), areaMaster.getUnitmasterid());
		Predicate predicateUnittype = criteriaBuilder.equal(areaMasterRoot.get("unittype"), areaMaster.getUnittype());
		Predicate predicateAreaname = criteriaBuilder.equal(areaMasterRoot.get("areaname"), areaMaster.getAreaname());
		Predicate predicateStatus = criteriaBuilder.equal(areaMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(areaMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateUnitmasterid,predicateUnittype,predicateAreaname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<AreaMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<AreaMaster> areaMasterList = typedQuery.getResultList();
		if(areaMasterList.size() != 0){
			newAreaMaster = (AreaMaster)areaMasterList.get(0);
		}
		return newAreaMaster;
	}

	@Override
	public AreaMaster save(AreaMaster areaMaster) {
		AreaMaster newAreaMaster = null;
		newAreaMaster = areaMasterRepository.save(areaMaster);
		return newAreaMaster;
	}

	@Transactional
	@Override
	public AreaMaster update(AreaMaster areaMaster) {
		long areaMasterId = areaMaster.getAreamasterid();
		AreaMaster newAreaMaster = null;
		if(areaMasterRepository.existsById(areaMasterId)){
			newAreaMaster = areaMasterRepository.save(areaMaster);
			return newAreaMaster;
		}else{
			return newAreaMaster;
		}
	}

	@Transactional
	@Override
	public AreaMaster delete(AreaMaster areaMaster) {
		long areaMasterId = areaMaster.getAreamasterid();
		AreaMaster newAreaMaster = null;
		if(areaMasterRepository.existsById(areaMasterId)){
			newAreaMaster = areaMasterRepository.save(areaMaster);
			return newAreaMaster;
		}else{
			return newAreaMaster;
		}
	}

	@Transactional
	@Override
	public String updateAreaMaster(AreaMaster areaMaster) {
		
		AreaMaster validateAreaMaster = validAreaMaster(areaMaster);
		if(validateAreaMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<AreaMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(AreaMaster.class);
			Root<AreaMaster> areaMasterRoot = criteriaUpdate.from(AreaMaster.class);
			criteriaUpdate.set("areaname", areaMaster.getAreaname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", areaMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(areaMasterRoot.get("areamasterid"), areaMaster.getAreamasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<AreaMaster> findAllAreaMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AreaMaster> criteriaQuery = criteriaBuilder.createQuery(AreaMaster.class);
		Root<AreaMaster> areaMasterRoot = criteriaQuery.from(AreaMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(areaMasterRoot.get("areaname")));
		List<AreaMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isAreaMasterExist(AreaMaster areaMaster) {
		
		return true;
	}

	@Override
	public List<AreaMaster> findAreaMasterBySearchType(AreaMaster areaMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AreaMaster> criteriaQuery = criteriaBuilder.createQuery(AreaMaster.class);
		Root<AreaMaster> areaMasterRoot = criteriaQuery.from(AreaMaster.class);
		criteriaQuery.select(areaMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT am.unitmasterid,am.enterprisemasterid,(select enterprisename "+ 
			"from EnterpriseMaster em where em.enterprisemasterid = am.enterprisemasterid)  enterprisename,"+
			" am.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = am.plantmasterid) as plantname, am.sitemasterid, "+
			"(select sitename from SiteMaster sm where sm.sitemasterid = am.sitemasterid) as sitename,"+
			"  am.remarks, am.areaname, am.unittype, am.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = am.unitmasterid) as unitname 	FROM AreaMaster am "+
			" WHERE am.deletedflag = ?1 and am.status = ?2";
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<AreaMaster> areaMasterList = new ArrayList<AreaMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					AreaMaster newAreaMaster = new AreaMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newAreaMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newAreaMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newAreaMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newAreaMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newAreaMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setUnitname(newObject.toString());
						}
					}
					areaMasterList.add(newAreaMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return areaMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(areaMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(areaMasterRoot.get("status"), areaMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("unitmasterid")){
			String strSql = "SELECT am.unitmasterid,am.enterprisemasterid,(select enterprisename "+ 
			"from EnterpriseMaster em where em.enterprisemasterid = am.enterprisemasterid)  enterprisename,"+
			" am.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = am.plantmasterid) as plantname, am.sitemasterid, "+
			"(select sitename from SiteMaster sm where sm.sitemasterid = am.sitemasterid) as sitename,"+
			"  am.remarks, am.areaname, am.unittype, am.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = am.unitmasterid) as unitname 	FROM AreaMaster am "+
			" WHERE am.deletedflag = ?1 and am.status = ?2 and am.unitmasterid = ?3";
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,areaMaster.getUnitmasterid());
			List<AreaMaster> areaMasterList = new ArrayList<AreaMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					AreaMaster newAreaMaster = new AreaMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newAreaMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newAreaMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newAreaMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newAreaMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newAreaMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newAreaMaster.setUnitname(newObject.toString());
						}
					}
					areaMasterList.add(newAreaMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return areaMasterList;

		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<AreaMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<AreaMaster> areaMasterList = typedQuery.getResultList();
		return areaMasterList;
	}
	@Transactional
	@Override
	public void deleteAreaMaster(AreaMaster areaMaster) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<AreaMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(AreaMaster.class);
		Root<AreaMaster> areaMasterRoot = criteriaUpdate.from(AreaMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(areaMasterRoot.get("areamasterid"), areaMaster.getAreamasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public AreaMaster findDetailsByMobileNo(AreaMaster areaMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AreaMaster findDetailsByEmail(AreaMaster areaMaster) {
		AreaMaster newAreaMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AreaMaster> criteriaQuery = criteriaBuilder.createQuery(AreaMaster.class);
		Root<AreaMaster> areaMasterRoot = criteriaQuery.from(AreaMaster.class);
		criteriaQuery.select(areaMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(areaMasterRoot.get("areaname"), areaMaster.getAreaname());
		Predicate predicateStatus = criteriaBuilder.equal(areaMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(areaMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<AreaMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<AreaMaster> areaMasterList = typedQuery.getResultList();
		if(areaMasterList.size() != 0){
			newAreaMaster = (AreaMaster)areaMasterList.get(0);
		}
		return newAreaMaster;
	}

	
}
