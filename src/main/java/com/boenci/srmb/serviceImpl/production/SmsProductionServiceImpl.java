package com.boenci.srmb.serviceImpl.production;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.production.SmsProduction;
import com.boenci.srmb.repository.production.SmsProductionRepository;
import com.boenci.srmb.service.production.SmsProductionService;
import com.boenci.srmb.utility.AppConstants;

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
public class SmsProductionServiceImpl implements SmsProductionService {

	@Autowired
	private SmsProductionRepository smsProductionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SmsProduction findById(long id) {
		SmsProduction newSmsProduction = null;
		newSmsProduction = smsProductionRepository.findById(id).get();		
		return newSmsProduction;
	}

	@Override
	public SmsProduction validSmsProduction(SmsProduction smsProduction) {
		SmsProduction newSmsProduction = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsProduction> criteriaQuery = criteriaBuilder.createQuery(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaQuery.from(SmsProduction.class);
		criteriaQuery.select(smsProductionRoot);
		Predicate predicateHeatno = criteriaBuilder.equal(smsProductionRoot.get("heatno"), smsProduction.getHeatno());
		Predicate predicateFcno = criteriaBuilder.equal(smsProductionRoot.get("fcno"), smsProduction.getFcno());
		Predicate predicateEmail = criteriaBuilder.equal(smsProductionRoot.get("productionid"), smsProduction.getProductionid());
		Predicate predicateStatus = criteriaBuilder.equal(smsProductionRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(smsProductionRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateHeatno,predicateFcno,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SmsProduction> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SmsProduction> smsProductionList = typedQuery.getResultList();
		if(smsProductionList.size() != 0){
			newSmsProduction = (SmsProduction)smsProductionList.get(0);
		}
		return newSmsProduction;
	}

	@Override
	public SmsProduction save(SmsProduction smsProduction) {
		SmsProduction newSmsProduction = null;
		newSmsProduction = smsProductionRepository.save(smsProduction);
		return newSmsProduction;
	}

	@Transactional
	@Override
	public SmsProduction update(SmsProduction smsProduction) {
		long smsProductionId = smsProduction.getSmsproductionid();
		SmsProduction newSmsProduction = null;
		SmsProduction newSmsProductionforSearch = smsProductionRepository.findById(smsProductionId).get();
		if(newSmsProductionforSearch != null){
			smsProduction.setProductionid(newSmsProductionforSearch.getProductionid());
			smsProduction.setIslaboratorylogupdated(newSmsProductionforSearch.getIslaboratorylogupdated());
			smsProduction.setIsproductionlogupdated(newSmsProductionforSearch.getIsproductionlogupdated());
			smsProduction.setIsmillproductionupdated(newSmsProductionforSearch.getIsmillproductionupdated());
			newSmsProduction = smsProductionRepository.save(smsProduction);
			return newSmsProduction;
		}else{
			return newSmsProduction;
		}
		
	}

	@Transactional
	@Override
	public String updateSmsProduction(SmsProduction smsProduction) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<SmsProduction> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaUpdate.from(SmsProduction.class);
		criteriaUpdate.set("islaboratorylogupdated", smsProduction.getIslaboratorylogupdated());
		criteriaUpdate.set("isproductionlogupdated", smsProduction.getIsproductionlogupdated());
		criteriaUpdate.set("ismillproductionupdated", smsProduction.getIsmillproductionupdated());
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("action", "update");
		criteriaUpdate.where(criteriaBuilder.equal(smsProductionRoot.get("productionid"), smsProduction.getProductionid()));
		int count = this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		if(count > 0){
			return "success";
		}else{
			return "failure";
		}
			
	}

	@Transactional
	@Override
	public List<SmsProduction> findAllSmsProduction() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsProduction> criteriaQuery = criteriaBuilder.createQuery(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaQuery.from(SmsProduction.class);
		//criteriaQuery.orderBy(criteriaBuilder.desc(smsProductionRoot.get("unitname")));
		List<SmsProduction> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isSmsProductionExist(SmsProduction smsProduction) {
		
		return true;
	}

	@Override
	public List<SmsProduction> findSmsProductionBySearchType(SmsProduction smsProduction, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsProduction> criteriaQuery = criteriaBuilder.createQuery(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaQuery.from(SmsProduction.class);
		criteriaQuery.select(smsProductionRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
			" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
			"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
			" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
			" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
			" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery;

			System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsProduction.setSmsproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsProduction.setCranedelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCycletime(newObject.toString());
						}else if(objectNDX == 21) {
							newSmsProduction.setElectricaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setFcno(newObject.toString());							
						}else if(objectNDX == 23) {
							newSmsProduction.setFurnacetemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatno(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatoftime(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatontime(newObject.toString());
						}else if(objectNDX == 27) {
							newSmsProduction.setLadledelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsProduction.setLoadcellweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsProduction.setMechanicaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsProduction.setMpdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newSmsProduction.setOthersdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newSmsProduction.setPatchinglife(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsProduction.setPowercutdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setProductionid(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRemarks(newObject.toString());
						}else if(objectNDX == 37) {
							newSmsProduction.setTappingtemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 38) {
							newSmsProduction.setTotaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiontime(newObject.toString());
						}else if(objectNDX == 40) {
							newSmsProduction.setSponge(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newSmsProduction.setScrapmelt(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newSmsProduction.setPigiron(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newSmsProduction.setSilicomanganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newSmsProduction.setAluminium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newSmsProduction.setCpc(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newSmsProduction.setFerroshot(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newSmsProduction.setSilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 48) {
							newSmsProduction.setChrome(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 49) {
							newSmsProduction.setNfapowder(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 50) {
							newSmsProduction.setMillscale(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 51) {
							newSmsProduction.setNickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 52) {
							newSmsProduction.setCopper(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 53) {
							newSmsProduction.setTotalchargemix(Double.valueOf(newObject.toString()));
						}	else if(objectNDX == 54) {
							newSmsProduction.setPellets(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 55) {
							newSmsProduction.setElectricityconsumtion(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 56) {
							newSmsProduction.setGrossweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 57) {
							newSmsProduction.setTareweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 58) {
							newSmsProduction.setNetweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 59) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsname(newObject.toString());
						} else if(objectNDX == 60) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 61) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCcmname(newObject.toString());
						} else if(objectNDX == 62) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRmname(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else if(searchType.equalsIgnoreCase("smsproductiondate")){
			String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
			" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
			"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
			" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
			" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
			" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ " AND querytable.smsproductiondate = ?3 order by querytable.smsproductionid DESC";

			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,smsProduction.getSmsproductiondate());
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsProduction.setSmsproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsProduction.setCranedelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCycletime(newObject.toString());
						}else if(objectNDX == 21) {
							newSmsProduction.setElectricaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setFcno(newObject.toString());							
						}else if(objectNDX == 23) {
							newSmsProduction.setFurnacetemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatno(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatoftime(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatontime(newObject.toString());
						}else if(objectNDX == 27) {
							newSmsProduction.setLadledelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsProduction.setLoadcellweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsProduction.setMechanicaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsProduction.setMpdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newSmsProduction.setOthersdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newSmsProduction.setPatchinglife(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsProduction.setPowercutdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setProductionid(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRemarks(newObject.toString());
						}else if(objectNDX == 37) {
							newSmsProduction.setTappingtemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 38) {
							newSmsProduction.setTotaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiontime(newObject.toString());
						}else if(objectNDX == 40) {
							newSmsProduction.setSponge(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newSmsProduction.setScrapmelt(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newSmsProduction.setPigiron(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newSmsProduction.setSilicomanganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newSmsProduction.setAluminium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newSmsProduction.setCpc(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newSmsProduction.setFerroshot(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newSmsProduction.setSilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 48) {
							newSmsProduction.setChrome(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 49) {
							newSmsProduction.setNfapowder(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 50) {
							newSmsProduction.setMillscale(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 51) {
							newSmsProduction.setNickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 52) {
							newSmsProduction.setCopper(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 53) {
							newSmsProduction.setTotalchargemix(Double.valueOf(newObject.toString()));
						}	else if(objectNDX == 54) {
							newSmsProduction.setPellets(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 55) {
							newSmsProduction.setElectricityconsumtion(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 56) {
							newSmsProduction.setGrossweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 57) {
							newSmsProduction.setTareweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 58) {
							newSmsProduction.setNetweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 59) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsname(newObject.toString());
						} else if(objectNDX == 60) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 61) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCcmname(newObject.toString());
						} else if(objectNDX == 62) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRmname(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else if(searchType.equalsIgnoreCase("laboratorylog")){
			long unitid = 0;
			if(smsProduction.getUnitmasterid() == 3){
				unitid = 1;
			}else if(smsProduction.getUnitmasterid() == 4){
				unitid = 2;
			}
			String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
			" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
			"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
			" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
			" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
			" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ 
			" AND querytable.islaboratorylogupdated = ?3 AND querytable.isproductionlogupdated = ?4 "+
			" AND querytable.ismillproductionupdated = ?5 AND querytable.unitmasterid = ?6 order by querytable.smsproductionid ASC LIMIT 1";

			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"no");
			query.setParameter(4,"no");
			query.setParameter(5,"no");
			query.setParameter(6,unitid);
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsProduction.setSmsproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsProduction.setCranedelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCycletime(newObject.toString());
						}else if(objectNDX == 21) {
							newSmsProduction.setElectricaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setFcno(newObject.toString());							
						}else if(objectNDX == 23) {
							newSmsProduction.setFurnacetemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatno(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatoftime(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatontime(newObject.toString());
						}else if(objectNDX == 27) {
							newSmsProduction.setLadledelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsProduction.setLoadcellweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsProduction.setMechanicaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsProduction.setMpdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newSmsProduction.setOthersdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newSmsProduction.setPatchinglife(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsProduction.setPowercutdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setProductionid(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRemarks(newObject.toString());
						}else if(objectNDX == 37) {
							newSmsProduction.setTappingtemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 38) {
							newSmsProduction.setTotaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiontime(newObject.toString());
						}else if(objectNDX == 40) {
							newSmsProduction.setSponge(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newSmsProduction.setScrapmelt(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newSmsProduction.setPigiron(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newSmsProduction.setSilicomanganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newSmsProduction.setAluminium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newSmsProduction.setCpc(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newSmsProduction.setFerroshot(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newSmsProduction.setSilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 48) {
							newSmsProduction.setChrome(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 49) {
							newSmsProduction.setNfapowder(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 50) {
							newSmsProduction.setMillscale(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 51) {
							newSmsProduction.setNickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 52) {
							newSmsProduction.setCopper(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 53) {
							newSmsProduction.setTotalchargemix(Double.valueOf(newObject.toString()));
						}	else if(objectNDX == 54) {
							newSmsProduction.setPellets(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 55) {
							newSmsProduction.setElectricityconsumtion(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 56) {
							newSmsProduction.setGrossweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 57) {
							newSmsProduction.setTareweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 58) {
							newSmsProduction.setNetweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 59) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsname(newObject.toString());
						} else if(objectNDX == 60) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 61) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCcmname(newObject.toString());
						} else if(objectNDX == 62) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRmname(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else if(searchType.equalsIgnoreCase("productionlog")){
			long unitid = 0;
			if(smsProduction.getUnitmasterid() == 5){
				unitid = 1;
			}else if(smsProduction.getUnitmasterid() == 6){
				unitid = 2;
			}
			String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
			" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
			"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
			" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
			" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
			" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
			String returnQuery = UtilityRestController.getQueryString(queryString);
			String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
			AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ 
			" AND querytable.islaboratorylogupdated = ?3 AND querytable.isproductionlogupdated = ?4 "+
			" AND querytable.ismillproductionupdated = ?5 AND querytable.unitmasterid = ?6 order by querytable.smsproductionid ASC LIMIT 1";

			//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
			Query query = entityManager.createQuery(sqlQuery);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,"yes");
			query.setParameter(4,"no");
			query.setParameter(5,"no");
			query.setParameter(6,unitid);
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsProduction.setSmsproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsProduction.setCranedelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCycletime(newObject.toString());
						}else if(objectNDX == 21) {
							newSmsProduction.setElectricaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setFcno(newObject.toString());							
						}else if(objectNDX == 23) {
							newSmsProduction.setFurnacetemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatno(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatoftime(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatontime(newObject.toString());
						}else if(objectNDX == 27) {
							newSmsProduction.setLadledelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsProduction.setLoadcellweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsProduction.setMechanicaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsProduction.setMpdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newSmsProduction.setOthersdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newSmsProduction.setPatchinglife(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsProduction.setPowercutdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setProductionid(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRemarks(newObject.toString());
						}else if(objectNDX == 37) {
							newSmsProduction.setTappingtemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 38) {
							newSmsProduction.setTotaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiontime(newObject.toString());
						}else if(objectNDX == 40) {
							newSmsProduction.setSponge(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newSmsProduction.setScrapmelt(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newSmsProduction.setPigiron(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newSmsProduction.setSilicomanganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newSmsProduction.setAluminium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newSmsProduction.setCpc(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newSmsProduction.setFerroshot(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newSmsProduction.setSilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 48) {
							newSmsProduction.setChrome(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 49) {
							newSmsProduction.setNfapowder(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 50) {
							newSmsProduction.setMillscale(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 51) {
							newSmsProduction.setNickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 52) {
							newSmsProduction.setCopper(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 53) {
							newSmsProduction.setTotalchargemix(Double.valueOf(newObject.toString()));
						}	else if(objectNDX == 54) {
							newSmsProduction.setPellets(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 55) {
							newSmsProduction.setElectricityconsumtion(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 56) {
							newSmsProduction.setGrossweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 57) {
							newSmsProduction.setTareweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 58) {
							newSmsProduction.setNetweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 59) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsname(newObject.toString());
						} else if(objectNDX == 60) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 61) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCcmname(newObject.toString());
						} else if(objectNDX == 62) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRmname(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else if(searchType.equalsIgnoreCase("millproduction")){
			Query query = null;
			if(smsProduction.getUnitmasterid() == 7){
				String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
				" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
				"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
				" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
				" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
				" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ 
				" AND querytable.islaboratorylogupdated = ?3 AND querytable.isproductionlogupdated = ?4 "+
				" AND querytable.ismillproductionupdated = ?5 AND querytable.unitmasterid in (1) order by querytable.smsproductionid ASC LIMIT 1";

				//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"yes");
				query.setParameter(4,"yes");
				query.setParameter(5,"no");

			}else if(smsProduction.getUnitmasterid() == 8){
				String queryString = "smsproductionid,cranedelay, cycletime, electricaldelay,"+
				" fcno, furnacetemperature, heatno, heatoftime, heatontime, ladledelay, "+
				"loadcellweight, mechanicaldelay, mpdelay, othersdelay, patchinglife, powercutdelay,"+
				" smsproductiondate, productionid, remarks, tappingtemperature, totaldelay,smsproductiontime,"+
				" sponge,scrapmelt,pigiron,silicomanganese,aluminium,cpc,ferroshot,silicon,chrome,nfapowder,millscale,nickel,"+ 
				" copper,totalchargemix,pellets,electricityconsumtion,grossweight,tareweight,netweight,smsname,laboratoryname,ccmname,rmname,SmsProduction";
				String returnQuery = UtilityRestController.getQueryString(queryString);
				String sqlQuery = AppConstants.COMMON_QUERY+","+AppConstants.MASTER_QUERY+","+
				AppConstants.UNITMASTER_QUERY+","+AppConstants.AREAMASTER_QUERY+returnQuery+ 
				" AND querytable.islaboratorylogupdated = ?3 AND querytable.isproductionlogupdated = ?4 "+
				" AND querytable.ismillproductionupdated = ?5 AND querytable.unitmasterid in (1,2) order by querytable.smsproductionid ASC LIMIT 1";

				//System.out.println("sqlQuery::::::::::::::::::: "+ sqlQuery);
				query = entityManager.createQuery(sqlQuery);
				query.setParameter(1,"no");
				query.setParameter(2,"active");
				query.setParameter(3,"yes");
				query.setParameter(4,"yes");
				query.setParameter(5,"no");
			}
			
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newSmsProduction.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setEnterprisename(newObject.toString());
						}else if(objectNDX == 10) {
							newSmsProduction.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setPlantname(newObject.toString());
						}else if(objectNDX == 12) {
							newSmsProduction.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSitename(newObject.toString());
						}else if(objectNDX == 14) {
							newSmsProduction.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setUnitname(newObject.toString());
						}else if(objectNDX == 16) {
							newSmsProduction.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setAreaname(newObject.toString());
						}else if(objectNDX == 18) {
							newSmsProduction.setSmsproductionid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 19) {
							newSmsProduction.setCranedelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCycletime(newObject.toString());
						}else if(objectNDX == 21) {
							newSmsProduction.setElectricaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 22) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setFcno(newObject.toString());							
						}else if(objectNDX == 23) {
							newSmsProduction.setFurnacetemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 24) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatno(newObject.toString());
						}else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatoftime(newObject.toString());
						}else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setHeatontime(newObject.toString());
						}else if(objectNDX == 27) {
							newSmsProduction.setLadledelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 28) {
							newSmsProduction.setLoadcellweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 29) {
							newSmsProduction.setMechanicaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 30) {
							newSmsProduction.setMpdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 31) {
							newSmsProduction.setOthersdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 32) {
							newSmsProduction.setPatchinglife(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 33) {
							newSmsProduction.setPowercutdelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 34) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}else if(objectNDX == 35) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setProductionid(newObject.toString());
						}else if(objectNDX == 36) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRemarks(newObject.toString());
						}else if(objectNDX == 37) {
							newSmsProduction.setTappingtemperature(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 38) {
							newSmsProduction.setTotaldelay(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 39) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiontime(newObject.toString());
						}else if(objectNDX == 40) {
							newSmsProduction.setSponge(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 41) {
							newSmsProduction.setScrapmelt(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 42) {
							newSmsProduction.setPigiron(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 43) {
							newSmsProduction.setSilicomanganese(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 44) {
							newSmsProduction.setAluminium(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 45) {
							newSmsProduction.setCpc(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 46) {
							newSmsProduction.setFerroshot(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 47) {
							newSmsProduction.setSilicon(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 48) {
							newSmsProduction.setChrome(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 49) {
							newSmsProduction.setNfapowder(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 50) {
							newSmsProduction.setMillscale(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 51) {
							newSmsProduction.setNickel(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 52) {
							newSmsProduction.setCopper(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 53) {
							newSmsProduction.setTotalchargemix(Double.valueOf(newObject.toString()));
						}	else if(objectNDX == 54) {
							newSmsProduction.setPellets(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 55) {
							newSmsProduction.setElectricityconsumtion(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 56) {
							newSmsProduction.setGrossweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 57) {
							newSmsProduction.setTareweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 58) {
							newSmsProduction.setNetweight(Double.valueOf(newObject.toString()));
						} else if(objectNDX == 59) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsname(newObject.toString());
						} else if(objectNDX == 60) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setLaboratoryname(newObject.toString());
						} else if(objectNDX == 61) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setCcmname(newObject.toString());
						} else if(objectNDX == 62) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setRmname(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else if(searchType.equalsIgnoreCase("totalweight")){
			String queryString = "SELECT SUM(grossweight) AS totalgrossweight, SUM(netweight) AS totalnetweight,"+
			" SUM(tareweight) AS totaltareweight, smsproductiondate  from SmsProduction "+
			"  WHERE  smsproductiondate = ?1  GROUP BY smsproductiondate";
			//System.out.println("sqlQuery::::::::::::::::::: "+ queryString);
			Query query = entityManager.createQuery(queryString);
			query.setParameter(1,smsProduction.getSmsproductiondate());
			List<SmsProduction> smsProductionList = new ArrayList<SmsProduction>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					SmsProduction newSmsProduction = new SmsProduction();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newSmsProduction.setTotalgrossweight(newObject.toString());
						}else if(objectNDX == 1) {
							newSmsProduction.setTotalnetweight(newObject.toString());
						}else if(objectNDX == 2) {
							newSmsProduction.setTotaltareweight(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newSmsProduction.setSmsproductiondate(newObject.toString());
						}
					}
					smsProductionList.add(newSmsProduction);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return smsProductionList;

		}else{
			List<SmsProduction> smsProductionList = null;
			return smsProductionList;
		}
		
	}

	@Override
	public void deleteSmsProduction(SmsProduction smsProduction) {
		SmsProduction newSmsProduction = findById(smsProduction.getSmsproductionid());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<SmsProduction> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaUpdate.from(SmsProduction.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(smsProductionRoot.get("smsproductionid"), newSmsProduction.getSmsproductionid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();
		
	}

	@Override
	public SmsProduction findDetailsByMobileNo(SmsProduction smsProduction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmsProduction findDetailsByEmail(SmsProduction smsProduction) {
		SmsProduction newSmsProduction = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SmsProduction> criteriaQuery = criteriaBuilder.createQuery(SmsProduction.class);
		Root<SmsProduction> smsProductionRoot = criteriaQuery.from(SmsProduction.class);
		criteriaQuery.select(smsProductionRoot);
		Predicate predicateEmail = criteriaBuilder.equal(smsProductionRoot.get("productionid"), smsProduction.getProductionid());
		Predicate predicateStatus = criteriaBuilder.equal(smsProductionRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(smsProductionRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<SmsProduction> typedQuery = entityManager.createQuery(criteriaQuery);
		List<SmsProduction> smsProductionList = typedQuery.getResultList();
		if(smsProductionList.size() != 0){
			newSmsProduction = (SmsProduction)smsProductionList.get(0);
		}
		return newSmsProduction;
	}
	
}
