package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long>{
    
}
