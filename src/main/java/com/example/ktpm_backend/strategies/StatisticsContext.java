package com.example.ktpm_backend.strategies;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ktpm_backend.models.Bill;
import com.example.ktpm_backend.models.WaterService;

@Component
public class StatisticsContext {
    
    private final Map<String, StatisticsStrategy> strategies;
    
    public StatisticsContext(List<StatisticsStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                StatisticsStrategy::getStrategyName,
                Function.identity()
            ));
    }
    
    public Double calculateRevenue(String strategyName, WaterService waterService, Date fromDate, Date toDate) {
        StatisticsStrategy strategy = getStrategy(strategyName);
        return strategy.calculateRevenue(waterService, fromDate, toDate);
    }
    
    public List<Bill> getBills(String strategyName, WaterService waterService, Date fromDate, Date toDate) {
        StatisticsStrategy strategy = getStrategy(strategyName);
        return strategy.getBills(waterService, fromDate, toDate);
    }
    
    private StatisticsStrategy getStrategy(String strategyName) {
        StatisticsStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            strategy = strategies.get("total");
        }
        return strategy;
    }
}
