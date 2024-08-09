package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.MillProduction;
import com.boenci.srmb.repository.production.MillProductionRepository;
import com.boenci.srmb.service.production.MillProductionService;
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
public class MillProductionServiceImpl implements MillProductionService {

	@Autowired
	private MillProductionRepository millProductionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public MillProduction findById(long id) {
		MillProduction newMillProduction = null;
		newMillProduction = millProductionRepository.findById(id).get();		
		return newMillProduction;
	}

	@Override
	public MillProduction validMillProduction(MillProduction millProduction) {
		MillProduction newMillProduction = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MillProduction> criteriaQuery = criteriaBuilder.createQuery(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaQuery.from(MillProduction.class);
		criteriaQuery.select(millProductionRoot);
		Predicate predicateMillType = criteriaBuilder.equal(millProductionRoot.get("areamasterid"), millProduction.getAreamasterid());
		Predicate predicateProductionDate = criteriaBuilder.equal(millProductionRoot.get("millproductiondate"), millProduction.getMillproductiondate());
		Predicate predicateProductionTime = criteriaBuilder.equal(millProductionRoot.get("millproductiontime"), millProduction.getMillproductiontime());
		Predicate predicateStatus = criteriaBuilder.equal(millProductionRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(millProductionRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateMillType,predicateProductionDate,predicateProductionTime,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MillProduction> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MillProduction> millProductionList = typedQuery.getResultList();
		if(millProductionList.size() != 0){
			newMillProduction = (MillProduction)millProductionList.get(0);
		}
		return newMillProduction;
	}

	@Override
	public MillProduction save(MillProduction millProduction) {
		MillProduction newMillProduction = null;
		newMillProduction = millProductionRepository.save(millProduction);
		return newMillProduction;
	}

	@Transactional
	@Override
	public MillProduction update(MillProduction millProduction) {
		long millProductionId = millProduction.getMillproductionid();
		MillProduction newMillProduction = null;
		MillProduction newMillProductionforSearch = millProductionRepository.findById(millProductionId).get();
		if(newMillProductionforSearch != null){
			millProduction.setProductionid(newMillProductionforSearch.getProductionid());
			newMillProduction = millProductionRepository.save(millProduction);
			return newMillProduction;
		}else{
			return newMillProduction;
		}
		
	}

	@Transactional
	@Override
	public String updateMillProduction(MillProduction millProduction) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<MillProduction> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaUpdate.from(MillProduction.class);
		criteriaUpdate.set("foremanid", millProduction.getForemanid());
		criteriaUpdate.set("croperatorid", millProduction.getCroperatorid());
		criteriaUpdate.set("rmoneproductionsize", millProduction.getRmoneproductionsize());
		criteriaUpdate.set("foremanid", millProduction.getForemanid());
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", millProduction.getStatus());
		criteriaUpdate.set("action", "update");
		Predicate predicateMillProductionId = criteriaBuilder.equal(millProductionRoot.get("millproductionid"), millProduction.getMillproductionid());
		Predicate predicateStatus = criteriaBuilder.equal(millProductionRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(millProductionRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateMillProductionId,predicateStatus,predicateDelete);
		criteriaUpdate.where(predicateAnd);
		int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		System.out.println("rowCount::  " + rowCount);
		if(rowCount > 0){
			return "Success";
		}else{
			return "Failure";
		}
			
		
	}

	@Transactional
	@Override
	public List<MillProduction> findAllMillProduction() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MillProduction> criteriaQuery = criteriaBuilder.createQuery(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaQuery.from(MillProduction.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(millProductionRoot.get("unitname")));
		List<MillProduction> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isMillProductionExist(MillProduction millProduction) {
		
		return true;
	}

	@Override
	public List<MillProduction> findMillProductionBySearchType(MillProduction millProduction, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MillProduction> criteriaQuery = criteriaBuilder.createQuery(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaQuery.from(MillProduction.class);
		criteriaQuery.select(millProductionRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			

			String queryString = "millproductionid,ccmcummulative,ccmdowntimebd,ccmdowntimedelay,"+
			"rmoneproductionsize,ccmstand,downtimemissroll,milldowntimebd,milldowntimedelay, milldowntimesequencebreak, "+
			" millproductiondate,millproductiontime,millsrollnos, powercutdowntime,totalproduction,unittype,itemtype,productionid, "+
			" rmoneproductionpcs,rmoneproductionmt,rmtwoproductionsize,rmtwoproductionpcs,rmtwoproductionmt,smsname,laboratoryname,ccmname,rmname,MillProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
			"foremanid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.foremanid) as foremanname "+
			" ,croperatorid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.croperatorid) as croperatorname"	+returnQuery;
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<MillProduction> millProductionList = new ArrayList<MillProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MillProduction newMillProduction = new MillProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMillProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMillProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMillProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMillProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMillProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMillProduction.setForemanid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setForemanname(newObject.toString());
						}else if(objectNDX == 20) {
							newMillProduction.setCroperatorid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCroperatorname(newObject.toString());
						}else if(objectNDX == 22) {
							newMillProduction.setMillproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newMillProduction.setCcmcummulative(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimebd(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimedelay(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionsize(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmstand(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDowntimemissroll(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimebd(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimedelay(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimesequencebreak(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiondate(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiontime(newObject.toString());
						}else if(objectNDX == 34) {
							newMillProduction.setMillsrollnos(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPowercutdowntime(newObject.toString());
						}else if(objectNDX == 36) {
							newMillProduction.setTotalproduction(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnittype(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setItemtype(newObject.toString());
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setProductionid(newObject.toString());
						} else if(objectNDX == 40) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionpcs(newObject.toString());
						} else if(objectNDX == 41) {							
							newMillProduction.setRmoneproductionmt(Double.valueOf(newObject.toString()));							
						} else if(objectNDX == 42) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionsize(newObject.toString());
						} else if(objectNDX == 43) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionpcs(newObject.toString());
						} else if(objectNDX == 44) {
							newMillProduction.setRmtwoproductionmt(Double.valueOf(newObject.toString()));
						}
					}
					millProductionList.add(newMillProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return millProductionList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(millProductionRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(millProductionRoot.get("status"), millProduction.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<MillProduction> typedQuery = entityManager.createQuery(criteriaQuery);
			List<MillProduction> millProductionList = typedQuery.getResultList();
			return millProductionList;
			
		}else if(searchType.equalsIgnoreCase("millproductiondate")){
			String queryString = "millproductionid,ccmcummulative,ccmdowntimebd,ccmdowntimedelay,"+
			"rmoneproductionsize,ccmstand,downtimemissroll,milldowntimebd,milldowntimedelay, milldowntimesequencebreak, "+
			" millproductiondate,millproductiontime,millsrollnos, powercutdowntime,totalproduction,unittype,itemtype,productionid, "+
			" rmoneproductionpcs,rmoneproductionmt,rmtwoproductionsize,rmtwoproductionpcs,rmtwoproductionmt,smsname,laboratoryname,ccmname,rmname,MillProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
			"foremanid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.foremanid) as foremanname "+
			" ,croperatorid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.croperatorid) as croperatorname"	
			+returnQuery+ " AND querytable.millproductiondate = ?3";
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,millProduction.getMillproductiondate());
			List<MillProduction> millProductionList = new ArrayList<MillProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MillProduction newMillProduction = new MillProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMillProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMillProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMillProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMillProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMillProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMillProduction.setForemanid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setForemanname(newObject.toString());
						}else if(objectNDX == 20) {
							newMillProduction.setCroperatorid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCroperatorname(newObject.toString());
						}else if(objectNDX == 22) {
							newMillProduction.setMillproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newMillProduction.setCcmcummulative(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimebd(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimedelay(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionsize(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmstand(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDowntimemissroll(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimebd(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimedelay(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimesequencebreak(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiondate(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiontime(newObject.toString());
						}else if(objectNDX == 34) {
							newMillProduction.setMillsrollnos(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPowercutdowntime(newObject.toString());
						}else if(objectNDX == 36) {
							newMillProduction.setTotalproduction(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnittype(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setItemtype(newObject.toString());
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setProductionid(newObject.toString());
						} else if(objectNDX == 40) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionpcs(newObject.toString());
						} else if(objectNDX == 41) {							
							newMillProduction.setRmoneproductionmt(Double.valueOf(newObject.toString()));							
						} else if(objectNDX == 42) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionsize(newObject.toString());
						} else if(objectNDX == 43) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionpcs(newObject.toString());
						} else if(objectNDX == 44) {
							newMillProduction.setRmtwoproductionmt(Double.valueOf(newObject.toString()));
						}
					}
					millProductionList.add(newMillProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return millProductionList;
		}else if(searchType.equalsIgnoreCase("daterange")){
			String dateRange = millProduction.getMillproductiondate();
			String[] arrOfStr = dateRange.split("#");
			String startDay =  arrOfStr[0];
			String endDay =  arrOfStr[1];
			String queryString = "millproductionid,ccmcummulative,ccmdowntimebd,ccmdowntimedelay,"+
			"rmoneproductionsize,ccmstand,downtimemissroll,milldowntimebd,milldowntimedelay, milldowntimesequencebreak, "+
			" millproductiondate,millproductiontime,millsrollnos, powercutdowntime,totalproduction,unittype,itemtype,productionid, "+
			" rmoneproductionpcs,rmoneproductionmt,rmtwoproductionsize,rmtwoproductionpcs,rmtwoproductionmt,smsname,laboratoryname,ccmname,rmname,MillProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+","+
			"foremanid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.foremanid) as foremanname "+
			" ,croperatorid,(SELECT om.employeename FROM OperatorMaster om WHERE "+
			" om.operatormasterid = querytable.croperatorid) as croperatorname"	
			+returnQuery+ " AND TO_DATE(querytable.millproductiondate, 'YYYY-MM-DD') BETWEEN ?3 AND ?4 ORDER BY querytable.millproductiondate ASC";
			System.out.println(sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,SrmbUtilty.convertStringToDate(startDay));
			query.setParameter(4,SrmbUtilty.convertStringToDate(endDay));
			List<MillProduction> millProductionList = new ArrayList<MillProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					MillProduction newMillProduction = new MillProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newMillProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newMillProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newMillProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newMillProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newMillProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newMillProduction.setForemanid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setForemanname(newObject.toString());
						}else if(objectNDX == 20) {
							newMillProduction.setCroperatorid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCroperatorname(newObject.toString());
						}else if(objectNDX == 22) {
							newMillProduction.setMillproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newMillProduction.setCcmcummulative(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimebd(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmdowntimedelay(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionsize(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setCcmstand(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setDowntimemissroll(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimebd(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimedelay(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMilldowntimesequencebreak(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiondate(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setMillproductiontime(newObject.toString());
						}else if(objectNDX == 34) {
							newMillProduction.setMillsrollnos(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setPowercutdowntime(newObject.toString());
						}else if(objectNDX == 36) {
							newMillProduction.setTotalproduction(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setUnittype(newObject.toString());
						}else if(objectNDX == 38) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setItemtype(newObject.toString());
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setProductionid(newObject.toString());
						} else if(objectNDX == 40) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmoneproductionpcs(newObject.toString());
						} else if(objectNDX == 41) {							
							newMillProduction.setRmoneproductionmt(Double.valueOf(newObject.toString()));							
						} else if(objectNDX == 42) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionsize(newObject.toString());
						} else if(objectNDX == 43) {
							if(newObject == null){
								newObject = "";
							}
							newMillProduction.setRmtwoproductionpcs(newObject.toString());
						} else if(objectNDX == 44) {
							newMillProduction.setRmtwoproductionmt(Double.valueOf(newObject.toString()));
						}
					}
					millProductionList.add(newMillProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return millProductionList;
		}else{
			
			List<MillProduction> millProductionList = null;
			return millProductionList;
		}
		
	}

	@Override
	public void deleteMillProduction(MillProduction millProduction) {
		MillProduction newMillProduction = findById(millProduction.getMillproductionid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<MillProduction> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaUpdate.from(MillProduction.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(millProductionRoot.get("millproductionid"), newMillProduction.getMillproductionid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public MillProduction findDetailsByMobileNo(MillProduction millProduction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MillProduction findDetailsByEmail(MillProduction millProduction) {
		MillProduction newMillProduction = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MillProduction> criteriaQuery = criteriaBuilder.createQuery(MillProduction.class);
		Root<MillProduction> millProductionRoot = criteriaQuery.from(MillProduction.class);
		criteriaQuery.select(millProductionRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(millProductionRoot.get("productiondate"), millProduction.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(millProductionRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(millProductionRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<MillProduction> typedQuery = entityManager.createQuery(criteriaQuery);
		List<MillProduction> millProductionList = typedQuery.getResultList();
		if(millProductionList.size() != 0){
			newMillProduction = (MillProduction)millProductionList.get(0);
		}
		return newMillProduction;
	}

	
	
}
