package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.Contract;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.models.WaterServiceStatistics;
import com.example.ktpm_backend.repositories.WaterServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaterServiceService {

    @Autowired
    private WaterServiceRepository waterServiceRepository;

    // Get all water services
    public List<WaterService> getAllWaterServices() {
        return waterServiceRepository.findAll();
    }

    // Get water service by id
    public Optional<WaterService> getWaterServiceById(Integer id) {
        return waterServiceRepository.findById(id);
    }

    // Create new water service
    public WaterService createWaterService(WaterService waterService) {
        // Thiết lập mối quan hệ hai chiều giữa WaterService và PriceRate
        if (waterService.getPriceRates() != null) {
            waterService.getPriceRates().forEach(priceRate -> priceRate.setWaterService(waterService));
        }
        return waterServiceRepository.save(waterService);
    }

    // Update water service
    public WaterService updateWaterService(WaterService waterService) {
        // Thiết lập mối quan hệ hai chiều giữa WaterService và PriceRate
        if (waterService.getPriceRates() != null) {
            waterService.getPriceRates().forEach(priceRate -> priceRate.setWaterService(waterService));
        }
        return waterServiceRepository.save(waterService);
    }

    // Delete water service
    public void deleteWaterService(Integer id) {
        waterServiceRepository.deleteById(id);
    }

    
}