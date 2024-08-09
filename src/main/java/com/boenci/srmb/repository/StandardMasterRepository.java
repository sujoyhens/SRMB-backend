package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.StandardMaster;

@Repository
public interface StandardMasterRepository extends JpaRepository<StandardMaster, Long>{
    
}
