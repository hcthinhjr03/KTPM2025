package com.example.ktpm_backend.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.models.WaterServiceStatistics;
import com.example.ktpm_backend.repositories.WaterServiceRepository;
import com.example.ktpm_backend.strategies.StatisticsContext;

@Service
public class WaterServiceStatisticService {

    @Autowired
    private WaterServiceRepository waterServiceRepository;
    
    @Autowired
    private StatisticsContext statisticsContext;

    public Optional<WaterServiceStatistics> getWaterServiceStatistics(
            Integer id, Date fromDate, Date toDate, String revenueStrategy) {
        
        if (revenueStrategy == null || revenueStrategy.trim().isEmpty()) {
            revenueStrategy = "total"; 
        }
        
        Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(id);
        
        if (waterServiceOpt.isPresent()) {
            WaterService waterService = waterServiceOpt.get();
            WaterServiceStatistics statistics = new WaterServiceStatistics();
            
            statistics.setServiceId(waterService.getServiceId());
            statistics.setServiceName(waterService.getServiceName());
            statistics.setDescription(waterService.getDescription());
            statistics.setUnit(waterService.getUnit());
            statistics.setPriceRates(waterService.getPriceRates());

            statistics.setFromDate(fromDate);
            statistics.setToDate(toDate);
            statistics.setRevenueStrategy(revenueStrategy);
            
            Double value = statisticsContext.calculateRevenue(
                revenueStrategy, waterService, fromDate, toDate);
            statistics.setRevenueValue(value);
            
            return Optional.of(statistics);
        }
        
        return Optional.empty();
    }

    public Optional<WaterServiceStatistics> getWaterServiceStatistics(Integer id, Date fromDate, Date toDate) {
        return getWaterServiceStatistics(id, fromDate, toDate, "total");
    }

    public List<WaterServiceStatistics> getAllWaterServiceStatistics(
            Date fromDate, Date toDate, String revenueStrategy) {
        
        List<WaterService> waterServices = waterServiceRepository.findAll();
        List<WaterServiceStatistics> statisticsList = new ArrayList<>();
        
        for (WaterService waterService : waterServices) {
            Optional<WaterServiceStatistics> statistics = 
                getWaterServiceStatistics(waterService.getServiceId(), fromDate, toDate, revenueStrategy);
            statistics.ifPresent(statisticsList::add);
        }
        
        return statisticsList;
    }
    
    public List<WaterServiceStatistics> getAllWaterServiceStatistics(Date fromDate, Date toDate) {
        return getAllWaterServiceStatistics(fromDate, toDate, "total");
    }

    public List<Bill> getWaterServiceBills(Integer serviceId, Date fromDate, Date toDate) {
        Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(serviceId);
    
        if (waterServiceOpt.isPresent()) {
            WaterService waterService = waterServiceOpt.get();
            return statisticsContext.getBills("total", waterService, fromDate, toDate);
        }
        return List.of();
    }
}