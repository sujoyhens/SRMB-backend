package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.RoleMaster;
import com.boenci.srmb.repository.RoleMasterRepository;
import com.boenci.srmb.service.RoleMasterService;

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
public class RoleMasterServiceImpl implements RoleMasterService {

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public RoleMaster findById(long id) {
		RoleMaster newRoleMaster = null;
		newRoleMaster =roleMasterRepository.findById(id).get();		
		return newRoleMaster;
	}

	@Transactional
	@Override
	public RoleMaster update(RoleMaster roleMaster) {
		long roleMasterId = roleMaster.getRolemasterid();
		RoleMaster newRoleMaster = null;
		if(roleMasterRepository.existsById(roleMasterId)){
			newRoleMaster = roleMasterRepository.save(roleMaster);
			return newRoleMaster;
		}else{
			return newRoleMaster;
		}
	}

	@Transactional
	@Override
	public RoleMaster delete(RoleMaster roleMaster) {
		long roleMasterId = roleMaster.getRolemasterid();
		RoleMaster newRoleMaster = null;
		if(roleMasterRepository.existsById(roleMasterId)){
			newRoleMaster = roleMasterRepository.save(roleMaster);
			return newRoleMaster;
		}else{
			return newRoleMaster;
		}
	}


	@Override
	public RoleMaster validRoleMaster(RoleMaster roleMaster) {
		RoleMaster newRoleMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMaster> criteriaQuery = criteriaBuilder.createQuery(RoleMaster.class);
		Root<RoleMaster> roleMasterRoot = criteriaQuery.from(RoleMaster.class);
		criteriaQuery.select(roleMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(roleMasterRoot.get("rolename"), roleMaster.getRolename());
		Predicate predicateStatus = criteriaBuilder.equal(roleMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(roleMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RoleMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RoleMaster> roleMasterList = typedQuery.getResultList();
		if(roleMasterList.size() != 0){
			newRoleMaster = (RoleMaster)roleMasterList.get(0);
		}
		return newRoleMaster;
	}

	@Override
	public RoleMaster save(RoleMaster roleMaster) {
		RoleMaster newRoleMaster = null;
		newRoleMaster = roleMasterRepository.save(roleMaster);
		return newRoleMaster;
	}

	@Transactional
	@Override
	public String updateRoleMaster(RoleMaster roleMaster) {
		
		RoleMaster validateRoleMaster = validRoleMaster(roleMaster);
		if(validateRoleMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<RoleMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(RoleMaster.class);
			Root<RoleMaster> roleMasterRoot = criteriaUpdate.from(RoleMaster.class);
			criteriaUpdate.set("rolename", roleMaster.getRolename());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", roleMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(roleMasterRoot.get("rolemasterid"), roleMaster.getRolemasterid()));
			int rowCount = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			if(rowCount > 0){
				return "Success";
			}else{
				return "Failure";
			}
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<RoleMaster> findAllRoleMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMaster> criteriaQuery = criteriaBuilder.createQuery(RoleMaster.class);
		Root<RoleMaster> roleMasterRoot = criteriaQuery.from(RoleMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(roleMasterRoot.get("rolename")));
		List<RoleMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isRoleMasterExist(RoleMaster roleMaster) {
		
		return true;
	}

	@Override
	public List<RoleMaster> findRoleMasterBySearchType(RoleMaster roleMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMaster> criteriaQuery = criteriaBuilder.createQuery(RoleMaster.class);
		Root<RoleMaster> roleMasterRoot = criteriaQuery.from(RoleMaster.class);
		criteriaQuery.select(roleMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateDelete = criteriaBuilder.equal(roleMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(roleMasterRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<RoleMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<RoleMaster> roleMasterList = typedQuery.getResultList();
			return roleMasterList;
		}else{
			List<RoleMaster> roleMasterList = null;
			return roleMasterList;
		}
		
	}
	@Transactional
	@Override
	public void deleteRoleMaster(RoleMaster roleMaster) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<RoleMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(RoleMaster.class);
		Root<RoleMaster> roleMasterRoot = criteriaUpdate.from(RoleMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(roleMasterRoot.get("rolemasterid"), roleMaster.getRolemasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();

	}

	@Override
	public RoleMaster findDetailsByMobileNo(RoleMaster roleMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoleMaster findDetailsByEmail(RoleMaster roleMaster) {
		RoleMaster newRoleMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMaster> criteriaQuery = criteriaBuilder.createQuery(RoleMaster.class);
		Root<RoleMaster> roleMasterRoot = criteriaQuery.from(RoleMaster.class);
		criteriaQuery.select(roleMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(roleMasterRoot.get("rolename"), roleMaster.getRolename());
		Predicate predicateStatus = criteriaBuilder.equal(roleMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(roleMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<RoleMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<RoleMaster> roleMasterList = typedQuery.getResultList();
		if(roleMasterList.size() != 0){
			newRoleMaster = (RoleMaster)roleMasterList.get(0);
		}
		return newRoleMaster;
	}
	
}
