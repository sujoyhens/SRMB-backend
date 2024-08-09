package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.LaboratoryLog;
import com.boenci.srmb.repository.production.LaboratoryLogRepository;
import com.boenci.srmb.service.production.LaboratoryLogService;
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
public class LaboratoryLogServiceImpl implements LaboratoryLogService {

	@Autowired
	private LaboratoryLogRepository laboratoryLogRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public LaboratoryLog findById(long id) {
		LaboratoryLog newLaboratoryLog = null;
		newLaboratoryLog =laboratoryLogRepository.findById(id).get();		
		return newLaboratoryLog;
	}

	@Override
	public LaboratoryLog validLaboratoryLog(LaboratoryLog laboratoryLog) {
		LaboratoryLog newLaboratoryLog = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratoryLog> criteriaQuery = criteriaBuilder.createQuery(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaQuery.from(LaboratoryLog.class);
		criteriaQuery.select(laboratoryLogRoot);
		Predicate predicateEmail = criteriaBuilder.equal(laboratoryLogRoot.get("productionid"), laboratoryLog.getProductionid());
		Predicate predicateStatus = criteriaBuilder.equal(laboratoryLogRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(laboratoryLogRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<LaboratoryLog> typedQuery = entityManager.createQuery(criteriaQuery);
		List<LaboratoryLog> laboratoryLogList = typedQuery.getResultList();
		if(laboratoryLogList.size() != 0){
			newLaboratoryLog = (LaboratoryLog)laboratoryLogList.get(0);
		}
		return newLaboratoryLog;
	}

	@Override
	public LaboratoryLog save(LaboratoryLog laboratoryLog) {
		LaboratoryLog newLaboratoryLog = null;
		newLaboratoryLog = laboratoryLogRepository.save(laboratoryLog);
		return newLaboratoryLog;
	}

	@Transactional
	@Override
	public LaboratoryLog update(LaboratoryLog laboratoryLog) {
		long laboratoryLogId = laboratoryLog.getLaboratorylogid();
		LaboratoryLog newLaboratoryLog = null;
		LaboratoryLog newLaboratoryLogforSearch = laboratoryLogRepository.findById(laboratoryLogId).get();
		if(newLaboratoryLogforSearch != null){
			laboratoryLog.setProductionid(newLaboratoryLogforSearch.getProductionid());
			laboratoryLog.setIsproductionlogupdated(newLaboratoryLogforSearch.getIsproductionlogupdated());
			laboratoryLog.setIsmillproductionupdated(newLaboratoryLogforSearch.getIsmillproductionupdated());
			newLaboratoryLog = laboratoryLogRepository.save(laboratoryLog);
			return newLaboratoryLog;
		}else{
			return newLaboratoryLog;
		}
		
	}

	@Transactional
	@Override
	public String updateLaboratoryLog(LaboratoryLog laboratoryLog) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<LaboratoryLog> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaUpdate.from(LaboratoryLog.class);
		criteriaUpdate.set("isproductionlogupdated", laboratoryLog.getIsproductionlogupdated());
		criteriaUpdate.set("ismillproductionupdated", laboratoryLog.getIsmillproductionupdated());
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("action", "update");
		criteriaUpdate.where(criteriaBuilder.equal(laboratoryLogRoot.get("productionid"), laboratoryLog.getProductionid()));
		int count = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		if(count > 0){
			return "success";
		}else{
			return "failure";
		}		
	}

	@Transactional
	@Override
	public List<LaboratoryLog> findAllLaboratoryLog() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratoryLog> criteriaQuery = criteriaBuilder.createQuery(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaQuery.from(LaboratoryLog.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(laboratoryLogRoot.get("unitname")));
		List<LaboratoryLog> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isLaboratoryLogExist(LaboratoryLog laboratoryLog) {
		
		return true;
	}

	@Override
	public List<LaboratoryLog> findLaboratoryLogBySearchType(LaboratoryLog laboratoryLog, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratoryLog> criteriaQuery = criteriaBuilder.createQuery(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaQuery.from(LaboratoryLog.class);
		criteriaQuery.select(laboratoryLogRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "laboratorylogid, carbon,chromium, manganese,"+
			" phosphorus, productionid,laboratorylogtime,"+
			"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");

			List<LaboratoryLog> laboratoryLogList = new ArrayList<LaboratoryLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratoryLog newLaboratoryLog = new LaboratoryLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratoryLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratoryLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratoryLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratoryLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratoryLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratoryLog.setLaboratorylogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratoryLog.setCarbon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratoryLog.setChromium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratoryLog.setManganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratoryLog.setPhosphorus(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setProductionid(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogtime(newObject.toString());
						}else if(objectNDX == 25) {
							newLaboratoryLog.setChargemixnickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratoryLog.setChargemixsilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogdate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSmsname(newObject.toString());
						} else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCcmname(newObject.toString());
						} else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setRmname(newObject.toString());
						}
					}
					laboratoryLogList.add(newLaboratoryLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratoryLogList;

		}else if(searchType.equalsIgnoreCase("status")){
			List<LaboratoryLog> laboratoryLogList = null;
			return laboratoryLogList;
			
		}else if(searchType.equalsIgnoreCase("productionid")){
			String queryString = "laboratorylogid, carbon,chromium, manganese,"+
			" phosphorus, productionid,laboratorylogtime,"+
			"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+
			" AND querytable.productionid = ?3";
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,laboratoryLog.getProductionid());
			List<LaboratoryLog> laboratoryLogList = new ArrayList<LaboratoryLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratoryLog newLaboratoryLog = new LaboratoryLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratoryLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratoryLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratoryLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratoryLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratoryLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratoryLog.setLaboratorylogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratoryLog.setCarbon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratoryLog.setChromium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratoryLog.setManganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratoryLog.setPhosphorus(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setProductionid(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogtime(newObject.toString());
						}else if(objectNDX == 25) {
							newLaboratoryLog.setChargemixnickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratoryLog.setChargemixsilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogdate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSmsname(newObject.toString());
						} else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCcmname(newObject.toString());
						} else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setRmname(newObject.toString());
						}
					}
					laboratoryLogList.add(newLaboratoryLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratoryLogList;

		}else if(searchType.equalsIgnoreCase("laboratorylogdate")){
			String queryString = "laboratorylogid, carbon,chromium, manganese,"+
			" phosphorus, productionid,laboratorylogtime,"+
			"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+
			" AND querytable.laboratorylogdate = ?3";
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,laboratoryLog.getLaboratorylogdate());
			List<LaboratoryLog> laboratoryLogList = new ArrayList<LaboratoryLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratoryLog newLaboratoryLog = new LaboratoryLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratoryLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratoryLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratoryLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratoryLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratoryLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratoryLog.setLaboratorylogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratoryLog.setCarbon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratoryLog.setChromium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratoryLog.setManganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratoryLog.setPhosphorus(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setProductionid(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogtime(newObject.toString());
						}else if(objectNDX == 25) {
							newLaboratoryLog.setChargemixnickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratoryLog.setChargemixsilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogdate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSmsname(newObject.toString());
						} else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCcmname(newObject.toString());
						} else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setRmname(newObject.toString());
						}
					}
					laboratoryLogList.add(newLaboratoryLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratoryLogList;

		}else if(searchType.equalsIgnoreCase("productionlog")){
			long unitid = 0;
			if(laboratoryLog.getUnitmasterid() == 5){
				unitid = 3;
			}else if(laboratoryLog.getUnitmasterid() == 6){
				unitid = 4;
			}
			String queryString = "laboratorylogid, carbon,chromium, manganese,"+
			" phosphorus, productionid,laboratorylogtime,"+
			"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+
			" AND querytable.isproductionlogupdated = ?3 "+
			" AND querytable.ismillproductionupdated = ?4 AND querytable.unitmasterid = ?5 order by querytable.laboratorylogid ASC LIMIT 1";
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"no");
			query.setParameter(5,unitid);
			List<LaboratoryLog> laboratoryLogList = new ArrayList<LaboratoryLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratoryLog newLaboratoryLog = new LaboratoryLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratoryLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratoryLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratoryLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratoryLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratoryLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratoryLog.setLaboratorylogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratoryLog.setCarbon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratoryLog.setChromium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratoryLog.setManganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratoryLog.setPhosphorus(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setProductionid(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogtime(newObject.toString());
						}else if(objectNDX == 25) {
							newLaboratoryLog.setChargemixnickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratoryLog.setChargemixsilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogdate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSmsname(newObject.toString());
						} else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCcmname(newObject.toString());
						} else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setRmname(newObject.toString());
						}
					}
					laboratoryLogList.add(newLaboratoryLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratoryLogList;

		}else if(searchType.equalsIgnoreCase("millproduction")){
			Query query = null;
			if(laboratoryLog.getUnitmasterid() == 7){

				String queryString = "laboratorylogid, carbon,chromium, manganese,"+
				" phosphorus, productionid,laboratorylogtime,"+
				"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+
				" AND querytable.isproductionlogupdated = ?3 "+
				" AND querytable.ismillproductionupdated = ?4 AND querytable.unitmasterid in (3) order by querytable.laboratorylogid ASC LIMIT 1";
				//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"yes");
				query.setParameter(4,"no");

			} else if(laboratoryLog.getUnitmasterid() == 8){
				String queryString = "laboratorylogid, carbon,chromium, manganese,"+
				" phosphorus, productionid,laboratorylogtime,"+
				"chargemixnickel,chargemixsilicon,laboratorylogdate,smsname,laboratoryname,ccmname,rmname,LaboratoryLog";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+
				" AND querytable.isproductionlogupdated = ?3 "+
				" AND querytable.ismillproductionupdated = ?4 AND querytable.unitmasterid in (3,4) order by querytable.laboratorylogid ASC LIMIT 1";
				//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"yes");
				query.setParameter(4,"no");
			}
			
			List<LaboratoryLog> laboratoryLogList = new ArrayList<LaboratoryLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratoryLog newLaboratoryLog = new LaboratoryLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratoryLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratoryLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratoryLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratoryLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratoryLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratoryLog.setLaboratorylogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratoryLog.setCarbon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratoryLog.setChromium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratoryLog.setManganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratoryLog.setPhosphorus(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setProductionid(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogtime(newObject.toString());
						}else if(objectNDX == 25) {
							newLaboratoryLog.setChargemixnickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratoryLog.setChargemixsilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratorylogdate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setSmsname(newObject.toString());
						} else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setCcmname(newObject.toString());
						} else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratoryLog.setRmname(newObject.toString());
						}
					}
					laboratoryLogList.add(newLaboratoryLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratoryLogList;

		}else{
			List<LaboratoryLog> laboratoryLogList = null;
			return laboratoryLogList;
		}
		
	}

	@Override
	public void deleteLaboratoryLog(LaboratoryLog laboratoryLog) {
		LaboratoryLog newLaboratoryLog = findById(laboratoryLog.getLaboratorylogid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<LaboratoryLog> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaUpdate.from(LaboratoryLog.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(laboratoryLogRoot.get("laboratorylogid"), newLaboratoryLog.getLaboratorylogid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public LaboratoryLog findDetailsByMobileNo(LaboratoryLog laboratoryLog) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LaboratoryLog findDetailsByEmail(LaboratoryLog laboratoryLog) {
		LaboratoryLog newLaboratoryLog = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratoryLog> criteriaQuery = criteriaBuilder.createQuery(LaboratoryLog.class);
		Root<LaboratoryLog> laboratoryLogRoot = criteriaQuery.from(LaboratoryLog.class);
		criteriaQuery.select(laboratoryLogRoot);
		Predicate predicateEmail = criteriaBuilder.equal(laboratoryLogRoot.get("productionid"), laboratoryLog.getProductionid());
		Predicate predicateStatus = criteriaBuilder.equal(laboratoryLogRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(laboratoryLogRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<LaboratoryLog> typedQuery = entityManager.createQuery(criteriaQuery);
		List<LaboratoryLog> laboratoryLogList = typedQuery.getResultList();
		if(laboratoryLogList.size() != 0){
			newLaboratoryLog = (LaboratoryLog)laboratoryLogList.get(0);
		}
		return newLaboratoryLog;
	}
	
}
