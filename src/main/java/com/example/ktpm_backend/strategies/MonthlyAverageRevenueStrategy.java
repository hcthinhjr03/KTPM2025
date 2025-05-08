package com.example.ktpm_backend.strategies;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;

@Component
public class MonthlyAverageRevenueStrategy implements StatisticsStrategy {

    @Override
    public Double calculateRevenue(WaterService waterService, Date fromDate, Date toDate) {
        if (waterService.getContracts() == null) {
            return 0.0;
        }
        
        List<Bill> bills = getBills(waterService, fromDate, toDate);
        double totalRevenue = bills.stream()
                .mapToDouble(Bill::getAmount)
                .sum();
        
        int months = countActualMonths(fromDate, toDate);
        if (months == 0) {
            return totalRevenue;
        }
        
        return totalRevenue / months;
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
        return "monthly_average"; 
    }
    
    private boolean isDateInRange(Date date, Date fromDate, Date toDate) {
        boolean isAfterFromDate = fromDate == null || date == null || !date.before(fromDate);
        boolean isBeforeToDate = toDate == null || date == null || !date.after(toDate);
        return isAfterFromDate && isBeforeToDate;
    }
    
    private int countActualMonths(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return 1; 
        }
        
        LocalDate startDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        Set<YearMonth> months = new HashSet<>();
        months.add(YearMonth.from(startDate));
        months.add(YearMonth.from(endDate));
        LocalDate current = startDate.withDayOfMonth(1).plusMonths(1);
        
        while (!current.isAfter(endDate) && 
               !YearMonth.from(current).equals(YearMonth.from(endDate))) {
            months.add(YearMonth.from(current));
            current = current.plusMonths(1);
        }
        
        return months.size();
    }
}