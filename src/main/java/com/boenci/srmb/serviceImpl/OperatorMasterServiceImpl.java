package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.OperatorMaster;
import com.boenci.srmb.repository.OperatorMasterRepository;
import com.boenci.srmb.service.OperatorMasterService;

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
public class OperatorMasterServiceImpl implements OperatorMasterService {

	@Autowired
	private OperatorMasterRepository operatorMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public OperatorMaster findById(long id) {
		OperatorMaster newOperatorMaster = null;
		newOperatorMaster =operatorMasterRepository.findById(id).get();		
		return newOperatorMaster;
	}

	@Override
	public OperatorMaster validOperatorMaster(OperatorMaster operatorMaster) {
		OperatorMaster newOperatorMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OperatorMaster> criteriaQuery = criteriaBuilder.createQuery(OperatorMaster.class);
		Root<OperatorMaster> operatorMasterRoot = criteriaQuery.from(OperatorMaster.class);
		criteriaQuery.select(operatorMasterRoot);
		Predicate predicateAreaname = criteriaBuilder.equal(operatorMasterRoot.get("employeeid"), operatorMaster.getEmployeeid());
		Predicate predicateStatus = criteriaBuilder.equal(operatorMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(operatorMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateAreaname,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<OperatorMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<OperatorMaster> operatorMasterList = typedQuery.getResultList();
		if(operatorMasterList.size() != 0){
			newOperatorMaster = (OperatorMaster)operatorMasterList.get(0);
		}
		return newOperatorMaster;
	}

	@Override
	public OperatorMaster save(OperatorMaster operatorMaster) {
		OperatorMaster newOperatorMaster = null;
		newOperatorMaster = operatorMasterRepository.save(operatorMaster);
		return newOperatorMaster;
	}

	@Transactional
	@Override
	public OperatorMaster update(OperatorMaster operatorMaster) {
		long operatorMasterId = operatorMaster.getOperatormasterid();
		OperatorMaster newOperatorMaster = null;
		if(operatorMasterRepository.existsById(operatorMasterId)){
			newOperatorMaster = operatorMasterRepository.save(operatorMaster);
			return newOperatorMaster;
		}else{
			return newOperatorMaster;
		}
	}

	@Transactional
	@Override
	public OperatorMaster delete(OperatorMaster operatorMaster) {
		long operatorMasterId = operatorMaster.getOperatormasterid();
		OperatorMaster newOperatorMaster = null;
		if(operatorMasterRepository.existsById(operatorMasterId)){
			newOperatorMaster = operatorMasterRepository.save(operatorMaster);
			return newOperatorMaster;
		}else{
			return newOperatorMaster;
		}
	}

	@Transactional
	@Override
	public String updateOperatorMaster(OperatorMaster operatorMaster) {
		
		OperatorMaster validateOperatorMaster = validOperatorMaster(operatorMaster);
		if(validateOperatorMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<OperatorMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(OperatorMaster.class);
			Root<OperatorMaster> operatorMasterRoot = criteriaUpdate.from(OperatorMaster.class);
			criteriaUpdate.set("areaname", operatorMaster.getAreaname());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", operatorMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(operatorMasterRoot.get("operatormasterid"), operatorMaster.getOperatormasterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<OperatorMaster> findAllOperatorMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OperatorMaster> criteriaQuery = criteriaBuilder.createQuery(OperatorMaster.class);
		Root<OperatorMaster> operatorMasterRoot = criteriaQuery.from(OperatorMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(operatorMasterRoot.get("areaname")));
		List<OperatorMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isOperatorMasterExist(OperatorMaster operatorMaster) {
		
		return true;
	}

	@Override
	public List<OperatorMaster> findOperatorMasterBySearchType(OperatorMaster operatorMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OperatorMaster> criteriaQuery = criteriaBuilder.createQuery(OperatorMaster.class);
		Root<OperatorMaster> operatorMasterRoot = criteriaQuery.from(OperatorMaster.class);
		criteriaQuery.select(operatorMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename "+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid) "+
			" enterprisename,om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = om.plantmasterid) as plantname, om.sitemasterid,"+
			" (select sm.sitename from SiteMaster sm where sm.sitemasterid = om.sitemasterid) as sitename,"+
			" om.remarks, (select am.areaname from AreaMaster am WHERE  am.areamasterid = om.areamasterid) "+
			"as areaname, om.unittype, om.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = om.unitmasterid) as unitname,om.designation,om.employeeid,"+
			" om.employeename,om.phonenumber,om.operatormasterid FROM OperatorMaster om "+
			" where om.deletedflag = ?1 and om.status = ?2";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<OperatorMaster> operatorMasterList = new ArrayList<OperatorMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					OperatorMaster newOperatorMaster = new OperatorMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newOperatorMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newOperatorMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newOperatorMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newOperatorMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newOperatorMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setDesignation(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEmployeeid(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEmployeename(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
						newOperatorMaster.setPhonenumber(newObject.toString());
						}else if(objectNDX == 16) {
							newOperatorMaster.setOperatormasterid(Long.valueOf(newObject.toString()));
						}
					}
					operatorMasterList.add(newOperatorMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return operatorMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(operatorMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(operatorMasterRoot.get("status"), operatorMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<OperatorMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<OperatorMaster> operatorMasterList = typedQuery.getResultList();
			return operatorMasterList;

		}else if(searchType.equalsIgnoreCase("designation")){
			String strSql = "SELECT om.unitmasterid,om.enterprisemasterid,(select ename.enterprisename "+
			" from EnterpriseMaster ename where ename.enterprisemasterid = om.enterprisemasterid) "+
			" enterprisename,om.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			" pm.plantmasterid = om.plantmasterid) as plantname, om.sitemasterid,"+
			" (select sm.sitename from SiteMaster sm where sm.sitemasterid = om.sitemasterid) as sitename,"+
			" om.remarks, (select am.areaname from AreaMaster am WHERE  am.areamasterid = om.areamasterid) "+
			"as areaname, om.unittype, om.areamasterid,(select um.unitname from UnitMaster um "+
			" WHERE um.unitmasterid = om.unitmasterid) as unitname,om.designation,om.employeeid,"+
			" om.employeename,om.phonenumber,om.operatormasterid FROM OperatorMaster om "+
			" where om.deletedflag = ?1 and om.status = ?2 and om.designation = ?3";
			//System.out.println("strSql=====================  "+ strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,operatorMaster.getDesignation());
			List<OperatorMaster> operatorMasterList = new ArrayList<OperatorMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					OperatorMaster newOperatorMaster = new OperatorMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newOperatorMaster.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newOperatorMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newOperatorMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newOperatorMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setRemarks(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setAreaname(newObject.toString());
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setUnittype(newObject.toString());
						}else if(objectNDX == 10) {
							newOperatorMaster.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setUnitname(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setDesignation(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEmployeeid(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newOperatorMaster.setEmployeename(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
						newOperatorMaster.setPhonenumber(newObject.toString());
						}else if(objectNDX == 16) {
							newOperatorMaster.setOperatormasterid(Long.valueOf(newObject.toString()));
						}
					}
					operatorMasterList.add(newOperatorMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return operatorMasterList;
		}else{
			List<OperatorMaster> operatorMasterList = null;
			return operatorMasterList;
		}
		
	}
	@Transactional
	@Override
	public void deleteOperatorMaster(OperatorMaster operatorMaster) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<OperatorMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(OperatorMaster.class);
		Root<OperatorMaster> operatorMasterRoot = criteriaUpdate.from(OperatorMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(operatorMasterRoot.get("operatormasterid"), operatorMaster.getOperatormasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public OperatorMaster findDetailsByMobileNo(OperatorMaster operatorMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorMaster findDetailsByEmail(OperatorMaster operatorMaster) {
		OperatorMaster newOperatorMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OperatorMaster> criteriaQuery = criteriaBuilder.createQuery(OperatorMaster.class);
		Root<OperatorMaster> operatorMasterRoot = criteriaQuery.from(OperatorMaster.class);
		criteriaQuery.select(operatorMasterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(operatorMasterRoot.get("areaname"), operatorMaster.getAreaname());
		Predicate predicateStatus = criteriaBuilder.equal(operatorMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(operatorMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<OperatorMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<OperatorMaster> operatorMasterList = typedQuery.getResultList();
		if(operatorMasterList.size() != 0){
			newOperatorMaster = (OperatorMaster)operatorMasterList.get(0);
		}
		return newOperatorMaster;
	}

	
}
