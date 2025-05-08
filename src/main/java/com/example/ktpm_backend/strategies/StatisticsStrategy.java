package com.example.ktpm_backend.strategies;

import java.util.Date;
import java.util.List;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;

public interface StatisticsStrategy {
    Double calculateRevenue(WaterService waterService, Date fromDate, Date toDate);
    
    List<Bill> getBills(WaterService waterService, Date fromDate, Date toDate);
    
    String getStrategyName();
}
