package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.BearingMaster;
import com.boenci.srmb.repository.BearingMasterRepository;
import com.boenci.srmb.service.BearingMasterService;
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
public class BearingMasterServiceImpl implements BearingMasterService {

	@Autowired
	private BearingMasterRepository bearingMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public BearingMaster findById(long id) {
		BearingMaster newBearingMaster = null;
		newBearingMaster =bearingMasterRepository.findById(id).get();		
		return newBearingMaster;
	}

	@Override
	public BearingMaster validBearingMaster(BearingMaster bearingMaster) {
		BearingMaster newBearingMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BearingMaster> criteriaQuery = criteriaBuilder.createQuery(BearingMaster.class);
		Root<BearingMaster> bearingMasterRoot = criteriaQuery.from(BearingMaster.class);
		criteriaQuery.select(bearingMasterRoot);
		Predicate predicateUnitmasterid = criteriaBuilder.equal(bearingMasterRoot.get("unitmasterid"), bearingMaster.getUnitmasterid());
		Predicate predicateUnittype = criteriaBuilder.equal(bearingMasterRoot.get("unittype"), bearingMaster.getUnittype());
		Predicate predicateBearingname = criteriaBuilder.equal(bearingMasterRoot.get("bearingno"), bearingMaster.getBearingno());
		Predicate predicateStatus = criteriaBuilder.equal(bearingMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(bearingMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateUnitmasterid,predicateUnittype,predicateBearingname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<BearingMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<BearingMaster> bearingMasterList = typedQuery.getResultList();
		if(bearingMasterList.size() != 0){
			newBearingMaster = (BearingMaster)bearingMasterList.get(0);
		}
		return newBearingMaster;
	}

	@Override
	public BearingMaster save(BearingMaster bearingMaster) {
		BearingMaster newBearingMaster = null;
		newBearingMaster = bearingMasterRepository.save(bearingMaster);
		return newBearingMaster;
	}

	@Transactional
	@Override
	public BearingMaster update(BearingMaster bearingMaster) {
		long bearingMasterId = bearingMaster.getBearingmasterid();
		BearingMaster newBearingMaster = null;
		if(bearingMasterRepository.existsById(bearingMasterId)){
			newBearingMaster = bearingMasterRepository.save(bearingMaster);
			return newBearingMaster;
		}else{
			return newBearingMaster;
		}
	}

	@Transactional
	@Override
	public BearingMaster delete(BearingMaster bearingMaster) {
		long bearingMasterId = bearingMaster.getBearingmasterid();
		BearingMaster newBearingMaster = null;
		if(bearingMasterRepository.existsById(bearingMasterId)){
			newBearingMaster = bearingMasterRepository.save(bearingMaster);
			return newBearingMaster;
		}else{
			return newBearingMaster;
		}
	}

	@Transactional
	@Override
	public String updateBearingMaster(BearingMaster bearingMaster) {
		
		BearingMaster validateBearingMaster = validBearingMaster(bearingMaster);
		if(validateBearingMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<BearingMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(BearingMaster.class);
			Root<BearingMaster> bearingMasterRoot = criteriaUpdate.from(BearingMaster.class);
			criteriaUpdate.set("bearingno", bearingMaster.getBearingno());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", bearingMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(bearingMasterRoot.get("bearingmasterid"), bearingMaster.getBearingmasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<BearingMaster> findAllBearingMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BearingMaster> criteriaQuery = criteriaBuilder.createQuery(BearingMaster.class);
		Root<BearingMaster> bearingMasterRoot = criteriaQuery.from(BearingMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(bearingMasterRoot.get("bearingname")));
		List<BearingMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isBearingMasterExist(BearingMaster bearingMaster) {
		
		return true;
	}

	@Override
	public List<BearingMaster> findBearingMasterBySearchType(BearingMaster bearingMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BearingMaster> criteriaQuery = criteriaBuilder.createQuery(BearingMaster.class);
		Root<BearingMaster> bearingMasterRoot = criteriaQuery.from(BearingMaster.class);
		criteriaQuery.select(bearingMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "bearingmasterid,actualchangeoverdate,bearinglife,bearingno,bearingstatus,"+
			"bearingtype,installationdate,lastchangeoverdate,make,remarks,schedulechangeoverdate,standname,unittype,side,position,BearingMaster";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<BearingMaster> bearingMasterList = new ArrayList<BearingMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					BearingMaster newBearingMaster = new BearingMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newBearingMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newBearingMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newBearingMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newBearingMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newBearingMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newBearingMaster.setBearingmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setActualchangeoverdate(newObject.toString());
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setBearinglife(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setBearingno(newObject.toString());
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setBearingstatus(newObject.toString());
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
						newBearingMaster.setBearingtype(newObject.toString());
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
						newBearingMaster.setInstallationdate(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setLastchangeoverdate(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setMake(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setSchedulechangeoverdate(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setStandname(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setSide(newObject.toString());
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newBearingMaster.setPosition(newObject.toString());
						}
					}
					bearingMasterList.add(newBearingMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return bearingMasterList;
			
		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(bearingMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(bearingMasterRoot.get("status"), bearingMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<BearingMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<BearingMaster> bearingMasterList = typedQuery.getResultList();
			return bearingMasterList;
		}else if(searchType.equalsIgnoreCase("unitmasterid")){
			List<BearingMaster> bearingMasterList = null;
			return bearingMasterList;	

		}else{
			
			List<BearingMaster> bearingMasterList = null;
			return bearingMasterList;			
		}
		
	}
	@Transactional
	@Override
	public void deleteBearingMaster(BearingMaster bearingMaster) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<BearingMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(BearingMaster.class);
		Root<BearingMaster> bearingMasterRoot = criteriaUpdate.from(BearingMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(bearingMasterRoot.get("bearingmasterid"), bearingMaster.getBearingmasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public BearingMaster findDetailsByMobileNo(BearingMaster bearingMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BearingMaster findDetailsByEmail(BearingMaster bearingMaster) {
		BearingMaster newBearingMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BearingMaster> criteriaQuery = criteriaBuilder.createQuery(BearingMaster.class);
		Root<BearingMaster> bearingMasterRoot = criteriaQuery.from(BearingMaster.class);
		criteriaQuery.select(bearingMasterRoot);
		Predicate predicateStatus = criteriaBuilder.equal(bearingMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(bearingMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<BearingMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<BearingMaster> bearingMasterList = typedQuery.getResultList();
		if(bearingMasterList.size() != 0){
			newBearingMaster = (BearingMaster)bearingMasterList.get(0);
		}
		return newBearingMaster;
	}

	
}
