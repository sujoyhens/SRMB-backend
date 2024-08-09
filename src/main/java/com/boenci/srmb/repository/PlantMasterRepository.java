package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.PlantMaster;

@Repository
public interface PlantMasterRepository extends JpaRepository<PlantMaster, Long>{
    
}
