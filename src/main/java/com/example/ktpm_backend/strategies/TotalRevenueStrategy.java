package com.example.ktpm_backend.strategies;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;

@Component
public class TotalRevenueStrategy implements StatisticsStrategy {

    @Override
    public Double calculateRevenue(WaterService waterService, Date fromDate, Date toDate) {
        if (waterService.getContracts() == null) {
            return 0.0;
        }
        
        return waterService.getContracts().stream()
            .flatMap(contract -> contract.getBills().stream())
            .filter(bill -> isDateInRange(bill.getBillDate(), fromDate, toDate))
            .mapToDouble(bill -> bill.getAmount())
            .sum();
    }
    
    @Override
    public List<Bill> getBills(WaterService waterService, Date fromDate, Date toDate) {
        if (waterService.getContracts() == null) {
            return List.of();
        }
        
        return waterService.getContracts().stream()
            .flatMap(contract -> contract.getBills().stream())
            .filter(bill -> isDateInRange(bill.getBillDate(), fromDate, toDate))
            .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "total"; 
    }
    
    private boolean isDateInRange(Date date, Date fromDate, Date toDate) {
        boolean isAfterFromDate = fromDate == null || date == null || !date.before(fromDate);
        boolean isBeforeToDate = toDate == null || date == null || !date.after(toDate);
        return isAfterFromDate && isBeforeToDate;
    }

}
