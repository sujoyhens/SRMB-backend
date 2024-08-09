package com.boenci.srmb.serviceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.InspectionMaster;
import com.boenci.srmb.repository.InspectionMasterRepository;
import com.boenci.srmb.service.InspectionMasterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class InspectionMasterServiceImpl implements InspectionMasterService {

	@Autowired
	private InspectionMasterRepository inspectionMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public InspectionMaster findById(long id) {
		InspectionMaster newInspectionMaster = null;
		newInspectionMaster =inspectionMasterRepository.findById(id).get();		
		return newInspectionMaster;
	}

	@Override
	public InspectionMaster validInspectionMaster(InspectionMaster inspectionMaster) {
		InspectionMaster newInspectionMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InspectionMaster> criteriaQuery = criteriaBuilder.createQuery(InspectionMaster.class);
		Root<InspectionMaster> inspectionMasterRoot = criteriaQuery.from(InspectionMaster.class);
		criteriaQuery.select(inspectionMasterRoot);
		Predicate predicateUnitmasterid = criteriaBuilder.equal(inspectionMasterRoot.get("unitmasterid"), inspectionMaster.getUnitmasterid());
		Predicate predicateUnittype = criteriaBuilder.equal(inspectionMasterRoot.get("unittype"), inspectionMaster.getUnittype());
		Predicate predicateAreaname = criteriaBuilder.equal(inspectionMasterRoot.get("areaname"), inspectionMaster.getAreaname());
		Predicate predicateStatus = criteriaBuilder.equal(inspectionMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(inspectionMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateUnitmasterid,predicateUnittype,predicateAreaname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<InspectionMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<InspectionMaster> inspectionMasterList = typedQuery.getResultList();
		if(inspectionMasterList.size() != 0){
			newInspectionMaster = (InspectionMaster)inspectionMasterList.get(0);
		}
		return newInspectionMaster;
	}

	@Override
	public InspectionMaster save(InspectionMaster inspectionMaster) {
		InspectionMaster newInspectionMaster = null;
		newInspectionMaster = inspectionMasterRepository.save(inspectionMaster);
		return newInspectionMaster;
	}

	@Transactional
	@Override
	public InspectionMaster update(InspectionMaster inspectionMaster) {
		long inspectionMasterId = inspectionMaster.getInspectionmasterid();
		InspectionMaster newInspectionMaster = null;
		if(inspectionMasterRepository.existsById(inspectionMasterId)){
			newInspectionMaster = inspectionMasterRepository.save(inspectionMaster);
			return newInspectionMaster;
		}else{
			return newInspectionMaster;
		}
	}

	@Transactional
	@Override
	public InspectionMaster delete(InspectionMaster inspectionMaster) {
		long inspectionMasterId = inspectionMaster.getInspectionmasterid();
		InspectionMaster newInspectionMaster = null;
		if(inspectionMasterRepository.existsById(inspectionMasterId)){
			newInspectionMaster = inspectionMasterRepository.save(inspectionMaster);
			return newInspectionMaster;
		}else{
			return newInspectionMaster;
		}
	}

	@Transactional
	@Override
	public String updateInspectionMaster(InspectionMaster inspectionMaster) {
		
		InspectionMaster validateInspectionMaster = validInspectionMaster(inspectionMaster);
		if(validateInspectionMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<InspectionMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(InspectionMaster.class);
			Root<InspectionMaster> inspectionMasterRoot = criteriaUpdate.from(InspectionMaster.class);
			criteriaUpdate.set("areaname", inspectionMaster.getAreaname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", inspectionMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(inspectionMasterRoot.get("inspectionmasterid"), inspectionMaster.getInspectionmasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<InspectionMaster> findAllInspectionMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InspectionMaster> criteriaQuery = criteriaBuilder.createQuery(InspectionMaster.class);
		Root<InspectionMaster> inspectionMasterRoot = criteriaQuery.from(InspectionMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(inspectionMasterRoot.get("areaname")));
		List<InspectionMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isInspectionMasterExist(InspectionMaster inspectionMaster) {
		
		return true;
	}

	@Override
	public List<InspectionMaster> findInspectionMasterBySearchType(InspectionMaster inspectionMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InspectionMaster> criteriaQuery = criteriaBuilder.createQuery(InspectionMaster.class);
		Root<InspectionMaster> inspectionMasterRoot = criteriaQuery.from(InspectionMaster.class);
		criteriaQuery.select(inspectionMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(inspectionMasterRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(inspectionMasterRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(inspectionMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(inspectionMasterRoot.get("status"), inspectionMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("unitmasterid")){
			Predicate predicateUnitMasterId = criteriaBuilder.equal(inspectionMasterRoot.get("unitmasterid"), inspectionMaster.getUnitmasterid());
			Predicate predicateDelete = criteriaBuilder.equal(inspectionMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(inspectionMasterRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateUnitMasterId,predicateStatus,predicateDelete);
			

		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<InspectionMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<InspectionMaster> inspectionMasterList = typedQuery.getResultList();
		return inspectionMasterList;
	}
	@Transactional
	@Override
	public void deleteInspectionMaster(InspectionMaster inspectionMaster) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<InspectionMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(InspectionMaster.class);
		Root<InspectionMaster> inspectionMasterRoot = criteriaUpdate.from(InspectionMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(inspectionMasterRoot.get("inspectionmasterid"), inspectionMaster.getInspectionmasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public InspectionMaster findDetailsByMobileNo(InspectionMaster inspectionMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InspectionMaster findDetailsByEmail(InspectionMaster inspectionMaster) {
		InspectionMaster newInspectionMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InspectionMaster> criteriaQuery = criteriaBuilder.createQuery(InspectionMaster.class);
		Root<InspectionMaster> inspectionMasterRoot = criteriaQuery.from(InspectionMaster.class);
		criteriaQuery.select(inspectionMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(inspectionMasterRoot.get("areaname"), inspectionMaster.getAreaname());
		Predicate predicateStatus = criteriaBuilder.equal(inspectionMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(inspectionMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<InspectionMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<InspectionMaster> inspectionMasterList = typedQuery.getResultList();
		if(inspectionMasterList.size() != 0){
			newInspectionMaster = (InspectionMaster)inspectionMasterList.get(0);
		}
		return newInspectionMaster;
	}

	
}
