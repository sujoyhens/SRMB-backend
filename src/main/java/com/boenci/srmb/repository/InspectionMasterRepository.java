package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.InspectionMaster;

@Repository
public interface InspectionMasterRepository extends JpaRepository<InspectionMaster, Long>{
    
}
