package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.UnitMaster;

@Repository
public interface UnitMasterRepository extends JpaRepository<UnitMaster, Long>{
    
}
