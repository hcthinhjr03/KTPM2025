package com.example.ktpm_backend.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;
import com.example.ktpm_backend.models.WaterServiceStatistics;
import com.example.ktpm_backend.repositories.WaterServiceRepository;

@Service
public class WaterServiceStatisticService {

    @Autowired
    private WaterServiceRepository waterServiceRepository;

    // thong ke chi tiet
    public Optional<WaterServiceStatistics> getWaterServiceStatistics(Integer id, Date fromDate, Date toDate) {
        Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(id);
        
        if (waterServiceOpt.isPresent()) {
            WaterService waterService = waterServiceOpt.get();
            WaterServiceStatistics statistics = new WaterServiceStatistics();
            
            // Copy base properties from WaterService
            statistics.setServiceId(waterService.getServiceId());
            statistics.setServiceName(waterService.getServiceName());
            statistics.setDescription(waterService.getDescription());
            statistics.setUnit(waterService.getUnit());
            statistics.setPriceRates(waterService.getPriceRates());
            
            // Set statistics date range
            statistics.setFromDate(fromDate);
            statistics.setToDate(toDate);
            
            // Calculate statistics
            double revenue = calculateRevenue(waterService, fromDate, toDate);
            statistics.setRevenue(revenue);
            
            
            return Optional.of(statistics);
        }
        
        return Optional.empty();
    }

    // thong ke tat ca
    public List<WaterServiceStatistics> getAllWaterServiceStatistics(Date fromDate, Date toDate) {
        List<WaterService> waterServices = waterServiceRepository.findAll();
        List<WaterServiceStatistics> statisticsList = new ArrayList<>();
        
        for (WaterService waterService : waterServices) {
            Optional<WaterServiceStatistics> statistics = getWaterServiceStatistics(waterService.getServiceId(), fromDate, toDate);
            statistics.ifPresent(statisticsList::add);
        }
        
        return statisticsList;
    }
    
    //tinh doanh thu
    private double calculateRevenue(WaterService waterService, Date fromDate, Date toDate) {
        if (waterService.getContracts() == null) {
            return 0.0;
        }
        
        return waterService.getContracts().stream()
            .flatMap(contract -> contract.getBills().stream())
            .filter(bill -> isDateInRange(bill.getBillDate(), fromDate, toDate))
            .mapToDouble(bill -> bill.getAmount())
            .sum();
    }
    
    
    // validate ngay 
    private boolean isDateInRange(Date date, Date fromDate, Date toDate) {
        boolean isAfterFromDate = fromDate == null || date == null || !date.before(fromDate);
        boolean isBeforeToDate = toDate == null || date == null || !date.after(toDate);
        return isAfterFromDate && isBeforeToDate;
    }

    public List<Bill> getWaterServiceBills(Integer serviceId, Date fromDate, Date toDate) {
        Optional<WaterService> waterServiceOpt = waterServiceRepository.findById(serviceId);
    
        if (waterServiceOpt.isPresent()) {
            WaterService waterService = waterServiceOpt.get();
        
            if (waterService.getContracts() == null) {
                return List.of();
            }
        
            return waterService.getContracts().stream()
                .flatMap(contract -> contract.getBills().stream())
                .filter(bill -> isDateInRange(bill.getBillDate(), fromDate, toDate))
                .collect(Collectors.toList());
        }
        return List.of();
    }
}
