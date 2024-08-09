package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.CcmSummary;
import com.boenci.srmb.repository.production.CcmSummaryRepository;
import com.boenci.srmb.service.production.CcmSummaryService;
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
public class CcmSummaryServiceImpl implements CcmSummaryService {

	@Autowired
	private CcmSummaryRepository ccmSummaryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CcmSummary findById(long id) {
		CcmSummary newCcmSummary = null;
		newCcmSummary =ccmSummaryRepository.findById(id).get();		
		return newCcmSummary;
	}

	@Override
	public CcmSummary validCcmSummary(CcmSummary ccmSummary) {
		CcmSummary newCcmSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmSummary> criteriaQuery = criteriaBuilder.createQuery(CcmSummary.class);
		Root<CcmSummary> ccmSummaryRoot = criteriaQuery.from(CcmSummary.class);
		criteriaQuery.select(ccmSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(ccmSummaryRoot.get("productiondate"), ccmSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(ccmSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(ccmSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<CcmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<CcmSummary> ccmSummaryList = typedQuery.getResultList();
		if(ccmSummaryList.size() != 0){
			newCcmSummary = (CcmSummary)ccmSummaryList.get(0);
		}
		return newCcmSummary;
	}

	@Override
	public CcmSummary save(CcmSummary ccmSummary) {
		CcmSummary newCcmSummary = null;
		newCcmSummary = ccmSummaryRepository.save(ccmSummary);
		return newCcmSummary;
	}

	@Transactional
	@Override
	public CcmSummary update(CcmSummary ccmSummary) {
		long ccmSummaryId = ccmSummary.getCcmsummaryid();
		CcmSummary newCcmSummary = null;
		if(ccmSummaryRepository.existsById(ccmSummaryId)){
			newCcmSummary = ccmSummaryRepository.save(ccmSummary);
			return newCcmSummary;
		}else{
			return newCcmSummary;
		}
		
	}

	@Transactional
	@Override
	public String updateCcmSummary(CcmSummary ccmSummary) {
		
		CcmSummary validateCcmSummary = validCcmSummary(ccmSummary);
		if(validateCcmSummary == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<CcmSummary> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(CcmSummary.class);
			Root<CcmSummary> ccmSummaryRoot = criteriaUpdate.from(CcmSummary.class);
			criteriaUpdate.set("productiondate", ccmSummary.getProductiondate());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", ccmSummary.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(ccmSummaryRoot.get("ccmsummaryid"), ccmSummary.getCcmsummaryid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<CcmSummary> findAllCcmSummary() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmSummary> criteriaQuery = criteriaBuilder.createQuery(CcmSummary.class);
		Root<CcmSummary> ccmSummaryRoot = criteriaQuery.from(CcmSummary.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(ccmSummaryRoot.get("unitname")));
		List<CcmSummary> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isCcmSummaryExist(CcmSummary ccmSummary) {
		
		return true;
	}

	@Override
	public List<CcmSummary> findCcmSummaryBySearchType(CcmSummary ccmSummary, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmSummary> criteriaQuery = criteriaBuilder.createQuery(CcmSummary.class);
		Root<CcmSummary> ccmSummaryRoot = criteriaQuery.from(CcmSummary.class);
		criteriaQuery.select(ccmSummaryRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "ccmsummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutccm, hotoutrm, hotoutpowercut, mechanicaldowntime,"+
			" rmsequencebreak, noofpowersequencebreak, "+
			" productiondate, remarks, ccmproduction, "+
			" unittype,CcmSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<CcmSummary> ccmSummaryList = new ArrayList<CcmSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					CcmSummary newCcmSummary = new CcmSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newCcmSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newCcmSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newCcmSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newCcmSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newCcmSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newCcmSummary.setCcmsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newCcmSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newCcmSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newCcmSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newCcmSummary.setHotoutccm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newCcmSummary.setHotoutrm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newCcmSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newCcmSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newCcmSummary.setRmsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 29) {
							newCcmSummary.setCcmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUnittype(newObject.toString());
						}
					}
					ccmSummaryList.add(newCcmSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return ccmSummaryList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(ccmSummaryRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(ccmSummaryRoot.get("status"), ccmSummary.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<CcmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
			List<CcmSummary> ccmSummaryList = typedQuery.getResultList();
			return ccmSummaryList;
			
		}else if(searchType.equalsIgnoreCase("productiondate")){
			String queryString = "ccmsummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutccm, hotoutrm, hotoutpowercut, mechanicaldowntime,"+
			" rmsequencebreak, noofpowersequencebreak, "+
			" productiondate, remarks, ccmproduction, "+
			" unittype,CcmSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.productiondate = ?3";
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,ccmSummary.getProductiondate());
			List<CcmSummary> ccmSummaryList = new ArrayList<CcmSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					CcmSummary newCcmSummary = new CcmSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newCcmSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newCcmSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newCcmSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newCcmSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newCcmSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newCcmSummary.setCcmsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newCcmSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newCcmSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newCcmSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newCcmSummary.setHotoutccm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newCcmSummary.setHotoutrm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newCcmSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newCcmSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newCcmSummary.setRmsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 29) {
							newCcmSummary.setCcmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newCcmSummary.setUnittype(newObject.toString());
						}
					}
					ccmSummaryList.add(newCcmSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return ccmSummaryList;
		}else{
			List<CcmSummary> ccmSummaryList = null;
			return ccmSummaryList;
		}
		
	}

	@Override
	public void deleteCcmSummary(CcmSummary ccmSummary) {
		CcmSummary newCcmSummary = findById(ccmSummary.getCcmsummaryid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<CcmSummary> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(CcmSummary.class);
		Root<CcmSummary> ccmSummaryRoot = criteriaUpdate.from(CcmSummary.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(ccmSummaryRoot.get("ccmsummaryid"), newCcmSummary.getCcmsummaryid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public CcmSummary findDetailsByMobileNo(CcmSummary ccmSummary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CcmSummary findDetailsByEmail(CcmSummary ccmSummary) {
		CcmSummary newCcmSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmSummary> criteriaQuery = criteriaBuilder.createQuery(CcmSummary.class);
		Root<CcmSummary> ccmSummaryRoot = criteriaQuery.from(CcmSummary.class);
		criteriaQuery.select(ccmSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(ccmSummaryRoot.get("productiondate"), ccmSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(ccmSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(ccmSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<CcmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<CcmSummary> ccmSummaryList = typedQuery.getResultList();
		if(ccmSummaryList.size() != 0){
			newCcmSummary = (CcmSummary)ccmSummaryList.get(0);
		}
		return newCcmSummary;
	}
	
}
