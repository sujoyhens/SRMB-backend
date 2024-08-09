package com.boenci.srmb.serviceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.EquipmentImages;
import com.boenci.srmb.repository.EquipmentImagesRepository;
import com.boenci.srmb.service.EquipmentImagesService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EquipmentImagesServiceImpl implements EquipmentImagesService {

	@Autowired
	private EquipmentImagesRepository equipmentImagesRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EquipmentImages findById(long id) {
		EquipmentImages newEquipmentImages = null;
		newEquipmentImages =equipmentImagesRepository.findById(id).get();		
		return newEquipmentImages;
	}

	@Override
	public EquipmentImages validEquipmentImages(EquipmentImages equipmentImages) {
		EquipmentImages newEquipmentImages = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentImages> criteriaQuery = criteriaBuilder.createQuery(EquipmentImages.class);
		Root<EquipmentImages> equipmentImagesRoot = criteriaQuery.from(EquipmentImages.class);
		criteriaQuery.select(equipmentImagesRoot);
		Predicate predicateEmail = criteriaBuilder.equal(equipmentImagesRoot.get("equipmentimagesid"), equipmentImages.getEquipmentimagesid());
		Predicate predicateStatus = criteriaBuilder.equal(equipmentImagesRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(equipmentImagesRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EquipmentImages> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EquipmentImages> equipmentImagesList = typedQuery.getResultList();
		if(equipmentImagesList.size() != 0){
			newEquipmentImages = (EquipmentImages)equipmentImagesList.get(0);
		}
		return newEquipmentImages;
	}

	@Override
	public EquipmentImages save(EquipmentImages equipmentImages) {
		EquipmentImages newEquipmentImages = null;
		newEquipmentImages = equipmentImagesRepository.save(equipmentImages);
		return newEquipmentImages;
	}

	@Transactional
	@Override
	public EquipmentImages update(EquipmentImages equipmentImages) {
		long equipmentImagesId = equipmentImages.getEquipmentimagesid();
		EquipmentImages newEquipmentImages = null;
		if(equipmentImagesRepository.existsById(equipmentImagesId)){
			newEquipmentImages = equipmentImagesRepository.save(equipmentImages);
			return newEquipmentImages;
		}else{
			return newEquipmentImages;
		}
	}

	@Transactional
	@Override
	public EquipmentImages delete(EquipmentImages equipmentImages) {
		long equipmentImagesId = equipmentImages.getEquipmentimagesid();
		EquipmentImages newEquipmentImages = null;
		if(equipmentImagesRepository.existsById(equipmentImagesId)){
			newEquipmentImages = equipmentImagesRepository.save(equipmentImages);
			return newEquipmentImages;
		}else{
			return newEquipmentImages;
		}
	}

	@Transactional
	@Override
	public String updateEquipmentImages(EquipmentImages equipmentImages) {
		
		EquipmentImages validateEquipmentImages = validEquipmentImages(equipmentImages);
		if(validateEquipmentImages == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<EquipmentImages> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(EquipmentImages.class);
			Root<EquipmentImages> equipmentImagesRoot = criteriaUpdate.from(EquipmentImages.class);
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", equipmentImages.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(equipmentImagesRoot.get("equipmentimagesid"), equipmentImages.getEquipmentimagesid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<EquipmentImages> findAllEquipmentImages() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentImages> criteriaQuery = criteriaBuilder.createQuery(EquipmentImages.class);
		Root<EquipmentImages> equipmentImagesRoot = criteriaQuery.from(EquipmentImages.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(equipmentImagesRoot.get("equipmentimagesid")));
		List<EquipmentImages> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isEquipmentImagesExist(EquipmentImages equipmentImages) {
		
		return true;
	}

	@Override
	public List<EquipmentImages> findEquipmentImagesBySearchType(EquipmentImages equipmentImages, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentImages> criteriaQuery = criteriaBuilder.createQuery(EquipmentImages.class);
		Root<EquipmentImages> equipmentImagesRoot = criteriaQuery.from(EquipmentImages.class);
		criteriaQuery.select(equipmentImagesRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			Predicate predicateStatus = criteriaBuilder.equal(equipmentImagesRoot.get("status"), "active");
			Predicate predicateDelete = criteriaBuilder.equal(equipmentImagesRoot.get("deletedflag"), "no");
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(equipmentImagesRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(equipmentImagesRoot.get("status"), equipmentImages.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			
		}else if(searchType.equalsIgnoreCase("equipmentmasterid")){
			Predicate predicateEquipmentMasterId = criteriaBuilder.equal(equipmentImagesRoot.get("equipmentmasterid"), equipmentImages.getEquipmentmasterid());
			Predicate predicateDelete = criteriaBuilder.equal(equipmentImagesRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(equipmentImagesRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateEquipmentMasterId,predicateStatus,predicateDelete);
		}
		criteriaQuery.where(predicateAnd);
		TypedQuery<EquipmentImages> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EquipmentImages> equipmentImagesList = typedQuery.getResultList();
		return equipmentImagesList;
	}

	@Override
	public void deleteEquipmentImages(EquipmentImages equipmentImages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EquipmentImages findDetailsByMobileNo(EquipmentImages equipmentImages) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentImages findDetailsByEmail(EquipmentImages equipmentImages) {
		EquipmentImages newEquipmentImages = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EquipmentImages> criteriaQuery = criteriaBuilder.createQuery(EquipmentImages.class);
		Root<EquipmentImages> equipmentImagesRoot = criteriaQuery.from(EquipmentImages.class);
		criteriaQuery.select(equipmentImagesRoot);
		Predicate predicateEmail = criteriaBuilder.equal(equipmentImagesRoot.get("equipmentmasterid"), equipmentImages.getEquipmentmasterid());
		Predicate predicateStatus = criteriaBuilder.equal(equipmentImagesRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(equipmentImagesRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<EquipmentImages> typedQuery = entityManager.createQuery(criteriaQuery);
		List<EquipmentImages> equipmentImagesList = typedQuery.getResultList();
		if(equipmentImagesList.size() != 0){
			newEquipmentImages = (EquipmentImages)equipmentImagesList.get(0);
		}
		return newEquipmentImages;
	}
	
}
