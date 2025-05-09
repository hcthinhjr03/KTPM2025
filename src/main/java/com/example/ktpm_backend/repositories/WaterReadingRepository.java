package com.example.ktpm_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ktpm_backend.models.WaterReading;
import com.example.ktpm_backend.models.Apartment;

import java.util.Date;
import java.util.List;

@Repository
public interface WaterReadingRepository extends JpaRepository<WaterReading, Integer> {
    
    List<WaterReading> findByApartmentOrderByReadingDateDesc(Apartment apartment);
    
    List<WaterReading> findByApartmentAndReadingDateBetweenOrderByReadingDateDesc(
        Apartment apartment, Date startDate, Date endDate);
}