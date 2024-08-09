package com.boenci.srmb.serviceImpl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boenci.srmb.controller.UtilityRestController;
import com.boenci.srmb.model.ProductMaster;
import com.boenci.srmb.repository.ProductMasterRepository;
import com.boenci.srmb.service.ProductMasterService;

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
public class ProductMasterServiceImpl implements ProductMasterService {

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProductMaster findById(long id) {
		ProductMaster newProductMaster = null;
		newProductMaster =productMasterRepository.findById(id).get();		
		return newProductMaster;
	}

	@Transactional
	@Override
	public ProductMaster update(ProductMaster productMaster) {
		long productMasterId = productMaster.getProductmasterid();
		ProductMaster newProductMaster = null;
		if(productMasterRepository.existsById(productMasterId)){
			newProductMaster = productMasterRepository.save(productMaster);
			return newProductMaster;
		}else{
			return newProductMaster;
		}
	}

	@Transactional
	@Override
	public ProductMaster delete(ProductMaster productMaster) {
		long productMasterId = productMaster.getProductmasterid();
		ProductMaster newProductMaster = null;
		if(productMasterRepository.existsById(productMasterId)){
			newProductMaster = productMasterRepository.save(productMaster);
			return newProductMaster;
		}else{
			return newProductMaster;
		}
	}


	@Override
	public ProductMaster validProductMaster(ProductMaster productMaster) {
		ProductMaster newProductMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductMaster> criteriaQuery = criteriaBuilder.createQuery(ProductMaster.class);
		Root<ProductMaster> productMasterRoot = criteriaQuery.from(ProductMaster.class);
		criteriaQuery.select(productMasterRoot);
		Predicate predicateunittype = criteriaBuilder.equal(productMasterRoot.get("productdescription"), productMaster.getProductdescription());
		Predicate predicateEmail = criteriaBuilder.equal(productMasterRoot.get("linenumber"), productMaster.getLinenumber());
		Predicate predicateStatus = criteriaBuilder.equal(productMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateunittype,predicateEmail,predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductMaster> productMasterList = typedQuery.getResultList();
		if(productMasterList.size() != 0){
			newProductMaster = (ProductMaster)productMasterList.get(0);
		}
		return newProductMaster;
	}

	@Override
	public ProductMaster save(ProductMaster productMaster) {
		ProductMaster newProductMaster = null;
		newProductMaster = productMasterRepository.save(productMaster);
		return newProductMaster;
	}

	@Transactional
	@Override
	public String updateProductMaster(ProductMaster productMaster) {
		
		ProductMaster validateProductMaster = validProductMaster(productMaster);
		if(validateProductMaster == null){
			CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			CriteriaUpdate<ProductMaster> criteriaUpdate = criteriaBuilder.
			createCriteriaUpdate(ProductMaster.class);
			Root<ProductMaster> productMasterRoot = criteriaUpdate.from(ProductMaster.class);
			criteriaUpdate.set("productdescription", productMaster.getProductdescription());
			criteriaUpdate.set("linenumber", productMaster.getLinenumber());
			criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
			criteriaUpdate.set("status", productMaster.getStatus());
			criteriaUpdate.set("action", "update");
			criteriaUpdate.where(criteriaBuilder.equal(productMasterRoot.get("productmasterid"), productMaster.getProductmasterid()));
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
	public List<ProductMaster> findAllProductMaster() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductMaster> criteriaQuery = criteriaBuilder.createQuery(ProductMaster.class);
		Root<ProductMaster> productMasterRoot = criteriaQuery.from(ProductMaster.class);
		criteriaQuery.orderBy(criteriaBuilder.desc(productMasterRoot.get("productdescription")));
		List<ProductMaster> countries = entityManager.createQuery(criteriaQuery).getResultList();
		return countries;
	}

	@Override
	public boolean isProductMasterExist(ProductMaster productMaster) {
		
		return true;
	}

	@Override
	public List<ProductMaster> findProductMasterBySearchType(ProductMaster productMaster, String searchType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductMaster> criteriaQuery = criteriaBuilder.createQuery(ProductMaster.class);
		Root<ProductMaster> productMasterRoot = criteriaQuery.from(ProductMaster.class);
		criteriaQuery.select(productMasterRoot);
		Predicate predicateAnd = null;
		if(searchType.equalsIgnoreCase("all")){
			String strSql = "SELECT um.productmasterid,um.enterprisemasterid,(select enterprisename "+
			" from EnterpriseMaster em where em.enterprisemasterid = um.enterprisemasterid)  enterprisename,"+
			" um.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = um.plantmasterid) "+
			" as plantname, um.sitemasterid, (select sitename from SiteMaster sm where "+
			"sm.sitemasterid = um.sitemasterid) as sitename,  um.productdescription, um.linenumber, um.linespeed, "+
			"um.sectionweight,um.size,um.itemtype FROM ProductMaster um WHERE um.deletedflag = ?1 and um.status = ?2";
			System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			List<ProductMaster> productMasterList = new ArrayList<ProductMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductMaster newProductMaster = new ProductMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newProductMaster.setProductmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newProductMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newProductMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newProductMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setProductdescription(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setLinenumber(newObject.toString());
						}else if(objectNDX == 9) {
							newProductMaster.setLinespeed(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 10) {
							newProductMaster.setSectionweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setSize(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setItemtype(newObject.toString());
						}
					}
					productMasterList.add(newProductMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productMasterList;

		}else if(searchType.equalsIgnoreCase("status")){
			Predicate predicateDelete = criteriaBuilder.equal(productMasterRoot.get("deletedflag"), "no");
			Predicate predicateStatus = criteriaBuilder.equal(productMasterRoot.get("status"), productMaster.getStatus());
			predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
			criteriaQuery.where(predicateAnd);
			TypedQuery<ProductMaster> typedQuery = entityManager.createQuery(criteriaQuery);
			List<ProductMaster> productMasterList = typedQuery.getResultList();
			return productMasterList;
			
		}else if(searchType.equalsIgnoreCase("productdescription")){
			String strSql = "SELECT um.productmasterid,um.enterprisemasterid,(select enterprisename "+
			" from EnterpriseMaster em where em.enterprisemasterid = um.enterprisemasterid)  enterprisename,"+
			" um.plantmasterid,(select pm.plantname from PlantMaster pm WHERE pm.plantmasterid = um.plantmasterid) "+
			" as plantname, um.sitemasterid, (select sitename from SiteMaster sm where "+
			"sm.sitemasterid = um.sitemasterid) as sitename,  um.productdescription, um.linenumber, um.linespeed, "+
			"um.sectionweight,um.size,um.itemtype FROM ProductMaster um WHERE um.deletedflag = ?1 and um.status = ?2 and um.productdescription = ?3";
			System.out.println(strSql);
			Query query = entityManager.createQuery(strSql);
			query.setParameter(1,"no");
			query.setParameter(2,"active");
			query.setParameter(3,productMaster.getProductdescription());
			List<ProductMaster> productMasterList = new ArrayList<ProductMaster>();
			try {
				for(int ndx = 0 ; ndx<query.getResultList().size() ; ndx++) {
					Object[] object = (Object[]) query.getResultList().get(ndx);
					ProductMaster newProductMaster = new ProductMaster();
					for(int objectNDX = 0 ; objectNDX<object.length; objectNDX++) {
						Object newObject = Array.get(object, objectNDX);
						if(objectNDX == 0) {
							newProductMaster.setProductmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 1) {
							newProductMaster.setEnterprisemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 2) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setEnterprisename(newObject.toString());
						}else if(objectNDX == 3) {
							newProductMaster.setPlantmasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 4) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setPlantname(newObject.toString());
						}else if(objectNDX == 5) {
							newProductMaster.setSitemasterid(Long.valueOf(newObject.toString()));
						}else if(objectNDX == 6) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setSitename(newObject.toString());
						}else if(objectNDX == 7) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setProductdescription(newObject.toString());
						}else if(objectNDX == 8) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setLinenumber(newObject.toString());
						}else if(objectNDX == 9) {
							newProductMaster.setLinespeed(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 10) {
							newProductMaster.setSectionweight(Double.valueOf(newObject.toString()));
						}else if(objectNDX == 11) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setSize(newObject.toString());
						}else if(objectNDX == 12) {
							if(newObject == null){
								newObject = "";
							}
							newProductMaster.setItemtype(newObject.toString());
						}
					}
					productMasterList.add(newProductMaster);
				}
			}catch(org.hibernate.ObjectNotFoundException ex) {
				System.out.println("ex" + ex);
			}
			return productMasterList;
		}else{
			List<ProductMaster> productMasterList = null;
		return productMasterList;
		}
		
	}
	@Transactional
	@Override
	public void deleteProductMaster(ProductMaster productMaster) {

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaUpdate<ProductMaster> criteriaUpdate = criteriaBuilder.
		createCriteriaUpdate(ProductMaster.class);
		Root<ProductMaster> productMasterRoot = criteriaUpdate.from(ProductMaster.class);
		criteriaUpdate.set("deletedflag", "yes");
		criteriaUpdate.set("updatedat", UtilityRestController.getCurrentDateTime());
		criteriaUpdate.set("status", "inactive");
		criteriaUpdate.set("action", "delete");
		criteriaUpdate.where(criteriaBuilder.equal(productMasterRoot.get("productmasterid"), productMaster.getProductmasterid()));
		this.entityManager.createQuery(criteriaUpdate).executeUpdate();

	}

	@Override
	public ProductMaster findDetailsByMobileNo(ProductMaster productMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductMaster findDetailsByEmail(ProductMaster productMaster) {
		ProductMaster newProductMaster = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductMaster> criteriaQuery = criteriaBuilder.createQuery(ProductMaster.class);
		Root<ProductMaster> productMasterRoot = criteriaQuery.from(ProductMaster.class);
		criteriaQuery.select(productMasterRoot);
		Predicate predicateStatus = criteriaBuilder.equal(productMasterRoot.get("status"), "active");
		Predicate predicateDelete = criteriaBuilder.equal(productMasterRoot.get("deletedflag"), "no");
		Predicate predicateAnd = criteriaBuilder.and(predicateStatus,predicateDelete);
		criteriaQuery.where(predicateAnd);
		TypedQuery<ProductMaster> typedQuery = entityManager.createQuery(criteriaQuery);
		List<ProductMaster> productMasterList = typedQuery.getResultList();
		if(productMasterList.size() != 0){
			newProductMaster = (ProductMaster)productMasterList.get(0);
		}
		return newProductMaster;
	}
	
}
