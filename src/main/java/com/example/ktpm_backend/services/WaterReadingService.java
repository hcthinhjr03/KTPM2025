package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.WaterReading;
import com.example.ktpm_backend.models.Apartment;
import com.example.ktpm_backend.repositories.WaterReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WaterReadingService {

    @Autowired
    private WaterReadingRepository waterReadingRepository;

    public List<WaterReading> getAllWaterReadings() {
        return waterReadingRepository.findAll();
    }

    public Optional<WaterReading> getWaterReadingById(Integer id) {
        return waterReadingRepository.findById(id);
    }

    public List<WaterReading> getWaterReadingsByApartment(Apartment apartment) {
        return waterReadingRepository.findByApartmentOrderByReadingDateDesc(apartment);
    }

    public List<WaterReading> getWaterReadingsByApartmentAndDateRange(Apartment apartment, Date startDate, Date endDate) {
        return waterReadingRepository.findByApartmentAndReadingDateBetweenOrderByReadingDateDesc(apartment, startDate, endDate);
    }

    public WaterReading createWaterReading(WaterReading waterReading) {
        if (waterReading.getReadingDate() == null) {
            waterReading.setReadingDate(new Date());
        }
        
        // Ensure apartment is set
        if (waterReading.getApartment() == null) {
            throw new IllegalArgumentException("Apartment must be specified for water reading");
        }
        
        // Ensure current and previous readings are set
        if (waterReading.getCurrentReading() == null) {
            throw new IllegalArgumentException("Current reading must be provided");
        }
        
        if (waterReading.getPreviousReading() == null) {
            waterReading.setPreviousReading(0.0);
        }
        
        try {
            return waterReadingRepository.save(waterReading);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving water reading: " + e.getMessage(), e);
        }
    }

    public WaterReading updateWaterReading(WaterReading waterReading) {
        return waterReadingRepository.save(waterReading);
    }

    public void deleteWaterReading(Integer id) {
        waterReadingRepository.deleteById(id);
    }
}