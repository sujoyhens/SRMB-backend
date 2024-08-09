package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.NotiFications;

@Repository
public interface NotiFicationsRepository extends JpaRepository<NotiFications, Long>{
    
}
