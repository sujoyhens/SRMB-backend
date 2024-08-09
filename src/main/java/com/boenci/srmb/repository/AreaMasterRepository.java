package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.AreaMaster;

@Repository
public interface AreaMasterRepository extends JpaRepository<AreaMaster, Long>{
    
}
