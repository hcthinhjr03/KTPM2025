package com.example.ktpm_backend.services;

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

    // Get statistics for a specific water service
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
            
            // Set statistics date range
            statistics.setFromDate(fromDate);
            statistics.setToDate(toDate);
            
            // Calculate statistics
            double revenue = calculateRevenue(waterService, fromDate, toDate);
            statistics.setRevenue(revenue);
            
            // Count total contracts in the period
            int totalContracts = countContractsInPeriod(waterService, fromDate, toDate);
            statistics.setTotalContracts(totalContracts);
            
            return Optional.of(statistics);
        }
        
        return Optional.empty();
    }

    // Get all water services with statistics
    public List<WaterServiceStatistics> getAllWaterServiceStatistics(Date fromDate, Date toDate) {
        List<WaterService> waterServices = waterServiceRepository.findAll();
        List<WaterServiceStatistics> statisticsList = new ArrayList<>();
        
        for (WaterService waterService : waterServices) {
            Optional<WaterServiceStatistics> statistics = getWaterServiceStatistics(waterService.getServiceId(), fromDate, toDate);
            statistics.ifPresent(statisticsList::add);
        }
        
        return statisticsList;
    }
    
    // Helper method to calculate revenue for a water service in a given period
    private double calculateRevenue(WaterService waterService, Date fromDate, Date toDate) {
        // Implement logic to calculate revenue from bills in the period
        // This is a placeholder implementation
        if (waterService.getContracts() == null) {
            return 0.0;
        }
        
        return waterService.getContracts().stream()
            .filter(contract -> isContractActiveInPeriod(contract, fromDate, toDate))
            .flatMap(contract -> contract.getBills().stream())
            .filter(bill -> isDateInRange(bill.getBillDate(), fromDate, toDate))
            .mapToDouble(bill -> bill.getAmount())
            .sum();
    }
    
    // Helper method to count contracts in a given period
    private int countContractsInPeriod(WaterService waterService, Date fromDate, Date toDate) {
        // Implement logic to count active contracts in the period
        // This is a placeholder implementation
        if (waterService.getContracts() == null) {
            return 0;
        }
        
        return (int) waterService.getContracts().stream()
            .filter(contract -> isContractActiveInPeriod(contract, fromDate, toDate))
            .count();
    }
    
    // Helper method to check if a contract is active in a given period
    private boolean isContractActiveInPeriod(Contract contract, Date fromDate, Date toDate) {
        // Contract starts before or at the end date of the period
        boolean startsBeforeEndOfPeriod = toDate == null || contract.getStartDate() == null || 
                                         !contract.getStartDate().after(toDate);
        
        // Contract ends after or at the start date of the period
        boolean endsAfterStartOfPeriod = fromDate == null || contract.getEndDate() == null || 
                                         !contract.getEndDate().before(fromDate);
        
        // Contract is active if it has an overlap with the period
        return startsBeforeEndOfPeriod && endsAfterStartOfPeriod;
    }
    
    // Helper method to check if a date is within a range
    private boolean isDateInRange(Date date, Date fromDate, Date toDate) {
        boolean isAfterFromDate = fromDate == null || date == null || !date.before(fromDate);
        boolean isBeforeToDate = toDate == null || date == null || !date.after(toDate);
        return isAfterFromDate && isBeforeToDate;
    }
}