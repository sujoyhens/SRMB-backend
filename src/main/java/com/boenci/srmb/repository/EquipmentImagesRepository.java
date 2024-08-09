package com.boenci.srmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boenci.srmb.model.EquipmentImages;

@Repository
public interface EquipmentImagesRepository extends JpaRepository<EquipmentImages, Long>{
    
}
