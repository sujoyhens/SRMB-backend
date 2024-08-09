package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.RmSummary;
import com.boenci.srmb.model.production.RmSummary;
import com.boenci.srmb.repository.production.RmSummaryRepository;
import com.boenci.srmb.service.production.RmSummaryService;
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
public class RmSummaryServiceImpl implements RmSummaryService {

	@Autowired
	private RmSummaryRepository rmSummaryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RmSummary findById(long id) {
		RmSummary newRmSummary = null;
		newRmSummary =rmSummaryRepository.findById(id).get();		
		return newRmSummary;
	}

	@Override
	public RmSummary validRmSummary(RmSummary rmSummary) {
		RmSummary newRmSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmSummary> criteriaQuery = criteriaBuilder.createQuery(RmSummary.class);
		Root<RmSummary> rmSummaryRoot = criteriaQuery.from(RmSummary.class);
		criteriaQuery.select(rmSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(rmSummaryRoot.get("productiondate"), rmSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(rmSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmSummary> rmSummaryList = typedQuery.getResultList();
		if(rmSummaryList.size() != 0){
			newRmSummary = (RmSummary)rmSummaryList.get(0);
		}
		return newRmSummary;
	}

	@Override
	public RmSummary save(RmSummary rmSummary) {
		RmSummary newRmSummary = null;
		newRmSummary = rmSummaryRepository.save(rmSummary);
		return newRmSummary;
	}

	@Transactional
	@Override
	public RmSummary update(RmSummary rmSummary) {
		long rmSummaryId = rmSummary.getRmsummaryid();
		RmSummary newRmSummary = null;
		if(rmSummaryRepository.existsById(rmSummaryId)){
			newRmSummary = rmSummaryRepository.save(rmSummary);
			return newRmSummary;
		}else{
			return newRmSummary;
		}
		
	}

	@Transactional
	@Override
	public String updateRmSummary(RmSummary rmSummary) {
		
		RmSummary validateRmSummary = validRmSummary(rmSummary);
		if(validateRmSummary == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<RmSummary> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(RmSummary.class);
			Root<RmSummary> rmSummaryRoot = criteriaUpdate.from(RmSummary.class);
			criteriaUpdate.set("productiondate", rmSummary.getProductiondate());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", rmSummary.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(rmSummaryRoot.get("rmsummaryid"), rmSummary.getRmsummaryid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<RmSummary> findAllRmSummary() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmSummary> criteriaQuery = criteriaBuilder.createQuery(RmSummary.class);
		Root<RmSummary> rmSummaryRoot = criteriaQuery.from(RmSummary.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(rmSummaryRoot.get("unitname")));
		List<RmSummary> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRmSummaryExist(RmSummary rmSummary) {
		
		return true;
	}

	@Override
	public List<RmSummary> findRmSummaryBySearchType(RmSummary rmSummary, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmSummary> criteriaQuery = criteriaBuilder.createQuery(RmSummary.class);
		Root<RmSummary> rmSummaryRoot = criteriaQuery.from(RmSummary.class);
		criteriaQuery.select(rmSummaryRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "rmsummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutrm, hotoutlrf, hotoutpowercut, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, remarks, rmproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,RmSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<RmSummary> rmSummaryList = new ArrayList<RmSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					RmSummary newRmSummary = new RmSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newRmSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newRmSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newRmSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newRmSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newRmSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newRmSummary.setRmsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newRmSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newRmSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newRmSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newRmSummary.setHotoutrm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newRmSummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newRmSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newRmSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newRmSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newRmSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newRmSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newRmSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newRmSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 33) {
							newRmSummary.setRmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newRmSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newRmSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newRmSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUnittype(newObject.toString());
						}
					}
					rmSummaryList.add(newRmSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return rmSummaryList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(rmSummaryRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rmSummaryRoot.get("status"), rmSummary.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<RmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
			List<RmSummary> rmSummaryList = typedQuery.getResultList();
			return rmSummaryList;
			
		}else if(searchType.equalsIgnoreCase("productiondate")){
			String queryString = "rmsummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" hotoutrm, hotoutlrf, hotoutpowercut, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, remarks, rmproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,RmSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.productiondate = ?3";
			//System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,rmSummary.getProductiondate());
			List<RmSummary> rmSummaryList = new ArrayList<RmSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					RmSummary newRmSummary = new RmSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newRmSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newRmSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newRmSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newRmSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newRmSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newRmSummary.setRmsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newRmSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newRmSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newRmSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newRmSummary.setHotoutrm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newRmSummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newRmSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newRmSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newRmSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newRmSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newRmSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newRmSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newRmSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 33) {
							newRmSummary.setRmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newRmSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newRmSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newRmSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newRmSummary.setUnittype(newObject.toString());
						}
					}
					rmSummaryList.add(newRmSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return rmSummaryList;
		}else{
			List<RmSummary> rmSummaryList = null;
			return rmSummaryList;
		}
		
	}

	@Override
	public void deleteRmSummary(RmSummary rmSummary) {
		RmSummary newRmSummary = findById(rmSummary.getRmsummaryid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RmSummary> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RmSummary.class);
		Root<RmSummary> rmSummaryRoot = criteriaUpdate.from(RmSummary.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(rmSummaryRoot.get("rmsummaryid"), newRmSummary.getRmsummaryid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public RmSummary findDetailsByMobileNo(RmSummary rmSummary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RmSummary findDetailsByEmail(RmSummary rmSummary) {
		RmSummary newRmSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmSummary> criteriaQuery = criteriaBuilder.createQuery(RmSummary.class);
		Root<RmSummary> rmSummaryRoot = criteriaQuery.from(RmSummary.class);
		criteriaQuery.select(rmSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(rmSummaryRoot.get("productiondate"), rmSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(rmSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmSummary> rmSummaryList = typedQuery.getResultList();
		if(rmSummaryList.size() != 0){
			newRmSummary = (RmSummary)rmSummaryList.get(0);
		}
		return newRmSummary;
	}
	
}
