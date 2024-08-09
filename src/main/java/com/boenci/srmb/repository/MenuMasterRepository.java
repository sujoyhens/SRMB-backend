package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.MenuMaster;

@Repository
public interface MenuMasterRepository extends JpaRepository<MenuMaster, Long>{
    
}
