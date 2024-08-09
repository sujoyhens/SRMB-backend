package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.ProductionLog;
import com.boenci.srmb.repository.production.ProductionLogRepository;
import com.boenci.srmb.service.production.ProductionLogService;
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
public class ProductionLogServiceImpl implements ProductionLogService {

	@Autowired
	private ProductionLogRepository productionLogRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProductionLog findById(long id) {
		ProductionLog newProductionLog = null;
		newProductionLog =productionLogRepository.findById(id).get();		
		return newProductionLog;
	}

	@Override
	public ProductionLog validProductionLog(ProductionLog productionLog) {
		ProductionLog newProductionLog = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionLog> criteriaQuery = criteriaBuilder.createQuery(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaQuery.from(ProductionLog.class);
		criteriaQuery.select(productionLogRoot);
		Predicate predicateDate = criteriaBuilder.equal(productionLogRoot.get("productionlogdate"), productionLog.getProductionlogdate());
		Predicate predicateTime = criteriaBuilder.equal(productionLogRoot.get("ladleno"), productionLog.getLadleno());
		Predicate predicateEmail = criteriaBuilder.equal(productionLogRoot.get("sequenceno"), productionLog.getSequenceno());
		Predicate predicateStatus = criteriaBuilder.equal(productionLogRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productionLogRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateDate,predicateTime,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductionLog> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductionLog> productionLogList = typedQuery.getResultList();
		if(productionLogList.size() != 0){
			newProductionLog = (ProductionLog)productionLogList.get(0);
		}
		return newProductionLog;
	}

	@Override
	public ProductionLog save(ProductionLog productionLog) {
		ProductionLog newProductionLog = null;
		newProductionLog = productionLogRepository.save(productionLog);
		return newProductionLog;
	}

	@Transactional
	@Override
	public ProductionLog update(ProductionLog productionLog) {
		long productionLogId = productionLog.getProductionlogid();
		ProductionLog newProductionLog = null;
		ProductionLog newProductionLogforSearch = productionLogRepository.findById(productionLogId).get();
		if(newProductionLogforSearch != null){
			productionLog.setProductionid(newProductionLogforSearch.getProductionid());
			productionLog.setIsmillproductionupdated(newProductionLogforSearch.getIsmillproductionupdated());
			newProductionLog = productionLogRepository.save(productionLog);
			return newProductionLog;
		}else{
			return newProductionLog;
		}
		
	}

	@Transactional
	@Override
	public String updateProductionLog(ProductionLog productionLog) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ProductionLog> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaUpdate.from(ProductionLog.class);
		criteriaUpdate.set("ismillproductionupdated", productionLog.getIsmillproductionupdated());
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("action", "update");
		criteriaUpdate.where(criteriaBuilder.equal(productionLogRoot.get("productionid"), productionLog.getProductionid()));
		int count = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		if(count > 0){
			return "success";
		}else{
			return "failure";
		}
	}

	@Transactional
	@Override
	public List<ProductionLog> findAllProductionLog() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionLog> criteriaQuery = criteriaBuilder.createQuery(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaQuery.from(ProductionLog.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(productionLogRoot.get("unitname")));
		List<ProductionLog> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isProductionLogExist(ProductionLog productionLog) {
		
		return true;
	}

	@Override
	public List<ProductionLog> findProductionLogBySearchType(ProductionLog productionLog, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionLog> criteriaQuery = criteriaBuilder.createQuery(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaQuery.from(ProductionLog.class);
		criteriaQuery.select(productionLogRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			
			String queryString = "productionlogid,fcladletemperature,sequenceno,fctappingtime,ladleclosetime,"+
			"ladleno,ladleopentime,ladlelife,ladletemperature,ladletipsused,productionid,"+
			"lancingpipeused,productionlogdate,purgingtime,remarks,"+
			"starndmoldoperatortime1,starndmoldoperatortime2,starndmoldoperatortime3,tundishtemperature,purgingtemperature,starttime,productionlogtime, "+
			"breakout,standardloss,closingtime,starndmouldnumber1,starndmouldnumber2,starndmouldnumber3,castingstarttime,smsname,laboratoryname,ccmname,rmname,ProductionLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
			"starndoperatorid1,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid1) as starndoperatorname1 "+
			" ,starndoperatorid2,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid2) as starndoperatorname2, "+
			" starndoperatorid3,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid3) as starnd3operatorname3"
			+returnQuery;

			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<ProductionLog> productionLogList = new ArrayList<ProductionLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionLog newProductionLog = new ProductionLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionLog.setStarndoperatorid1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname1(newObject.toString());
						}else if(objectNDX == 20) {
							newProductionLog.setStarndoperatorid2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname2(newObject.toString());
						}else if(objectNDX == 22) {
							newProductionLog.setStarndoperatorid3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname3(newObject.toString());
						}else if(objectNDX == 24) {
							newProductionLog.setProductionlogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionLog.setFcladletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSequenceno(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setFctappingtime(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleclosetime(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleno(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleopentime(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadlelife(newObject.toString());
						}else if(objectNDX == 32) {
							newProductionLog.setLadletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadletipsused(newObject.toString());
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionid(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLancingpipeused(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogdate(newObject.toString());
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPurgingtime(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRemarks(newObject.toString());
						}else if(objectNDX == 39) {
							newProductionLog.setStarndmoldoperatortime1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionLog.setStarndmoldoperatortime2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newProductionLog.setStarndmoldoperatortime3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newProductionLog.setTundishtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionLog.setPurgingtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarttime(newObject.toString());
						}else if(objectNDX == 45) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogtime(newObject.toString());
						} else if(objectNDX == 46) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setBreakout(newObject.toString());
						}else if(objectNDX == 47) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStandardloss(newObject.toString());
						}else if(objectNDX == 48) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setClosingtime(newObject.toString());
						} else if(objectNDX == 49) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber1(newObject.toString());
						} else if(objectNDX == 50) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber2(newObject.toString());
						} else if(objectNDX == 51) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber3(newObject.toString());
						} else if(objectNDX == 52) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCastingstarttime(newObject.toString());
						}else if(objectNDX == 53) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSmsname(newObject.toString());
						} else if(objectNDX == 54) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 55) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCcmname(newObject.toString());
						} else if(objectNDX == 56) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRmname(newObject.toString());
						}
					}
					productionLogList.add(newProductionLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionLogList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(productionLogRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(productionLogRoot.get("status"), productionLog.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<ProductionLog> typedQuery = entityManager.createQuery(criteriaQuery);
			List<ProductionLog> productionLogList = typedQuery.getResultList();
			return productionLogList;
			
		}else if(searchType.equalsIgnoreCase("productionlogdate")){
			String queryString = "productionlogid,fcladletemperature,sequenceno,fctappingtime,ladleclosetime,"+
			"ladleno,ladleopentime,ladlelife,ladletemperature,ladletipsused,productionid,"+
			"lancingpipeused,productionlogdate,purgingtime,remarks,"+
			"starndmoldoperatortime1,starndmoldoperatortime2,starndmoldoperatortime3,tundishtemperature,purgingtemperature,starttime,productionlogtime, "+
			"breakout,standardloss,closingtime,starndmouldnumber1,starndmouldnumber2,starndmouldnumber3,castingstarttime,smsname,laboratoryname,ccmname,rmname,ProductionLog";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
			"starndoperatorid1,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid1) as starndoperatorname1 "+
			" ,starndoperatorid2,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid2) as starndoperatorname2, "+
			" starndoperatorid3,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.starndoperatorid3) as starnd3operatorname3"
			+returnQuery+ " AND querytable.productionlogdate = ?3";

			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,productionLog.getProductionlogdate());
			List<ProductionLog> productionLogList = new ArrayList<ProductionLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionLog newProductionLog = new ProductionLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionLog.setStarndoperatorid1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname1(newObject.toString());
						}else if(objectNDX == 20) {
							newProductionLog.setStarndoperatorid2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname2(newObject.toString());
						}else if(objectNDX == 22) {
							newProductionLog.setStarndoperatorid3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname3(newObject.toString());
						}else if(objectNDX == 24) {
							newProductionLog.setProductionlogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionLog.setFcladletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSequenceno(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setFctappingtime(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleclosetime(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleno(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleopentime(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadlelife(newObject.toString());
						}else if(objectNDX == 32) {
							newProductionLog.setLadletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadletipsused(newObject.toString());
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionid(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLancingpipeused(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogdate(newObject.toString());
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPurgingtime(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRemarks(newObject.toString());
						}else if(objectNDX == 39) {
							newProductionLog.setStarndmoldoperatortime1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionLog.setStarndmoldoperatortime2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newProductionLog.setStarndmoldoperatortime3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newProductionLog.setTundishtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionLog.setPurgingtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarttime(newObject.toString());
						}else if(objectNDX == 45) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogtime(newObject.toString());
						} else if(objectNDX == 46) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setBreakout(newObject.toString());
						}else if(objectNDX == 47) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStandardloss(newObject.toString());
						}else if(objectNDX == 48) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setClosingtime(newObject.toString());
						} else if(objectNDX == 49) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber1(newObject.toString());
						} else if(objectNDX == 50) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber2(newObject.toString());
						} else if(objectNDX == 51) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber3(newObject.toString());
						} else if(objectNDX == 52) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCastingstarttime(newObject.toString());
						}else if(objectNDX == 53) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSmsname(newObject.toString());
						} else if(objectNDX == 54) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 55) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCcmname(newObject.toString());
						} else if(objectNDX == 56) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRmname(newObject.toString());
						}
					}
					productionLogList.add(newProductionLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionLogList;
		}else if(searchType.equalsIgnoreCase("millproduction")){

			Query query = null;
			if(productionLog.getUnitmasterid() == 7){

				String queryString = "productionlogid,fcladletemperature,sequenceno,fctappingtime,ladleclosetime,"+
				"ladleno,ladleopentime,ladlelife,ladletemperature,ladletipsused,productionid,"+
				"lancingpipeused,productionlogdate,purgingtime,remarks,"+
				"starndmoldoperatortime1,starndmoldoperatortime2,starndmoldoperatortime3,tundishtemperature,purgingtemperature,starttime,productionlogtime, "+
				"breakout,standardloss,closingtime,starndmouldnumber1,starndmouldnumber2,starndmouldnumber3,castingstarttime,smsname,laboratoryname,ccmname,rmname,ProductionLog";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
				"starndoperatorid1,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid1) as starndoperatorname1 "+
				" ,starndoperatorid2,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid2) as starndoperatorname2, "+
				" starndoperatorid3,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid3) as starnd3operatorname3"
				+returnQuery+" AND querytable.ismillproductionupdated = ?3 AND querytable.unitmasterid in (5) order by querytable.productionlogid ASC LIMIT 1";

				System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"no");

			}else if(productionLog.getUnitmasterid() == 8){
				
				String queryString = "productionlogid,fcladletemperature,sequenceno,fctappingtime,ladleclosetime,"+
				"ladleno,ladleopentime,ladlelife,ladletemperature,ladletipsused,productionid,"+
				"lancingpipeused,productionlogdate,purgingtime,remarks,"+
				"starndmoldoperatortime1,starndmoldoperatortime2,starndmoldoperatortime3,tundishtemperature,purgingtemperature,starttime,productionlogtime, "+
				"breakout,standardloss,closingtime,starndmouldnumber1,starndmouldnumber2,starndmouldnumber3,castingstarttime,smsname,laboratoryname,ccmname,rmname,ProductionLog";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
				"starndoperatorid1,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid1) as starndoperatorname1 "+
				" ,starndoperatorid2,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid2) as starndoperatorname2, "+
				" starndoperatorid3,(SELECT om.employeename FROM OperatorMaster om WHERE "+
				" om.operatormasterid = querytable.starndoperatorid3) as starnd3operatorname3"
				+returnQuery+" AND querytable.ismillproductionupdated = ?3 AND querytable.unitmasterid in (5,6) order by querytable.productionlogid ASC LIMIT 1";

				System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"no");
			}
			
			List<ProductionLog> productionLogList = new ArrayList<ProductionLog>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionLog newProductionLog = new ProductionLog();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionLog.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionLog.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionLog.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionLog.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionLog.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionLog.setStarndoperatorid1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname1(newObject.toString());
						}else if(objectNDX == 20) {
							newProductionLog.setStarndoperatorid2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname2(newObject.toString());
						}else if(objectNDX == 22) {
							newProductionLog.setStarndoperatorid3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndoperatorname3(newObject.toString());
						}else if(objectNDX == 24) {
							newProductionLog.setProductionlogid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionLog.setFcladletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSequenceno(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setFctappingtime(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleclosetime(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleno(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadleopentime(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadlelife(newObject.toString());
						}else if(objectNDX == 32) {
							newProductionLog.setLadletemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLadletipsused(newObject.toString());
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionid(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLancingpipeused(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogdate(newObject.toString());
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setPurgingtime(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRemarks(newObject.toString());
						}else if(objectNDX == 39) {
							newProductionLog.setStarndmoldoperatortime1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionLog.setStarndmoldoperatortime2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newProductionLog.setStarndmoldoperatortime3(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newProductionLog.setTundishtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionLog.setPurgingtemperature(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarttime(newObject.toString());
						}else if(objectNDX == 45) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setProductionlogtime(newObject.toString());
						} else if(objectNDX == 46) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setBreakout(newObject.toString());
						}else if(objectNDX == 47) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStandardloss(newObject.toString());
						}else if(objectNDX == 48) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setClosingtime(newObject.toString());
						} else if(objectNDX == 49) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber1(newObject.toString());
						} else if(objectNDX == 50) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber2(newObject.toString());
						} else if(objectNDX == 51) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setStarndmouldnumber3(newObject.toString());
						} else if(objectNDX == 52) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCastingstarttime(newObject.toString());
						}else if(objectNDX == 53) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setSmsname(newObject.toString());
						} else if(objectNDX == 54) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 55) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setCcmname(newObject.toString());
						} else if(objectNDX == 56) {
							if(newObject == null){
								newObject = "";
							}
							newProductionLog.setRmname(newObject.toString());
						}
					}
					productionLogList.add(newProductionLog);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionLogList;
		}else{
			
			List<ProductionLog> productionLogList = null;
			return productionLogList;
		}
		
	}

	@Override
	public void deleteProductionLog(ProductionLog productionLog) {
		ProductionLog newProductionLog = findById(productionLog.getProductionlogid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ProductionLog> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaUpdate.from(ProductionLog.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(productionLogRoot.get("productionlogid"), newProductionLog.getProductionlogid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public ProductionLog findDetailsByMobileNo(ProductionLog productionLog) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductionLog findDetailsByEmail(ProductionLog productionLog) {
		ProductionLog newProductionLog = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionLog> criteriaQuery = criteriaBuilder.createQuery(ProductionLog.class);
		Root<ProductionLog> productionLogRoot = criteriaQuery.from(ProductionLog.class);
		criteriaQuery.select(productionLogRoot);
		Predicate predicateEmail = criteriaBuilder.equal(productionLogRoot.get("productionid"), productionLog.getProductionid());
		Predicate predicateStatus = criteriaBuilder.equal(productionLogRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productionLogRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductionLog> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductionLog> productionLogList = typedQuery.getResultList();
		if(productionLogList.size() != 0){
			newProductionLog = (ProductionLog)productionLogList.get(0);
		}
		return newProductionLog;
	}
	
}
