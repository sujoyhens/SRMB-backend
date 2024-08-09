package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.RoleMaster;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Long>{
    
}
