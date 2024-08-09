package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.UserRegister;

@Repository
public interface UserRegisterRepository extends JpaRepository<UserRegister, Long>{
    
}
