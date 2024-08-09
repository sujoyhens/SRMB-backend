package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.StandardMaster;
import com.boenci.srmb.repository.StandardMasterRepository;
import com.boenci.srmb.service.StandardMasterService;

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
public class StandardMasterServiceImpl implements StandardMasterService {

	@Autowired
	private StandardMasterRepository standardMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public StandardMaster findById(long id) {
		StandardMaster newStandardMaster = null;
		newStandardMaster =standardMasterRepository.findById(id).get();		
		return newStandardMaster;
	}

	@Override
	public StandardMaster validStandardMaster(StandardMaster standardMaster) {
		StandardMaster newStandardMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StandardMaster> criteriaQuery = criteriaBuilder.createQuery(StandardMaster.class);
		Root<StandardMaster> standardMasterRoot = criteriaQuery.from(StandardMaster.class);
		criteriaQuery.select(standardMasterRoot);
		Predicate predicateEquipment = criteriaBuilder.equal(standardMasterRoot.get("equipmentmasterid"), standardMaster.getEquipmentmasterid());
		Predicate predicateSubEquipment = criteriaBuilder.equal(standardMasterRoot.get("subequipmentmasterid"), standardMaster.getSubequipmentmasterid());
		Predicate predicateEmail = criteriaBuilder.equal(standardMasterRoot.get("standardname"), standardMaster.getStandardname());
		Predicate predicateStatus = criteriaBuilder.equal(standardMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(standardMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEquipment,predicateSubEquipment,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<StandardMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<StandardMaster> standardMasterList = typedQuery.getResultList();
		if(standardMasterList.size() != 0){
			newStandardMaster = (StandardMaster)standardMasterList.get(0);
		}
		return newStandardMaster;
	}

	@Override
	public StandardMaster save(StandardMaster standardMaster) {
		StandardMaster newStandardMaster = null;
		newStandardMaster = standardMasterRepository.save(standardMaster);
		return newStandardMaster;
	}

	@Transactional
	@Override
	public StandardMaster update(StandardMaster standardMaster) {
		long standardMasterId = standardMaster.getStandardmasterid();
		StandardMaster newStandardMaster = null;
		if(standardMasterRepository.existsById(standardMasterId)){
			newStandardMaster = standardMasterRepository.save(standardMaster);
			return newStandardMaster;
		}else{
			return newStandardMaster;
		}
	}

	@Transactional
	@Override
	public StandardMaster delete(StandardMaster standardMaster) {
		long standardMasterId = standardMaster.getStandardmasterid();
		StandardMaster newStandardMaster = null;
		if(standardMasterRepository.existsById(standardMasterId)){
			newStandardMaster = standardMasterRepository.save(standardMaster);
			return newStandardMaster;
		}else{
			return newStandardMaster;
		}
	}


	@Transactional
	@Override
	public String updateStandardMaster(StandardMaster standardMaster) {
		
		StandardMaster validateStandardMaster = validStandardMaster(standardMaster);
		if(validateStandardMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<StandardMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(StandardMaster.class);
			Root<StandardMaster> standardMasterRoot = criteriaUpdate.from(StandardMaster.class);
			criteriaUpdate.set("standardname", standardMaster.getStandardname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", standardMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(standardMasterRoot.get("standardmasterid"), standardMaster.getStandardmasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<StandardMaster> findAllStandardMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StandardMaster> criteriaQuery = criteriaBuilder.createQuery(StandardMaster.class);
		Root<StandardMaster> standardMasterRoot = criteriaQuery.from(StandardMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(standardMasterRoot.get("standardname")));
		List<StandardMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isStandardMasterExist(StandardMaster standardMaster) {
		
		return true;
	}

	@Override
	public List<StandardMaster> findStandardMasterBySearchType(StandardMaster standardMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StandardMaster> criteriaQuery = criteriaBuilder.createQuery(StandardMaster.class);
		Root<StandardMaster> standardMasterRoot = criteriaQuery.from(StandardMaster.class);
		criteriaQuery.select(standardMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename"+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid)"+
			"  enterprisename,om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = om.plantmasterid) as plantname, om.sitemasterid, (select sm.sitename "+
			" from SiteMaster sm where sm.sitemasterid = om.sitemasterid) as sitename, om.remarks,"+
			" (select am.areaname from AreaMaster am WHERE  am.areamasterid = om.areamasterid) as areaname,"+
			" om.unittype, om.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = om.unitmasterid) as unitname, om.equipmentmasterid,"+
			"(select em.equipmentname from EquipmentMaster em 	WHERE em.equipmentmasterid = om.equipmentmasterid)"+
			" as equipmentname, (SELECT  sem.subequipmentname 	FROM SubEquipmentMaster sem WHERE "+
			" sem.subequipmentmasterid = om.subequipmentmasterid) as subequipmentname,om.standardname, "+
			" om.standardmasterid,om.subequipmentmasterid"+
			" FROM StandardMaster om where om.deletedflag = ?1 and om.status = ?2 "+
			" ORDER BY om.standardname ASC";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<StandardMaster> standardMasterList = new ArrayList<StandardMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					StandardMaster newStandardMaster = new StandardMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newStandardMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newStandardMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newStandardMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newStandardMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newStandardMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							newStandardMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setStandardname(newObject.toString());
						}else if(objectNDX == 16) {
							newStandardMaster.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							newStandardMaster.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}
					}
					standardMasterList.add(newStandardMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return standardMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(standardMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(standardMasterRoot.get("status"), standardMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<StandardMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<StandardMaster> standardMasterList = typedQuery.getResultList();
			return standardMasterList;

		}else if(searchType.equalsIgnoreCase("subequipmentmasterid")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename"+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid)"+
			"  enterprisename,om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = om.plantmasterid) as plantname, om.sitemasterid, (select sm.sitename "+
			" from SiteMaster sm where sm.sitemasterid = om.sitemasterid) as sitename, om.remarks,"+
			" (select am.areaname from AreaMaster am WHERE  am.areamasterid = om.areamasterid) as areaname,"+
			" om.unittype, om.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = om.unitmasterid) as unitname, om.equipmentmasterid,"+
			"(select em.equipmentname from EquipmentMaster em 	WHERE em.equipmentmasterid = om.equipmentmasterid)"+
			" as equipmentname, (SELECT  sem.subequipmentname 	FROM SubEquipmentMaster sem WHERE "+
			" sem.subequipmentmasterid = om.subequipmentmasterid) as subequipmentname,om.standardname, "+
			" om.standardmasterid,om.subequipmentmasterid"+
			" FROM StandardMaster om where om.deletedflag = ?1 and om.status = ?2 and om.subequipmentmasterid = ?3"+
			" ORDER BY om.standardname ASC";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,standardMaster.getSubequipmentmasterid());
			List<StandardMaster> standardMasterList = new ArrayList<StandardMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					StandardMaster newStandardMaster = new StandardMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newStandardMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newStandardMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newStandardMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newStandardMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newStandardMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							newStandardMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setStandardname(newObject.toString());
						}else if(objectNDX == 16) {
							newStandardMaster.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							newStandardMaster.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}
					}
					standardMasterList.add(newStandardMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return standardMasterList;
		}else if(searchType.equalsIgnoreCase("equipmentmasterid")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename"+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid)"+
			"  enterprisename,om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = om.plantmasterid) as plantname, om.sitemasterid, (select sm.sitename "+
			" from SiteMaster sm where sm.sitemasterid = om.sitemasterid) as sitename, om.remarks,"+
			" (select am.areaname from AreaMaster am WHERE  am.areamasterid = om.areamasterid) as areaname,"+
			" om.unittype, om.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = om.unitmasterid) as unitname, om.equipmentmasterid,"+
			"(select em.equipmentname from EquipmentMaster em 	WHERE em.equipmentmasterid = om.equipmentmasterid)"+
			" as equipmentname, (SELECT  sem.subequipmentname 	FROM SubEquipmentMaster sem WHERE "+
			" sem.subequipmentmasterid = om.subequipmentmasterid) as subequipmentname,om.standardname, "+
			" om.standardmasterid,om.subequipmentmasterid"+
			" FROM StandardMaster om where om.deletedflag = ?1 and om.status = ?2 and om.equipmentmasterid = ?3"+
			" ORDER BY om.standardname ASC";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,standardMaster.getEquipmentmasterid());
			List<StandardMaster> standardMasterList = new ArrayList<StandardMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					StandardMaster newStandardMaster = new StandardMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newStandardMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newStandardMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newStandardMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newStandardMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newStandardMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							newStandardMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newStandardMaster.setStandardname(newObject.toString());
						}else if(objectNDX == 16) {
							newStandardMaster.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							newStandardMaster.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}
					}
					standardMasterList.add(newStandardMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return standardMasterList;
		}else{
			List<StandardMaster> standardMasterList = null;
			return standardMasterList;
		}
		
	}

	@Override
	public void deleteStandardMaster(StandardMaster standardMaster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StandardMaster findDetailsByMobileNo(StandardMaster standardMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StandardMaster findDetailsByEmail(StandardMaster standardMaster) {
		StandardMaster newStandardMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StandardMaster> criteriaQuery = criteriaBuilder.createQuery(StandardMaster.class);
		Root<StandardMaster> standardMasterRoot = criteriaQuery.from(StandardMaster.class);
		criteriaQuery.select(standardMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(standardMasterRoot.get("standardname"), standardMaster.getStandardname());
		Predicate predicateStatus = criteriaBuilder.equal(standardMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(standardMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<StandardMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<StandardMaster> standardMasterList = typedQuery.getResultList();
		if(standardMasterList.size() != 0){
			newStandardMaster = (StandardMaster)standardMasterList.get(0);
		}
		return newStandardMaster;
	}
	
}
