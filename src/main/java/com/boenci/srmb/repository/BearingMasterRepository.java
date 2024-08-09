package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.BearingMaster;

@Repository
public interface BearingMasterRepository extends JpaRepository<BearingMaster, Long>{
    
}
