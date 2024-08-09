package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.PlantMaster;
import com.boenci.srmb.repository.PlantMasterRepository;
import com.boenci.srmb.service.PlantMasterService;

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
public class PlantMasterServiceImpl implements PlantMasterService {

	@Autowired
	private PlantMasterRepository plantMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public PlantMaster findById(long id) {
		PlantMaster newPlantMaster = null;
		newPlantMaster =plantMasterRepository.findById(id).get();		
		return newPlantMaster;
	}

	@Override
	public PlantMaster validPlantMaster(PlantMaster plantMaster) {
		PlantMaster newPlantMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlantMaster> criteriaQuery = criteriaBuilder.createQuery(PlantMaster.class);
		Root<PlantMaster> plantMasterRoot = criteriaQuery.from(PlantMaster.class);
		criteriaQuery.select(plantMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(plantMasterRoot.get("plantname"), plantMaster.getPlantname());
		Predicate predicateStatus = criteriaBuilder.equal(plantMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(plantMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<PlantMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<PlantMaster> plantMasterList = typedQuery.getResultList();
		if(plantMasterList.size() != 0){
			newPlantMaster = (PlantMaster)plantMasterList.get(0);
		}
		return newPlantMaster;
	}

	@Override
	public PlantMaster save(PlantMaster plantMaster) {
		PlantMaster newPlantMaster = null;
		newPlantMaster = plantMasterRepository.save(plantMaster);
		return newPlantMaster;
	}

	@Transactional
	@Override
	public PlantMaster update(PlantMaster plantMaster) {
		long plantMasterId = plantMaster.getPlantmasterid();
		PlantMaster newPlantMaster = null;
		if(plantMasterRepository.existsById(plantMasterId)){
			newPlantMaster = plantMasterRepository.save(plantMaster);
			return newPlantMaster;
		}else{
			return newPlantMaster;
		}
	}

	@Transactional
	@Override
	public PlantMaster delete(PlantMaster plantMaster) {
		long plantMasterId = plantMaster.getPlantmasterid();
		PlantMaster newPlantMaster = null;
		if(plantMasterRepository.existsById(plantMasterId)){
			newPlantMaster = plantMasterRepository.save(plantMaster);
			return newPlantMaster;
		}else{
			return newPlantMaster;
		}
	}

	@Transactional
	@Override
	public String updatePlantMaster(PlantMaster plantMaster) {
		
		PlantMaster validatePlantMaster = validPlantMaster(plantMaster);
		if(validatePlantMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<PlantMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(PlantMaster.class);
			Root<PlantMaster> plantMasterRoot = criteriaUpdate.from(PlantMaster.class);
			criteriaUpdate.set("plantname", plantMaster.getPlantname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", plantMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(plantMasterRoot.get("plantmasterid"), plantMaster.getPlantmasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<PlantMaster> findAllPlantMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlantMaster> criteriaQuery = criteriaBuilder.createQuery(PlantMaster.class);
		Root<PlantMaster> plantMasterRoot = criteriaQuery.from(PlantMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(plantMasterRoot.get("plantname")));
		List<PlantMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isPlantMasterExist(PlantMaster plantMaster) {
		
		return true;
	}

	@Override
	public List<PlantMaster> findPlantMasterBySearchType(PlantMaster plantMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlantMaster> criteriaQuery = criteriaBuilder.createQuery(PlantMaster.class);
		Root<PlantMaster> plantMasterRoot = criteriaQuery.from(PlantMaster.class);
		criteriaQuery.select(plantMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT pm.plantmasterid, pm.address, pm.contactemail, pm.contactnumber,"+
			" pm.contactperson,pm.description, pm.enterprisemasterid,"+
			"(select enterprisename from EnterpriseMaster em where "+
			"em.enterprisemasterid = pm.enterprisemasterid and  em.deletedflag = ?1 "+
			"and em.status = ?2) as enterprisename, pm.licenseno, pm.plantname, "+
			"pm.sitemasterid,(select sitename from SiteMaster sm where "+
			"sm.sitemasterid = pm.sitemasterid and  sm.deletedflag = ?3 and "+
			" sm.status = ?4) as sitename FROM PlantMaster pm where "+
			"pm.deletedflag = ?5 and  pm.status = ?6 ";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"active");
			query.setParameter(5,"no");
			query.setParameter(6,"active");
			List<PlantMaster> plantMasterList = new ArrayList<PlantMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					PlantMaster newPlantMaster = new PlantMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newPlantMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setAddress(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactemail(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactnumber(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactperson(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setDescription(newObject.toString());
						}else if(objectNDX == 6) {
							newPlantMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 8) {
							newPlantMaster.setLicenseno(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 10) {
							newPlantMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setSitename(newObject.toString());
						}
					}
					plantMasterList.add(newPlantMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return plantMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(plantMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(plantMasterRoot.get("status"), plantMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<PlantMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<PlantMaster> plantMasterList = typedQuery.getResultList();
			return plantMasterList;

		}else if(searchType.equalsIgnoreCase("sitemasterid")){
		
			String strSql = "SELECT pm.plantmasterid, pm.address, pm.contactemail, pm.contactnumber,"+
			" pm.contactperson,pm.description, pm.enterprisemasterid,"+
			"(select enterprisename from EnterpriseMaster em where "+
			"em.enterprisemasterid = pm.enterprisemasterid and  em.deletedflag = ?1 "+
			"and em.status = ?2) as enterprisename, pm.licenseno, pm.plantname, "+
			"pm.sitemasterid,(select sitename from SiteMaster sm where "+
			"sm.sitemasterid = pm.sitemasterid and  sm.deletedflag = ?3 and "+
			" sm.status = ?4) as sitename FROM PlantMaster pm where "+
			"pm.deletedflag = ?5 and  pm.status = ?6 and pm.sitemasterid = ?7";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"active");
			query.setParameter(5,"no");
			query.setParameter(6,"active");
			query.setParameter(7,plantMaster.getSitemasterid());

			List<PlantMaster> plantMasterList = new ArrayList<PlantMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					PlantMaster newPlantMaster = new PlantMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newPlantMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setAddress(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactemail(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactnumber(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setContactperson(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setDescription(newObject.toString());
						}else if(objectNDX == 6) {
							newPlantMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 8) {
							newPlantMaster.setLicenseno(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 10) {
							newPlantMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newPlantMaster.setSitename(newObject.toString());
						}
					}
					plantMasterList.add(newPlantMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return plantMasterList;

		}else{
			List<PlantMaster> plantMasterList = null;
			return plantMasterList;
		}
	}

	@Override
	public void deletePlantMaster(PlantMaster plantMaster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlantMaster findDetailsByMobileNo(PlantMaster plantMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlantMaster findDetailsByEmail(PlantMaster plantMaster) {
		PlantMaster newPlantMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PlantMaster> criteriaQuery = criteriaBuilder.createQuery(PlantMaster.class);
		Root<PlantMaster> plantMasterRoot = criteriaQuery.from(PlantMaster.class);
		criteriaQuery.select(plantMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(plantMasterRoot.get("plantname"), plantMaster.getPlantname());
		Predicate predicateStatus = criteriaBuilder.equal(plantMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(plantMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<PlantMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<PlantMaster> plantMasterList = typedQuery.getResultList();
		if(plantMasterList.size() != 0){
			newPlantMaster = (PlantMaster)plantMasterList.get(0);
		}
		return newPlantMaster;
	}
	
}
