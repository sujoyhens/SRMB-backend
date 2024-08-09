package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.LaboratorySummary;
import com.boenci.srmb.model.production.LaboratorySummary;
import com.boenci.srmb.repository.production.LaboratorySummaryRepository;
import com.boenci.srmb.service.production.LaboratorySummaryService;
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
public class LaboratorySummaryServiceImpl implements LaboratorySummaryService {

	@Autowired
	private LaboratorySummaryRepository laboratorySummaryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public LaboratorySummary findById(long id) {
		LaboratorySummary newLaboratorySummary = null;
		newLaboratorySummary =laboratorySummaryRepository.findById(id).get();		
		return newLaboratorySummary;
	}

	@Override
	public LaboratorySummary validLaboratorySummary(LaboratorySummary laboratorySummary) {
		LaboratorySummary newLaboratorySummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratorySummary> criteriaQuery = criteriaBuilder.createQuery(LaboratorySummary.class);
		Root<LaboratorySummary> laboratorySummaryRoot = criteriaQuery.from(LaboratorySummary.class);
		criteriaQuery.select(laboratorySummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(laboratorySummaryRoot.get("productiondate"), laboratorySummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(laboratorySummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(laboratorySummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<LaboratorySummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<LaboratorySummary> laboratorySummaryList = typedQuery.getResultList();
		if(laboratorySummaryList.size() != 0){
			newLaboratorySummary = (LaboratorySummary)laboratorySummaryList.get(0);
		}
		return newLaboratorySummary;
	}

	@Override
	public LaboratorySummary save(LaboratorySummary laboratorySummary) {
		LaboratorySummary newLaboratorySummary = null;
		newLaboratorySummary = laboratorySummaryRepository.save(laboratorySummary);
		return newLaboratorySummary;
	}

	@Transactional
	@Override
	public LaboratorySummary update(LaboratorySummary laboratorySummary) {
		long laboratorySummaryId = laboratorySummary.getLaboratorysummaryid();
		LaboratorySummary newLaboratorySummary = null;
		if(laboratorySummaryRepository.existsById(laboratorySummaryId)){
			newLaboratorySummary = laboratorySummaryRepository.save(laboratorySummary);
			return newLaboratorySummary;
		}else{
			return newLaboratorySummary;
		}
		
	}

	@Transactional
	@Override
	public String updateLaboratorySummary(LaboratorySummary laboratorySummary) {
		
		LaboratorySummary validateLaboratorySummary = validLaboratorySummary(laboratorySummary);
		if(validateLaboratorySummary == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<LaboratorySummary> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(LaboratorySummary.class);
			Root<LaboratorySummary> laboratorySummaryRoot = criteriaUpdate.from(LaboratorySummary.class);
			criteriaUpdate.set("productiondate", laboratorySummary.getProductiondate());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", laboratorySummary.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(laboratorySummaryRoot.get("laboratorysummaryid"), laboratorySummary.getLaboratorysummaryid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<LaboratorySummary> findAllLaboratorySummary() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratorySummary> criteriaQuery = criteriaBuilder.createQuery(LaboratorySummary.class);
		Root<LaboratorySummary> laboratorySummaryRoot = criteriaQuery.from(LaboratorySummary.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(laboratorySummaryRoot.get("unitname")));
		List<LaboratorySummary> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isLaboratorySummaryExist(LaboratorySummary laboratorySummary) {
		
		return true;
	}

	@Override
	public List<LaboratorySummary> findLaboratorySummaryBySearchType(LaboratorySummary laboratorySummary, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratorySummary> criteriaQuery = criteriaBuilder.createQuery(LaboratorySummary.class);
		Root<LaboratorySummary> laboratorySummaryRoot = criteriaQuery.from(LaboratorySummary.class);
		criteriaQuery.select(laboratorySummaryRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "laboratorysummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutlab, hotoutlrf, hotoutpowercut, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, remarks, labproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,LaboratorySummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<LaboratorySummary> laboratorySummaryList = new ArrayList<LaboratorySummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratorySummary newLaboratorySummary = new LaboratorySummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratorySummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratorySummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratorySummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratorySummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratorySummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratorySummary.setLaboratorysummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratorySummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratorySummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratorySummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratorySummary.setHotoutlab(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newLaboratorySummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newLaboratorySummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newLaboratorySummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratorySummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newLaboratorySummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newLaboratorySummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newLaboratorySummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newLaboratorySummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setRemarks(newObject.toString());
						}else if(objectNDX == 33) {
							newLaboratorySummary.setLabproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newLaboratorySummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newLaboratorySummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newLaboratorySummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUnittype(newObject.toString());
						}
					}
					laboratorySummaryList.add(newLaboratorySummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratorySummaryList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(laboratorySummaryRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(laboratorySummaryRoot.get("status"), laboratorySummary.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<LaboratorySummary> typedQuery = entityManager.createQuery(criteriaQuery);
			List<LaboratorySummary> laboratorySummaryList = typedQuery.getResultList();
			return laboratorySummaryList;
			
		}else if(searchType.equalsIgnoreCase("productiondate")){
			String queryString = "laboratorysummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutlab, hotoutlrf, hotoutpowercut, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, remarks, labproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,LaboratorySummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.productiondate = ?3";
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,laboratorySummary.getProductiondate());
			List<LaboratorySummary> laboratorySummaryList = new ArrayList<LaboratorySummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					LaboratorySummary newLaboratorySummary = new LaboratorySummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newLaboratorySummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newLaboratorySummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newLaboratorySummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newLaboratorySummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newLaboratorySummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newLaboratorySummary.setLaboratorysummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newLaboratorySummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newLaboratorySummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newLaboratorySummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newLaboratorySummary.setHotoutlab(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newLaboratorySummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newLaboratorySummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newLaboratorySummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newLaboratorySummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newLaboratorySummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newLaboratorySummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newLaboratorySummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newLaboratorySummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setRemarks(newObject.toString());
						}else if(objectNDX == 33) {
							newLaboratorySummary.setLabproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newLaboratorySummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newLaboratorySummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newLaboratorySummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newLaboratorySummary.setUnittype(newObject.toString());
						}
					}
					laboratorySummaryList.add(newLaboratorySummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return laboratorySummaryList;
		}else{
			List<LaboratorySummary> laboratorySummaryList = null;
			return laboratorySummaryList;
		}
		
	}

	@Override
	public void deleteLaboratorySummary(LaboratorySummary laboratorySummary) {
		LaboratorySummary newLaboratorySummary = findById(laboratorySummary.getLaboratorysummaryid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<LaboratorySummary> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(LaboratorySummary.class);
		Root<LaboratorySummary> laboratorySummaryRoot = criteriaUpdate.from(LaboratorySummary.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(laboratorySummaryRoot.get("laboratorysummaryid"), newLaboratorySummary.getLaboratorysummaryid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public LaboratorySummary findDetailsByMobileNo(LaboratorySummary laboratorySummary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LaboratorySummary findDetailsByEmail(LaboratorySummary laboratorySummary) {
		LaboratorySummary newLaboratorySummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LaboratorySummary> criteriaQuery = criteriaBuilder.createQuery(LaboratorySummary.class);
		Root<LaboratorySummary> laboratorySummaryRoot = criteriaQuery.from(LaboratorySummary.class);
		criteriaQuery.select(laboratorySummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(laboratorySummaryRoot.get("productiondate"), laboratorySummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(laboratorySummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(laboratorySummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<LaboratorySummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<LaboratorySummary> laboratorySummaryList = typedQuery.getResultList();
		if(laboratorySummaryList.size() != 0){
			newLaboratorySummary = (LaboratorySummary)laboratorySummaryList.get(0);
		}
		return newLaboratorySummary;
	}
	
}
