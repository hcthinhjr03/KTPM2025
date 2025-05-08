package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.repositories.WaterServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WaterServiceService {

    @Autowired
    private WaterServiceRepository waterServiceRepository;

    public List<WaterService> getAllWaterServices() {
        return waterServiceRepository.findAll();
    }

    public Optional<WaterService> getWaterServiceById(Integer id) {
        return waterServiceRepository.findById(id);
    }

    public WaterService createWaterService(WaterService waterService) {
        if (waterService.getPriceRates() != null) {
            waterService.getPriceRates().forEach(priceRate -> priceRate.setWaterService(waterService));
        }
        return waterServiceRepository.save(waterService);
    }

    public WaterService updateWaterService(WaterService waterService) {
        if (waterService.getPriceRates() != null) {
            waterService.getPriceRates().forEach(priceRate -> priceRate.setWaterService(waterService));
        }
        return waterServiceRepository.save(waterService);
    }

    public void deleteWaterService(Integer id) {
        waterServiceRepository.deleteById(id);
    }

    
}