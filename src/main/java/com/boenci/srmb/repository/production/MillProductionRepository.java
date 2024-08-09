package com.boenci.srmb.repository.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.production.MillProduction;

@Repository
public interface MillProductionRepository extends JpaRepository<MillProduction, Long>{
    
}
