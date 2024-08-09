package com.boenci.srmb.serviceImpl.production;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.RmoneBilletBypass;
import com.boenci.srmb.repository.production.RmoneBilletBypassRepository;
import com.boenci.srmb.service.production.RmoneBilletBypassService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class RmoneBilletBypassServiceImpl implements RmoneBilletBypassService {

	@Autowired
	private RmoneBilletBypassRepository rmoneBilletBypassRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RmoneBilletBypass findById(long id) {
		RmoneBilletBypass newRmoneBilletBypass = null;
		newRmoneBilletBypass = rmoneBilletBypassRepository.findById(id).get();		
		return newRmoneBilletBypass;
	}

	@Override
	public RmoneBilletBypass validRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass) {
		RmoneBilletBypass newRmoneBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmoneBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaQuery.from(RmoneBilletBypass.class);
		criteriaQuery.select(rmoneBilletBypassRoot);
		Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmoneBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmoneBilletBypass> rmoneBilletBypassList = typedQuery.getResultList();
		if(rmoneBilletBypassList.size() != 0){
			newRmoneBilletBypass = (RmoneBilletBypass)rmoneBilletBypassList.get(0);
		}
		return newRmoneBilletBypass;
	}

	@Override
	public RmoneBilletBypass save(RmoneBilletBypass rmoneBilletBypass) {
		RmoneBilletBypass newRmoneBilletBypass = null;
		newRmoneBilletBypass = rmoneBilletBypassRepository.save(rmoneBilletBypass);
		return newRmoneBilletBypass;
	}

	@Transactional
	@Override
	public RmoneBilletBypass update(RmoneBilletBypass rmoneBilletBypass) {
		long rmoneBilletBypassId = rmoneBilletBypass.getRmonebilletbypassid();
		RmoneBilletBypass newRmoneBilletBypass = null;
		if(rmoneBilletBypassRepository.existsById(rmoneBilletBypassId)){
			newRmoneBilletBypass = rmoneBilletBypassRepository.save(rmoneBilletBypass);
			return newRmoneBilletBypass;
		}else{
			return newRmoneBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public RmoneBilletBypass delete(RmoneBilletBypass rmoneBilletBypass) {
		long rmoneBilletBypassId = rmoneBilletBypass.getRmonebilletbypassid();
		RmoneBilletBypass newRmoneBilletBypass = null;
		if(rmoneBilletBypassRepository.existsById(rmoneBilletBypassId)){
			newRmoneBilletBypass = rmoneBilletBypassRepository.save(rmoneBilletBypass);
			return newRmoneBilletBypass;
		}else{
			return newRmoneBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public String updateRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RmoneBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaUpdate.from(RmoneBilletBypass.class);
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", rmoneBilletBypass.getStatus());
		criteriaUpdate.set("action", "update");
		Predicate predicateRmoneBilletBypassId = criteriaBuilder.equal(rmoneBilletBypassRoot.get("rmonebilletbypassid"), rmoneBilletBypass.getRmonebilletbypassid());
		Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateRmoneBilletBypassId,predicateStatus,predicateDelete);
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
	public List<RmoneBilletBypass> findAllRmoneBilletBypass() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmoneBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaQuery.from(RmoneBilletBypass.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(rmoneBilletBypassRoot.get("rmonebilletbypassdate")));
		List<RmoneBilletBypass> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRmoneBilletBypassExist(RmoneBilletBypass rmoneBilletBypass) {
		
		return true;
	}

	@Override
	public List<RmoneBilletBypass> findRmoneBilletBypassBySearchType(RmoneBilletBypass rmoneBilletBypass, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmoneBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaQuery.from(RmoneBilletBypass.class);
		criteriaQuery.select(rmoneBilletBypassRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), rmoneBilletBypass.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}else if(searchType.equalsIgnoreCase("coldbilletbypassid")){
			Predicate predicatePlanteMasterid = criteriaBuilder.equal(rmoneBilletBypassRoot.get("coldbilletbypassid"), rmoneBilletBypass.getColdbilletbypassid());
			Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicatePlanteMasterid,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmoneBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmoneBilletBypass> rmoneBilletBypassList = typedQuery.getResultList();
		return rmoneBilletBypassList;
	}

	@Override
	public void deleteRmoneBilletBypass(RmoneBilletBypass rmoneBilletBypass) {
		RmoneBilletBypass newRmoneBilletBypass = findById(rmoneBilletBypass.getRmonebilletbypassid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RmoneBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaUpdate.from(RmoneBilletBypass.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(rmoneBilletBypassRoot.get("rmonebilletbypassid"), newRmoneBilletBypass.getRmonebilletbypassid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public RmoneBilletBypass findDetailsByMobileNo(RmoneBilletBypass rmoneBilletBypass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RmoneBilletBypass findDetailsByEmail(RmoneBilletBypass rmoneBilletBypass) {
		RmoneBilletBypass newRmoneBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmoneBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmoneBilletBypass.class);
		Root<RmoneBilletBypass> rmoneBilletBypassRoot = criteriaQuery.from(RmoneBilletBypass.class);
		criteriaQuery.select(rmoneBilletBypassRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(rmoneBilletBypassRoot.get("productiondate"), rmoneBilletBypass.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(rmoneBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmoneBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmoneBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmoneBilletBypass> rmoneBilletBypassList = typedQuery.getResultList();
		if(rmoneBilletBypassList.size() != 0){
			newRmoneBilletBypass = (RmoneBilletBypass)rmoneBilletBypassList.get(0);
		}
		return newRmoneBilletBypass;
	}

	
	
}
