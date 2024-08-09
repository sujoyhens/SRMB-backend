package com.boenci.srmb.serviceImpl.production;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.CcmBilletBypass;
import com.boenci.srmb.repository.production.CcmBilletBypassRepository;
import com.boenci.srmb.service.production.CcmBilletBypassService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class CcmBilletBypassServiceImpl implements CcmBilletBypassService {

	@Autowired
	private CcmBilletBypassRepository ccmBilletBypassRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CcmBilletBypass findById(long id) {
		CcmBilletBypass newCcmBilletBypass = null;
		newCcmBilletBypass = ccmBilletBypassRepository.findById(id).get();		
		return newCcmBilletBypass;
	}

	@Override
	public CcmBilletBypass validCcmBilletBypass(CcmBilletBypass ccmBilletBypass) {
		CcmBilletBypass newCcmBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmBilletBypass> criteriaQuery = criteriaBuilder.createQuery(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaQuery.from(CcmBilletBypass.class);
		criteriaQuery.select(ccmBilletBypassRoot);
		Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<CcmBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<CcmBilletBypass> ccmBilletBypassList = typedQuery.getResultList();
		if(ccmBilletBypassList.size() != 0){
			newCcmBilletBypass = (CcmBilletBypass)ccmBilletBypassList.get(0);
		}
		return newCcmBilletBypass;
	}

	@Override
	public CcmBilletBypass save(CcmBilletBypass ccmBilletBypass) {
		CcmBilletBypass newCcmBilletBypass = null;
		newCcmBilletBypass = ccmBilletBypassRepository.save(ccmBilletBypass);
		return newCcmBilletBypass;
	}

	@Transactional
	@Override
	public CcmBilletBypass update(CcmBilletBypass ccmBilletBypass) {
		long ccmBilletBypassId = ccmBilletBypass.getCcmbilletbypassid();
		CcmBilletBypass newCcmBilletBypass = null;
		if(ccmBilletBypassRepository.existsById(ccmBilletBypassId)){
			newCcmBilletBypass = ccmBilletBypassRepository.save(ccmBilletBypass);
			return newCcmBilletBypass;
		}else{
			return newCcmBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public CcmBilletBypass delete(CcmBilletBypass ccmBilletBypass) {
		long ccmBilletBypassId = ccmBilletBypass.getCcmbilletbypassid();
		CcmBilletBypass newCcmBilletBypass = null;
		if(ccmBilletBypassRepository.existsById(ccmBilletBypassId)){
			newCcmBilletBypass = ccmBilletBypassRepository.save(ccmBilletBypass);
			return newCcmBilletBypass;
		}else{
			return newCcmBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public String updateCcmBilletBypass(CcmBilletBypass ccmBilletBypass) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<CcmBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaUpdate.from(CcmBilletBypass.class);
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", ccmBilletBypass.getStatus());
		criteriaUpdate.set("action", "update");
		Predicate predicateCcmBilletBypassId = criteriaBuilder.equal(ccmBilletBypassRoot.get("ccmbilletbypassid"), ccmBilletBypass.getCcmbilletbypassid());
		Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateCcmBilletBypassId,predicateStatus,predicateDelete);
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
	public List<CcmBilletBypass> findAllCcmBilletBypass() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmBilletBypass> criteriaQuery = criteriaBuilder.createQuery(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaQuery.from(CcmBilletBypass.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(ccmBilletBypassRoot.get("ccmbilletbypassdate")));
		List<CcmBilletBypass> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isCcmBilletBypassExist(CcmBilletBypass ccmBilletBypass) {
		
		return true;
	}

	@Override
	public List<CcmBilletBypass> findCcmBilletBypassBySearchType(CcmBilletBypass ccmBilletBypass, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmBilletBypass> criteriaQuery = criteriaBuilder.createQuery(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaQuery.from(CcmBilletBypass.class);
		criteriaQuery.select(ccmBilletBypassRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), ccmBilletBypass.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}else if(searchType.equalsIgnoreCase("coldbilletbypassid")){
			Predicate predicatePlanteMasterid = criteriaBuilder.equal(ccmBilletBypassRoot.get("coldbilletbypassid"), ccmBilletBypass.getColdbilletbypassid());
			Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicatePlanteMasterid,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<CcmBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<CcmBilletBypass> ccmBilletBypassList = typedQuery.getResultList();
		return ccmBilletBypassList;
	}

	@Override
	public void deleteCcmBilletBypass(CcmBilletBypass ccmBilletBypass) {
		CcmBilletBypass newCcmBilletBypass = findById(ccmBilletBypass.getCcmbilletbypassid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<CcmBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaUpdate.from(CcmBilletBypass.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(ccmBilletBypassRoot.get("ccmbilletbypassid"), newCcmBilletBypass.getCcmbilletbypassid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public CcmBilletBypass findDetailsByMobileNo(CcmBilletBypass ccmBilletBypass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CcmBilletBypass findDetailsByEmail(CcmBilletBypass ccmBilletBypass) {
		CcmBilletBypass newCcmBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CcmBilletBypass> criteriaQuery = criteriaBuilder.createQuery(CcmBilletBypass.class);
		Root<CcmBilletBypass> ccmBilletBypassRoot = criteriaQuery.from(CcmBilletBypass.class);
		criteriaQuery.select(ccmBilletBypassRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(ccmBilletBypassRoot.get("productiondate"), ccmBilletBypass.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(ccmBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(ccmBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<CcmBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<CcmBilletBypass> ccmBilletBypassList = typedQuery.getResultList();
		if(ccmBilletBypassList.size() != 0){
			newCcmBilletBypass = (CcmBilletBypass)ccmBilletBypassList.get(0);
		}
		return newCcmBilletBypass;
	}

	
	
}
