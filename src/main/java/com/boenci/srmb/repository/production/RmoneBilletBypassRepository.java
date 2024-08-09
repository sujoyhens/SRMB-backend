package com.boenci.srmb.repository.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.production.RmoneBilletBypass;

@Repository
public interface RmoneBilletBypassRepository extends JpaRepository<RmoneBilletBypass, Long>{
    
}
