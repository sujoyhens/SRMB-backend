package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.model.UserRegister;
import com.boenci.srmb.repository.UserRegisterRepository;
import com.boenci.srmb.service.UserRegisterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private UserRegisterRepository userRegisterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserRegister findById(long id) {
		UserRegister newUserRegister = null;
		newUserRegister =userRegisterRepository.findById(id).get();		
		return newUserRegister;
	}
	@Override
	public UserRegister validateUserFromAPI(JsonNode jsonNode) {	
		JsonNode jsonUserMode = jsonNode.path("usermode");
		JsonNode jsonEmail = jsonNode.path("email");
		JsonNode jsonPassword = jsonNode.path("password");
		JsonNode jsonCaller = jsonNode.path("caller");
		String strCaller = jsonCaller.asText();
		String strEmail = jsonEmail.asText();
		String strPassword = jsonPassword.asText();
		String strUserMode = jsonUserMode.asText();
		UserRegister newUserRegister  = new UserRegister();
		newUserRegister.setUseremail(strEmail);
		newUserRegister.setPassword(strPassword);
		newUserRegister.setUsermode(strUserMode);
		UserRegister userRegister = validUserRegister(newUserRegister);
	    return userRegister;

	}

	@Override
	public UserRegister validUserRegister(UserRegister userRegister) {
		UserRegister newUserRegister = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRegister> criteriaQuery = criteriaBuilder.createQuery(UserRegister.class);
		Root<UserRegister> userRegisterRoot = criteriaQuery.from(UserRegister.class);
		criteriaQuery.select(userRegisterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(userRegisterRoot.get("useremail"), userRegister.getUseremail());
		Predicate predicatePass = criteriaBuilder.equal(userRegisterRoot.get("password"), userRegister.getPassword());
		Predicate predicateStatus = criteriaBuilder.equal(userRegisterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(userRegisterRoot.get("deletedflag"), "no");
		Predicate predicateUserMode = criteriaBuilder.equal(userRegisterRoot.get("usermode"), userRegister.getUsermode());
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail, predicatePass,predicateStatus,predicateDelete,predicateUserMode);
		criteriaQuery.where(predicateAnd);
		TypedQuery<UserRegister> typedQuery = entityManager.createQuery(criteriaQuery);
		List<UserRegister> userRegisterList = typedQuery.getResultList();
		if(userRegisterList.size() != 0){
			newUserRegister = (UserRegister)userRegisterList.get(0);
		}
		return newUserRegister;
	}

	@Override
	public UserRegister save(UserRegister userRegister) {
		UserRegister newUserRegister = null;
		newUserRegister = userRegisterRepository.save(userRegister);
		return newUserRegister;
	}

	@Transactional
	@Override
	public UserRegister update(UserRegister userRegister) {
		long userRegisterId = userRegister.getUserregisterid();
		UserRegister newUserRegister = null;
		if(userRegisterRepository.existsById(userRegisterId)){
			newUserRegister = userRegisterRepository.save(userRegister);
			return newUserRegister;
		}else{
			return newUserRegister;
		}
	}

	@Transactional
	@Override
	public UserRegister delete(UserRegister userRegister) {
		long userRegisterId = userRegister.getUserregisterid();
		UserRegister newUserRegister = null;
		if(userRegisterRepository.existsById(userRegisterId)){
			newUserRegister = userRegisterRepository.save(userRegister);
			return newUserRegister;
		}else{
			return newUserRegister;
		}
	}

	@Transactional
	@Override
	public String updateUserRegister(UserRegister userRegister) {
		UserRegister validateUserRegister = validUserRegister(userRegister);
		if(validateUserRegister != null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<UserRegister> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(UserRegister.class);
			Root<UserRegister> userRegisterRoot = criteriaUpdate.from(UserRegister.class);
			criteriaUpdate.set("theme", userRegister.getTheme());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(userRegisterRoot.get("userregisterid"), userRegister.getUserregisterid()));
			this.entityManager.createQuery(criteriaUpdate).executeUpdate();
			return "Success";
		}else{
			return "Failure";
		}
		
	}

	@Transactional
	@Override
	public List<UserRegister> findAllUserRegister() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRegister> criteriaQuery = criteriaBuilder.createQuery(UserRegister.class);
		Root<UserRegister> userRegisterRoot = criteriaQuery.from(UserRegister.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(userRegisterRoot.get("username")));
		List<UserRegister> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isUserRegisterExist(UserRegister userRegister) {
		
		return true;
	}

	@Override
	public List<UserRegister> findUserRegisterBySearchType(UserRegister userRegister, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRegister> criteriaQuery = criteriaBuilder.createQuery(UserRegister.class);
		Root<UserRegister> userRegisterRoot = criteriaQuery.from(UserRegister.class);
		criteriaQuery.select(userRegisterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT ur.userregisterid, ur.areamasterid,(select am.areaname "+
			"from AreaMaster am WHERE am.areamasterid = ur.areamasterid) as areaname,"+
			"ur.employeeno, ur.enterprisemasterid,(select enterprisename from EnterpriseMaster em "+
			" where em.enterprisemasterid = ur.enterprisemasterid)  enterprisename,ur.phoneumber,"+
			" ur.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			"pm.plantmasterid = ur.plantmasterid) as plantname, ur.sitemasterid, "+
			"(select sitename from SiteMaster sm where sm.sitemasterid = ur.sitemasterid) as sitename,"+
			" ur.unitmasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = ur.unitmasterid) as unitname,"+
			" ur.unittype,ur.useremail, ur.usermode, ur.username,ur.theme,ur.imagepath,ur.rolemasterid,ur.division,ur.contactemail"+
			", workingunit1id,workingunit1name,workingunit2id,workingunit2name,isallunit "+
			" FROM UserRegister ur WHERE ur.deletedflag=?1 and  ur.status =?2";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<UserRegister> userRegisterList = new ArrayList<UserRegister>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					UserRegister newUserRegister = new UserRegister();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newUserRegister.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newUserRegister.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setAreaname(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEmployeeno(newObject.toString());
						}else if(objectNDX == 4) {
							newUserRegister.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEnterprisename(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPhoneumber(newObject.toString());
						}else if(objectNDX == 7) {
							newUserRegister.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPlantname(newObject.toString());
						}else if(objectNDX == 9) {
							newUserRegister.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setSitename(newObject.toString());
						}else if(objectNDX == 11) {
							newUserRegister.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnitname(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnittype(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUseremail(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsermode(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsername(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setTheme(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setImagepath(newObject.toString());
						}else if(objectNDX == 19) {
							newUserRegister.setRolemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setDivision(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setContactemail(newObject.toString());
						} else if(objectNDX == 22) {
							newUserRegister.setWorkingunit1id(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit1name(newObject.toString());
						} else if(objectNDX == 24) {
							newUserRegister.setWorkingunit2id(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit2name(newObject.toString());
						} else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setIsallunit(newObject.toString());
						}
					}
					userRegisterList.add(newUserRegister);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return userRegisterList;
		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(userRegisterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(userRegisterRoot.get("status"), userRegister.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<UserRegister> typedQuery = entityManager.createQuery(criteriaQuery);
			List<UserRegister> userRegisterList = typedQuery.getResultList();
			return userRegisterList;
		}else if(searchType.equalsIgnoreCase("usermode")){
			Predicate predicateUserMode = criteriaBuilder.equal(userRegisterRoot.get("usermode"), userRegister.getUsermode());
			Predicate predicateDelete = criteriaBuilder.equal(userRegisterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(userRegisterRoot.get("status"), "active");
			predicateAnd = criteriaBuilder.and(predicateUserMode,predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<UserRegister> typedQuery = entityManager.createQuery(criteriaQuery);
			List<UserRegister> userRegisterList = typedQuery.getResultList();
			return userRegisterList;
		}else if(searchType.equalsIgnoreCase("userregisterid")){

			String strSql = "SELECT ur.userregisterid, ur.areamasterid,(select am.areaname "+
			"from AreaMaster am WHERE am.areamasterid = ur.areamasterid) as areaname,"+
			"ur.employeeno, ur.enterprisemasterid,(select enterprisename from EnterpriseMaster em "+
			" where em.enterprisemasterid = ur.enterprisemasterid)  enterprisename,ur.phoneumber,"+
			" ur.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			"pm.plantmasterid = ur.plantmasterid) as plantname, ur.sitemasterid, "+
			"(select sitename from SiteMaster sm where sm.sitemasterid = ur.sitemasterid) as sitename,"+
			" ur.unitmasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = ur.unitmasterid) as unitname,"+
			" ur.unittype,ur.useremail, ur.usermode, ur.username,ur.theme,ur.imagepath,ur.rolemasterid,ur.division,ur.contactemail"+
			", workingunit1id,workingunit1name,workingunit2id,workingunit2name,isallunit "+
			" FROM UserRegister ur WHERE ur.deletedflag=?1 and  ur.status =?2 and ur.userregisterid =?3";
			System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,userRegister.getUserregisterid());
			List<UserRegister> userRegisterList = new ArrayList<UserRegister>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					UserRegister newUserRegister = new UserRegister();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newUserRegister.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newUserRegister.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setAreaname(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEmployeeno(newObject.toString());
						}else if(objectNDX == 4) {
							newUserRegister.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEnterprisename(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPhoneumber(newObject.toString());
						}else if(objectNDX == 7) {
							newUserRegister.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPlantname(newObject.toString());
						}else if(objectNDX == 9) {
							newUserRegister.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setSitename(newObject.toString());
						}else if(objectNDX == 11) {
							newUserRegister.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnitname(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnittype(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUseremail(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsermode(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsername(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setTheme(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setImagepath(newObject.toString());
						}else if(objectNDX == 19) {
							newUserRegister.setRolemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setDivision(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setContactemail(newObject.toString());
						} else if(objectNDX == 22) {
							newUserRegister.setWorkingunit1id(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit1name(newObject.toString());
						} else if(objectNDX == 24) {
							newUserRegister.setWorkingunit2id(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit2name(newObject.toString());
						} else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setIsallunit(newObject.toString());
						}
					}
					userRegisterList.add(newUserRegister);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return userRegisterList;			
		}else if(searchType.equalsIgnoreCase("unitmasterid")){

			String strSql = "SELECT ur.userregisterid, ur.areamasterid,(select am.areaname "+
			"from AreaMaster am WHERE am.areamasterid = ur.areamasterid) as areaname,"+
			"ur.employeeno, ur.enterprisemasterid,(select enterprisename from EnterpriseMaster em "+
			" where em.enterprisemasterid = ur.enterprisemasterid)  enterprisename,ur.phoneumber,"+
			" ur.plantmasterid,(select pm.plantname from PlantMaster pm WHERE "+
			"pm.plantmasterid = ur.plantmasterid) as plantname, ur.sitemasterid, "+
			"(select sitename from SiteMaster sm where sm.sitemasterid = ur.sitemasterid) as sitename,"+
			" ur.unitmasterid,(select um.unitname from UnitMaster um WHERE um.unitmasterid = ur.unitmasterid) as unitname,"+
			" ur.unittype,ur.useremail, ur.usermode, ur.username,ur.theme,ur.imagepath,ur.rolemasterid,ur.division,ur.contactemail"+
			", workingunit1id,workingunit1name,workingunit2id,workingunit2name,isallunit "+
			" FROM UserRegister ur WHERE ur.deletedflag=?1 and  ur.status =?2 and ur.unitmasterid =?3";
			//System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,userRegister.getUnitmasterid());
			List<UserRegister> userRegisterList = new ArrayList<UserRegister>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					UserRegister newUserRegister = new UserRegister();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newUserRegister.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newUserRegister.setAreamasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setAreaname(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEmployeeno(newObject.toString());
						}else if(objectNDX == 4) {
							newUserRegister.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setEnterprisename(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPhoneumber(newObject.toString());
						}else if(objectNDX == 7) {
							newUserRegister.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setPlantname(newObject.toString());
						}else if(objectNDX == 9) {
							newUserRegister.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setSitename(newObject.toString());
						}else if(objectNDX == 11) {
							newUserRegister.setUnitmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnitname(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUnittype(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUseremail(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsermode(newObject.toString());
						}else if(objectNDX == 16) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setUsername(newObject.toString());
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setTheme(newObject.toString());
						}else if(objectNDX == 18) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setImagepath(newObject.toString());
						}else if(objectNDX == 19) {
							newUserRegister.setRolemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 20) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setDivision(newObject.toString());
						}else if(objectNDX == 21) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setContactemail(newObject.toString());
						} else if(objectNDX == 22) {
							newUserRegister.setWorkingunit1id(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 23) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit1name(newObject.toString());
						} else if(objectNDX == 24) {
							newUserRegister.setWorkingunit2id(Long.valueOf(newObject.toString()));
						} else if(objectNDX == 25) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setWorkingunit2name(newObject.toString());
						} else if(objectNDX == 26) {
							if(newObject == null){
								newObject = "";
							}
							newUserRegister.setIsallunit(newObject.toString());
						}
					}
					userRegisterList.add(newUserRegister);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return userRegisterList;			
		}else{
			List<UserRegister> userRegisterList = null;
			return userRegisterList;
		}
		
	}

	@Override
	public void deleteUserRegister(UserRegister userRegister) {
		
	}

	@Override
	public UserRegister findDetailsByMobileNo(UserRegister userRegister) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRegister findDetailsByEmail(UserRegister userRegister) {
		UserRegister newUserRegister = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserRegister> criteriaQuery = criteriaBuilder.createQuery(UserRegister.class);
		Root<UserRegister> userRegisterRoot = criteriaQuery.from(UserRegister.class);
		criteriaQuery.select(userRegisterRoot);
		Predicate predicateEmail = criteriaBuilder.equal(userRegisterRoot.get("useremail"), userRegister.getUseremail());
		Predicate predicatePass = criteriaBuilder.equal(userRegisterRoot.get("password"), userRegister.getPassword());
		Predicate predicateStatus = criteriaBuilder.equal(userRegisterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(userRegisterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateEmail, predicatePass,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<UserRegister> typedQuery = entityManager.createQuery(criteriaQuery);
		List<UserRegister> userRegisterList = typedQuery.getResultList();
		if(userRegisterList.size() != 0){
			newUserRegister = (UserRegister)userRegisterList.get(0);
		}
		return newUserRegister;
	}
	
}
