package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.ProductionSummary;
import com.boenci.srmb.repository.production.ProductionSummaryRepository;
import com.boenci.srmb.service.production.ProductionSummaryService;
import com.boenci.srmb.utility.AppConstants;
import com.boenci.srmb.utility.SrmbUtilty;

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
public class ProductionSummaryServiceImpl implements ProductionSummaryService {

	@Autowired
	private ProductionSummaryRepository productionSummaryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProductionSummary findById(long id) {
		ProductionSummary newProductionSummary = null;
		newProductionSummary =productionSummaryRepository.findById(id).get();		
		return newProductionSummary;
	}

	@Override
	public ProductionSummary validProductionSummary(ProductionSummary productionSummary) {
		ProductionSummary newProductionSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionSummary> criteriaQuery = criteriaBuilder.createQuery(ProductionSummary.class);
		Root<ProductionSummary> productionSummaryRoot = criteriaQuery.from(ProductionSummary.class);
		criteriaQuery.select(productionSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(productionSummaryRoot.get("productiondate"), productionSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(productionSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productionSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductionSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductionSummary> productionSummaryList = typedQuery.getResultList();
		if(productionSummaryList.size() != 0){
			newProductionSummary = (ProductionSummary)productionSummaryList.get(0);
		}
		return newProductionSummary;
	}

	@Override
	public ProductionSummary save(ProductionSummary productionSummary) {
		ProductionSummary newProductionSummary = null;
		newProductionSummary = productionSummaryRepository.save(productionSummary);
		return newProductionSummary;
	}

	@Transactional
	@Override
	public ProductionSummary update(ProductionSummary productionSummary) {
		long productionSummaryId = productionSummary.getProductionsummaryid();
		ProductionSummary newProductionSummary = null;
		if(productionSummaryRepository.existsById(productionSummaryId)){
			newProductionSummary = productionSummaryRepository.save(productionSummary);
			return newProductionSummary;
		}else{
			return newProductionSummary;
		}
		
	}

	@Transactional
	@Override
	public String updateProductionSummary(ProductionSummary productionSummary) {
		
		ProductionSummary validateProductionSummary = validProductionSummary(productionSummary);
		if(validateProductionSummary == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<ProductionSummary> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(ProductionSummary.class);
			Root<ProductionSummary> productionSummaryRoot = criteriaUpdate.from(ProductionSummary.class);
			criteriaUpdate.set("productiondate", productionSummary.getProductiondate());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", productionSummary.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(productionSummaryRoot.get("productionsummaryid"), productionSummary.getProductionsummaryid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<ProductionSummary> findAllProductionSummary() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionSummary> criteriaQuery = criteriaBuilder.createQuery(ProductionSummary.class);
		Root<ProductionSummary> productionSummaryRoot = criteriaQuery.from(ProductionSummary.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(productionSummaryRoot.get("unitname")));
		List<ProductionSummary> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isProductionSummaryExist(ProductionSummary productionSummary) {
		
		return true;
	}

	@Override
	public List<ProductionSummary> findProductionSummaryBySearchType(ProductionSummary productionSummary, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionSummary> criteriaQuery = criteriaBuilder.createQuery(ProductionSummary.class);
		Root<ProductionSummary> productionSummaryRoot = criteriaQuery.from(ProductionSummary.class);
		criteriaQuery.select(productionSummaryRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){

			String queryString = "productionsummaryid,downtime, electricaldowntime, hotchargingpercentage,"+
			" hotoutccm, hotoutfurnace, hotoutlrf, hotoutpowercut, hotoutrm1, hotoutrm2, lrfdowntime,"+
			" mechanicaldowntime, noofccmsequencebreak, nooffurnacesequencebreak, "+
			" noofpowersequencebreak, powercutdowntime, processdowntime, productiondate, smsproduction,"+
			" remarks, totalfurnacesequencebreaktime, totalpowersequencebreaktime, totalsequencebreaktime,unittype,noofheat, "+
			" ccmproduction,rmproduction,totalproduction,totalccmsequencebreaktime,noofsequencebreak,ProductionSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<ProductionSummary> productionSummaryList = new ArrayList<ProductionSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionSummary newProductionSummary = new ProductionSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionSummary.setProductionsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newProductionSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newProductionSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newProductionSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newProductionSummary.setHotoutccm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newProductionSummary.setHotoutfurnace(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newProductionSummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newProductionSummary.setHotoutrm1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newProductionSummary.setHotoutrm2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newProductionSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newProductionSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newProductionSummary.setNoofccmsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newProductionSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newProductionSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newProductionSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newProductionSummary.setProcessdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 36) {
							newProductionSummary.setSmsproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 38) {
							newProductionSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							newProductionSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnittype(newObject.toString());
						}else if(objectNDX == 42) {
							newProductionSummary.setNoofheat(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionSummary.setCcmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newProductionSummary.setRmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newProductionSummary.setTotalproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newProductionSummary.setTotalccmsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newProductionSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}
					}
					productionSummaryList.add(newProductionSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionSummaryList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(productionSummaryRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(productionSummaryRoot.get("status"), productionSummary.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<ProductionSummary> typedQuery = entityManager.createQuery(criteriaQuery);
			List<ProductionSummary> productionSummaryList = typedQuery.getResultList();
			return productionSummaryList;
			
		}else if(searchType.equalsIgnoreCase("productiondate")){
			String queryString = "productionsummaryid,downtime, electricaldowntime, hotchargingpercentage,"+
			" hotoutccm, hotoutfurnace, hotoutlrf, hotoutpowercut, hotoutrm1, hotoutrm2, lrfdowntime,"+
			" mechanicaldowntime, noofccmsequencebreak, nooffurnacesequencebreak, "+
			" noofpowersequencebreak, powercutdowntime, processdowntime, productiondate, smsproduction,"+
			" remarks, totalfurnacesequencebreaktime, totalpowersequencebreaktime, totalsequencebreaktime,unittype,noofheat, "+
			" ccmproduction,rmproduction,totalproduction,totalccmsequencebreaktime,noofsequencebreak,ProductionSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.productiondate = ?3";
			System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,productionSummary.getProductiondate());
			List<ProductionSummary> productionSummaryList = new ArrayList<ProductionSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionSummary newProductionSummary = new ProductionSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionSummary.setProductionsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newProductionSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newProductionSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newProductionSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newProductionSummary.setHotoutccm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newProductionSummary.setHotoutfurnace(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newProductionSummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newProductionSummary.setHotoutrm1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newProductionSummary.setHotoutrm2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newProductionSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newProductionSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newProductionSummary.setNoofccmsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newProductionSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newProductionSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newProductionSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newProductionSummary.setProcessdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 36) {
							newProductionSummary.setSmsproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 38) {
							newProductionSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							newProductionSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnittype(newObject.toString());
						}else if(objectNDX == 42) {
							newProductionSummary.setNoofheat(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionSummary.setCcmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newProductionSummary.setRmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newProductionSummary.setTotalproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newProductionSummary.setTotalccmsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newProductionSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}
					}
					productionSummaryList.add(newProductionSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionSummaryList;
		}else if(searchType.equalsIgnoreCase("daterange")){
			String dateRange = productionSummary.getProductiondate();
			String[] arrOfStr = dateRange.split("#");
			String startDay =  arrOfStr[0];
			String endDay =  arrOfStr[1];
			String queryString = "productionsummaryid,downtime, electricaldowntime, hotchargingpercentage,"+
			" hotoutccm, hotoutfurnace, hotoutlrf, hotoutpowercut, hotoutrm1, hotoutrm2, lrfdowntime,"+
			" mechanicaldowntime, noofccmsequencebreak, nooffurnacesequencebreak, "+
			" noofpowersequencebreak, powercutdowntime, processdowntime, productiondate, smsproduction,"+
			" remarks, totalfurnacesequencebreaktime, totalpowersequencebreaktime, totalsequencebreaktime,unittype,noofheat, "+
			" ccmproduction,rmproduction,totalproduction,totalccmsequencebreaktime,noofsequencebreak,ProductionSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ 
			" AND TO_DATE(querytable.productiondate, 'YYYY-MM-DD') BETWEEN ?3 AND ?4";
			System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,SrmbUtilty.convertStringToDate(startDay));
			query.setParameter(4,SrmbUtilty.convertStringToDate(endDay));
			List<ProductionSummary> productionSummaryList = new ArrayList<ProductionSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductionSummary newProductionSummary = new ProductionSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newProductionSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newProductionSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newProductionSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newProductionSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newProductionSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newProductionSummary.setProductionsummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newProductionSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newProductionSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newProductionSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newProductionSummary.setHotoutccm(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newProductionSummary.setHotoutfurnace(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newProductionSummary.setHotoutlrf(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newProductionSummary.setHotoutpowercut(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newProductionSummary.setHotoutrm1(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newProductionSummary.setHotoutrm2(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newProductionSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newProductionSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newProductionSummary.setNoofccmsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newProductionSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newProductionSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newProductionSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newProductionSummary.setProcessdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 36) {
							newProductionSummary.setSmsproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setRemarks(newObject.toString());
						}else if(objectNDX == 38) {
							newProductionSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							newProductionSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 40) {
							newProductionSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							if(newObject == null){
								newObject = "";
							}
							newProductionSummary.setUnittype(newObject.toString());
						}else if(objectNDX == 42) {
							newProductionSummary.setNoofheat(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newProductionSummary.setCcmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newProductionSummary.setRmproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newProductionSummary.setTotalproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newProductionSummary.setTotalccmsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newProductionSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}
					}
					productionSummaryList.add(newProductionSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productionSummaryList;
		}else{
			
			List<ProductionSummary> productionSummaryList = null;
			return productionSummaryList;
		}
		
	}

	@Override
	public void deleteProductionSummary(ProductionSummary productionSummary) {
		ProductionSummary newProductionSummary = findById(productionSummary.getProductionsummaryid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ProductionSummary> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ProductionSummary.class);
		Root<ProductionSummary> productionSummaryRoot = criteriaUpdate.from(ProductionSummary.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(productionSummaryRoot.get("productionsummaryid"), newProductionSummary.getProductionsummaryid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public ProductionSummary findDetailsByMobileNo(ProductionSummary productionSummary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductionSummary findDetailsByEmail(ProductionSummary productionSummary) {
		ProductionSummary newProductionSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductionSummary> criteriaQuery = criteriaBuilder.createQuery(ProductionSummary.class);
		Root<ProductionSummary> productionSummaryRoot = criteriaQuery.from(ProductionSummary.class);
		criteriaQuery.select(productionSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(productionSummaryRoot.get("productiondate"), productionSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(productionSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productionSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductionSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductionSummary> productionSummaryList = typedQuery.getResultList();
		if(productionSummaryList.size() != 0){
			newProductionSummary = (ProductionSummary)productionSummaryList.get(0);
		}
		return newProductionSummary;
	}
	
}
