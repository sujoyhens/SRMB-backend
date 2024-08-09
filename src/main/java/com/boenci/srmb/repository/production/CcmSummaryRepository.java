package com.boenci.srmb.repository.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.production.CcmSummary;

@Repository
public interface CcmSummaryRepository extends JpaRepository<CcmSummary, Long>{
    
}
