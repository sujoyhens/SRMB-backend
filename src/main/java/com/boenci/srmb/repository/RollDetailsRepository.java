package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.RollDetails;

@Repository
public interface RollDetailsRepository extends JpaRepository<RollDetails, Long>{
    
}
