package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.SiteMaster;

@Repository
public interface SiteMasterRepository extends JpaRepository<SiteMaster, Long>{
    
}
