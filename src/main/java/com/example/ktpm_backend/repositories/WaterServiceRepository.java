package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.WaterService;

@Repository
public interface WaterServiceRepository extends JpaRepository<WaterService, Integer>{

}
