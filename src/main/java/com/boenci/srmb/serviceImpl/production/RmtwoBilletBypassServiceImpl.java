package com.boenci.srmb.serviceImpl.production;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.RmtwoBilletBypass;
import com.boenci.srmb.repository.production.RmtwoBilletBypassRepository;
import com.boenci.srmb.service.production.RmtwoBilletBypassService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class RmtwoBilletBypassServiceImpl implements RmtwoBilletBypassService {

	@Autowired
	private RmtwoBilletBypassRepository rmtwoBilletBypassRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RmtwoBilletBypass findById(long id) {
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		newRmtwoBilletBypass = rmtwoBilletBypassRepository.findById(id).get();		
		return newRmtwoBilletBypass;
	}

	@Override
	public RmtwoBilletBypass validRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass) {
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmtwoBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaQuery.from(RmtwoBilletBypass.class);
		criteriaQuery.select(rmtwoBilletBypassRoot);
		Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmtwoBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmtwoBilletBypass> rmtwoBilletBypassList = typedQuery.getResultList();
		if(rmtwoBilletBypassList.size() != 0){
			newRmtwoBilletBypass = (RmtwoBilletBypass)rmtwoBilletBypassList.get(0);
		}
		return newRmtwoBilletBypass;
	}

	@Override
	public RmtwoBilletBypass save(RmtwoBilletBypass rmtwoBilletBypass) {
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		newRmtwoBilletBypass = rmtwoBilletBypassRepository.save(rmtwoBilletBypass);
		return newRmtwoBilletBypass;
	}

	@Transactional
	@Override
	public RmtwoBilletBypass update(RmtwoBilletBypass rmtwoBilletBypass) {
		long rmtwoBilletBypassId = rmtwoBilletBypass.getRmtwobilletbypassid();
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		if(rmtwoBilletBypassRepository.existsById(rmtwoBilletBypassId)){
			newRmtwoBilletBypass = rmtwoBilletBypassRepository.save(rmtwoBilletBypass);
			return newRmtwoBilletBypass;
		}else{
			return newRmtwoBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public RmtwoBilletBypass delete(RmtwoBilletBypass rmtwoBilletBypass) {
		long rmtwoBilletBypassId = rmtwoBilletBypass.getRmtwobilletbypassid();
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		if(rmtwoBilletBypassRepository.existsById(rmtwoBilletBypassId)){
			newRmtwoBilletBypass = rmtwoBilletBypassRepository.save(rmtwoBilletBypass);
			return newRmtwoBilletBypass;
		}else{
			return newRmtwoBilletBypass;
		}
		
	}

	@Transactional
	@Override
	public String updateRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass) {
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RmtwoBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaUpdate.from(RmtwoBilletBypass.class);
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", rmtwoBilletBypass.getStatus());
		criteriaUpdate.set("action", "update");
		Predicate predicateRmtwoBilletBypassId = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("rmtwobilletbypassid"), rmtwoBilletBypass.getRmtwobilletbypassid());
		Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateRmtwoBilletBypassId,predicateStatus,predicateDelete);
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
	public List<RmtwoBilletBypass> findAllRmtwoBilletBypass() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmtwoBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaQuery.from(RmtwoBilletBypass.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(rmtwoBilletBypassRoot.get("rmtwobilletbypassdate")));
		List<RmtwoBilletBypass> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRmtwoBilletBypassExist(RmtwoBilletBypass rmtwoBilletBypass) {
		
		return true;
	}

	@Override
	public List<RmtwoBilletBypass> findRmtwoBilletBypassBySearchType(RmtwoBilletBypass rmtwoBilletBypass, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmtwoBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaQuery.from(RmtwoBilletBypass.class);
		criteriaQuery.select(rmtwoBilletBypassRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), rmtwoBilletBypass.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}else if(searchType.equalsIgnoreCase("plantmasterid")){
			Predicate predicatePlanteMasterid = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("coldbilletbypassid"), rmtwoBilletBypass.getColdbilletbypassid());
			Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicatePlanteMasterid,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmtwoBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmtwoBilletBypass> rmtwoBilletBypassList = typedQuery.getResultList();
		return rmtwoBilletBypassList;
	}

	@Override
	public void deleteRmtwoBilletBypass(RmtwoBilletBypass rmtwoBilletBypass) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RmtwoBilletBypass> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaUpdate.from(RmtwoBilletBypass.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(rmtwoBilletBypassRoot.get("coldbilletbypassid"), rmtwoBilletBypass.getColdbilletbypassid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public RmtwoBilletBypass findDetailsByMobileNo(RmtwoBilletBypass rmtwoBilletBypass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RmtwoBilletBypass findDetailsByEmail(RmtwoBilletBypass rmtwoBilletBypass) {
		RmtwoBilletBypass newRmtwoBilletBypass = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RmtwoBilletBypass> criteriaQuery = criteriaBuilder.createQuery(RmtwoBilletBypass.class);
		Root<RmtwoBilletBypass> rmtwoBilletBypassRoot = criteriaQuery.from(RmtwoBilletBypass.class);
		criteriaQuery.select(rmtwoBilletBypassRoot);
		//Predicate predicateEmail = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("productiondate"), rmtwoBilletBypass.getProductiondate());
		Predicate predicateStatus = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(rmtwoBilletBypassRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RmtwoBilletBypass> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RmtwoBilletBypass> rmtwoBilletBypassList = typedQuery.getResultList();
		if(rmtwoBilletBypassList.size() != 0){
			newRmtwoBilletBypass = (RmtwoBilletBypass)rmtwoBilletBypassList.get(0);
		}
		return newRmtwoBilletBypass;
	}

	
	
}
