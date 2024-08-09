package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.EquipmentMaster;
import com.boenci.srmb.repository.EquipmentMasterRepository;
import com.boenci.srmb.service.EquipmentMasterService;

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
public class EquipmentMasterServiceImpl implements EquipmentMasterService {

	@Autowired
	private EquipmentMasterRepository equipmentMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EquipmentMaster findById(long id) {
		EquipmentMaster newEquipmentMaster = null;
		newEquipmentMaster =equipmentMasterRepository.findById(id).get();		
		return newEquipmentMaster;
	}

	@Override
	public EquipmentMaster validEquipmentMaster(EquipmentMaster equipmentMaster) {
		EquipmentMaster newEquipmentMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(EquipmentMaster.class);
		Root<EquipmentMaster> equipmentMasterRoot = criteriaQuery.from(EquipmentMaster.class);
		criteriaQuery.select(equipmentMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(equipmentMasterRoot.get("equipmentname"), equipmentMaster.getEquipmentname());
		Predicate predicateAssetid = criteriaBuilder.equal(equipmentMasterRoot.get("assetid"), equipmentMaster.getAssetid());
		Predicate predicateStatus = criteriaBuilder.equal(equipmentMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(equipmentMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateAssetid,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EquipmentMaster> equipmentMasterList = typedQuery.getResultList();
		if(equipmentMasterList.size() != 0){
			newEquipmentMaster = (EquipmentMaster)equipmentMasterList.get(0);
		}
		return newEquipmentMaster;
	}

	@Override
	public EquipmentMaster save(EquipmentMaster equipmentMaster) {
		EquipmentMaster newEquipmentMaster = null;
		newEquipmentMaster = equipmentMasterRepository.save(equipmentMaster);
		return newEquipmentMaster;
	}

	@Transactional
	@Override
	public EquipmentMaster update(EquipmentMaster equipmentMaster) {
		long equipmentMasterId = equipmentMaster.getEquipmentmasterid();
		EquipmentMaster newEquipmentMaster = null;
		if(equipmentMasterRepository.existsById(equipmentMasterId)){
			newEquipmentMaster = equipmentMasterRepository.save(equipmentMaster);
			return newEquipmentMaster;
		}else{
			return newEquipmentMaster;
		}
	}

	@Transactional
	@Override
	public EquipmentMaster delete(EquipmentMaster equipmentMaster) {
		long equipmentMasterId = equipmentMaster.getEquipmentmasterid();
		EquipmentMaster newEquipmentMaster = null;
		if(equipmentMasterRepository.existsById(equipmentMasterId)){
			newEquipmentMaster = equipmentMasterRepository.save(equipmentMaster);
			return newEquipmentMaster;
		}else{
			return newEquipmentMaster;
		}
	}

	@Transactional
	@Override
	public String updateEquipmentMaster(EquipmentMaster equipmentMaster) {
		
		EquipmentMaster validateEquipmentMaster = validEquipmentMaster(equipmentMaster);
		if(validateEquipmentMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<EquipmentMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(EquipmentMaster.class);
			Root<EquipmentMaster> equipmentMasterRoot = criteriaUpdate.from(EquipmentMaster.class);
			criteriaUpdate.set("equipmentname", equipmentMaster.getEquipmentname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", equipmentMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(equipmentMasterRoot.get("equipmentmasterid"), equipmentMaster.getEquipmentmasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<EquipmentMaster> findAllEquipmentMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(EquipmentMaster.class);
		Root<EquipmentMaster> equipmentMasterRoot = criteriaQuery.from(EquipmentMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(equipmentMasterRoot.get("equipmentname")));
		List<EquipmentMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isEquipmentMasterExist(EquipmentMaster equipmentMaster) {
		
		return true;
	}

	@Override
	public List<EquipmentMaster> findEquipmentMasterBySearchType(EquipmentMaster equipmentMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(EquipmentMaster.class);
		Root<EquipmentMaster> equipmentMasterRoot = criteriaQuery.from(EquipmentMaster.class);
		criteriaQuery.select(equipmentMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT em.unitmasterid,em.enterprisemasterid,(select ename.enterprisename "+
			"from EnterpriseMaster ename where ename.enterprisemasterid = em.enterprisemasterid) enterprisename,"+
			" em.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = em.plantmasterid) "+
			" as plantname, em.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = em.sitemasterid) as sitename, em.remarks, (select am.areaname from "+
			" AreaMaster am WHERE  am.areamasterid = em.areamasterid) as areaname, em.unittype,"+
			" em.areamasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = em.unitmasterid) "+
			" as unitname, em.assetid, em.changeoverdate, em.changeoverfrequency, em.criticality,"+
			" em.equipmentname, em.purchasedate,  em.shutdownneeded, em.equipmentmasterid,em.frequency,em.installationdate,em.subequipmentrequirment FROM EquipmentMaster em"+
			" WHERE em.deletedflag = ?1 and em.status = ?2";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<EquipmentMaster> equipmentMasterList = new ArrayList<EquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					EquipmentMaster newEquipmentMaster = new EquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAssetid(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setChangeoverdate(newObject.toString());
						}else if(objectNDX == 14) {
							newEquipmentMaster.setChangeoverfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setCriticality(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setShutdownneeded(newObject.toString());
						}else if(objectNDX == 19) {
							newEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setFrequency(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setInstallationdate(newObject.toString());
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSubequipmentrequirment(newObject.toString());
						}
					}
					equipmentMasterList.add(newEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return equipmentMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(equipmentMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(equipmentMasterRoot.get("status"), equipmentMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<EquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<EquipmentMaster> equipmentMasterList = typedQuery.getResultList();
			return equipmentMasterList;
			
		}else if(searchType.equalsIgnoreCase("areamasterid")){
			String strSql = "SELECT em.unitmasterid,em.enterprisemasterid,(select ename.enterprisename "+
			"from EnterpriseMaster ename where ename.enterprisemasterid = em.enterprisemasterid) enterprisename,"+
			" em.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = em.plantmasterid) "+
			" as plantname, em.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = em.sitemasterid) as sitename, em.remarks, (select am.areaname from "+
			" AreaMaster am WHERE  am.areamasterid = em.areamasterid) as areaname, em.unittype,"+
			" em.areamasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = em.unitmasterid) "+
			" as unitname, em.assetid, em.changeoverdate, em.changeoverfrequency, em.criticality,"+
			" em.equipmentname, em.purchasedate,  em.shutdownneeded, em.equipmentmasterid,em.frequency,em.installationdate,em.subequipmentrequirment FROM EquipmentMaster em"+
			" WHERE em.deletedflag = ?1 and em.status = ?2 and em.areamasterid = ?3";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,equipmentMaster.getAreamasterid());
			List<EquipmentMaster> equipmentMasterList = new ArrayList<EquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					EquipmentMaster newEquipmentMaster = new EquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAssetid(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setChangeoverdate(newObject.toString());
						}else if(objectNDX == 14) {
							newEquipmentMaster.setChangeoverfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setCriticality(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setShutdownneeded(newObject.toString());
						}else if(objectNDX == 19) {
							newEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setFrequency(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setInstallationdate(newObject.toString());
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSubequipmentrequirment(newObject.toString());
						}
					}
					equipmentMasterList.add(newEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return equipmentMasterList;
		}else if(searchType.equalsIgnoreCase("unitmasterid")){
			String strSql = "SELECT em.unitmasterid,em.enterprisemasterid,(select ename.enterprisename "+
			"from EnterpriseMaster ename where ename.enterprisemasterid = em.enterprisemasterid) enterprisename,"+
			" em.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = em.plantmasterid) "+
			" as plantname, em.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = em.sitemasterid) as sitename, em.remarks, (select am.areaname from "+
			" AreaMaster am WHERE  am.areamasterid = em.areamasterid) as areaname, em.unittype,"+
			" em.areamasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = em.unitmasterid) "+
			" as unitname, em.assetid, em.changeoverdate, em.changeoverfrequency, em.criticality,"+
			" em.equipmentname, em.purchasedate,  em.shutdownneeded, em.equipmentmasterid,em.frequency,em.installationdate,em.subequipmentrequirment FROM EquipmentMaster em"+
			" WHERE em.deletedflag = ?1 and em.status = ?2 and em.unitmasterid = ?3";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,equipmentMaster.getUnitmasterid());
			List<EquipmentMaster> equipmentMasterList = new ArrayList<EquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					EquipmentMaster newEquipmentMaster = new EquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAssetid(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setChangeoverdate(newObject.toString());
						}else if(objectNDX == 14) {
							newEquipmentMaster.setChangeoverfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setCriticality(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setShutdownneeded(newObject.toString());
						}else if(objectNDX == 19) {
							newEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setFrequency(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setInstallationdate(newObject.toString());
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSubequipmentrequirment(newObject.toString());
						}
					}
					equipmentMasterList.add(newEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return equipmentMasterList;
		}else if(searchType.equalsIgnoreCase("frequency")){
			String strSql = "SELECT em.unitmasterid,em.enterprisemasterid,(select ename.enterprisename "+
			"from EnterpriseMaster ename where ename.enterprisemasterid = em.enterprisemasterid) enterprisename,"+
			" em.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = em.plantmasterid) "+
			" as plantname, em.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = em.sitemasterid) as sitename, em.remarks, (select am.areaname from "+
			" AreaMaster am WHERE  am.areamasterid = em.areamasterid) as areaname, em.unittype,"+
			" em.areamasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = em.unitmasterid) "+
			" as unitname, em.assetid, em.changeoverdate, em.changeoverfrequency, em.criticality,"+
			" em.equipmentname, em.purchasedate,  em.shutdownneeded, em.equipmentmasterid,em.frequency,em.installationdate,em.subequipmentrequirment FROM EquipmentMaster em"+
			" WHERE em.deletedflag = ?1 and em.status = ?2 and em.frequency = ?3";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,equipmentMaster.getFrequency());
			List<EquipmentMaster> equipmentMasterList = new ArrayList<EquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					EquipmentMaster newEquipmentMaster = new EquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setAssetid(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setChangeoverdate(newObject.toString());
						}else if(objectNDX == 14) {
							newEquipmentMaster.setChangeoverfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setCriticality(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
						newEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setShutdownneeded(newObject.toString());
						}else if(objectNDX == 19) {
							newEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setFrequency(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setInstallationdate(newObject.toString());
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newEquipmentMaster.setSubequipmentrequirment(newObject.toString());
						}
					}
					equipmentMasterList.add(newEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return equipmentMasterList;
		}else{
			List<EquipmentMaster> equipmentMasterList = null;
			return equipmentMasterList;
		}
		
	}

	@Override
	public void deleteEquipmentMaster(EquipmentMaster equipmentMaster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EquipmentMaster findDetailsByMobileNo(EquipmentMaster equipmentMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentMaster findDetailsByEmail(EquipmentMaster equipmentMaster) {
		EquipmentMaster newEquipmentMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(EquipmentMaster.class);
		Root<EquipmentMaster> equipmentMasterRoot = criteriaQuery.from(EquipmentMaster.class);
		criteriaQuery.select(equipmentMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(equipmentMasterRoot.get("equipment"), equipmentMaster.getEquipmentname());
		Predicate predicateStatus = criteriaBuilder.equal(equipmentMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(equipmentMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EquipmentMaster> equipmentMasterList = typedQuery.getResultList();
		if(equipmentMasterList.size() != 0){
			newEquipmentMaster = (EquipmentMaster)equipmentMasterList.get(0);
		}
		return newEquipmentMaster;
	}
	
}
