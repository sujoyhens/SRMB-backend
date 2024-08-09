package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.EnterpriseMaster;

@Repository
public interface EnterpriseMasterRepository extends JpaRepository<EnterpriseMaster, Long>{
    
}
