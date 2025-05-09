package com.example.ktpm_backend.services;

import com.example.ktpm_backend.models.PriceRate;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.repositories.PriceRateRepository;
import com.example.ktpm_backend.repositories.WaterServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaterServiceService {

    @Autowired
    private WaterServiceRepository waterServiceRepository;
    
    @Autowired
    private PriceRateRepository priceRateRepository;

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

    @Transactional
    public WaterService updateWaterService(WaterService waterService) {
        Integer serviceId = waterService.getServiceId();
        
        Optional<WaterService> existingWaterServiceOpt = waterServiceRepository.findById(serviceId);
        
        if (!existingWaterServiceOpt.isPresent()) {
            return waterServiceRepository.save(waterService);
        }
        
        WaterService existingWaterService = existingWaterServiceOpt.get();
        List<PriceRate> existingPriceRates = existingWaterService.getPriceRates();
        List<PriceRate> newPriceRates = waterService.getPriceRates();
        
        final List<PriceRate> finalNewPriceRates = newPriceRates != null 
            ? newPriceRates 
            : new ArrayList<>();
        
        for (PriceRate priceRate : finalNewPriceRates) {
            priceRate.setWaterService(waterService);
        }
        
        if (existingPriceRates != null) {
            List<PriceRate> pricesToRemove = existingPriceRates.stream()
                    .filter(existing -> !isPriceRateInList(existing, finalNewPriceRates))
                    .collect(Collectors.toList());
            
            for (PriceRate priceToRemove : pricesToRemove) {
                priceRateRepository.deleteById(priceToRemove.getRateId());
            }
        }
        
        return waterServiceRepository.save(waterService);
    }
    
    private boolean isPriceRateInList(PriceRate priceRate, List<PriceRate> priceRates) {
        if (priceRate.getRateId() == null) {
            return false;
        }
        
        return priceRates.stream()
                .anyMatch(pr -> pr.getRateId() != null && pr.getRateId().equals(priceRate.getRateId()));
    }

    public void deleteWaterService(Integer id) {
        waterServiceRepository.deleteById(id);
    }
}