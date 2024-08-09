package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.EquipmentMaster;

@Repository
public interface EquipmentMasterRepository extends JpaRepository<EquipmentMaster, Long>{
    
}
