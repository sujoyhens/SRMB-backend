package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.NotiFications;
import com.boenci.srmb.repository.NotiFicationsRepository;
import com.boenci.srmb.service.NotiFicationsService;

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
public class NotiFicationsServiceImpl implements NotiFicationsService {

	@Autowired
	private NotiFicationsRepository notiFicationsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public NotiFications findById(long id) {
		NotiFications newNotiFications = null;
		newNotiFications =notiFicationsRepository.findById(id).get();		
		return newNotiFications;
	}

	@Transactional
	@Override
	public NotiFications update(NotiFications notiFications) {
		long notiFicationsId = notiFications.getNotificationsid();
		NotiFications newNotiFications = null;
		if(notiFicationsRepository.existsById(notiFicationsId)){
			newNotiFications = notiFicationsRepository.save(notiFications);
			return newNotiFications;
		}else{
			return newNotiFications;
		}
	}

	@Transactional
	@Override
	public NotiFications delete(NotiFications notiFications) {
		long notiFicationsId = notiFications.getNotificationsid();
		NotiFications newNotiFications = null;
		if(notiFicationsRepository.existsById(notiFicationsId)){
			newNotiFications = notiFicationsRepository.save(notiFications);
			return newNotiFications;
		}else{
			return newNotiFications;
		}
	}


	@Override
	public NotiFications validNotiFications(NotiFications notiFications) {
		NotiFications newNotiFications = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NotiFications> criteriaQuery = criteriaBuilder.createQuery(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaQuery.from(NotiFications.class);
		criteriaQuery.select(notiFicationsRoot);
		Predicate predicateunittype = criteriaBuilder.equal(notiFicationsRoot.get("notificationsreferenceid"), notiFications.getNotificationsreferenceid());
		Predicate predicateEmail = criteriaBuilder.equal(notiFicationsRoot.get("referenceno"), notiFications.getReferenceno());
		Predicate predicateStatus = criteriaBuilder.equal(notiFicationsRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(notiFicationsRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<NotiFications> typedQuery = entityManager.createQuery(criteriaQuery);
		List<NotiFications> notiFicationsList = typedQuery.getResultList();
		if(notiFicationsList.size() != 0){
			newNotiFications = (NotiFications)notiFicationsList.get(0);
		}
		return newNotiFications;
	}

	@Override
	public NotiFications save(NotiFications notiFications) {
		NotiFications newNotiFications = null;
		newNotiFications = notiFicationsRepository.save(notiFications);
		return newNotiFications;
	}

	@Transactional
	@Override
	public String updateNotiFications(NotiFications notiFications) {
		
		NotiFications validateNotiFications = validNotiFications(notiFications);
		if(validateNotiFications == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<NotiFications> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(NotiFications.class);
			Root<NotiFications> notiFicationsRoot = criteriaUpdate.from(NotiFications.class);
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", notiFications.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(notiFicationsRoot.get("notificationsid"), notiFications.getNotificationsid()));
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
	public List<NotiFications> findAllNotiFications() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NotiFications> criteriaQuery = criteriaBuilder.createQuery(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaQuery.from(NotiFications.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(notiFicationsRoot.get("unitname")));
		List<NotiFications> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isNotiFicationsExist(NotiFications notiFications) {
		//notificationdate,notificationdetails,notificationsreferenceid,notificationtopic,referenceno,userregisterid
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NotiFications> criteriaQuery = criteriaBuilder.createQuery(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaQuery.from(NotiFications.class);
		criteriaQuery.select(notiFicationsRoot);
		Predicate predicateDate = criteriaBuilder.equal(notiFicationsRoot.get("notificationdate"), notiFications.getNotificationdate());
		Predicate predicateDetails = criteriaBuilder.equal(notiFicationsRoot.get("notificationdetails"), notiFications.getNotificationdetails());
		Predicate predicateTopic = criteriaBuilder.equal(notiFicationsRoot.get("notificationtopic"), notiFications.getNotificationtopic());
		Predicate predicateUserId = criteriaBuilder.equal(notiFicationsRoot.get("userregisterid"), notiFications.getUserregisterid());
		Predicate predicateunittype = criteriaBuilder.equal(notiFicationsRoot.get("notificationsreferenceid"), notiFications.getNotificationsreferenceid());
		Predicate predicateEmail = criteriaBuilder.equal(notiFicationsRoot.get("referenceno"), notiFications.getReferenceno());
		Predicate predicateStatus = criteriaBuilder.equal(notiFicationsRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(notiFicationsRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateDate,predicateDetails,predicateTopic,predicateUserId,predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<NotiFications> typedQuery = entityManager.createQuery(criteriaQuery);
		List<NotiFications> notiFicationsList = typedQuery.getResultList();
		if(notiFicationsList.size() != 0){
			return true;
		}else{
			return false;
		}
		
	}

	@Transactional
	@Override
	public List<NotiFications> findNotiFicationsBySearchType(NotiFications notiFications, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NotiFications> criteriaQuery = criteriaBuilder.createQuery(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaQuery.from(NotiFications.class);
		criteriaQuery.select(notiFicationsRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String queryString = "SELECT action,caller, createdat, createdby, "+
			" deletedflag,status,updatedat,updatedby,notificationsid, notificationdate, notificationdetails, "+
			"  notificationsreferenceid,notificationtime, notificationtopic,referenceno, remarks,  "+
			"  userregisterid,contactemail FROM NotiFications WHERE deletedflag = ?1 "+
			" AND status = ?2 AND userregisterid = ?3 ORDER BY notificationsid DESC";

			//System.out.println("sqlQuery::::::::::::::::::: "+ queryString);
			Query query = entityManager.createQuery(queryString);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,notiFications.getUserregisterid());
			List<NotiFications> notiFicationsList = new ArrayList<NotiFications>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					NotiFications newNotiFications = new NotiFications();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newNotiFications.setNotificationsid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdate(newObject.toString());
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdetails(newObject.toString());
						}else if(objectNDX == 11) {
							newNotiFications.setNotificationsreferenceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtime(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtopic(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setReferenceno(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setRemarks(newObject.toString());
						}else if(objectNDX == 16) {
							newNotiFications.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setContactemail(newObject.toString());
						}
					}
					notiFicationsList.add(newNotiFications);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return notiFicationsList;		
	
		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(notiFicationsRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(notiFicationsRoot.get("status"), notiFications.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<NotiFications> typedQuery = entityManager.createQuery(criteriaQuery);
			List<NotiFications> notiFicationsList = typedQuery.getResultList();
			return notiFicationsList;			
	
		}else if(searchType.equalsIgnoreCase("notificationdate")){			

			String queryString = "SELECT action,caller, createdat, createdby, "+
			" deletedflag,status,updatedat,updatedby,notificationsid, notificationdate, notificationdetails, "+
			"  notificationsreferenceid,notificationtime, notificationtopic,referenceno, remarks,  "+
			"  userregisterid,contactemail FROM NotiFications WHERE deletedflag = ?1 "+
			" AND status = ?2 AND notificationdate = ?3 AND userregisterid = ?4 ORDER BY notificationsid DESC";

			//System.out.println("sqlQuery::::::::::::::::::: "+ queryString);
			Query query = entityManager.createQuery(queryString);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,notiFications.getNotificationdate());
			query.setParameter(4,notiFications.getUserregisterid());
			List<NotiFications> notiFicationsList = new ArrayList<NotiFications>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					NotiFications newNotiFications = new NotiFications();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newNotiFications.setNotificationsid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdate(newObject.toString());
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdetails(newObject.toString());
						}else if(objectNDX == 11) {
							newNotiFications.setNotificationsreferenceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtime(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtopic(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setReferenceno(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setRemarks(newObject.toString());
						}else if(objectNDX == 16) {
							newNotiFications.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setContactemail(newObject.toString());
						}
					}
					notiFicationsList.add(newNotiFications);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return notiFicationsList;		
	
		}else if(searchType.equalsIgnoreCase("notificationdateformail")){			

			String queryString = "SELECT action,caller, createdat, createdby, "+
			" deletedflag,status,updatedat,updatedby,notificationsid, notificationdate, notificationdetails, "+
			"  notificationsreferenceid,notificationtime, notificationtopic,referenceno, remarks,  "+
			"  userregisterid,contactemail FROM NotiFications WHERE deletedflag = ?1 "+
			" AND status = ?2 AND notificationdate = ?3 ORDER BY notificationsid DESC";

			System.out.println("sqlQuery::::::::::::::::::: "+ queryString);
			Query query = entityManager.createQuery(queryString);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,notiFications.getNotificationdate());
			List<NotiFications> notiFicationsList = new ArrayList<NotiFications>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					NotiFications newNotiFications = new NotiFications();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setAction(newObject.toString());
						}else if(objectNDX == 1) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCaller(newObject.toString());
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedat(newObject.toString());
						}else if(objectNDX == 3) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setCreatedby(newObject.toString());
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setDeletedflag(newObject.toString());
						}else if(objectNDX == 5) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setStatus(newObject.toString());
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedat(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setUpdatedby(newObject.toString());
						}else if(objectNDX == 8) {
							newNotiFications.setNotificationsid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 9) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdate(newObject.toString());
						}else if(objectNDX == 10) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationdetails(newObject.toString());
						}else if(objectNDX == 11) {
							newNotiFications.setNotificationsreferenceid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtime(newObject.toString());
						}else if(objectNDX == 13) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setNotificationtopic(newObject.toString());
						}else if(objectNDX == 14) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setReferenceno(newObject.toString());
						}else if(objectNDX == 15) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setRemarks(newObject.toString());
						}else if(objectNDX == 16) {
							newNotiFications.setUserregisterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 17) {
							if(newObject == null){
								newObject = "";
							}
							newNotiFications.setContactemail(newObject.toString());
						}
					}
					notiFicationsList.add(newNotiFications);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return notiFicationsList;		
	
		}else{
			List<NotiFications> notiFicationsList = null;
		return notiFicationsList;
		}
		
	}
	@Transactional
	@Override
	public void deleteNotiFications(NotiFications notiFications) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<NotiFications> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaUpdate.from(NotiFications.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(notiFicationsRoot.get("notificationsid"), notiFications.getNotificationsid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();

	}

	@Override
	public NotiFications findDetailsByMobileNo(NotiFications notiFications) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NotiFications findDetailsByEmail(NotiFications notiFications) {
		NotiFications newNotiFications = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<NotiFications> criteriaQuery = criteriaBuilder.createQuery(NotiFications.class);
		Root<NotiFications> notiFicationsRoot = criteriaQuery.from(NotiFications.class);
		criteriaQuery.select(notiFicationsRoot);
		
		Predicate predicateStatus = criteriaBuilder.equal(notiFicationsRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(notiFicationsRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<NotiFications> typedQuery = entityManager.createQuery(criteriaQuery);
		List<NotiFications> notiFicationsList = typedQuery.getResultList();
		if(notiFicationsList.size() != 0){
			newNotiFications = (NotiFications)notiFicationsList.get(0);
		}
		return newNotiFications;
	}

	@Transactional
	@Override
	public String saveNotification(String createdAt, String createdBy, Long userRegisterId, String notificationDate,
			String notificationTime, Long referenceId, String referenceNo, String notificationTopic,
			String description,String contactEmail) {

		NotiFications notiFications =  new NotiFications();
		notiFications.setCreatedat(createdAt);
		notiFications.setCreatedby(createdBy);
		notiFications.setStatus("active");
		notiFications.setDeletedflag("no");
		notiFications.setAction("save");
		notiFications.setUserregisterid(userRegisterId);
		notiFications.setNotificationdate(notificationDate);
		notiFications.setNotificationtime(notificationTime);
		notiFications.setNotificationsreferenceid(referenceId);
		notiFications.setReferenceno(referenceNo);
		notiFications.setNotificationtopic(notificationTopic);
		notiFications.setNotificationdetails(description);
		notiFications.setContactemail(contactEmail);
		Boolean response = isNotiFicationsExist(notiFications);
		if(!response){
			NotiFications newNotiFications = notiFicationsRepository.save(notiFications);
			System.out.println("NotiFications Added");			
		}else{
			System.out.println("NotiFications Not Added");	
		}
		return null;
	}
	
}
