package com.example.ktpm_backend.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WaterServiceStatistics extends WaterService {
    private Double revenueValue;      
    private String revenueStrategy;   
    private Date fromDate;
    private Date toDate;
    
    public WaterServiceStatistics() {
        super();
    }
}
