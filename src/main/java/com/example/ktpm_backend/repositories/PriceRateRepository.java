package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.PriceRate;
import com.example.ktpm_backend.models.WaterService;

import java.util.List;

@Repository
public interface PriceRateRepository extends JpaRepository<PriceRate, Integer> {
    List<PriceRate> findByWaterService(WaterService waterService);
}