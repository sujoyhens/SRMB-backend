package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.SmsSummary;
import com.boenci.srmb.repository.production.SmsSummaryRepository;
import com.boenci.srmb.service.production.SmsSummaryService;
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
public class SmsSummaryServiceImpl implements SmsSummaryService {

	@Autowired
	private SmsSummaryRepository smsSummaryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SmsSummary findById(long id) {
		SmsSummary newSmsSummary = null;
		newSmsSummary =smsSummaryRepository.findById(id).get();		
		return newSmsSummary;
	}

	@Override
	public SmsSummary validSmsSummary(SmsSummary smsSummary) {
		SmsSummary newSmsSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsSummary> criteriaQuery = criteriaBuilder.createQuery(SmsSummary.class);
		Root<SmsSummary> smsSummaryRoot = criteriaQuery.from(SmsSummary.class);
		criteriaQuery.select(smsSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(smsSummaryRoot.get("productiondate"), smsSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(smsSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(smsSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SmsSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SmsSummary> smsSummaryList = typedQuery.getResultList();
		if(smsSummaryList.size() != 0){
			newSmsSummary = (SmsSummary)smsSummaryList.get(0);
		}
		return newSmsSummary;
	}

	@Override
	public SmsSummary save(SmsSummary smsSummary) {
		SmsSummary newSmsSummary = null;
		newSmsSummary = smsSummaryRepository.save(smsSummary);
		return newSmsSummary;
	}

	@Transactional
	@Override
	public SmsSummary update(SmsSummary smsSummary) {
		long smsSummaryId = smsSummary.getSmssummaryid();
		SmsSummary newSmsSummary = null;
		if(smsSummaryRepository.existsById(smsSummaryId)){
			newSmsSummary = smsSummaryRepository.save(smsSummary);
			return newSmsSummary;
		}else{
			return newSmsSummary;
		}
		
	}

	@Transactional
	@Override
	public String updateSmsSummary(SmsSummary smsSummary) {
		
		SmsSummary validateSmsSummary = validSmsSummary(smsSummary);
		if(validateSmsSummary == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<SmsSummary> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(SmsSummary.class);
			Root<SmsSummary> smsSummaryRoot = criteriaUpdate.from(SmsSummary.class);
			criteriaUpdate.set("productiondate", smsSummary.getProductiondate());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", smsSummary.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(smsSummaryRoot.get("smssummaryid"), smsSummary.getSmssummaryid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<SmsSummary> findAllSmsSummary() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsSummary> criteriaQuery = criteriaBuilder.createQuery(SmsSummary.class);
		Root<SmsSummary> smsSummaryRoot = criteriaQuery.from(SmsSummary.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(smsSummaryRoot.get("unitname")));
		List<SmsSummary> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isSmsSummaryExist(SmsSummary smsSummary) {
		
		return true;
	}

	@Override
	public List<SmsSummary> findSmsSummaryBySearchType(SmsSummary smsSummary, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsSummary> criteriaQuery = criteriaBuilder.createQuery(SmsSummary.class);
		Root<SmsSummary> smsSummaryRoot = criteriaQuery.from(SmsSummary.class);
		criteriaQuery.select(smsSummaryRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "smssummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" multiplyingfactor, totalbilletnos, totalbilletsize, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, totalbilletweight, smsproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,hotoutccmnos,hotoutccmsize,hotoutccmweight,"+
			"hotoutrmnos,hotoutrmsize,hotoutrmweight,hotoutpowernos,hotoutpowersize,hotoutpowerweight,hotcharging,SmsSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<SmsSummary> smsSummaryList = new ArrayList<SmsSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsSummary newSmsSummary = new SmsSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsSummary.setSmssummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newSmsSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newSmsSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newSmsSummary.setMultiplyingfactor(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newSmsSummary.setTotalbilletnos(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newSmsSummary.setTotalbilletsize(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newSmsSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newSmsSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newSmsSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							newSmsSummary.setTotalbilletweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsSummary.setSmsproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newSmsSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newSmsSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newSmsSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUnittype(newObject.toString());
						} else if(objectNDX == 38) {
							newSmsSummary.setHotoutccmnos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 39) {
							newSmsSummary.setHotoutccmsize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 40) {
							newSmsSummary.setHotoutccmweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 41) {
							newSmsSummary.setHotoutrmnos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 42) {
							newSmsSummary.setHotoutrmsize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 43) {
							newSmsSummary.setHotoutrmweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 44) {
							newSmsSummary.setHotoutpowernos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 45) {
							newSmsSummary.setHotoutpowersize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 46) {
							newSmsSummary.setHotoutpowerweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 47) {
							newSmsSummary.setHotcharging(Long.valueOf(newObject.toString()));
						}
					}
					smsSummaryList.add(newSmsSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsSummaryList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(smsSummaryRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(smsSummaryRoot.get("status"), smsSummary.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<SmsSummary> typedQuery = entityManager.createQuery(criteriaQuery);
			List<SmsSummary> smsSummaryList = typedQuery.getResultList();
			return smsSummaryList;
			
		}else if(searchType.equalsIgnoreCase("productiondate")){
			String queryString = "smssummaryid,downtime, electricaldowntime, hotchargingpercentage, "+
			" multiplyingfactor, totalbilletnos, totalbilletsize, lrfdowntime, mechanicaldowntime,"+
			" nooffurnacesequencebreak, noofpowersequencebreak, noofsequencebreak, powercutdowntime, "+
			" productiondate, totalbilletweight, smsproduction, totalfurnacesequencebreaktime, "+
			" totalpowersequencebreaktime, totalsequencebreaktime,unittype,hotoutccmnos,hotoutccmsize,hotoutccmweight,"+
			"hotoutrmnos,hotoutrmsize,hotoutrmweight,hotoutpowernos,hotoutpowersize,hotoutpowerweight,hotcharging,SmsSummary";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.productiondate = ?3";
			System.out.println("======  "+sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,smsSummary.getProductiondate());
			List<SmsSummary> smsSummaryList = new ArrayList<SmsSummary>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsSummary newSmsSummary = new SmsSummary();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsSummary.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsSummary.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsSummary.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsSummary.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsSummary.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsSummary.setSmssummaryid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsSummary.setDowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							newSmsSummary.setElectricaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							newSmsSummary.setHotchargingpercentage(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							newSmsSummary.setMultiplyingfactor(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							newSmsSummary.setTotalbilletnos(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							newSmsSummary.setTotalbilletsize(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 25) {
							newSmsSummary.setLrfdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 26) {
							newSmsSummary.setMechanicaldowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 27) {
							newSmsSummary.setNooffurnacesequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsSummary.setNoofpowersequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsSummary.setNoofsequencebreak(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsSummary.setPowercutdowntime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setProductiondate(newObject.toString());
						}else if(objectNDX == 32) {
							newSmsSummary.setTotalbilletweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsSummary.setSmsproduction(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							newSmsSummary.setTotalfurnacesequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 35) {
							newSmsSummary.setTotalpowersequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 36) {
							newSmsSummary.setTotalsequencebreaktime(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 37) {
							if(newObject == null){
								newObject = "";
							}
							newSmsSummary.setUnittype(newObject.toString());
						} else if(objectNDX == 38) {
							newSmsSummary.setHotoutccmnos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 39) {
							newSmsSummary.setHotoutccmsize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 40) {
							newSmsSummary.setHotoutccmweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 41) {
							newSmsSummary.setHotoutrmnos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 42) {
							newSmsSummary.setHotoutrmsize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 43) {
							newSmsSummary.setHotoutrmweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 44) {
							newSmsSummary.setHotoutpowernos(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 45) {
							newSmsSummary.setHotoutpowersize(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 46) {
							newSmsSummary.setHotoutpowerweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 47) {
							newSmsSummary.setHotcharging(Long.valueOf(newObject.toString()));
						}
					}
					smsSummaryList.add(newSmsSummary);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsSummaryList;
		}else{
			List<SmsSummary> smsSummaryList = null;
			return smsSummaryList;
		}
		
	}

	@Override
	public void deleteSmsSummary(SmsSummary smsSummary) {
		SmsSummary newSmsSummary = findById(smsSummary.getSmssummaryid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<SmsSummary> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(SmsSummary.class);
		Root<SmsSummary> smsSummaryRoot = criteriaUpdate.from(SmsSummary.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(smsSummaryRoot.get("smssummaryid"), newSmsSummary.getSmssummaryid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public SmsSummary findDetailsByMobileNo(SmsSummary smsSummary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmsSummary findDetailsByEmail(SmsSummary smsSummary) {
		SmsSummary newSmsSummary = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsSummary> criteriaQuery = criteriaBuilder.createQuery(SmsSummary.class);
		Root<SmsSummary> smsSummaryRoot = criteriaQuery.from(SmsSummary.class);
		criteriaQuery.select(smsSummaryRoot);
		Predicate predicateEmail = criteriaBuilder.equal(smsSummaryRoot.get("productiondate"), smsSummary.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(smsSummaryRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(smsSummaryRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SmsSummary> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SmsSummary> smsSummaryList = typedQuery.getResultList();
		if(smsSummaryList.size() != 0){
			newSmsSummary = (SmsSummary)smsSummaryList.get(0);
		}
		return newSmsSummary;
	}
	
}
