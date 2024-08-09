package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.RoleMenu;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long>{
    
}
