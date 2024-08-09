package com.boenci.srmb.serviceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.EnterpriseMaster;
import com.boenci.srmb.repository.EnterpriseMasterRepository;
import com.boenci.srmb.service.EnterpriseMasterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EnterpriseMasterServiceImpl implements EnterpriseMasterService {

	@Autowired
	private EnterpriseMasterRepository enterpriseMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EnterpriseMaster findById(long id) {
		EnterpriseMaster newEnterpriseMaster = null;
		newEnterpriseMaster =enterpriseMasterRepository.findById(id).get();		
		return newEnterpriseMaster;
	}

	@Override
	public EnterpriseMaster validEnterpriseMaster(EnterpriseMaster enterpriseMaster) {
		EnterpriseMaster newEnterpriseMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EnterpriseMaster> criteriaQuery = criteriaBuilder.createQuery(EnterpriseMaster.class);
		Root<EnterpriseMaster> enterpriseMasterRoot = criteriaQuery.from(EnterpriseMaster.class);
		criteriaQuery.select(enterpriseMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(enterpriseMasterRoot.get("enterprisename"), enterpriseMaster.getEnterprisename());
		Predicate predicateStatus = criteriaBuilder.equal(enterpriseMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(enterpriseMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EnterpriseMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EnterpriseMaster> enterpriseMasterList = typedQuery.getResultList();
		if(enterpriseMasterList.size() != 0){
			newEnterpriseMaster = (EnterpriseMaster)enterpriseMasterList.get(0);
		}
		return newEnterpriseMaster;
	}

	@Override
	public EnterpriseMaster save(EnterpriseMaster enterpriseMaster) {
		EnterpriseMaster newEnterpriseMaster = null;
		newEnterpriseMaster = enterpriseMasterRepository.save(enterpriseMaster);
		return newEnterpriseMaster;
	}

	@Transactional
	@Override
	public EnterpriseMaster update(EnterpriseMaster enterpriseMaster) {
		long enterpriseMasterId = enterpriseMaster.getEnterprisemasterid();
		EnterpriseMaster newEnterpriseMaster = null;
		if(enterpriseMasterRepository.existsById(enterpriseMasterId)){
			newEnterpriseMaster = enterpriseMasterRepository.save(enterpriseMaster);
			return newEnterpriseMaster;
		}else{
			return newEnterpriseMaster;
		}
	}

	@Transactional
	@Override
	public EnterpriseMaster delete(EnterpriseMaster enterpriseMaster) {
		long enterpriseMasterId = enterpriseMaster.getEnterprisemasterid();
		EnterpriseMaster newEnterpriseMaster = null;
		if(enterpriseMasterRepository.existsById(enterpriseMasterId)){
			newEnterpriseMaster = enterpriseMasterRepository.save(enterpriseMaster);
			return newEnterpriseMaster;
		}else{
			return newEnterpriseMaster;
		}
	}

	@Transactional
	@Override
	public String updateEnterpriseMaster(EnterpriseMaster enterpriseMaster) {
		
		EnterpriseMaster validateEnterpriseMaster = validEnterpriseMaster(enterpriseMaster);
		if(validateEnterpriseMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<EnterpriseMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(EnterpriseMaster.class);
			Root<EnterpriseMaster> enterpriseMasterRoot = criteriaUpdate.from(EnterpriseMaster.class);
			criteriaUpdate.set("enterprisename", enterpriseMaster.getEnterprisename());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", enterpriseMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(enterpriseMasterRoot.get("enterprisemasterid"), enterpriseMaster.getEnterprisemasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<EnterpriseMaster> findAllEnterpriseMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EnterpriseMaster> criteriaQuery = criteriaBuilder.createQuery(EnterpriseMaster.class);
		Root<EnterpriseMaster> enterpriseMasterRoot = criteriaQuery.from(EnterpriseMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(enterpriseMasterRoot.get("plantname")));
		List<EnterpriseMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isEnterpriseMasterExist(EnterpriseMaster enterpriseMaster) {
		
		return true;
	}

	@Override
	public List<EnterpriseMaster> findEnterpriseMasterBySearchType(EnterpriseMaster enterpriseMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EnterpriseMaster> criteriaQuery = criteriaBuilder.createQuery(EnterpriseMaster.class);
		Root<EnterpriseMaster> enterpriseMasterRoot = criteriaQuery.from(EnterpriseMaster.class);
		criteriaQuery.select(enterpriseMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(enterpriseMasterRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(enterpriseMasterRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(enterpriseMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(enterpriseMasterRoot.get("status"), enterpriseMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("enterprisemasterid")){
			Predicate predicateEquipmentMasterId = criteriaBuilder.equal(enterpriseMasterRoot.get("enterprisemasterid"), enterpriseMaster.getEnterprisemasterid());
			Predicate predicateDelete = criteriaBuilder.equal(enterpriseMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(enterpriseMasterRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateEquipmentMasterId,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<EnterpriseMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EnterpriseMaster> enterpriseMasterList = typedQuery.getResultList();
		return enterpriseMasterList;
	}

	@Override
	public void deleteEnterpriseMaster(EnterpriseMaster enterpriseMaster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnterpriseMaster findDetailsByMobileNo(EnterpriseMaster enterpriseMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnterpriseMaster findDetailsByEmail(EnterpriseMaster enterpriseMaster) {
		EnterpriseMaster newEnterpriseMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EnterpriseMaster> criteriaQuery = criteriaBuilder.createQuery(EnterpriseMaster.class);
		Root<EnterpriseMaster> enterpriseMasterRoot = criteriaQuery.from(EnterpriseMaster.class);
		criteriaQuery.select(enterpriseMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(enterpriseMasterRoot.get("enterprisename"), enterpriseMaster.getEnterprisename());
		Predicate predicateStatus = criteriaBuilder.equal(enterpriseMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(enterpriseMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EnterpriseMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EnterpriseMaster> enterpriseMasterList = typedQuery.getResultList();
		if(enterpriseMasterList.size() != 0){
			newEnterpriseMaster = (EnterpriseMaster)enterpriseMasterList.get(0);
		}
		return newEnterpriseMaster;
	}
	
}
