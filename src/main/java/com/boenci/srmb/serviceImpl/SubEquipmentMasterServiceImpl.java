package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SubEquipmentMaster;
import com.boenci.srmb.repository.SubEquipmentMasterRepository;
import com.boenci.srmb.service.SubEquipmentMasterService;

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
public class SubEquipmentMasterServiceImpl implements SubEquipmentMasterService {

	@Autowired
	private SubEquipmentMasterRepository subEquipmentMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SubEquipmentMaster findById(long id) {
		SubEquipmentMaster newSubEquipmentMaster = null;
		newSubEquipmentMaster =subEquipmentMasterRepository.findById(id).get();		
		return newSubEquipmentMaster;
	}

	@Override
	public SubEquipmentMaster validSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster) {
		SubEquipmentMaster newSubEquipmentMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubEquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(SubEquipmentMaster.class);
		Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaQuery.from(SubEquipmentMaster.class);
		criteriaQuery.select(subEquipmentMasterRoot);
		
		Predicate predicateName = criteriaBuilder.equal(subEquipmentMasterRoot.get("subequipmentname"), subEquipmentMaster.getSubequipmentname());
		Predicate predicateEmail = criteriaBuilder.equal(subEquipmentMasterRoot.get("equipmentmasterid"), subEquipmentMaster.getEquipmentmasterid());
		Predicate predicateStatus = criteriaBuilder.equal(subEquipmentMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(subEquipmentMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateName,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SubEquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SubEquipmentMaster> subEquipmentMasterList = typedQuery.getResultList();
		if(subEquipmentMasterList.size() != 0){
			newSubEquipmentMaster = (SubEquipmentMaster)subEquipmentMasterList.get(0);
		}
		return newSubEquipmentMaster;
	}

	@Override
	public SubEquipmentMaster save(SubEquipmentMaster subEquipmentMaster) {
		SubEquipmentMaster newSubEquipmentMaster = null;
		newSubEquipmentMaster = subEquipmentMasterRepository.save(subEquipmentMaster);
		return newSubEquipmentMaster;
	}

	@Transactional
	@Override
	public SubEquipmentMaster update(SubEquipmentMaster subEquipmentMaster) {
		long subEquipmentMasterId = subEquipmentMaster.getSubequipmentmasterid();
		SubEquipmentMaster newSubEquipmentMaster = null;
		if(subEquipmentMasterRepository.existsById(subEquipmentMasterId)){
			newSubEquipmentMaster = subEquipmentMasterRepository.save(subEquipmentMaster);
			return newSubEquipmentMaster;
		}else{
			return newSubEquipmentMaster;
		}
	}

	@Transactional
	@Override
	public SubEquipmentMaster delete(SubEquipmentMaster subEquipmentMaster) {
		long subEquipmentMasterId = subEquipmentMaster.getSubequipmentmasterid();
		SubEquipmentMaster newSubEquipmentMaster = null;
		if(subEquipmentMasterRepository.existsById(subEquipmentMasterId)){
			newSubEquipmentMaster = subEquipmentMasterRepository.save(subEquipmentMaster);
			return newSubEquipmentMaster;
		}else{
			return newSubEquipmentMaster;
		}
	}

	@Transactional
	@Override
	public String updateSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster) {
		
		SubEquipmentMaster validateSubEquipmentMaster = validSubEquipmentMaster(subEquipmentMaster);
		if(validateSubEquipmentMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<SubEquipmentMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(SubEquipmentMaster.class);
			Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaUpdate.from(SubEquipmentMaster.class);
			criteriaUpdate.set("subequipmentname", subEquipmentMaster.getSubequipmentname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", subEquipmentMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(subEquipmentMasterRoot.get("subequipmentmasterid"), subEquipmentMaster.getSubequipmentmasterid()));
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
	public List<SubEquipmentMaster> findAllSubEquipmentMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubEquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(SubEquipmentMaster.class);
		Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaQuery.from(SubEquipmentMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(subEquipmentMasterRoot.get("subequipmentname")));
		List<SubEquipmentMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isSubEquipmentMasterExist(SubEquipmentMaster subEquipmentMaster) {
		
		return true;
	}

	@Override
	public List<SubEquipmentMaster> findSubEquipmentMasterBySearchType(SubEquipmentMaster subEquipmentMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubEquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(SubEquipmentMaster.class);
		Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaQuery.from(SubEquipmentMaster.class);
		criteriaQuery.select(subEquipmentMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename "+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid)  enterprisename, "+
			" om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = om.plantmasterid)"
			+" as plantname, om.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = om.sitemasterid) as sitename, om.remarks, (select am.areaname from AreaMaster am WHERE "+
			" am.areamasterid = om.areamasterid) as areaname, om.unittype, om.areamasterid,(select um.unitname "+
			" from UnitMaster um WHERE um.unitmasterid = om.unitmasterid) as unitname,om.conditionbasedfrequency,"+
			" om.criticalstatus, om.detection, om.equipmentmasterid,(select em.equipmentname from EquipmentMaster em "+
			" WHERE em.equipmentmasterid = om.equipmentmasterid) as equipmentname, om.occurrence, om.purchasedate,"+
			" om.riskprioritynumber, om.severity, om.subequipmentname, om.timebasedfrequency,om.subequipmentmasterid"+
			" FROM SubEquipmentMaster om where om.deletedflag = ?1 and om.status = ?2 "+
			" ORDER BY om.subequipmentname ASC";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<SubEquipmentMaster> subEquipmentMasterList = new ArrayList<SubEquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SubEquipmentMaster newSubEquipmentMaster = new SubEquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newSubEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newSubEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newSubEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newSubEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newSubEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							newSubEquipmentMaster.setConditionbasedfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setCriticalstatus(newObject.toString());
						}else if(objectNDX == 14) {
							newSubEquipmentMaster.setDetection(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 15) {
							newSubEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							newSubEquipmentMaster.setOccurrence(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 19) {
							newSubEquipmentMaster.setRiskprioritynumber(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newSubEquipmentMaster.setSeverity(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 22) {
							newSubEquipmentMaster.setTimebasedfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newSubEquipmentMaster.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}
					}
					subEquipmentMasterList.add(newSubEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return subEquipmentMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(subEquipmentMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(subEquipmentMasterRoot.get("status"), subEquipmentMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			

		}else if(searchType.equalsIgnoreCase("equipmentmasterid")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename "+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid)  enterprisename, "+
			" om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = om.plantmasterid)"
			+" as plantname, om.sitemasterid, (select sm.sitename from SiteMaster sm where "+
			" sm.sitemasterid = om.sitemasterid) as sitename, om.remarks, (select am.areaname from AreaMaster am WHERE "+
			" am.areamasterid = om.areamasterid) as areaname, om.unittype, om.areamasterid,(select um.unitname "+
			" from UnitMaster um WHERE um.unitmasterid = om.unitmasterid) as unitname,om.conditionbasedfrequency,"+
			" om.criticalstatus, om.detection, om.equipmentmasterid,(select em.equipmentname from EquipmentMaster em "+
			" WHERE em.equipmentmasterid = om.equipmentmasterid) as equipmentname, om.occurrence, om.purchasedate,"+
			" om.riskprioritynumber, om.severity, om.subequipmentname, om.timebasedfrequency,om.subequipmentmasterid"+
			" FROM SubEquipmentMaster om where om.deletedflag = ?1 and om.status = ?2 and om.equipmentmasterid = ?3"+
			" ORDER BY om.subequipmentname ASC";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3, subEquipmentMaster.getEquipmentmasterid());
			List<SubEquipmentMaster> subEquipmentMasterList = new ArrayList<SubEquipmentMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SubEquipmentMaster newSubEquipmentMaster = new SubEquipmentMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newSubEquipmentMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newSubEquipmentMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newSubEquipmentMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newSubEquipmentMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newSubEquipmentMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							newSubEquipmentMaster.setConditionbasedfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setCriticalstatus(newObject.toString());
						}else if(objectNDX == 14) {
							newSubEquipmentMaster.setDetection(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 15) {
							newSubEquipmentMaster.setEquipmentmasterid(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setEquipmentname(newObject.toString());
						}else if(objectNDX == 17) {
							newSubEquipmentMaster.setOccurrence(Long.valueOf(newObject.toString()));
							
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setPurchasedate(newObject.toString());
						}else if(objectNDX == 19) {
							newSubEquipmentMaster.setRiskprioritynumber(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newSubEquipmentMaster.setSeverity(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newSubEquipmentMaster.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 22) {
							newSubEquipmentMaster.setTimebasedfrequency(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newSubEquipmentMaster.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}
					}
					subEquipmentMasterList.add(newSubEquipmentMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return subEquipmentMasterList;
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<SubEquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SubEquipmentMaster> subEquipmentMasterList = typedQuery.getResultList();
		return subEquipmentMasterList;
	}

	@Override
	public String deleteSubEquipmentMaster(SubEquipmentMaster subEquipmentMaster) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<SubEquipmentMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(SubEquipmentMaster.class);
		Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaUpdate.from(SubEquipmentMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(subEquipmentMasterRoot.get("subequipmentmasterid"), subEquipmentMaster.getSubequipmentmasterid()));
		int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		if(rowCount > 0){
			return "Success";
		}else{
			return "Failure";
		}
	}

	@Override
	public SubEquipmentMaster findDetailsByMobileNo(SubEquipmentMaster subEquipmentMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubEquipmentMaster findDetailsByEmail(SubEquipmentMaster subEquipmentMaster) {
		SubEquipmentMaster newSubEquipmentMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubEquipmentMaster> criteriaQuery = criteriaBuilder.createQuery(SubEquipmentMaster.class);
		Root<SubEquipmentMaster> subEquipmentMasterRoot = criteriaQuery.from(SubEquipmentMaster.class);
		criteriaQuery.select(subEquipmentMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(subEquipmentMasterRoot.get("subequipment"), subEquipmentMaster.getSubequipmentname());
		Predicate predicateStatus = criteriaBuilder.equal(subEquipmentMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(subEquipmentMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SubEquipmentMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SubEquipmentMaster> subEquipmentMasterList = typedQuery.getResultList();
		if(subEquipmentMasterList.size() != 0){
			newSubEquipmentMaster = (SubEquipmentMaster)subEquipmentMasterList.get(0);
		}
		return newSubEquipmentMaster;
	}
	
}
