package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.MechanicalMaintenance;

@Repository
public interface MechanicalMaintenanceRepository extends JpaRepository<MechanicalMaintenance, Long>{
    
}
