package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.MechanicalMaintenance;
import com.boenci.srmb.repository.MechanicalMaintenanceRepository;
import com.boenci.srmb.service.MechanicalMaintenanceService;
import com.boenci.srmb.utility.AppConstants;

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
public class MechanicalMaintenanceServiceImpl implements MechanicalMaintenanceService {

	@Autowired
	private MechanicalMaintenanceRepository mechanicalMaintenanceRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MechanicalMaintenance findById(long id) {
		MechanicalMaintenance newMechanicalMaintenance = null;
		newMechanicalMaintenance =mechanicalMaintenanceRepository.findById(id).get();		
		return newMechanicalMaintenance;
	}

	@Override
	public MechanicalMaintenance validMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance) {
		MechanicalMaintenance newMechanicalMaintenance = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MechanicalMaintenance> criteriaQuery = criteriaBuilder.createQuery(MechanicalMaintenance.class);
		Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaQuery.from(MechanicalMaintenance.class);
		criteriaQuery.select(mechanicalMaintenanceRoot);
		Predicate predicateUnitmasterid = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("equipmentmasterid"), mechanicalMaintenance.getEquipmentmasterid());
		Predicate predicateUnittype = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("subequipmentmasterid"), mechanicalMaintenance.getSubequipmentmasterid());
		Predicate predicateAreaname = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("standardmasterid"), mechanicalMaintenance.getStandardmasterid());
		Predicate predicateStatus = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateUnitmasterid,predicateUnittype,predicateAreaname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MechanicalMaintenance> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MechanicalMaintenance> mechanicalMaintenanceList = typedQuery.getResultList();
		if(mechanicalMaintenanceList.size() != 0){
			newMechanicalMaintenance = (MechanicalMaintenance)mechanicalMaintenanceList.get(0);
		}
		return newMechanicalMaintenance;
	}

	@Override
	public MechanicalMaintenance save(MechanicalMaintenance mechanicalMaintenance) {
		MechanicalMaintenance newMechanicalMaintenance = null;
		newMechanicalMaintenance = mechanicalMaintenanceRepository.save(mechanicalMaintenance);
		return newMechanicalMaintenance;
	}

	@Transactional
	@Override
	public MechanicalMaintenance update(MechanicalMaintenance mechanicalMaintenance) {
		long mechanicalMaintenanceId = mechanicalMaintenance.getMechanicalmaintenanceid();
		MechanicalMaintenance newMechanicalMaintenance = null;
		if(mechanicalMaintenanceRepository.existsById(mechanicalMaintenanceId)){
			newMechanicalMaintenance = mechanicalMaintenanceRepository.save(mechanicalMaintenance);
			return newMechanicalMaintenance;
		}else{
			return newMechanicalMaintenance;
		}
	}

	@Transactional
	@Override
	public MechanicalMaintenance delete(MechanicalMaintenance mechanicalMaintenance) {
		long mechanicalMaintenanceId = mechanicalMaintenance.getMechanicalmaintenanceid();
		MechanicalMaintenance newMechanicalMaintenance = null;
		if(mechanicalMaintenanceRepository.existsById(mechanicalMaintenanceId)){
			newMechanicalMaintenance = mechanicalMaintenanceRepository.save(mechanicalMaintenance);
			return newMechanicalMaintenance;
		}else{
			return newMechanicalMaintenance;
		}
	}

	@Transactional
	@Override
	public String updateMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance) {
		
		MechanicalMaintenance validateMechanicalMaintenance = validMechanicalMaintenance(mechanicalMaintenance);
		if(validateMechanicalMaintenance == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<MechanicalMaintenance> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(MechanicalMaintenance.class);
			Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaUpdate.from(MechanicalMaintenance.class);
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", mechanicalMaintenance.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(mechanicalMaintenanceRoot.get("mechanicalmaintenanceid"), mechanicalMaintenance.getMechanicalmaintenanceid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<MechanicalMaintenance> findAllMechanicalMaintenance() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MechanicalMaintenance> criteriaQuery = criteriaBuilder.createQuery(MechanicalMaintenance.class);
		Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaQuery.from(MechanicalMaintenance.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(mechanicalMaintenanceRoot.get("mechanicalmaintenanceid")));
		List<MechanicalMaintenance> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isMechanicalMaintenanceExist(MechanicalMaintenance mechanicalMaintenance) {
		
		return true;
	}

	@Override
	public List<MechanicalMaintenance> findMechanicalMaintenanceBySearchType(MechanicalMaintenance mechanicalMaintenance, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MechanicalMaintenance> criteriaQuery = criteriaBuilder.createQuery(MechanicalMaintenance.class);
		Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaQuery.from(MechanicalMaintenance.class);
		criteriaQuery.select(mechanicalMaintenanceRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "mechanicalmaintenanceid,remarks,llf,frequency,activity,"+
			"standardvalue,sparesrequired,cranerequired,shutdownrequired,unittype,MechanicalMaintenance";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+AppConstants.EQUIPMENT_QURY
			+","+AppConstants.SUBEQUIPMENT_QURY
			+","+AppConstants.STANDARDMASTER_QUERY+returnQuery;
			Query query = entityManager.createQuery(sqlQuery);
			//System.out.println("queryString============= "+ sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<MechanicalMaintenance> mechanicalMaintenanceList = new ArrayList<MechanicalMaintenance>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MechanicalMaintenance newMechanicalMaintenance = new MechanicalMaintenance();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMechanicalMaintenance.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMechanicalMaintenance.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMechanicalMaintenance.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMechanicalMaintenance.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMechanicalMaintenance.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMechanicalMaintenance.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEquipmentname(newObject.toString());
						}else if(objectNDX == 20) {
							newMechanicalMaintenance.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 22) {
							newMechanicalMaintenance.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardname(newObject.toString());
						}else if(objectNDX == 24) {
							newMechanicalMaintenance.setMechanicalmaintenanceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setRemarks(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setLlf(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setFrequency(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setActivity(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardvalue(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSparesrequired(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCranerequired(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setShutdownrequired(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnittype(newObject.toString());
						}
					}
					mechanicalMaintenanceList.add(newMechanicalMaintenance);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return mechanicalMaintenanceList;
		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("status"), mechanicalMaintenance.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<MechanicalMaintenance> typedQuery = entityManager.createQuery(criteriaQuery);
			List<MechanicalMaintenance> mechanicalMaintenanceList = typedQuery.getResultList();
			return mechanicalMaintenanceList;

		}else if(searchType.equalsIgnoreCase("equipmentmasterid")){
			String queryString = "mechanicalmaintenanceid,remarks,llf,frequency,activity,"+
			"standardvalue,sparesrequired,cranerequired,shutdownrequired,unittype,MechanicalMaintenance";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+AppConstants.EQUIPMENT_QURY
			+","+AppConstants.SUBEQUIPMENT_QURY
			+","+AppConstants.STANDARDMASTER_QUERY+returnQuery+" AND querytable.equipmentmasterid = ?3";
			Query query = entityManager.createQuery(sqlQuery);
			//System.out.println("queryString============= "+ sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,mechanicalMaintenance.getEquipmentmasterid());
			List<MechanicalMaintenance> mechanicalMaintenanceList = new ArrayList<MechanicalMaintenance>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MechanicalMaintenance newMechanicalMaintenance = new MechanicalMaintenance();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMechanicalMaintenance.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMechanicalMaintenance.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMechanicalMaintenance.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMechanicalMaintenance.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMechanicalMaintenance.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMechanicalMaintenance.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEquipmentname(newObject.toString());
						}else if(objectNDX == 20) {
							newMechanicalMaintenance.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 22) {
							newMechanicalMaintenance.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardname(newObject.toString());
						}else if(objectNDX == 24) {
							newMechanicalMaintenance.setMechanicalmaintenanceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setRemarks(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setLlf(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setFrequency(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setActivity(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardvalue(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSparesrequired(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCranerequired(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setShutdownrequired(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnittype(newObject.toString());
						}
					}
					mechanicalMaintenanceList.add(newMechanicalMaintenance);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return mechanicalMaintenanceList;
		}else if(searchType.equalsIgnoreCase("subequipmentmasterid")){
			String queryString = "mechanicalmaintenanceid,remarks,llf,frequency,activity,"+
			"standardvalue,sparesrequired,cranerequired,shutdownrequired,unittype,MechanicalMaintenance";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+AppConstants.EQUIPMENT_QURY
			+","+AppConstants.SUBEQUIPMENT_QURY
			+","+AppConstants.STANDARDMASTER_QUERY+returnQuery+" AND querytable.subequipmentmasterid = ?3";
			Query query = entityManager.createQuery(sqlQuery);
			//System.out.println("queryString============= "+ sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,mechanicalMaintenance.getSubequipmentmasterid());
			List<MechanicalMaintenance> mechanicalMaintenanceList = new ArrayList<MechanicalMaintenance>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MechanicalMaintenance newMechanicalMaintenance = new MechanicalMaintenance();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMechanicalMaintenance.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMechanicalMaintenance.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMechanicalMaintenance.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMechanicalMaintenance.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMechanicalMaintenance.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMechanicalMaintenance.setEquipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setEquipmentname(newObject.toString());
						}else if(objectNDX == 20) {
							newMechanicalMaintenance.setSubequipmentmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSubequipmentname(newObject.toString());
						}else if(objectNDX == 22) {
							newMechanicalMaintenance.setStandardmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardname(newObject.toString());
						}else if(objectNDX == 24) {
							newMechanicalMaintenance.setMechanicalmaintenanceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setRemarks(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
						newMechanicalMaintenance.setLlf(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setFrequency(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setActivity(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setStandardvalue(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setSparesrequired(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setCranerequired(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setShutdownrequired(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMechanicalMaintenance.setUnittype(newObject.toString());
						}
					}
					mechanicalMaintenanceList.add(newMechanicalMaintenance);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return mechanicalMaintenanceList;
		}else{
			
			List<MechanicalMaintenance> mechanicalMaintenanceList = null;
			return mechanicalMaintenanceList;
		}
		
	}
	@Transactional
	@Override
	public void deleteMechanicalMaintenance(MechanicalMaintenance mechanicalMaintenance) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<MechanicalMaintenance> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(MechanicalMaintenance.class);
		Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaUpdate.from(MechanicalMaintenance.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(mechanicalMaintenanceRoot.get("mechanicalmaintenanceid"), mechanicalMaintenance.getMechanicalmaintenanceid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public MechanicalMaintenance findDetailsByMobileNo(MechanicalMaintenance mechanicalMaintenance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MechanicalMaintenance findDetailsByEmail(MechanicalMaintenance mechanicalMaintenance) {
		MechanicalMaintenance newMechanicalMaintenance = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MechanicalMaintenance> criteriaQuery = criteriaBuilder.createQuery(MechanicalMaintenance.class);
		Root<MechanicalMaintenance> mechanicalMaintenanceRoot = criteriaQuery.from(MechanicalMaintenance.class);
		criteriaQuery.select(mechanicalMaintenanceRoot);
		Predicate predicateEmail = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("mechanicalmaintenanceid"), mechanicalMaintenance.getMechanicalmaintenanceid());
		Predicate predicateStatus = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(mechanicalMaintenanceRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MechanicalMaintenance> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MechanicalMaintenance> mechanicalMaintenanceList = typedQuery.getResultList();
		if(mechanicalMaintenanceList.size() != 0){
			newMechanicalMaintenance = (MechanicalMaintenance)mechanicalMaintenanceList.get(0);
		}
		return newMechanicalMaintenance;
	}

	
}
