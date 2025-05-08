package com.example.ktpm_backend.strategies;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;

@Component
public class GrowthRateRevenueStrategy implements StatisticsStrategy {

    @Override
    public Double calculateRevenue(WaterService waterService, Date fromDate, Date toDate) {
        if (waterService.getContracts() == null) {
            return 0.0;
        }

        List<Bill> bills = getBills(waterService, fromDate, toDate);
    
        if (bills.isEmpty()) {
            return 0.0;
        }
        
        Map<YearMonth, Double> monthlyRevenue = groupBillsByMonth(bills);
        if (monthlyRevenue.size() <= 1) {
            return 0.0;
        }
    
        List<YearMonth> sortedMonths = new ArrayList<>(monthlyRevenue.keySet());
        Collections.sort(sortedMonths);
        
        double totalGrowthRate = 0.0;
        int countMonths = 0;
        
        for (int i = 1; i < sortedMonths.size(); i++) {
            YearMonth currentMonth = sortedMonths.get(i);
            YearMonth previousMonth = sortedMonths.get(i-1);
            
            double currentRevenue = monthlyRevenue.get(currentMonth);
            double previousRevenue = monthlyRevenue.get(previousMonth);
            
            if (previousRevenue > 0) {
                double growthRate = (currentRevenue - previousRevenue) / previousRevenue * 100;
                totalGrowthRate += growthRate;
                countMonths++;
            }
        }
        
        if (countMonths > 0) {
            return totalGrowthRate / countMonths;
        } else {
            return 0.0;
        }
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
        return "growth_rate"; 
    }
    
    private boolean isDateInRange(Date date, Date fromDate, Date toDate) {
        boolean isAfterFromDate = fromDate == null || date == null || !date.before(fromDate);
        boolean isBeforeToDate = toDate == null || date == null || !date.after(toDate);
        return isAfterFromDate && isBeforeToDate;
    }
    
    private static class YearMonth implements Comparable<YearMonth> {
        private final int year;
        private final int month;
        
        public YearMonth(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.year = cal.get(Calendar.YEAR);
            this.month = cal.get(Calendar.MONTH);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            YearMonth yearMonth = (YearMonth) o;
            return year == yearMonth.year && month == yearMonth.month;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(year, month);
        }
        
        @Override
        public int compareTo(YearMonth other) {
            if (this.year != other.year) {
                return Integer.compare(this.year, other.year);
            }
            return Integer.compare(this.month, other.month);
        }
    }
    
    private Map<YearMonth, Double> groupBillsByMonth(List<Bill> bills) {
        Map<YearMonth, Double> result = new HashMap<>();
        
        for (Bill bill : bills) {
            if (bill.getBillDate() != null) {
                YearMonth yearMonth = new YearMonth(bill.getBillDate());
                result.merge(yearMonth, bill.getAmount(), Double::sum);
            }
        }
        
        return result;
    }

}
