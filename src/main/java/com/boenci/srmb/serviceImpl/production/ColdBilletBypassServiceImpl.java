package com.boenci.srmb.serviceImpl.production;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.ColdBilletBypass;
import com.boenci.srmb.repository.production.ColdBilletBypassRepository;
import com.boenci.srmb.service.production.ColdBilletBypassService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ColdBilletBypassServiceImpl implements ColdBilletBypassService {

	@Autowired
	private ColdBilletBypassRepository coldBilletBypassRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ColdBilletBypass findById(long id) {
		ColdBilletBypass newColdBilletBypass = null;
		newColdBilletBypass = coldBilletBypassRepository.findById(id).get();		
		return newColdBilletBypass;
	}

	@Override
	public ColdBilletBypass validColdBilletBypass(ColdBilletBypass coldBilletBypass) {
		ColdBilletBypass newColdBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ColdBilletBypass> criteriaQuery = criteriaBuilder.createQuery(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaQuery.from(ColdBilletBypass.class);
		criteriaQuery.select(coldBilletBypassRoot);
		Predicate predicateProductionDate = criteriaBuilder.equal(coldBilletBypassRoot.get("coldbilletbypassdate"), coldBilletBypass.getColdbilletbypassdate());
		Predicate predicateProductionTime = criteriaBuilder.equal(coldBilletBypassRoot.get("coldbilletbypasstime"), coldBilletBypass.getColdbilletbypasstime());
		Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateProductionDate,predicateProductionTime,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ColdBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ColdBilletBypass> coldBilletBypassList = typedQuery.getResultList();
		if(coldBilletBypassList.size() != 0){
			newColdBilletBypass = (ColdBilletBypass)coldBilletBypassList.get(0);
		}
		return newColdBilletBypass;
	}

	@Override
	public ColdBilletBypass save(ColdBilletBypass coldBilletBypass) {
		ColdBilletBypass newColdBilletBypass = null;
		newColdBilletBypass = coldBilletBypassRepository.save(coldBilletBypass);
		return newColdBilletBypass;
	}

	@Transactional
	@Override
	public ColdBilletBypass update(ColdBilletBypass coldBilletBypass) {
		long coldBilletBypassId = coldBilletBypass.getColdbilletbypassid();
		ColdBilletBypass newColdBilletBypass = null;
		if(coldBilletBypassRepository.existsById(coldBilletBypassId)){
			newColdBilletBypass = coldBilletBypassRepository.save(coldBilletBypass);
			return newColdBilletBypass;
		}else{
			return newColdBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public String updateColdBilletBypass(ColdBilletBypass coldBilletBypass) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ColdBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaUpdate.from(ColdBilletBypass.class);
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", coldBilletBypass.getStatus());
		criteriaUpdate.set("action", "update");
		Predicate predicateColdBilletBypassId = criteriaBuilder.equal(coldBilletBypassRoot.get("coldbilletbypassid"), coldBilletBypass.getColdbilletbypassid());
		Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateColdBilletBypassId,predicateStatus,predicateDelete);
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
	public List<ColdBilletBypass> findAllColdBilletBypass() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ColdBilletBypass> criteriaQuery = criteriaBuilder.createQuery(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaQuery.from(ColdBilletBypass.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(coldBilletBypassRoot.get("coldbilletbypassdate")));
		List<ColdBilletBypass> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isColdBilletBypassExist(ColdBilletBypass coldBilletBypass) {
		
		return true;
	}

	@Override
	public List<ColdBilletBypass> findColdBilletBypassBySearchType(ColdBilletBypass coldBilletBypass, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ColdBilletBypass> criteriaQuery = criteriaBuilder.createQuery(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaQuery.from(ColdBilletBypass.class);
		criteriaQuery.select(coldBilletBypassRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), coldBilletBypass.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}else if(searchType.equalsIgnoreCase("plantmasterid")){
			Predicate predicatePlanteMasterid = criteriaBuilder.equal(coldBilletBypassRoot.get("plantmasterid"), coldBilletBypass.getPlantmasterid());
			Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicatePlanteMasterid,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<ColdBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ColdBilletBypass> coldBilletBypassList = typedQuery.getResultList();
		return coldBilletBypassList;
	}

	@Override
	public void deleteColdBilletBypass(ColdBilletBypass coldBilletBypass) {
		ColdBilletBypass newColdBilletBypass = findById(coldBilletBypass.getColdbilletbypassid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ColdBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaUpdate.from(ColdBilletBypass.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(coldBilletBypassRoot.get("coldbilletbypassid"), newColdBilletBypass.getColdbilletbypassid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public ColdBilletBypass findDetailsByMobileNo(ColdBilletBypass coldBilletBypass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColdBilletBypass findDetailsByEmail(ColdBilletBypass coldBilletBypass) {
		ColdBilletBypass newColdBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ColdBilletBypass> criteriaQuery = criteriaBuilder.createQuery(ColdBilletBypass.class);
		Root<ColdBilletBypass> coldBilletBypassRoot = criteriaQuery.from(ColdBilletBypass.class);
		criteriaQuery.select(coldBilletBypassRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(coldBilletBypassRoot.get("productiondate"), coldBilletBypass.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(coldBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(coldBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ColdBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ColdBilletBypass> coldBilletBypassList = typedQuery.getResultList();
		if(coldBilletBypassList.size() != 0){
			newColdBilletBypass = (ColdBilletBypass)coldBilletBypassList.get(0);
		}
		return newColdBilletBypass;
	}

	
	
}
