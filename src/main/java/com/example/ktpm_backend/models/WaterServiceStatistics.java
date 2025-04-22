package com.example.ktpm_backend.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WaterServiceStatistics extends WaterService {
    private Double revenue;
    private Date fromDate;
    private Date toDate;
    private Integer totalContracts;
    
    // Constructor mặc định
    public WaterServiceStatistics() {
        super();
    }
}
