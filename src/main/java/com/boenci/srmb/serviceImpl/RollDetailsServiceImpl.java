package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.RollDetails;
import com.boenci.srmb.repository.RollDetailsRepository;
import com.boenci.srmb.service.RollDetailsService;
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
public class RollDetailsServiceImpl implements RollDetailsService {

	@Autowired
	private RollDetailsRepository rollDetailsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RollDetails findById(long id) {
		RollDetails newRollDetails = null;
		newRollDetails =rollDetailsRepository.findById(id).get();		
		return newRollDetails;
	}

	@Override
	public RollDetails validRollDetails(RollDetails rollDetails) {
		RollDetails newRollDetails = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RollDetails> criteriaQuery = criteriaBuilder.createQuery(RollDetails.class);
		Root<RollDetails> rollDetailsRoot = criteriaQuery.from(RollDetails.class);
		criteriaQuery.select(rollDetailsRoot);
		Predicate predicateUnitmasterid = criteriaBuilder.equal(rollDetailsRoot.get("unitmasterid"), rollDetails.getUnitmasterid());
		Predicate predicateUnittype = criteriaBuilder.equal(rollDetailsRoot.get("areamasterid"), rollDetails.getAreamasterid());
		Predicate predicateAreaname = criteriaBuilder.equal(rollDetailsRoot.get("standname"), rollDetails.getStandname());
		Predicate predicateStatus = criteriaBuilder.equal(rollDetailsRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rollDetailsRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateUnitmasterid,predicateUnittype,predicateAreaname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RollDetails> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RollDetails> rollDetailsList = typedQuery.getResultList();
		if(rollDetailsList.size() != 0){
			newRollDetails = (RollDetails)rollDetailsList.get(0);
		}
		return newRollDetails;
	}

	@Override
	public RollDetails save(RollDetails rollDetails) {
		RollDetails newRollDetails = null;
		newRollDetails = rollDetailsRepository.save(rollDetails);
		return newRollDetails;
	}

	@Transactional
	@Override
	public RollDetails update(RollDetails rollDetails) {
		long rollDetailsId = rollDetails.getRolldetailsid();
		RollDetails newRollDetails = null;
		if(rollDetailsRepository.existsById(rollDetailsId)){
			newRollDetails = rollDetailsRepository.save(rollDetails);
			return newRollDetails;
		}else{
			return newRollDetails;
		}
	}

	@Transactional
	@Override
	public RollDetails delete(RollDetails rollDetails) {
		long rollDetailsId = rollDetails.getRolldetailsid();
		RollDetails newRollDetails = null;
		if(rollDetailsRepository.existsById(rollDetailsId)){
			newRollDetails = rollDetailsRepository.save(rollDetails);
			return newRollDetails;
		}else{
			return newRollDetails;
		}
	}

	@Transactional
	@Override
	public String updateRollDetails(RollDetails rollDetails) {
		
		RollDetails validateRollDetails = validRollDetails(rollDetails);
		if(validateRollDetails == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<RollDetails> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(RollDetails.class);
			Root<RollDetails> rollDetailsRoot = criteriaUpdate.from(RollDetails.class);
			//criteriaUpdate.set("standardmasterid", rollDetails.getStandardmasterid());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", rollDetails.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(rollDetailsRoot.get("rolldetailsid"), rollDetails.getRolldetailsid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<RollDetails> findAllRollDetails() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RollDetails> criteriaQuery = criteriaBuilder.createQuery(RollDetails.class);
		Root<RollDetails> rollDetailsRoot = criteriaQuery.from(RollDetails.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(rollDetailsRoot.get("stand")));
		List<RollDetails> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRollDetailsExist(RollDetails rollDetails) {
		
		return true;
	}

	@Override
	public List<RollDetails> findRollDetailsBySearchType(RollDetails rollDetails, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RollDetails> criteriaQuery = criteriaBuilder.createQuery(RollDetails.class);
		Root<RollDetails> rollDetailsRoot = criteriaQuery.from(RollDetails.class);
		criteriaQuery.select(rollDetailsRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "rolldetailsid, actualchangeoverdate, bd, bl, changeoverdia, nopass, passtype, remarks, rollerdia, rollerlife, rollerno, rollerstatus, rollmax, rollmin, schedulechangeoverdate, standname,unittype,RollDetails";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;
			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<RollDetails> rollDetailsList = new ArrayList<RollDetails>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					RollDetails newRollDetails = new RollDetails();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newRollDetails.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newRollDetails.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newRollDetails.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newRollDetails.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newRollDetails.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newRollDetails.setRolldetailsid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setActualchangeoverdate(newObject.toString());
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setBd(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setBl(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setChangeoverdia(newObject.toString());
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
						newRollDetails.setNopass(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
						newRollDetails.setPasstype(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRemarks(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollerdia(newObject.toString());
						}else if(objectNDX == 27) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollerlife(newObject.toString());
						}else if(objectNDX == 28) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollerno(newObject.toString());
						}else if(objectNDX == 29) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollerstatus(newObject.toString());
						}else if(objectNDX == 30) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollmax(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setRollmin(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setSchedulechangeoverdate(newObject.toString());
						}else if(objectNDX == 33) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setStandname(newObject.toString());
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newRollDetails.setUnittype(newObject.toString());
						}
					}
					rollDetailsList.add(newRollDetails);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return rollDetailsList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(rollDetailsRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rollDetailsRoot.get("status"), rollDetails.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<RollDetails> typedQuery = entityManager.createQuery(criteriaQuery);
			List<RollDetails> rollDetailsList = typedQuery.getResultList();
			return rollDetailsList;

		}else if(searchType.equalsIgnoreCase("areamasterid")){
			Predicate predicateUnitMasterId = criteriaBuilder.equal(rollDetailsRoot.get("areamasterid"), rollDetails.getAreamasterid());
			Predicate predicateDelete = criteriaBuilder.equal(rollDetailsRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rollDetailsRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateUnitMasterId,predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<RollDetails> typedQuery = entityManager.createQuery(criteriaQuery);
			List<RollDetails> rollDetailsList = typedQuery.getResultList();
			return rollDetailsList;			

		}else{
			
			List<RollDetails> rollDetailsList = null;
			return rollDetailsList;
		}
		
	}
	@Transactional
	@Override
	public void deleteRollDetails(RollDetails rollDetails) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RollDetails> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RollDetails.class);
		Root<RollDetails> rollDetailsRoot = criteriaUpdate.from(RollDetails.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(rollDetailsRoot.get("rolldetailsid"), rollDetails.getRolldetailsid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public RollDetails findDetailsByMobileNo(RollDetails rollDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RollDetails findDetailsByEmail(RollDetails rollDetails) {
		RollDetails newRollDetails = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RollDetails> criteriaQuery = criteriaBuilder.createQuery(RollDetails.class);
		Root<RollDetails> rollDetailsRoot = criteriaQuery.from(RollDetails.class);
		criteriaQuery.select(rollDetailsRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(rollDetailsRoot.get("standardmasterid"), rollDetails.getStandardmasterid());
		Predicate predicateStatus = criteriaBuilder.equal(rollDetailsRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rollDetailsRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RollDetails> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RollDetails> rollDetailsList = typedQuery.getResultList();
		if(rollDetailsList.size() != 0){
			newRollDetails = (RollDetails)rollDetailsList.get(0);
		}
		return newRollDetails;
	}

	
}
