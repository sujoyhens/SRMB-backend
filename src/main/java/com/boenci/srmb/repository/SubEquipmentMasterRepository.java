package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.SubEquipmentMaster;

@Repository
public interface SubEquipmentMasterRepository extends JpaRepository<SubEquipmentMaster, Long>{
    
}
