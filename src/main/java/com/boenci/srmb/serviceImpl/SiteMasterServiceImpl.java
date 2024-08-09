package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.SiteMaster;
import com.boenci.srmb.repository.SiteMasterRepository;
import com.boenci.srmb.service.SiteMasterService;

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
public class SiteMasterServiceImpl implements SiteMasterService {

	@Autowired
	private SiteMasterRepository siteMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SiteMaster findById(long id) {
		SiteMaster newSiteMaster = null;
		newSiteMaster =siteMasterRepository.findById(id).get();		
		return newSiteMaster;
	}

	@Override
	public SiteMaster validSiteMaster(SiteMaster siteMaster) {
		SiteMaster newSiteMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteMaster> criteriaQuery = criteriaBuilder.createQuery(SiteMaster.class);
		Root<SiteMaster> siteMasterRoot = criteriaQuery.from(SiteMaster.class);
		criteriaQuery.select(siteMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(siteMasterRoot.get("sitename"), siteMaster.getSitename());
		Predicate predicateStatus = criteriaBuilder.equal(siteMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(siteMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SiteMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SiteMaster> siteMasterList = typedQuery.getResultList();
		if(siteMasterList.size() != 0){
			newSiteMaster = (SiteMaster)siteMasterList.get(0);
		}
		return newSiteMaster;
	}

	@Override
	public SiteMaster save(SiteMaster siteMaster) {
		SiteMaster newSiteMaster = null;
		newSiteMaster = siteMasterRepository.save(siteMaster);
		return newSiteMaster;
	}

	@Transactional
	@Override
	public SiteMaster update(SiteMaster siteMaster) {
		long siteMasterId = siteMaster.getSitemasterid();
		SiteMaster newSiteMaster = null;
		if(siteMasterRepository.existsById(siteMasterId)){
			newSiteMaster = siteMasterRepository.save(siteMaster);
			return newSiteMaster;
		}else{
			return newSiteMaster;
		}
	}

	@Transactional
	@Override
	public SiteMaster delete(SiteMaster siteMaster) {
		long siteMasterId = siteMaster.getSitemasterid();
		SiteMaster newSiteMaster = null;
		if(siteMasterRepository.existsById(siteMasterId)){
			newSiteMaster = siteMasterRepository.save(siteMaster);
			return newSiteMaster;
		}else{
			return newSiteMaster;
		}
	}
	
	@Transactional
	@Override
	public String updateSiteMaster(SiteMaster siteMaster) {
		
		SiteMaster validateSiteMaster = validSiteMaster(siteMaster);
		if(validateSiteMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<SiteMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(SiteMaster.class);
			Root<SiteMaster> siteMasterRoot = criteriaUpdate.from(SiteMaster.class);
			criteriaUpdate.set("sitename", siteMaster.getSitename());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", siteMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(siteMasterRoot.get("sitemasterid"), siteMaster.getSitemasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<SiteMaster> findAllSiteMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteMaster> criteriaQuery = criteriaBuilder.createQuery(SiteMaster.class);
		Root<SiteMaster> siteMasterRoot = criteriaQuery.from(SiteMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(siteMasterRoot.get("sitename")));
		List<SiteMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isSiteMasterExist(SiteMaster siteMaster) {
		
		return true;
	}

	@Override
	public List<SiteMaster> findSiteMasterBySearchType(SiteMaster siteMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteMaster> criteriaQuery = criteriaBuilder.createQuery(SiteMaster.class);
		Root<SiteMaster> siteMasterRoot = criteriaQuery.from(SiteMaster.class);
		criteriaQuery.select(siteMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT sm.sitemasterid, sm.address, sm.contactemail, sm.contactnumber,"+
			" sm.contactperson,sm.description, sm.enterprisemasterid,"+
			 "(select enterprisename from EnterpriseMaster em where "+
			 "em.enterprisemasterid = sm.enterprisemasterid and  em.deletedflag = ?1 "+
			 "and em.status = ?2) as enterprisename ,sm.licenseno, sm.sitename "+
			 " FROM SiteMaster sm where sm.deletedflag = ?3 and  sm.status = ?4";

			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"active");
			List<SiteMaster> siteMasterList = new ArrayList<SiteMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SiteMaster newSiteMaster = new SiteMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newSiteMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setAddress(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactemail(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactnumber(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactperson(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setDescription(newObject.toString());
						}else if(objectNDX == 6) {
							newSiteMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setLicenseno(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setSitename(newObject.toString());
						}
					}
					siteMasterList.add(newSiteMaster);
				}
				
			} catch (org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return siteMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(siteMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(siteMasterRoot.get("status"), siteMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<SiteMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<SiteMaster> siteMasterList = typedQuery.getResultList();
			return siteMasterList;
			
		}else if(searchType.equalsIgnoreCase("enterprisemasterid")){
			String strSql = "SELECT sm.sitemasterid, sm.address, sm.contactemail, sm.contactnumber,"+
			" sm.contactperson,sm.description, sm.enterprisemasterid,"+
			 "(select enterprisename from EnterpriseMaster em where "+
			 "em.enterprisemasterid = sm.enterprisemasterid and  em.deletedflag = ?1 "+
			 "and em.status = ?2) as enterprisename ,sm.licenseno, sm.sitename "+
			 " FROM SiteMaster sm where sm.deletedflag = ?3 and  sm.status = ?4 and sm.enterprisemasterid = ?5";

			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"active");
			query.setParameter(5,siteMaster.getEnterprisemasterid());
			List<SiteMaster> siteMasterList = new ArrayList<SiteMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SiteMaster newSiteMaster = new SiteMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newSiteMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setAddress(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactemail(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactnumber(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setContactperson(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setDescription(newObject.toString());
						}else if(objectNDX == 6) {
							newSiteMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setLicenseno(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSiteMaster.setSitename(newObject.toString());
						}
					}
					siteMasterList.add(newSiteMaster);
				}
				
			} catch (org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return siteMasterList;
		}else{
			List<SiteMaster> siteMasterList = null;
			return siteMasterList;
		}
		
	}

	@Override
	public void deleteSiteMaster(SiteMaster siteMaster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SiteMaster findDetailsByMobileNo(SiteMaster siteMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteMaster findDetailsByEmail(SiteMaster siteMaster) {
		SiteMaster newSiteMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteMaster> criteriaQuery = criteriaBuilder.createQuery(SiteMaster.class);
		Root<SiteMaster> siteMasterRoot = criteriaQuery.from(SiteMaster.class);
		criteriaQuery.select(siteMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(siteMasterRoot.get("sitename"), siteMaster.getSitename());
		Predicate predicateStatus = criteriaBuilder.equal(siteMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(siteMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SiteMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SiteMaster> siteMasterList = typedQuery.getResultList();
		if(siteMasterList.size() != 0){
			newSiteMaster = (SiteMaster)siteMasterList.get(0);
		}
		return newSiteMaster;
	}

	
	
}
