package com.boenci.srmb.serviceImpl.production;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.SequenceParameter;
import com.boenci.srmb.repository.production.SequenceParameterRepository;
import com.boenci.srmb.service.production.SequenceParameterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SequenceParameterServiceImpl implements SequenceParameterService {

	@Autowired
	private SequenceParameterRepository sequenceParameterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SequenceParameter findById(long id) {
		SequenceParameter newSequenceParameter = null;
		newSequenceParameter =sequenceParameterRepository.findById(id).get();		
		return newSequenceParameter;
	}

	@Override
	public SequenceParameter validSequenceParameter(SequenceParameter sequenceParameter) {
		SequenceParameter newSequenceParameter = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SequenceParameter> criteriaQuery = criteriaBuilder.createQuery(SequenceParameter.class);
		Root<SequenceParameter> sequenceParameterRoot = criteriaQuery.from(SequenceParameter.class);
		criteriaQuery.select(sequenceParameterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(sequenceParameterRoot.get("heatnumber"), sequenceParameter.getHeatnumber());
		Predicate predicateStatus = criteriaBuilder.equal(sequenceParameterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(sequenceParameterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SequenceParameter> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SequenceParameter> sequenceParameterList = typedQuery.getResultList();
		if(sequenceParameterList.size() != 0){
			newSequenceParameter = (SequenceParameter)sequenceParameterList.get(0);
		}
		return newSequenceParameter;
	}

	@Override
	public SequenceParameter save(SequenceParameter sequenceParameter) {
		SequenceParameter newSequenceParameter = null;
		newSequenceParameter = sequenceParameterRepository.save(sequenceParameter);
		return newSequenceParameter;
	}

	@Transactional
	@Override
	public SequenceParameter update(SequenceParameter sequenceParameter) {
		long sequenceParameterId = sequenceParameter.getSequenceparameterid();
		SequenceParameter newSequenceParameter = null;
		if(sequenceParameterRepository.existsById(sequenceParameterId)){
			newSequenceParameter = sequenceParameterRepository.save(sequenceParameter);
			return newSequenceParameter;
		}else{
			return newSequenceParameter;
		}
		
	}

	@Transactional
	@Override
	public String updateSequenceParameter(SequenceParameter sequenceParameter) {
		
		SequenceParameter validateSequenceParameter = validSequenceParameter(sequenceParameter);
		if(validateSequenceParameter == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<SequenceParameter> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(SequenceParameter.class);
			Root<SequenceParameter> sequenceParameterRoot = criteriaUpdate.from(SequenceParameter.class);
			criteriaUpdate.set("heatnumber", sequenceParameter.getHeatnumber());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", sequenceParameter.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(sequenceParameterRoot.get("sequenceparameterid"), sequenceParameter.getSequenceparameterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<SequenceParameter> findAllSequenceParameter() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SequenceParameter> criteriaQuery = criteriaBuilder.createQuery(SequenceParameter.class);
		Root<SequenceParameter> sequenceParameterRoot = criteriaQuery.from(SequenceParameter.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(sequenceParameterRoot.get("unitname")));
		List<SequenceParameter> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isSequenceParameterExist(SequenceParameter sequenceParameter) {
		
		return true;
	}

	@Override
	public List<SequenceParameter> findSequenceParameterBySearchType(SequenceParameter sequenceParameter, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SequenceParameter> criteriaQuery = criteriaBuilder.createQuery(SequenceParameter.class);
		Root<SequenceParameter> sequenceParameterRoot = criteriaQuery.from(SequenceParameter.class);
		criteriaQuery.select(sequenceParameterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(sequenceParameterRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(sequenceParameterRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(sequenceParameterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(sequenceParameterRoot.get("status"), sequenceParameter.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<SequenceParameter> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SequenceParameter> sequenceParameterList = typedQuery.getResultList();
		return sequenceParameterList;
	}

	@Override
	public void deleteSequenceParameter(SequenceParameter sequenceParameter) {
		SequenceParameter newSequenceParameter = findById(sequenceParameter.getSequenceparameterid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<SequenceParameter> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(SequenceParameter.class);
		Root<SequenceParameter> sequenceParameterRoot = criteriaUpdate.from(SequenceParameter.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(sequenceParameterRoot.get("sequenceparameterid"), newSequenceParameter.getSequenceparameterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public SequenceParameter findDetailsByMobileNo(SequenceParameter sequenceParameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SequenceParameter findDetailsByEmail(SequenceParameter sequenceParameter) {
		SequenceParameter newSequenceParameter = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SequenceParameter> criteriaQuery = criteriaBuilder.createQuery(SequenceParameter.class);
		Root<SequenceParameter> sequenceParameterRoot = criteriaQuery.from(SequenceParameter.class);
		criteriaQuery.select(sequenceParameterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(sequenceParameterRoot.get("heatnumber"), sequenceParameter.getHeatnumber());
		Predicate predicateStatus = criteriaBuilder.equal(sequenceParameterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(sequenceParameterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SequenceParameter> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SequenceParameter> sequenceParameterList = typedQuery.getResultList();
		if(sequenceParameterList.size() != 0){
			newSequenceParameter = (SequenceParameter)sequenceParameterList.get(0);
		}
		return newSequenceParameter;
	}
	
}
