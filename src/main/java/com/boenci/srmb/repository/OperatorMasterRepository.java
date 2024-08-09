package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.OperatorMaster;

@Repository
public interface OperatorMasterRepository extends JpaRepository<OperatorMaster, Long>{
    
}
